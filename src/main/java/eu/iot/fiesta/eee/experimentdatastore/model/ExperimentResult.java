package eu.iot.fiesta.eee.experimentdatastore.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "result"
})
public class ExperimentResult {

    public ExperimentResult() {
    }

    public ExperimentResult(String result) {

        this.result = result;

    }

    @JsonProperty("result")
    private String result;

    @JsonProperty("result")
    public String getResult() {
        return result;
    }

    @JsonProperty("result")
    public void setResult(String result) {
        this.result = result;
    }

}
