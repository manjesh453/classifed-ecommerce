package com.esewashopping.security.controller;

import com.esewashopping.customer.CustomerService;
import com.esewashopping.security.helper.JwtAuthenticationResponse;
import com.esewashopping.security.helper.SignUpRequest;
import com.esewashopping.security.helper.SigninRequest;
import com.esewashopping.security.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final CustomerService customerService;

    @PostMapping("/sign-up")
    public String signup(@Valid @RequestBody SignUpRequest request, HttpServletRequest httpServletRequest) throws MessagingException, UnsupportedEncodingException {
        return authenticationService.signup(request, getSiteURL(httpServletRequest));
    }

    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signin(@RequestBody SigninRequest request) {
        return authenticationService.signin(request);
    }

    @PostMapping("/forget-password")
    public String forgetPassword(@RequestBody SigninRequest request, HttpServletRequest httpServletRequest) throws MessagingException, UnsupportedEncodingException {
        return authenticationService.forgetPassword(request,getSiteURL(httpServletRequest));
    }


    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public String verifyEmployee(@Param("code") String code) {
        if (customerService.verify(code)) {
            return "Verify sucessfully";
        } else {
            return "verify denied";
        }
    }
}
