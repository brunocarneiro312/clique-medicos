package com.hepta.cliquemedicos.dto.pagseguro;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "installment")
public class InstallmentDTO {
	private String quantity;
	private String value;
	private String noInterestInstallmentQuantity;

	public InstallmentDTO() {
		super();
	}
	
	public boolean validarCampos() {
		if(this.getQuantity() != null
		&& this.getValue() != null
		&& this.getNoInterestInstallmentQuantity() != null
		&& !this.getQuantity().isEmpty()
		&& !this.getValue().isEmpty()
		&& !this.getNoInterestInstallmentQuantity().isEmpty())
			return true;
		return false;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getNoInterestInstallmentQuantity() {
		return noInterestInstallmentQuantity;
	}

	public void setNoInterestInstallmentQuantity(String noInterestInstallmentQuantity) {
		this.noInterestInstallmentQuantity = noInterestInstallmentQuantity;
	}



}
