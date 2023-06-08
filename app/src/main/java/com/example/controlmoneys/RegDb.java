package com.example.controlmoneys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class RegDb extends AppCompatActivity {
    EditText email_ed,passwd_ed;
    // для админа
    private String email_admin = "admin@admin.ru", password_admin = "000000";
    TextView enter, register, tobd;
    // для SharedPreferences
    SharedPreferences sPref; public String prefName="", curName="UserData", allUsers="alls";
    // для проверки на подключение
    Context context;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg);
        init();
        if(!icConnected()) Toast.makeText(getApplicationContext(), "Пожалуйста, проверьте интернет соединение", Toast.LENGTH_SHORT).show();
    }
    protected void onResume() {
        super.onResume();
        Bundle arguments = getIntent().getExtras();
        //текущий пользователь
        sPref = getSharedPreferences(curName,MODE_PRIVATE);
        if (arguments != null) {
            if (arguments.get("log_out").toString().equals("log_out")) {
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString("EMAIL", "log_out");
                ed.apply();
            }
        }
        if (!(sPref.getString("EMAIL", "").equals("log_out")) && !(sPref.getString("EMAIL", "").equals(""))) {
            String name = "Вы вошли в аккаунт как " + sPref.getString("EMAIL","");
            Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegDb.this, MainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Вы вышли", Toast.LENGTH_SHORT).show();
        }
    }
    private void init(){
        // кнопки
        enter = findViewById(R.id.buttonEnter);
        register = findViewById(R.id.buttonReg);
        tobd = findViewById(R.id.button_to_bd);
        tobd.setVisibility(View.GONE);

        // поля
        email_ed = findViewById(R.id.editEmail);
        passwd_ed = findViewById(R.id.editTextPassword);

        sPref = getSharedPreferences(curName, MODE_PRIVATE);
    }

    public void reg(View view){
        if(!TextUtils.isEmpty(email_ed.getText().toString()) && !TextUtils.isEmpty(passwd_ed.getText().toString())) {
            sPref = getSharedPreferences(allUsers, MODE_PRIVATE);
            if (!sPref.getString(email_ed.getText().toString(), "").equals("")) {
                Toast.makeText(getApplicationContext(), "У вас уже есть аккаунт. Нажмите войти", Toast.LENGTH_SHORT).show();
            } else {
                prefName = email_ed.getText().toString();
                // объект editot
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(email_ed.getText().toString(), passwd_ed.getText().toString());
                ed.apply();
                saveText(email_ed.getText().toString());
                Toast.makeText(getApplicationContext(), "Добро пожаловать!", Toast.LENGTH_SHORT).show();
                //переходим
                Intent intent = new Intent(RegDb.this, Form.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //записать в файл
                startActivity(intent);
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Поля должны быть заполнены",Toast.LENGTH_SHORT).show();
        }
    }
    public void enter(View view) {
        if(!TextUtils.isEmpty(email_ed.getText().toString()) && !TextUtils.isEmpty(passwd_ed.getText().toString())) {
            if (email_ed.getText().toString().equals(email_admin) && passwd_ed.getText().toString().equals(password_admin)) {
                tobd.setVisibility(View.VISIBLE);
                email_ed.setText("");
                passwd_ed.setText("");
            }
            else{
                sPref = getSharedPreferences(allUsers, MODE_PRIVATE);
                if (sPref.getString(email_ed.getText().toString(), "").equals(passwd_ed.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Добро пожаловать", Toast.LENGTH_SHORT).show();
                    saveText(email_ed.getText().toString());
                    //переходим
                    Intent intent = new Intent(RegDb.this, MainMenu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //записать в файл
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Зарегистрируйтесь", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"Поля должны быть заполнены",Toast.LENGTH_SHORT).show();
        }
    }
    // кнопка для админа
    public void toBd(View view){
        Intent intent = new Intent(this, ReadBd.class);
        startActivity(intent);
    }
    // сохранение почты текущего пользователя
    private void saveText(String text){
        // получаем объект для считывания и записи данных, настройка доступа только этому приложению
        sPref = getSharedPreferences(prefName, MODE_PRIVATE);
        // объект editot
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("EMAIL", text);
        ed.apply();
        // текущий пользователь
        sPref = getSharedPreferences(curName, MODE_PRIVATE);
        // объект editot
        ed = sPref.edit();
        ed.putString("EMAIL", text);
        ed.apply();
    }
    private boolean icConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}
