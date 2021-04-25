package com.sns.todo_app_android_fireabse.ui.adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sns.todo_app_android_fireabse.R;
import com.sns.todo_app_android_fireabse.models.Todo;
import com.sns.todo_app_android_fireabse.ui.activity.TodoItemActivity;
import com.sns.todo_app_android_fireabse.ui.dialog.AddTodoDialog;

import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.MyViewHolder> {

    private FirebaseFirestore mFireStore;
    private FirebaseAuth mAuth;

    private List<Todo> mTodoList;

    private AppCompatActivity mActivity;
    private OnTodoListRefreshListener mOnTodoListRefreshListener;

    public TodoListAdapter(
            AppCompatActivity activity,
            OnTodoListRefreshListener onTodoListRefreshListener,
            List<Todo> todo,
            FirebaseFirestore fireStore,
            FirebaseAuth auth
    ) {
        this.mActivity = activity;
        this.mOnTodoListRefreshListener = onTodoListRefreshListener;
        this.mFireStore = fireStore;
        this.mTodoList = todo;
        this.mAuth = auth;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Todo todo = mTodoList.get(position);
        // TODOのタイトル名
        String name = todo.getName();
        holder.tvName.setText(name);
        // クリックリスナーのセット
        holder.todoListLayout.setOnClickListener(v -> transitionTodoItemActivity(todo));
        holder.btnUpdate.setOnClickListener(v -> updateTodoList(todo));
        holder.btnDelete.setOnClickListener(v -> deleteTodoList(todo));
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }

    public void updateTodoList(Todo todo) {
        new AddTodoDialog(
                mActivity,
                todo,
                mOnTodoListRefreshListener,
                mActivity.getString(R.string.update_todo_list_dialog_title),
                mActivity.getString(R.string.common_update_positive_button),
                mActivity.getString(R.string.common_string_chancel),
                mFireStore,
                mAuth
        );
    }

    public void deleteTodoList(Todo todo) {
        mFireStore.collection("TodoLists").
                document(todo.getId())
                .delete()
                .addOnCompleteListener(mActivity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(mActivity, todo.getName() + "を削除しました", Toast.LENGTH_SHORT).show();
                        mTodoList.remove(todo);
                        mOnTodoListRefreshListener.onRefresh();
                    }
                });
    }

    /**
     * TodoItemActivityに遷移する処理
     */
    private void transitionTodoItemActivity(Todo todo) {
        Intent intent = new Intent(mActivity, TodoItemActivity.class);
        intent.putExtra(TodoItemActivity.INTENT_TODO_ID, todo.getId());
        intent.putExtra(TodoItemActivity.INTENT_TODO_NAME, todo.getName());
        mActivity.startActivity(intent);
    }

    /**
     * TodoListを更新するリスナー
     */
    public interface OnTodoListRefreshListener {
        public void onRefresh();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
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

//            btnUpdate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onUpdateClicked(todoList.get(getAdapterPosition()));
//                }
//            });
//
//            btnDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onDeleteClicked(todoList.get(getAdapterPosition()));
//                }
//            });
        }
    }
}
