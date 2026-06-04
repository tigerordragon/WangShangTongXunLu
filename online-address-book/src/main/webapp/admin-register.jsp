<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>管理员注册 - 网上通讯录</title>
  <link rel="stylesheet" href="assets/styles.css">
</head>
<body data-page="admin-register" data-context="<%= request.getContextPath() %>">
  <div class="shell">
    <section class="hero">
      <div>
        <div class="eyebrow">Admin Register</div>
        <h1>管理员注册页面</h1>
        <p class="lead">
          新管理员注册后会进入待审核状态，由默认管理员 <strong>gl1</strong> 审核通过后才可以登录。
        </p>
        <div class="hero-actions">
          <button type="button" class="secondary" data-go="admin-login">管理员登录</button>
          <button type="button" class="secondary" data-go="login">学生登录</button>
        </div>
      </div>
      <div class="session-panel">
        <div class="session-label">管理员会话</div>
        <div id="adminSessionSummary" class="session-summary">尚未登录</div>
        <div id="adminRegisterStatus" class="notice notice-info">请填写管理员注册信息。</div>
      </div>
    </section>

    <section class="grid grid-two">
      <article class="card">
        <div class="card-header">
          <h2>注册管理员</h2>
          <p>填写账号、密码和显示名称，提交后等待 gl1 审核。</p>
        </div>
        <form id="adminRegisterForm" class="card-body form-grid">
          <label>
            管理员账号
            <input id="adminRegisterUsername" name="username" autocomplete="username" placeholder="例如：admin2">
          </label>
          <label>
            显示名称
            <input id="adminRegisterDisplayName" name="displayName" placeholder="例如：管理员二号">
          </label>
          <label>
            管理员密码
            <input id="adminRegisterPassword" name="password" type="password" autocomplete="new-password" placeholder="请输入密码">
          </label>
          <label>
            确认密码
            <input id="adminRegisterPasswordConfirm" name="passwordConfirm" type="password" autocomplete="new-password" placeholder="再次输入密码">
          </label>
          <div class="form-actions">
            <button type="submit">提交注册</button>
            <button type="button" class="secondary" id="adminRegisterDemoBtn">填充示例</button>
          </div>
          <div id="adminRegisterFormStatus" class="notice notice-info">请填写注册信息后提交。</div>
        </form>
      </article>

      <article class="card">
        <div class="card-header">
          <h2>审核流程</h2>
          <p>管理员注册完成后，会出现在管理员审核页面的待审核列表中。</p>
        </div>
        <div class="card-body">
          <div class="step-list">
            <div class="step-item">
              <div class="step-index">1</div>
              <div>
                <div class="step-title">提交注册</div>
                <div class="step-desc">新管理员账号进入待审核状态。</div>
              </div>
            </div>
            <div class="step-item">
              <div class="step-index">2</div>
              <div>
                <div class="step-title">gl1 审核</div>
                <div class="step-desc">默认管理员可以通过或拒绝其他管理员注册。</div>
              </div>
            </div>
            <div class="step-item">
              <div class="step-index">3</div>
              <div>
                <div class="step-title">管理员登录</div>
                <div class="step-desc">审核通过后才可以使用该账号登录管理员页面。</div>
              </div>
            </div>
          </div>
        </div>
        <div class="card-footer">
          <button type="button" class="secondary" data-go="admin-login">已有账号？去管理员登录</button>
        </div>
      </article>
    </section>
  </div>

  <script src="assets/app.js"></script>
</body>
</html>
