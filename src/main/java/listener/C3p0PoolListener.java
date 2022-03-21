package listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.DBUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;

@WebListener
public class C3p0PoolListener implements ServletContextListener {
    private final static Logger logger = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Connection connection = DBUtil.getConnection();
        logger.info("初始化连接池...");
        DBUtil.release(connection, null, null);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
