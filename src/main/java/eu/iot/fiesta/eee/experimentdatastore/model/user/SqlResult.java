/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.iot.fiesta.eee.experimentdatastore.model.user;

/**
 *
 * @author te0003
 */
public class SqlResult {

    private String femoId;
    private String jobId;
    private String timestamp;
    private String exprResult;

    public SqlResult() {
    }

    public SqlResult(String femoId, String jobId, String timestamp, String exprResult) {
        this.femoId = femoId;
        this.jobId = jobId;
        this.timestamp = timestamp;
        this.exprResult = exprResult;
    }
    
    public String getFemoId() {
        return femoId;
    }

    public void setFemoId(String femoId) {
        this.femoId = femoId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getExprResult() {
        return exprResult;
    }

    public void setExprResult(String exprResult) {
        this.exprResult = exprResult;
    }

}
