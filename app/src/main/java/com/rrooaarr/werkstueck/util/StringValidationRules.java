package com.rrooaarr.werkstueck.util;

import android.text.Editable;
import android.text.TextUtils;

public class StringValidationRules {

    public static StringRule NOT_EMPTY = new StringRule() {
        @Override
        public boolean validate(Editable s) {
            return TextUtils.isEmpty(s.toString());
        }
    };

    public static StringRule SERVER = new StringRule() {
        @Override
        public boolean validate(Editable s) {
            boolean result = false;
            final String string = s.toString();
            if(TextUtils.isEmpty(string))
                result = true;
            if(!string.matches("^(http|https|Http|Https|HTTP|HTTPS)://.*$")){
                result = true;
            }

            return result;
        }
    };

    public static StringRule EMAIL = new StringRule() {
        @Override
        public boolean validate(Editable s) {
            return !android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();
        }
    };

    public static StringRule PASSWORD = new StringRule() {
        @Override
        public boolean validate(Editable s) {
            return s.length() < 8;
        }
    };

    public static StringRule PORT = new StringRule() {
        @Override
        public boolean validate(Editable s) {
            if(TextUtils.isEmpty(s.toString())){
                return true;
            }
            String potentialNumber = s.toString();
            int value;
            try {
                 value = Integer.valueOf(potentialNumber);
            } catch (NumberFormatException nfe){
                  value = -1;
            }
            return !(value < 65535 || value > 0);
        }
    };

    public interface StringRule {
        boolean validate(Editable s);
    }
}