package com.data.controller;

import com.data.dto.ProductInvoiceDetailDTO;
import com.data.entity.*;
import com.data.service.CustomerService;
import com.data.service.InvoiceDetailService;
import com.data.service.InvoiceService;
import com.data.service.ProductService;
import com.data.ulti.StatusInvoice;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class InvoiceController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceDetailService invoiceDetailService;

    @GetMapping("/invoices")
    public String list(@RequestParam(value = "searchCustomer", required = false) String searchCustomer,
                       @RequestParam(value = "searchDate", required = false) String searchDate,
                       Model model, HttpSession session) {
        session.removeAttribute("invoiceItems");
        List<Invoice> invoices = invoiceService.findAll();

        if (searchCustomer != null && !searchCustomer.trim().isEmpty()) {
            invoices = invoiceService.searchByCustomerName(searchCustomer.trim());
        } else if (searchDate != null && !searchDate.trim().isEmpty()) {
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                df.setLenient(false);
                Date parsedDate = df.parse(searchDate.trim());
                invoices = invoiceService.searchByCreatedDate(parsedDate);
            } catch (ParseException e) {
                model.addAttribute("dateError", "Định dạng ngày không hợp lệ (yyyy-MM-dd)");
            }
        }

        model.addAttribute("searchCustomer", searchCustomer);
        model.addAttribute("searchDate", searchDate);
        model.addAttribute("invoices", invoices);
        return "invoice/list";
    }

    @GetMapping("/invoices/show")
    public String show(Model model, @RequestParam("id") int id) {
        List<InvoiceDetail> invoiceDetails = invoiceDetailService.findAllByIdInvoice(id);
        model.addAttribute("invoiceDetails", invoiceDetails);
        return "invoice/show";
    }

    @GetMapping("/invoices/edit")
    public String edit(Model model, @RequestParam("id") int id) {
        Invoice invoice = invoiceService.findById(id);
        List<InvoiceDetail> invoiceDetails = invoiceDetailService.findAllByIdInvoice(id);

        model.addAttribute("invoice", invoice);
        model.addAttribute("invoiceDetails", invoiceDetails);
        model.addAttribute("statuses", StatusInvoice.values());
        return "invoice/edit";
    }

    @PostMapping("/invoices/updateStatus")
    public String updateStatus(@RequestParam("id") int id,
                               @RequestParam("status") StatusInvoice status) {
        Invoice invoice = invoiceService.findById(id);
        if (invoice != null) {
            invoice.setStatus(status);
            invoiceService.update(invoice);
        }
        return "redirect:/invoices";
    }

    @GetMapping("/invoices/add")
    public String add(Model model, HttpSession session) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);

        // Kiểm tra nếu chưa có khách hàng trong session thì gán khách đầu tiên
        Customer selectedCustomer = (Customer) session.getAttribute("selectedCustomer");

        if (selectedCustomer == null && !customers.isEmpty()) {
            selectedCustomer = customers.get(0); // Mặc định chọn khách đầu tiên
            session.setAttribute("selectedCustomer", selectedCustomer);
        }

        model.addAttribute("selectedCustomer", selectedCustomer);

        return "invoice/add";
    }

    @PostMapping("/invoices/save")
    public String saveInvoice(HttpSession session) {
        Customer customer = (Customer) session.getAttribute("selectedCustomer");
        List<InvoiceItem> invoiceItems = (List<InvoiceItem>) session.getAttribute("invoiceItems");

        if (customer == null || invoiceItems == null || invoiceItems.isEmpty()) {
            return "redirect:/invoices/add";
        }

        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setCreated_at(new Date());
        invoice.setStatus(StatusInvoice.PENDING);
        invoice.setTotal_amount(
                invoiceItems.stream().mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity()).sum()
        );
        invoice.setStatus(StatusInvoice.PENDING);

        invoiceService.save(invoice);

        for (InvoiceItem item : invoiceItems) {
            InvoiceDetail detail = new InvoiceDetail();
            detail.setInvoice(invoice);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            detail.setUnit_price(item.getProduct().getPrice());
            Product product = productService.findById(item.getProduct().getId());
            if (product != null) {
                product.setStock(product.getStock() - item.getQuantity());
                productService.save(product);
            }
            invoiceDetailService.save(detail);
        }

        session.removeAttribute("invoiceItems");
        session.removeAttribute("selectedCustomer");

        return "redirect:/invoices";
    }

    @PostMapping("/invoices/selectCustomer")
    public String selectCustomer(@RequestParam("customerId") int customerId, HttpSession session) {
        Customer customer = customerService.findById(customerId);
        if (customer != null) {
            session.setAttribute("selectedCustomer", customer);
        }
        return "redirect:/invoices/add";
    }

    @GetMapping("/invoices/add/addDetail")
    public String addDetail(Model model, HttpSession session,
                            @RequestParam(value = "productId", required = false) Integer productId) {
        List<Product> products = productService.findAll();
        List<ProductInvoiceDetailDTO> productInvoiceDetailDTOS = new ArrayList<>();

        List<InvoiceItem> invoiceItems = (List<InvoiceItem>) session.getAttribute("invoiceItems");
        for (Product product : products) {
            ProductInvoiceDetailDTO productInvoiceDetailDTO = new ProductInvoiceDetailDTO();
            productInvoiceDetailDTO.setId(product.getId());
            productInvoiceDetailDTO.setName(product.getName());
            productInvoiceDetailDTO.setBrand(product.getBrand());
            productInvoiceDetailDTO.setPrice(product.getPrice());
            productInvoiceDetailDTO.setStock(product.getStock());
            productInvoiceDetailDTO.setImage(product.getImage());
            productInvoiceDetailDTO.setStatus(product.isStatus());
            productInvoiceDetailDTO.setQuantity(1);

            if (invoiceItems != null) {
                boolean exists = invoiceItems.stream()
                        .anyMatch(item -> item.getProduct().getId() == product.getId());
                productInvoiceDetailDTO.setExistInvoice(exists);
                if (exists) {
                    InvoiceItem existingItem = invoiceItems.stream()
                            .filter(item -> item.getProduct().getId() == product.getId())
                            .findFirst()
                            .orElse(null);
                    if (existingItem != null) {
                        productInvoiceDetailDTO.setQuantity(existingItem.getQuantity());
                    }
                } else {
                    productInvoiceDetailDTO.setQuantity(1);
                }
            }

            productInvoiceDetailDTOS.add(productInvoiceDetailDTO);
        }

        model.addAttribute("productInvoiceDetailDTOS", productInvoiceDetailDTOS);
        model.addAttribute("invoiceItems", invoiceItems);
        return "invoice/addDetail";
    }

    @PostMapping("/invoices/addDetail/save")
    public String saveInvoiceItems(@RequestParam("productIds") List<Integer> productIds,
                                   @RequestParam("quantities") List<Integer> quantities,
                                   HttpSession session, Model model) {
//        if (productIds == null || productIds.isEmpty()) {
//            model.addAttribute("error", "Vui lòng chọn ít nhất một sản phẩm!");
//            return addDetail(model, session, null);
//        }
        List<Product> allProducts = productService.findAll();
        List<InvoiceItem> invoiceItems = new ArrayList<>();

        for (int i = 0; i < productIds.size(); i++) {
            int id = productIds.get(i);
            int quantity = quantities.get(i);
            Product product = allProducts.stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .orElse(null);
            if (product != null) {
                invoiceItems.add(new InvoiceItem(product, quantity));
            }
        }

        session.setAttribute("invoiceItems", invoiceItems);
        return "redirect:/invoices/add";
    }

    @GetMapping("/invoices/increase/{index}")
    public String increaseQuantity(@PathVariable int index, HttpSession session) {
        List<InvoiceItem> items = (List<InvoiceItem>) session.getAttribute("invoiceItems");
        if (items != null && index >= 0 && index < items.size()) {
            InvoiceItem item = items.get(index);
            item.setQuantity(item.getQuantity() + 1);
        }
        return "redirect:/invoices/add";
    }

    @GetMapping("/invoices/decrease/{index}")
    public String decreaseQuantity(@PathVariable int index, HttpSession session) {
        List<InvoiceItem> items = (List<InvoiceItem>) session.getAttribute("invoiceItems");
        if (items != null && index >= 0 && index < items.size()) {
            InvoiceItem item = items.get(index);
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            }
        }
        return "redirect:/invoices/add";
    }

    @PostMapping("/invoices/updateQuantity/{index}")
    public String updateQuantity(@PathVariable int index,
                                 @RequestParam("quantity") int quantity,
                                 HttpSession session) {
        List<InvoiceItem> items = (List<InvoiceItem>) session.getAttribute("invoiceItems");
        if (items != null && index >= 0 && index < items.size()) {
            if (quantity > 0) {
                items.get(index).setQuantity(quantity);
            }
        }
        return "redirect:/invoices/add";
    }

    @GetMapping("/invoices/deleteItem/{index}")
    public String deleteItem(@PathVariable int index, HttpSession session) {
        List<InvoiceDetail> invoiceItems = (List<InvoiceDetail>) session.getAttribute("invoiceItems");
        if (invoiceItems != null && index >= 0 && index < invoiceItems.size()) {
            invoiceItems.remove(index);
            session.setAttribute("invoiceItems", invoiceItems);
        }
        return "redirect:/invoices/add";
    }
}