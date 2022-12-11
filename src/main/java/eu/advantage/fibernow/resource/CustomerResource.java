package eu.advantage.fibernow.resource;

import eu.advantage.fibernow.dto.CustomerDto;
import eu.advantage.fibernow.service.CustomerService;
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
    public Response getById(@PathParam("id") Long id) {
        return Response.status(Response.Status.ACCEPTED).entity(service.findCustomer(id)).build();
    }

    @GET
    public Response search(@QueryParam("tin") String tin, @QueryParam("email") String email) {
        return Response.status(Response.Status.ACCEPTED).entity(service.searchCustomers(tin, email)).build();
    }

    @POST
    public Response save(@Valid CustomerDto customerDto) {
        CustomerDto result = service.saveCustomer(customerDto);
        return Response.created(UriBuilder
                .fromResource(CustomerResource.class)
                .path("/" + result.getId())
                .build()
        )
                .entity(result)
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return Response.status(Response.Status.ACCEPTED).entity(service.deleteCustomer(id)).build();
    }
}
