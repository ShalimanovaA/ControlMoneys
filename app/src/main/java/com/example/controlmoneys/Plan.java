package com.example.controlmoneys;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;

public class Plan extends AppCompatActivity {
    TextView txt;
    String text;
    SharedPreferences sPref; public String prefName = "UserData";
    SpendHelper spendHelper; DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan);
        setTitle("План");

        txt = findViewById(R.id.textView1);
        LocalDate currentDate;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
            LocalDate last = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
            text = "";

            sPref = getSharedPreferences(prefName, MODE_PRIVATE);
            loadData(currentDate, last);
        }
        plan();
    }
    private void plan(){

            LocalDate currentDate;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                currentDate = LocalDate.now();
                int curMonth = currentDate.getMonth().getValue();
                if(sPref.getInt("PLAN", 0)==1) {

                    if (curMonth == 12) {
                        int curYear = currentDate.getYear() +1;
                        LocalDate first = currentDate.withMonth(0).withDayOfMonth(1).withYear(curYear);
                        LocalDate last = currentDate.withMonth(0).withDayOfMonth(currentDate.lengthOfMonth()).withYear(curYear);
                        loadData(first, last);
                    } else {
                        LocalDate first = currentDate.withMonth(curMonth + 1).withDayOfMonth(1);
                        LocalDate last = currentDate.withMonth(curMonth + 1).withDayOfMonth(currentDate.lengthOfMonth());
                        loadData(first, last);
                    }
                }
                else{
                    int monthBefore = 12-curMonth;
                    for(int i=curMonth+1;i<=12;i++){
                        LocalDate first = currentDate.withMonth(i).withDayOfMonth(1);
                        LocalDate last = first.withDayOfMonth(first.lengthOfMonth());
                        loadData(first,last);
                    }
                    for(int i=1;i<monthBefore;i++){
                        int curYear = currentDate.getYear()+1;
                        LocalDate first = currentDate.withMonth(i).withDayOfMonth(1).withYear(curYear);
                        LocalDate last = first.withDayOfMonth(first.lengthOfMonth()).withYear(curYear);
                        loadData(first,last);
                    }
                }
            }
    }
    private void loadData(LocalDate first,LocalDate last){
        // сколько дней в периоде
        int count_days = Integer.parseInt(first.toString().split("-")[2]) +
                Integer.parseInt(last.toString().split("-")[2]);
        if(count_days==0)count_days=1;

        //объявление для SharedPreferences для зарплаты и накоплений
        sPref = getSharedPreferences(prefName, MODE_PRIVATE);
        //рассматриваемый период
        text += first + " - " + last+ "\n";

        //подсчеты
        text += "Зарплата:" + sPref.getInt("ZP", 0) + "\n";
//        text += "Накопления:" + sPref.getInt("ACCUMULATION", 0) + "\n";
        // подсчёт ежемесячных трат
        spendHelper = new SpendHelper(this);
        int sum = 0;
        SQLiteDatabase database = spendHelper.getWritableDatabase();
        Cursor cursor = database.query(SpendHelper.TABLE_CONTACTS, null, null, null, null, null, null);
        int sumIndex = cursor.getColumnIndex(SpendHelper.KEY_SUM);
        int nameIndex = cursor.getColumnIndex(SpendHelper.KEY_NAME);
        if (cursor.moveToFirst()) {
            do {
                sum += cursor.getInt(sumIndex);
            }
            while (cursor.moveToNext());
        }
            cursor.close();
            //вывод
            text += "На день с учётом ежемесячных расходов:" + (sPref.getInt("ACCT", 0) + sPref.getInt("ZP", 0) - sum) / count_days + "\n";




            //считывание трат из календаря
            dbHelper = new DBHelper(this);
            int sum1 = 0;
            database = dbHelper.getWritableDatabase();
            cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
            sumIndex = cursor.getColumnIndex(DBHelper.KEY_SUM);
            int kindIndex = cursor.getColumnIndex(DBHelper.KEY_KIND);
            int dataIndex = cursor.getColumnIndex(DBHelper.KEY_DATA);
            if (cursor.moveToFirst()) {
                do {
                    if (checkMonth(first, cursor.getString(dataIndex))) {
                        if (cursor.getString(kindIndex).equals("Покупка"))
                            sum1 += cursor.getInt(sumIndex);
                        else sum1 -= cursor.getInt(sumIndex);
                    }
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            //вывод
            text += "На день с учётом ежемесячных расходов с тратами из календаря:" + (sPref.getInt("ACCT", 0) + sPref.getInt("ZP", 0) - sum-sum1) / count_days + "\n";
            text += "Счёт с учётом ежемесячных расходов с тратами из календаря и зарплатой:" + (sPref.getInt("ACCT", 0) + sPref.getInt("ZP", 0) - sum-sum1) + "\n";
            text+="\n";
            txt.setText(text);
    }
    private boolean checkMonth(LocalDate data, String s){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return data.getMonth().getValue() == Integer.parseInt(s.split("-")[1]);
        }
        return false;
    }
}
