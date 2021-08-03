package br.com.builders.cliente.helper;

import java.util.Random;

public class CpfHelper {

    public static String generateCPF(boolean valid) {
        String partial = "";
        String cpf = "";
        Integer number;

        for (int i = 0; i < 9; i++) {
            number = Integer.valueOf((int) (Math.random() * 10));
            partial += number.toString();
        }

        if(valid) {
            cpf = partial + calculateVerificationDigit(partial);
        } else {
            Random random = new Random();
            long elevenDigits = (random.nextInt(1000000000) + (random.nextInt(90) + 10) * 1000000000L);
            cpf = String.valueOf(elevenDigits);
        }

        return cpf;
    }

    public static String calculateVerificationDigit(String num) {

        Integer primDig, segDig;
        int sum = 0, peso = 10;
        for (int i = 0; i < num.length(); i++)
            sum += Integer.parseInt(num.substring(i, i + 1)) * peso--;

        if (sum % 11 == 0 | sum % 11 == 1)
            primDig = Integer.valueOf(0);
        else
            primDig = Integer.valueOf(11 - (sum % 11));

        sum = 0;
        peso = 11;
        for (int i = 0; i < num.length(); i++)
            sum += Integer.parseInt(num.substring(i, i + 1)) * peso--;

        sum += primDig.intValue() * 2;
        if (sum % 11 == 0 | sum % 11 == 1)
            segDig = Integer.valueOf(0);
        else
            segDig = Integer.valueOf(11 - (sum % 11));

        return primDig.toString() + segDig.toString();
    }
}
