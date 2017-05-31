/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.iot.fiesta.eee.experimentdatastore.store;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import eu.iot.fiesta.eee.experimentdatastore.model.KatResult;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author te0003
 */
public class StoreAccess {

    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_USERNAME = "fiesta";
    private static final String DB_PASSWORD = "fiesta";
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3307/test2";//?user=" + DB_USERNAME + "&password=" + DB_PASSWORD;

    private static final DateFormat DATE_FORMAT = new ISO8601DateFormat();

    public static void main(String[] argv) {

        StoreAccess sa = new StoreAccess();

        try {
            sa.storeExperimentResult("1234", "567", "890", new KatResult("test1"));
//            String exprResult = sa.getExperimentResult("user1234", "femo567", "fismo890");
//            System.out.println("exprResult: "+ exprResult);
        } catch (SQLException ex) {
            Logger.getLogger(StoreAccess.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void storeExperimentResult(String userId, String femoId, String jobId, KatResult result) throws SQLException {

        Connection dbConnection = null;
        Statement statement = null;

        String query = "INSERT INTO test2.experiment"
                + " (USER_ID, FEMO_ID, JOB_ID, TIME_STAMP, EXPR_RESULT)"
                + " VALUES (?,?,?,?,?)";

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            PreparedStatement preparedStmt = dbConnection.prepareStatement(query);
            preparedStmt.setString(1, userId);
            preparedStmt.setString(2, femoId);
            preparedStmt.setString(3, jobId);
            preparedStmt.setTimestamp(4, Timestamp.from(Instant.now()));
        //preparedStmt.setTimestamp(4, Timestamp.valueOf(DATE_FORMAT.format(new Date())));
            preparedStmt.setString(5, result.getResult());

            System.out.println(preparedStmt.toString());

            preparedStmt.execute();

            System.out.println("Record is inserted into test2.experiment table!");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {

            if (statement != null) {
                statement.close();
            }

            if (dbConnection != null) {
                dbConnection.close();
            }

        }

    }

    public String getExperimentResult(String userId, String femoId, String jobId) throws SQLException {

        Connection dbConnection = null;
        Statement statement = null;

        String selectTableSQL = "SELECT EXPR_RESULT FROM test2.experiment "
                + "WHERE (USER_ID = \'" + userId + "\' AND FEMO_ID = \'" + femoId + "\'AND JOB_ID = \'" + jobId + "\');";
        
        String exprResult = "";

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            System.out.println(selectTableSQL);

            // execute select SQL stetement
            ResultSet rs = statement.executeQuery(selectTableSQL);

            while (rs.next()) {

//                userId = rs.getString("USER_ID");
//                fismoId = rs.getString("FISMO_ID");
//                String timestamp = rs.getString("TIME_STAMP");
                exprResult = rs.getString("EXPR_RESULT");

                System.out.println("userid : " + userId);
                System.out.println("fismoId : " + jobId);
//                System.out.println("userid : " + timestamp);
                System.out.println("exprResult : " + exprResult);
                
            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            exprResult = "INVALID SQL QUERY";

        } finally {

            if (statement != null) {
                statement.close();
            }

            if (dbConnection != null) {
                dbConnection.close();
            }

        }
        
         return exprResult;

    }

    private static Connection getDBConnection() {

        Connection dbConnection = null;

        try {

            Class.forName(StoreStartup.DB_DRIVER);

        } catch (ClassNotFoundException | java.lang.NoClassDefFoundError e) {
            
            try {
                Class.forName(DB_DRIVER);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(StoreAccess.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println(e.getMessage());

        }

        try {

            dbConnection = DriverManager.getConnection(
                    StoreStartup.DB_CONNECTION, StoreStartup.DB_USERNAME, StoreStartup.DB_PASSWORD);
            return dbConnection;

        } catch (SQLException | java.lang.NoClassDefFoundError e) {
            
            try {
                dbConnection = DriverManager.getConnection(
                        DB_CONNECTION, DB_USERNAME, DB_PASSWORD);
            } catch (SQLException ex) {
                Logger.getLogger(StoreAccess.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println(e.getMessage());

        }

        return dbConnection;

    }

    private static String getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return DATE_FORMAT.format(today.getTime());

    }

}
