package com.rrooaarr.werkstueck.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.rrooaarr.werkstueck.MainViewModel;

public class BookingFragment extends Fragment implements FragmentBase,View.OnClickListener {
    public static final String TAG = DashboardFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText userName, userOccupation;
    Button saveUser;
    private String mParam1;
    private String mParam2;
    private View userInfoView = null;
    private MainViewModel bookingViewModel;

    public BookingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookingFragment newInstance(String param1, String param2) {
        BookingFragment fragment = new BookingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        bookingViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        userInfoView = inflater.inflate(R.layout.frag_booking, container, false);
//        initViews(userInfoView);
//        return userInfoView;
        return null;
    }

    private void initViews(View view) {
        saveUser.setOnClickListener(this);
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public void onClick(View view) {

    }
}