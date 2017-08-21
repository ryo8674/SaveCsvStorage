package com.example.peter.savecsvstorage;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Fragmentを配置するActivity
 *
 * @author :ryo.yamada
 * @since :1.0 :2017/08/22
 */
public class SubActivity extends AppCompatActivity {
    private static final String BOOL_KEY = "bool";

    /**
     * onCreateメソッド
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Bundle bundle = new Bundle();
        bundle.putBoolean(BOOL_KEY, getIntent().getBooleanExtra(BOOL_KEY, false));

        // ListView表示用のフラグメントをセット
        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, mainFragment);
        transaction.commit();
    }
}
