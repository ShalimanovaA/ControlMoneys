package com.example.controlmoneys;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;

public class Calender extends AppCompatActivity implements View.OnClickListener{
    public int year,month,day;
    public String CurData;
    public TextView by, get, end, see, db, del;
    public EditText category, sum;
    public DatePicker datePicker;
    DBHelper dbHelper;SharedPreferences sPref;
    public String prefName = "",curName="UserData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_lay);

        sPref = getSharedPreferences(curName, MODE_PRIVATE);
        prefName = sPref.getString("EMAIL","");
        sPref = getSharedPreferences(prefName, MODE_PRIVATE);

        by = findViewById(R.id.dateTextView2);
        get = findViewById(R.id.dateTextView3);
        end = findViewById(R.id.dateTextView4);
        see = findViewById(R.id.dateTextView5);
        db = findViewById(R.id.dateTextViewDB);
        del = findViewById(R.id.dateTextView6);

        db.setVisibility(View.GONE);

        datePicker = findViewById(R.id.datePicker);
        category = findViewById(R.id.editTextTextMultiLineCat);
        sum = findViewById(R.id.editTextNumberDecimalSum);

        DatePicker datePicker = this.findViewById(R.id.datePicker);
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
            String[] parts = currentDate.toString().split("-");
            year = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            day = Integer.parseInt(parts[2]);
            CurData = parts[2] +"-"+ parts[1]+"-"+parts[0];
            // Месяц начиная с нуля. Для отображения добавляем 1.
            datePicker.init(year, month - 1, day, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    CurData = Integer.toString(view.getDayOfMonth()) +"-"+ Integer.toString(view.getMonth()+1)+
                            "-"+Integer.toString(view.getYear()) ;
                    setTitle(CurData);
                }
            });
        }
        setTitle(CurData);
        // бд
        dbHelper = new DBHelper(this);
    }
    public void onClick(View view){

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS,null, null,null,null,null,null);
        int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
        int dataIndex = cursor.getColumnIndex(DBHelper.KEY_DATA);
        int catIndex = cursor.getColumnIndex(DBHelper.KEY_CATEGORY);
        int kindIndex = cursor.getColumnIndex(DBHelper.KEY_KIND);
        int sumIndex = cursor.getColumnIndex(DBHelper.KEY_SUM);
            switch (view.getId()){
                case R.id.dateTextView2:
                    if(!sum.getText().toString().equals("") && !category.getText().toString().equals("")) {
                        sPref = getSharedPreferences(prefName, MODE_PRIVATE);
                        contentValues.put(DBHelper.KEY_NAME,sPref.getString("EMAIL",""));
                        contentValues.put(DBHelper.KEY_DATA, CurData);
                        contentValues.put(DBHelper.KEY_CATEGORY, category.getText().toString());
                        contentValues.put(DBHelper.KEY_SUM, Integer.parseInt(sum.getText().toString()));
                        contentValues.put(DBHelper.KEY_KIND, "Покупка");
                        database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);

                        // объект editot
                        SharedPreferences.Editor ed = sPref.edit();
                        int n = sPref.getInt("ACCT",0)-Integer.parseInt(sum.getText().toString());
                        ed.putInt("ACCT", n);
                        ed.apply();
                        Toast.makeText(getApplicationContext(), "Данные записаны для "+CurData, Toast.LENGTH_SHORT).show();
                        category.setText("");
                        sum.setText("");
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Проверьте корректность заполнения", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.dateTextView3:
                    if(!sum.getText().toString().equals("") && !category.getText().toString().equals("")) {
                        contentValues.put(DBHelper.KEY_DATA, CurData);
                        contentValues.put(DBHelper.KEY_CATEGORY, category.getText().toString());
                        contentValues.put(DBHelper.KEY_SUM, Integer.parseInt(sum.getText().toString()));
                        contentValues.put(DBHelper.KEY_KIND, "Зарплата");
                        sPref = getSharedPreferences(prefName, MODE_PRIVATE);
                        contentValues.put(DBHelper.KEY_NAME,sPref.getString("EMAIL",""));
                        // объект editot
                        SharedPreferences.Editor ed = sPref.edit();
                        int n = sPref.getInt("ACCT",0)+Integer.parseInt(sum.getText().toString());
                        ed.putInt("ACCT", n);
                        ed.apply();
                        database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
                        Toast.makeText(getApplicationContext(), "Данные записаны для "+CurData, Toast.LENGTH_SHORT).show();
                        category.setText("");
                        sum.setText("");
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Проверьте корректность заполнения", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.dateTextView5:
                    cursor = database.query(DBHelper.TABLE_CONTACTS,null, null,null,null,null,null);
                    dataIndex = cursor.getColumnIndex(DBHelper.KEY_DATA);
                    catIndex = cursor.getColumnIndex(DBHelper.KEY_CATEGORY);
                    kindIndex = cursor.getColumnIndex(DBHelper.KEY_KIND);
                    sumIndex = cursor.getColumnIndex(DBHelper.KEY_SUM);
                    nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                    db.setVisibility(View.VISIBLE);
                    String text = "";
                    int sum_day=0; int i =1;
                    if (cursor.moveToFirst()){
                        do{
                            if(cursor.getString(dataIndex).equals(CurData) && cursor.getString(nameIndex).equals(sPref.getString("EMAIL",""))){
                                text += Integer.toString(i) +")" + cursor.getString(dataIndex) + cursor.getString(kindIndex) +
                                        ":"+cursor.getString(catIndex) + " на " +cursor.getInt(sumIndex)+"\n";
                                i++;
                            if(cursor.getString(kindIndex).equals("Покупка")){
                                sum_day-=cursor.getInt(sumIndex);
                            }
                            else{
                                sum_day+=cursor.getInt(sumIndex);
                            }
                            }

                        }while(cursor.moveToNext());
                        db.setText(text+"\n"+"Итог: "+sum_day);}
                    else{
                        Toast.makeText(getApplicationContext(), "Здесь пока нет записей", Toast.LENGTH_SHORT).show();
                        db.setText("");}
                    cursor.close();
                    break;
                case R.id.dateTextView6:
                    cursor = database.query(DBHelper.TABLE_CONTACTS,null, null,null,null,null,null);
                    dataIndex = cursor.getColumnIndex(DBHelper.KEY_DATA);
                    kindIndex = cursor.getColumnIndex(DBHelper.KEY_KIND);
                    sumIndex = cursor.getColumnIndex(DBHelper.KEY_SUM);
                    nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                    sum_day=0;
                    if (cursor.moveToFirst()){
                        do{
                            if(cursor.getString(dataIndex).equals(CurData) && cursor.getString(nameIndex).equals(sPref.getString("EMAIL",""))){
                                if(cursor.getString(kindIndex).equals("Покупка")){
                                    sum_day-=cursor.getInt(sumIndex);
                                }
                                else{
                                    sum_day+=cursor.getInt(sumIndex);
                                }
                            }

                        }while(cursor.moveToNext());
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Здесь пока нет записей", Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();

                    sPref = getSharedPreferences(prefName, MODE_PRIVATE);
                    // объект editot
                    SharedPreferences.Editor ed = sPref.edit();
                    int n = sPref.getInt("ACCT",0)-sum_day;
                    ed.putInt("ACCT", n);
                    ed.apply();
                    database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_DATA + " = ?",new String[]{CurData});
                    Toast.makeText(getApplicationContext(), "Удалены данные для "+ CurData, Toast.LENGTH_SHORT).show();
                    break;
            }
            dbHelper.close();
    }
    public void out(View view){
        Intent intent = new Intent(Calender.this, MainMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
