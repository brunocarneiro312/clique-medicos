package com.hepta.cliquemedicos.dto.enums;

public enum EstadoCompraEnum {
	AGUARDANDO_PAGAMENTO(1),
	EM_ANALISE(2),
	PAGO(3), 
	CANCELADA(7);

	final private Integer valor;

	private EstadoCompraEnum(Integer valor) {
		this.valor = valor;
	}

	public Integer getValor() {
		return valor;
	}

	public static EstadoCompraEnum fromInt(int x) {
		for (EstadoCompraEnum e : EstadoCompraEnum.values()) {
			if (e.getValor() == x)
				return e;
		}
		return null;
	}

}
