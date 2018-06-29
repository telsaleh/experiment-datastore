/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.iot.fiesta.eee.experimentdatastore.restlet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eu.iot.fiesta.eee.experimentdatastore.store.StoreAccess;
import eu.iot.fiesta.eee.experimentdatastore.model.ExperimentResult;
import eu.iot.fiesta.eee.experimentdatastore.security.OpenAmAuth;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.data.Header;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

public class StoreHandler extends ServerResource {
    
    @Post
    public Representation handleRegister(Representation entity) throws IOException {
                
        Series<Header> series = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String userId = series.getFirstValue("userId", true);
        String femoId = series.getFirstValue("femoId", true);
        String jobId = series.getFirstValue("jobId", true);

        String reqBody = entity.getText();
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        
        ExperimentResult exprResult = new ExperimentResult();
        String result = "";
        
        exprResult = objectMapper.readValue(reqBody, ExperimentResult.class);//            Logger.getLogger(StoreHandler.class.getName()).log(Level.SEVERE, null, ex);
        
        StoreAccess sa = new StoreAccess();
       
        try {
            sa.storeExperimentResult(userId, femoId, jobId, exprResult);
            result = "\n{\"status\": \"Result Stored\"}\n";
        } catch (SQLException ex) {
//            Logger.getLogger(StoreHandler.class.getName()).log(Level.SEVERE, null, ex);
            result = "\n{\"status\": \"Internal Error\"}\n";
        }        

        return new StringRepresentation(result);
    }

    @Get
    public Representation handleLookup() {

        Series<Header> series = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");

        String tokenId = series.getFirstValue("iPlanetDirectoryPro", true);
        String femoId = series.getFirstValue("femoId", true);
        String jobId = series.getFirstValue("jobId", true);
        
        OpenAmAuth oaa = new OpenAmAuth();
        
        String userId = oaa.getUserId(tokenId);
        
        if (userId.contains(" ")) {
            setStatus(Status.CLIENT_ERROR_UNAUTHORIZED, "token does not belong to an authorized user");
            return new StringRepresentation(userId, MediaType.APPLICATION_JSON);
            
        }
        
        String result = "";
        
         StoreAccess sa = new StoreAccess();
         try {
            try {
                result = sa.getExperimentResult(userId, femoId, jobId);
            } catch (SQLException ex) {
                Logger.getLogger(StoreHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
         } catch (JsonProcessingException ex) {
             Logger.getLogger(StoreHandler.class.getName()).log(Level.SEVERE, null, ex);
         }
        return new StringRepresentation(result, MediaType.APPLICATION_JSON);
    }

    @Put
    public Representation handleUpdate(Representation entity) throws ResourceException, IOException {

        Representation result;
        String reqBody = entity.getText();
        String repoId = (String) getRequest().getAttributes().get("result_id");
        String resourceId = (String) getRequest().getAttributes().get("resource_id");

        return new StringRepresentation(reqBody);

    }

    @Delete
    public Representation handleRemove() throws ResourceException, IOException {

        Representation result;
        String repoId = (String) getRequest().getAttributes().get("repository_id");
        String resourceId = (String) getRequest().getAttributes().get("resource_id");

        return new StringRepresentation("DELETE called");

    }

}
