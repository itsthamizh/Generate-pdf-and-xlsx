package com.example.crmtask.controller;

import com.example.crmtask.model.Customer;
import com.example.crmtask.service.CustomerService;
import com.example.crmtask.service.DownloadService;
import com.lowagie.text.DocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.thymeleaf.context.Context;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/download")
public class DownloadController {
    @Autowired
    private DownloadService downloadService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("/pdf")
    public ResponseEntity<InputStreamResource> generatePdf(Model model) {
        List<Customer> customers = customerService.listAllCustomers();
        model.addAttribute("customers", customers);

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(model.asMap());
        String htmlContent = templateEngine.process("pdfTemplate", thymeleafContext);

        try {
            //html to pdf
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(pdfOutputStream);

            //pdf to streams
            ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfOutputStream.toByteArray());

            //set the headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=customerDetails.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(inputStream));
        } catch (DocumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
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
