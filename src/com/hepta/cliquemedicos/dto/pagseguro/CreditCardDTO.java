package com.hepta.cliquemedicos.dto.pagseguro;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "creditCard")
public class CreditCardDTO {
	private String token;
	private InstallmentDTO installment;
	private HolderDTO holder;
	private BillingAddressDTO billingAddress;
	

	public CreditCardDTO() {
		super();
	}
	
	public boolean validarCampos() {
		if(this.getToken() != null
				&& this.getHolder() != null
				&& this.getBillingAddress() != null
				&& !this.getToken().isEmpty()
				&& this.getHolder().validarCampos()
				&& this.getBillingAddress().validarCampos())
					return true;
		
		/*if(this.getToken() != null
		&& this.getInstallment() != null
		&& this.getHolder() != null
		&& this.getBillingAddress() != null
		&& !this.getToken().isEmpty()
		&& this.getInstallment().validarCampos()
		&& this.getHolder().validarCampos()
		&& this.getBillingAddress().validarCampos())
			return true;*/
		return false;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public InstallmentDTO getInstallment() {
		return installment;
	}

	public void setInstallment(InstallmentDTO installment) {
		this.installment = installment;
	}

	public HolderDTO getHolder() {
		return holder;
	}

	public void setHolder(HolderDTO holder) {
		this.holder = holder;
	}

	public BillingAddressDTO getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(BillingAddressDTO billingAddress) {
		this.billingAddress = billingAddress;
	}


	
}
