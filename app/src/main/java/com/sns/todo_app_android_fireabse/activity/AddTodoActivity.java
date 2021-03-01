package com.sns.todo_app_android_fireabse.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sns.todo_app_android_fireabse.R;

import java.util.Collections;

public class AddTodoActivity extends AppCompatActivity {

//    public static final String WIFI = "Wi-Fi";
//    public static final String ANY = "Any";
//    private static final String URL = "http://stackoverflow.com/feeds/tag?tagnames=android&sort;=newest";
//    public static String sPref = null;

    private EditText etTodo;
    private Button btnAddTodo;
    private FirebaseFirestore db;

    private String id;
    private String name;

    RenderScript rs; // RenderScript
    ImageView imageView;


//    private ConnectivityManager connectivityManager = (ConnectivityManager) AddTodoActivity.this.getSystemService(CONNECTIVITY_SERVICE);
//    private NetworkReceiver receiver = new NetworkReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        db = FirebaseFirestore.getInstance();
        etTodo = findViewById(R.id.etTodo);
        btnAddTodo = findViewById(R.id.AddTodoBtn);
        imageView = findViewById(R.id.imageViewAddView);

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");

        Log.d("TAG", " id = " + id + " name = " + name);

        // 名前があるかどうか(アップデートの場合)
        if (name != null) {
            etTodo.setText(name);
            // 名前がある場合はUpdate Todoをいれる
            btnAddTodo.setText("更新");
        }

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddTodoActivity.this, MainActivity.class));
            }
        });
    }

    public void onClickAddTodoButton(View view) {
        // このメソッドはバージョンによっては使えないので、後で変更する
        if (!isOnline()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddTodoActivity.this);
            builder.setTitle(R.string.add_todo_activity_network_error_dialog_title)
                    .setMessage(R.string.add_todo_activity_network_error_dialog_body)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    }).show();
            return;
        }


        Log.d("TAG", " public void onClickAddTodoButton(View view) { ");

        String todoName = etTodo.getText().toString().trim();
        if (todoName.isEmpty()) {
            Toast.makeText(AddTodoActivity.this, "内容を入力してください", Toast.LENGTH_SHORT).show();
            return;
        }

        if (id == null) {

            db.collection("todos")
                    .document()
                    .set(Collections.singletonMap("name", todoName))
                    .addOnCompleteListener(AddTodoActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AddTodoActivity.this, "TODOを追加しました", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        } else {
            db.collection("todos")
                    // IDを指定することで、更新処理ができる
                    .document(id)
                    .set(Collections.singletonMap("name", todoName))
                    .addOnCompleteListener(AddTodoActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AddTodoActivity.this, "更新しました", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        }
    }

    /**
     * 通信できるか否かのを返す
     */
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}