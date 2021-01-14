package com.yang.app1028;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yang.app1028.adapters.RecyclerAdapter;
import com.yang.app1028.retrofit.gerrit.Controller;
import com.yang.app1028.viewModels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private MainActivityViewModel mMainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Controller controller = new Controller();
        controller.start();
//        setContentView(R.layout.activity_main);
//        mFab = findViewById(R.id.fab);
//        mRecyclerView = findViewById(R.id.recycler_view);
//        mProgressBar = findViewById(R.id.progress_bar);
//
//        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
//        mMainActivityViewModel.init();
//        mMainActivityViewModel.getNicePlaces().observe(this, new Observer<List<NicePlace>>() {
//            @Override
//            public void onChanged(List<NicePlace> nicePlaces) {
//                mAdapter.notifyDataSetChanged();
//            }
//        });
//
//        mMainActivityViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                if (aBoolean) {
//                    showProgressBar();
//                } else {
//                    hideProgressBar();
//                    mRecyclerView.smoothScrollToPosition(mMainActivityViewModel.getNicePlaces().getValue().size() - 1);
//                }
//            }
//        });
//
//        mFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mMainActivityViewModel.addValue(new NicePlace("aaaaa", null));
//            }
//        });
//
//        initRecycleView();
    }

    private void initRecycleView() {
        mAdapter = new RecyclerAdapter(this, mMainActivityViewModel.getNicePlaces().getValue());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}