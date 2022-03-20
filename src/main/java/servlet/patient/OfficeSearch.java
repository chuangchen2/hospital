package servlet.patient;

import bean.Office;
import bean.Pages;
import dao.OfficeDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/searchOffice")
public class OfficeSearch extends HttpServlet {
    private final static Logger logger = LogManager.getLogger();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String office = Util.nullToString(req.getParameter("office"));
        int start = Util.nullToZero(req.getParameter("start"));
        OfficeDao officeDao = OfficeDao.getInstance();

        Pages p = null;
        try {
            p = new Pages(start, officeDao.getOfficeCount("officename", office), 10);
        } catch (SQLException e) {
            logger.info(e);
        }
        String limit = "limit " + ((p.getCurrentPage() - 1) * 10) + ",10";
        List<Office> offices = null;
        try {
            offices = officeDao.query("officename", office, limit);
        } catch (SQLException e) {
            logger.info(e);
        }
        req.setAttribute("offices", offices);
        req.setAttribute("office", office);
        req.setAttribute("pages", p);
        logger.info("查询所有科室");
        req.getRequestDispatcher("orderOffice.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
