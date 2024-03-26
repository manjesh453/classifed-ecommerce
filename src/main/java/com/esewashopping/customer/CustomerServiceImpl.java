package com.esewashopping.customer;

import com.esewashopping.exception.ResourceNotFoundException;
import com.esewashopping.shared.ErrorMessage;
import com.esewashopping.shared.Status;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final ModelMapper modelMapper;

    private final CustomerRepo customerRepo;

    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender mailSender;

    @Override
    public String updateCustomer(CustomerDto customerDto, Integer cid) {
        Customer existingCustomer = customerRepo.findById(cid).orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerId", cid));
        Customer updateCustomer = modelMapper.map(customerDto, Customer.class);
        existingCustomer.setContact(updateCustomer.getContact());
        existingCustomer.setEmail(updateCustomer.getEmail());
        existingCustomer.setAddress(updateCustomer.getAddress());
        existingCustomer.setFullName(updateCustomer.getFullName());
        existingCustomer.setPassword(passwordEncoder.encode(updateCustomer.getPassword()));
        customerRepo.save(existingCustomer);
        return "Updated Successfully";
    }

    @Override
    public CustomerDto findCustomerById(Integer cid) {
        Customer customer = customerRepo.findById(cid).orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerId", cid));
        return modelMapper.map(customer, CustomerDto.class);
    }

    @Override
    public void deleteCustomer(Integer cid) {
        Customer customer = customerRepo.findById(cid).orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerId", cid));
        customer.setStatus(Status.DELETED);
        customerRepo.save(customer);
    }

    @Override
    public List<CustomerDto> findByStatus(Status status) {
        List<Customer> statusCustomer = customerRepo.findByStatus(status);
        return statusCustomer.stream().map(li -> modelMapper.map(li, CustomerDto.class)).toList();
    }

    @Override
    public String changeStatus(Integer cid) {
        Customer customer = customerRepo.findById(cid).orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerId", cid));
        customer.setStatus(Status.VERIFIED);
        customerRepo.save(customer);
        return "Successfully change Status";
    }

    @Override
    public List<CustomerDto> getAllCustomer() {
        List<Customer> listOfCustomer = customerRepo.findAll();
        return listOfCustomer.stream().map(list -> modelMapper.map(list, CustomerDto.class)).toList();
    }


    @Override
    public void sendVerification(Customer customer, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = customer.getEmail();
        String fromAddress = ErrorMessage.fromAddress;
        String senderName = ErrorMessage.senderName;
        String subject = ErrorMessage.subject;
        String content = "Dear " + customer.getFullName() + "<br>"
                + "Please click the link below to verify:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Esewa-Store Management.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", customer.getUsername());
        String verifyURL = siteURL + "/api/v1/auth/verify?code=" + customer.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @Override
    public boolean verify(String code) {
        Customer customer = this.customerRepo.findByVerificationCode(code);
        if (customer == null) {
            return false;
        } else {
            customer.setVerificationCode(null);
            if(customer.getStatus()==Status.INACTIVE){
                customer.setStatus(Status.UNVERIFIED);
            }
            this.customerRepo.save(customer);
            return true;
        }
    }
}
