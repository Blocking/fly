package com.zhangxy.mvc;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.zhangxy.entity.Customer;
import com.zhangxy.service.CustomerService;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Controller
public class CustomerApiController {
    private Logger log = Logger.getLogger(getClass());

    @Autowired  private CustomerService customerService;

    public static final String CUSTOMERS_ENTRY_URL = "/crm/customers";
    public static final String CUSTOMERS_SEARCH_URL = "/crm/search";
    public static final String CUSTOMERS_BY_ID_ENTRY_URL = CUSTOMERS_ENTRY_URL + "/{id}";
    
    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @ResponseBody
    @RequestMapping(value = CUSTOMERS_SEARCH_URL, method = RequestMethod.GET)
    public Collection<Customer> search(@RequestParam("q") String query) throws Exception {
        Collection<Customer> customers = customerService.search(query);
        if (log.isDebugEnabled())
            log.debug(String.format("retrieved %s results for search query '%s'", Integer.toString(customers.size()), query));
        return customers;
    }

    @ResponseBody
    @RequestMapping(value = CUSTOMERS_BY_ID_ENTRY_URL, method = RequestMethod.GET)
    public Customer customerById(@PathVariable  Long id) {
        return this.customerService.getCustomerById(id);
    }

    @ResponseBody
    @RequestMapping(value = CUSTOMERS_ENTRY_URL, method = RequestMethod.GET)
    public List<Customer> customers() {
        return this.customerService.getAllCustomers();
    }

    @ResponseBody
    @RequestMapping(value = CUSTOMERS_ENTRY_URL, method = RequestMethod.PUT)
    public Long addCustomer(@RequestParam String firstName, @RequestParam String lastName) {
        return customerService.createCustomer(firstName, lastName, new Date()).getId();
    }

    @ResponseBody
    @RequestMapping(value = CUSTOMERS_BY_ID_ENTRY_URL, method = RequestMethod.POST)
    public Long updateCustomer(@PathVariable  Long id, @RequestBody Customer customer) {
        customerService.updateCustomer(id, customer.getFirstName(), customer.getLastName(), customer.getSignupDate());
        return id;
    }
}