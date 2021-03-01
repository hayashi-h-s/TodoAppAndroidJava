package com.sns.todo_app_android_fireabse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sns.todo_app_android_fireabse.R;
import com.sns.todo_app_android_fireabse.adapter.TodoAdapter;
import com.sns.todo_app_android_fireabse.models.Todo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRvTodo;
    private List<Todo> mTodoList = new ArrayList<>();
    private TodoAdapter mAdapter;
    private FirebaseFirestore db;

    // View関係のメンバ変数
    private FrameLayout mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        // Viewの取得
        mRvTodo = findViewById(R.id.rvTodo);
        mProgressBar = findViewById(R.id.progressBar);

        mAdapter = new TodoAdapter(
                MainActivity.this,
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
                    public void onDeleteClicked(final Todo todo) {
                        Log.d("TAG" ,"public void onDeleteClicked(Todo todo) {");

                        db.collection("todos").
                                document(todo.getId())
                                .delete()
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(MainActivity.this, todo.getName() + "を削除しました", Toast.LENGTH_SHORT).show();
                                        mTodoList.remove(todo);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });
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
        mProgressBar.setVisibility(View.VISIBLE);
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
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
    }
}