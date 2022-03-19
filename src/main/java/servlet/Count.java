package servlet;

import org.json.JSONArray;
import org.json.JSONObject;
import util.DBUtil;
import util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet("/count")
public class Count extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = Util.nullToString(req.getParameter("action"));
        JSONArray array=new JSONArray();
        String sql;
        resp.setContentType("application/json; charset=utf-8");
        try {
        switch (action){
            case "1":
                array.put(DBUtil.executeQueryCount("select count(*) from office"));
                array.put(DBUtil.executeQueryCount("select count(*) from room"));
                array.put(DBUtil.executeQueryCount("select count(*) from doctor"));
                array.put(DBUtil.executeQueryCount("select count(*) from patient"));
                break;
            case "2":
                sql="select doctor.office,count(doctor.did) as did from recode,doctor where recode.did=doctor.did group by doctor.office ";
                Connection connection = DBUtil.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                JSONArray data = new JSONArray();
                JSONArray data1 = new JSONArray();
                while (rs.next()){
                    data.put(rs.getString(1));
                    data1.put(rs.getString(2));
                }
                DBUtil.release(connection, statement, rs);
                JSONObject object=new JSONObject();
                object.put("data",data);
                object.put("data1",data1);
                resp.getWriter().write(object.toString());
                return;
            case "3":
                sql="select count(recode.did) from recode,workday where workday.wid=recode.wid group by workday.worktime order by workday.worktime";
                ArrayList<Integer> list = DBUtil.executeQueryCounts(sql);
                for (Integer item : list){
                    array.put(item);
                }
                break;
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resp.getWriter().write(array.toString());
    }
}
