package servlet.patient;

import bean.Patient;
import dao.PatientDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/register")
public class Register extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*email char(32) primary key ,
        password char(32),
                gender char(2),
                name char(16),*/
        String message = "";
        String password = req.getParameter("password");
        String passwordCof = req.getParameter("passwordCof");
        if (password != null && password.equals(passwordCof)) {
            String account = req.getParameter("account");
            if (account != null) {
                PatientDao patientDao = PatientDao.getInstance();
                List<Patient> patients = null;
                try {
                    patients = patientDao.query("account", account);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (patients.size() == 0) {
                    String name = req.getParameter("name");
                    String email = req.getParameter("email");
                    Patient patient = new Patient(null, account, email, password, name, "100");
                    try {
                        if (patientDao.insert(patient)) {
                            logger.info("注册成功");
                            message = "注册成功，请登录！";
                            req.getSession().setAttribute("message", message);
                            req.getSession().removeAttribute("checkCode");
                            resp.sendRedirect("login.jsp");
                            return;
                        } else {
                            logger.info("注册失败，sql改动数为0");
                            message = "注册失败，请稍后再试！";
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    message = "该账号已存在！";
                }
            } else {
                message = "请输入账号！";
            }
        } else {
            message = "两次密码不正确！";
        }
        req.setAttribute("message", message);
        req.getRequestDispatcher("register.jsp").forward(req, resp);
    }
}
