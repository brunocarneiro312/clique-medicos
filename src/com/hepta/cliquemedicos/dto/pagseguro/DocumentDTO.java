package com.hepta.cliquemedicos.dto.pagseguro;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "document")
public class DocumentDTO {
	private String type;
	private String value;

	public DocumentDTO() {
		super();
	}
	public boolean validarCampos() {
		if(this.getType() != null
		&& this.getValue() != null
		&& !this.getType().isEmpty()
		&& !this.getValue().isEmpty())
			return true;
		
		return false;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
