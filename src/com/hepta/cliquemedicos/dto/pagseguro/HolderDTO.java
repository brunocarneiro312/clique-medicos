package com.hepta.cliquemedicos.dto.pagseguro;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.xml.annotate.JacksonXmlElementWrapper;
import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "holder")
public class HolderDTO {
	private String name;
	@JsonProperty("documents")
	@JacksonXmlElementWrapper(localName = "documents")
	private List<DocumentDTO> document;
	private String birthDate;
	private PhoneDTO phone;

	public HolderDTO() {
		super();
	}
	public boolean validarCampos() {
		if(this.getName() != null
			&& this.getBirthDate() != null
			&& this.getDocument() != null
			&& this.getPhone() != null
			&& !this.getName().isEmpty() 
			&& !this.getBirthDate().isEmpty()
			&& this.getPhone().validarCampos()) {
			{
				for (int i = 0; i < this.getDocument().size(); i++) {
					if(!this.getDocument().get(0).validarCampos())
						return false;
				}
			}
			return true;
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public List<DocumentDTO> getDocument() {
		return document;
	}
	public void setDocument(List<DocumentDTO> document) {
		this.document = document;
	}
	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public PhoneDTO getPhone() {
		return phone;
	}

	public void setPhone(PhoneDTO phone) {
		this.phone = phone;
	}

}
