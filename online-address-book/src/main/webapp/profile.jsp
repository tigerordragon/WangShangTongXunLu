<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>我的通讯录</title>
  <meta charset="UTF-8"/>
</head>
<body>
<h2>我的通讯录信息</h2>
<p><a href="index.jsp">返回首页</a></p>
<p id="statusMessage"></p>
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
    <input name="enrollmentYear" id="enrollmentYear" type="number" min="1900" max="2100"/>
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
<script>
var ACCESS_TOKEN_KEY = 'studentProfileAccessToken';
var accessToken = sessionStorage.getItem(ACCESS_TOKEN_KEY) || '';

function showStatus(text, isError) {
  var el = document.getElementById('statusMessage');
  el.textContent = text || '';
  el.style.color = isError ? '#b00020' : '#1b5e20';
}

function parseMessage(body) {
  try {
    var data = JSON.parse(body);
    if (data.message) {
      return data.message;
    }
  } catch (e) {
    // ignore
  }
  return body;
}

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
  document.getElementById('enrollmentYear').value = data.enrollmentYear != null ? data.enrollmentYear : '';
  document.getElementById('jobUnit').value = data.jobUnit || '';
  document.getElementById('city').value = data.city || '';
  document.getElementById('contactMethod').value = data.contactMethod || '';
  document.getElementById('email').value = data.email || '';
}

function persistToken(token) {
  accessToken = token || '';
  if (accessToken) {
    sessionStorage.setItem(ACCESS_TOKEN_KEY, accessToken);
  } else {
    sessionStorage.removeItem(ACCESS_TOKEN_KEY);
  }
}

function loadProfile() {
  return apiFetch('/students/me/contact', { method: 'GET' }).then(function (result) {
    if (result.ok) {
      fillProfile(JSON.parse(result.body));
      showStatus('已加载通讯录信息', false);
    } else {
      showStatus(parseMessage(result.body), true);
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
    if (!result.ok) {
      persistToken('');
      showStatus(parseMessage(result.body), true);
      return;
    }
    var loginData = JSON.parse(result.body);
    persistToken(loginData.accessToken);
    return loadProfile();
  });
});

document.getElementById('profileForm').addEventListener('submit', function (event) {
  event.preventDefault();
  if (!accessToken) {
    showStatus('请先登录', true);
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
    if (result.ok) {
      fillProfile(JSON.parse(result.body));
      showStatus('保存成功', false);
    } else {
      showStatus(parseMessage(result.body), true);
    }
  });
});

if (accessToken) {
  loadProfile();
}
</script>
</body>
</html>
