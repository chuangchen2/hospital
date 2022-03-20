package servlet;

import bean.Admin;
import bean.Doctor;
import bean.Patient;
import dao.AdminDao;
import dao.DoctorDao;
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
        String action = Util.nullToString(req.getParameter("action"));
        String jmm = Util.nullToString(req.getParameter("jmm"));
        String xmm = Util.nullToString(req.getParameter("xmm"));
        String qdmm = Util.nullToString(req.getParameter("qdmm"));
        String message = "";
        switch (action) {
            case "doctor":
                Doctor doctor = (Doctor) req.getSession().getAttribute("doctor");
                if (doctor.getPassword().equals(jmm)) {
                    if (xmm.equals(qdmm)) {
                        DoctorDao doctorDao = DoctorDao.getInstance();
                        String set = "set password=? where did=?";
                        try {
                            if (doctorDao.update(set, new Object[]{xmm, doctor.getDid()})) {
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
                req.getRequestDispatcher("doctor/modifyPwd.jsp").forward(req, resp);
                break;
            case "patient":
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
                break;
            case "admin":
                Admin admin = (Admin) req.getSession().getAttribute("admin");
                if (admin.getPassword().equals(jmm)) {
                    if (xmm.equals(qdmm)) {
                        AdminDao adminDao = AdminDao.getInstance();
                        String set = "set password=? where account=?";
                        try {
                            if (adminDao.updateAdmin(set, new Object[]{xmm, admin.getAccount()})) {
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
                req.getRequestDispatcher("admin/modifyPwd.jsp").forward(req, resp);
                break;
        }
    }
}
