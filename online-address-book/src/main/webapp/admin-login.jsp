<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>

<html lang="zh-CN">

<head>

  <meta charset="UTF-8">

  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>管理员登录 - 网上通讯录</title>

  <link rel="stylesheet" href="assets/styles.css">

</head>

<body class="auth-page" data-page="admin-login" data-context="<%= request.getContextPath() %>">

  <div class="auth-backdrop">

    <nav class="auth-top-nav" aria-label="顶部导航">

      <span class="auth-top-brand-text">网上通讯录</span>

    </nav>



    <div class="auth-card">

      <div class="auth-panel-left">

        <p class="auth-lead">Admin Portal</p>

        <p class="auth-lead-cn">需要管理员账号？前往注册</p>

        <button type="button" class="auth-gradient-btn" data-go="admin-register">Sign Up</button>

      </div>



      <div class="auth-panel-right">

        <h1 class="auth-script-title">Sign In</h1>

        <form id="adminLoginForm" class="auth-form">

          <label class="auth-sr-only" for="adminLoginUsername">管理员账号</label>

          <input id="adminLoginUsername" class="auth-field" name="username" type="text" autocomplete="username" placeholder="Admin Username">

          <label class="auth-sr-only" for="adminLoginPassword">管理员密码</label>

          <input id="adminLoginPassword" class="auth-field" name="password" type="password" autocomplete="current-password" placeholder="Password">

          <button type="submit" class="auth-gradient-btn">Sign In</button>

          <button type="button" class="auth-ghost-btn" id="adminLoginDemoBtn">填充默认 gl1 / 123456</button>

          <div id="adminLoginStatus" class="notice notice-info">请使用已通过审核的管理员账号登录。</div>

          <div class="auth-session">

            <span class="session-label">当前管理员：</span>

            <span id="adminSessionSummary">尚未登录</span>

          </div>

        </form>

      </div>

    </div>



    <div class="auth-footer-links">

      <button type="button" data-go="login">返回学生登录</button>

    </div>

  </div>



  <script src="assets/app.js"></script>

</body>

</html>

