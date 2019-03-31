import dtos.UserResultDto;
import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
        import javax.ws.rs.core.Response;

@Path("/seabattle")
public class SeaBattleRESTService{
    private static final Logger log = LoggerFactory.getLogger(SeaBattleRESTService.class);

    public SeaBattleRESTService() {

    }

    @PUT
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String username, String password) {

        log.info("[Server login]");

        if (username == null || password == null) {
            // Client error 400 - Bad Request
            return Response.status(400).entity(RestResponseHelper.getErrorResponseString()).build();
        }

        UserDataAccess dataAccess = new UserDataAccess();

        UserResultDto user = dataAccess.getExistingUser(username, password);
        // Define response
        return Response.status(200).entity(RestResponseHelper.getLoginResponse(user)).build();
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(String username, String password) {

        log.info("[Server register]");
        // Check request
        if (username == null || password == null) {
            // Client error 400 - Bad Request
            return Response.status(400).entity(RestResponseHelper.getErrorResponseString()).build();
        }

        UserDataAccess dataAccess = new UserDataAccess();

        boolean success = dataAccess.resgisterUser(username, password);
        // Define response
        return Response.status(200).entity(RestResponseHelper.getRegisterResponse(success)).build();
    }
}
