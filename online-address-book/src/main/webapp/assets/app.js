(function () {
  "use strict";

  var PAGE_MAP = {
    login: "index.jsp",
    register: "register.jsp",
    "admin-login": "admin-login.jsp",
    "admin-register": "admin-register.jsp",
    "admin-audit": "admin-audit.jsp",
    search: "search.jsp",
    profile: "profile.jsp"
  };

  var STUDENT_AUTH_KEY = "onlineAddressBook.student.auth";
  var ADMIN_SESSION_KEY = "onlineAddressBook.admin.session";

  function byId(id) {
    return document.getElementById(id);
  }

  function contextPath() {
    return document.body && document.body.dataset ? document.body.dataset.context || "" : "";
  }

  function pageUrl(name) {
    return contextPath() + "/" + PAGE_MAP[name];
  }

  function redirectTo(page) {
    window.location.href = pageUrl(page);
  }

  function confirmAction(message) {
    return window.confirm(message);
  }

  function apiUrl(path) {
    return contextPath() + "/api" + path;
  }

  function escapeHtml(value) {
    return String(value == null ? "" : value)
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;")
      .replace(/'/g, "&#39;");
  }

  function parseJson(raw) {
    try {
      return JSON.parse(raw || "null");
    } catch (error) {
      return null;
    }
  }

  function readStorage(key) {
    return parseJson(localStorage.getItem(key));
  }

  function writeStorage(key, value) {
    localStorage.setItem(key, JSON.stringify(value));
  }

  function setNotice(id, kind, message) {
    var el = byId(id);
    if (!el) {
      return;
    }
    el.className = "notice notice-" + kind;
    el.textContent = message;
  }

  function loadStudentAuth() {
    return readStorage(STUDENT_AUTH_KEY);
  }

  function saveStudentAuth(auth) {
    writeStorage(STUDENT_AUTH_KEY, auth);
  }

  function clearStudentAuth() {
    localStorage.removeItem(STUDENT_AUTH_KEY);
  }

  function getStudentToken() {
    var auth = loadStudentAuth();
    return auth && auth.accessToken ? auth.accessToken : "";
  }

  function loadAdminSession() {
    return readStorage(ADMIN_SESSION_KEY);
  }

  function saveAdminSession(session) {
    writeStorage(ADMIN_SESSION_KEY, session);
  }

  function clearAdminSession() {
    localStorage.removeItem(ADMIN_SESSION_KEY);
  }

  function isSuperAdminSession(session) {
    return session && session.username === "gl1";
  }

  function request(path, options) {
    options = options || {};
    var headers = options.headers ? Object.assign({}, options.headers) : {};
    var body = options.body;

    if (options.auth) {
      var token = getStudentToken();
      if (token) {
        headers.Authorization = "Bearer " + token;
      }
    }

    if (body && !(body instanceof FormData)) {
      headers["Content-Type"] = "application/json";
    }

    var fetchOptions = {
      method: options.method || "GET",
      headers: headers
    };
    if (body) {
      fetchOptions.body = body instanceof FormData ? body : JSON.stringify(body);
    }

    return fetch(apiUrl(path), fetchOptions).then(function (response) {
      return response.text().then(function (text) {
        var payload = text;
        try {
          payload = text ? JSON.parse(text) : null;
        } catch (error) {
          payload = text;
        }
        return {
          ok: response.ok,
          status: response.status,
          payload: payload,
          text: text
        };
      });
    }).catch(function (error) {
      return {
        ok: false,
        status: 0,
        payload: null,
        text: error && error.message ? error.message : "网络错误"
      };
    });
  }

  function attachNavigation() {
    document.querySelectorAll("[data-go]").forEach(function (button) {
      button.addEventListener("click", function () {
        window.location.href = pageUrl(button.dataset.go);
      });
    });
  }

  function renderStudentSessionSummary() {
    var el = byId("loginSessionSummary");
    if (!el) {
      return;
    }
    var auth = loadStudentAuth();
    if (!auth || !auth.accessToken) {
      el.textContent = "尚未登录";
      return;
    }
    el.innerHTML = "<div>已登录：<strong>" + escapeHtml(auth.username || "未知") + "</strong></div><div class='muted'>学生访问令牌已保存在本地</div>";
  }

  function renderAdminSessionSummary() {
    var el = byId("adminSessionSummary");
    if (!el) {
      return;
    }
    var session = loadAdminSession();
    if (!session) {
      el.textContent = "尚未登录";
      return;
    }
    el.innerHTML = "<div>管理员：<strong>" + escapeHtml(session.displayName || session.username || "未知") + "</strong></div><div class='muted'>账号：" + escapeHtml(session.username || "-") + "</div>";
  }

  function renderLoginInfo(data) {
    var panel = byId("loginInfoPanel");
    if (!panel) {
      return;
    }
    var values = panel.querySelectorAll(".info-value");
    if (values.length < 3) {
      return;
    }
    values[0].textContent = data && data.loginCount != null ? String(data.loginCount) : "-";
    values[1].textContent = data && data.lastLoginTime ? data.lastLoginTime : "-";
    values[2].textContent = data && data.accessToken ? data.accessToken : "-";
  }

  function renderProfileLoginInfo(data) {
    var panel = byId("profileLoginInfo");
    if (!panel) {
      return;
    }
    var values = panel.querySelectorAll(".info-value");
    if (values.length < 2) {
      return;
    }
    values[0].textContent = data && data.loginCount != null ? String(data.loginCount) : "-";
    values[1].textContent = data && data.lastLoginTime ? data.lastLoginTime : "-";
  }

  function renderTable(containerId, columns, rows, emptyText) {
    var container = byId(containerId);
    if (!container) {
      return;
    }
    if (!rows || !rows.length) {
      container.className = "table-wrap empty-state";
      container.textContent = emptyText || "暂无数据。";
      return;
    }

    var html = ["<table><thead><tr>"];
    columns.forEach(function (column) {
      html.push("<th>" + escapeHtml(column.label) + "</th>");
    });
    html.push("</tr></thead><tbody>");

    rows.forEach(function (row, rowIndex) {
      html.push("<tr>");
      columns.forEach(function (column) {
        var value = typeof column.render === "function" ? column.render(row, rowIndex) : row[column.key];
        html.push("<td>" + (value == null ? "" : value) + "</td>");
      });
      html.push("</tr>");
    });

    html.push("</tbody></table>");
    container.className = "table-wrap";
    container.innerHTML = html.join("");
  }

  function storeStudentLogin(payload, username) {
    saveStudentAuth({
      accessToken: payload.accessToken,
      refreshToken: payload.refreshToken,
      username: username,
      loginCount: payload.loginCount,
      lastLoginTime: payload.lastLoginTime
    });
  }

  function handleStudentAuthError(result, noticeId) {
    if (result.status === 401) {
      clearStudentAuth();
    }
    if (noticeId) {
      var message = result.payload && result.payload.message ? result.payload.message : result.text || "请求失败";
      setNotice(noticeId, "danger", message);
    }
  }

  function auditStatusLabel(status) {
    var map = {
      PENDING: "待审核",
      APPROVED: "已通过",
      REJECTED: "已拒绝",
      DISABLED: "已禁用"
    };
    var key = String(status || "").toUpperCase();
    return map[key] || status || "-";
  }

  var LOGIN_STATUS_HINT = "请使用审核通过的学生账号登录。";

  function setLoginStatusHint() {
    setNotice("loginStatus", "info", LOGIN_STATUS_HINT);
  }

  function initLoginPage() {
    attachNavigation();
    renderStudentSessionSummary();
    setLoginStatusHint();

    var form = byId("loginForm");
    if (form) {
      form.addEventListener("submit", function (event) {
        event.preventDefault();
        var username = byId("loginUsername").value.trim();
        var password = byId("loginPassword").value;

        request("/auth/login", {
          method: "POST",
          body: { username: username, password: password }
        }).then(function (result) {
          if (!result.ok) {
            handleStudentAuthError(result, "loginStatus");
            renderLoginInfo(null);
            return;
          }
          storeStudentLogin(result.payload, username);
          redirectTo("profile");
        });
      });
    }

    var demoBtn = byId("loginDemoBtn");
    if (demoBtn) {
      demoBtn.addEventListener("click", function () {
        byId("loginUsername").value = "student";
        byId("loginPassword").value = "123456";
        setNotice("loginStatus", "info", "示例学生已填充");
      });
    }

    if (getStudentToken()) {
      redirectTo("profile");
    }
  }

  function loadStudentLoginInfo() {
    request("/students/me/login-info", { method: "GET", auth: true }).then(function (result) {
      if (!result.ok) {
        handleStudentAuthError(result, "loginStatus");
        return;
      }
      var auth = loadStudentAuth() || {};
      auth.loginCount = result.payload.loginCount;
      auth.lastLoginTime = result.payload.lastLoginTime;
      saveStudentAuth(auth);
      renderStudentSessionSummary();
      renderLoginInfo(auth);
    });
  }

  function initRegisterPage() {
    attachNavigation();
    var form = byId("registerForm");
    if (form) {
      form.addEventListener("submit", function (event) {
        event.preventDefault();
        var username = byId("registerUsername").value.trim();
        var password = byId("registerPassword").value;
        var confirm = byId("registerPasswordConfirm").value;
        if (!username || !password) {
          setNotice("registerStatus", "warning", "请填写登录账号和密码。");
          return;
        }
        if (password !== confirm) {
          setNotice("registerStatus", "warning", "两次输入的密码不一致。");
          return;
        }
        setNotice("registerStatus", "info", "正在提交注册...");
        request("/students/register", {
          method: "POST",
          body: { username: username, password: password }
        }).then(function (result) {
          if (!result.ok) {
            setNotice("registerStatus", "danger", result.payload && result.payload.message ? result.payload.message : "注册失败。");
            return;
          }
          setNotice("registerStatus", "success", "注册成功，当前状态：" + escapeHtml(auditStatusLabel(result.payload.auditStatus || "PENDING")));
          form.reset();
          byId("registerUsername").value = username;
        });
      });
    }

    var demoBtn = byId("registerDemoBtn");
    if (demoBtn) {
      demoBtn.addEventListener("click", function () {
        byId("registerUsername").value = "new-student";
        byId("registerPassword").value = "123456";
        byId("registerPasswordConfirm").value = "123456";
        setNotice("registerStatus", "info", "示例学生注册已填充");
      });
    }
  }

  function initAdminLoginPage() {
    attachNavigation();
    renderAdminSessionSummary();

    var form = byId("adminLoginForm");
    if (form) {
      form.addEventListener("submit", function (event) {
        event.preventDefault();
        var username = byId("adminLoginUsername").value.trim();
        var password = byId("adminLoginPassword").value;
        if (!username || !password) {
          setNotice("adminLoginStatus", "warning", "请填写管理员账号和密码。");
          return;
        }
        request("/admin/auth/login", {
          method: "POST",
          body: { username: username, password: password }
        }).then(function (result) {
          if (!result.ok || !result.payload || !result.payload.success) {
            var message = result.payload && result.payload.message ? result.payload.message : "登录失败";
            setNotice("adminLoginStatus", "danger", message);
            return;
          }
          var payload = result.payload;
          saveAdminSession({
            id: payload.id,
            username: payload.username,
            displayName: payload.displayName,
            auditStatus: payload.auditStatus
          });
          setNotice("adminLoginStatus", "success", "登录成功，正在进入管理员审核页面...");
          redirectTo("admin-audit");
        });
      });
    }

    var demoBtn = byId("adminLoginDemoBtn");
    if (demoBtn) {
      demoBtn.addEventListener("click", function () {
        byId("adminLoginUsername").value = "gl1";
        byId("adminLoginPassword").value = "123456";
        setNotice("adminLoginStatus", "info", "示例管理员已填充");
      });
    }

    if (loadAdminSession()) {
      redirectTo("admin-audit");
    }
  }

  function initAdminRegisterPage() {
    attachNavigation();
    renderAdminSessionSummary();

    var form = byId("adminRegisterForm");
    if (form) {
      form.addEventListener("submit", function (event) {
        event.preventDefault();
        var username = byId("adminRegisterUsername").value.trim();
        var password = byId("adminRegisterPassword").value;
        var passwordConfirm = byId("adminRegisterPasswordConfirm").value;
        var displayName = byId("adminRegisterDisplayName").value.trim();

        if (!username || !password) {
          setNotice("adminRegisterStatus", "warning", "请填写管理员账号和密码。");
          setNotice("adminRegisterFormStatus", "warning", "请填写管理员账号和密码。");
          return;
        }
        if (password !== passwordConfirm) {
          setNotice("adminRegisterStatus", "warning", "两次输入的密码不一致。");
          setNotice("adminRegisterFormStatus", "warning", "两次输入的密码不一致。");
          return;
        }

        setNotice("adminRegisterStatus", "info", "正在提交注册...");
        setNotice("adminRegisterFormStatus", "info", "正在提交注册...");
        request("/admin/auth/register", {
          method: "POST",
          body: { username: username, password: password, displayName: displayName }
        }).then(function (result) {
          if (!result.ok) {
            var message = result.payload && result.payload.message
              ? result.payload.message
              : (result.status === 0 ? "无法连接服务器，请确认 Tomcat 与数据库已启动。" : "注册失败，请稍后重试。");
            setNotice("adminRegisterStatus", "danger", message);
            setNotice("adminRegisterFormStatus", "danger", message);
            return;
          }
          var successMessage = "注册已提交，请等待 gl1 审核通过后再登录。可通过右侧「已有账号？去管理员登录」进入登录页。";
          setNotice("adminRegisterStatus", "success", successMessage);
          setNotice("adminRegisterFormStatus", "success", successMessage);
          form.reset();
        }).catch(function () {
          setNotice("adminRegisterStatus", "danger", "注册请求失败，请检查网络或后端服务。");
          setNotice("adminRegisterFormStatus", "danger", "注册请求失败，请检查网络或后端服务。");
        });
      });
    }

    var demoBtn = byId("adminRegisterDemoBtn");
    if (demoBtn) {
      demoBtn.addEventListener("click", function () {
        byId("adminRegisterUsername").value = "admin2";
        byId("adminRegisterPassword").value = "123456";
        byId("adminRegisterPasswordConfirm").value = "123456";
        byId("adminRegisterDisplayName").value = "管理员二号";
        setNotice("adminRegisterStatus", "info", "已填充示例账号。");
        setNotice("adminRegisterFormStatus", "info", "已填充示例账号。");
      });
    }
  }

  function renderStudentAuditRows(rows, mode) {
    return rows.map(function (row) {
      var actions = [];
      if (mode === "pending") {
        actions.push("<button type='button' class='success' data-approve='" + row.studentId + "'>通过</button>");
        actions.push("<button type='button' class='danger' data-reject='" + row.studentId + "'>拒绝</button>");
      } else if (mode === "unapproved") {
        actions.push("<button type='button' class='danger' data-delete-student='" + row.id + "'>删除</button>");
      } else if (mode === "approved") {
        actions.push("<button type='button' class='danger' data-disable-student='" + row.id + "'>禁用</button>");
      } else if (mode === "disabled") {
        actions.push("<button type='button' class='success' data-enable-student='" + row.id + "'>启用</button>");
      }
      return {
        id: row.id || row.studentId,
        studentId: row.studentId || row.id,
        username: escapeHtml(row.username || "-"),
        auditStatus: "<span class='tag tag-" + String(row.auditStatus || "").toLowerCase() + "'>" + escapeHtml(auditStatusLabel(row.auditStatus)) + "</span>",
        major: escapeHtml(row.major || "-"),
        className: escapeHtml(row.className || "-"),
        enrollmentYear: row.enrollmentYear == null ? "-" : String(row.enrollmentYear),
        actions: "<div class='row-actions'>" + actions.join("") + "</div>"
      };
    });
  }

  function renderProfessionalRows(rows) {
    return rows.map(function (row) {
      return {
        id: row.id,
        name: escapeHtml(row.name || ""),
        actions: "<div class='row-actions'>" +
          "<button type='button' class='secondary' data-edit-professional='" + row.id + "' data-name='" + escapeHtml(row.name || "") + "'>编辑</button>" +
          "<button type='button' class='danger' data-delete-professional='" + row.id + "'>删除</button>" +
          "</div>"
      };
    });
  }

  function renderAdminRows(rows, canAudit) {
    return rows.map(function (row) {
      var actions = [];
      if (canAudit && row.auditStatus === "PENDING") {
        actions.push("<button type='button' class='success' data-approve-admin='" + row.id + "'>通过</button>");
        actions.push("<button type='button' class='danger' data-reject-admin='" + row.id + "'>拒绝</button>");
      }
      return {
        id: row.id,
        username: escapeHtml(row.username || "-"),
        displayName: escapeHtml(row.displayName || "-"),
        auditStatus: "<span class='tag tag-" + String(row.auditStatus || "").toLowerCase() + "'>" + escapeHtml(auditStatusLabel(row.auditStatus)) + "</span>",
        createdAt: escapeHtml(row.createdAt || "-"),
        reviewedBy: escapeHtml(row.reviewedBy || "-"),
        reviewedAt: escapeHtml(row.reviewedAt || "-"),
        actions: "<div class='row-actions'>" + actions.join("") + "</div>"
      };
    });
  }

  function bindStudentAuditActions(root) {
    root.querySelectorAll("[data-approve]").forEach(function (button) {
      button.addEventListener("click", function () {
        actStudentAudit("/admin/audits/students/" + button.dataset.approve + "/approve", "通过");
      });
    });
    root.querySelectorAll("[data-reject]").forEach(function (button) {
      button.addEventListener("click", function () {
        actStudentAudit("/admin/audits/students/" + button.dataset.reject + "/reject", "拒绝");
      });
    });
    root.querySelectorAll("[data-delete-student]").forEach(function (button) {
      button.addEventListener("click", function () {
        actStudentAdmin("/admin/students/" + button.dataset.deleteStudent, "删除", "DELETE");
      });
    });
    root.querySelectorAll("[data-disable-student]").forEach(function (button) {
      button.addEventListener("click", function () {
        actStudentAdmin("/admin/students/" + button.dataset.disableStudent + "/disable", "禁用", "POST");
      });
    });
    root.querySelectorAll("[data-enable-student]").forEach(function (button) {
      button.addEventListener("click", function () {
        actStudentAdmin("/admin/students/" + button.dataset.enableStudent + "/enable", "启用", "POST");
      });
    });
    root.querySelectorAll("[data-edit-professional]").forEach(function (button) {
      button.addEventListener("click", function () {
        byId("professionalId").value = button.dataset.editProfessional;
        byId("professionalName").value = button.dataset.name || "";
        byId("professionalSubmitBtn").textContent = "保存";
      });
    });
    root.querySelectorAll("[data-delete-professional]").forEach(function (button) {
      button.addEventListener("click", function () {
        actProfessional("/admin/professionals/" + button.dataset.deleteProfessional, "删除", "DELETE");
      });
    });
  }

  function bindAdminAuditActions(root) {
    root.querySelectorAll("[data-approve-admin]").forEach(function (button) {
      button.addEventListener("click", function () {
        actAdminAudit(button.dataset.approveAdmin, "approve");
      });
    });
    root.querySelectorAll("[data-reject-admin]").forEach(function (button) {
      button.addEventListener("click", function () {
        actAdminAudit(button.dataset.rejectAdmin, "reject");
      });
    });
  }

  function actStudentAudit(path, label) {
    if (!confirmAction("确定要" + label + "该学生账号吗？")) {
      return;
    }
    request(path, { method: "POST" }).then(function (result) {
      if (!result.ok) {
        setNotice("adminStatus", "danger", result.payload && result.payload.message ? result.payload.message : label + "失败。");
        return;
      }
      setNotice("adminStatus", "success", label + "成功。");
      loadAdminStudentData();
    });
  }

  function actStudentAdmin(path, label, method) {
    if (!confirmAction("确定要" + label + "该学生账号吗？此操作可能无法撤销。")) {
      return;
    }
    request(path, { method: method }).then(function (result) {
      if (!result.ok) {
        setNotice("adminStatus", "danger", result.payload && result.payload.message ? result.payload.message : label + "失败。");
        return;
      }
      setNotice("adminStatus", "success", label + "成功。");
      loadAdminStudentData();
    });
  }

  function actProfessional(path, label, method, body) {
    if (method === "DELETE" && !confirmAction("确定要删除该专业吗？若已有学生使用将无法删除。")) {
      return;
    }
    request(path, { method: method, body: body }).then(function (result) {
      if (!result.ok) {
        setNotice("adminStatus", "danger", result.payload && result.payload.message ? result.payload.message : label + "失败。");
        return;
      }
      setNotice("adminStatus", "success", label + "成功。");
      resetProfessionalForm();
      loadAdminStudentData();
    });
  }

  function actAdminAudit(id, action) {
    var session = loadAdminSession();
    if (!isSuperAdminSession(session)) {
      setNotice("adminStatus", "warning", "仅 gl1 可审核管理员注册申请。");
      return;
    }
    var label = action === "approve" ? "通过" : "拒绝";
    if (!confirmAction("确定要" + label + "该管理员注册申请吗？")) {
      return;
    }
    var path = "/admin/audits/admins/" + id + (action === "approve" ? "/approve" : "/reject");
    request(path, {
      method: "POST",
      body: { reviewerUsername: session.username }
    }).then(function (result) {
      if (!result.ok) {
        var message = result.payload && result.payload.message ? result.payload.message : label + "失败。";
        setNotice("adminStatus", "danger", message);
        return;
      }
      setNotice("adminStatus", "success", "管理员审核已完成。");
      loadAdminReviewData();
    });
  }

  function resetProfessionalForm() {
    var id = byId("professionalId");
    var name = byId("professionalName");
    var submit = byId("professionalSubmitBtn");
    if (id) {
      id.value = "";
    }
    if (name) {
      name.value = "";
    }
    if (submit) {
      submit.textContent = "新增专业";
    }
  }

  function loadAdminReviewData() {
    var session = loadAdminSession();
    var hint = byId("adminAuditHint");
    var container = byId("pendingAdminUsers");
    if (!session) {
      if (hint) {
        setNotice("adminAuditHint", "warning", "请先登录管理员账号。");
      }
      if (container) {
        container.className = "table-wrap empty-state";
        container.textContent = "请先登录管理员账号。";
      }
      return;
    }

    renderAdminSessionSummary();

    var canAuditAdmins = isSuperAdminSession(session);
    if (hint) {
      setNotice("adminAuditHint", canAuditAdmins ? "info" : "warning", canAuditAdmins ? "gl1 可审核管理员注册与学生注册申请。" : "当前管理员不是 gl1，仅可审核学生注册。");
    }

    request("/admin/audits/admins/pending", { method: "GET" }).then(function (result) {
      var pendingAdmins = [];
      if (result.ok && result.payload && result.payload.admins) {
        pendingAdmins = result.payload.admins;
      } else if (!result.ok) {
        setNotice("adminStatus", "danger", "加载待审核管理员列表失败。");
      }
      renderTable("pendingAdminUsers", [
        { label: "编号", key: "id" },
        { label: "账号", key: "username" },
        { label: "显示名称", key: "displayName" },
        { label: "状态", key: "auditStatus" },
        { label: "注册时间", key: "createdAt" },
        { label: "审核人", key: "reviewedBy" },
        { label: "审核时间", key: "reviewedAt" },
        { label: "操作", key: "actions" }
      ], renderAdminRows(pendingAdmins, canAuditAdmins), "暂无待审核管理员。");
      bindAdminAuditActions(document);
    });
  }

  function loadAdminStudentData() {
    var pendingReq = request("/admin/audits/students/pending", { method: "GET" });
    var unapprovedReq = request("/admin/students/unapproved", { method: "GET" });
    var approvedReq = request("/admin/students/approved", { method: "GET" });
    var disabledReq = request("/admin/students/disabled", { method: "GET" });
    var professionalReq = request("/admin/professionals", { method: "GET" });

    Promise.all([pendingReq, unapprovedReq, approvedReq, disabledReq, professionalReq]).then(function (results) {
      var pending = results[0];
      var unapproved = results[1];
      var approved = results[2];
      var disabled = results[3];
      var professional = results[4];

      if (!pending.ok || !unapproved.ok || !approved.ok || !disabled.ok || !professional.ok) {
        setNotice("adminStatus", "danger", "加载管理数据失败，请检查后端服务。");
        return;
      }

      renderTable("pendingStudents", [
        { label: "编号", key: "studentId" },
        { label: "账号", key: "username" },
        { label: "状态", key: "auditStatus" },
        { label: "操作", key: "actions" }
      ], renderStudentAuditRows(pending.payload || [], "pending"), "暂无待审核学生。");

      renderTable("unapprovedStudents", [
        { label: "编号", key: "id" },
        { label: "账号", key: "username" },
        { label: "状态", key: "auditStatus" },
        { label: "专业", key: "major" },
        { label: "班级", key: "className" },
        { label: "入学年份", key: "enrollmentYear" },
        { label: "操作", key: "actions" }
      ], renderStudentAuditRows((unapproved.payload && unapproved.payload.students) || [], "unapproved"), "暂无未通过审核的学生。");

      renderTable("approvedStudents", [
        { label: "编号", key: "id" },
        { label: "账号", key: "username" },
        { label: "状态", key: "auditStatus" },
        { label: "专业", key: "major" },
        { label: "班级", key: "className" },
        { label: "入学年份", key: "enrollmentYear" },
        { label: "操作", key: "actions" }
      ], renderStudentAuditRows((approved.payload && approved.payload.students) || [], "approved"), "暂无已通过审核的学生。");

      renderTable("disabledStudents", [
        { label: "编号", key: "id" },
        { label: "账号", key: "username" },
        { label: "状态", key: "auditStatus" },
        { label: "专业", key: "major" },
        { label: "班级", key: "className" },
        { label: "入学年份", key: "enrollmentYear" },
        { label: "操作", key: "actions" }
      ], renderStudentAuditRows((disabled.payload && disabled.payload.students) || [], "disabled"), "暂无已禁用学生。");

      renderTable("professionalList", [
        { label: "编号", key: "id" },
        { label: "专业名称", key: "name" },
        { label: "操作", key: "actions" }
      ], renderProfessionalRows((professional.payload && professional.payload.professionals) || professional.payload || []), "暂无专业数据。");

      bindStudentAuditActions(document);
    });
  }

  function initAdminAuditPage() {
    if (!loadAdminSession()) {
      redirectTo("admin-login");
      return;
    }
    attachNavigation();
    renderAdminSessionSummary();
    var form = byId("professionalForm");
    if (form) {
      form.addEventListener("submit", function (event) {
        event.preventDefault();
        var id = byId("professionalId").value.trim();
        var name = byId("professionalName").value.trim();
        if (!name) {
          setNotice("adminStatus", "warning", "请填写专业名称。");
          return;
        }
        if (id) {
          actProfessional("/admin/professionals/" + id, "更新专业", "PUT", { name: name });
        } else {
          actProfessional("/admin/professionals", "新增专业", "POST", { name: name });
        }
      });
    }

    var resetBtn = byId("professionalResetBtn");
    if (resetBtn) {
      resetBtn.addEventListener("click", function () {
        resetProfessionalForm();
        setNotice("adminStatus", "info", "专业表单已重置。");
      });
    }

    var logoutBtn = byId("adminAuditLogoutBtn");
    if (logoutBtn) {
      logoutBtn.addEventListener("click", function () {
        clearAdminSession();
        redirectTo("admin-login");
      });
    }

    loadAdminReviewData();
    loadAdminStudentData();
  }

  function fillProfileForm(data) {
    byId("profileMajor").value = data.major || "";
    byId("profileClassName").value = data.className || "";
    byId("profileEnrollmentYear").value = data.enrollmentYear == null ? "" : data.enrollmentYear;
    byId("profileJobUnit").value = data.jobUnit || "";
    byId("profileCity").value = data.city || "";
    byId("profileContactMethod").value = data.contactMethod || "";
    byId("profileEmail").value = data.email || "";
  }

  function loadProfile() {
    request("/students/me/contact", { method: "GET", auth: true }).then(function (result) {
      if (!result.ok) {
        handleStudentAuthError(result, "profileSummary");
        return;
      }
      fillProfileForm(result.payload || {});
      setNotice("profileSummary", "success", "通讯录已加载。");
    });

    request("/students/me/login-info", { method: "GET", auth: true }).then(function (result) {
      if (!result.ok) {
        return;
      }
      var auth = loadStudentAuth() || {};
      auth.loginCount = result.payload.loginCount;
      auth.lastLoginTime = result.payload.lastLoginTime;
      saveStudentAuth(auth);
      renderProfileLoginInfo(auth);
    });
  }

  function initProfilePage() {
    attachNavigation();
    renderProfileLoginInfo(loadStudentAuth());

    var form = byId("profileForm");
    if (form) {
      form.addEventListener("submit", function (event) {
        event.preventDefault();
        var payload = {
          major: byId("profileMajor").value.trim(),
          className: byId("profileClassName").value.trim(),
          enrollmentYear: byId("profileEnrollmentYear").value.trim() ? Number(byId("profileEnrollmentYear").value.trim()) : null,
          jobUnit: byId("profileJobUnit").value.trim(),
          city: byId("profileCity").value.trim(),
          contactMethod: byId("profileContactMethod").value.trim(),
          email: byId("profileEmail").value.trim()
        };
        setNotice("profileSummary", "info", "正在保存通讯录...");
        request("/students/me/contact", {
          method: "PUT",
          auth: true,
          body: payload
        }).then(function (result) {
          if (!result.ok) {
            handleStudentAuthError(result, "profileSummary");
            return;
          }
          fillProfileForm(result.payload || {});
          setNotice("profileSummary", "success", "通讯录已保存。");
        });
      });
    }

    var reloadBtn = byId("profileReloadBtn");
    if (reloadBtn) {
      reloadBtn.addEventListener("click", function () {
        setNotice("profileSummary", "info", "正在重新加载...");
        loadProfile();
      });
    }

    var logoutBtn = byId("profileLogoutBtn");
    if (logoutBtn) {
      logoutBtn.addEventListener("click", function () {
        clearStudentAuth();
        redirectTo("login");
      });
    }

    if (!getStudentToken()) {
      redirectTo("login");
      return;
    }

    loadProfile();
  }

  function initSearchPage() {
    attachNavigation();
    var form = byId("searchForm");
    var results = byId("searchResults");
    if (form) {
      form.addEventListener("submit", function (event) {
        event.preventDefault();
        var major = byId("searchMajor").value.trim();
        var className = byId("searchClassName").value.trim();
        var year = byId("searchEnrollmentYear").value.trim();
        var params = [];
        if (major) {
          params.push("major=" + encodeURIComponent(major));
        }
        if (className) {
          params.push("className=" + encodeURIComponent(className));
        }
        if (year) {
          params.push("enrollmentYear=" + encodeURIComponent(year));
        }
        setNotice("searchStatus", "info", "正在查询...");
        request("/students/contacts" + (params.length ? "?" + params.join("&") : ""), {
          method: "GET",
          auth: true
        }).then(function (result) {
          if (!result.ok) {
            handleStudentAuthError(result, "searchStatus");
            if (results) {
              results.className = "table-wrap empty-state";
              results.textContent = "暂无查询结果。";
            }
            return;
          }
          var students = result.payload && result.payload.students ? result.payload.students : [];
          setNotice("searchStatus", "success", "共找到 " + students.length + " 条记录。");
          renderTable("searchResults", [
            { label: "账号", key: "username" },
            { label: "专业", key: "major" },
            { label: "班级", key: "className" },
            { label: "入学年份", key: "enrollmentYear" },
            { label: "就业单位", key: "jobUnit" },
            { label: "城市", key: "city" },
            { label: "联系方式", key: "contactMethod" },
            { label: "邮箱", key: "email" }
          ], students.map(function (row) {
            return {
              username: escapeHtml(row.username || "-"),
              major: escapeHtml(row.major || "-"),
              className: escapeHtml(row.className || "-"),
              enrollmentYear: row.enrollmentYear == null ? "-" : String(row.enrollmentYear),
              jobUnit: escapeHtml(row.jobUnit || "-"),
              city: escapeHtml(row.city || "-"),
              contactMethod: escapeHtml(row.contactMethod || "-"),
              email: escapeHtml(row.email || "-")
            };
          }), "暂无查询结果。");
        });
      });
    }

    var clearBtn = byId("searchClearBtn");
    if (clearBtn) {
      clearBtn.addEventListener("click", function () {
        form.reset();
        setNotice("searchStatus", "info", "筛选条件已清空。");
        if (results) {
          results.className = "table-wrap empty-state";
          results.textContent = "暂无查询结果。";
        }
      });
    }

    if (!getStudentToken()) {
      redirectTo("login");
      return;
    }
    setNotice("searchStatus", "info", "已登录，可以按条件查询同学通讯录。");
  }

  function boot() {
    attachNavigation();
    var page = document.body && document.body.dataset ? document.body.dataset.page : "";
    if (page === "login") {
      initLoginPage();
      return;
    }
    if (page === "register") {
      initRegisterPage();
      return;
    }
    if (page === "admin-login") {
      initAdminLoginPage();
      return;
    }
    if (page === "admin-register") {
      initAdminRegisterPage();
      return;
    }
    if (page === "admin-audit") {
      initAdminAuditPage();
      return;
    }
    if (page === "search") {
      initSearchPage();
      return;
    }
    if (page === "profile") {
      initProfilePage();
    }
  }

  document.addEventListener("DOMContentLoaded", boot);
})();
