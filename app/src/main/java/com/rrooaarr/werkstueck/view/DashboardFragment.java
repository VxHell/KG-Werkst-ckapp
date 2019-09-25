package com.rrooaarr.werkstueck.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.rrooaarr.werkstueck.R;
import com.rrooaarr.werkstueck.WordViewModel2;

public class DashboardFragment extends Fragment implements FragmentBase,View.OnClickListener {
    public static final String TAG = DashboardFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button book_finishing;
    private String mParam1;
    private String mParam2;
    private View userInfoView = null;
    private WordViewModel2 mainViewModel;

    public DashboardFragment() {
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
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
//        userListViewModel = ViewModelProviders.of(getActivity()).get(UserListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userInfoView = inflater.inflate(R.layout.frag_dashboard, container, false);

//        DashboardFragmentBinding binding = DataBindingUtil.inflate(
//                inflater, R.layout.frag_dashboard, container, false);

        initViews(userInfoView);
        return userInfoView;
    }

    private void initViews(View view) {
//        userName = (EditText) view.findViewById(R.id.user_name);
//        userOccupation = (EditText) view.findViewById(R.id.user_occupation);
        book_finishing = view.findViewById(R.id.button_book_finishing);
        book_finishing.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_book_finishing) {
            bookFinishing();
        }
    }

    private void bookFinishing() {
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}