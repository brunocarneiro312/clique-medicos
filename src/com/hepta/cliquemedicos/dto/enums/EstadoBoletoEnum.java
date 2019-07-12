package com.hepta.cliquemedicos.dto.enums;

public enum EstadoBoletoEnum {
	AGUARDANDO_PAGAMENTO("A"),
	PAGO("P"), 
	CANCELADO("C");

	final private String valor;

	private EstadoBoletoEnum(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}

	public static EstadoBoletoEnum fromString(String x) {
		for (EstadoBoletoEnum e : EstadoBoletoEnum.values()) {
			if (e.getValor().equals(x))
				return e;
		}
		return null;
	}
}
