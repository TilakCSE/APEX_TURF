package org.example.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Sport;
import org.example.service.SportService;

import java.io.IOException;

@WebServlet("/admin/sports")
public class AdminSportServlet extends HttpServlet {
    private final SportService sportService = new SportService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        action = (action == null) ? "list" : action;

        try {
            switch (action) {
                case "new":
                    showNewForm(req, resp);
                    break;
                case "edit":
                    showEditForm(req, resp);
                    break;
                case "delete":
                    deleteSport(req, resp);
                    break;
                default:
                    listSports(req, resp);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        String name = req.getParameter("name");
        boolean active = "on".equals(req.getParameter("active"));

        Sport sport = new Sport(name, active);
        if (idStr != null && !idStr.isEmpty()) {
            sport.setId(Long.parseLong(idStr));
        }

        try {
            sportService.saveSport(sport);
            resp.sendRedirect(req.getContextPath() + "/admin/sports");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listSports(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("pageTitle", "Sports");
        req.setAttribute("sportList", sportService.getAllSports());
        req.getRequestDispatcher("/WEB-INF/views/admin/sports.jsp").forward(req, resp);
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("pageTitle", "New Sport");
        req.setAttribute("sport", new Sport()); // Empty sport for the form
        req.getRequestDispatcher("/WEB-INF/views/admin/sport-form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        long id = Long.parseLong(req.getParameter("id"));
        req.setAttribute("pageTitle", "Edit Sport");
        req.setAttribute("sport", sportService.getSportById(id));
        req.getRequestDispatcher("/WEB-INF/views/admin/sport-form.jsp").forward(req, resp);
    }

    private void deleteSport(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        long id = Long.parseLong(req.getParameter("id"));
        sportService.deleteSport(id);
        resp.sendRedirect(req.getContextPath() + "/admin/sports");
    }
}