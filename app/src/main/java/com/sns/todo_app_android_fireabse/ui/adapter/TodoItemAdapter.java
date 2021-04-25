package com.sns.todo_app_android_fireabse.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sns.todo_app_android_fireabse.R;
import com.sns.todo_app_android_fireabse.models.TodoItem;
import com.sns.todo_app_android_fireabse.ui.dialog.AddTodoItemDialog;

import java.util.List;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemAdapter.MyViewHolder> {

    private List<TodoItem> mTodoItemList;
    private Activity mActivity;
    private FirebaseFirestore mFireStore;
    private String mTodoId;
    private OnTodoItemRefreshListener mOnTodoItemRefreshListener;

    public TodoItemAdapter(
            Activity activity,
            OnTodoItemRefreshListener onTodoItemRefreshListener,
            List<TodoItem> todoItemList,
            FirebaseFirestore fireStore,
            String todoId
    ) {
        this.mActivity = activity;
        this.mOnTodoItemRefreshListener = onTodoItemRefreshListener;
        this.mTodoItemList = todoItemList;
        this.mFireStore = fireStore;
        this.mTodoId = todoId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TodoItem todoItem = mTodoItemList.get(position);
        // TODOのタイトル名
        String name = todoItem.getName();
        holder.tvName.setText(name);
        holder.btnUpdate.setOnClickListener(v -> updateTodoItem(todoItem));
        holder.btnDelete.setOnClickListener(v -> deleteTodoItem(todoItem));
    }

    /**
     * TodoItemを更新する処理
     */
    private void updateTodoItem(TodoItem todoItem) {
        new AddTodoItemDialog(
                mActivity,
                mOnTodoItemRefreshListener,
                mActivity.getString(R.string.common_update_positive_button),
                mActivity.getString(R.string.common_update_positive_name),
                mActivity.getString(R.string.common_string_chancel),
                mFireStore,
                mTodoId,
                todoItem
        );
    }

    /**
     * TodoItemを削除する処理
     */
    private void deleteTodoItem(TodoItem todoItem) {
        mFireStore.collection("TodoLists")
                .document(mTodoId)
                .collection("TodoItems")
                .document(todoItem.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mOnTodoItemRefreshListener.onRefresh();
                        Toast.makeText(mActivity, todoItem.getName() + "を削除しました", Toast.LENGTH_SHORT).show();
                        mTodoItemList.remove(todoItem);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return mTodoItemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout todoListLayout;
        TextView tvName;
        TextView tvTimestamp;
        ImageButton btnUpdate, btnDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            todoListLayout = itemView.findViewById(R.id.todoListLayout);
            tvName = itemView.findViewById(R.id.tvName);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public interface OnTodoItemRefreshListener {
        public void onRefresh();
    }
}
