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
import com.sns.todo_app_android_fireabse.LogUtil;
import com.sns.todo_app_android_fireabse.R;
import com.sns.todo_app_android_fireabse.models.TodoItem;
import com.sns.todo_app_android_fireabse.ui.adapter.TodoItemAdapter;
import com.sns.todo_app_android_fireabse.ui.dialog.AddTodoDialog;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_item);

        db = FirebaseFirestore.getInstance();

        todoId = getIntent().getStringExtra(INTENT_TODO_ID);

        LogUtil.d(" Log " + " = todoId =" +todoId);

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
                mTodoItemList,
                db,
                todoId
        );

        // タイトルは後でつける
        String name = getIntent().getStringExtra(INTENT_TODO_NAME);

        LogUtil.d(" Log " + " = name =" +name);

        TextView mToolbarTitle = findViewById(R.id.toolbar_title);
        mToolbarTitle.setText(name);

        rvTodoItem.setLayoutManager(new LinearLayoutManager(this));
        rvTodoItem.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.VISIBLE);
        CollectionReference TodoLists = db.collection("TodoLists");
        TodoLists
                .document(todoId)
                .collection("TodoItems")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(TodoItemActivity.this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        LogUtil.d(" Log " + " = task.getResult().size() =" + task.getResult().size());

                        mTodoItemList.clear();
                        for (DocumentSnapshot doc : task.getResult()) {
                            TodoItem todoItem = doc.toObject(TodoItem.class);
                            todoItem.setId(doc.getId());
                            mTodoItemList.add(todoItem);
                        }

                        LogUtil.d(" Log " + " = mTodoItemList.size() =" + mTodoItemList.size());
//
//                        LogUtil.d(" Log " + " = mTodoItemList.get(1).getItemName()　＝" +mTodoItemList.get(1).getItemName());
//                        LogUtil.d(" Log " + " = mTodoItemList.get(1).getItemId()　＝" +mTodoItemList.get(1).getItemId());

                        // アダプターの更新
                        mAdapter.notifyDataSetChanged();
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    /**
     *
     */
    public void onClickAddTodoItemButton(View view) {

        new AddTodoDialog(
                TodoItemActivity.this,
                TodoItemActivity.this.getString(R.string.add_todo_item_dialog_title),
                TodoItemActivity.this.getString(R.string.add_todo_item_dialog_positive_button),
                TodoItemActivity.this.getString(R.string.common_string_chancel),
                db,
                todoId
        );

//        AlertDialog.Builder dialog = new AlertDialog.Builder(TodoItemActivity.this);
//        dialog.setTitle("追加する");
//
//        View todoItemView = getLayoutInflater().inflate(R.layout.dialog_add_todo_item, null);
//        final EditText toDoItemName = todoItemView.findViewById(R.id.todoItemEt);
//        dialog.setView(todoItemView);
//        dialog.setPositiveButton("追加", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (!NetworkUtil.isOnline(TodoItemActivity.this)) {
//                    // 通信不可能ならエラーダイアログをを表示
//                    AlertDialog.Builder builder = new AlertDialog.Builder(TodoItemActivity.this);
//                    builder.setTitle(R.string.add_todo_activity_network_error_dialog_title)
//                            .setMessage(R.string.add_todo_activity_network_error_dialog_body)
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                }
//                            })
//                            .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                }
//                            }).show();
//                    return;
//                }
//                String toDoItemNameSt = toDoItemName.getText().toString().trim();
//
//                LogUtil.d(" Log " + " = todoName =" + toDoItemNameSt);
//
//                if (toDoItemNameSt.isEmpty()) {
//                    Toast.makeText(TodoItemActivity.this, "内容を入力してください", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                Map<String, Object> todoItem = new HashMap<>();
//                todoItem.put("name", toDoItemNameSt);
//                todoItem.put("timestamp", FieldValue.serverTimestamp());
//
////                    TodoItem todoItem = new TodoItem();
////                    todoItem.setItemName(toDoItemNameSt);
////                    todoItem.setTodoId(todoId);
////                    todoItem.setCompleted(false);
////                    dbHandler.addToDoItem(item);
////                    refreshList();
//
//                db.collection("TodoLists")
//                        .document(todoId)
//                        .collection("TodoItems")
//                        .add(todoItem)
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Toast.makeText(TodoItemActivity.this, "追加しました", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(TodoItemActivity.this, "追加に失敗しました", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }
//        });
//        dialog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        dialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        LogUtil.d(" Log " + " = item =" + item);
        if (item.getItemId() == android.R.id.home) {
            // 今回はActivityを終了させている
            finish();
        }
        return super.onOptionsItemSelected(item);

//        Toast toast = Toast.makeText(getApplicationContext(), "MainActivity", Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();
//        return false;
    }

}