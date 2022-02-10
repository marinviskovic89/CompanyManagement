package com.CompanyManagement.reports;

import com.CompanyManagement.persistence.entities.Customer;
import com.CompanyManagement.persistence.entities.Employee;
import com.CompanyManagement.persistence.entities.ItemInvoice;
import com.CompanyManagement.web.dto.InvoiceDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;

@Slf4j
public class InvoicePdfView {

    InvoiceDto invoice;

    public InvoicePdfView(InvoiceDto invoice) {
        this.invoice = invoice;
    }

    public void export(HttpServletResponse response) {

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            //response.setHeader("Content-Disposition", "attachment; filename=\"invoice.pdf\"");

            Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, BaseFont.CP1250, 16);

            Paragraph merchantInfo = new Paragraph();
            merchantInfo.add(new Chunk("Company Management \n", font));
            merchantInfo.add(new Chunk("all your parts in one place \n", font));
            merchantInfo.add(new Chunk("Address: Antuna Gustava Matoša 18 \n", font));
            merchantInfo.add(new Chunk("E-mail: company.management@gmail.com \n", font));
            merchantInfo.add(new Chunk("Owners:\nMarin Visković\nJosipa Tokić\nVesna Šimundić Bendić", font));
            merchantInfo.setSpacingAfter(15);
            document.add(merchantInfo);

            Customer customer = invoice.getCustomer();

            Paragraph customerInfo = new Paragraph();
            customerInfo.add(new Chunk(customer.getCustomerName() + " " + customer.getSurname() + "\n", font));
            customerInfo.add(new Chunk("OIB:" + " " + customer.getOib() + "\n", font));
            customerInfo.add(new Chunk("Address:" + " " + customer.getAddress() + "\n", font));
            customerInfo.add(new Chunk("City:" + " " + customer.getCity() + "\n", font));
            customerInfo.add(new Chunk("Contact:" + " " + customer.getTelephone() + "\n", font));
            customerInfo.setSpacingAfter(15);
            customerInfo.setAlignment(Element.ALIGN_RIGHT);
            document.add(customerInfo);


            Paragraph headerParagraph = new Paragraph();
            headerParagraph.add(new Chunk("Invoice: " + invoice.getInvoiceNumber() + "\n", font));
            headerParagraph.setAlignment(Element.ALIGN_CENTER);
            headerParagraph.setSpacingAfter(30);
            headerParagraph.setSpacingBefore(30);


            document.add(headerParagraph);


            Employee employee = invoice.getEmployee();

            PdfPTable table = new PdfPTable(1);
            table.setSpacingAfter(20);
            PdfPCell cell = null;
            cell = new PdfPCell(new Phrase("Employee info", font));
            cell.setPadding(8f);
            table.addCell(cell);
            table.addCell("Employee: " + employee.getEmployeeName() + ' ' + employee.getSurname());
            table.addCell("Employee Email: " + employee.getEmail());

            //Details
            PdfPTable table2 = new PdfPTable(1);
            table2.setSpacingAfter(20);
            cell = new PdfPCell(new Phrase("Invoice info", font));
            // cell.setBackgroundColor(new BaseColor(76, 67, 151));
            cell.setPadding(8f);
            table2.addCell(cell);
            table2.addCell("Invoice number: " + invoice.getInvoiceNumber());
            table2.addCell("Date of issue: " + invoice.getDateOfIssue());
            table2.addCell("Due date: " + invoice.getDueDate());
            table2.addCell("Payment status: " + invoice.getPaymentStatus());
            table2.addCell("Payment method: " + invoice.getPaymentMethod());
            table2.addCell("Discount: " + invoice.getDiscount() + '%');


            PdfPTable items = new PdfPTable(4);
            //items.setWidths(new float[]{3.5f, 1, 1, 1});
            items.addCell("Item");
            items.addCell("Price");
            items.addCell("Quantity");
            items.addCell("Total");

            for (ItemInvoice item : invoice.getItems()) {
                items.addCell(item.getItem().getItemName());
                items.addCell(String.valueOf(item.getItem().getPrice()));
                cell = new PdfPCell(new Phrase(String.valueOf(item.getAmount())));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                items.addCell(cell);
                items.addCell(String.valueOf(item.calculateImport()));
            }

            cell = new PdfPCell(new Phrase("Total"));
            cell.setColspan(3);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            items.addCell(cell);
            items.addCell(String.valueOf(invoice.getTotalAmount()+ "kn"));

            document.add(table);
            document.add(table2);
            document.add(items);

            document.close();

        } catch (Exception e) {
            log.error("ERROR - ", e);
        }
    }
}
