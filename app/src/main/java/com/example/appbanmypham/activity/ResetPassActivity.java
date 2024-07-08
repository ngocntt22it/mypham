package com.example.appbanmypham.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appbanmypham.R;
import com.example.appbanmypham.retrofit.ApiBanHang;
import com.example.appbanmypham.retrofit.RetrofitClient;
import com.example.appbanmypham.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ResetPassActivity extends AppCompatActivity {
    EditText email;
    AppCompatButton btnreset;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        initView();
        initControl();
    }

    private void initControl() {
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getText().toString().trim();
                if (TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập Email", Toast.LENGTH_SHORT).show();

                }else {
                    // Hiển thị thanh tiến trình
                    progressBar.setVisibility(View.VISIBLE);
                    //reset pass onfirebase
                    FirebaseAuth.getInstance().sendPasswordResetEmail(str_email)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra Email của bạn", Toast.LENGTH_LONG).show();
                                }
                                finish();
                            });
//                    compositeDisposable.add(apiBanHang.resetPass(str_email)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(
//                                    userModel -> {
//                                        if (userModel.isSuccess()){
//                                            Toast.makeText(getApplicationContext(),userModel.getMessage(), Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
//                                            startActivity(intent);
//                                            finish();
//                                        }else {
//                                            Toast.makeText(getApplicationContext(),userModel.getMessage(), Toast.LENGTH_SHORT).show();
//                                        }
//                                        progressBar.setVisibility(View.INVISIBLE);
//                                    },
//                                    throwable -> {
//                                        Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                                        progressBar.setVisibility(View.INVISIBLE);
//                                    }
//                            ));

                }

            }
        });
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.edtresetpass);
        btnreset = findViewById(R.id.btnresetpass);
        progressBar = findViewById(R.id.progressbar);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}