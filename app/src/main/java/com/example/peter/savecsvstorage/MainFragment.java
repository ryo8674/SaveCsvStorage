package com.example.peter.savecsvstorage;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * リストを表示するフラグメント
 *
 * @author :ryo.yamada
 * @since :1.0 :2017/08/22
 */
public class MainFragment extends Fragment {

    private static final String INTERNAL_CSV_FILENAME = "internal.csv";
    private static final String EXTERNAL_CSV_FILENAME = "external.csv";
    private static final String NOT_FOUND_FILE = "ファイルが見つかりませんでした。";
    private static final String SD_UNMOUNTED = "SDカードがマウントされていません。";
    private static final String NOT_EXIST_SD = "SDカードがありません。";
    private static final String BOOL_KEY = "bool";
    private static final String SPLIT_WORD = ",";

    private String message;

    /**
     * コンストラクタ
     */
    public MainFragment() {
    }

    /**
     * onCreateViewメソッド
     *
     * @param inflater           inflater
     * @param container          container
     * @param savedInstanceState savedInstanceState
     * @return Fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    /**
     * onViewCreated
     *
     * @param view               view
     * @param savedInstanceState savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        boolean flag = bundle.getBoolean(BOOL_KEY);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, loadFile(flag));
        ListView listView = view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }

    /**
     * csvファイルをロードするメソッド
     *
     * @param flag 内部ストレージか外部ストレージかを判定するフラグ
     * @return csvファイルを読み込んだリスト
     */
    private List<String> loadFile(boolean flag) {
        ArrayList<String> list = new ArrayList<>();
        BufferedReader in = null;
        String[] csvArray;
        try {
            if (flag) {
                if (IsExternalStorageAvailableAndWritable()) {
                    String path = getContext().getExternalFilesDir(null) + File.separator + EXTERNAL_CSV_FILENAME;
                    in = new BufferedReader(new FileReader(path));
                } else {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            } else {
                InputStream inputStream = getContext().openFileInput(INTERNAL_CSV_FILENAME);
                in = new BufferedReader(new InputStreamReader(inputStream));
            }

            String line;
            while ((line = in.readLine()) != null) {

                csvArray = line.split(SPLIT_WORD);
                Collections.addAll(list, csvArray);
            }
            in.close();
        } catch (IOException ignored) {
            Toast.makeText(getContext(), NOT_FOUND_FILE, Toast.LENGTH_SHORT).show();
        }
        return list;
    }

    /**
     * 外部ストレージが存在するか、書き込み可能かをチェックする
     *
     * @return bool
     */
    private boolean IsExternalStorageAvailableAndWritable() {
        boolean externalStorageAvailable;

        //アクセス可能か、状態を取得
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_UNMOUNTED.equals(state)) {
            //マウントされていない
            externalStorageAvailable = false;
            message = SD_UNMOUNTED;
        } else if (Environment.MEDIA_REMOVED.equals(state)) {
            //存在しない
            externalStorageAvailable = false;
            message = NOT_EXIST_SD;
        } else {
            //読み込み可
            externalStorageAvailable = true;
        }
        return externalStorageAvailable;
    }


}


