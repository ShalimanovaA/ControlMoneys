package com.example.controlmoneys;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;

public class MainMenu extends AppCompatActivity {
    TextView email, accum_text, his_text;
    EditText accum_num;
    RelativeLayout accum, hist;
    SharedPreferences sPref; public String prefName = "UserData";
    public String user_email; DBHelper dbHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        email = findViewById(R.id.textViewUser);
        sPref = getSharedPreferences(prefName, MODE_PRIVATE);
        email.setText(sPref.getString("EMAIL",""));

        accum=findViewById(R.id.RelAccum);
        hist = findViewById(R.id.RelHis);
        accum.setVisibility(View.GONE);
        hist.setVisibility(View.GONE);

        accum_text=findViewById(R.id.textViewAc2);
        accum_num=findViewById(R.id.editTextNumberDecimalAc);
        his_text=findViewById(R.id.textViewHis1);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_settings :
                Intent intent = new Intent(MainMenu.this, Form.class);
                intent.putExtra("email", user_email);
                startActivity(intent);
                return true;
            case R.id.log_out:
                Intent intent_out = new Intent(MainMenu.this, Reg_Auth.class);
                intent_out.putExtra("log_out", "log_out");
                startActivity(intent_out);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView1:
                accum.setVisibility(View.GONE);
                hist.setVisibility(View.GONE);
                Intent intent1 = new Intent(MainMenu.this,Account.class);
                startActivity(intent1);
                break;
            case R.id.textView2:
                Intent intent2 = new Intent(MainMenu.this,  Plan.class);
                startActivity(intent2);
                break;
            case R.id.textView3:
                accum.setVisibility(View.GONE);
                hist.setVisibility(View.GONE);
                Intent intent3 = new Intent(MainMenu.this,  Family.class);
                startActivity(intent3);
                break;
            case R.id.textView4:
                accum.setVisibility(View.GONE);
                hist.setVisibility(View.GONE);
                Intent intent4 = new Intent(MainMenu.this, Calender.class);
                startActivity(intent4);
                break;
            case R.id.textView5:
                hist.setVisibility(View.GONE);
                accum.setVisibility(View.VISIBLE);
                accum_text.setText("Накопления: " + sPref.getInt("ACCUMULATION",0));
                break;
            case R.id.textView6:
                accum.setVisibility(View.GONE);
                hist.setVisibility(View.VISIBLE);
                dbHelper = new DBHelper(this);
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor =  database.query(DBHelper.TABLE_CONTACTS,null, null,null,null,null,DBHelper.KEY_DATA);
                int dataIndex = cursor.getColumnIndex(DBHelper.KEY_DATA);
                int catIndex = cursor.getColumnIndex(DBHelper.KEY_CATEGORY);
                int kindIndex = cursor.getColumnIndex(DBHelper.KEY_KIND);
                int sumIndex = cursor.getColumnIndex(DBHelper.KEY_SUM);
                String text = "";
                int sum_day=0; int i = 1;
                if (cursor.moveToFirst()){
                    do{
                            text += Integer.toString(i)+")" + cursor.getString(dataIndex) + cursor.getString(kindIndex) +
                                    ":"+cursor.getString(catIndex) + " на " +cursor.getInt(sumIndex)+"\n";
                            i++;
                            if(cursor.getString(kindIndex).equals("Покупка")){
                                sum_day-=cursor.getInt(sumIndex);
                            }
                            else{
                                sum_day+=cursor.getInt(sumIndex);
                            }
                    }while(cursor.moveToNext());
                    his_text.setText(text+"\n"+"Итог: "+sum_day);}
                else{
                    Toast.makeText(getApplicationContext(), "Здесь пока нет записей", Toast.LENGTH_SHORT).show();
                    his_text.setText("");}
                cursor.close();
                break;
            case R.id.textView7:
                Intent intent7 = new Intent(MainMenu.this, Sms.class);
                startActivity(intent7);
                break;
            case R.id.textViewAc3:
                if(accum_num.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Введите сумму",Toast.LENGTH_SHORT).show();
                }
                else{

                    int c = sPref.getInt("ACCT",0) - Integer.parseInt(accum_num.getText().toString());
                    if(c<0){
                        Toast.makeText(getApplicationContext(),"Недостаточно средств",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        int a = sPref.getInt("ACCUMULATION",0) + Integer.parseInt(accum_num.getText().toString());
                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putInt("ACCUMULATION", a);
                        ed.putInt("ACCT", c);
                        ed.apply();
                        accum_num.setText("");
                        accum_text.setText("Накопления: " + sPref.getInt("ACCUMULATION",0));
                        Toast.makeText(getApplicationContext(),"Сумма изменена",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.textViewAc4:
                if(accum_num.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Введите сумму",Toast.LENGTH_SHORT).show();
                }
                else{

                    int a = sPref.getInt("ACCUMULATION",0) - Integer.parseInt(accum_num.getText().toString());
                    if(a<0){
                        Toast.makeText(getApplicationContext(),"Недостаточно средств",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        int c = sPref.getInt("ACCT",0) + Integer.parseInt(accum_num.getText().toString());
                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putInt("ACCUMULATION", a);
                        ed.putInt("ACCT", c);
                        ed.apply();
                        accum_num.setText("");
                        accum_text.setText("Накопления: " + sPref.getInt("ACCUMULATION",0));
                        Toast.makeText(getApplicationContext(),"Сумма изменена",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.textViewAc5:
                accum.setVisibility(View.GONE);
                break;
            case R.id.textViewHis2:
                hist.setVisibility(View.GONE);
                break;
        }
    }
}
