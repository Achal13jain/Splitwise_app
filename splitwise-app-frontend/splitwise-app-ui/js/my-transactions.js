
const API_BASE = "http://localhost:8080/api";

document.addEventListener("DOMContentLoaded", async () => {
  const userId = localStorage.getItem("userId");
  const res = await fetch(`${API_BASE}/users/${userId}/transactions`);
  const data = await res.json();

  const tbody = document.getElementById("transactionsTableBody");
  tbody.innerHTML = "";
  data.forEach(txn => {
    const row = `<tr>
      <td>${txn.description}</td>
      <td>₹${txn.amount.toFixed(2)}</td>
      <td>${txn.groupName}</td>
      <td>${txn.paidBy}</td>
      <td>${txn.type}</td>
      <td>${txn.date.replace("T", " ").split(".")[0]}</td>
    </tr>`;
    tbody.innerHTML += row;
  });
});
