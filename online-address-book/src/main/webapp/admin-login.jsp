<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>管理员登录 - 网上通讯录</title>
  <link rel="stylesheet" href="assets/styles.css">
</head>
<body data-page="admin-login" data-context="<%= request.getContextPath() %>">
  <div class="shell">
    <section class="hero hero-login">
      <div>
        <div class="eyebrow">Admin Login</div>
        <h1>管理员登录页面</h1>
        <p class="lead">
          默认第一个管理员账号由前端内部预置为 <strong>gl1 / 123456</strong>。
          只有已通过审核的管理员可以登录系统并进入审核页面。
        </p>
      </div>
      <div class="session-panel">
        <div class="session-label">当前管理员</div>
        <div id="adminSessionSummary" class="session-summary">尚未登录</div>
        <div id="adminLoginStatus" class="notice notice-info">请使用管理员账号登录。</div>
      </div>
    </section>

    <section class="grid grid-two">
      <article class="card">
        <div class="card-header">
          <h2>管理员登录</h2>
          <p>管理员账号是前端本地态，不影响学生端后端接口。</p>
        </div>
        <form id="adminLoginForm" class="card-body form-grid">
          <label>
            管理员账号
            <input id="adminLoginUsername" name="username" autocomplete="username" value="gl1" placeholder="请输入管理员账号">
          </label>
          <label>
            管理员密码
            <input id="adminLoginPassword" name="password" type="password" autocomplete="current-password" value="123456" placeholder="请输入密码">
          </label>
          <div class="form-actions">
            <button type="submit">登录</button>
            <button type="button" class="secondary" id="adminLoginDemoBtn">填充默认账号</button>
          </div>
        </form>
      </article>

      <article class="card">
        <div class="card-header">
          <h2>快捷入口</h2>
          <p>管理员登录后可以进入审核页面，未登录时也可以跳转到注册页查看流程。</p>
        </div>
        <div class="card-body quick-links">
          <button type="button" class="secondary" data-go="admin-register">管理员注册</button>
          <button type="button" class="secondary" data-go="login">返回学生登录</button>
        </div>
        <div class="card-footer">
          登录成功后将自动进入管理员审核页面。gl1 为默认首个管理员，可审核其他管理员注册申请。
        </div>
      </article>
    </section>
  </div>

  <script src="assets/app.js"></script>
</body>
</html>
