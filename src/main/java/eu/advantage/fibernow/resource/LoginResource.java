package eu.advantage.fibernow.resource;

import eu.advantage.fibernow.dto.UserCredentialsDto;
import eu.advantage.fibernow.model.Admin;
import eu.advantage.fibernow.model.Customer;
import eu.advantage.fibernow.service.LoginService;
import eu.advantage.fibernow.service.UserService;
import eu.advantage.fibernow.util.rest.ApiResponse;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class LoginResource {

    @Inject
    LoginService loginService;

    @POST
    public Response login(@Valid UserCredentialsDto userCredentialsDto) {
        return Response
                    .status(Response.Status.ACCEPTED)
                    .entity(ApiResponse.builder().data(loginService.getUserType(userCredentialsDto)).build())
                    .build();
    }
}
