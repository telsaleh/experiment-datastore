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
    
//    public static final String storePrefix = "/experiment-store"; //POST
    
    @Override
    public synchronized Restlet createInboundRoot() {

        Router router = new Router(getContext());
        
        router.attachDefault(StoreHandler.class);        
//        router.attach("/{result_id}", StoreHandler.class); //POST, GET, UPDATE, DELETE        
        
        return router;
    }

}
