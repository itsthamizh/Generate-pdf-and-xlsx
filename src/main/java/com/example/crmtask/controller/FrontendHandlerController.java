package com.example.crmtask.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class FrontendHandlerController {
    /* ========================= for front end request handling controller ======================  */
    @GetMapping("/customer-list")
    public String showCustomerListPage() {
        return "customer-list";
    }
    @GetMapping("/add-customer-frontend")
    public String showAddCustomerForm() {
        return "add-customer";
    }
}
