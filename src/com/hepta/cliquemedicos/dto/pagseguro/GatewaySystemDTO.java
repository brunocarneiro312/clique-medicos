package com.hepta.cliquemedicos.dto.pagseguro;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "gatewaySystem")
public class GatewaySystemDTO {
	private String type;
	private String rawCode;
	private String rawMessage;
	private String normalizedCode;
	private String normalizedMessage;
	private String authorizationCode;
	private String nsu;
	private String tid;
	private String establishmentCode;
	private String acquirerName;

	public GatewaySystemDTO() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRawCode() {
		return rawCode;
	}

	public void setRawCode(String rawCode) {
		this.rawCode = rawCode;
	}

	public String getRawMessage() {
		return rawMessage;
	}

	public void setRawMessage(String rawMessage) {
		this.rawMessage = rawMessage;
	}

	public String getNormalizedCode() {
		return normalizedCode;
	}

	public void setNormalizedCode(String normalizedCode) {
		this.normalizedCode = normalizedCode;
	}

	public String getNormalizedMessage() {
		return normalizedMessage;
	}

	public void setNormalizedMessage(String normalizedMessage) {
		this.normalizedMessage = normalizedMessage;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public String getNsu() {
		return nsu;
	}

	public void setNsu(String nsu) {
		this.nsu = nsu;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getEstablishmentCode() {
		return establishmentCode;
	}

	public void setEstablishmentCode(String establishmentCode) {
		this.establishmentCode = establishmentCode;
	}

	public String getAcquirerName() {
		return acquirerName;
	}

	public void setAcquirerName(String acquirerName) {
		this.acquirerName = acquirerName;
	}

}
