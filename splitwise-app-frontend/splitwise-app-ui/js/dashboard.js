const API_BASE = "http://localhost:8080/api";

//  Check login
const userEmail = localStorage.getItem("userEmail");
if (!userEmail) {
  window.location.href = "index.html";
}

document.addEventListener("DOMContentLoaded", () => {
  //  Load groups
  const groupContainer = document.getElementById("groupsContainer");

  async function loadGroups() {
    try {
      const res = await fetch(`${API_BASE}/groups/user/email/${userEmail}`);
      const groups = await res.json();

      if (!Array.isArray(groups)) {
        groupContainer.innerHTML = "<p>Error loading groups</p>";
        return;
      }

      if (groups.length === 0) {
        groupContainer.innerHTML = "<p>No groups found. Create one to get started!</p>";
        return;
      }

      groupContainer.innerHTML = "";

      groups.forEach((group) => {
        const card = document.createElement("div");
        card.className = "group-card";
        card.innerHTML = `
          <h2>${group.name}</h2>
          <p>Group ID: ${group.id}</p>
          <button class="open-group-btn" data-id="${group.id}">Open Group</button>
        `;
        groupContainer.appendChild(card);
      });

      document.querySelectorAll(".open-group-btn").forEach((btn) => {
        btn.addEventListener("click", () => {
          const groupId = btn.getAttribute("data-id");
          window.location.href = `group.html?id=${groupId}`;
        });
      });
    } catch (err) {
      console.error("Failed to load groups", err);
      groupContainer.innerHTML = "<p>Failed to load groups.</p>";
    }
  }

  loadGroups();

  // Avatar dropdown toggle
  const avatar = document.getElementById("avatarBtn");
  const dropdown = document.getElementById("dropdownMenu");

  avatar.addEventListener("click", () => {
    dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
  });

  // Hide dropdown if clicked outside
  window.addEventListener("click", (e) => {
    if (!avatar.contains(e.target) && !dropdown.contains(e.target)) {
      dropdown.style.display = "none";
    }
  });

  // Profile Modal Logic
  const profileBtn = document.getElementById("profileBtn");
  const profileModal = document.getElementById("profileModal");
  const closeProfileModal = document.getElementById("closeProfileModal");

  profileBtn.addEventListener("click", async () => {
    try {
      const res = await fetch(`${API_BASE}/users/email/${userEmail}`);
      const user = await res.json();

      if (user?.name && user?.email) {
        document.getElementById("profileName").textContent = user.name;
        document.getElementById("profileEmail").textContent = user.email;
        profileModal.style.display = "block";
      } else {
        alert("⚠️ Could not fetch user details.");
      }
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

  // Logout button now working
  const logoutBtn = document.getElementById("logoutBtn");
  logoutBtn.addEventListener("click", () => {
    if (confirm("Are you sure you want to logout?")) {
      localStorage.removeItem("userEmail");
      window.location.href = "home.html";
    }
    dropdown.style.display = "none";
  });
});
