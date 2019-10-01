package com.rrooaarr.werkstueck.wsinfo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.rrooaarr.werkstueck.R;
import com.rrooaarr.werkstueck.booking.BookingViewModel;
import com.rrooaarr.werkstueck.databinding.ActivityWsinfoBinding;
import com.rrooaarr.werkstueck.util.StringValidationRules;

public class WerkstückinfoActivity extends AppCompatActivity implements View.OnClickListener {

    private BookingViewModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupBindings();
    }

    private void setupBindings() {
        // TODO consider to make viewmodel singelton for instance Dagger
        model = new ViewModelProvider(this).get(BookingViewModel.class);
        ActivityWsinfoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_wsinfo);
        binding.setModel(model);

        initViews(binding.getRoot());
    }

    private void initViews(View view) {

        final Button button = findViewById(R.id.button_book_action);
        button.setOnClickListener(this);

        final Button button_cancel = findViewById(R.id.button_back);
        button_cancel.setOnClickListener(this);

//        model.getSetting().observe(this, new Observer<UserSetting>() {
//            @Override
//            public void onChanged(UserSetting setting) {
//            }
//        });

    }

    @BindingAdapter({"validation", "errorMsg"})
    public static void setErrorEnable(EditText editText, StringValidationRules.StringRule stringRule, final String errorMsg) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (stringRule.validate(editText.getText())) {
                    editText.setError(errorMsg);
                } else {
                    editText.setError(null);
                }
            }
        });
    }

    private void onAction(){
        Intent replyIntent = new Intent();


    }

    private void onBack(){
       onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_book_action:
                onAction();
                break;
            case R.id.button_back:
                onBack();
                break;
        }
    }
}