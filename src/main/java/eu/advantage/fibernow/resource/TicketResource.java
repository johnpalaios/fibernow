package eu.advantage.fibernow.resource;

import eu.advantage.fibernow.converter.DomainToDtoConverter;
import eu.advantage.fibernow.converter.DtoToDomainConverter;
import eu.advantage.fibernow.dto.TicketDto;
import eu.advantage.fibernow.model.Ticket;
import eu.advantage.fibernow.service.TicketService;
import static eu.advantage.fibernow.util.rest.ResponseUtils.*;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Path("/tickets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class TicketResource {
        @Inject
        private TicketService service;

        @GET
        @Path("/{id}")
        public Response getById(@PathParam("id") Long id) {
            return Response.status(Response.Status.ACCEPTED).entity(service.findTicket(id)).build();
        }

        @GET
        public Response search(@QueryParam("customerId") Long customerId, @QueryParam("startDate") String startDateString,
                               @QueryParam("endDate") String endDateString) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = null;
            LocalDate endDate = null;
            if(startDateString != null) {
                startDate = LocalDate.parse(startDateString, formatter);
            }
            if(endDateString != null) {
                endDate = LocalDate.parse(endDateString, formatter);
            }
            List<TicketDto> result = service.searchTickets(customerId, startDate, endDate);
            return successResponse(result);
        }

        @POST
        public Response save(@Valid TicketDto ticketDto) {
            TicketDto result = service.saveTicket(ticketDto);
            return Response.created(UriBuilder
                            .fromResource(eu.advantage.fibernow.resource.TicketResource.class)
                            .path("/" + result.getId())
                            .build()
                    )
                    .entity(toJsonString(result))
                    .build();
        }

        @DELETE
        @Path("/{id}")
        public Response delete(@PathParam("id") Long id) {
            return successResponse(service.deleteTicket(id));
        }

}
