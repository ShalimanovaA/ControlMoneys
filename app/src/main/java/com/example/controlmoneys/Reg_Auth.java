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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

public class Reg_Auth extends AppCompatActivity {
    EditText email_ed,passwd_ed;
    // для админа
    private String email_admin = "admin@admin.ru", password_admin = "000000";
    TextView enter, register, tobd;
    // для доступа к бд (ссылка на бд)
    private FirebaseAuth mAuth;public FirebaseUser cUser;
    // для SharedPreferences
    SharedPreferences sPref; public String prefName="", curName="UserData";
    // для проверки на подключение
    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg);
        sPref = getSharedPreferences(curName, MODE_PRIVATE);
        init();
        if(!icConnected()) Toast.makeText(getApplicationContext(), "Пожалуйста, проверьте интернет соединение", Toast.LENGTH_SHORT).show();
    }

    // проверка на регистрацию
    @Override
    protected void onResume() {
        super.onResume();
        cUser = mAuth.getCurrentUser();
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            if (arguments.get("log_out").toString().equals("log_out")) {
                cUser = null;
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString("EMAIL", "log_out");
                ed.apply();
            }
        }
        if ((cUser != null) && !(sPref.getString("EMAIL", "").equals("log_out"))) {
            String name = "Вы вошли в аккаунт как " + cUser.getEmail();
            Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Reg_Auth.this, MainMenu.class);
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

        //бд
        mAuth = FirebaseAuth.getInstance();
    }
    public void reg(View view){
        if(!TextUtils.isEmpty(email_ed.getText().toString()) && !TextUtils.isEmpty(passwd_ed.getText().toString())){
                mAuth.createUserWithEmailAndPassword(email_ed.getText().toString(), passwd_ed.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            prefName = email_ed.getText().toString();
                            saveText(email_ed.getText().toString());
                            Toast.makeText(getApplicationContext(),"Добро пожаловать!",Toast.LENGTH_SHORT).show();
                            //переходим
                            Intent intent = new Intent(Reg_Auth.this, Form.class);
                            //записать в файл
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Что-то пошло не так...!",Toast.LENGTH_SHORT).show();
                            if(passwd_ed.getText().toString().length() < 6)
                                Toast.makeText(getApplicationContext(),"Ваш пароль должен быть больше 5 символов",Toast.LENGTH_SHORT).show();
                            else Toast.makeText(getApplicationContext(),"Проверьте корректность email адреса",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        }
        else{
            Toast.makeText(getApplicationContext(),"Поля должны быть заполнены",Toast.LENGTH_SHORT).show();
        }
    }
    public void enter(View view) {
        if(!TextUtils.isEmpty(email_ed.getText().toString()) && !TextUtils.isEmpty(passwd_ed.getText().toString())){
            if(email_ed.getText().toString().equals(email_admin) && passwd_ed.getText().toString().equals(password_admin))
            {
                tobd.setVisibility(View.VISIBLE);
                email_ed.setText(""); passwd_ed.setText("");
            }
            else{
                    try {
                        mAuth.signInWithEmailAndPassword(email_ed.getText().toString(), passwd_ed.getText().toString())
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            prefName = email_ed.getText().toString();
                                            saveText(email_ed.getText().toString());
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Toast.makeText(getApplicationContext(),"Добро пожаловать!",Toast.LENGTH_SHORT).show();
                                            //переходим
                                            Intent intent = new Intent(Reg_Auth.this, MainMenu.class);
                                            //записать в файл

                                            startActivity(intent);
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Вы не зарегистрированы", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }catch (Exception e){
                        Toast.makeText(this, "Вы не зарегистрированы", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
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
