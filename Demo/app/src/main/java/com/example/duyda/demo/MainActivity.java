package com.example.duyda.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duyda.demo.Model.Account;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private EditText txtUN, txtPW;
    private Button btnLogin, btnSignUp;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();

        txtUN = (EditText) findViewById(R.id.txtName);
        txtPW = (EditText) findViewById(R.id.txtPass);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtUN.getText().toString().isEmpty() || txtPW.getText().toString().isEmpty())
                    Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                else {
                    if (checkLogin(txtUN.getText().toString().trim(), txtPW.getText().toString().trim())) {
                        Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, BugActivity.class);
                        startActivity(intent);
                    } else
                        Toast.makeText(MainActivity.this, "Lỗi đăng nhập", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtUN.getText().toString().isEmpty() || txtPW.getText().toString().isEmpty())
                    Toast.makeText(MainActivity.this, "Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                else {
                    if (checkExist(txtUN.getText().toString().trim()))
                        Toast.makeText(MainActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                    else
                    {
                        addAccount(txtUN.getText().toString().trim(), txtPW.getText().toString());
                        Toast.makeText(MainActivity.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private Boolean checkExist(String name) {
        Account result = realm.where(Account.class)
                .equalTo("UserName", name)
                .findFirst();
        if (result != null)
            return true;
        else
            return false;
    }

    private Boolean checkLogin(String name, String pass) {
        Account result = realm.where(Account.class)
                .equalTo("UserName", name).and().equalTo("PassWord", pass)
                .findFirst();
        if (result != null)
            return true;
        else
            return false;
    }

    private void addAccount(final String name, final String pass) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Account account = bgRealm.createObject(Account.class);
                account.setUserName(name);
                account.setPassWord(pass);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                Log.v("Success", ">>>>>>>>>>>>>>>OK<<<<<<<<<<<<<<<");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e("Failed", error.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
