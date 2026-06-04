<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>学生登录 - 网上通讯录</title>
  <link rel="stylesheet" href="assets/styles.css">
</head>
<body data-page="login" data-context="<%= request.getContextPath() %>">
  <div class="shell">
    <section class="hero hero-login">
      <div>
        <div class="eyebrow">Online Address Book</div>
        <h1>学生登录页面</h1>
        <p class="lead">
          这是系统默认入口。学生登录成功后将自动进入通讯录完善页面；尚未注册的同学可先注册，管理员请使用管理员登录入口。
        </p>
      </div>
      <div class="session-panel">
        <div class="session-label">当前会话</div>
        <div id="loginSessionSummary" class="session-summary">尚未登录</div>
        <div id="loginStatus" class="notice notice-info">请使用审核通过的学生账号登录。</div>
      </div>
    </section>

    <section class="grid grid-two">
      <article class="card">
        <div class="card-header">
          <h2>学生登录</h2>
          <p>调用 <code>/api/auth/login</code>，登录后会保存访问令牌和刷新令牌。</p>
        </div>
        <form id="loginForm" class="card-body form-grid">
          <label>
            登录账号
            <input id="loginUsername" name="username" autocomplete="username" value="student" placeholder="请输入账号">
          </label>
          <label>
            登录密码
            <input id="loginPassword" name="password" type="password" autocomplete="current-password" value="123456" placeholder="请输入密码">
          </label>
          <div class="form-actions">
            <button type="submit">登录</button>
            <button type="button" class="secondary" id="loginDemoBtn">填充示例</button>
          </div>
        </form>
      </article>

      <article class="card">
        <div class="card-header">
          <h2>其他入口</h2>
          <p>尚未拥有账号的同学可先注册；系统管理员请从管理员登录进入审核后台。</p>
        </div>
        <div class="card-body quick-links">
          <button type="button" class="secondary" data-go="register">学生注册</button>
          <button type="button" class="secondary" data-go="admin-login">管理员登录</button>
        </div>
        <div class="card-footer">
          审核通过的学生账号登录后将自动跳转到通讯录完善页面。
        </div>
      </article>
    </section>
  </div>

  <script src="assets/app.js"></script>
</body>
</html>
