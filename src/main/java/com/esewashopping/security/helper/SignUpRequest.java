package com.esewashopping.security.helper;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @JsonAlias("full_name")
    @Pattern(regexp = "[a-zA-Z]{7,50}", message = "firstName must be between 7 and 50 characters and contain only letters")
    private String fullName;

    @Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]$", message = "Please provide a valid email address")
    private String email;

    @Pattern(regexp = "^(?=.?[A-Z])(?=.?[a-z])(?=.?[0-9])(?=.?[#?!@$%^&*-]).{8,20}$", message = "Password must meet the specified criteria")
    private String password;

    @Pattern(regexp = "[a-zA-Z]{5,20}", message = "firstName must be between 5 and 20 characters and contain only letters")
    private String address;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be a 10-digit number")
    private String contact;
}
