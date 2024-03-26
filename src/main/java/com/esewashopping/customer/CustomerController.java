package com.esewashopping.customer;

import com.esewashopping.shared.ApiResponse;
import com.esewashopping.shared.Status;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/update/{cid}")
    public String updateCustomer(@Valid @RequestBody CustomerDto customerDto, @PathVariable Integer cid) {
        return customerService.updateCustomer(customerDto, cid);
    }

    @GetMapping("/status/{status}")
    public List<CustomerDto> getByStatus(@PathVariable Status status) {
        return customerService.findByStatus(status);
    }

    @PostMapping("/delete/{cid}")
    public ApiResponse deleteCustomer(@PathVariable Integer cid) {
        customerService.deleteCustomer(cid);
        return new ApiResponse("Customer deleted successfully", false);
    }

    @GetMapping("/get-all-customer")
    public List<CustomerDto> getAllCustomer() {
        return customerService.getAllCustomer();
    }

    @GetMapping("/get-by-id/{cid}")
    public CustomerDto getCustomerById(@PathVariable Integer cid) {
        return customerService.findCustomerById(cid);
    }

    @GetMapping("/change-status/{cid}")
    @PreAuthorize("hasRole('ADMIN')")
    public String changeStatus(@PathVariable Integer cid) {
        return customerService.changeStatus(cid);
    }

}
