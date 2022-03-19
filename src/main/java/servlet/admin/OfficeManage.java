package servlet.admin;

import bean.Office;
import bean.Pages;
import dao.OfficeDao;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
@WebServlet("/admin/office")
public class OfficeManage extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = Util.nullToString(req.getParameter("action"));
        String message="";
        OfficeDao officeDao=OfficeDao.getInstance();
        List<Office> offices = null;
        switch (action){
            case "add":
                String officename = req.getParameter("officename");
                String description = req.getParameter("description");
                try {
                    offices= officeDao.query("officename", officename,"");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if(offices.size()==0){
                    Office office = new Office(officename, description, 0);
                    try {
                        if(officeDao.insert(office)){
                            message=officename+"添加成功！";
                        }else {
                            message=officename+"添加失败！";
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else {
                    message=officename+"已存在！";
                }
                req.setAttribute("message",message);
                //req.getRequestDispatcher("admin/office.jsp").forward(req,resp);
                break;
            case "query":
                try {
                    offices = officeDao.query("officename", "","");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                JSONArray array=new JSONArray();
                for(Office office:offices){
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("officename",office.getOfficename());
                    jsonObject.put("description",office.getDescription());
                    jsonObject.put("doctornum",office.getDoctornum());
                    array.put(jsonObject);
                }
                resp.setContentType("application/json; charset=utf-8");
                resp.getWriter().write(array.toString());
                return;
        }

        String office= Util.nullToString(req.getParameter("office"));
        int start=Util.nullToZero(req.getParameter("start"));
        Pages p = null;
        try {
            p = new Pages(start , officeDao.getOfficeCount("officename", office),10);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String limit="limit "+((p.getCurrentPage()-1)*10)+",10";
        try {
            offices = officeDao.query("officename", office,limit);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.setAttribute("offices",offices);
        req.setAttribute("office",office);
        req.setAttribute("pages",p);
        req.getRequestDispatcher("office.jsp").forward(req,resp);
    }
}
