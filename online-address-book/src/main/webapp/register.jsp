<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>

<html lang="zh-CN">

<head>

  <meta charset="UTF-8">

  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>学生注册 - 网上通讯录</title>

  <link rel="stylesheet" href="assets/styles.css">

</head>

<body class="auth-page" data-page="register" data-context="<%= request.getContextPath() %>">

  <div class="auth-backdrop">

    <nav class="auth-top-nav" aria-label="顶部导航">

      <span class="auth-top-brand-text">网上通讯录</span>

    </nav>



    <div class="auth-card">

      <div class="auth-panel-left">

        <p class="auth-lead">Welcome Back</p>

        <p class="auth-lead-cn">已有学生账号？前往登录</p>

        <button type="button" class="auth-gradient-btn" data-go="login">Sign In</button>

      </div>



      <div class="auth-panel-right">

        <h1 class="auth-script-title">Sign Up</h1>

        <form id="registerForm" class="auth-form">

          <label class="auth-sr-only" for="registerUsername">登录账号</label>

          <input id="registerUsername" class="auth-field" name="username" type="text" autocomplete="username" placeholder="Username">

          <label class="auth-sr-only" for="registerPassword">登录密码</label>

          <input id="registerPassword" class="auth-field" name="password" type="password" autocomplete="new-password" placeholder="Password">

          <label class="auth-sr-only" for="registerPasswordConfirm">确认密码</label>

          <input id="registerPasswordConfirm" class="auth-field" name="passwordConfirm" type="password" autocomplete="new-password" placeholder="Confirm Password">

          <button type="submit" class="auth-gradient-btn">Sign Up</button>

          <button type="button" class="auth-ghost-btn" id="registerDemoBtn">填充示例账号</button>

          <div id="registerStatus" class="notice notice-info">请填写注册信息。</div>

        </form>

      </div>

    </div>

  </div>



  <script src="assets/app.js"></script>

</body>

</html>

