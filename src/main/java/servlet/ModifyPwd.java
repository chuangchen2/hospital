package servlet;

import bean.Patient;
import dao.PatientDao;
import util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/modifyPwd")
public class ModifyPwd extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jmm = Util.nullToString(req.getParameter("jmm"));
        String xmm = Util.nullToString(req.getParameter("xmm"));
        String qdmm = Util.nullToString(req.getParameter("qdmm"));
        String message = "";
        Patient patient = (Patient) req.getSession().getAttribute("patient");
        if (patient.getPassword().equals(jmm)) {
            if (xmm.equals(qdmm)) {
                PatientDao patientDao = PatientDao.getInstance();
                String set = "set password=? where pid=?";
                try {
                    if (patientDao.update(set, new Object[]{xmm, patient.getId()})) {
                        message = "修改成功";
                    } else {
                        message = "修改失败";
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                message = "两次密码不一样";
            }
        } else {
            message = "密码错误";
        }
        req.setAttribute("message", message);
        req.getRequestDispatcher("modifyPwd.jsp").forward(req, resp);
    }
}
