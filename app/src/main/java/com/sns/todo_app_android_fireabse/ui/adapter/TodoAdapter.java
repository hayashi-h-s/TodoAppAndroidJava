package com.sns.todo_app_android_fireabse.ui.adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sns.todo_app_android_fireabse.R;
import com.sns.todo_app_android_fireabse.models.Todo;
import com.sns.todo_app_android_fireabse.ui.activity.TodoItemActivity;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder> {

    List<Todo> todoList;
    Todo mTodo;

    OnButtonClickListener listener;
    private AppCompatActivity mActivity;

    public TodoAdapter(
            AppCompatActivity activity,
            List<Todo> todoList,
            OnButtonClickListener listener
    ) {
        this.mActivity = activity;
        this.todoList = todoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        mTodo = todoList.get(position);
        // TODOのタイトル名
        String name = mTodo.getName();
        holder.tvName.setText(name);

        // 時間は後で設定
//        Timestamp timestamp = todo.getTimestamp();
//        String timeSt = sdf.format(timestamp.toDate());
//        holder.tvTimestamp.setText(timeSt);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/h:m");

        // クリックリスナーのセット
        holder.todoListLayout.setOnClickListener(this::onClickTodoUpdate);

//        onClickTodoUpdate(
//                holder.todoListLayout,
//                todo
//        );

//        holder.todoListLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LogUtil.d(" Log " + " = ");
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void onClickTodoUpdate(
            View view
    ) {
        Intent intent = new Intent(mActivity, TodoItemActivity.class);
        intent.putExtra(TodoItemActivity.INTENT_TODO_ID, mTodo.getId());
        intent.putExtra(TodoItemActivity.INTENT_TODO_NAME, mTodo.getName());
        mActivity.startActivity(intent);
    }

    public interface OnButtonClickListener {
        public void onUpdateClicked(Todo todo);

        public void onDeleteClicked(Todo todo);
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
            tvTimestamp = itemView.findViewById(R.id.tvTimestampAt);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUpdateClicked(todoList.get(getAdapterPosition()));
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteClicked(todoList.get(getAdapterPosition()));
                }
            });
        }
    }
}
