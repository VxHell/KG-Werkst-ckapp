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

    public interface StringRule {
        boolean validate(Editable s);
    }
}