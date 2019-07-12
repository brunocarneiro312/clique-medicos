package com.hepta.cliquemedicos.util;

public class ValidadorCPF {
	public static boolean validar(String cpf) {
		cpf = cpf.replace(".", "").replace("-", "");
		if (cpf.equals("00000000000") || cpf.equals("11111111111") || cpf.equals("22222222222")
				|| cpf.equals("33333333333") || cpf.equals("44444444444") || cpf.equals("55555555555")
				|| cpf.equals("66666666666") || cpf.equals("77777777777") || cpf.equals("88888888888")
				|| cpf.equals("99999999999") || cpf.length() != 11)
			return false;

		Integer digito1 = primeiroDigito(cpf);
		Integer digito2 = segundoDigito(cpf);
		Integer char9 = Character.getNumericValue(cpf.charAt(9));
		Integer char10 = Character.getNumericValue(cpf.charAt(10));

		if (digito1 == null || digito2 == null || char9 < 0 || char10 < 0)
			return false;

		if (char9.equals(digito1) && char10.equals(digito2))
			return true;
		else
			return false;
	}

	private static Integer primeiroDigito(String cpf) {
		Integer soma = 0;
		Integer peso = 10;
		for (int i = 0; i < 9; i++) {
			Integer num = Character.getNumericValue(cpf.charAt(i));
			if (num < 0)
				return null;
			soma += (num * peso);
			peso--;
		}

		Integer resto = soma % 11;
		if (resto < 2)
			return 0;
		else
			return 11 - resto;
	}

	private static Integer segundoDigito(String cpf) {
		Integer soma = 0;
		Integer peso = 11;
		for (int i = 0; i < 10; i++) {
			Integer num = Character.getNumericValue(cpf.charAt(i));
			if (num < 0)
				return null;
			soma += (num * peso);
			peso--;
		}

		Integer resto = soma % 11;
		if (resto < 2)
			return 0;
		else
			return 11 - resto;
	}

}
