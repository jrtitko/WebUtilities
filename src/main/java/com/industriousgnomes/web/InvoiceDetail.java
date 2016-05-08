package com.industriousgnomes.web;

public class InvoiceDetail {

	private String item;
	private Integer quantity;

	public InvoiceDetail() {} // Needed for JSON
	
	public InvoiceDetail(String item, Integer quantity) {
		this.item = item;
		this.quantity = quantity;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "InvoiceDetail [item=" + item + ", quantity=" + quantity + "]";
	}

}
