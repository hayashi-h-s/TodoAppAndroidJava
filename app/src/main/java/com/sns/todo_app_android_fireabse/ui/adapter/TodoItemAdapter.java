package com.sns.todo_app_android_fireabse.ui.adapter;


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
import com.google.firebase.firestore.FirebaseFirestore;
import com.sns.todo_app_android_fireabse.LogUtil;
import com.sns.todo_app_android_fireabse.R;
import com.sns.todo_app_android_fireabse.models.TodoItem;

import java.util.List;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemAdapter.MyViewHolder> {

    private List<TodoItem> mTodoItemList;
    private AppCompatActivity mActivity;
    private FirebaseFirestore mDb;
    private String mTodoId;

    public TodoItemAdapter(
            AppCompatActivity activity,
            List<TodoItem> todoItemList,
            FirebaseFirestore db,
            String todoId
    ) {
        this.mActivity = activity;
        this.mTodoItemList = todoItemList;
        this.mDb = db;
        this.mTodoId = todoId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        LogUtil.d(" Log " + " = todoItemList.size" + mTodoItemList.size());

        TodoItem todoItem = mTodoItemList.get(position);
        // TODOのタイトル名
        String name = todoItem.getName();

        LogUtil.d(" Log " + " = name  =" + name);

        holder.tvName.setText(name);

        // 時間は後で設定
//        Timestamp timestamp = todo.getTimestamp();
//        String timeSt = sdf.format(timestamp.toDate());
//        holder.tvTimestamp.setText(timeSt);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/h:m");

        holder.btnUpdate.setOnClickListener(v -> todoItemUpdate(todoItem));
        holder.btnDelete.setOnClickListener(v -> todoItemDelete(todoItem));

//        holder.todoListLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LogUtil.d(" Log " + " = ");
//            }
//        });

    }

    /**
     * TodoItemを更新する処理
     */
    private void todoItemUpdate(TodoItem todoItem) {
        LogUtil.d(" Log " + " = todoItem =" + todoItem);
    }

    /**
     * TodoItemを削除する処理
     */
    private void todoItemDelete(TodoItem todoItem) {

        LogUtil.d(" Log " + " = todoItem =" + todoItem.getId()
                + " mTodoId =" + mTodoId
        );

        mDb.collection("todoLists")
                .document(mTodoId)
                .collection("TodoItems")
                .document(todoItem.getId())
                .delete()
                .addOnCompleteListener(mActivity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        LogUtil.d(" Log " + " = todoItem.getName()　＝ " + todoItem.getName());

                        Toast.makeText(mActivity, todoItem.getName() + "を削除しました", Toast.LENGTH_SHORT).show();
                        mTodoItemList.remove(todoItem);
//                        mAdapter.notifyDataSetChanged();
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
            tvTimestamp = itemView.findViewById(R.id.tvTimestampAt);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
