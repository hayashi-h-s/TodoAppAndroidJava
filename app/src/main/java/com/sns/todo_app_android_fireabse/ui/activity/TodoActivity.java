package com.sns.todo_app_android_fireabse.ui.activity;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sns.todo_app_android_fireabse.LogUtil;
import com.sns.todo_app_android_fireabse.R;
import com.sns.todo_app_android_fireabse.models.Todo;
import com.sns.todo_app_android_fireabse.ui.adapter.TodoListAdapter;
import com.sns.todo_app_android_fireabse.ui.dialog.AddTodoDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoActivity extends AppCompatActivity {

    private FirebaseFirestore mFireStore;
    private FirebaseAuth mAuth;

    private List<Todo> mTodoList = new ArrayList<>();
    private TodoListAdapter mAdapter;
    // View関係のメンバ変数
    private FrameLayout mProgressBar;
    private TodoListAdapter.OnTodoListRefreshListener mOnTodoListRefreshListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        mAuth = FirebaseAuth.getInstance();

        mFireStore = FirebaseFirestore.getInstance();
        mOnTodoListRefreshListener = setTodoListRefreshListener();

        // Viewの取得
        RecyclerView rvTodo = findViewById(R.id.rvTodo);
        mProgressBar = findViewById(R.id.progressBar);

        mAdapter = new TodoListAdapter(
                TodoActivity.this,
                setTodoListRefreshListener(),
                mTodoList,
                mFireStore,
                mAuth
        );

        rvTodo.setLayoutManager(new LinearLayoutManager(this));
        rvTodo.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            currentUser.getUid();
//            LogUtil.d(" Log " + " =currentUser.getUid() = " + currentUser.getUid());
//        }

        LogUtil.d(" Log " + " = ");

        DocumentReference userDocRef = mFireStore.collection("User").document(currentUser.getUid());
        LogUtil.d(" Log " + " = userDocRef =" +userDocRef);

        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    LogUtil.d(" Log " + " = document =" +document);

//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        if (currentUser == null) {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                LogUtil.d(" Log " + " = ログイン成功");

                                Map<String, Object> user = new HashMap<>();
                                user.put("name", currentUser.getUid());

                                mFireStore.collection("User")
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(TodoActivity.this, "追加しました", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(TodoActivity.this, "追加に失敗しました", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            } else {
                                Log.w("TAG", "signInAnonymously:failure", task.getException());
                                Toast.makeText(TodoActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTodoList();
    }

    /**
     * TodoListを追加
     *
     * @param view
     */
    public void onClickAddTodoButton(View view) {
        new AddTodoDialog(
                TodoActivity.this,
                null,
                mOnTodoListRefreshListener,
                TodoActivity.this.getString(R.string.add_todo_list_dialog_title),
                TodoActivity.this.getString(R.string.common_add_positive_button),
                TodoActivity.this.getString(R.string.common_string_chancel),
                mFireStore,
                mAuth
        );
    }

    /**
     * TodoListを更新するリスナーをセット
     *
     * @return
     */
    private TodoListAdapter.OnTodoListRefreshListener setTodoListRefreshListener() {
        return new TodoListAdapter.OnTodoListRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTodoList();
            }
        };
    }

    /**
     * TodoListを更新
     */
    public void refreshTodoList() {
        mProgressBar.setVisibility(View.VISIBLE);
        CollectionReference TodoLists = mFireStore.collection("TodoLists");
        TodoLists.orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(TodoActivity.this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        mTodoList.clear();
                        for (DocumentSnapshot doc : task.getResult()) {
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