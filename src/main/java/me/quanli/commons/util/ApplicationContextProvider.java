package me.quanli.commons.util;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.PooledDataSource;

@Service("applicationContextProvider")
@WebListener
public class ApplicationContextProvider
        implements ApplicationContextAware, ServletContextAware, ServletContextListener {

    private static final Log LOGGER = LogFactory.getLog(ApplicationContextProvider.class);

    private static ApplicationContext APPLICATION_CONTEXT = null;

    private static ServletContext SERVLET_CONTEXT = null;

    public static String CONTEXT_PATH = "";

    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    public static ServletContext getServletContext() {
        return SERVLET_CONTEXT;
    }

    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.debug("ServletContext init...");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        Iterator<?> it = C3P0Registry.getPooledDataSources().iterator();
        while (it.hasNext()) {
            try {
                PooledDataSource dataSource = (PooledDataSource) it.next();
                dataSource.close();
            } catch (Exception e) {
                LOGGER.error(e, e);
            }
        }
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                LOGGER.error(e, e);
            }
        }
    }

    public void setServletContext(ServletContext servletContext) {
        ApplicationContextProvider.CONTEXT_PATH = servletContext.getContextPath();
        ApplicationContextProvider.SERVLET_CONTEXT = servletContext;
        LOGGER.debug("servlet context path : " + ApplicationContextProvider.CONTEXT_PATH);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.APPLICATION_CONTEXT = applicationContext;
    }

}
