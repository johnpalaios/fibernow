package eu.advantage.fibernow.service;

import eu.advantage.fibernow.converter.DomainToDtoConverter;
import eu.advantage.fibernow.dto.CustomerDto;
import eu.advantage.fibernow.exception.BusinessException;
import eu.advantage.fibernow.exception.ExceptionStatus;
import eu.advantage.fibernow.model.Customer;
import eu.advantage.fibernow.model.Ticket;
import eu.advantage.fibernow.model.enums.TicketStatus;
import eu.advantage.fibernow.model.enums.UserStatus;
import eu.advantage.fibernow.repository.CustomerRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerServiceImpl customerService;
    private Ticket ticket = new Ticket();
    private Set<Ticket> tickets = new HashSet<>();

    private Customer customer = new Customer();
    private List<Customer> customers = new ArrayList<>();
    private CustomerDto customerDto;
    private List<CustomerDto> customerDtos = new ArrayList<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        Set<String> phones = new HashSet<>();
        phones.add("+1511651651");

        customer.setName("kostas");
        customer.setSurname("mitsos");
        customer.setTin("12321414");
        customer.setPhoneNumber(phones);
        customer.setAddress("Ermou 14");
        customer.setEmail("kostas@gmail.com");
        customerDto = DomainToDtoConverter.toDto(customer);

        ticket.setAddress("ermou 14");
        ticket.setDescription("plio");
        ticket.setEstimatedCost(new BigDecimal(12.43));
        ticket.setCustomer(customer);
        ticket.setScheduledDatetime(LocalDateTime.now());
        ticket.setReceivedDate(LocalDate.from(ZonedDateTime.now()));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Test when saveCustomer() when nothing wrong happens")
    final void saveCustomerWithNoExceptions() {
        // Test if Customer created by assuring customer got ACTIVE status
        assertEquals(UserStatus.ACTIVE, customerService.saveCustomer(customerDto).getUserStatus());
    }
    @Test
    @DisplayName("Test when saveCustomer() (update) with given ID")
    final void saveCustomerWithGivenIdUpdate() {
        // Test if Customer with given ID is updated
        customerDto.setId(1L);
        when(customerRepository.findById(1L)).thenReturn(customer);
        assertEquals(customerDto, customerService.saveCustomer(customerDto));
    }

    @Test
    @DisplayName("Test when saveCustomer() finds already a customer with the same email")
    final void saveCustomerUniqueEmailException() {
        customers.add(customer);

        when(customerRepository.searchByCriteria("email", customer.getEmail())).thenReturn(customers);
        BusinessException thrown = Assertions.assertThrows(BusinessException.class, () -> {
            customerService.saveCustomer(customerDto);
        });

        assertEquals(thrown.getStatus(), ExceptionStatus.BZ_ERROR_1004);
    }

    @Test
    @DisplayName("Test when saveCustomer() finds already a customer with the same TIN")
    final void saveCustomerUniqueTinException() {
        customers.add(customer);

        when(customerRepository.searchByCriteria("tin", customer.getTin())).thenReturn(customers);
        BusinessException thrown = Assertions.assertThrows(BusinessException.class, () -> {
            customerService.saveCustomer(customerDto);
        });

        assertEquals(thrown.getStatus(), ExceptionStatus.BZ_ERROR_1003);
    }

    @Test
    @DisplayName("Test when saveCustomer() (update) with given ID is not found in DB")
    final void saveCustomerWhenGivenIdNotInDB() {
        customerDto.setId(1L);
        when(customerRepository.findById(1L)).thenReturn(null);
        BusinessException thrown = Assertions.assertThrows(BusinessException.class, () -> {
            customerService.saveCustomer(customerDto);
        });

        assertEquals(thrown.getStatus(), ExceptionStatus.BZ_ERROR_1001);
    }

    @Test
    @DisplayName("Test when saveCustomer() (update) with given ID when same TIN found in DB")
    final void saveCustomerWithGivenIdSameTin() {
        customerDto.setId(1L);
        customer.setId(1L);
        customers.add(customer);

        when(customerRepository.searchByCriteria("tin", customer.getTin())).thenReturn(customers);
        customer.setId(2L);
        when(customerRepository.findById(1L)).thenReturn(customer);
        BusinessException thrown = Assertions.assertThrows(BusinessException.class, () -> {
            customerService.saveCustomer(customerDto);
        });
        assertEquals(thrown.getStatus(), ExceptionStatus.BZ_ERROR_1005);
    }

    @Test
    @DisplayName("Test when saveCustomer() (update) with given ID when same Email found in DB")
    final void saveCustomerWithGivenIdSameEmail() {
        customerDto.setId(1L);
        customer.setId(1L);
        customers.add(customer);

        when(customerRepository.searchByCriteria("email", customer.getEmail())).thenReturn(customers);
        customer.setId(2L);
        when(customerRepository.findById(1L)).thenReturn(customer);
        BusinessException thrown = Assertions.assertThrows(BusinessException.class, () -> {
            customerService.saveCustomer(customerDto);
        });
        assertEquals(thrown.getStatus(), ExceptionStatus.BZ_ERROR_1006);
    }

    @Test
    @DisplayName("Test findCustomer() with given ID")
    final void findCustomerWithNoExceptions() {
        // Find customer
        when(customerRepository.findById(1L)).thenReturn(customer);
        assertEquals(customerDto, customerService.findCustomer(1L));
    }

    @Test
    @DisplayName("Test findCustomer() when ID is not in DB")
    final void findCustomerWhenNotExists() {
        // Test BusinessException BZ_ERROR_1001

        when(customerRepository.findById(anyLong())).thenReturn(null);
        BusinessException thrown = Assertions.assertThrows(BusinessException.class, () -> {
            customerService.findCustomer(anyLong());
        });
        assertEquals(thrown.getStatus(), ExceptionStatus.BZ_ERROR_1001);
    }

    @Test
    @DisplayName("Test searchCustomer() with given TIN when it is found (no exception)")
    void searchCustomerWithGivenTinAndFound() {
        customers.add(customer);
        customerDtos.add(customerDto);

        when(customerRepository.searchByCriteria("tin", customer.getTin())).thenReturn(customers);
        assertEquals(customerDtos, customerService.searchCustomers(null, customer.getTin()));
    }

    @Test
    @DisplayName("Test searchCustomer() with given TIN when it is not found (BZ_ERROR_1008)")
    void searchCustomerWithGivenTinAndNotFound() {
        when(customerRepository.searchByCriteria("tin", customer.getTin())).thenReturn(customers);
        BusinessException thrown = Assertions.assertThrows(BusinessException.class, () -> {
            customerService.searchCustomers(null, customer.getTin());
        });
        assertEquals(thrown.getStatus(), ExceptionStatus.BZ_ERROR_1008);

    }

    @Test
    @DisplayName("Test searchCustomer() with given Email when it is found (no exception)")
    void searchCustomerWithGivenEmailAndFound() {
        customers.add(customer);
        customerDtos.add(customerDto);

        when(customerRepository.searchByCriteria("email", customer.getEmail())).thenReturn(customers);
        assertEquals(customerDtos, customerService.searchCustomers(customer.getEmail(), null));
    }


    @Test
    @DisplayName("Test searchCustomer() with given Email when it is not found (BZ_ERROR_1009)")
    void searchCustomerWithGivenEmailAndNotFound() {
        when(customerRepository.searchByCriteria("email", customer.getEmail())).thenReturn(customers);
        BusinessException thrown = Assertions.assertThrows(BusinessException.class, () -> {
            customerService.searchCustomers(customer.getEmail(), null);
        });
        assertEquals(thrown.getStatus(), ExceptionStatus.BZ_ERROR_1009);

    }

    @Test
    @DisplayName("Test searchCustomer() with null parameters testing findAll()")
    void searchCustomerWithNullEmailAndTin() {
        customers.add(customer);
        customerDtos.add(customerDto);

        when(customerRepository.findAll()).thenReturn(customers);
        assertEquals(customerDtos, customerService.searchCustomers(null, null));
    }

    @Test
    @DisplayName("Test searchCustomer() with null parameters testing findAll() when not any records in DB")
    void searchCustomerWithNullEmailAndTinException() {
        when(customerRepository.findAll()).thenReturn(customers);
        BusinessException thrown = Assertions.assertThrows(BusinessException.class, () -> {
            customerService.searchCustomers(null, null);
        });
        assertEquals(thrown.getStatus(), ExceptionStatus.BZ_ERROR_1002);

    }

    @Test
    @DisplayName("Test deleteCustomer() with given ID and no tickets to delete")
    final void deleteCustomerWithoutTickets() {
        when(customerRepository.findById(anyLong())).thenReturn(customer);
        assertEquals(customerService.deleteCustomer( anyLong()), customerDto);
    }

    @Test
    @DisplayName("Test deleteCustomer() exception when customer has open tickets")
    final void deleteCustomerWithOpenedTickets() {
        tickets.add(ticket);
        customer.setTickets(tickets);

        when(customerRepository.findById(anyLong())).thenReturn(customer);
        BusinessException thrown = Assertions.assertThrows(BusinessException.class, () -> {
            customerService.deleteCustomer( anyLong() );
        });
        assertEquals(thrown.getStatus(), ExceptionStatus.BZ_ERROR_1007);
    }

    @Test
    @DisplayName("Test deleteCustomer() when customer has deleted tickets")
    final void deleteCustomerWithDeletedTickets() {
        ticket.setTicketStatus(TicketStatus.DELETED);
        tickets.add(ticket);
        customer.setTickets(tickets);

        when(customerRepository.findById(anyLong())).thenReturn(customer);
        customerService.deleteCustomer( anyLong() );
        assertEquals(customerDto, customerService.deleteCustomer( anyLong() ));
    }

    @Test
    @DisplayName("Test deleteCustomer() when not exists in DB")
    final void deleteCustomerWhenNotExists() {
        when(customerRepository.findById(anyLong())).thenReturn(null);
        BusinessException thrown = Assertions.assertThrows(BusinessException.class, () -> {
            customerService.deleteCustomer(anyLong());
        });
        assertEquals(thrown.getStatus(), ExceptionStatus.BZ_ERROR_1001);
    }

}