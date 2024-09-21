package com.example.pomodoro;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class PomodoroActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pomodoro_pucp);


        //Lógica para el logout con el top app bar, esto lo había hecho en mi proyecto así que lo reutilice
        topAppBar = findViewById(R.id.appbar);
        setSupportActionBar(topAppBar);

        topAppBar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(PomodoroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
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
            Intent intent = new Intent(PomodoroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
