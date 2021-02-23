package com.sns.todo_app_android_fireabse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sns.todo_app_android_fireabse.R;
import com.sns.todo_app_android_fireabse.adapter.TodoAdapter;
import com.sns.todo_app_android_fireabse.models.Todo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mAddTodoButton;
    private RecyclerView mRvTodo;
    private List<Todo> mTodoList = new ArrayList<>();
    private TodoAdapter mAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        mAddTodoButton = findViewById(R.id.addTodoButton);
        mRvTodo = findViewById(R.id.rvTodo);

//        Todo todo = Todo.toObject("名前", "15");
//        Log.d("TAG" ," todo = "+todo);
//        mTodoList.add(todo);
//        Log.d("TAG" ,"mTodoList =" +mTodoList);

//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Haya");
//        user.put("last", "だよ");
//        user.put("born", 1993);
//
//        // Add a new document with a generated ID
//        db.collection("users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("TAG", "Error adding document", e);
//                    }
//                });

        mAdapter = new TodoAdapter(
                mTodoList,
                // インターフェース
                new TodoAdapter.OnButtonClickListener() {
                    @Override
                    public void onUpdateClicked(Todo todo) {
                        Log.d("TAG" ,"public void onUpdateClicked(Todo todo) {");
                        // アップデートするためにはIDを渡す
                        Intent i = new Intent(MainActivity.this, AddTodoActivity.class);
                        i.putExtra("id",todo.getId());
                        i.putExtra("name",todo.getName());
                        startActivity(i);
                    }
                    @Override
                    public void onDeleteClicked(Todo todo) {
                        Log.d("TAG" ,"public void onDeleteClicked(Todo todo) {");
                    }
                });

        mRvTodo.setLayoutManager(new LinearLayoutManager(this));
        mRvTodo.setAdapter(mAdapter);
    }

    public void onClickAddTodoButton(View view) {
        Log.d("TAG", "public void onClickAddTodoButton(View view) {");
         startActivity(new Intent(MainActivity.this,AddTodoActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // リストを取得する
        db.collection("todos").get()
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        mTodoList.clear();
                        for (DocumentSnapshot doc: task.getResult()){
                            Todo todo = doc.toObject(Todo.class);
                            todo.setId(doc.getId());
                            mTodoList.add(todo);
                        }
                        // アダプターの更新
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }
}