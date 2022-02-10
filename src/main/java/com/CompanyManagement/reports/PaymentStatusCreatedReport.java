package com.CompanyManagement.reports;

import com.CompanyManagement.persistence.entities.Customer;
import com.CompanyManagement.service.InvoiceService;
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
public class PaymentStatusCreatedReport {

    private final InvoiceService invoiceService;
    private float sum;


    public PaymentStatusCreatedReport(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public void export(HttpServletResponse response) {

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA, BaseFont.CP1250, 8);

            Paragraph headerParagraph = new Paragraph();
            headerParagraph.add(new Chunk("List of invoices with payment status: CREATED" + "\n", font));
            headerParagraph.setAlignment(Element.ALIGN_LEFT);
            headerParagraph.add(new Chunk("Date:" + " " + LocalDate.now().format(DateTimeFormatter.ISO_DATE), font));
            headerParagraph.setAlignment(Element.ALIGN_RIGHT);
            headerParagraph.setSpacingAfter(30);
            headerParagraph.setSpacingBefore(30);

            document.add(headerParagraph);


            var invoices = invoiceService.getInvoices();
            Paragraph methodInfo = new Paragraph();
            int num = 0;
            invoices.forEach(i -> {

                if(i.getPaymentStatus().equals("CREATED")) {
                    Customer customer = i.getCustomer();

                    sum += i.getTotalAmount();

                    PdfPTable pdfPTable3 = new PdfPTable(3);
                    PdfPTable pdfPTable4 = new PdfPTable(4);


                    PdfPCell pdfPCell1 = new PdfPCell(new Paragraph("Date of Issue:" + "\n" + i.getDateOfIssue(), font));
                    PdfPCell pdfPCell2 = new PdfPCell(new Paragraph("Due Date:" + "\n" + i.getDueDate(), font));
                    PdfPCell pdfPCell3 = new PdfPCell(new Paragraph("Customer:" + "\n" + customer.getCustomerName() + " " + customer.getSurname(), font));

                    PdfPCell pdfPCell4 = new PdfPCell(new Paragraph("Invoice number:" + " " + i.getInvoiceNumber(), font));
                    PdfPCell pdfPCell5 = new PdfPCell(new Paragraph("Total amount:" + " " + i.getTotalAmount() + "KN", font));
                    PdfPCell pdfPCell6 = new PdfPCell(new Paragraph("Method:" + "\n" + i.getPaymentMethod(), font));
                    PdfPCell PdfPCell7 = new PdfPCell(new Paragraph("Payment Status:" + "\n" + i.getPaymentStatus(), font));


                    pdfPTable3.addCell(pdfPCell1);
                    pdfPTable3.addCell(pdfPCell2);
                    pdfPTable3.addCell(pdfPCell3);

                    pdfPTable4.addCell(pdfPCell4);
                    pdfPTable4.addCell(pdfPCell5);
                    pdfPTable4.addCell(pdfPCell6);
                    pdfPTable4.addCell(PdfPCell7);

                    try {
                        document.add(pdfPTable3);
                        document.add(pdfPTable4);
                        document.add(Chunk.NEWLINE);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
            });
            headerParagraph.setAlignment(Element.ALIGN_RIGHT);
            Font font2 = FontFactory.getFont(FontFactory.HELVETICA, BaseFont.CP1250, 12);
            methodInfo.add(new Chunk("\n" + "Total turnover:" + " " + sum + "KN", font2));
            document.add(methodInfo);


            document.close();

        } catch (Exception e) {
            log.error("ERROR - ", e);
        }
    }
}
