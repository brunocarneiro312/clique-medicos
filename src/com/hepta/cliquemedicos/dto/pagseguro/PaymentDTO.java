package com.hepta.cliquemedicos.dto.pagseguro;

import java.util.List;

import com.fasterxml.jackson.xml.annotate.JacksonXmlElementWrapper;
import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "payment")
public class PaymentDTO {
	private String mode;
	private String method;
	private String currency;
	private SenderDTO sender;
	@JacksonXmlElementWrapper(localName = "items")
	private List<ItemDTO> item;
	private CreditCardDTO creditCard;
	private String shippingAddressRequired;
	private ShippingDTO shipping;

	public PaymentDTO(PaymentDTO a) {
		super();
		this.setCreditCard(a.getCreditCard());
		this.setCurrency(a.getCurrency());
		this.setItem(a.getItem());
		this.setMethod(a.getMethod());
		this.setMode(a.getMode());
		this.setSender(a.getSender());
		this.setShipping(a.getShipping());
		this.setShippingAddressRequired(a.getShippingAddressRequired());
	}

	public PaymentDTO() {
		// TODO Auto-generated constructor stub
	}

	public Boolean validarCampos() {
		if (this.getCreditCard() != null 
				&& this.getSender() != null
				&& this.getSender().validarCampos()
				&& this.getCreditCard().validarCampos())
			return true;
				
		/*if (this.getMode() != null 
		&& this.getMethod() != null 
		&& this.getCurrency() != null 
		&& this.getSender() != null
		&& this.getItem() != null
		&& this.getCreditCard() != null 
		&& !this.getMode().isEmpty()
		&& !this.getMethod().isEmpty() 
		&& !this.getCurrency().isEmpty()
		&& !this.getShippingAddressRequired().isEmpty() 
		&& this.getSender().validarCampos()
		&& this.getCreditCard().validarCampos()) {
			for (int i = 0; i < this.getItem().size(); i++) {
				if (!this.getItem().get(i).validarCampos())
					return false;
			}
			return true;
		}*/
		return false;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public SenderDTO getSender() {
		return sender;
	}

	public void setSender(SenderDTO sender) {
		this.sender = sender;
	}

	public List<ItemDTO> getItem() {
		return item;
	}

	public void setItem(List<ItemDTO> item) {
		this.item = item;
	}

	public CreditCardDTO getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCardDTO creditCard) {
		this.creditCard = creditCard;
	}

	public String getShippingAddressRequired() {
		return shippingAddressRequired;
	}

	public void setShippingAddressRequired(String shippingAdressRequired) {
		this.shippingAddressRequired = shippingAdressRequired;
	}

	public ShippingDTO getShipping() {
		return shipping;
	}

	public void setShipping(ShippingDTO shipping) {
		this.shipping = shipping;
	}
	
}
