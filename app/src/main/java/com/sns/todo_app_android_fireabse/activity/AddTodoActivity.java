package com.sns.todo_app_android_fireabse.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sns.todo_app_android_fireabse.LogUtil;
import com.sns.todo_app_android_fireabse.R;

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


        LogUtil.d(" Log " + " = public void onClickAddTodoButton(View view) {");

//        db.collection("rooms").document("roomA")
//                .collection("messages").document("message1");

//        Map<String, Object> city = new HashMap<>();
//        city.put("name", "Los Angeles");
//        city.put("state", "CA");
//        city.put("country", "USA");

//        db.collection("cities").document("LA")
//                .set(city)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("TAG", "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("TAG", "Error writing document", e);
//                    }
//                });


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

//        Map<String, Object> user = new HashMap<>();
//
//        user.put("first", todoName);
//        user.put("middle", todoName);
//        user.put("last", "Turing");
//        user.put("born", 1912);
//        user.put("id", id);

// Add a new document with a generated ID
//        db.collection("users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });

//        new Date().getTime();
//
//        LogUtil.d(" Log " + " = new Date().getTime()");
//
//        if (id == null) {
//            db.collection("todoLists")
//                    .document(id)
//                    .collection("test")
//                    .document("ドキュメント")
//                    .set(Collections.singletonMap("name", todoName))
//                    .addOnCompleteListener(AddTodoActivity.this, new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Toast.makeText(AddTodoActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
//                    });
//        } else {
//            db.collection("todoList")
//                    .document(id)
//                    .set(Collections.singletonMap("name", todoName))
//                    .addOnCompleteListener(AddTodoActivity.this, new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Toast.makeText(AddTodoActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
//                    });
//        }

        // モデルからリストに追加
        Map<String, Object> todo = new HashMap<>();
        todo.put("name", todoName);
        todo.put("timestamp", FieldValue.serverTimestamp());

        // ネストしたデータ構造を
        Map<String, Object> nestedData = new HashMap<>();
        nestedData.put("a", 5);

        todo.put("objectExample", nestedData);

        db.collection("todoLists")
                .document("todoList")
                .set(todo)
                .addOnCompleteListener(AddTodoActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddTodoActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });


        // テスト用のコード
//        db.collection("todoLists")
//                .set(todo)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Toast.makeText(AddTodoActivity.this, "投稿しました", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(AddTodoActivity.this, "投稿に失敗しました", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                });

//        db.collection("rooms").document("roomA")
//                .collection("messages").document("message1");


        if (id == null) {
            db.collection("todoList")
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
            db.collection("todoList")
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

    /**
     * 通信できるか否かのを返す
     */
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void firebaseTextButton(View view) {


        Map<String, Object> data = new HashMap<>();
        data.put("capital", true);

        db.collection("cities").document("BJ")
                .set(data, SetOptions.merge());


//        LogUtil.d(" Log " + " = ");
//
//        CollectionReference cities = db.collection("cities");
//
//        Map<String, Object> data1 = new HashMap<>();
//        data1.put("name", "San Francisco");
//        data1.put("state", "CA");
//        data1.put("country", "USA");
//        data1.put("capital", false);
//        data1.put("population", 860000);
//        data1.put("regions", Arrays.asList("west_coast", "norcal"));
//        cities.document("SF").set(data1);
//
//        Map<String, Object> data2 = new HashMap<>();
//        data2.put("name", "Los Angeles");
//        data2.put("state", "CA");
//        data2.put("country", "USA");
//        data2.put("capital", false);
//        data2.put("population", 3900000);
//        data2.put("regions", Arrays.asList("west_coast", "socal"));
//        cities.document("LA").set(data2);
//
//        Map<String, Object> data3 = new HashMap<>();
//        data3.put("name", "Washington D.C.");
//        data3.put("state", null);
//        data3.put("country", "USA");
//        data3.put("capital", true);
//        data3.put("population", 680000);
//        data3.put("regions", Arrays.asList("east_coast"));
//        cities.document("DC").set(data3);
//
//        Map<String, Object> data4 = new HashMap<>();
//        data4.put("name", "Tokyo");
//        data4.put("state", null);
//        data4.put("country", "Japan");
//        data4.put("capital", true);
//        data4.put("population", 9000000);
//        data4.put("regions", Arrays.asList("kanto", "honshu"));
//        cities.document("TOK").set(data4);
//
//        Map<String, Object> data5 = new HashMap<>();
//        data5.put("name", "Beijing");
//        data5.put("state", null);
//        data5.put("country", "China");
//        data5.put("capital", true);
//        data5.put("population", 21500000);
//        data5.put("regions", Arrays.asList("jingjinji", "hebei"));
//        cities.document("BJ").set(data5);
//
//        DocumentReference docRef = db.collection("cities").document("SF");
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//
//                        LogUtil.d(" Log " + " =document.getData()) = " +document.getData());
//                    } else {
//                        Log.d("TAG", "No such document");
//                    }
//                } else {
//                    Log.d("TAG!", "get failed with ", task.getException());
//
//                    LogUtil.d(" Log " + " =document.getData()) = " + task.getException());
//                }
//            }
//        });
    }
}