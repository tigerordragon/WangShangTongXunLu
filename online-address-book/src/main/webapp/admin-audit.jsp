<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>管理员审核 - 网上通讯录</title>
  <link rel="stylesheet" href="assets/styles.css">
</head>
<body class="app-page" data-page="admin-audit" data-context="<%= request.getContextPath() %>">
  <nav class="site-nav" aria-label="主导航">
    <button type="button" class="site-nav-brand" data-go="login">网上通讯录</button>
    <div class="site-nav-menu">
      <button type="button" class="site-nav-link" data-go="login">学生登录</button>
      <button type="button" class="site-nav-link" data-go="register">学生注册</button>
      <button type="button" class="site-nav-link" data-go="profile">我的通讯录</button>
      <button type="button" class="site-nav-link" data-go="search">查询同学</button>
      <button type="button" class="site-nav-link is-active" data-go="admin-audit">审核后台</button>
      <button type="button" class="site-nav-link" data-go="admin-login">管理员登录</button>
    </div>
  </nav>
  <div class="shell">
    <section class="hero">
      <div>
        <div class="eyebrow">Admin Console</div>
        <h1>管理员审核页面</h1>
        <p class="lead">
          管理员登录后进入本页：审核待注册学生、删除未通过账号、禁用或启用已通过账号，并维护专业信息（增删改查）。
          默认管理员 gl1 还可审核其他管理员的注册申请。
        </p>
      </div>
      <div class="hero-actions">
        <button type="button" class="secondary" id="adminAuditLogoutBtn">退出管理员</button>
        <button type="button" class="secondary" data-go="login">学生登录入口</button>
      </div>
    </section>

    <section class="card">
      <div class="card-header">
        <h2>管理员注册审核</h2>
        <p>只有默认管理员 gl1 可以审核其他管理员的注册申请。</p>
      </div>
      <div class="card-body stacked">
        <div id="adminSessionSummary" class="session-summary">尚未登录管理员</div>
        <div id="adminAuditHint" class="notice notice-info">请先登录管理员账号。</div>
        <div id="pendingAdminUsers" class="table-wrap empty-state">正在加载待审核管理员...</div>
      </div>
    </section>

    <section class="grid grid-stack admin-student-panels">
      <article class="card">
        <div class="card-header">
          <h2>普通学生待审核账号</h2>
          <p>对应 <code>/api/admin/audits/students/pending</code> 和审核动作接口。</p>
        </div>
        <div class="card-body">
          <div id="pendingStudents" class="table-wrap empty-state">正在加载待审核数据...</div>
        </div>
      </article>

      <article class="card">
        <div class="card-header">
          <h2>学生账户总览</h2>
          <p>展示未通过、已通过和已禁用学生账号列表，并提供基础管理按钮。</p>
        </div>
        <div class="card-body stacked">
          <div>
            <h3 class="section-title">未通过审核</h3>
            <div id="unapprovedStudents" class="table-wrap empty-state">正在加载...</div>
          </div>
          <div>
            <h3 class="section-title">已通过审核</h3>
            <div id="approvedStudents" class="table-wrap empty-state">正在加载...</div>
          </div>
          <div>
            <h3 class="section-title">已禁用账号</h3>
            <div id="disabledStudents" class="table-wrap empty-state">正在加载...</div>
          </div>
        </div>
      </article>
    </section>

    <section class="card">
      <div class="card-header">
        <h2>专业信息维护</h2>
      </div>
      <div class="card-body stacked">
        <form id="professionalForm" class="form-inline">
          <input type="hidden" id="professionalId" value="">
          <label class="flex-grow">
            专业名称
            <input id="professionalName" placeholder="请输入专业名称">
          </label>
          <div class="form-actions">
            <button type="submit" id="professionalSubmitBtn">新增专业</button>
            <button type="button" class="secondary" id="professionalResetBtn">重置</button>
          </div>
        </form>
        <div id="professionalList" class="table-wrap empty-state">正在加载专业列表...</div>
      </div>
    </section>

    <div id="adminStatus" class="notice notice-info">管理员页面已就绪。</div>
  </div>

  <script src="assets/app.js"></script>
</body>
</html>
