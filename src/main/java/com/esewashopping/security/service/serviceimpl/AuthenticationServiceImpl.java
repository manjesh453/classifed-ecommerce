package com.esewashopping.security.service.serviceimpl;

import com.esewashopping.customer.Customer;
import com.esewashopping.customer.CustomerRepo;
import com.esewashopping.customer.CustomerService;
import com.esewashopping.refreshtoken.RefreshRepo;
import com.esewashopping.refreshtoken.RefreshService;
import com.esewashopping.refreshtoken.RefreshToken;
import com.esewashopping.security.helper.JwtAuthenticationResponse;
import com.esewashopping.security.helper.SignUpRequest;
import com.esewashopping.security.helper.SigninRequest;
import com.esewashopping.security.service.AuthenticationService;
import com.esewashopping.security.service.JwtService;
import com.esewashopping.shared.Role;
import com.esewashopping.shared.Status;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final CustomerRepo customerRepo;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final CustomerService customerService;

    private final RefreshRepo refreshRepo;

    private final RefreshService refreshService;

    @Override
    public String signup(SignUpRequest request, String siteURL) throws MessagingException, UnsupportedEncodingException {
        var user = Customer.builder().fullName(request.getFullName()).contact(request.getContact())
                .address(request.getAddress()).email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER).status(Status.INACTIVE).verificationCode(RandomString.make(64))
                .build();
        Customer customer = customerRepo.findByEmail(user.getEmail());
        if (customer == null) {
            customerRepo.save(user);
            customerService.sendVerification(user, siteURL);
            return "User register successful";
        } else {
            return "Sorry this email has been already register";
        }
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Customer user = customerRepo.findByEmail(request.getEmail());
        RefreshToken refreshToken = refreshRepo.findByCustomer(user);
        if (refreshToken == null) {
            refreshToken = refreshService.createRefreshToken(request.getEmail());
        }
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).refreshToken(refreshToken.getToken()).build();
    }

    @Override
    public String forgetPassword(SigninRequest request, String siteURL) throws MessagingException, UnsupportedEncodingException {
        Customer customer = customerRepo.findByEmail(request.getEmail());
        if (customer != null) {
            customer.setPassword(passwordEncoder.encode(request.getPassword()));
            customer.setVerificationCode(RandomString.make(64));
            customerRepo.save(customer);
            customerService.sendVerification(customer, siteURL);
            return "Verify link send in Email";
        } else {
            return "Sorry invalid Email";
        }
    }
}
