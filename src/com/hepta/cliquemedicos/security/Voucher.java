package com.hepta.cliquemedicos.security;

public class Voucher {
	public static Long MAX = 9999999999L;
	public static Long MIN = 0L;

	public static Long gerar() {
		return MIN + (long) (Math.random() * (MAX - MIN));
	}

	public static String gerarString() {
		String aux = String.format("%010d", gerar());
		return aux;
	}
}
