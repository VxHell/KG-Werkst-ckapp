package com.rrooaarr.werkstueck.view;

//public class UserInfoFragment extends Fragment implements FragmentBase {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    EditText userName, userOccupation;
//    Button saveUser;
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//    private View userInfoView = null;
//    private UserListViewModel userListViewModel;
//
//    public UserInfoFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment UserInfoFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static UserInfoFragment newInstance(String param1, String param2) {
//        UserInfoFragment fragment = new UserInfoFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//        userListViewModel = ViewModelProviders.of(getActivity()).get(UserListViewModel.class);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        userInfoView = inflater.inflate(R.layout.fragment_user_info, container, false);
//        initViews(userInfoView);
//        return userInfoView;
//    }
//
//    private void initViews(View view) {
//        userName = (EditText) view.findViewById(R.id.user_name);
//        userOccupation = (EditText) view.findViewById(R.id.user_occupation);
//        saveUser = (Button) view.findViewById(R.id.save_info);
//        saveUser.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View view) {
//        if (view.getId() == R.id.save_info) {
//            saveInfo();
//        }
//    }
//
//    private void saveInfo() {
//        if (!TextUtils.isEmpty(userName.getText().toString()) && !TextUtils.isEmpty(userOccupation.getText().toString())) {
//            String name = userName.getText().toString();
//            String userOccup = userOccupation.getText().toString();
//            User user = new User(name, userOccup);
//            userListViewModel.insert(user);
//            userName.setText("");
//            userOccupation.setText("");
//        } else {
//            Toast.makeText(getActivity(), "Plz Fill Required info...", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @Override
//    public String getFragmentTag() {
//        return UserInfoFragment.class.getSimpleName();
//    }
//}