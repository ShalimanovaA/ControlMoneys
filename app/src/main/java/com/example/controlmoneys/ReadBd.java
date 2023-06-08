package com.example.controlmoneys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;

import java.util.ArrayList;
import java.util.List;

public class ReadBd extends AppCompatActivity {
    private ListView listView;
    private String USER_KEY = "ADMIN";
    private ArrayAdapter<String> adapter;
    private List<String> data;
    private DatabaseReference mDataBase;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_from_bd);
        init();
    }
    private void init(){
        listView = findViewById(R.id.list);
        data = new ArrayList<>();
        data.add("admin@admin.ru");data.add("helpdesk@mail.ru");data.add("server@mail.ru");

        // Создаём адаптер ArrayAdapter, чтобы привязать массив к ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        // устанавливаем для списка адаптер
        listView.setAdapter(adapter);
        setTitle("Техническая поддержка");
    }
}
