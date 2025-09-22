package com.codeit.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private static UserDto loginUser;

    @Override
    public void init() throws ServletException {
        loginUser = new UserDto("test1", "1234", "홍길동", 25);
        System.out.println("UserServlet init! 사용자 : " + loginUser);
    }

    // service는 doGet,doPost가 호출되기 전에 항상 호출되는 메서드
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("getRequestURI : " + req.getRequestURI());
        System.out.println("User-Agent : " + req.getHeader("User-Agent"));
        System.out.println("remote IP : " + req.getRemoteAddr());
        super.service(req, resp); // 이거 지우면 큰일남!! -> doGet, doPost 호출하는 코드
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("login.jsp"); // 301
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (loginUser.getUsername().equals(username) && loginUser.getPassword().equals(password)) {
            req.setAttribute("user", loginUser); // spring에서는 model에 데이터 넣는 행위
            RequestDispatcher dispatcher = req.getRequestDispatcher("login-success.jsp");
            dispatcher.forward(req, resp);
        } else {
            req.setAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
            RequestDispatcher dispatcher = req.getRequestDispatcher("login-fail.jsp");
            dispatcher.forward(req, resp);
        }
    }

    @Override
    public void destroy() {
        System.out.println("UserServlet destroy!");
    }
}
