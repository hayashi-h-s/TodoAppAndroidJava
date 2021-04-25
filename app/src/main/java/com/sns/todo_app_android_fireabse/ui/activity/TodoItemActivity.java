package com.sns.todo_app_android_fireabse.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sns.todo_app_android_fireabse.R;
import com.sns.todo_app_android_fireabse.models.TodoItem;
import com.sns.todo_app_android_fireabse.ui.adapter.TodoItemAdapter;
import com.sns.todo_app_android_fireabse.ui.dialog.AddTodoItemDialog;

import java.util.ArrayList;
import java.util.List;

public class TodoItemActivity extends AppCompatActivity {

    public static final String INTENT_TODO_ID = "INTENT_TODO_ID";
    public static final String INTENT_TODO_NAME = "INTENT_TODO_NAME";

    private FrameLayout mProgressBar;
    private TodoItemAdapter mAdapter;
    private androidx.appcompat.widget.Toolbar mToolbar;

    private List<TodoItem> mTodoItemList = new ArrayList<>();
    private String todoId;

    private FirebaseFirestore db;
    private TodoItemAdapter.OnTodoItemRefreshListener mOnTodoItemRefreshListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_item);

        db = FirebaseFirestore.getInstance();
        mOnTodoItemRefreshListener = setTodoItemRefreshListener();

        todoId = getIntent().getStringExtra(INTENT_TODO_ID);
        // Viewの取得
        RecyclerView rvTodoItem = findViewById(R.id.rvTodoItem);
        mProgressBar = findViewById(R.id.progressBar);
        mToolbar = findViewById(R.id.toolbar);

        // アクションバーにツールバーをセット
        setSupportActionBar(mToolbar);
        // ツールバーの設定
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mAdapter = new TodoItemAdapter(
                TodoItemActivity.this,
                setTodoItemRefreshListener(),
                mTodoItemList,
                db,
                todoId
        );
        // タイトルは後でつける
        String name = getIntent().getStringExtra(INTENT_TODO_NAME);
        TextView mToolbarTitle = findViewById(R.id.toolbar_title);
        mToolbarTitle.setText(name);

        rvTodoItem.setLayoutManager(new LinearLayoutManager(this));
        rvTodoItem.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTodoItem();
    }

    /**
     * TodoItemを追加するダイアログを表示
     */
    public void onClickAddTodoItemButton(View view) {
        new AddTodoItemDialog(
                TodoItemActivity.this,
                mOnTodoItemRefreshListener,
                TodoItemActivity.this.getString(R.string.add_todo_item_dialog_title),
                TodoItemActivity.this.getString(R.string.common_add_positive_button),
                TodoItemActivity.this.getString(R.string.common_string_chancel),
                db,
                todoId,
                null
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // 今回はActivityを終了させている
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * TodoItemを更新するリスナーをセット
     * @return
     */
    private TodoItemAdapter.OnTodoItemRefreshListener setTodoItemRefreshListener() {
            return new TodoItemAdapter.OnTodoItemRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshTodoItem();
                }
            };
    }

    /**
     * TodoItemを更新するメソッド
     */
    public void refreshTodoItem() {
        mProgressBar.setVisibility(View.VISIBLE);
        CollectionReference TodoLists = db.collection("TodoLists");
        TodoLists.document(todoId)
                .collection("TodoItems")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(TodoItemActivity.this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        mTodoItemList.clear();
                        for (DocumentSnapshot doc : task.getResult()) {
                            TodoItem todoItem = doc.toObject(TodoItem.class);
                            todoItem.setId(doc.getId());
                            mTodoItemList.add(todoItem);
                        }
                        // アダプターの更新
                        mAdapter.notifyDataSetChanged();
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
    }
}