package com.hepta.cliquemedicos.dto.pagseguro;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "session")
public class SessionDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement
	public String id;

	public SessionDTO(String id) {
		super();
		this.id = id;
	}

	public SessionDTO() {
		super();
	}

	public void setId(String id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
