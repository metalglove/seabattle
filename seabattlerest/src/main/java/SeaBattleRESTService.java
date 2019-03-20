import org.eclipse.jetty.server.Authentication;
import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
        import javax.ws.rs.core.Response;
import java.util.List;

@Path("/seabattle")
public class SeaBattleRESTService {
    private static final Logger log = LoggerFactory.getLogger(SeaBattleRESTService.class);

    public SeaBattleRESTService() {

    }

    // TODO: ADD DATABASE OBJECT TO PARAMETER. GET DATA FROM GIVEN OBJECT, CREATE USER OBJECT AND FILL WITH DATA, THEN PASS OBJECT TO RESPONSE.
    @PUT
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login() {

        log.info("[Server login]");

        if (false /* Replace this with databaseObject == null */) {
            // Client error 400 - Bad Request
            return Response.status(400).entity(RestResponseHelper.getErrorResponseString()).build();
        }


        // Define response
        return Response.status(200).entity(RestResponseHelper.getLoginResponse(/* User object as param */)).build();
    }

    // TODO: ADD DATABASE OBJECT TO PARAMETER. GET DATA FROM GIVEN OBJECT, CREATE USER OBJECT AND FILL WITH DATA, THEN PASS OBJECT TO RESPONSE.
    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response register() {

        log.info("[Server register]");
        // Check request
        if (false /* Replace this with databaseObject == null */) {
            // Client error 400 - Bad Request
            return Response.status(400).entity(RestResponseHelper.getErrorResponseString()).build();
        }

        // Define response
        return Response.status(200).entity(RestResponseHelper.getRegisterResponse(/* User object as param */)).build();
    }

}
