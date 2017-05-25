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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author te0003
 */
public class StoreAccess {

//    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
//    private static final String DB_USER = "fiesta";
//    private static final String DB_PASSWORD = "fiesta";
//    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3307/test2?user=" + DB_USER + "&password=" + DB_PASSWORD;

    private static final DateFormat DATE_FORMAT = new ISO8601DateFormat();

    public static void main(String[] argv) {

        StoreAccess sa = new StoreAccess();

        try {
//            sa.storeExperimentResult("1234", "567", new KatResult("hi"));
            String exprResult = sa.getExperimentResult("1234", "567");
            System.out.println("exprResult: "+ exprResult);
        } catch (SQLException ex) {
            Logger.getLogger(StoreAccess.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void storeExperimentResult(String userId, String fismoId, KatResult result) throws SQLException {

        Connection dbConnection = null;
        Statement statement = null;

        String query = "INSERT INTO test2.experiment"
                + " (USER_ID, FISMO_ID, TIME_STAMP, EXPR_RESULT)"
                + " VALUES (?,?,?,?)";

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            PreparedStatement preparedStmt = dbConnection.prepareStatement(query);
            preparedStmt.setString(1, userId);
            preparedStmt.setString(2, fismoId);
            //preparedStmt.setObject(3, result.getResult(),);
            preparedStmt.setTimestamp(3, Timestamp.from(Instant.now()));
//preparedStmt.setTimestamp(3, Timestamp.valueOf(DATE_FORMAT.format(new Date())));
            preparedStmt.setString(4, result.getResult());

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

    public String getExperimentResult(String userId, String fismoId) throws SQLException {

        Connection dbConnection = null;
        Statement statement = null;

        String selectTableSQL = "SELECT EXPR_RESULT FROM test2.experiment "
                + "WHERE (USER_ID = \'" + userId + "\' AND FISMO_ID = \'" + fismoId + "\');";
        
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
                System.out.println("fismoId : " + fismoId);
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

        } catch (ClassNotFoundException e) {

            System.out.println(e.getMessage());

        }

        try {

            dbConnection = DriverManager.getConnection(
                    StoreStartup.DB_CONNECTION, StoreStartup.DB_USERNAME, StoreStartup.DB_PASSWORD);
            return dbConnection;

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return dbConnection;

    }

    private static String getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return DATE_FORMAT.format(today.getTime());

    }

}
