const API_BASE = "http://localhost:8080/api";

// Check if user is logged in
const userEmail = localStorage.getItem("userEmail");
if (!userEmail) {
  window.location.href = "index.html";
}

//  Form submission to create group with members
document.getElementById("groupForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const name = document.getElementById("groupName").value;
  const membersRaw = document.getElementById("members").value;
  const memberUsernames = membersRaw
    .split(",")
    .map(username => username.trim())
    .filter(username => username.length > 0);

  try {
    const res = await fetch(`${API_BASE}/users/email/${userEmail}`);
    const user = await res.json();

    const payload = {
      name: name,
      userId: user.id,                       
      memberUsernames: memberUsernames       
    };

    const createRes = await fetch(`${API_BASE}/groups/create-with-members`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    if (createRes.ok) {
      alert("✅ Group created successfully!");
      window.location.href = "dashboard.html";
    } else {
      alert("❌ Failed to create group.");
    }

  } catch (err) {
    console.error("Error:", err);
    alert("⚠️ Something went wrong.");
  }
});

// Avatar dropdown toggle
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

// Profile button
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

closeProfileModal?.addEventListener("click", () => {
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
