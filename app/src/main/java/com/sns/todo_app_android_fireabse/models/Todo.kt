package com.sns.todo_app_android_fireabse.models

import com.google.firebase.Timestamp
import java.io.Serializable

class Todo : Serializable {
    //     class Timestamp implements Serializable {}
    var id: String? = null
    var name: String? = null
    private var timestamp: Timestamp? = null

    // 空のコンストラクタを用意しないと不具合になる
//    constructor() {}
//    constructor(id: String?, name: String?, timestamp: Timestamp?) {
//        this.id = id
//        this.name = name
//        this.timestamp = timestamp
//    }
}