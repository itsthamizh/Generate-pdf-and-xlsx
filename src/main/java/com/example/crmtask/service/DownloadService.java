package com.example.crmtask.service;

import com.example.crmtask.model.Customer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class DownloadService {
    public byte[] generatePdf(List<Customer> customers) throws IOException {
        //Generating PDF
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                PDType0Font font = PDType0Font.load(document, new File("/home/muthamizh-15019/FullStack/crm-task/src/main/resources/font/LiberationSans-Bold.ttf"));
                contentStream.setFont(font, 12);

                contentStream.beginText();
                contentStream.newLineAtOffset(15, 750);
                contentStream.showText("        ****************** Customer Details ***************** ");
                contentStream.newLineAtOffset(0, -20);

                contentStream.endText();

                float yStart = 710;
                float yPosition = yStart;
                for (Customer customer : customers) {
                    contentStream.beginText();

                    contentStream.newLineAtOffset(15, yPosition);
                    contentStream.showText("FirstName: " + customer.getFirstName() +
                            " LastName: " + customer.getLastName() +
                            " Company: " + customer.getCompanyName() +
                            " Address: " + customer.getAddress() +
                            " PhoneNumber: " + customer.getPhoneNumber());
                    contentStream.newLineAtOffset(0, -20);

                    contentStream.endText();
                    yPosition -= 40;
                }
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
    public Workbook generateExcelWorkbook(List<Customer> customers) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Customers");

        // Adding header names to the sheet
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("First Name");
        headerRow.createCell(1).setCellValue("Last Name");
        headerRow.createCell(2).setCellValue("Address");
        headerRow.createCell(2).setCellValue("phoneNumber");

        // Adding data to the sheet
        int rowNum = 1;
        for (Customer customer : customers) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(customer.getFirstName());
            row.createCell(1).setCellValue(customer.getLastName());
            row.createCell(2).setCellValue(customer.getAddress());
            row.createCell(3).setCellValue(customer.getPhoneNumber());
        }

        return workbook;
    }
}
