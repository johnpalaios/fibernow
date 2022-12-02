package eu.advantage.fibernow.resource;

import eu.advantage.fibernow.converter.DomainToDtoConverter;
import eu.advantage.fibernow.converter.DtoToDomainConverter;
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
        Customer customer = service.findCustomer(id);
        CustomerDto customerDto = DomainToDtoConverter.toDto(customer);
        return ResponseUtils.successResponse(customerDto);
    }

    @GET
    public Response search(@QueryParam("tin") String tin, @QueryParam("email") String email) {
        Customer customer = service.searchCustomer(tin, email);
        CustomerDto customerDto = DomainToDtoConverter.toDto(customer);
        return ResponseUtils.successResponse(customerDto);
    }

    @POST
    public Response save(CustomerDto customerDto) {
        Customer customer = DtoToDomainConverter.toDomain(customerDto);
        Customer resultDomain = service.saveCustomer(customer);
        CustomerDto resultDto = DomainToDtoConverter.toDto(resultDomain);
        return Response.created(UriBuilder
                .fromResource(CustomerResource.class)
                .path("/" + resultDto.getId())
                .build()
        )
                .entity(ResponseUtils.toJsonString(resultDto))
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        Customer customer = service.deleteCustomer(id);
        CustomerDto customerDto = DomainToDtoConverter.toDto(customer);
        return ResponseUtils.successResponse(customerDto);
    }
}
