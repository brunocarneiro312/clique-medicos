package com.hepta.cliquemedicos.dto.pagseguro;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.xml.annotate.JacksonXmlElementWrapper;

@XmlRootElement(name = "transaction")
public class TransactionDTO {
	private String date;
	private String code;
	private String type;
	private String status;
	private String lastEventDate;
	private PaymentMethodDTO paymentMethod;
	private String grossAmount;
	private String discountAmount;
	private String feeAmount;
	private String netAmount;
	private String extraAmount;
	private String installmentCount;
	private String cancellationSource;
	private String itemCount;
	@JacksonXmlElementWrapper(localName = "items")
	private List<ItemDTO> item;
	private SenderDTO sender;
	private ShippingDTO shipping;
	private GatewaySystemDTO gatewaySystem;

	public TransactionDTO() {
		super();
	}

	public String getCancellationSource() {
		return cancellationSource;
	}

	public void setCancellationSource(String cancellationSource) {
		this.cancellationSource = cancellationSource;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastEventDate() {
		return lastEventDate;
	}

	public void setLastEventDate(String lastEventDate) {
		this.lastEventDate = lastEventDate;
	}

	public PaymentMethodDTO getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethodDTO paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getGrossAmount() {
		return grossAmount;
	}

	public void setGrossAmount(String grossAmount) {
		this.grossAmount = grossAmount;
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(String feeAmount) {
		this.feeAmount = feeAmount;
	}

	public String getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(String netAmount) {
		this.netAmount = netAmount;
	}

	public String getExtraAmount() {
		return extraAmount;
	}

	public void setExtraAmount(String extraAmount) {
		this.extraAmount = extraAmount;
	}

	public String getInstallmentCount() {
		return installmentCount;
	}

	public void setInstallmentCount(String installmentCount) {
		this.installmentCount = installmentCount;
	}

	public String getItemCount() {
		return itemCount;
	}

	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}

	public List<ItemDTO> getItem() {
		return item;
	}

	public void setItem(List<ItemDTO> item) {
		this.item = item;
	}

	public SenderDTO getSender() {
		return sender;
	}

	public void setSender(SenderDTO sender) {
		this.sender = sender;
	}

	public ShippingDTO getShipping() {
		return shipping;
	}

	public void setShipping(ShippingDTO shipping) {
		this.shipping = shipping;
	}

	public GatewaySystemDTO getGatewaySystem() {
		return gatewaySystem;
	}

	public void setGatewaySystem(GatewaySystemDTO gatewaySystem) {
		this.gatewaySystem = gatewaySystem;
	}

}
