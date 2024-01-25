package com.example.crmtask.controller;

import com.example.crmtask.model.Customer;
import com.example.crmtask.service.CustomerService;
import com.example.crmtask.service.DownloadService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/download")
public class DownloadController {
    @Autowired
    private DownloadService downloadService;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadPdf() throws IOException {

        List<Customer> customers = customerService.listAllCustomers();
        byte[] pdfBytes = downloadService.generatePdf(customers);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "customers.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/xlsx")
    public ResponseEntity<byte[]> downloadCustomers() throws IOException {
        List<Customer> customers = customerService.listAllCustomers();
        Workbook workbook = downloadService.generateExcelWorkbook(customers);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ((Workbook) workbook).write(outputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "customers.xlsx");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(outputStream.toByteArray());
        }
    }
}
