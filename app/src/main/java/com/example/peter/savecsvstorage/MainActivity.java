package com.example.peter.savecsvstorage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * メイン画面
 * 内部ストレージか外部ストレージかを選択し、csvファイルを書き込みか読み込むかを選択する。
 *
 * @author :ryo.yamada
 * @since :1.0 :2017/08/22
 */
public class MainActivity extends AppCompatActivity {

    private static final String INTERNAL_CSV_FILENAME = "internal.csv";
    private static final String EXTERNAL_CSV_FILENAME = "external.csv";
    private static final String INTERNAL_CONTENT = "internal";
    private static final String EXTERNAL_CONTENT = "external";
    private static final String INTERNAL_SUCCESS_MESSAGE = "内部ファイル作成に成功しました。";
    private static final String EXTERNAL_SUCCESS_MESSAGE = "外部ファイル作成に成功しました。";
    private static final String NOT_FOUND_FILE = "ファイルが見つかりませんでした。";
    private static final String FAILED_WRITE = "書き込みに失敗しました。";
    private static final String BOOL_KEY = "bool";
    private static final String SPLIT_WORD = ",";
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 20;

    private FileOutputStream outputStream;

    /**
     * onCreateメソッド
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ToggleButton toggleButton = (ToggleButton) findViewById(R.id.storage_toggle_button);
        Button createDataButton = (Button) findViewById(R.id.create_data_button);
        Button createListButton = (Button) findViewById(R.id.create_list_button);

        // create dataボタン押下時の処理
        createDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggleButton.isChecked()) {
                    createExternalCsvFile();
                } else
                    createInternalCsvFile();
            }
        });

        // create list押下時の処理
        createListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                intent.putExtra(BOOL_KEY, toggleButton.isChecked());
                startActivity(intent);
            }
        });

    }

    /**
     * 内部ストレージにcsvファイルを保存するメソッド
     */
    private void createInternalCsvFile() {
        try {
            outputStream = openFileOutput(INTERNAL_CSV_FILENAME, MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            Toast.makeText(this, NOT_FOUND_FILE, LENGTH_SHORT).show();
        }

        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        try {
            for (int i = MIN_VALUE; i <= MAX_VALUE; i++) {
                writer.write(INTERNAL_CONTENT + i + SPLIT_WORD); //テキストの書き込み（バッファにためている）
            }
            writer.flush(); //バッファリングされている文字列をファイルに書き込む
            writer.close(); //ファイルを閉じる
            Toast.makeText(this, INTERNAL_SUCCESS_MESSAGE, LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, FAILED_WRITE, LENGTH_SHORT).show();
        }
    }

    /**
     * 外部ストレージにcsvファイルを保存するメソッド
     */
    private void createExternalCsvFile() {
        if (IsExternalStorageAvailableAndWritable()) {
            File file = new File(getExternalFilesDir(null), EXTERNAL_CSV_FILENAME);

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);

                for (int i = MIN_VALUE; i <= MAX_VALUE; i++) {
                    writer.write(EXTERNAL_CONTENT + i + SPLIT_WORD); //テキストの書き込み（バッファにためている）
                }
                writer.flush();
                writer.close();
                Toast.makeText(getApplicationContext(), EXTERNAL_SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), FAILED_WRITE, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), FAILED_WRITE, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 外部ストレージが存在するか、書き込み可能かをチェックする
     *
     * @return bool
     */
    private boolean IsExternalStorageAvailableAndWritable() {
        boolean externalStorageAvailable;
        boolean externalStorageWritable;

        //アクセス可能か、状態を取得
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            //ストレージの読み書きが可能
            externalStorageAvailable = true;
            externalStorageWritable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            //ストレージの読み込みが可能
            externalStorageAvailable = true;
            externalStorageWritable = false;
        } else {
            //読み込みも書き込みもできない
            externalStorageAvailable = externalStorageWritable = false;
        }
        return externalStorageAvailable && externalStorageWritable;
    }
}
