package com.sns.todo_app_android_fireabse.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.sns.todo_app_android_fireabse.LogUtil;
import com.sns.todo_app_android_fireabse.R;
import com.sns.todo_app_android_fireabse.models.Todo;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder> {

    List<Todo> todoList;
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
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        Log.d("TAG", "todoList =" + todoList);

        Todo todo = todoList.get(position);
        String name = todo.getName();
        holder.tvName.setText(name);

        Timestamp timestamp = todo.getCreatedAt();
        LogUtil.d(" Log " + " = timestamp="+timestamp);
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class
    MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvCreatedAt;
        ImageButton btnUpdate, btnDelete;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvName = itemView.findViewById(R.id.tvCreatedAt);
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

    public interface OnButtonClickListener {
        public void onUpdateClicked(Todo todo);

        public void onDeleteClicked(Todo todo);
    }
}
