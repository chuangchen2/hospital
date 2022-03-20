package servlet.patient;

import bean.Doctor;
import bean.Office;
import bean.Pages;
import dao.DoctorDao;
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

@WebServlet("/searchDoctor")
public class DoctorSearch extends HttpServlet {
    private final static Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String office = Util.nullToString(req.getParameter("office"));
        String name = Util.nullToString(req.getParameter("doctor"));
        int start = Util.nullToZero(req.getParameter("start"));
        DoctorDao doctorDao = DoctorDao.getInstance();
        String where = "where office like ? and dname like ? ";
        int total = 0;
        try {
            total = doctorDao.getDoctorCount(where, new Object[]{Util.toLike(office), Util.toLike(name)});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Pages pages = new Pages(start, total, 6);
        where += " limit " + ((pages.getCurrentPage() - 1) * 6) + ",6";
        List<Doctor> doctors = null;
        try {
            doctors = doctorDao.query(where, new Object[]{Util.toLike(office), Util.toLike(name)});
        } catch (SQLException e) {
            logger.error(e);
        }
        req.setAttribute("doctors", doctors);
        req.setAttribute("pages", pages);
        req.setAttribute("doctor", name);
        OfficeDao officeDao = OfficeDao.getInstance();
        List<Office> offices = null;
        try {
            offices = officeDao.query("officename", office, "");
        } catch (SQLException e) {
            logger.error(e);
        }
        req.setAttribute("office", offices.get(0));
        String order = req.getParameter("order");
        if ("doctor".equals(order)) {
            logger.info("查询所有医生第 " + pages.getCurrentPage() + " 页");
            req.getRequestDispatcher("/orderDoctor.jsp").forward(req, resp);
        } else {
            logger.info("按" + order + "查询医生");
            req.getRequestDispatcher("/officeInfo.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
