package eu.advantage.fibernow.resource;

import eu.advantage.fibernow.dto.TicketDto;
import eu.advantage.fibernow.service.TicketService;
import static eu.advantage.fibernow.util.rest.ResponseUtils.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Path("ticket")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TicketResource {
        @Inject
        private TicketService service;

        @GET
        @Path("/{id}")
        public Response getById(@PathParam("id") Long id) {
            return successResponse(service.findTicket(id));
        }

        @GET
        public Response search(@QueryParam("id") Long customerId, @QueryParam("startDate") String startDateString,
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
            List<TicketDto> ticketDtoList = service.searchTickets(customerId, startDate, endDate);
            return successResponse(ticketDtoList);
        }

        @POST
        public Response save(TicketDto ticketDto) {
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
            TicketDto ticketDto = service.findTicket(id);
            return successResponse(service.deleteTicket(ticketDto));
        }

}
