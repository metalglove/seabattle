package rest;

import common.MessageLogger;
import dal.UserDataAccess;
import daldtos.UserResultDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/seabattle")
public class SeaBattleRestService {
    private static final MessageLogger messageLogger = new MessageLogger("REST-SERVICE");

    public SeaBattleRestService() {

    }

    @PUT @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(final CredentialsInput input) {
        String username = input.username;
        String password = input.password;

        messageLogger.info("Login request");

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

        messageLogger.info("Register request");
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

        messageLogger.info("Username request");

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
