<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>学生注册 - 网上通讯录</title>
  <link rel="stylesheet" href="assets/styles.css">
</head>
<body data-page="register" data-context="<%= request.getContextPath() %>">
  <div class="shell">
    <section class="hero">
      <div>
        <div class="eyebrow">Register</div>
        <h1>学生注册页面</h1>
        <p class="lead">
          提交账号与密码后，系统会创建待审核账号，管理员审核通过后才可以登录。
        </p>
      </div>
      <div class="hero-actions">
        <button type="button" class="secondary" data-go="login">返回登录</button>
      </div>
    </section>

    <section class="grid grid-two">
      <article class="card">
        <div class="card-header">
          <h2>注册表单</h2>
          <p>调用 <code>/api/students/register</code>，注册成功后状态为待审核。</p>
        </div>
        <form id="registerForm" class="card-body form-grid">
          <label>
            登录账号
            <input id="registerUsername" name="username" placeholder="new-student" autocomplete="username">
          </label>
          <label>
            登录密码
            <input id="registerPassword" name="password" type="password" placeholder="请输入密码" autocomplete="new-password">
          </label>
          <label>
            确认密码
            <input id="registerPasswordConfirm" name="passwordConfirm" type="password" placeholder="再次输入密码" autocomplete="new-password">
          </label>
          <div class="form-actions">
            <button type="submit">提交注册</button>
            <button type="button" class="secondary" id="registerDemoBtn">填充示例</button>
          </div>
          <div id="registerStatus" class="notice notice-info">请填写注册信息。</div>
        </form>
      </article>

      <article class="card">
        <div class="card-header">
          <h2>审核说明</h2>
          <p>注册完成后，管理员可以在审核页面进行通过或拒绝处理。</p>
        </div>
        <div class="card-body">
          <div class="step-list">
            <div class="step-item">
              <div class="step-index">1</div>
              <div>
                <div class="step-title">提交注册</div>
                <div class="step-desc">账号进入待审核状态。</div>
              </div>
            </div>
            <div class="step-item">
              <div class="step-index">2</div>
              <div>
                <div class="step-title">管理员审核</div>
                <div class="step-desc">审核通过后账号才可以登录。</div>
              </div>
            </div>
            <div class="step-item">
              <div class="step-index">3</div>
              <div>
                <div class="step-title">完善通讯录</div>
                <div class="step-desc">登录后进入个人通讯录页面补充专业、班级等信息。</div>
              </div>
            </div>
          </div>
        </div>
      </article>
    </section>
  </div>

  <script src="assets/app.js"></script>
</body>
</html>
