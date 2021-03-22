package com.sns.todo_app_android_fireabse.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sns.todo_app_android_fireabse.LogUtil;
import com.sns.todo_app_android_fireabse.R;
import com.sns.todo_app_android_fireabse.models.Todo;
import com.sns.todo_app_android_fireabse.models.TodoItem;

public class TodoItemActivity extends AppCompatActivity {

    public static final String INTENT_TODO_INSTANCE = "INTENT_TODO_INSTANCE";

    private Todo mTodo;

    private String todoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_item);

//        todoId = getIntent().getStringExtra("id");
//        String name = getIntent().getStringExtra("name");

        mTodo = (Todo) getIntent().getSerializableExtra(INTENT_TODO_INSTANCE);

        Log.d("Log",
                " id =" + mTodo.getId() +
                        " name =" + mTodo.getName()
        );

        TextView mToolbarTitle = findViewById(R.id.toolbar_title);
        mToolbarTitle.setText(mTodo.getName());

    }

    public void onClickAddTodoItemButton(View view) {

        LogUtil.d(" Log " + " = ");

        AlertDialog.Builder dialog = new AlertDialog.Builder(TodoItemActivity.this);
        dialog.setTitle("アイテムを追加");
        View todoItemView = getLayoutInflater().inflate(R.layout.dialog_add_todo_item, null);
        final EditText toDoName = view.findViewById(R.id.todoItemEt);
        dialog.setView(todoItemView);
        dialog.setPositiveButton("追加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                LogUtil.d(" Log " + " = onClickAddTodoItemButton =");

                if (toDoName.getText().toString().length() > 0) {
                    TodoItem todoItem = new TodoItem();
                    todoItem.setItemName(toDoName.getText().toString());
                    todoItem.setTodoId(todoId);
//                    todoItem.setCompleted(false);
//                    dbHandler.addToDoItem(item);
//                    refreshList();




                }
            }
        });
        dialog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();

    }
}