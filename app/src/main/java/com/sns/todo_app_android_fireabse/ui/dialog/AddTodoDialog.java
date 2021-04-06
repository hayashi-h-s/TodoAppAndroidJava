package com.sns.todo_app_android_fireabse.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sns.todo_app_android_fireabse.LogUtil;
import com.sns.todo_app_android_fireabse.R;
import com.sns.todo_app_android_fireabse.util.NetworkUtil;

import java.util.HashMap;
import java.util.Map;

public class AddTodoDialog extends Dialog {

    public AddTodoDialog(
            Context context,
            String title,
            String positiveName,
            String negativeName,
            FirebaseFirestore db,
            String todoId
    ) {
        super(context);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        View todoItemView = getLayoutInflater().inflate(R.layout.dialog_add_todo_item, null);
        dialog.setView(todoItemView);
        dialog.setPositiveButton(positiveName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        LogUtil.d(" Log " + " = ポジティブボタン");

                        if (!NetworkUtil.isOnline(context)) {
                            // 通信不可能ならエラーダイアログをを表示
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                        }

                        final EditText itemName = todoItemView.findViewById(R.id.todoItemEt);
                        String toDoItemNameSt = itemName.getText().toString().trim();

                        LogUtil.d(" Log " + " = todoName =" + toDoItemNameSt);

                        if (toDoItemNameSt.isEmpty()) {
                            Toast.makeText(context, "内容を入力してください", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Map<String, Object> todoItem = new HashMap<>();
                        todoItem.put("name", toDoItemNameSt);
                        todoItem.put("timestamp", FieldValue.serverTimestamp());

//                    TodoItem todoItem = new TodoItem();
//                    todoItem.setItemName(toDoItemNameSt);
//                    todoItem.setTodoId(todoId);
//                    todoItem.setCompleted(false);
//                    dbHandler.addToDoItem(item);
//                    refreshList();

                        db.collection("TodoLists")
                                .document(todoId)
                                .collection("TodoItems")
                                .add(todoItem)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(context, "追加しました", Toast.LENGTH_SHORT).show();
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

    /**
     * 専用リスナー
     */
    public interface AddTodoDialogInteractionListener {

        /**
         * ポジティブボタン押下時に呼ばれる処理
         */
        void onClickPositiveButton(String text);
    }
}
