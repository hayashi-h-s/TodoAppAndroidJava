package com.sns.todo_app_android_fireabse.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sns.todo_app_android_fireabse.R;
import com.sns.todo_app_android_fireabse.util.NetworkUtil;

import java.util.HashMap;
import java.util.Map;

public class AddTodoActivity extends AppCompatActivity {

    Button mFirebaseTextButton;

    private EditText etTodo;
    private FirebaseFirestore db;

    private String id;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        db = FirebaseFirestore.getInstance();
        etTodo = findViewById(R.id.etTodo);
        Button btnAddTodo = findViewById(R.id.AddTodoBtn);

        // アップデートの場合は値が渡される
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");

        Log.d("Log",
                " id =" + id +
                        " name =" + name
        );

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
        if (!NetworkUtil.isOnline(AddTodoActivity.this)) {
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

        String todoName = etTodo.getText().toString().trim();
        if (todoName.isEmpty()) {
            Toast.makeText(AddTodoActivity.this, "内容を入力してください", Toast.LENGTH_SHORT).show();
            return;
        }

        // モデルからリストに追加
        Map<String, Object> todo = new HashMap<>();
        todo.put("name", todoName);
        todo.put("timestamp", FieldValue.serverTimestamp());

        if (id == null) {
            db.collection("TodoLists")
                    .add(todo)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(AddTodoActivity.this, "投稿しました", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddTodoActivity.this, "投稿に失敗しました", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        } else {
            db.collection("TodoLists")
                    .add(todo)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(AddTodoActivity.this, "更新しました", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddTodoActivity.this, "更新に失敗しました", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        }
    }



    public void firebaseTextButton(View view) {

        String todoTitle = etTodo.getText().toString().trim();

        // コレクション参考サイト
        Map<String, Object> todo = new HashMap<>();
        todo.put("name", todoTitle);
        todo.put("timestamp", FieldValue.serverTimestamp());

        db.collection("TodoLists").add(todo);

    }

    public void onClickToNoteActivity(View view) {

        String todoTitle = etTodo.getText().toString().trim();
        CollectionReference notebookRef = db.collection("Notebook");

        Map<String, Object> todoItem = new HashMap<>();
        todoItem.put("name", todoTitle);
        todoItem.put("timestamp", FieldValue.serverTimestamp());
        db.collection("TodoLists").document("yW4mfyuVmjHxBfl8Ocnx").collection("TodoItems").add(todoItem);

    }
}