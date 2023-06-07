package com.example.controlmoneys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChangeAccountData extends AppCompatActivity {
    public EditText name_ch,acct_ch,accumulation_ch;
    SharedPreferences sPref; public String prefName = "", curName="UserData";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_account_data);

        sPref = getSharedPreferences(curName, MODE_PRIVATE);
        prefName = sPref.getString("EMAIL","");

        name_ch = findViewById(R.id.editTextName);
        acct_ch = findViewById(R.id.editTextNumberDecimal);
        accumulation_ch = findViewById(R.id.editTextNumberDecimal2);
        // получаем объект для считывания и записи данных, настройка доступа только этому приложению
        sPref = getSharedPreferences(prefName, MODE_PRIVATE);
        if(!sPref.getString("NAME","").equals("")){
            name_ch.setText(sPref.getString("NAME",""));
            acct_ch.setText(Integer.toString(sPref.getInt("ACCT",0)));
            accumulation_ch.setText(Integer.toString(sPref.getInt("ACCUMULATION",0)));
        }

    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView9:
                if(name_ch.getText().toString().equals("") || acct_ch.getText().toString().equals("") || accumulation_ch.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Проверьте корректность введенных данных",Toast.LENGTH_SHORT).show();
                }
                else{
                    saveText();
                    name_ch.setText(""); acct_ch.setText("");accumulation_ch.setText("");
                    Toast.makeText(getApplicationContext(),"Данные сохранены",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void saveText(){
        // объект editot
        SharedPreferences.Editor ed = sPref.edit();
        //записываем данные
        ed.putString("NAME", name_ch.getText().toString());
        ed.putInt("ACCT", Integer.parseInt(acct_ch.getText().toString()));
        ed.putInt("ACCUMULATION", Integer.parseInt(accumulation_ch.getText().toString()));
        ed.apply();
    }
}
