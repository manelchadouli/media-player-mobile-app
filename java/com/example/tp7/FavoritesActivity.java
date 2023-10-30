package com.example.tp7;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView noFavoritesTextView;
    ArrayList<AudioModel> favoritesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.recycler_view_favorites);
        noFavoritesTextView = findViewById(R.id.text_view_no_favorites);

        // Retrieve the favorites from the database using the DatabaseHelper
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getFavorites();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") AudioModel favoriteSong = new AudioModel(
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PATH)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DURATION))
            );
            favoritesList.add(favoriteSong);
        }
        cursor.close();

        if (favoritesList.size() == 0) {
            noFavoritesTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MusicListAdapter(favoritesList, getApplicationContext()));
        }
    }
}

