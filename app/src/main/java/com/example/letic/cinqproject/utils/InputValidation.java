package com.example.letic.cinqproject.utils;

import android.content.Context;
import android.widget.EditText;

/**
 * Created by letic on 07/09/2018.
 */

public class InputValidation {
    private Context context;

    public InputValidation(Context context) {
        this.context = context;
    }

    public boolean isEditTextFilled(EditText editText, String message){
        String input = editText.getText().toString().trim();
        if(input.isEmpty()){
            editText.setError(message);
            return false;
        }else{
            editText.setError(null);
        }
        return true;
    }

    public boolean isInputEditTextEmail(EditText email, String message) {
        String value = email.getText().toString().trim();
        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            email.setError(message);
            return false;
        } else {
            email.setError(null);
        }
        return true;
    }

    public boolean isInputEditTextMatches(EditText editText1, EditText editText2, String message) {
        String value1 = editText1.getText().toString().trim();
        String value2 = editText2.getText().toString().trim();
        if (!value1.contentEquals(value2)) {
            editText1.setError(message);
            return false;
        } else {
            editText1.setError(null);
        }
        return true;
    }
}
