
const API_BASE = "http://localhost:8080/api";

const userEmail = localStorage.getItem("userEmail");
if (!userEmail) {
  window.location.href = "index.html";
}

const groupId = new URLSearchParams(window.location.search).get("id");
let loggedInUserId = null;

const groupTitle = document.getElementById("groupTitle");
const memberList = document.getElementById("memberList");
const expenseList = document.getElementById("expenseList");
const balanceList = document.getElementById("balanceList");

async function fetchUserId() {
  const res = await fetch(`${API_BASE}/users/email/${userEmail}`);
  const user = await res.json();
  loggedInUserId = user.id;
  return user;
}

async function loadGroupDetails() {
  try {
    if (!loggedInUserId) {
      await fetchUserId();
    }

    const res = await fetch(`${API_BASE}/groups/${groupId}/details?userId=${loggedInUserId}`);
    if (!res.ok) throw new Error("Failed to fetch group data");

    const group = await res.json();

    groupTitle.textContent = `Group: ${group.name}`;

    memberList.innerHTML = "";
    group.members.forEach(m => {
      const li = document.createElement("li");
      li.textContent = `${m.name}`;
      memberList.appendChild(li);
    });

    expenseList.innerHTML = "";
    if (group.expenses.length === 0) {
      expenseList.innerHTML = "<li>No expenses yet.</li>";
    } else {
      group.expenses.forEach(exp => {
        const li = document.createElement("li");
        li.textContent = `${exp.description}: ₹${exp.amount} paid by ${exp.payerName}`;
        expenseList.appendChild(li);
      });
    }

    balanceList.innerHTML = "";

    //  Hardcoded check: if user has settled, always show settled message
    const settledFlag = localStorage.getItem(`settled_${groupId}_${loggedInUserId}`) === "true";
    if (settledFlag) {
      const li = document.createElement("li");
      li.textContent = "✅ You are all settled up!";
      balanceList.appendChild(li);
    } else {
      if (group.balances.length === 0) {
        balanceList.innerHTML = "<li>No balances yet.</li>";
      } else {
        group.balances.forEach(msg => {
          const li = document.createElement("li");
          li.textContent = msg;
          balanceList.appendChild(li);
        });
      }
    }

  } catch (err) {
    console.error(err);
    alert("⚠️ Failed to load group data");
  }
}

document.getElementById("logoutBtn").addEventListener("click", () => {
  if (confirm("Logout?")) {
    localStorage.clear();
    window.location.href = "index.html";
  }
});

const avatar = document.getElementById("avatarBtn");
const dropdown = document.getElementById("dropdownMenu");
avatar.addEventListener("click", () => {
  dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
});
window.addEventListener("click", (e) => {
  if (!avatar.contains(e.target) && !dropdown.contains(e.target)) {
    dropdown.style.display = "none";
  }
});

const profileBtn = document.getElementById("profileBtn");
const profileModal = document.getElementById("profileModal");
const closeProfileModal = document.getElementById("closeProfileModal");

profileBtn.addEventListener("click", async () => {
  try {
    const res = await fetch(`${API_BASE}/users/email/${userEmail}`);
    const user = await res.json();
    document.getElementById("profileName").textContent = user.name;
    document.getElementById("profileEmail").textContent = user.email;
    profileModal.style.display = "block";
  } catch {
    alert("⚠️ Error loading profile info.");
  }
  dropdown.style.display = "none";
});

closeProfileModal?.addEventListener("click", () => {
  profileModal.style.display = "none";
});
window.addEventListener("click", (e) => {
  if (e.target === profileModal) {
    profileModal.style.display = "none";
  }
});

fetchUserId().then(() => loadGroupDetails());

// --- Settle Modal Logic ---
const settleBtn = document.getElementById("settleBtn");
const settleModal = document.getElementById("settleModal");
const closeSettleModal = document.getElementById("closeSettleModal");
const settleWithDropdown = document.getElementById("settleWith");

settleBtn.addEventListener("click", async () => {
  settleModal.style.display = "block";

  try {
    const res = await fetch(`${API_BASE}/groups/${groupId}/details?userId=${loggedInUserId}`);
    const group = await res.json();

    settleWithDropdown.innerHTML = "";
    const balances = group.balances;

    balances.forEach(msg => {
      const match = msg.match(/^You owe (.+) ₹([\d.]+)/);
      if (match) {
        const name = match[1];
        const user = group.members.find(m => m.name === name);
        if (user) {
          const option = document.createElement("option");
          option.value = user.id;
          option.textContent = `${name} (₹${match[2]})`;
          settleWithDropdown.appendChild(option);
        }
      }
    });

  } catch (err) {
    alert("⚠️ Could not load members.");
  }
});

closeSettleModal.addEventListener("click", () => {
  settleModal.style.display = "none";
});
window.addEventListener("click", (e) => {
  if (e.target === settleModal) {
    settleModal.style.display = "none";
  }
});

document.getElementById("settleForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const payerId = loggedInUserId;
  const payeeId = settleWithDropdown.value;

  try {
    const res = await fetch(`${API_BASE}/settle`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ payerId, payeeId, groupId }),
    });

    if (res.ok) {
      alert("✅ Settlement recorded!");
      // ✅ Store settled flag in localStorage
      localStorage.setItem(`settled_${groupId}_${loggedInUserId}`, "true");
      settleModal.style.display = "none";
      location.reload(); // reload to show "settled up"
    } else {
      alert("❌ Failed to settle up.");
    }
  } catch (err) {
    console.error(err);
    alert("⚠️ Something went wrong.");
  }
});

// --- Transaction History Modal ---
const historyBtn = document.getElementById("historyBtn");
const historyModal = document.getElementById("historyModal");
const closeHistoryModal = document.getElementById("closeHistoryModal");
const historyList = document.getElementById("historyList");

historyBtn.addEventListener("click", async () => {
  historyModal.style.display = "block";
  historyList.innerHTML = "";

  try {
    const res = await fetch(`${API_BASE}/groups/${groupId}/details?userId=${loggedInUserId}`);
    const group = await res.json();

    group.expenses.forEach(e => {
      const li = document.createElement("li");
      li.textContent = `💸 ${e.payerName} paid ₹${e.amount} for ${e.description}`;
      historyList.appendChild(li);
    });

    if (group.settlements && group.settlements.length > 0) {
      group.settlements.forEach(s => {
        const li = document.createElement("li");
        li.textContent = `🤝 ${s.payerName} settled ₹${s.amount} with ${s.payeeName} on ${s.date}`;
        historyList.appendChild(li);
      });
    }

  } catch (err) {
    console.error("Failed to load history:", err);
    historyList.innerHTML = "<li>⚠️ Could not load history.</li>";
  }
});

closeHistoryModal?.addEventListener("click", () => {
  historyModal.style.display = "none";
});
window.addEventListener("click", (e) => {
  if (e.target === historyModal) {
    historyModal.style.display = "none";
  }
});
