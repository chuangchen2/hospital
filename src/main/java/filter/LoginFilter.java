package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class LoginFilter implements Filter {
    private List<String> patient;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        patient = Arrays.asList("/integrity", "/order", "/orderList");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        // 得到用户请求的URI
        // 得到web应用程序的上下文路径
        // 去除上下文路径，得到剩余部分的路径
        String uri = req.getRequestURI().substring(req.getContextPath().length());//请求的页面
        String url = req.getHeader("referer");//之前的页面
        if (req.getSession().getAttribute("patient") == null && patient.contains(uri)) {
            req.getSession().setAttribute("message", "请先登录!");
            req.getSession().setAttribute("url", url);
//            System.out.println(req.getSession().getAttribute("url"));
            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            resp.sendRedirect("login.jsp");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }


    }

    @Override
    public void destroy() {

    }
}
