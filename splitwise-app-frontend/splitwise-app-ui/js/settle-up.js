const API_BASE = "http://localhost:8080/api";

//  Check if user is logged in
const userEmail = localStorage.getItem("userEmail");
if (!userEmail) {
  window.location.href = "index.html";
}

//  Get groupId from query param
const urlParams = new URLSearchParams(window.location.search);
const groupId = urlParams.get("id");

if (!groupId) {
  document.getElementById("balancesContainer").innerHTML = "<p>❌ Group ID not provided.</p>";
} else {
  loadBalances();
}

async function loadBalances() {
  try {
    const res = await fetch(`${API_BASE}/expenses/group/${groupId}/balances`);
    const balances = await res.json();

    const container = document.getElementById("balancesContainer");
    container.innerHTML = "";

    if (balances.length === 0) {
      container.innerHTML = "<p>No balances to display.</p>";
      return;
    }

    balances.forEach((b) => {
      const div = document.createElement("div");
      div.className = "balance-card";

      let status = "";
      if (b.balance > 0) {
        status = `<span class="you-get">You are owed ₹${b.balance.toFixed(2)}</span>`;
      } else if (b.balance < 0) {
        status = `<span class="you-owe">You owe ₹${Math.abs(b.balance).toFixed(2)}</span>`;
      } else {
        status = `<span class="settled">Settled Up</span>`;
      }

      div.innerHTML = `
        <h3>${b.name}</h3>
        <p>Email: ${b.email}</p>
        ${status}
      `;

      container.appendChild(div);
    });

  } catch (err) {
    console.error("Error fetching balances:", err);
    document.getElementById("balancesContainer").innerHTML = "<p>⚠️ Error loading balances</p>";
  }
}

// Avatar dropdown
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

// Profile
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
  } catch (err) {
    alert("⚠️ Error loading profile info.");
  }
  dropdown.style.display = "none";
});

closeProfileModal.addEventListener("click", () => {
  profileModal.style.display = "none";
});
window.addEventListener("click", (event) => {
  if (event.target === profileModal) {
    profileModal.style.display = "none";
  }
});

// Logout
const logoutBtn = document.getElementById("logoutBtn");
logoutBtn.addEventListener("click", () => {
  if (confirm("Are you sure you want to logout?")) {
    localStorage.removeItem("userEmail");
    window.location.href = "index.html";
  }
  dropdown.style.display = "none";
});
