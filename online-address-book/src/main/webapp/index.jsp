<html>
<body>
<h2>Online Address Book</h2>
<form id="loginForm">
  <p>
    <label>Username</label>
    <input name="username" value="student">
  </p>
  <p>
    <label>Password</label>
    <input name="password" type="password" value="123456">
  </p>
  <button type="submit">Login</button>
</form>
<pre id="loginResult"></pre>
<script>
document.getElementById('loginForm').addEventListener('submit', function (event) {
  event.preventDefault();
  var form = event.target;
  fetch('api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      username: form.username.value,
      password: form.password.value
    })
  }).then(function (response) {
    return response.text();
  }).then(function (body) {
    document.getElementById('loginResult').textContent = body;
  });
});
</script>
</body>
</html>
