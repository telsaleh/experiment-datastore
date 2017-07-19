/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.iot.fiesta.eee.experimentdatastore.store;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.h2.tools.RunScript;

/**
 * Web application lifecycle listener.
 *
 * @author te0003
 */
public class StoreStartup implements ServletContextListener {

    protected static final String DB_DRIVER = "com.mysql.jdbc.Driver";

    protected static String DB_HOSTNAME = "";
    protected static String DB_PORT = "";
//    protected static String DB_NAME = "";

    protected static String DB_USERNAME = "";
    protected static String DB_PASSWORD = "";
    public static String DB_CONNECTION = "";
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
                Logger.getLogger(StoreStartup.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        DB_USERNAME = dbProp.getProperty("eee.ers.db.USERNAME");
        DB_PASSWORD = dbProp.getProperty("eee.ers.db.PASSWORD");
        DB_CONNECTION = dbProp.getProperty("eee.ers.db.URL"); 

//        runSqlScript();
    }

    public void runSqlScript() {

        try {
            try {
                Class.forName(DB_DRIVER);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(StoreStartup.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                System.out.println("DB access, using default path: " + DB_CONNECTION);
                conn = DriverManager.getConnection(DB_CONNECTION, DB_USERNAME, DB_PASSWORD);
            } catch (MySQLSyntaxErrorException ex) {
                System.out.println("DB not found, using default path: " + DB_CONNECTION.replaceFirst(DB_CONNECTION.split("/")[DB_CONNECTION.split("/").length - 1], ""));
                conn = DriverManager.getConnection(DB_CONNECTION.replaceFirst(DB_CONNECTION.split("/")[DB_CONNECTION.split("/").length - 1], ""), DB_USERNAME, DB_PASSWORD);
            }

            try {
                // Initialize object for ScripRunner
                ScriptRunner sr = new ScriptRunner(conn);
                // Give the input file to Reader
                Reader reader = new BufferedReader(new FileReader(EXPR_STORE_SCRIPT));
                // Execute script 
//                sr.runScript(reader);
                RunScript.execute(conn, reader);
                System.out.println("Script executed");
            } catch (FileNotFoundException e) {
                System.err.println("Failed to Execute" + EXPR_STORE_SCRIPT
                        + " The error is " + e.getMessage());
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoreStartup.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
