<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Online Address Book</title>
  <style>
    :root {
      --bg: #0f172a;
      --panel: rgba(15, 23, 42, 0.78);
      --border: rgba(148, 163, 184, 0.18);
      --text: #e2e8f0;
      --muted: #94a3b8;
      --primary: #38bdf8;
      --primary-strong: #0ea5e9;
      --success: #22c55e;
      --danger: #ef4444;
      --shadow: 0 24px 80px rgba(2, 6, 23, 0.45);
    }

    * { box-sizing: border-box; }

    body {
      margin: 0;
      min-height: 100vh;
      font-family: "Segoe UI", "PingFang SC", "Microsoft YaHei", sans-serif;
      color: var(--text);
      background:
        radial-gradient(circle at top left, rgba(56, 189, 248, 0.22), transparent 28%),
        radial-gradient(circle at top right, rgba(34, 197, 94, 0.15), transparent 24%),
        linear-gradient(160deg, #020617 0%, #0f172a 52%, #111827 100%);
    }

    .shell {
      width: min(1180px, calc(100% - 32px));
      margin: 0 auto;
      padding: 32px 0 40px;
    }

    .hero, .card {
      background: var(--panel);
      border: 1px solid var(--border);
      border-radius: 22px;
      box-shadow: var(--shadow);
      backdrop-filter: blur(18px);
    }

    .hero {
      padding: 28px;
      margin-bottom: 20px;
    }

    .eyebrow {
      color: var(--primary);
      font-size: 12px;
      letter-spacing: 0.18em;
      text-transform: uppercase;
    }

    h1 {
      margin: 8px 0 0;
      font-size: clamp(28px, 5vw, 46px);
      line-height: 1.08;
    }

    .subtitle {
      margin: 12px 0 0;
      color: var(--muted);
      line-height: 1.7;
      max-width: 72ch;
    }

    .grid {
      display: grid;
      grid-template-columns: repeat(12, minmax(0, 1fr));
      gap: 18px;
    }

    .span-5 { grid-column: span 5; }
    .span-7 { grid-column: span 7; }
    .span-12 { grid-column: span 12; }

    .card-header { padding: 18px 20px 0; }
    .card-body { padding: 18px 20px 20px; }

    .card-title { margin: 0; font-size: 18px; }
    .card-subtitle { margin: 6px 0 0; color: var(--muted); font-size: 13px; line-height: 1.6; }

    form { display: grid; gap: 12px; }
    label { display: grid; gap: 8px; font-size: 14px; }
    .row { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 12px; }

    input {
      width: 100%;
      padding: 12px 14px;
      border-radius: 12px;
      border: 1px solid rgba(148, 163, 184, 0.2);
      background: rgba(2, 6, 23, 0.65);
      color: var(--text);
      outline: none;
    }

    input:focus {
      border-color: rgba(56, 189, 248, 0.8);
      box-shadow: 0 0 0 4px rgba(56, 189, 248, 0.15);
    }

    .actions {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
      margin-top: 4px;
    }

    button {
      border: 0;
      border-radius: 12px;
      padding: 11px 14px;
      cursor: pointer;
      color: white;
      background: linear-gradient(180deg, var(--primary), var(--primary-strong));
      box-shadow: 0 12px 28px rgba(14, 165, 233, 0.28);
    }

    button.secondary {
      background: rgba(148, 163, 184, 0.18);
      box-shadow: none;
      border: 1px solid rgba(148, 163, 184, 0.22);
      color: var(--text);
    }

    button.success {
      background: linear-gradient(180deg, #34d399, #16a34a);
      box-shadow: 0 12px 28px rgba(34, 197, 94, 0.25);
    }

    button.danger {
      background: linear-gradient(180deg, #fb7185, #ef4444);
      box-shadow: 0 12px 28px rgba(239, 68, 68, 0.25);
    }

    .output {
      min-height: 140px;
      padding: 16px;
      border-radius: 16px;
      background: rgba(2, 6, 23, 0.7);
      border: 1px solid rgba(148, 163, 184, 0.2);
      white-space: pre-wrap;
      word-break: break-word;
      color: #cbd5e1;
      line-height: 1.6;
    }

    .pending-list {
      display: grid;
      gap: 12px;
    }

    .pending-item {
      display: grid;
      gap: 12px;
      padding: 14px;
      border-radius: 16px;
      background: rgba(2, 6, 23, 0.55);
      border: 1px solid rgba(148, 163, 184, 0.16);
    }

    .pending-meta {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
      align-items: center;
      justify-content: space-between;
    }

    .pending-title { font-weight: 600; }
    .pending-badge {
      display: inline-flex;
      align-items: center;
      padding: 6px 10px;
      border-radius: 999px;
      background: rgba(245, 158, 11, 0.16);
      border: 1px solid rgba(245, 158, 11, 0.22);
      color: #fbbf24;
      font-size: 12px;
    }

    .muted { color: var(--muted); }
    .mini { font-size: 12px; color: var(--muted); }

    @media (max-width: 980px) {
      .span-5, .span-7, .span-12 { grid-column: span 12; }
    }

    @media (max-width: 680px) {
      .shell { width: min(100% - 20px, 1180px); padding-top: 20px; }
      .hero, .card-header, .card-body { padding-left: 16px; padding-right: 16px; }
      .row { grid-template-columns: 1fr; }
      .pending-meta { flex-direction: column; align-items: flex-start; }
    }
  </style>
</head>
<body>
  <div class="shell">
    <section class="hero">
      <div class="eyebrow">Online Address Book</div>
      <h1>学生账号控制台</h1>
      <p class="subtitle">
        这个页面可以直接操作后端接口，完成登录、注册、查看待审核学生以及管理员审核操作。
        所有请求都通过当前应用的 `/api/...` 路由发送。
      </p>
    </section>

    <section class="grid">
      <article class="card span-5">
        <div class="card-header">
          <h2 class="card-title">登录</h2>
          <p class="card-subtitle">调用 <code>/api/auth/login</code>。</p>
        </div>
        <div class="card-body">
          <form id="loginForm">
            <label>
              用户名
              <input name="username" value="student" autocomplete="username" />
            </label>
            <label>
              密码
              <input name="password" type="password" value="123456" autocomplete="current-password" />
            </label>
            <div class="actions">
              <button type="submit">登录</button>
              <button type="button" class="secondary" id="fillLogin">填充示例</button>
            </div>
          </form>
        </div>
      </article>

      <article class="card span-7">
        <div class="card-header">
          <h2 class="card-title">注册</h2>
          <p class="card-subtitle">调用 <code>/api/students/register</code>，注册后默认进入待审核状态。</p>
        </div>
        <div class="card-body">
          <form id="registerForm">
            <div class="row">
              <label>
                用户名
                <input name="username" placeholder="new-student" />
              </label>
              <label>
                密码
                <input name="password" type="password" placeholder="请输入密码" />
              </label>
            </div>
            <div class="actions">
              <button type="submit">注册</button>
              <button type="button" class="secondary" id="fillRegister">填充示例</button>
            </div>
          </form>
        </div>
      </article>

      <article class="card span-12">
        <div class="card-header">
          <h2 class="card-title">待审核列表</h2>
          <p class="card-subtitle">调用 <code>/api/admin/audits/students/pending</code>，只显示待审核学生。</p>
        </div>
        <div class="card-body">
          <div class="actions" style="margin-bottom: 12px;">
            <button type="button" id="loadPending">刷新待审核列表</button>
            <button type="button" class="secondary" id="showPendingRaw">查看原始返回</button>
          </div>
          <div id="pendingList" class="pending-list">
            <div class="muted">点击“刷新待审核列表”后显示结果。</div>
          </div>
        </div>
      </article>

      <article class="card span-12">
        <div class="card-header">
          <h2 class="card-title">接口输出</h2>
          <p class="card-subtitle">请求结果会显示在这里，便于快速排查问题。</p>
        </div>
        <div class="card-body">
          <div id="output" class="output">等待操作...</div>
        </div>
      </article>
    </section>
  </div>

  <script>
    var outputEl = document.getElementById('output');
    var pendingListEl = document.getElementById('pendingList');
    var lastPendingPayload = null;

    function writeOutput(value) {
      outputEl.textContent = typeof value === 'string' ? value : JSON.stringify(value, null, 2);
    }

    function parseResponse(response) {
      var contentType = response.headers.get('content-type') || '';
      if (contentType.indexOf('application/json') >= 0) {
        return response.json();
      }
      return response.text();
    }

    function requestJson(url, options) {
      return fetch(url, options).then(function (response) {
        return parseResponse(response).then(function (body) {
          if (!response.ok) {
            var error = new Error('Request failed');
            error.status = response.status;
            error.body = body;
            throw error;
          }
          return body;
        });
      });
    }

    function escapeHtml(value) {
      return String(value)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
    }

    function renderPendingList(items) {
      if (!items || !items.length) {
        pendingListEl.innerHTML = '<div class="muted">当前没有待审核学生。</div>';
        return;
      }

      pendingListEl.innerHTML = items.map(function (item) {
        return [
          '<div class="pending-item" data-student-id="' + item.studentId + '">',
          '  <div class="pending-meta">',
          '    <div>',
          '      <div class="pending-title">#' + item.studentId + ' ' + escapeHtml(item.username || '') + '</div>',
          '      <div class="mini">状态：' + escapeHtml(item.auditStatus || '') + '</div>',
          '    </div>',
          '    <div class="pending-badge">' + escapeHtml(item.auditStatus || 'PENDING') + '</div>',
          '  </div>',
          '  <div class="actions">',
          '    <button type="button" class="success" data-action="approve">通过</button>',
          '    <button type="button" class="danger" data-action="reject">拒绝</button>',
          '  </div>',
          '</div>'
        ].join('');
      }).join('');
    }

    document.getElementById('loginForm').addEventListener('submit', function (event) {
      event.preventDefault();
      var form = event.target;
      requestJson('api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          username: form.username.value,
          password: form.password.value
        })
      }).then(writeOutput).catch(function (error) {
        writeOutput({ status: error.status || 0, body: error.body });
      });
    });

    document.getElementById('registerForm').addEventListener('submit', function (event) {
      event.preventDefault();
      var form = event.target;
      requestJson('api/students/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          username: form.username.value,
          password: form.password.value
        })
      }).then(writeOutput).catch(function (error) {
        writeOutput({ status: error.status || 0, body: error.body });
      });
    });

    function loadPending() {
      requestJson('api/admin/audits/students/pending', { method: 'GET' })
        .then(function (body) {
          lastPendingPayload = body;
          renderPendingList(body);
          writeOutput(body);
        })
        .catch(function (error) {
          pendingListEl.innerHTML = '<div class="muted">加载失败，请查看下方输出。</div>';
          writeOutput({ status: error.status || 0, body: error.body });
        });
    }

    pendingListEl.addEventListener('click', function (event) {
      var button = event.target;
      if (!button || !button.matches('button[data-action]')) {
        return;
      }
      var item = button.closest('.pending-item');
      if (!item) {
        return;
      }
      var studentId = item.getAttribute('data-student-id');
      var action = button.getAttribute('data-action');
      requestJson('api/admin/audits/students/' + studentId + '/' + action, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
      }).then(function (body) {
        writeOutput(body);
        loadPending();
      }).catch(function (error) {
        writeOutput({ status: error.status || 0, body: error.body });
      });
    });

    document.getElementById('loadPending').addEventListener('click', loadPending);
    document.getElementById('showPendingRaw').addEventListener('click', function () {
      if (lastPendingPayload) {
        writeOutput(lastPendingPayload);
      } else {
        writeOutput('还没有加载过待审核列表。');
      }
    });
    document.getElementById('fillLogin').addEventListener('click', function () {
      document.querySelector('#loginForm input[name="username"]').value = 'student';
      document.querySelector('#loginForm input[name="password"]').value = '123456';
    });
    document.getElementById('fillRegister').addEventListener('click', function () {
      document.querySelector('#registerForm input[name="username"]').value = 'student-' + Date.now().toString().slice(-4);
      document.querySelector('#registerForm input[name="password"]').value = '123456';
    });
  </script>
</body>
</html>
