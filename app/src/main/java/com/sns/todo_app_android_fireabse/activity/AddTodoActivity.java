package com.sns.todo_app_android_fireabse.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sns.todo_app_android_fireabse.R;
import com.sns.todo_app_android_fireabse.api.AsyncTask.DownloadTask;

public class AddTodoActivity extends AppCompatActivity {

    private EditText etTodo;
    private Button btnAddTodo;
    private FirebaseFirestore db;

    private String id;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        db = FirebaseFirestore.getInstance();
        etTodo = findViewById(R.id.etTodo);
        btnAddTodo = findViewById(R.id.AddTodoBtn);

        final String id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");

        Log.d("TAG", " id = " + id + " name = " + name);

        // 名前があるかどうか(アップデートの場合)
        if (name != null) {
            etTodo.setText(name);
            // 名前がある場合はUpdate Todoをいれる
            btnAddTodo.setText("Update Todo");
        }

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddTodoActivity.this, MainActivity.class));
            }
        });
    }

    public void onClickAddTodoButton(View view) {

        Log.d("TAG" ,"public void onClickAddTodoButton(View view) {");

//        String param0 = etTodo.getText().toString();
        String param0 = "https://pbs.twimg.com/media/CQsH6fbUEAEzPp2.jpg";

        Log.d("TAG" ,"param0 =" +param0);

        if (param0.length() != 0) {
            // ボタンをタップして非同期処理を開始
            DownloadTask task = new DownloadTask();
            // Listenerを設定
            task.setListener(createListener());
            task.execute(param0);
        }

        return;

//        Log.d("TAG" ,"public void onClickAddTodoButton(View view) {");
//
//        String todoName = etTodo.getText().toString().trim();
//        if (todoName.isEmpty()){
//            Toast.makeText(AddTodoActivity.this, "内容を入力してください", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (id == null) {
//
//            Log.d("TAG" ,"if (id == null) {");
//
//            db.collection("todos")
//                    .document()
//                    .set(Collections.singletonMap("name",todoName))
//                    .addOnCompleteListener(AddTodoActivity.this, new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Toast.makeText(AddTodoActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
//                    });
//        }
    }

    private DownloadTask.Listener createListener() {
        return new DownloadTask.Listener() {
            @Override
            public void onSuccess(Bitmap bmp) {
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(bmp);
            }
        };
    }
}