package com.hepta.cliquemedicos.dto.pagseguro;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "phone")
public class PhoneDTO {
	private String areaCode;
	private String number;

	public PhoneDTO() {
		super();
	}
	
	public boolean validarCampos() {
		if(this.getAreaCode() != null
		&& this.getNumber() != null
		&& !this.getAreaCode().isEmpty()
		&& !this.getNumber().isEmpty())
			return true;
		return false;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	

}
