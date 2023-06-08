package com.example.controlmoneys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class Family extends AppCompatActivity {
    ArrayList<String> familyArray;
    EditText editText;
    ArrayAdapter<String> adapter;
    SharedPreferences sPref; public String prefName = "",curName="UserData" ;

    public Family() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_lay);
        setTitle("Ежемесячные траты");

        sPref = getSharedPreferences(curName, MODE_PRIVATE);
        prefName = sPref.getString("EMAIL","");

        // получаем экземпляр элемента ListView edittext
        ListView listView = findViewById(R.id.FamilyList);
        editText = findViewById(R.id.editTextName);

        // Создаём пустой массив для хранения имен котов
        familyArray = new ArrayList<>();

        //получить список
        sPref = getSharedPreferences(prefName, MODE_PRIVATE);
        String s = sPref.getString("FAMILY","");

        if(!s.equals("")){
                familyArray = getArrayFromString(s);
        }

        // Создаём адаптер ArrayAdapter, чтобы привязать массив к ListView
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, familyArray);
        // Привяжем массив через адаптер к ListView
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                Intent intent = new Intent(Family.this, PersonSpending.class);
                intent.putExtra("name", ((TextView) itemClicked).getText());
                startActivity(intent);
            }
        });
    }
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.textViewAdd:
                if(editText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Введите имя",Toast.LENGTH_SHORT).show();
                }
                else{
                    familyArray.add(0, editText.getText().toString());
                    String s = sPref.getString("FAMILY","");
                    if(!s.equals("")) s+=";"+editText.getText().toString();
                    else s=editText.getText().toString();
                    SharedPreferences.Editor ed = sPref.edit();
                    //s="";
                    ed.putString("FAMILY", s);
                    ed.apply();
                    adapter.notifyDataSetChanged();
                    editText.setText("");
                }
                break;
            case R.id.textViewSub:
                if(editText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Введите имя",Toast.LENGTH_SHORT).show();
                }
                else{
                    familyArray.remove(editText.getText().toString());
                    String s=String.join(";",familyArray);
                    //s="";
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString("FAMILY", s);
                    ed.apply();
                    adapter.notifyDataSetChanged();
                    editText.setText("");
                }
                break;
        }
    }
    public ArrayList<String> getArrayFromString(String s) {
        ArrayList<String> arrayList = new ArrayList<>();
        String[] words = s.split(";");
        for (String word : words) {
            arrayList.add(word);
        }
        return arrayList;
    }

}
