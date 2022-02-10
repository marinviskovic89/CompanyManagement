package com.CompanyManagement.reports;

import com.CompanyManagement.persistence.entities.Customer;
import com.CompanyManagement.persistence.entities.Employee;
import com.CompanyManagement.service.InvoiceService;
import com.google.common.collect.Table;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class InvoiceReport {

    private final InvoiceService invoiceService;
    private float sum;

    public InvoiceReport(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public void export(HttpServletResponse response) {

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA, BaseFont.CP1250, 8);

            Paragraph headerParagraph = new Paragraph();
            headerParagraph.add(new Chunk("List of all invoices and their total turnover" + "\n", font));
            headerParagraph.setAlignment(Element.ALIGN_LEFT);
            headerParagraph.add(new Chunk("Date:" + " " + LocalDate.now().format(DateTimeFormatter.ISO_DATE), font));
            headerParagraph.setAlignment(Element.ALIGN_RIGHT);
            headerParagraph.setSpacingAfter(30);
            headerParagraph.setSpacingBefore(30);

            document.add(headerParagraph);


            var invoices = invoiceService.getInvoices();
            Paragraph invoicesInfo = new Paragraph();
            int num = 0;
            invoices.forEach(i -> {
                Customer customer = i.getCustomer();
                Employee employee = i.getEmployee();

                sum += i.getTotalAmount();

                PdfPTable pdfPTable = new PdfPTable(2);
                PdfPTable pdfPTable2 = new PdfPTable(2);
                PdfPCell pdfPCell4 = new PdfPCell(new Paragraph("Total amount:" + " " + i.getTotalAmount() + "KN", font));
                PdfPCell pdfPCell2 = new PdfPCell(new Paragraph("Customer:" + "\n" + customer.getCustomerName() + " " + customer.getSurname(), font));
                PdfPCell pdfPCell3 = new PdfPCell(new Paragraph("Invoice number:" + " " + i.getInvoiceNumber(), font));
                PdfPCell pdfPCell1 = new PdfPCell(new Paragraph("Employee:" + "\n" + employee.getEmployeeName() + " " + employee.getSurname(), font));

                pdfPTable.addCell(pdfPCell1);
                pdfPTable.addCell(pdfPCell3);
                pdfPTable2.addCell(pdfPCell2);
                pdfPTable2.addCell(pdfPCell4);
                try {
                    document.add(pdfPTable);
                    document.add(pdfPTable2);
                    document.add( Chunk.NEWLINE );
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            });
            headerParagraph.setAlignment(Element.ALIGN_RIGHT);
            Font font2 = FontFactory.getFont(FontFactory.HELVETICA, BaseFont.CP1250, 12);
            invoicesInfo.add(new Chunk("\n" + "Total turnover:" + " " + sum + "KN", font2));
            document.add(invoicesInfo);


            document.close();

        } catch (Exception e) {
            log.error("ERROR - ", e);
        }
    }
}
