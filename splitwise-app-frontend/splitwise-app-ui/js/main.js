const API_BASE = "http://localhost:8080/api";

const container = document.getElementById("container");

document.getElementById("signUp").addEventListener("click", () =>
  container.classList.add("right-panel-active")
);
document.getElementById("signIn").addEventListener("click", () =>
  container.classList.remove("right-panel-active")
);

document.getElementById("registerForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const name = document.getElementById("regName").value;
  const email = document.getElementById("regEmail").value;
  const password = document.getElementById("regPassword").value;

  const res = await fetch(API_BASE + "/users/register", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name, email, password }),
  });

  const data = await res.text();
  alert(data);
});

document.getElementById("loginForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const email = document.getElementById("loginEmail").value;
  const password = document.getElementById("loginPassword").value;

  try {
    const res = await fetch(API_BASE + "/users/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });

    if (!res.ok) {
      const errorText = await res.text();
      alert("Login failed: " + errorText);
      return;
    }

    const user = await res.json();

    localStorage.setItem("userId", user.id);
    localStorage.setItem("userEmail", user.email);

    window.location.href = "dashboard.html";
  } catch (err) {
    console.error("Login Error:", err);
    alert("⚠️ Error logging in.");
  }
});
