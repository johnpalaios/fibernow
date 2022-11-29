package eu.advantage.fibernow.resource;

import eu.advantage.fibernow.dto.CustomerDto;
import eu.advantage.fibernow.model.Customer;
import eu.advantage.fibernow.service.CustomerService;
import eu.advantage.fibernow.util.rest.ResponseUtils;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

@Path("customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @Inject
    private CustomerService service;

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return ResponseUtils.successResponse(service.findCustomer(id));
    }

    @GET
    public Response search(@QueryParam("tin") String tin, @QueryParam("email") String email) {
        return ResponseUtils.successResponse(service.searchCustomer(tin, email));
    }

    @POST
    public Response save(CustomerDto customerDto) {
        CustomerDto result = service.saveCustomer(customerDto);
        return Response.created(UriBuilder
                .fromResource(CustomerResource.class)
                .path("/" + result.getId())
                .build()
        )
                .entity(ResponseUtils.toJsonString(result))
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return ResponseUtils.successResponse(service.deleteCustomer(id));
    }
}
