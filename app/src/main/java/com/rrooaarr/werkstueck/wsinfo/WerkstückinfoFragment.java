package com.rrooaarr.werkstueck.wsinfo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rrooaarr.werkstueck.R;
import com.rrooaarr.werkstueck.booking.BookingViewModel;
import com.rrooaarr.werkstueck.booking.model.WorkpieceContainer;
import com.rrooaarr.werkstueck.databinding.FragmentWsinfoBinding;
import com.rrooaarr.werkstueck.util.StringValidationRules;
import com.rrooaarr.werkstueck.util.Utils;
import com.rrooaarr.werkstueck.view.FragmentBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.rrooaarr.werkstueck.booking.model.AppDefaults.ACTION;
import static com.rrooaarr.werkstueck.booking.model.AppDefaults.BOOK;
import static com.rrooaarr.werkstueck.booking.model.AppDefaults.PK;
import static com.rrooaarr.werkstueck.booking.model.AppDefaults.WST;

public class WerkstückinfoFragment extends Fragment implements FragmentBase, View.OnClickListener{

    public static String TAG = WerkstückinfoFragment.class.getSimpleName();
    private BookingViewModel model;
    WorkpieceListAdapter workpieceAdapter;
    RecyclerView recyclerView;
    private View myRoot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = ViewModelProviders.of(getActivity()).get(BookingViewModel.class);
        model.setNavtitel("Buchen");
        final Bundle arguments = getArguments();
        if(arguments != null ) {
            String wst = arguments.getString(WST);

            if (model.getSetting().getValue() != null) {
                model.initApi(model.getSetting().getValue());

                model.fetchWorkpieceInfo(wst);
                model.getWorkpieceInfoData().observe(this, new Observer<WorkpieceContainer>() {
                    @Override
                    public void onChanged(WorkpieceContainer workpiece) {
                        if(workpiece != null) {
                            List<WorkpieceListElement> wst_liste = new ArrayList<>(workpiece.getWst_infos().size());

                            for (Map.Entry<String, String> stringStringEntry : workpiece.getWst_infos().entrySet()) {
                                wst_liste.add(new WorkpieceListElement(stringStringEntry.getKey(), stringStringEntry.getValue()));
                            }

                            workpieceAdapter.setWorkpieces(wst_liste);
                            model.setPK(workpiece.getPk());
                        } else {
                            Toast.makeText(getContext(), R.string.no_service, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), R.string.no_settings, Toast.LENGTH_LONG).show();
            }

        }
    }

    public WerkstückinfoFragment() {
    }


    public static WerkstückinfoFragment newInstance() {

        return new WerkstückinfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (myRoot == null) {
            FragmentWsinfoBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wsinfo, container, false);
            binding.setModel(model);
            View fragmentView = binding.getRoot();
            initViews(fragmentView);
            myRoot = fragmentView;
        }

        return myRoot;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.wsinfo_list);

        final Button button = view.findViewById(R.id.button_book_action);
        button.setOnClickListener(this);

        final Button button_cancel = view.findViewById(R.id.button_back);
        button_cancel.setOnClickListener(this);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        if (workpieceAdapter == null) {
            workpieceAdapter = new WorkpieceListAdapter(getContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

        Fragment frag = BookingFragment.newInstance();
        Bundle bundle = new Bundle();

        bundle.putSerializable(ACTION, model.getAction());
        bundle.putBoolean(BOOK, true);
        bundle.putString(PK, model.getPk());

        frag.setArguments(bundle);

        Utils.replaceFragment(frag,true, getActivity().getSupportFragmentManager(), R.id.master_booking_fragment);

    }

    private void onBack(){
        Objects.requireNonNull(getActivity()).onBackPressed();
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

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}