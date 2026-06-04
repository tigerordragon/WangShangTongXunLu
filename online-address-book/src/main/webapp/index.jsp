<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>

<html lang="zh-CN">

<head>

  <meta charset="UTF-8">

  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>学生登录 - 网上通讯录</title>

  <link rel="stylesheet" href="assets/styles.css">

</head>

<body class="auth-page" data-page="login" data-context="<%= request.getContextPath() %>">

  <div class="auth-backdrop">

    <nav class="auth-top-nav" aria-label="顶部导航">

      <span class="auth-top-brand-text">网上通讯录</span>

    </nav>



    <div class="auth-card">

      <div class="auth-panel-left">

        <p class="auth-lead">Hello, Friend!</p>

        <p class="auth-lead-cn">还没有账号？立即注册</p>

        <button type="button" class="auth-gradient-btn" data-go="register">Sign Up</button>

      </div>



      <div class="auth-panel-right">

        <h1 class="auth-script-title">Sign In</h1>

        <form id="loginForm" class="auth-form">

          <label class="auth-sr-only" for="loginUsername">登录账号</label>

          <input id="loginUsername" class="auth-field" name="username" type="text" autocomplete="username" placeholder="Username">

          <label class="auth-sr-only" for="loginPassword">登录密码</label>

          <input id="loginPassword" class="auth-field" name="password" type="password" autocomplete="current-password" placeholder="Password">

          <button type="submit" class="auth-gradient-btn">Sign In</button>

          <button type="button" class="auth-ghost-btn" id="loginDemoBtn">填充示例 student / 123456</button>

          <div id="loginStatus" class="notice notice-info">请使用审核通过的学生账号登录。</div>

          <div class="auth-session">

            <span class="session-label">当前会话：</span>

            <span id="loginSessionSummary">尚未登录</span>

          </div>

        </form>

      </div>

    </div>



    <div class="auth-footer-links">

      <button type="button" data-go="admin-login">管理员登录</button>

    </div>

  </div>



  <script src="assets/app.js"></script>

</body>

</html>

