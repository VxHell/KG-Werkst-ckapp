package com.rrooaarr.werkstueck.wsinfo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rrooaarr.werkstueck.R;
import com.rrooaarr.werkstueck.booking.BookingFragment;
import com.rrooaarr.werkstueck.booking.BookingViewModel;
import com.rrooaarr.werkstueck.booking.api.errorhandling.DataErrorWrapper;
import com.rrooaarr.werkstueck.booking.api.errorhandling.ErrorHelper;
import com.rrooaarr.werkstueck.booking.model.WorkpieceContainer;
import com.rrooaarr.werkstueck.databinding.FragmentWsinfoBinding;
import com.rrooaarr.werkstueck.setting.UserSetting;
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

public class WerkstückinfoFragment extends Fragment implements FragmentBase, View.OnClickListener {

    public static String TAG = WerkstückinfoFragment.class.getSimpleName();
    WorkpieceListAdapter workpieceAdapter;
    RecyclerView recyclerView;
    Button actionButton;
    private BookingViewModel model;
    private View myRoot;
    private UserSetting setting;

    public WerkstückinfoFragment() {
    }

    public static WerkstückinfoFragment newInstance() {

        return new WerkstückinfoFragment();
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(getActivity()).get(BookingViewModel.class);

        model.getSetting().observe(this, new Observer<UserSetting>() {
            @Override
            public void onChanged(UserSetting settingLoaded) {
                setting = settingLoaded;
                model.initApi(setting);

                if(setting.getServer().isEmpty() && setting.getPort().isEmpty() && setting.getPassword().isEmpty() && setting.getUsername().isEmpty() )
                    ErrorHelper.onShowErrorDialog(getActivity().getSupportFragmentManager(), getResources().getString(R.string.errTitle), getResources().getString(R.string.errNo_settings));

                final Bundle arguments = getArguments();
                if (arguments != null) {
                    String wst = arguments.getString(WST);
                    model.fetchWorkpieceInfo(wst);
                } else {
                    ErrorHelper.onShowErrorDialog(getActivity().getSupportFragmentManager(), getResources().getString(R.string.errTitle), getResources().getString(R.string.errNo_settings));
                }

                model.getWorkpieceInfoData().observe(getViewLifecycleOwner(), new Observer<DataErrorWrapper<WorkpieceContainer>>() {
                    @Override
                    public void onChanged(DataErrorWrapper<WorkpieceContainer> dataWrapper) {
                        if (dataWrapper != null) {
                            final WorkpieceContainer workpiece = dataWrapper.getData();
                            if (dataWrapper.getStatus() == DataErrorWrapper.APIStatus.SUCCESS) {
                                List<WorkpieceListElement> wst_liste = new ArrayList<>(workpiece.getWst_infos().size());

                                for (Map.Entry<String, String> stringStringEntry : workpiece.getWst_infos().entrySet()) {
                                    wst_liste.add(new WorkpieceListElement(stringStringEntry.getKey(), stringStringEntry.getValue()));
                                }

                                workpieceAdapter.setWorkpieces(wst_liste);
                                model.setPK(workpiece.getPk());
                                if (actionButton != null) {
                                    actionButton.setEnabled(true);
                                    actionButton.setClickable(true);
                                }
                            } else if (dataWrapper.getStatus() == DataErrorWrapper.APIStatus.WSTNOTFOUND) {
                                model.setPK(null);
                                if (actionButton != null) {
                                    actionButton.setEnabled(false);
                                    actionButton.setClickable(false);
                                }
                                ErrorHelper.doDefaultApiErrorHandling(getActivity(),dataWrapper);
                            } else {
                                model.setPK(null);
                                ErrorHelper.doDefaultApiErrorHandling(getActivity(),dataWrapper);
                            }
                        } else {
                            ErrorHelper.onShowErrorDialog(getActivity().getSupportFragmentManager(),getResources().getString(R.string.errTitle),getResources().getString( R.string.errNo_service));
                        }
                    }
                });
            }
        });

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

        actionButton = view.findViewById(R.id.button_book_action);
        actionButton.setOnClickListener(this);

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

    private void onAction() {

        Fragment frag = BookingFragment.newInstance();

        Bundle bundle = new Bundle();
        bundle.putSerializable(ACTION, model.getAction());
        bundle.putBoolean(BOOK, true);
        bundle.putString(PK, model.getPk());

        frag.setArguments(bundle);

        final FragmentManager manager = getActivity().getSupportFragmentManager();

        popDuplicatedFragmentAndUnderlyingActivityToNormalizeBackstackOrder(manager);

        Utils.replaceFragmentBooking(frag, true, manager, R.id.master_booking_fragment);
    }

    private void onBack() {
        Objects.requireNonNull(getActivity()).onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_book_action:
                onAction();
                break;
            case R.id.button_back:
                onBack();
                break;
        }
    }

    private void popDuplicatedFragmentAndUnderlyingActivityToNormalizeBackstackOrder(FragmentManager manager){

        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack();
        }
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack();
        }
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}