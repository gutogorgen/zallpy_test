package com.evaluation.webproject.utils;

public class Util {


    public static final boolean validateStringValue(String... values) {
        for (String value : values) {
            if (value == null || value.isEmpty()) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public static final boolean validateIntegerValue(Integer... values) {
        for (Integer value : values) {
            if (value == null) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public static final boolean validadeCPF(Long cpf) {
        if (cpf == null) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


}
