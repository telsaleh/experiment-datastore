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
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"id",
"realm",
"dn",
"successURL",
"fullLoginURL"
})
public class UserResolveResponse {

@JsonProperty("id")
private String id;
@JsonProperty("realm")
private String realm;
@JsonProperty("dn")
private String dn;
@JsonProperty("successURL")
private String successURL;
@JsonProperty("fullLoginURL")
private String fullLoginURL;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* No args constructor for use in serialization
* 
*/
public UserResolveResponse() {
}

/**
* 
* @param id
* @param realm
* @param successURL
* @param fullLoginURL
* @param dn
*/
public UserResolveResponse(String id, String realm, String dn, String successURL, String fullLoginURL) {
super();
this.id = id;
this.realm = realm;
this.dn = dn;
this.successURL = successURL;
this.fullLoginURL = fullLoginURL;
}

@JsonProperty("id")
public String getId() {
return id;
}

@JsonProperty("id")
public void setId(String id) {
this.id = id;
}

@JsonProperty("realm")
public String getRealm() {
return realm;
}

@JsonProperty("realm")
public void setRealm(String realm) {
this.realm = realm;
}

@JsonProperty("dn")
public String getDn() {
return dn;
}

@JsonProperty("dn")
public void setDn(String dn) {
this.dn = dn;
}

@JsonProperty("successURL")
public String getSuccessURL() {
return successURL;
}

@JsonProperty("successURL")
public void setSuccessURL(String successURL) {
this.successURL = successURL;
}

@JsonProperty("fullLoginURL")
public String getFullLoginURL() {
return fullLoginURL;
}

@JsonProperty("fullLoginURL")
public void setFullLoginURL(String fullLoginURL) {
this.fullLoginURL = fullLoginURL;
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
