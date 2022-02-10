package com.CompanyManagement.api;

import com.CompanyManagement.exception.NotEnoughQuantityException;
import com.CompanyManagement.persistence.entities.Invoice;
import com.CompanyManagement.persistence.repositories.InvoiceRepository;
import com.CompanyManagement.reports.InvoicePdfView;
import com.CompanyManagement.service.CustomerService;
import com.CompanyManagement.service.InvoiceService;
import com.CompanyManagement.service.ItemService;
import com.CompanyManagement.util.MapperUtils;
import com.CompanyManagement.util.PaymentMethods;
import com.CompanyManagement.util.PaymentStatus;
import com.CompanyManagement.web.SearchParams;
import com.CompanyManagement.web.dto.InvoiceDto;
import com.CompanyManagement.web.dto.ItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/invoices")
@SessionAttributes("invoice")
public class InvoiceController {

    private final ItemService itemService;
    private final InvoiceService invoiceService;
    private final InvoiceRepository invoiceRepository;
    private final CustomerService customerService;

    public InvoiceController(ItemService itemService, InvoiceService invoiceService, InvoiceRepository invoiceRepository, CustomerService customerService) {
        this.itemService = itemService;
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
        this.customerService = customerService;
    }

    @PostMapping
    public void createInvoice(@RequestBody Invoice invoice) {
        invoiceService.createInvoice(invoice);
    }

    @GetMapping("listOfInvoices")
    public String getInvoices(Model model) {
        model.addAttribute("invoices", MapperUtils.mapList(invoiceService.getInvoices(), InvoiceDto.class));
        model.addAttribute("searchParams", new SearchParams());
        return "invoice-list";
    }

    /**
     * Returns form for creating new item.
     */
    @GetMapping("/invoice")
    public String getCreateItemForm(Model model) {
        model.addAttribute("invoice", new InvoiceDto());
        model.addAttribute("customers", customerService.getCustomers());
        model.addAttribute("items", MapperUtils.mapList(itemService.getItems(), ItemDto.class));
        model.addAttribute("paymentMethods", PaymentMethods.values());
        model.addAttribute("paymentStatus", PaymentStatus.values());
        return "invoice-add";
    }

    @PostMapping("/invoice")
    public String createInvoice(@Valid InvoiceDto invoice, BindingResult result, Model model,
                             @RequestParam(name = "item_id[]", required = false) UUID[] itemId,
                             @RequestParam(name = "amount[]", required = false) Integer[] amount) {
        if (result.hasErrors()) {
            model.addAttribute("invoice", invoice);
            model.addAttribute("customers", customerService.getCustomers());
            return "invoice-add";
        }

        try {
            invoiceService.createInvoice(invoice, itemId, amount);
        } catch (NotEnoughQuantityException e) {
            model.addAttribute("invoice", invoice);
            model.addAttribute("customers", customerService.getCustomers());
            model.addAttribute("info", e.getMessage());
            return "invoice-add";
        }
        return "redirect:/invoices/viewPage";
    }

    /**
     * Product autocomplete. Called by javascript.
     */
    @GetMapping(value = "/load-products/{term}", produces = {"application/json"})
    public @ResponseBody
    List<ItemDto> loadProducts(@PathVariable String term) {
        return MapperUtils.mapList(itemService.getItems(term), ItemDto.class);
    }

    //UPDATE PAYMENT STATUS
    @PostMapping("/update/{id}")
    public String updateStatus(@PathVariable(value = "id") UUID id, InvoiceDto invoice) {
        Invoice invoiceEntity = invoiceService.getById(id);
        invoiceEntity.setPaymentStatus(invoice.getPaymentStatus());
        invoiceRepository.save(invoiceEntity);
        return "redirect:/invoices/view/" + id;
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable(value = "id") UUID id, Model model) {
        InvoiceDto invoice = MapperUtils.mapObject(invoiceService.getById(id), InvoiceDto.class);
        if (invoice == null) {
            return "redirect:";
        }
        model.addAttribute("invoice", invoice);
        return "invoice-detail";
    }

    /**
     * Generates .pdf document based on selected invoice.
     */
    @GetMapping("/view/{id}/pdf")
    public void downloadExcel(@PathVariable(value = "id") UUID id, HttpServletResponse response) {
        InvoiceDto invoice = MapperUtils.mapObject(invoiceService.getById(id), InvoiceDto.class);
        response.setContentType("application/pdf");
        String headerValue = "attachment; filename=invoice_" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".pdf";
        response.setHeader("Content-Disposition", headerValue);
        InvoicePdfView exporter = new InvoicePdfView(invoice);
        exporter.export(response);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") UUID id) {
        invoiceService.deleteInvoice(id);
        return "redirect:/invoices/viewPage";
    }

    //NOVI PAGING
    @RequestMapping("/viewPage")
    public String viewPage(Model model) {
        return listByPage(model, 1, "dateOfIssue", "asc", null);
    }

    @PostMapping("/search")
    public String searchInvoices(Model model, SearchParams searchParams) {
        return listByPage(model, 1, "dateOfIssue", "asc", searchParams);
    }

    @GetMapping(value = {"/page/{pageNumber}","/page/{pageNumber}/{customerName}"})
    public String listByPage(Model model,
                             @PathVariable("pageNumber") int currentPage,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @PathVariable(required = false) SearchParams searchParams) {

        Page<Invoice> page = invoiceService.listAll(currentPage, sortField, sortDir, searchParams);
        int totalElements = (int) page.getTotalElements();
        int totalPages = page.getTotalPages();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("searchParams", searchParams == null ? new SearchParams() : searchParams);
        model.addAttribute("invoices", MapperUtils.mapList(page.getContent(), InvoiceDto.class));

        String reverseSortDir =sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("reverseSortDir", reverseSortDir);

        return "invoice-list";
    }


}
