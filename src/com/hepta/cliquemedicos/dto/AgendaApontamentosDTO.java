package com.hepta.cliquemedicos.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hepta.cliquemedicos.util.LocalDateTimeDeserializer;

public class AgendaApontamentosDTO {
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	LocalDateTime inicio;
	
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	LocalDateTime fim;

	public AgendaApontamentosDTO() {
		super();
	}

	public LocalDateTime getInicio() {
		return inicio;
	}

	public void setInicio(LocalDateTime inicio) {
		this.inicio = inicio;
	}

	public LocalDateTime getFim() {
		return fim;
	}

	public void setFim(LocalDateTime fim) {
		this.fim = fim;
	}

}
