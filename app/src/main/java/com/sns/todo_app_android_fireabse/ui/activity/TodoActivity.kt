package com.sns.todo_app_android_fireabse.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sns.todo_app_android_fireabse.R
import com.sns.todo_app_android_fireabse.models.Todo
import com.sns.todo_app_android_fireabse.ui.adapter.TodoListAdapter
import com.sns.todo_app_android_fireabse.ui.adapter.TodoListAdapter.OnTodoListRefreshListener
import com.sns.todo_app_android_fireabse.ui.dialog.AddTodoDialog
import java.util.*

class TodoActivity : AppCompatActivity() {
    private var mFireStore: FirebaseFirestore? = null
    private var mAuth: FirebaseAuth? = null
    private val mTodoList: MutableList<Todo?> = ArrayList()
    private var mAdapter: TodoListAdapter? = null

    // View関係のメンバ変数
    private var mProgressBar: FrameLayout? = null
    private var mOnTodoListRefreshListener: OnTodoListRefreshListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
        mAuth = FirebaseAuth.getInstance()
        mFireStore = FirebaseFirestore.getInstance()
        mOnTodoListRefreshListener = setTodoListRefreshListener()

        // Viewの取得
        val rvTodo = findViewById<RecyclerView>(R.id.rvTodo)
        mProgressBar = findViewById(R.id.progressBar)
        mAdapter = TodoListAdapter(
            this@TodoActivity,
            setTodoListRefreshListener(),
            mTodoList,
            mFireStore,
            mAuth
        )
        rvTodo.layoutManager = LinearLayoutManager(this)
        rvTodo.adapter = mAdapter
    }

    override fun onResume() {
        super.onResume()
        refreshTodoList()
    }

    /**
     * TodoListを追加
     *
     * @param view
     */
    fun onClickAddTodoButton(view: View?) {
        AddTodoDialog(
            this@TodoActivity,
            null,
            mOnTodoListRefreshListener,
            this@TodoActivity.getString(R.string.add_todo_list_dialog_title),
            this@TodoActivity.getString(R.string.common_add_positive_button),
            this@TodoActivity.getString(R.string.common_string_chancel),
            mFireStore,
            mAuth
        )
    }

    /**
     * TodoListを更新するリスナーをセット
     *
     * @return
     */
    private fun setTodoListRefreshListener(): OnTodoListRefreshListener {
        return OnTodoListRefreshListener { refreshTodoList() }
    }

    /**
     * TodoListを更新
     */
    private fun refreshTodoList() {
        mProgressBar!!.visibility = View.VISIBLE
        val TodoLists = mFireStore!!.collection("User")
        TodoLists.document(mAuth?.uid!!)
            .collection("TodoLists")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener(this@TodoActivity) { task ->
                mTodoList.clear()
                for (doc in task.result!!) {
                    val todo = doc.toObject(Todo::class.java)
                    todo.id = doc.id
                    mTodoList.add(todo)
                }
                // アダプターの更新
                mAdapter!!.notifyDataSetChanged()
                mProgressBar!!.visibility = View.GONE
            }
    }
}