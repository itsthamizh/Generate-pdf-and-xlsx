package com.example.crmtask.service;

import com.example.crmtask.model.Customer;
import com.example.crmtask.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    public String addCustomer(Customer customer){
        UUID randomUUID = UUID.randomUUID();
        customer.setCustomerID(String.valueOf(randomUUID));
        customerRepository.save(customer);
        return "customer added succrss";
    }
    public List<Customer> listAllCustomers(){
        List<Customer> customerList = customerRepository.findAll();
        return customerList;
    }
    public boolean deleteCustomer(String customerID){
        try {
            customerRepository.deleteById(customerID);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
