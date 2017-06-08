/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.iot.fiesta.eee.experimentdatastore.model.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobid",
    "results"
})
public class FemoResult {

    @JsonProperty("jobid")
    private String jobid;
    @JsonProperty("results")
    private List<JobResult> results = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public FemoResult() {
    }

    /**
     *
     * @param results
     * @param jobid
     */
    public FemoResult(String jobid, List<JobResult> results) {
        super();
        this.jobid = jobid;
        this.results = results;
    }

    @JsonProperty("jobid")
    public String getJobid() {
        return jobid;
    }

    @JsonProperty("jobid")
    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    @JsonProperty("results")
    public List<JobResult> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<JobResult> results) {
        this.results = results;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
