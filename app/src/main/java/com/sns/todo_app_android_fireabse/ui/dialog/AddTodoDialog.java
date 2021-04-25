package com.sns.todo_app_android_fireabse.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sns.todo_app_android_fireabse.LogUtil;
import com.sns.todo_app_android_fireabse.R;
import com.sns.todo_app_android_fireabse.models.Todo;
import com.sns.todo_app_android_fireabse.ui.adapter.TodoListAdapter;
import com.sns.todo_app_android_fireabse.util.NetworkUtil;

import java.util.HashMap;
import java.util.Map;

public class AddTodoDialog extends Dialog {

    public AddTodoDialog(
            Activity activity,
            Todo todo,
            TodoListAdapter.OnTodoListRefreshListener onTodoListRefreshListener,
            String title,
            String positiveName,
            String negativeName,
            FirebaseFirestore fireStore,
            FirebaseAuth auth
    ) {
        super(activity);
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(title);
        View todoItemView = getLayoutInflater().inflate(R.layout.dialog_add_todo_item, null);
        dialog.setView(todoItemView);
        final EditText itemNameEt = todoItemView.findViewById(R.id.todoItemEt);

        LogUtil.d(" Log "
                + " = auth.getUid();" +auth.getUid()
        );

        // アップデートの時はEditTextに初期値を入力
        if (todo != null) {
            itemNameEt.setText(todo.getName());
        }
        dialog.setPositiveButton(positiveName, (dialogInterface, i) -> {
                    if (!NetworkUtil.isOnline(activity)) {
                        // 通信不可能ならエラーダイアログをを表示
                        NetworkUtil.showNetWorkErrorDialog(activity);
                    }

                    String toDoListNameSt = itemNameEt.getText().toString().trim();

                    if (toDoListNameSt.isEmpty()) {
                        Toast.makeText(activity, "内容を入力してください", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // モデルからリストに追加
                    Map<String, Object> newTodoList = new HashMap<>();
                    newTodoList.put("name", toDoListNameSt);

                    // Todo 位置を変えないように、既に作成されている場合はそのタイムスタンプを使用する
                    newTodoList.put("timestamp", FieldValue.serverTimestamp());

                    if (todo == null) {
//                        fireStore.collection("TodoLists")
//                                .add(newTodoList)
//                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                    @Override
//                                    public void onSuccess(DocumentReference documentReference) {
//                                        Toast.makeText(activity, "投稿しました", Toast.LENGTH_SHORT).show();
//                                        onTodoListRefreshListener.onRefresh();
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(activity, "投稿に失敗しました", Toast.LENGTH_SHORT).show();
//                                    }
//                                });

                        fireStore.collection("User")
                                .document(auth.getUid())
                                .collection("TodoList")
                                .add(newTodoList)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        onTodoListRefreshListener.onRefresh();
                                        Toast.makeText(activity, "追加しました", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(activity, "追加に失敗しました", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        fireStore.collection("TodoLists")
                                .document(todo.getId())
                                .set(newTodoList)
                                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(activity, "更新しました", Toast.LENGTH_SHORT).show();
                                        onTodoListRefreshListener.onRefresh();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(activity, "更新に失敗しました", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

//                    if (todoItem == null) {
//                        // 新規作成時の処理
//                        fireStore.collection("TodoLists")
//                                .document(todoId)
//                                .collection("TodoItems")
//                                .add(newTodoItem)
//                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                    @Override
//                                    public void onSuccess(DocumentReference documentReference) {
//                                        onTodoItemRefreshListener.onRefresh();
//                                        Toast.makeText(context, "追加しました", Toast.LENGTH_SHORT).show();
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(context, "追加に失敗しました", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                    } else {
//                        // 更新時の処理
//                        fireStore.collection("TodoLists")
//                                .document(todoId)
//                                .collection("TodoItems")
//                                .document(todoItem.getId())
//                                .set(newTodoItem)
//                                .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        onTodoItemRefreshListener.onRefresh();
//                                        Toast.makeText(context, "更新しました", Toast.LENGTH_SHORT).show();
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(context, "追加に失敗しました", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                    }
                }
        );
        dialog.setNegativeButton(negativeName, null);
        dialog.show();
    }
}
