<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>管理员审核 - 网上通讯录</title>
  <link rel="stylesheet" href="assets/styles.css">
</head>
<body data-page="admin-audit" data-context="<%= request.getContextPath() %>">
  <div class="shell">
    <section class="hero">
      <div>
        <div class="eyebrow">Admin Console</div>
        <h1>管理员审核页面</h1>
        <p class="lead">
          这里可以审核学生账号，也可以让默认管理员 gl1 审核其他管理员的注册申请。
          所有已审核通过的管理员都可以处理普通学生的注册。
        </p>
      </div>
      <div class="hero-actions">
        <button type="button" class="secondary" data-go="login">返回登录</button>
        <button type="button" class="secondary" data-go="admin-login">管理员登录</button>
        <button type="button" class="secondary" data-go="admin-register">管理员注册</button>
        <button type="button" class="secondary" data-go="register">学生注册</button>
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

    <section class="grid grid-two">
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
        <p>对应 <code>/api/admin/professionals</code> 的增删改查。</p>
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
