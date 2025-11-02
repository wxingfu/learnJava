package com.weixf.servlet;// LoginServlet.java

import com.weixf.event.EventPublisher;
import com.weixf.event.impl.UserLoginEvent;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class LoginServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // String username = request.getParameter("username");
        String username = "admin";
        // String password = request.getParameter("password");
        String password = "password";


        String ipAddress = request.getRemoteAddr();

        // 执行登录验证逻辑
        if (validateUser(username, password)) {
            // 获取事件发布器
            EventPublisher publisher = (EventPublisher) getServletContext().getAttribute("eventPublisher");
            // 发布登录事件
            UserLoginEvent loginEvent = new UserLoginEvent(this, username, ipAddress);
            publisher.publish(loginEvent);

            // 继续处理登录成功逻辑
            HttpSession session = request.getSession();
            session.setAttribute("username", username);

            // response.sendRedirect("welcome.jsp");

        } else {
            // response.sendRedirect("login.jsp?error=1");
        }
    }


    private boolean validateUser(String username, String password) {
        // 实现用户验证逻辑
        return "admin".equals(username) && "password".equals(password);
    }


}
