package com.sns.todo_app_android_fireabse.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.sns.todo_app_android_fireabse.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    // youtube firebase
    // 1Firebase認証を使用したログインと登録のAndroidアプリチュートリアル-ユーザーの作成
    // https://www.youtube.com/watch?v=Z-RE1QuUWPg
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("TAG" ,"");
//        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }
        });



    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 9999){
//            if (resultCode == RESULT_OK){
//                launchActivity(mAuth.getCurrentUser());
//            }else{
//                Log.e("LoginActivity", "onActivityResult: ", IdpResponse.fromResultIntent(data).getError());
//                Toast.makeText(this, "Login Failed. Try Again", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void launchActivity(FirebaseUser currentUser) {
//        Intent i = new Intent(this,MainActivity.class);
//        i.putExtra("name",currentUser.getDisplayName());
//        i.putExtra("email",currentUser.getEmail());
//        startActivity(i);
//        finish();
//    }
//



}
