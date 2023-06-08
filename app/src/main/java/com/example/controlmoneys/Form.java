package com.example.controlmoneys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;

public class Form extends AppCompatActivity {
    public String EMAIL, ID;
    public int user_period=1;
    public boolean isInDb = false;
    public EditText money_user;
    TextView quest,y,n;
    SharedPreferences sPref; public String prefName = "", curName="UserData";



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Аккаунт");

        sPref = getSharedPreferences(curName, MODE_PRIVATE);
        prefName = sPref.getString("EMAIL","");
        sPref = getSharedPreferences(prefName, MODE_PRIVATE);
        // анкета при регистрации
        setContentView(R.layout.form);
        //скрыть элемента
        quest = findViewById(R.id.textView8);
        quest.setVisibility(View.GONE);
        y = findViewById(R.id.textView9);
        y.setVisibility(View.GONE);
        n = findViewById(R.id.textView10);
        n.setVisibility(View.GONE);

        getData();

        money_user = findViewById(R.id.editTextNumberDecimal);

        sPref = getSharedPreferences(prefName, MODE_PRIVATE);
        EMAIL=sPref.getString("EMAIL","");

    }

    private void getData() {
        if(sPref.getInt("ZP",0) != 0) {
            Toast.makeText(getApplicationContext(), "У вас есть сохраненные данные", Toast.LENGTH_SHORT).show();
            isInDb = true;
            Toast.makeText(getApplicationContext(), concatStr(), Toast.LENGTH_LONG).show();
            // спросить про перезапись
            quest.setVisibility(View.VISIBLE);
            y.setVisibility(View.VISIBLE);
            n.setVisibility(View.VISIBLE);
        }
}

    public void onRadioButtonClicked(View view) {
        // если переключатель отмечен
        boolean checked = ((RadioButton) view).isChecked();
        // Получаем нажатый переключатель
        switch(view.getId()) {
            case R.id.radioButton3:
                if (checked){
                    //год
                    user_period = 12;
                }
                break;
            case R.id.radioButton4:
                if (checked){
                    // месяц
                    user_period = 1;
                }
                break;
        }
    }
    // после заполнения анкеты
    public void next(View view){
        if (!isInDb){
            // проверка на заполненность
            if (user_period!=0 && !money_user.getText().toString().equals("")) {
                //запись в SharedPreferences
                SharedPreferences.Editor ed = sPref.edit();
                ed.putInt("PLAN", user_period);
                ed.putInt("ZP", Integer.parseInt(money_user.getText().toString()));
                ed.apply();
                Intent intent = new Intent(Form.this, MainMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(), "Проверьте корректность заполнения", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Intent intent = new Intent(Form.this, MainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
    // обновление данных
    public void yes(View view){
        // проверка на заполненность
        if (user_period!=0 && !money_user.getText().toString().equals("")) {
            // запись в бд
            SharedPreferences.Editor ed = sPref.edit();
            ed.putInt("PLAN", user_period);
            ed.putInt("ZP", Integer.parseInt(money_user.getText().toString()));
            ed.apply();
            Toast.makeText(getApplicationContext(), "Данные обновлены", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Form.this, MainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "Проверьте корректность заполнения", Toast.LENGTH_SHORT).show();
        }
    }
    public void no(View view){
        Intent intent = new Intent(Form.this, MainMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public String concatStr(){
        return sPref.getString("EMAIL","") +" "+ sPref.getInt("ZP",0) + " руб. " + sPref.getInt("PLAN",1) + "/мес.";
    }
}
