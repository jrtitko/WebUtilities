package com.industriousgnomes.web;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebUtilitiesApplication.class)
@WebAppConfiguration
public class WebUtilitiesApplicationTests {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        assertNotNull("the JSON message converter must not be null",
                mappingJackson2HttpMessageConverter);
        
    }

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();

    }
	
	@Test
	public void greeting() throws Exception {
		MvcResult result = mockMvc.perform(get("/greeting")
				.param("name", "Jimmy"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.content", is("Hello, Jimmy!")))
				.andReturn()
				;
		System.out.println("%%% " + result.getResponse().getContentAsString());
	}

	@Test
	public void getInvoice() throws Exception {
		MvcResult result = mockMvc.perform(get("/invoice")
				.param("invoiceNumber", "12345"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.invoiceNumber", is(12345)))
				.andExpect(jsonPath("$.sellerName", is("SellersRUs")))
				.andExpect(jsonPath("$.details[0].item", is("Item 1")))
				.andExpect(jsonPath("$.details[0].quantity", is(5)))
				.andExpect(jsonPath("$.details[1].item", is("Item 2")))
				.andExpect(jsonPath("$.details[1].quantity", is(10)))
				.andExpect(jsonPath("$.details[2].item", is("Item 3")))
				.andExpect(jsonPath("$.details[2].quantity", is(15)))
				.andReturn()
				;
		System.out.println("%%% " + result.getResponse().getContentAsString());
	}

	@Test
	public void postInvoice() throws Exception {
		Invoice invoice = new Invoice();
		invoice.setInvoiceNumber(98765);
		invoice.setSellerName("Dante");
		
		MvcResult result = mockMvc.perform(post("/invoice")
				.content(json(invoice))
				.contentType(contentType))
				.andExpect(status().isOk())
				.andReturn();	// return is optional
		System.out.println("%%% " + result.getResponse().getContentAsString());
	}
	
	protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        String message = mockHttpOutputMessage.getBodyAsString();
        System.out.println("$$$ json = " + message);
        return message;
    }
}
