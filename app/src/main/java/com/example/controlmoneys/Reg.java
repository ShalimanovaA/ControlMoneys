package com.example.controlmoneys;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Reg extends AppCompatActivity {
    EditText email_ed,passwd_ed;
    private String email_admin = "admin@admin.ru", password_admin = "000000";
    TextView enter, register, tobd;
    // для доступа к бд (ссылка на бд)
    private DatabaseReference mDataBase, dbForAdmin;
    private String USER_KEY = "User";
    private String ADMIN_KEY = "ADMIN";
    private ArrayAdapter<String> adapter;
    private List<String> data;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg);

        init();
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
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        dbForAdmin = FirebaseDatabase.getInstance().getReference(ADMIN_KEY);

        // для админа
        User new_user = new User(dbForAdmin.getKey(), email_admin, password_admin);
        User new_user1 = new User(mDataBase.getKey(), "helpdesk@mail.ru", "1");
        User new_user2 = new User(mDataBase.getKey(), "server@mail.ru", "1");
        dbForAdmin.push().setValue(new_user); dbForAdmin.push().setValue(new_user1);
        dbForAdmin.push().setValue(new_user2);


        data = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
    }


    // проверка на заполнение полей
    public boolean checkEd(){
        if(!email_ed.getText().toString().equals("") && !passwd_ed.getText().toString().equals("")){
            // если поля заполнены, можно регистрировать
            return true;
        }
        else{
            // вывести сообщение о необходимости заполнить
            Toast.makeText(Reg.this,"Для начала заполните все поля", Toast.LENGTH_SHORT);
            return false;
        }
    }
    //проверка на админа
    public void checkAdmin(){
        if(email_ed.getText().toString().equals(email_admin) && passwd_ed.getText().toString().equals(password_admin)){
            tobd.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Вы вошли как администратор", Toast.LENGTH_SHORT);
        }
    }
    // проверка на существование в бд


    public void reg(View view){
        // проверяем ввод на корректность
        if (checkEd()){
            // проверить на существавание в бд

            // запись в бд
            String id = mDataBase.getKey();
            // сериализуем данные
            User new_user = new User(id, email_ed.getText().toString(), passwd_ed.getText().toString());
            mDataBase.push().setValue(new_user);
            // на новую страницу анкута
            Intent intent = new Intent(this, Form.class);
            startActivity(intent);
        }
    }
    public void enter(View view){
        if (checkEd()){
            checkAdmin();
            // проверить на существавание в бд войти


            // на основной экран
        }
    }
    // кнопка для админа
    public void toBd(View view){
        Intent intent = new Intent(Reg.this, ReadBd.class);
        startActivity(intent);
    }

}
