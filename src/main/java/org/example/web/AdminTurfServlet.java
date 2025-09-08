package org.example.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.SportDao;
import org.example.model.Turf;
import org.example.service.TurfService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/admin/turfs")
public class AdminTurfServlet extends HttpServlet {
    private final TurfService turfService = new TurfService();
    private final SportDao sportDao = new SportDao(); // To fetch sports for the form

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
                    deleteTurf(req, resp);
                    break;
                default:
                    listTurfs(req, resp);
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
        String location = req.getParameter("location");
        boolean active = "on".equals(req.getParameter("active"));
        String[] sportIdsStr = req.getParameterValues("sportIds");

        List<Long> sportIds = (sportIdsStr == null) ? null :
                Arrays.stream(sportIdsStr).map(Long::parseLong).collect(Collectors.toList());

        Turf turf = new Turf(name, location, active);
        if (idStr != null && !idStr.isEmpty()) {
            turf.setId(Long.parseLong(idStr));
        }

        try {
            turfService.saveTurf(turf, sportIds);
            resp.sendRedirect(req.getContextPath() + "/admin/turfs");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listTurfs(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("pageTitle", "Turfs");
        req.setAttribute("turfList", turfService.getAllTurfs());
        req.getRequestDispatcher("/WEB-INF/views/admin/turfs.jsp").forward(req, resp);
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("pageTitle", "New Turf");
        req.setAttribute("turf", new Turf()); // Empty turf for the form
        req.setAttribute("allSports", sportDao.findAllActive());
        req.setAttribute("assignedSportIds", new long[0]); // No sports assigned yet
        req.getRequestDispatcher("/WEB-INF/views/admin/turf-form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        long id = Long.parseLong(req.getParameter("id"));
        req.setAttribute("pageTitle", "Edit Turf");
        req.setAttribute("turf", turfService.getTurfById(id));
        req.setAttribute("allSports", sportDao.findAllActive());
        req.setAttribute("assignedSportIds", turfService.getSportIdsForTurf(id));
        req.getRequestDispatcher("/WEB-INF/views/admin/turf-form.jsp").forward(req, resp);
    }

    private void deleteTurf(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        long id = Long.parseLong(req.getParameter("id"));
        turfService.deleteTurf(id);
        resp.sendRedirect(req.getContextPath() + "/admin/turfs");
    }
}