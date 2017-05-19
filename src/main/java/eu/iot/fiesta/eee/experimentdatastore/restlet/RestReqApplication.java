/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.iot.fiesta.eee.experimentdatastore.restlet;


import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class RestReqApplication extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    
    public static final String storePrefix = "/store"; //POST
    
    @Override
    public synchronized Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a new instance of HelloWorldResource.
        Router router = new Router(getContext());
        
        router.attach(storePrefix, StoreHandler.class);        
        router.attach(storePrefix+"/{repository_id}", StoreHandler.class); //POST, GET, UPDATE, DELETE        
        
        return router;
    }

}
