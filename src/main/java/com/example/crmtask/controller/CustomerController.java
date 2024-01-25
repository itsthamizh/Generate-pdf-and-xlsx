package com.example.crmtask.controller;

import com.example.crmtask.model.Customer;
import com.example.crmtask.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/add-customer")
    public ResponseEntity<String> addCustomer(@RequestBody Customer customer) {
        String response = customerService.addCustomer(customer);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-customer")
    public ResponseEntity<String> getCustomer() {
        String message = "called by get user";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/get-all-customer")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.listAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @PutMapping("/update-customer")
    public ResponseEntity<String> updateCustomer() {
        String message = "called by update user";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @DeleteMapping("/delete-customer")
    public ResponseEntity<String> get() {
        String message = "called by delete user";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
