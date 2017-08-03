/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.iot.fiesta.eee.experimentdatastore.store;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import eu.iot.fiesta.eee.experimentdatastore.model.ExperimentResult;
import eu.iot.fiesta.eee.experimentdatastore.model.user.FemoResult;
import eu.iot.fiesta.eee.experimentdatastore.model.user.FemoResults;
import eu.iot.fiesta.eee.experimentdatastore.model.user.JobResult;
import eu.iot.fiesta.eee.experimentdatastore.model.user.SqlResult;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
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
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3307/fiesta_iot_ers";//?user=" + DB_USERNAME + "&password=" + DB_PASSWORD;

    private static final DateFormat DATE_FORMAT = new ISO8601DateFormat();

    public void storeExperimentResult(String userId, String femoId, String jobId, ExperimentResult result) throws SQLException { 

        Connection dbConnection = null;
        Statement statement = null;
        
        String dbName = "";
        System.out.println("DB name: "+StoreStartup.DB_CONNECTION.split("/")[DB_CONNECTION.split("/").length-1]);

        try {
//            dbName = StoreStartup.DB_NAME;

            dbName = StoreStartup.DB_CONNECTION.split("/")[DB_CONNECTION.split("/").length-1];
        } catch (NoClassDefFoundError ex) {
            dbName = DB_CONNECTION.split("/")[DB_CONNECTION.split("/").length - 1]; 
        }

        String query = "INSERT INTO " + dbName + ".experiments"
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
            preparedStmt.setString(5, result.getResult());

            System.out.println("Prepared Statement: "+ preparedStmt.toString());

            preparedStmt.execute();

            System.out.println("Record is inserted into "+dbName+".experiments table!");

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

    public String getExperimentResult(String userId, String femoId, String jobId) throws SQLException, JsonProcessingException {

        Connection dbConnection = null;
        Statement statement = null;

        String dbName = "";

        try {
            dbName = StoreStartup.DB_CONNECTION.split("/")[DB_CONNECTION.split("/").length - 1];
        } catch (NoClassDefFoundError ex) {
            dbName = DB_CONNECTION.split("/")[DB_CONNECTION.split("/").length - 1];
        }

        String selectSqlQuery = "SELECT FEMO_ID, JOB_ID, TIME_STAMP, EXPR_RESULT FROM " + dbName + ".experiments "
                + "WHERE (USER_ID = \'" + userId + "\' AND (FEMO_ID = \'" + femoId + "\' OR JOB_ID = \'" + jobId + "\')) ORDER BY JOB_ID;";
        
        String selectSqlQueryWhere1 = "WHERE (USER_ID = \'" + userId + "\' AND (FEMO_ID = \'" + femoId + "\' AND JOB_ID = \'" + jobId + "\')) ORDER BY JOB_ID;";
        String selectSqlQueryWhere2 = "WHERE (USER_ID = \'" + userId + "\' AND (FEMO_ID = \'" + femoId + "\')) ORDER BY JOB_ID;";
        String selectSqlQueryWhere3 = "WHERE (USER_ID = \'" + userId + "\' AND (FEMO_ID = \'" + femoId + "\' AND JOB_ID = \'" + jobId + "\')) ORDER BY JOB_ID;";

        String deleteSqlQuery = "DELETE FROM " + dbName + ".experiments "
                + "WHERE (USER_ID = \'" + userId + "\' AND (FEMO_ID = \'" + femoId + "\' OR JOB_ID = \'" + jobId + "\')) ORDER BY JOB_ID;";

        String exprResult = "";

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            System.out.println(selectSqlQuery);
            // execute select SQL statement
            ResultSet rs = statement.executeQuery(selectSqlQuery);

            String timestamp = "";
            ArrayList<SqlResult> sqlResList = new ArrayList();

            while (rs.next()) {
                jobId = rs.getString("JOB_ID");
                timestamp = rs.getString("TIME_STAMP").replace(" ", "T").concat("Z");
                exprResult = rs.getString("EXPR_RESULT");
                SqlResult sqlr = new SqlResult(femoId, jobId, timestamp, exprResult);
                sqlResList.add(sqlr);
//                System.out.println("userid : " + userId + "\tfemoId : " + femoId + "\t jobId : " + jobId + "\ttimestamp : " + timestamp + "\texprResult : " + exprResult);
            }

            exprResult = annotateResults(sqlResList);

        } catch (SQLException e) {
            e.printStackTrace();
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
        
        deleteExprResults(deleteSqlQuery);

        return exprResult;

    }
    
     public String annotateResults(ArrayList<SqlResult> sqlResList) {
         
         ArrayList<String> jobIds = new ArrayList<>();
            for (int i = 0; i < sqlResList.size(); i++) {
                jobIds.add(sqlResList.get(i).getJobId());
            }

            Set<String> hs = new LinkedHashSet<>();
            hs.addAll(jobIds);
            jobIds.clear();
            jobIds.addAll(hs);

            FemoResults femoResults = new FemoResults();
            ArrayList<FemoResult> frlist = new ArrayList<>();

            System.out.println("size of jobIds: " + jobIds.size());

            for (int i = 0; i < jobIds.size(); i++) {

                String jobid = jobIds.get(i);
                ArrayList<JobResult> jrList = new ArrayList<>();

                for (int j = 0; j < sqlResList.size(); j++) {

                    if (sqlResList.get(j).getJobId().equalsIgnoreCase(jobid)) {
                        jrList.add(new JobResult(sqlResList.get(j).getTimestamp(), sqlResList.get(j).getExprResult()));
                    }
                }

                FemoResult femoResult = new FemoResult(jobid, jrList);
                frlist.add(femoResult);
            }
            femoResults.setFemoResults(frlist);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            String exprResult="";
        try {
            exprResult = objectMapper.writeValueAsString(femoResults);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StoreAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            return exprResult;
         
     }

    public static void deleteExprResults(String sqlStatement) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = getDBConnection();
            PreparedStatement st = connection.prepareStatement(sqlStatement);
//            st.setString(1, name);
            st.executeUpdate();

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }

    private static Connection getDBConnection() {

        Connection dbConnection = null;

        try {

            System.out.println("Startup Driver: " + StoreStartup.DB_DRIVER);
            Class.forName(StoreStartup.DB_DRIVER);
            

        } catch (ClassNotFoundException | java.lang.NoClassDefFoundError e) {
            
            System.out.println("Use coded Driver: " + DB_DRIVER);

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
    
    public static void main(String[] argv) {

        StoreAccess sa = new StoreAccess();

        System.out.println(DB_CONNECTION);

        try {
            try {
                //            sa.storeExperimentResult("tarek", "567", "900", new ExperimentResult("test1"));
                String exprResult = sa.getExperimentResult("surreyadmin", "567", "");
                System.out.println("This is the json payload:\n" + exprResult);
            } catch (JsonProcessingException ex) {
                Logger.getLogger(StoreAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoreAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
