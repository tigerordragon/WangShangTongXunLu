<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>查询其他通讯录 - 网上通讯录</title>
  <link rel="stylesheet" href="assets/styles.css">
</head>
<body data-page="search" data-context="<%= request.getContextPath() %>">
  <div class="shell">
    <section class="hero">
      <div>
        <div class="eyebrow">Search</div>
        <h1>查询其他学生通讯录</h1>
        <p class="lead">
          登录后可以按照专业、班级和入学年份筛选同学通讯录。查询结果只展示已审核通过的公开信息。
        </p>
      </div>
      <div class="hero-actions">
        <button type="button" class="secondary" data-go="profile">返回通讯录完善</button>
      </div>
    </section>

    <section class="card">
      <div class="card-header">
        <h2>筛选条件</h2>
        <p>对应 <code>/api/students/contacts</code> 的查询参数。</p>
      </div>
      <form id="searchForm" class="card-body search-form">
        <label>
          专业
          <input id="searchMajor" name="major" placeholder="例如：计算机科学与技术">
        </label>
        <label>
          班级
          <input id="searchClassName" name="className" placeholder="例如：一班">
        </label>
        <label>
          入学年份
          <input id="searchEnrollmentYear" name="enrollmentYear" type="number" placeholder="例如：2022">
        </label>
        <div class="form-actions">
          <button type="submit">开始查询</button>
          <button type="button" class="secondary" id="searchClearBtn">清空条件</button>
        </div>
      </form>
    </section>

    <section class="card">
      <div class="card-header">
        <h2>查询结果</h2>
        <p>只会显示审核通过的公开通讯录信息。</p>
      </div>
      <div class="card-body">
        <div id="searchStatus" class="notice notice-info">请先登录后再查询。</div>
        <div id="searchResults" class="table-wrap empty-state">暂无查询结果。</div>
      </div>
    </section>
  </div>

  <script src="assets/app.js"></script>
</body>
</html>
