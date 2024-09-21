package com.example.pomodoro;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class PomodoroTareasActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pomodoro_tareas);

        //Lógica para el logout con el top app bar, esto lo había hecho en mi proyecto así que lo reutilice
        topAppBar = findViewById(R.id.appbar);
        setSupportActionBar(topAppBar);

        topAppBar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(PomodoroTareasActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        Intent intentTimer= getIntent();


        /*
        topAppBar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(PomodoroTareasActivity.this, PomodoroActivity.class);
            startActivity(intent);
        });

         */

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        topAppBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            Intent intent = new Intent(PomodoroTareasActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}