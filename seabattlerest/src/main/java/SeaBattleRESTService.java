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

    @PUT @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(final CredentialsInput input) {
        String username = input.username;
        String password = input.password;

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

    @POST @Consumes(MediaType.APPLICATION_JSON)
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(final CredentialsInput input) {
        String username = input.username;
        String password = input.password;

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

    @GET
    @Path("/getInUse/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserNameInUse(@PathParam("username") String username) {

        log.info("get username");

        if (username == null) {
            // Client error 400 - Bad Request
            return Response.status(400).entity(RestResponseHelper.getErrorResponseString()).build();
        }

        UserDataAccess dataAccess = new UserDataAccess();

        boolean usernameInUse = dataAccess.getUsernameInUse(username);
        // Define response
        return Response.status(200).entity(RestResponseHelper.getUsernameInUseResponse(usernameInUse)).build();
    }
}
