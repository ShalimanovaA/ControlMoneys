package com.example.controlmoneys;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sms extends AppCompatActivity {
    private static final int REQUEST_CODE_READ_CONTACTS=1;
    private static boolean READ_CONTACTS_GRANTED =false;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_lay);
        setTitle("Сообщения");
        txt = findViewById(R.id.header);
        // получаем разрешения
        int hasReadContactPermission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_SMS);
        // если устройство до API 23, устанавливаем разрешение
        // вызываем диалоговое окно для установки разрешений
        ActivityCompat.requestPermissions(this, new
                        String[]{Manifest.permission.READ_SMS},
                REQUEST_CODE_READ_CONTACTS);
        // если разрешение установлено, загружаем контакты
        if (READ_CONTACTS_GRANTED){
            loadContacts();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                READ_CONTACTS_GRANTED = true;
            }
        }
        if(READ_CONTACTS_GRANTED){
            loadContacts();
        }
        else{
            Toast.makeText(this, "Требуется установить разрешения",
                    Toast.LENGTH_LONG).show();
        }
    }
    private void loadContacts(){
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor =
                contentResolver.query(Telephony.Sms.Inbox.CONTENT_URI, null, null,
                        null, null);
        ArrayList<String> contacts = new ArrayList<String>();
        int sum = 0;
        if(cursor!=null){
            while (cursor.moveToNext()) {
                String contact = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
                Pattern pattern = Pattern.compile("Оплата\\s\\d+");
                Matcher matcher = pattern.matcher(contact);
                if (matcher.find()) {
                    contacts.add("- "+contact);
                    sum-=Integer.parseInt(contact.substring(matcher.start()+7, matcher.end()));
                }
                pattern = Pattern.compile("Покупка\\s\\d+");
                matcher = pattern.matcher(contact);
                if (matcher.find()) {
                    contacts.add("- "+contact);
                    sum-=Integer.parseInt(contact.substring(matcher.start()+8, matcher.end()));
                }
                pattern = Pattern.compile("Платёж\\s\\d+");
                matcher = pattern.matcher(contact);
                if (matcher.find()) {
                    contacts.add("- "+contact);
                    sum-=Integer.parseInt(contact.substring(matcher.start()+7, matcher.end()));
                }
                pattern = Pattern.compile("Перевод\\s\\d+");
                matcher = pattern.matcher(contact);
                if (matcher.find()) {
                    contacts.add("+ "+contact);
                    sum+=Integer.parseInt(contact.substring(matcher.start()+8, matcher.end()));
                }
                pattern = Pattern.compile("перевод\\s\\d+");
                matcher = pattern.matcher(contact);
                if (matcher.find()) {
                    contacts.add("- "+contact);
                    sum-=Integer.parseInt(contact.substring(matcher.start()+8, matcher.end()));
                }
            }
            cursor.close();
            txt.setText("Общая сумма: "+sum);
        }
        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, contacts);
        ListView contactList = findViewById(R.id.contactList);
        // устанавливаем для списка адаптер
        contactList.setAdapter(adapter);
    }
}
