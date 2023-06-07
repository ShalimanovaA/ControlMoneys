package com.example.controlmoneys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Account extends AppCompatActivity {
    TextView name,mail,acct,family,accumulation;
    SharedPreferences sPref; public String prefName = "", curName="UserData";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);
        setTitle("Аккаунт");
        name = findViewById(R.id.textView1);
        mail = findViewById(R.id.textView2);
        acct = findViewById(R.id.textView3);
        family = findViewById(R.id.textView4);
        accumulation = findViewById(R.id.textView5);

        sPref = getSharedPreferences(curName, MODE_PRIVATE);
        prefName = sPref.getString("EMAIL","");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.change_acc, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_settings :
                Intent intent = new Intent(Account.this, ChangeAccountData.class);
                startActivity(intent);
                return true;
            case R.id.log_out:
                Intent intent_out = new Intent(Account.this, Reg_Auth.class);
                intent_out.putExtra("log_out", "log_out");
                startActivity(intent_out);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    private void loadText(){
        sPref = getSharedPreferences(prefName, MODE_PRIVATE);
        name.setText("Имя: " + sPref.getString("NAME","нет записи"));
        mail.setText("Почта: " + sPref.getString("EMAIL","нет записи"));
        acct.setText("Счёт: " + sPref.getInt("ACCT",0));
        family.setText("Семья: " + sPref.getString("FAMILY"," нет записи"));
        accumulation.setText("Накопления: " + sPref.getInt("ACCUMULATION",0));
    }
}
