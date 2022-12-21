package eu.advantage.fibernow.resource;

import eu.advantage.fibernow.dto.CustomerDto;
import eu.advantage.fibernow.service.CustomerService;
import eu.advantage.fibernow.util.rest.ApiResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @Inject
    private CustomerService service;

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "CUSTOMER"})
    public Response getById(@PathParam("id") Long id) {
        return Response
                .status(Response.Status.ACCEPTED)
                .entity(ApiResponse.builder().data(service.findCustomer(id)).build())
                .build();
    }

    @GET
    @Path("/username/{username}")
    @RolesAllowed({"ADMIN", "CUSTOMER"})
    public Response getByUsername(@PathParam("username") String username) {
        return Response
                .status(Response.Status.ACCEPTED)
                .entity(ApiResponse.builder().data(service.findCustomerByUsername(username)).build())
                .build();
    }

    @GET
    @RolesAllowed("ADMIN")
    public Response search(@QueryParam("tin") String tin, @QueryParam("email") String email) {
        return Response
                .status(Response.Status.ACCEPTED)
                .entity(ApiResponse.builder().data(service.searchCustomers(email, tin)).build())
                .build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response save(@Valid CustomerDto customerDto) {
        CustomerDto result = service.saveCustomer(customerDto);
        return Response.created(UriBuilder
                .fromResource(CustomerResource.class)
                .path("/" + result.getId())
                .build()
        )
                .entity(ApiResponse.builder().data(result).build())
                .build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        return Response
                .status(Response.Status.ACCEPTED)
                .entity(ApiResponse.builder().data(service.deleteCustomer(id)).build())
                .build();
    }
}
