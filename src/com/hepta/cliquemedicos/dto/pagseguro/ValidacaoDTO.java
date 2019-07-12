package com.hepta.cliquemedicos.dto.pagseguro;

public class ValidacaoDTO {
	private String CardNumber;
	private String Expiry;
	private String SecurityCode;
	private String CardholderName;
	private String EmailAddress;
	private String BillingAddressLine1;
	private String BillingAddressPostalCode;
	private String str_cpf_cliente;
	private String str_telefone_cliente;

	public ValidacaoDTO() {
		super();
	}
	public Boolean validarCampos() {
		if( this.getCardNumber() != null
		&& this.getExpiry() != null
		&& this.getSecurityCode() != null
		&& this.getCardholderName() != null
		&& this.getEmailAddress() != null
		&& this.getBillingAddressLine1() != null
		&& this.getBillingAddressPostalCode() != null
		&& this.getStr_cpf_cliente() != null
		&& this.getStr_telefone_cliente() != null
		&& !this.getCardNumber().isEmpty()
		&& !this.getExpiry().isEmpty()
		&& !this.getSecurityCode().isEmpty()
		&& !this.getCardholderName().isEmpty()
		&& !this.getEmailAddress().isEmpty()
		&& !this.getBillingAddressLine1().isEmpty()
		&& !this.getBillingAddressPostalCode().isEmpty()
		&& !this.getStr_cpf_cliente().isEmpty()
		&& !this.getStr_telefone_cliente().isEmpty())
			return true;
		return false;
	}

	public String getCardNumber() {
		return CardNumber;
	}

	public void setCardNumber(String cardNumber) {
		CardNumber = cardNumber;
	}

	public String getExpiry() {
		return Expiry;
	}

	public void setExpiry(String expiry) {
		Expiry = expiry;
	}

	public String getSecurityCode() {
		return SecurityCode;
	}

	public void setSecurityCode(String securityCode) {
		SecurityCode = securityCode;
	}

	public String getCardholderName() {
		return CardholderName;
	}

	public void setCardholderName(String cardholderName) {
		CardholderName = cardholderName;
	}

	public String getEmailAddress() {
		return EmailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		EmailAddress = emailAddress;
	}

	public String getBillingAddressLine1() {
		return BillingAddressLine1;
	}

	public void setBillingAddressLine1(String billingAddressLine1) {
		BillingAddressLine1 = billingAddressLine1;
	}

	public String getBillingAddressPostalCode() {
		return BillingAddressPostalCode;
	}

	public void setBillingAddressPostalCode(String billingAddressPostalCode) {
		BillingAddressPostalCode = billingAddressPostalCode;
	}

	public String getStr_cpf_cliente() {
		return str_cpf_cliente;
	}

	public void setStr_cpf_cliente(String str_cpf_cliente) {
		this.str_cpf_cliente = str_cpf_cliente;
	}

	public String getStr_telefone_cliente() {
		return str_telefone_cliente;
	}

	public void setStr_telefone_cliente(String str_telefone_cliente) {
		this.str_telefone_cliente = str_telefone_cliente;
	}

}
