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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rrooaarr.werkstueck.R;
import com.rrooaarr.werkstueck.booking.BookingActivity;
import com.rrooaarr.werkstueck.booking.BookingViewModel;
import com.rrooaarr.werkstueck.booking.model.Action;
import com.rrooaarr.werkstueck.booking.model.WorkpieceContainer;
import com.rrooaarr.werkstueck.databinding.ActivityWsinfoBinding;
import com.rrooaarr.werkstueck.setting.UserSetting;
import com.rrooaarr.werkstueck.util.StringValidationRules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rrooaarr.werkstueck.booking.model.AppDefaults.ACTION;
import static com.rrooaarr.werkstueck.booking.model.AppDefaults.BOOK;
import static com.rrooaarr.werkstueck.booking.model.AppDefaults.PK;
import static com.rrooaarr.werkstueck.booking.model.AppDefaults.WST;

public class WerkstückinfoActivity extends AppCompatActivity implements View.OnClickListener {

    private BookingViewModel model;
    WorkpieceListAdapter workpieceAdapter;
    RecyclerView recyclerView;

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
        String wst = (String) getIntent().getSerializableExtra(WST);
        LifecycleOwner lifecycleOwner = this;
        model.getSetting().observe(lifecycleOwner, new Observer<UserSetting>() {
            @Override
            public void onChanged(UserSetting setting) {
                model.initApi(setting);
                model.fetchWorkpieceInfo(wst);
                model.getWorkpieceInfoData().observe(lifecycleOwner, new Observer<WorkpieceContainer>() {
                    @Override
                    public void onChanged(WorkpieceContainer workpiece) {
                        List<WorkpieceListElement> wst_liste = new ArrayList<>(workpiece.getWst_infos().size());

                        for (Map.Entry<String, String> stringStringEntry : workpiece.getWst_infos().entrySet()) {
                            wst_liste.add( new WorkpieceListElement(stringStringEntry.getKey(), stringStringEntry.getValue()));
                        }

                        workpieceAdapter.setWorkpieces(wst_liste);
                        model.setPK(workpiece.getPk());
                    }
                });
            }
        });

        // Note: ViewmodelProvider(this).get(class) gives per fragment/activity an own instance of ViewModel
        Action action = (Action) getIntent().getSerializableExtra(ACTION);
        model.setAction(action);

        initViews(binding.getRoot());
    }

    private void initViews(View view) {
        recyclerView = findViewById(R.id.wsinfo_list);

        final Button button = findViewById(R.id.button_book_action);
        button.setOnClickListener(this);

        final Button button_cancel = findViewById(R.id.button_back);
        button_cancel.setOnClickListener(this);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        if (workpieceAdapter == null) {
            workpieceAdapter = new WorkpieceListAdapter(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(workpieceAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setNestedScrollingEnabled(true);
        } else {
            workpieceAdapter.notifyDataSetChanged();
        }
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

        final Intent intent = new Intent(WerkstückinfoActivity.this, BookingActivity.class);
        intent.putExtra(ACTION, model.getAction());
        intent.putExtra(BOOK, true);
        intent.putExtra(PK, model.getPk());

        startActivity(intent);
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