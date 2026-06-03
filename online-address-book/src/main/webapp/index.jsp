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
          这里是系统的入口。登录成功后可以直接跳转到个人通讯录、查询同学通讯录，也可以进入注册和管理员审核页面。
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
            <button type="button" class="secondary" id="loginLogoutBtn">退出登录</button>
          </div>
        </form>
      </article>

      <article class="card">
        <div class="card-header">
          <h2>快捷入口</h2>
          <p>所有页面都在本地 Tomcat 中运行，按钮只是在不同 JSP 页面之间跳转。</p>
        </div>
        <div class="card-body quick-links">
          <button type="button" class="secondary" data-go="admin-login">管理员登录</button>
          <button type="button" class="secondary" data-go="admin-register">管理员注册</button>
          <button type="button" class="secondary" data-go="register">注册页面</button>
          <button type="button" class="secondary" data-go="admin-audit">管理员审核页面</button>
          <button type="button" class="secondary" data-go="search">查询其他通讯录</button>
          <button type="button" class="secondary" data-go="profile">完善自己的通讯录</button>
        </div>
        <div class="card-footer">
          登录成功后，页面会自动读取“登录次数”和“最近登录时间”。
        </div>
      </article>
    </section>

    <section class="card">
      <div class="card-header">
        <h2>登录信息</h2>
        <p>用于展示当前账号的登录记录。若未登录，页面会显示提示信息。</p>
      </div>
      <div class="card-body">
        <div id="loginInfoPanel" class="info-grid">
          <div class="info-box">
            <div class="info-title">登录次数</div>
            <div class="info-value">-</div>
          </div>
          <div class="info-box">
            <div class="info-title">最近登录时间</div>
            <div class="info-value">-</div>
          </div>
          <div class="info-box">
            <div class="info-title">访问令牌</div>
            <div class="info-value mono">-</div>
          </div>
        </div>
      </div>
    </section>
  </div>

  <script src="assets/app.js"></script>
</body>
</html>
