package com.esewashopping.customer;

import com.esewashopping.exception.ResourceNotFoundException;
import com.esewashopping.shared.Status;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void updateCustomer() {
        Integer customerId = 1;
        CustomerDto customerDto = new CustomerDto("New Name", "newemail@example.com", passwordEncoder.encode("newpassword"), "New Address", "New Contact");
        Customer existingCustomer = Customer.builder()
                .cId(customerId)
                .fullName("Old Name")
                .email("oldemail@example.com")
                .password(passwordEncoder.encode("Oldpassword"))
                .address("Old Address")
                .contact("Old Contact")
                .build();
        when(customerRepo.findById(customerId)).thenReturn(Optional.ofNullable(existingCustomer));
        when(modelMapper.map(customerDto, Customer.class)).thenReturn(existingCustomer);
        String result = customerService.updateCustomer(customerDto, customerId);
        assertEquals("Updated Successfully", result);
    }

    @Test
    void findCustomerById() {
        Integer customerId = 1;
        CustomerDto customerDto = new CustomerDto("New Name", "newemail@example.com", passwordEncoder.encode("newpassword"), "New Address", "New Contact");
        Customer existingCustomer = Customer.builder()
                .cId(customerId)
                .fullName("New Name")
                .email("newemail@example.com")
                .password(passwordEncoder.encode("newpassword"))
                .address("New Address")
                .contact("New Contact")
                .build();
        when(customerRepo.findById(1)).thenReturn(Optional.ofNullable(existingCustomer));
        when(modelMapper.map(existingCustomer, CustomerDto.class)).thenReturn(customerDto);
        CustomerDto testData = customerService.findCustomerById(1);
        assertEquals(customerDto, testData);

    }

    @Test
    void deleteCustomer() {
        Integer customerId = 2;
        Customer customer = new Customer();
        when(customerRepo.findById(2)).thenReturn(Optional.ofNullable(customer));
        customerService.deleteCustomer(customerId);
        assert (customer.getStatus() == Status.DELETED);
    }

    @Test
    void findByStatus() {
        List<Customer> customers = Arrays.asList(new Customer(1, "Manjesh Rayamajhi", "rayaman@gamil.com", passwordEncoder.encode("password"), "Chitwan", "9868384322", Status.ACTIVE)
                , new Customer(2, "Sudarshan uprety", "suds@gamil.com", passwordEncoder.encode("newpassword"), "Parsa", "9868385322", Status.ACTIVE)
        );

        List<CustomerDto> customerDtoList = Arrays.asList(new CustomerDto("Manjesh Rayamahi", "rayaman@gamil.com", passwordEncoder.encode("password"), "Chitwan", "9868384322")
                , new CustomerDto("Sudarshan uprety", "suds@gamil.com", passwordEncoder.encode("newpassword"), "Parsa", "9868385322")

        );

        when(customerRepo.findByStatus(Status.ACTIVE)).thenReturn(customers);
        for (int i = 0; i < customers.size(); i++) {
            when(modelMapper.map(customers.get(i), CustomerDto.class)).thenReturn(customerDtoList.get(i));
        }
        List<CustomerDto> customerDtos = customerService.findByStatus(Status.ACTIVE);
        assertEquals(customerDtoList, customerDtos);
    }

    @Test
    void changeStatus() {
        Integer customerId = 2;
        Customer customer = new Customer();
        when(customerRepo.findById(customerId)).thenReturn(Optional.of(customer));
        String result = customerService.changeStatus(customerId);
        assertEquals("Successfully change Status", result);
    }

    @Test
    void getAllCustomer() {
        List<Customer> customers = Arrays.asList(new Customer(1, "Manjesh Rayamajhi", "rayaman@gamil.com", passwordEncoder.encode("password"), "Chitwan", "9868384322", Status.ACTIVE)
                , new Customer(2, "Sudarshan uprety", "suds@gamil.com", passwordEncoder.encode("newpassword"), "Parsa", "9868385322", Status.ACTIVE)
        );

        List<CustomerDto> customerDtoList = Arrays.asList(new CustomerDto("Manjesh Rayamahi", "rayaman@gamil.com", passwordEncoder.encode("password"), "Chitwan", "9868384322")
                , new CustomerDto("Sudarshan uprety", "suds@gamil.com", passwordEncoder.encode("newpassword"), "Parsa", "9868385322")

        );

        when(customerRepo.findAll()).thenReturn(customers);
        for (int i = 0; i < customers.size(); i++) {
            when(modelMapper.map(customers.get(i), CustomerDto.class)).thenReturn(customerDtoList.get(i));
        }
        List<CustomerDto> customerDtos = customerService.getAllCustomer();
        assertEquals(customerDtoList, customerDtos);

    }

    @Test
    void whenThrown_ResourceNotFoundException() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            customerService.findCustomerById(1);
        });
        String expectedMessage = "Customer not found with CustomerId : 1";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testSendVerificationEmail() throws MessagingException, UnsupportedEncodingException {

        Customer customer = new Customer();
        customer.setEmail("manjesh.rayamajhi@esewa.com.np");
        customer.setVerificationCode("98937");
        customer.setFullName("Manjesh bahadur Rayamajhi");
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(mailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        customerService.sendVerification(customer, "http:localhost:9090");
        verify(mailSender).createMimeMessage();
        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(any(MimeMessage.class));

    }
}