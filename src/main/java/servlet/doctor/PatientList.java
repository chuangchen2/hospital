package servlet.doctor;

import bean.Doctor;
import bean.Integrity;
import dao.IntegrityDao;
import dao.PatientDao;
import dao.RecodeDao;
import util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet("/doctor/patientList")
public class PatientList extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = Util.nullToString(req.getParameter("action"));
        Doctor doctor = (Doctor) req.getSession().getAttribute("doctor");
        String rid = Util.nullToString(req.getParameter("rid"));
        String pid = Util.nullToString(req.getParameter("pid"));
        RecodeDao recodeDao = RecodeDao.getInstance();
        IntegrityDao integrityDao = new IntegrityDao();
        Integrity integrity;
        String set;
        switch (action) {
            case "finish":
                set = "set state ='完成' where rid=?";
                try {
                    recodeDao.update(set, new Object[]{rid});
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                integrity = new Integrity("", pid, doctor.getDname(), doctor.getOffice(), "", "完成预约", "10");
                try {
                    integrityDao.insert(integrity);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "sy":
                set = "set state ='爽约' where rid=?";
                try {
                    recodeDao.update(set, new Object[]{rid});
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                integrity = new Integrity("", pid, doctor.getDname(), doctor.getOffice(), "", "爽约", "-10");
                try {
                    integrityDao.insert(integrity);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
        PatientDao patientDao = PatientDao.getInstance();

        ArrayList<HashMap<String, String>> list = null;
        try {
            list = patientDao.patientList(doctor.getDid());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.setAttribute("list", list);
        req.getRequestDispatcher("patientList.jsp").forward(req, resp);
    }
}
