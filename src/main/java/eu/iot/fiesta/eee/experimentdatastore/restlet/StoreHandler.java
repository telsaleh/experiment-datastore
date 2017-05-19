/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.iot.fiesta.eee.experimentdatastore.restlet;


import java.io.IOException;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

/**
 * Resource which has only one representation.
 */
public class StoreHandler extends ServerResource {

    @Post
    public Representation handleRegister(Representation entity) throws ResourceException, IOException {

        String repoId = (String) getRequest().getAttributes().get("repository_id");
        String reqBody = entity.getText();


        return new StringRepresentation(reqBody);
    }

    @Get
    public Representation handleLookup() {

        String repoId = (String) getRequest().getAttributes().get("repository_id");
        String resURI = getRequest().getResourceRef().toUri().toString();

        return new StringRepresentation("GET called");
    }

    @Put
    public Representation handleUpdate(Representation entity) throws ResourceException, IOException {

        Representation result;
        String reqBody = entity.getText();
        String repoId = (String) getRequest().getAttributes().get("repository_id");
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
