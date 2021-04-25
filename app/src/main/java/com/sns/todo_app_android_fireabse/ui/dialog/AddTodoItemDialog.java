package com.sns.todo_app_android_fireabse.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sns.todo_app_android_fireabse.R;
import com.sns.todo_app_android_fireabse.models.TodoItem;
import com.sns.todo_app_android_fireabse.ui.adapter.TodoItemAdapter;
import com.sns.todo_app_android_fireabse.util.NetworkUtil;

import java.util.HashMap;
import java.util.Map;

public class AddTodoItemDialog extends Dialog {

    public AddTodoItemDialog(
            Context context,
            TodoItemAdapter.OnTodoItemRefreshListener onTodoItemRefreshListener,
            String title,
            String positiveName,
            String negativeName,
            FirebaseFirestore fireStore,
            String todoId,
            TodoItem todoItem
    ) {
        super(context);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        View todoItemView = getLayoutInflater().inflate(R.layout.dialog_add_todo_item, null);
        dialog.setView(todoItemView);
        final EditText itemNameEt = todoItemView.findViewById(R.id.todoItemEt);
        // アップデートの時はEditTextに初期値を入力
        if (todoItem != null) {
            itemNameEt.setText(todoItem.getName());
        }
        dialog.setPositiveButton(positiveName, (dialogInterface, i) -> {
                    if (!NetworkUtil.isOnline(context)) {
                        // 通信不可能ならエラーダイアログをを表示
                        NetworkUtil.showNetWorkErrorDialog(context);
                    }

                    String toDoItemNameSt = itemNameEt.getText().toString().trim();

                    if (toDoItemNameSt.isEmpty()) {
                        Toast.makeText(context, "内容を入力してください", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Map<String, Object> newTodoItem = new HashMap<>();
                    newTodoItem.put("name", toDoItemNameSt);
                    newTodoItem.put("timestamp", FieldValue.serverTimestamp());

                    if (todoItem == null) {
                        // 新規作成時の処理
                        fireStore.collection("TodoLists")
                                .document(todoId)
                                .collection("TodoItems")
                                .add(newTodoItem)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        onTodoItemRefreshListener.onRefresh();
                                        Toast.makeText(context, "追加しました", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "追加に失敗しました", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // 更新時の処理
                        fireStore.collection("TodoLists")
                                .document(todoId)
                                .collection("TodoItems")
                                .document(todoItem.getId())
                                .set(newTodoItem)
                                .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        onTodoItemRefreshListener.onRefresh();
                                        Toast.makeText(context, "更新しました", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "追加に失敗しました", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
        );
        dialog.setNegativeButton(negativeName, null);
        dialog.show();
    }
}
