/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.iot.fiesta.eee.experimentdatastore.store;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author te0003
 */
public class StoreStartup implements ServletContextListener {
    
    protected static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    
    protected static String DB_HOSTNAME = "";
    protected static String DB_PORT = "";
    protected static String DB_NAME = "";
    
    protected static String DB_USERNAME = "";
    protected static String DB_PASSWORD = "";
    public static String DB_CONNECTION = "";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        ServletContext context = sce.getServletContext();
        
        Properties dbProp = new Properties();
        try {
            String path = context.getInitParameter("db_access_properties");
            final InputStream is = context.getResourceAsStream(path);
            dbProp.load(is);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        DB_HOSTNAME = dbProp.getProperty("hostname");
        DB_PORT = dbProp.getProperty("port");
        DB_NAME = dbProp.getProperty("name");

        DB_USERNAME = dbProp.getProperty("username");
        DB_PASSWORD = dbProp.getProperty("password");
        DB_CONNECTION = "jdbc:mysql://" + DB_HOSTNAME + ":" + DB_PORT +"/"+ DB_NAME;
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
