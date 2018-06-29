/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.iot.fiesta.eee.experimentdatastore.store;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    protected static String DB_USERNAME = "";
    protected static String DB_PASSWORD = "";
    protected static String DB_CONNECTION = "";

    public static ServletContext context;

    protected static String EXPR_STORE_SCRIPT = "/WEB-INF/sql-scripts/create-expr-store.sql";
    protected static Connection conn = null;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        context = sce.getServletContext();
        EXPR_STORE_SCRIPT = context.getRealPath(EXPR_STORE_SCRIPT);

        Properties dbProp = new Properties();
        try {
            String propertiesPath = System.getProperty("jboss.server.config.dir") + "/fiesta-iot.properties";
            final FileInputStream fis = new FileInputStream(propertiesPath);
            dbProp.load(fis);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            String propertiesPath = context.getInitParameter("db");
            final InputStream is = context.getResourceAsStream(propertiesPath);
            try {
                dbProp.load(is);
            } catch (IOException ex1) {
                System.out.println("Error loading properties file: " + ex1.getLocalizedMessage());
                Logger.getLogger(StoreStartup.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        DB_USERNAME = dbProp.getProperty("eee.ers.db.USERNAME");
        DB_PASSWORD = dbProp.getProperty("eee.ers.db.PASSWORD");
        DB_CONNECTION = dbProp.getProperty("eee.ers.db.URL");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
