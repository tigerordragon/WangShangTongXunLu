package com.addressbook.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** 把学生认证 HTTP 请求转发给控制器。 */
public class StudentAuthServlet extends HttpServlet {
    private StudentAuthController controller;

    /** 初始化认证控制器。 */
    @Override
    public void init() throws ServletException {
        controller = ApplicationContext.getInstance().getStudentAuthController();
    }

    /** 处理登录、续期和退出请求。 */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonResponse jsonResponse;
        String path = request.getPathInfo();
        if ("/login".equals(path)) {
            jsonResponse = controller.login(readForm(request));
        } else if ("/refresh".equals(path)) {
            jsonResponse = controller.refresh(readForm(request));
        } else if ("/logout".equals(path)) {
            jsonResponse = controller.logout(readForm(request));
        } else {
            jsonResponse = new JsonResponse(404, JsonUtil.error("接口不存在"));
        }
        writeJson(response, jsonResponse);
    }

    /** 读取表单参数。 */
    private Map<String, String> readForm(HttpServletRequest request) {
        Map<String, String> values = new HashMap<String, String>();
        values.put("username", request.getParameter("username"));
        values.put("password", request.getParameter("password"));
        values.put("refreshToken", request.getParameter("refreshToken"));
        return values;
    }

    /** 输出 JSON 响应。 */
    private void writeJson(HttpServletResponse response, JsonResponse jsonResponse) throws IOException {
        response.setStatus(jsonResponse.getStatusCode());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonResponse.getBody());
    }
}
