package com.example.duyda.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.example.duyda.demo.Model.Account;

import io.realm.Realm;
import io.realm.RealmResults;

public class BugActivity extends AppCompatActivity {

    private EditText txtShow;
    private TextView lbShow;
    private Button btnDelete;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug);

        realm = Realm.getDefaultInstance();

        txtShow = (EditText) findViewById(R.id.txtShow);
        lbShow = (TextView) findViewById(R.id.lbShow);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccount(txtShow.getText().toString().trim());
                showAccount();
            }
        });

        lbShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Crashlytics.getInstance().crash();
            }
        });

    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        showAccount();
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        showAccount();
    }

    private void showAccount() {
        RealmResults<Account> results = realm.where(Account.class).findAll();
        results.load();
        String output = "";
        for (Account account : results)
            output += account.toString();
        lbShow.setText(output);
    }

    private void deleteAccount(String name) {
        final RealmResults<Account> results = realm.where(Account.class)
                .equalTo("UserName", name).findAll();

        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // remove single match
//                results.deleteFirstFromRealm();
//                results.deleteLastFromRealm();

                // remove a single object
//                Account account = results.get(5);
//                account.deleteFromRealm();

                // Delete all matches
                results.deleteAllFromRealm();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
