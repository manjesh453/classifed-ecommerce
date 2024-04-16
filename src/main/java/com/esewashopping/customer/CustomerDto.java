package com.esewashopping.customer;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerDto {
    @JsonAlias({"full_name","FullName"})
    @Pattern(regexp = "[a-zA-Z]{10,50}", message = "firstName must be between 10 and 50 characters and contain only letters")
    private String fullName;

    @Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Please provide a valid email address")
    private String email;

    @Pattern(regexp = "^(?=.?[A-Z])(?=.?[a-z])(?=.?[0-9])(?=.?[#?!@$%^&*-]).{6,20}$", message = "Password must meet the specified criteria")
    private String password;

    @Pattern(regexp = "[a-zA-Z]{2,50}", message = "firstName must be between 2 and 50 characters and contain only letters")
    private String address;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be a 10-digit number")
    private String contact;

    public CustomerDto(String fullName, String email, String password, String address, String contact) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.contact = contact;
    }

}
