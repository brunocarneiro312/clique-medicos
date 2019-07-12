package com.hepta.cliquemedicos.dto.pagseguro;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "paymentMethod")
public class PaymentMethodDTO {
	private String type;
	private String code;

	public PaymentMethodDTO() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
