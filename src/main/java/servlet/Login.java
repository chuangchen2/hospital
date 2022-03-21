package servlet;

import bean.Patient;
import dao.PatientDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/login")
public class Login extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String account = req.getParameter("account");
        String password = req.getParameter("password");
        String accounttype = req.getParameter("accounttype");
        req.getSession().removeAttribute("message");
        PatientDao patientDao = PatientDao.getInstance();
        List<Patient> patients = null;
        try {
            patients = patientDao.query("account", account);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (patients.size() > 0) {
            Patient patient = patients.get(0);
            if (patient.getPassword().equals(password)) {
                req.getSession().setAttribute("patient", patient);
                String url = (String) req.getSession().getAttribute("url");
                if (url == null)
                    url = "index.jsp";
                resp.sendRedirect(url);
                return;
            }
        }
        req.getSession().setAttribute("message", "用户名或密码错误！！");
        resp.sendRedirect("login.jsp");
    }
}
