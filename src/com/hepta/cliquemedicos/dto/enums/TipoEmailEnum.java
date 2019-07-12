package com.hepta.cliquemedicos.dto.enums;

public enum TipoEmailEnum {
	VERIFICACAO(0), 
	AGENDAMENTO_CLIENTE(1), 
	AGENDAMENTO_ASSOCIADO(2),
	BOLETO(3),
	ALTERAR_SENHA(4), 
	AGENDAMENTO_PAGO(5),
	AGENDAMENTO_CANCELADO(6),
	AGENDAMENTO_ASSOCIADO_CANCELADO(7);
	
	final private Integer valor;

	private TipoEmailEnum(Integer valor) {
		this.valor = valor;
	}

	public Integer getValor() {
		return valor;
	}

}
