package org.example.web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.model.User;

import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Allow access to the admin login page itself
        String loginURI = httpRequest.getContextPath() + "/admin/login";
        if (httpRequest.getRequestURI().equals(loginURI)) {
            chain.doFilter(request, response);
            return;
        }

        boolean isAdmin = false;
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null && "ADMIN".equals(user.getRole())) {
                isAdmin = true;
            }
        }

        if (isAdmin) {
            // User is an admin, allow access to the requested admin page
            chain.doFilter(request, response);
        } else {
            // User is not an admin, redirect them to the regular user login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?error=admin_required");
        }
    }
}