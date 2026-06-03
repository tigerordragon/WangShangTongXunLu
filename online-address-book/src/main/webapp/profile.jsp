<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>我的通讯录</title>
  <meta charset="UTF-8"/>
</head>
<body>
<h2>我的通讯录信息</h2>
<p>
  <label>登录账号</label>
  <input id="username" name="username" value="student"/>
</p>
<p>
  <label>密码</label>
  <input id="password" name="password" type="password" value="123456"/>
</p>
<button type="button" id="loginBtn">登录并加载</button>
<hr/>
<form id="profileForm">
  <p>
    <label>专业</label>
    <input name="major" id="major"/>
  </p>
  <p>
    <label>班级</label>
    <input name="className" id="className"/>
  </p>
  <p>
    <label>年份</label>
    <input name="enrollmentYear" id="enrollmentYear" type="number"/>
  </p>
  <p>
    <label>就业单位</label>
    <input name="jobUnit" id="jobUnit"/>
  </p>
  <p>
    <label>城市</label>
    <input name="city" id="city"/>
  </p>
  <p>
    <label>联系方式</label>
    <input name="contactMethod" id="contactMethod"/>
  </p>
  <p>
    <label>邮箱</label>
    <input name="email" id="email" type="email"/>
  </p>
  <button type="submit">保存</button>
</form>
<pre id="profileResult"></pre>
<script>
var accessToken = '';

function apiFetch(path, options) {
  var headers = options.headers || {};
  headers['Content-Type'] = 'application/json';
  if (accessToken) {
    headers['Authorization'] = 'Bearer ' + accessToken;
  }
  options.headers = headers;
  return fetch('api' + path, options).then(function (response) {
    return response.text().then(function (body) {
      return { ok: response.ok, status: response.status, body: body };
    });
  });
}

function fillProfile(data) {
  document.getElementById('major').value = data.major || '';
  document.getElementById('className').value = data.className || '';
  document.getElementById('enrollmentYear').value = data.enrollmentYear || '';
  document.getElementById('jobUnit').value = data.jobUnit || '';
  document.getElementById('city').value = data.city || '';
  document.getElementById('contactMethod').value = data.contactMethod || '';
  document.getElementById('email').value = data.email || '';
}

function loadProfile() {
  return apiFetch('/students/me/contact', { method: 'GET' }).then(function (result) {
    document.getElementById('profileResult').textContent = result.body;
    if (result.ok) {
      fillProfile(JSON.parse(result.body));
    }
    return result;
  });
}

document.getElementById('loginBtn').addEventListener('click', function () {
  apiFetch('/auth/login', {
    method: 'POST',
    body: JSON.stringify({
      username: document.getElementById('username').value,
      password: document.getElementById('password').value
    })
  }).then(function (result) {
    document.getElementById('profileResult').textContent = result.body;
    if (!result.ok) {
      accessToken = '';
      return;
    }
    var loginData = JSON.parse(result.body);
    accessToken = loginData.accessToken;
    return loadProfile();
  });
});

document.getElementById('profileForm').addEventListener('submit', function (event) {
  event.preventDefault();
  if (!accessToken) {
    document.getElementById('profileResult').textContent = '请先登录';
    return;
  }
  var form = event.target;
  apiFetch('/students/me/contact', {
    method: 'PUT',
    body: JSON.stringify({
      major: form.major.value,
      className: form.className.value,
      enrollmentYear: form.enrollmentYear.value ? Number(form.enrollmentYear.value) : null,
      jobUnit: form.jobUnit.value,
      city: form.city.value,
      contactMethod: form.contactMethod.value,
      email: form.email.value
    })
  }).then(function (result) {
    document.getElementById('profileResult').textContent = result.body;
    if (result.ok) {
      fillProfile(JSON.parse(result.body));
    }
  });
});
</script>
</body>
</html>
