package com.example.controlmoneys;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PersonSpending extends AppCompatActivity {
    SpendHelper spendHelper;
    TextView spend;public String name;
    EditText cat_spend,sum_spend;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_spending);
        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        setTitle(name);
        spend =findViewById(R.id.textViewSpendings);
        spendHelper = new SpendHelper(this);
        cat_spend = findViewById(R.id.editTextTextMultiLineCat);
        sum_spend = findViewById(R.id.editTextNumberDecimalSum);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();

    }

    private void updateData(){
        SQLiteDatabase database = spendHelper.getWritableDatabase();
        //database.delete(SpendHelper.TABLE_CONTACTS, null,null);


        Cursor cursor = database.query(SpendHelper.TABLE_CONTACTS,null, null,null,null,null,null);
        int nameIndex = cursor.getColumnIndex(SpendHelper.KEY_NAME);
        int catIndex = cursor.getColumnIndex(SpendHelper.KEY_CATEGORY);
        int sumIndex = cursor.getColumnIndex(SpendHelper.KEY_SUM);
        String text = "";
        int sum=0; int i =1;
        if (cursor.moveToFirst()){
            do{
                if(cursor.getString(nameIndex).equals(name)){
                    text += Integer.toString(i) +")" +cursor.getString(catIndex) + ":"  +cursor.getInt(sumIndex)+"\n";
                    i++;
                    sum+=cursor.getInt(sumIndex);
                }

            }while(cursor.moveToNext());
            spend.setText(text+"\n"+"Итог: "+sum);}
        else{
            Toast.makeText(getApplicationContext(), "Здесь пока нет записей", Toast.LENGTH_SHORT).show();
            spend.setText("");}
        cursor.close();
    }
    public void onClick(View view) {
        SQLiteDatabase database = spendHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        switch (view.getId()) {
            case R.id.textViewAddSpend:
                if(!cat_spend.getText().toString().equals("") && !sum_spend.getText().toString().equals("")) {
                    contentValues.put(SpendHelper.KEY_NAME, name);
                    contentValues.put(SpendHelper.KEY_CATEGORY, cat_spend.getText().toString());
                    contentValues.put(SpendHelper.KEY_SUM, Integer.parseInt(sum_spend.getText().toString()));
                    database.insert(SpendHelper.TABLE_CONTACTS, null, contentValues);
                    Toast.makeText(getApplicationContext(), "Данные записаны для "+name, Toast.LENGTH_SHORT).show();
                    cat_spend.setText("");
                    sum_spend.setText("");
                    // обновить данные в поле
                    updateData();

                }
                else {
                    Toast.makeText(getApplicationContext(), "Проверьте корректность заполнения", Toast.LENGTH_SHORT).show();
              }
                break;
            case R.id.textViewSubSpend:
                if(!cat_spend.getText().toString().equals(""))
                {
                database.delete(SpendHelper.TABLE_CONTACTS, SpendHelper.KEY_NAME + " = ? AND "+ SpendHelper.KEY_CATEGORY +" = ?",new String[]{name, cat_spend.getText().toString()});
                Toast.makeText(getApplicationContext(), "Удалены данные для "+ name, Toast.LENGTH_SHORT).show();
                // обновить данные в поле
                updateData();
                }
                else{
                Toast.makeText(getApplicationContext(), "Проверьте корректность заполнения", Toast.LENGTH_SHORT).show();
            }
                break;
        }
    }
}
