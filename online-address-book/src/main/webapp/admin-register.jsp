<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>

<html lang="zh-CN">

<head>

  <meta charset="UTF-8">

  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>管理员注册 - 网上通讯录</title>

  <link rel="stylesheet" href="assets/styles.css">

</head>

<body class="auth-page" data-page="admin-register" data-context="<%= request.getContextPath() %>">

  <div class="auth-backdrop">

    <nav class="auth-top-nav" aria-label="顶部导航">

      <span class="auth-top-brand-text">网上通讯录</span>

    </nav>



    <div class="auth-card">

      <div class="auth-panel-left">

        <p class="auth-lead">Welcome Back</p>

        <p class="auth-lead-cn">已有管理员账号？前往登录</p>

        <button type="button" class="auth-gradient-btn" data-go="admin-login">Sign In</button>

      </div>



      <div class="auth-panel-right">

        <h1 class="auth-script-title">Sign Up</h1>

        <form id="adminRegisterForm" class="auth-form">

          <label class="auth-sr-only" for="adminRegisterUsername">管理员账号</label>

          <input id="adminRegisterUsername" class="auth-field" name="username" type="text" autocomplete="username" placeholder="Admin Username">

          <label class="auth-sr-only" for="adminRegisterDisplayName">显示名称</label>

          <input id="adminRegisterDisplayName" class="auth-field" name="displayName" type="text" placeholder="Display Name">

          <label class="auth-sr-only" for="adminRegisterPassword">密码</label>

          <input id="adminRegisterPassword" class="auth-field" name="password" type="password" autocomplete="new-password" placeholder="Password">

          <label class="auth-sr-only" for="adminRegisterPasswordConfirm">确认密码</label>

          <input id="adminRegisterPasswordConfirm" class="auth-field" name="passwordConfirm" type="password" autocomplete="new-password" placeholder="Confirm Password">

          <button type="submit" class="auth-gradient-btn">Sign Up</button>

          <button type="button" class="auth-ghost-btn" id="adminRegisterDemoBtn">填充示例</button>

          <div id="adminRegisterFormStatus" class="notice notice-info">请填写注册信息后提交。</div>

          <div id="adminRegisterStatus" class="notice notice-info" hidden aria-hidden="true"></div>

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

