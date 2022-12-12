package eu.advantage.fibernow.service;

import eu.advantage.fibernow.converter.DomainToDtoConverter;
import eu.advantage.fibernow.dto.CustomerDto;
import eu.advantage.fibernow.dto.TicketDto;
import eu.advantage.fibernow.exception.BusinessException;
import eu.advantage.fibernow.exception.ExceptionStatus;
import eu.advantage.fibernow.model.Customer;
import eu.advantage.fibernow.model.Ticket;
import eu.advantage.fibernow.model.enums.TicketStatus;
import eu.advantage.fibernow.repository.CustomerRepository;
import eu.advantage.fibernow.repository.TicketRepository;
import eu.advantage.fibernow.repository.TicketRepositoryImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TicketServiceImplTest {

    @Mock
    private CustomerServiceImpl customerService;
    @Mock
    private TicketRepository ticketRepository;
    @InjectMocks
    private TicketServiceImpl ticketService;
    private Ticket ticket = new Ticket();
    private TicketDto ticketDto = new TicketDto();
    private Set<TicketDto> ticketDtos = new HashSet<>();
    private CustomerDto customerDto = new CustomerDto();
    private Customer customer = new Customer();

    @AfterEach
    void tearDown() {
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ticket.setAddress("ermou 14");
        ticket.setDescription("plio");
        ticket.setEstimatedCost(new BigDecimal(12.43));
        ticket.setCustomer(new Customer());
        ticket.setScheduledDatetime(LocalDateTime.now());
        ticket.setReceivedDate(LocalDate.from(ZonedDateTime.now()));
        ticketDto = DomainToDtoConverter.toDto(ticket);

        Set<String> phones = new HashSet<>();
        phones.add("+1511651651");
        customer.setName("kostas");
        customer.setSurname("mitsos");
        customer.setTin("12321414");
        customer.setPhoneNumber(phones);
        customer.setAddress("Ermou 14");
        customer.setEmail("kostas@gmail.com");
        customerDto = DomainToDtoConverter.toDto(customer);
    }


    @Test
    @DisplayName("Test creation in saveTicket()")
    void createTicketWithNoExceptions() {
        // Test create ticket by assuring TicketStatus is initialized
        customerDto.setId(1L);

        when(customerService.findCustomer(null)).thenReturn(customerDto);
        when(customerService.findCustomer(anyLong())).thenReturn(customerDto);
        assertEquals(1L, ticketService.saveTicket(ticketDto).getCustomerId());
    }

    @Test
    @DisplayName("Test saveTicket() (update) with given ID")
    void updateTicketWithNoExceptions() {
        ticketDto.setId(1L);
        customerDto.setId(1L);
        when(customerService.findCustomer(null)).thenReturn(customerDto);
        when(customerService.findCustomer(anyLong())).thenReturn(customerDto);
        when(ticketRepository.findById(anyLong())).thenReturn(ticket);

        assertEquals(ticketDto, ticketService.saveTicket(ticketDto));
    }

    @Test
    @DisplayName("Test saveTicket() (update) when given ID is not in DB")
    void saveTicketWhenGivenIdNotInDb() {
        ticketDto.setId(1L);
        customerDto.setId(1L);
        when(customerService.findCustomer(null)).thenReturn(customerDto);
        when(customerService.findCustomer(anyLong())).thenReturn(customerDto);

        BusinessException thrown = Assertions.assertThrows(BusinessException.class, () -> {
            ticketService.saveTicket(ticketDto);
        });
        assertEquals(thrown.getStatus(), ExceptionStatus.BZ_ERROR_2001);
    }

    @Test
    @DisplayName("Test findTicket() when ID is not in DB")
    void findTicketWhenNotExists() {
        when(ticketRepository.findById(anyLong())).thenReturn(null);
        BusinessException thrown = Assertions.assertThrows(BusinessException.class, () -> {
            ticketService.findTicket(ticketDto.getId());
        });
        assertEquals(thrown.getStatus(), ExceptionStatus.BZ_ERROR_2001);
    }

    @Test
    @DisplayName("Test findTicket() when ID is in DB")
    void findTicketWhenExists() {
        when(ticketRepository.findById(anyLong())).thenReturn(ticket);
        assertEquals(ticketDto, ticketService.findTicket(anyLong()));
    }

    @Test
    @DisplayName("Test searchTickets() with given customerId and with start date-> findAll tickets between dates")
    void searchTicketsWithGivenCustomerIdAndStartDate() {
        LocalDate date = LocalDate.now();
        LocalDate start = date.minusDays(30);
        LocalDate end = date.minusDays(20);
        LocalDate mid = date.minusDays(25);
        ticketDto.setReceivedDate(mid);
        ticketDtos.add(ticketDto);
        customerDto.setTickets(ticketDtos);

        when(customerService.findCustomer(anyLong())).thenReturn(customerDto);
        assertEquals(ticketDto, ticketService.searchTickets(1L, start, end).get(0));
    }

    @Test
    @DisplayName("Test searchTickets() with given customerId and without start date")
    void searchTicketsWithGivenCustomerIdAndWithoutStartDate() {
        LocalDate date = LocalDate.now();
        LocalDate end = date.minusDays(20);
        LocalDate mid = date.minusDays(25);
        ticketDto.setReceivedDate(mid);
        ticketDtos.add(ticketDto);
        customerDto.setTickets(ticketDtos);

        when(customerService.findCustomer(anyLong())).thenReturn(customerDto);
        assertEquals(ticketDto, ticketService.searchTickets(1L, null, end).get(0));
    }

    @Test
    @DisplayName("Test searchTickets() when all params are null")
    void searchTicketsWhenParamsNull() {
        BusinessException thrown = Assertions.assertThrows(BusinessException.class, () -> {
            ticketService.searchTickets(null, null, null);
        });
        assertEquals(thrown.getStatus(), ExceptionStatus.BZ_ERROR_2003);
    }

    @Test
    @DisplayName("Test deleteTicket() with given ID")
    void deleteTicketWithNoExceptions() {
        when(ticketRepository.findById(anyLong())).thenReturn(ticket);
        assertEquals(TicketStatus.DELETED, ticketService.deleteTicket(anyLong()).getTicketStatus());
    }

    @Test
    @DisplayName("Test deleteTicket() when ticket not exists in DB")
    void deleteTicketWhenNotExists() {
        when(ticketRepository.findById(anyLong())).thenReturn(null);
        BusinessException thrown = Assertions.assertThrows(BusinessException.class, () -> {
            ticketService.deleteTicket(anyLong());
        });
        assertEquals(thrown.getStatus(), ExceptionStatus.BZ_ERROR_2001);
    }

}