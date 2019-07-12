package com.hepta.cliquemedicos.dto.pagseguro;

public class ClienteDTO {
	private String CardNumber;
	private String Expiry;
	private String SecurityCode;
	private String CardholderName;
	private String EmailAddress;
	private String BillingAddressLine1;
	private String BillingAddressPostalCode;
	private String str_cpf_cliente;
	private String str_telefone_cliente;
	private String BillingAddressCountry;
	private String Timestamp;
	private String MerchantID;
	private String OrderId;
	private String Amount;
	private String Currency;
	private String AutoSettleFlag;
	private String HPPVersion;
	private String Sha1hash;

	public ClienteDTO() {
		super();
	}
	
	public Boolean validarCampos() {
		if(this.getCardNumber() != null
		&& this.getExpiry() != null
		&& this.getSecurityCode() != null
		&& this.getCardholderName() != null
		&& this.getEmailAddress() != null
		&& this.getBillingAddressLine1() != null
		&& this.getBillingAddressPostalCode() != null
		&& this.getStr_cpf_cliente() != null
		&& this.getStr_telefone_cliente() != null
		&& this.getBillingAddressCountry() != null
		&& this.getTimestamp() != null
		&& this.getMerchantID() != null
		&& this.getOrderId() != null
		&& this.getAmount() != null
		&& this.getCurrency() != null
		&& this.getAutoSettleFlag() != null
		&& this.getHPPVersion() != null
		&& this.getSha1hash() != null
		&& !this.getCardNumber().isEmpty()
		&& !this.getExpiry().isEmpty()
		&& !this.getSecurityCode().isEmpty()
		&& !this.getCardholderName().isEmpty()
		&& !this.getEmailAddress().isEmpty()
		&& !this.getBillingAddressLine1().isEmpty()
		&& !this.getBillingAddressPostalCode().isEmpty()
		&& !this.getStr_cpf_cliente().isEmpty()
		&& !this.getStr_telefone_cliente().isEmpty()
		&& !this.getBillingAddressCountry().isEmpty()
		&& !this.getTimestamp().isEmpty()
		&& !this.getMerchantID().isEmpty()
		&& !this.getOrderId().isEmpty()
		&& !this.getAmount().isEmpty()
		&& !this.getCurrency().isEmpty()
		&& !this.getAutoSettleFlag().isEmpty()
		&& !this.getHPPVersion().isEmpty()
		&& !this.getSha1hash().isEmpty())
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

	public String getBillingAddressCountry() {
		return BillingAddressCountry;
	}

	public void setBillingAddressCountry(String billingAddressCountry) {
		BillingAddressCountry = billingAddressCountry;
	}

	public String getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}

	public String getMerchantID() {
		return MerchantID;
	}

	public void setMerchantID(String merchantID) {
		MerchantID = merchantID;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	public String getAutoSettleFlag() {
		return AutoSettleFlag;
	}

	public void setAutoSettleFlag(String autoSettleFlag) {
		AutoSettleFlag = autoSettleFlag;
	}

	public String getHPPVersion() {
		return HPPVersion;
	}

	public void setHPPVersion(String hPPVersion) {
		HPPVersion = hPPVersion;
	}

	public String getSha1hash() {
		return Sha1hash;
	}

	public void setSha1hash(String sha1hash) {
		Sha1hash = sha1hash;
	}

}
