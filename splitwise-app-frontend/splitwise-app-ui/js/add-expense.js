const API_BASE = "http://localhost:8080/api";
const userEmail = localStorage.getItem("userEmail");
if (!userEmail) window.location.href = "index.html";

// Load group options
const groupDropdown = document.getElementById("group");
async function loadGroups() {
  try {
    const res = await fetch(`${API_BASE}/groups/user/email/${userEmail}`);
    const groups = await res.json();
    groupDropdown.innerHTML = "";

    groups.forEach(group => {
      const option = document.createElement("option");
      option.value = group.id;
      option.textContent = group.name;
      groupDropdown.appendChild(option);
    });
  } catch (err) {
    console.error("Failed to fetch groups", err);
  }
}
loadGroups();

// Submit expense
document.getElementById("expenseForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const groupId = document.getElementById("group").value;
  const description = document.getElementById("description").value;
  const amount = parseFloat(document.getElementById("amount").value);

  try {
    const userRes = await fetch(`${API_BASE}/users/email/${userEmail}`);
    const user = await userRes.json();

    const payload = {
      groupId: groupId,
      description: description,
      amount: amount,
      payerId: user.id
    };

    const res = await fetch(`${API_BASE}/expenses/add`, {
    method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    if (res.ok) {
      alert("✅ Expense added successfully!");
      window.location.href = "dashboard.html";
    } else {
      alert("❌ Failed to add expense.");
    }
  } catch (err) {
    console.error(err);
    alert("⚠️ Something went wrong.");
  }
});

// Avatar logic
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

// Profile logic
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

closeProfileModal.addEventListener("click", () => {
  profileModal.style.display = "none";
});

window.addEventListener("click", (e) => {
  if (e.target === profileModal) {
    profileModal.style.display = "none";
  }
});

// Logout
document.getElementById("logoutBtn").addEventListener("click", () => {
  if (confirm("Are you sure you want to logout?")) {
    localStorage.clear();
    window.location.href = "index.html";
  }
});
