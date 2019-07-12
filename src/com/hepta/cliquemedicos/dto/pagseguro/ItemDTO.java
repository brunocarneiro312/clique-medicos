package com.hepta.cliquemedicos.dto.pagseguro;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "item")
public class ItemDTO {
	private String id;
	private String description;
	private String amount;
	private String quantity;

	public ItemDTO() {
		super();
	}
	
	public Boolean validarCampos() {
		if(this.getId() != null
		&& this.getDescription() != null
		&& this.getAmount() != null
		&& this.getQuantity() != null
		&& !this.getId().isEmpty()
		&& !this.getDescription().isEmpty()
		&& !this.getAmount().isEmpty()
		&& !this.getQuantity().isEmpty())
			return true;
		return false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

}
