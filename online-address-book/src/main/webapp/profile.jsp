<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>完善自己的通讯录 - 网上通讯录</title>
  <link rel="stylesheet" href="assets/styles.css">
</head>
<body class="app-page" data-page="profile" data-context="<%= request.getContextPath() %>">
  <nav class="site-nav" aria-label="主导航">
    <button type="button" class="site-nav-brand" data-go="login">网上通讯录</button>
    <div class="site-nav-menu">
      <button type="button" class="site-nav-link" data-go="login">学生登录</button>
      <button type="button" class="site-nav-link" data-go="register">学生注册</button>
      <button type="button" class="site-nav-link is-active" data-go="profile">我的通讯录</button>
      <button type="button" class="site-nav-link" data-go="search">查询同学</button>
      <button type="button" class="site-nav-link" data-go="admin-login">管理员</button>
    </div>
  </nav>
  <div class="shell">
    <section class="hero">
      <div>
        <div class="eyebrow">Profile</div>
        <h1>学生完善自己的通讯录</h1>
        <p class="lead">
          登录后可以维护自己的专业、班级、入学年份、就业单位、城市、联系方式和邮箱。
        </p>
      </div>
      <div class="hero-actions">
        <button type="button" class="secondary" data-go="search">查询同学通讯录</button>
        <button type="button" class="secondary" id="profileLogoutBtn">退出登录</button>
      </div>
    </section>

    <section class="grid grid-two">
      <article class="card">
        <div class="card-header">
          <h2>个人通讯录</h2>
          <p>调用 <code>/api/students/me/contact</code> 获取和保存自己的通讯录信息。</p>
        </div>
        <form id="profileForm" class="card-body form-grid">
          <div class="form-row">
            <label>
              专业
              <input id="profileMajor" name="major" placeholder="请输入专业">
            </label>
            <label>
              班级
              <input id="profileClassName" name="className" placeholder="请输入班级">
            </label>
          </div>
          <div class="form-row">
            <label>
              入学年份
              <input id="profileEnrollmentYear" name="enrollmentYear" type="number" placeholder="例如：2022">
            </label>
            <label>
              就业单位
              <input id="profileJobUnit" name="jobUnit" placeholder="请输入就业单位">
            </label>
          </div>
          <div class="form-row">
            <label>
              城市
              <input id="profileCity" name="city" placeholder="请输入城市">
            </label>
            <label>
              联系方式
              <input id="profileContactMethod" name="contactMethod" placeholder="手机号 / 微信 / 其他">
            </label>
          </div>
          <label>
            邮箱
            <input id="profileEmail" name="email" type="email" placeholder="请输入邮箱">
          </label>
          <div class="form-actions">
            <button type="submit">保存通讯录</button>
            <button type="button" class="secondary" id="profileReloadBtn">重新加载</button>
          </div>
        </form>
      </article>

      <article class="card">
        <div class="card-header">
          <h2>登录记录</h2>
          <p>页面会同步显示当前账号的登录次数和最近登录时间。</p>
        </div>
        <div class="card-body stacked">
          <div class="info-grid info-grid-single" id="profileLoginInfo">
            <div class="info-box">
              <div class="info-title">登录次数</div>
              <div class="info-value">-</div>
            </div>
            <div class="info-box">
              <div class="info-title">最近登录时间</div>
              <div class="info-value">-</div>
            </div>
          </div>
          <div id="profileSummary" class="notice notice-info">请先登录后查看和编辑自己的通讯录。</div>
        </div>
      </article>
    </section>
  </div>

  <script src="assets/app.js"></script>
</body>
</html>
