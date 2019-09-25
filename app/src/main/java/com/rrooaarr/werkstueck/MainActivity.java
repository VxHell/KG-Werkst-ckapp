package com.rrooaarr.werkstueck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.rrooaarr.werkstueck.databinding.ActivityMainBinding;
import com.rrooaarr.werkstueck.util.Utils;
import com.rrooaarr.werkstueck.view.BookingFragment;
import com.rrooaarr.werkstueck.view.DashboardFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MainActivity";

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    private WordViewModel2 mainViewModel;
    Button btn_book_finishing, btn_settings;
    private long lastBackPressTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(WordViewModel2.class);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

//        Utils.replaceFragment(DashboardFragment.newInstance("param1", "param2"), true, getSupportFragmentManager(), R.id.content_frame);

//        RecyclerView recyclerView = findViewById(R.id.recyclerview);
//        final WordListAdapter adapter = new WordListAdapter(this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//
        /**
         * Anonym Class impl
         */
//        mainViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
//            @Override
//            public void onChanged(@Nullable final List<Word> words) {
//                // Update the cached copy of the words in the adapter.
//                adapter.setWords(words);
//            }
//        });

        /**
         * Lambda for onChanged impl
         */
//        mainViewModel.getNewsRepository().observe(this, word -> {
//            String fetchedWord = word.getWord();
//        });

        binding.setMainViewModel(mainViewModel);

    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        final Fragment currentFragmentIsDashboard = Utils.getVisibleFragment(this);
        if (currentFragmentIsDashboard != null && DashboardFragment.TAG.equals(currentFragmentIsDashboard.getTag())) {
            finish();
            return;
        }
        try {
            if (manager.getBackStackEntryCount() > 1) {
                manager.popBackStackImmediate();
                for (int i = 0; i < manager.getBackStackEntryCount(); i++) {
                    final Fragment visibleFragment = Utils.getVisibleFragment(this);
                    if (visibleFragment != null && BookingFragment.TAG.equals(visibleFragment.getTag())) {
                        manager.popBackStackImmediate();
                    } else {
                        break;
                    }
                }
            } else if (manager.getBackStackEntryCount() == 1) {
                manager.popBackStackImmediate();
            } else {
                Toast toast;
                if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
                    toast = Toast.makeText(this, "Press back again to close this app", Toast.LENGTH_SHORT);
                    toast.show();
                    this.lastBackPressTime = System.currentTimeMillis();
                } else {
                    super.onBackPressed();
                }
            }
        } catch (IllegalStateException ise) {
            Utils.doErrorHandling(R.string.errBackPressNavi, getApplicationContext());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
            mainViewModel.insert(word);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void initViews(View view) {
//        userName = (EditText) view.findViewById(R.id.user_name);
//        userOccupation = (EditText) view.findViewById(R.id.user_occupation);
        btn_book_finishing = view.findViewById(R.id.button_book_finishing);
        btn_book_finishing.setOnClickListener(this);
        btn_settings = view.findViewById(R.id.button_settings);
        btn_settings.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_book_finishing) {
            Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        }
        if (view.getId() == R.id.button_settings) {
            Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        }
    }

}
