package com.esewashopping.security.service;

import com.esewashopping.security.helper.JwtAuthenticationResponse;
import com.esewashopping.security.helper.SignUpRequest;
import com.esewashopping.security.helper.SigninRequest;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface AuthenticationService {
    String signup(SignUpRequest request, String siteURL) throws MessagingException, UnsupportedEncodingException;

    JwtAuthenticationResponse signin(SigninRequest request);

    String forgetPassword(SigninRequest request, String siteURL) throws MessagingException, UnsupportedEncodingException;

}
