package com.industriousgnomes.web;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	
	@RequestMapping(value="/greeting")
	public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
	    System.out.println("*** greeting: name = " + name);
		return new Greeting(counter.incrementAndGet(),
							String.format(template, name));
	}
	
	@RequestMapping(value = "/invoice", method = RequestMethod.GET)
	public Invoice getInvoice(@RequestParam("invoiceNumber") Integer invoiceNumber) {
	    System.out.println("*** getInvoice: invoiceNumber = " + invoiceNumber);
	    Invoice invoice = new Invoice();
	    invoice.setInvoiceNumber(invoiceNumber);
	    invoice.setSellerName("SellersRUs");
	    
	    List<InvoiceDetail> details = Arrays.asList(
	    		new InvoiceDetail("Item 1", 5),
	    		new InvoiceDetail("Item 2", 10),
	    		new InvoiceDetail("Item 3", 15)
	    );
	    
	    invoice.setDetails(details);
	    System.out.println("*** getInvoice: invoice = " + invoice);
	    return invoice;
	}

	@RequestMapping(value = "/invoice", method = RequestMethod.POST)
	public String postInvoice(@RequestBody Invoice invoice) {
	    System.out.println("*** postInvoice: invoice = " + invoice);
	    return "Success";
	}	
}
