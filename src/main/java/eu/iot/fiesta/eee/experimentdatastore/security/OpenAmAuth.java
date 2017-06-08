/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.iot.fiesta.eee.experimentdatastore.security;

import eu.iot.fiesta.eee.experimentdatastore.model.user.UserResolveResponse;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eu.iot.fiesta.eee.experimentdatastore.store.StoreStartup;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

/**
 *
 * @author te0003
 */
public class OpenAmAuth {

    private String OPEN_AM_RESOLVE_USER_URL = "";

    public OpenAmAuth() {

        try {
            Properties globalProp = new Properties();
            ServletContext context = StoreStartup.context;
            String path = context.getInitParameter("global");
            final InputStream is = context.getResourceAsStream(path);
            globalProp.load(is);
            OPEN_AM_RESOLVE_USER_URL = globalProp.getProperty("hostname");
        } catch (IOException | NoClassDefFoundError ex) {
            System.out.println(ex.getMessage());
            OPEN_AM_RESOLVE_USER_URL = "https://platform-dev.fiesta-iot.eu/openam/json/users?_action=idFromSession";
        }
    }

    public String getUserId(String openAmToken) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        Context context = new Context();
        Client client = new Client(new Context(), Protocol.HTTP);
        client.getContext().getParameters().add("maxConnectionsPerHost", "5");
        client.getContext().getParameters().add("maxTotalConnections", "5");
        ClientResource oaClientRes = new ClientResource(context, OPEN_AM_RESOLVE_USER_URL);
        oaClientRes.setNext(client);
        oaClientRes.accept(MediaType.APPLICATION_JSON);
//        Series<Header> headers = (Series<Header>) sicsClientResource.getRequestAttributes().get("org.restlet.http.headers");
//        System.out.println(headers.size());
//        headers.add("iPlanetDirectoryPro", openAmToken);
        oaClientRes.getRequest().getHeaders().add("iPlanetDirectoryPro", openAmToken);
        System.out.println(oaClientRes.getRequest().getHeaders().getFirstValue("iPlanetDirectoryPro"));

        String messageResponse = "";

        try {
            Representation result = oaClientRes
                    .post(new StringRepresentation("", MediaType.APPLICATION_JSON));
            oaClientRes.release();
            try {
                UserResolveResponse urr = objectMapper.readValue(result.getStream(), UserResolveResponse.class);
                messageResponse = urr.getId();

            } catch (Exception ex) {
                Logger.getLogger(OpenAmAuth.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ResourceException ex) {
//            Logger.getLogger(RegistryHandler.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR IS THIS: " + ex.getLocalizedMessage());
            messageResponse = ex.getLocalizedMessage();
        }

        return messageResponse;
    }

    public static void main(String[] argv) {

        OpenAmAuth oaa = new OpenAmAuth();
        String openAmToken = "AQIC5wM2LY4SfcyaHmTqpOYOdFtwGFGR60Tm_WuYMXksVsc.*AAJTSQACMDEAAlNLABMtNjU4OTMxMjQwOTQyNzAzOTIwAAJTMQAA*";
        String result = oaa.getUserId(openAmToken);

        if (result.contains(" ")) {
            System.out.println("error response is: " + result);
        } else {
            System.out.println("message response is: " + result);
        }

    }

}
