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

import java.util.HashMap;

public class Form extends AppCompatActivity {
    private String USER_KEY = "User";
    public String EMAIL, ID;
    public int user_period=1;
    public boolean isInDb = false;
    public EditText money_user;
    TextView quest,y,n;
    private DatabaseReference mDataBase;
    SharedPreferences sPref; public String prefName = "UserData";



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Аккаунт");
        // анкета при регистрации
        setContentView(R.layout.form);
        //скрыть элемента
        quest = findViewById(R.id.textView8);
        quest.setVisibility(View.GONE);
        y = findViewById(R.id.textView9);
        y.setVisibility(View.GONE);
        n = findViewById(R.id.textView10);
        n.setVisibility(View.GONE);
        // получение почты

        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        getDataFromDb();

        money_user = findViewById(R.id.editTextNumberDecimal);

        sPref = getSharedPreferences(prefName, MODE_PRIVATE);
        EMAIL=sPref.getString("EMAIL","");

    }



    private void getDataFromDb() {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // достаем из snapshot все данные
                for (DataSnapshot ds : snapshot.getChildren()) {
                    UserAccount user = ds.getValue(UserAccount.class);
                    if (user != null){
                        if (user.email.equals(EMAIL)) {
                            Toast.makeText(getApplicationContext(), "У вас есть сохраненные данные", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor ed = sPref.edit();
                            ed.putInt("PLAN", user.period);
                            ed.putInt("ZP", user.money);
                            ed.apply();
                            isInDb = true;
                            ID = ds.getKey();
                            String text = user.email +" "+ user.money + " руб. " + user.period + "/мес.";
                            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                            // спросить про перезапись
                            quest.setVisibility(View.VISIBLE);
                            y.setVisibility(View.VISIBLE);
                            n.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        };
        mDataBase.addValueEventListener(vListener);
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
                // запись в бд
                UserAccount new_user = new UserAccount(mDataBase.getKey(), EMAIL, user_period, Integer.parseInt(money_user.getText().toString()));
                mDataBase.push().setValue(new_user);

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
        HashMap map = new HashMap();
        // проверка на заполненность
        if (user_period!=0 && money_user.getText().toString() != null) {
            // запись в бд
//            SharedPreferences.Editor ed = sPref.edit();
//            ed.putInt("PLAN", user_period);
//            ed.putInt("ZP", Integer.parseInt(money_user.getText().toString()));
//            ed.apply();
            map.put("money", Integer.parseInt(money_user.getText().toString()));
            map.put("period", user_period);
            try{
                if(mDataBase.child(ID)!=null){
                    mDataBase.child(ID).updateChildren(map);
                    Toast.makeText(getApplicationContext(), "Данные обновлены", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Данные не бновлены", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
}
