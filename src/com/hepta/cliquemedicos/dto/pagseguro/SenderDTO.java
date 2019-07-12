package com.hepta.cliquemedicos.dto.pagseguro;

import java.util.List;

import com.fasterxml.jackson.xml.annotate.JacksonXmlElementWrapper;
import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "sender")
public class SenderDTO {
	private String hash;
	private String name;
	private String email;
	private PhoneDTO phone;
	@JacksonXmlElementWrapper(localName = "documents")
	private List<DocumentDTO> document;

	public SenderDTO() {
		super();
	}
	
	public boolean validarCampos() {
		if(this.getHash() != null)
			return true;
		
		/*if(this.getHash() != null
		&& this.getName() != null
		&& this.getEmail() != null
		&& this.getPhone() != null
		&& this.getDocument() != null
		&& !this.getHash().isEmpty()
		&& !this.getName().isEmpty()
		&& !this.getEmail().isEmpty()
		&& this.getPhone().validarCampos()) {
			for (int i = 0; i < this.getDocument().size(); i++) {
				if(!this.getDocument().get(0).validarCampos())
					return false;
			}
			return true;
		}*/
			
		return false;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public PhoneDTO getPhone() {
		return phone;
	}

	public void setPhone(PhoneDTO phone) {
		this.phone = phone;
	}

	public List<DocumentDTO> getDocument() {
		return document;
	}

	public void setDocument(List<DocumentDTO> document) {
		this.document = document;
	}

}
