package com.example.pomodoro;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PomodoroTareasActivity extends AppCompatActivity {

    DummyService dummyService;
    private String msg;
    private MaterialToolbar topAppBar;
    private UserLogin user;
    private List<Tarea> listaTareas;
    private TextView tTareas;
    private Button btCambiarEstado;
    Tarea cTarea;

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

        Intent intent= getIntent();

        listaTareas = (List<Tarea>) intent.getSerializableExtra("list");
        user = (UserLogin) intent.getSerializableExtra("user");
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        btCambiarEstado = (Button) findViewById(R.id.bt_cambiar_estado);
        tTareas = findViewById(R.id.t_tareas);
        tTareas.setText("Ver tareas de " + user.getFirstName());
        String [] SpinnerItems = new String[listaTareas.size()];


        for(Tarea t : listaTareas){
            SpinnerItems[listaTareas.indexOf(t)] = t.getTodo() + "-" + (t.isCompleted()?"Completado":"No completado");
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.text_spinner, SpinnerItems);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btCambiarEstado.setOnClickListener(view -> {
            cTarea = listaTareas.get(spinner.getSelectedItemPosition());
            cTarea.setCompleted(!cTarea.isCompleted());

            cambiarEstadoTarea(cTarea.getId(),!cTarea.isCompleted());

        });

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



    private void cambiarEstadoTarea(int tareaId, boolean nuevoEstado) {
        DummyService dummyService = new Retrofit.Builder()
                .baseUrl("https://dummyjson.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DummyService.class);

        dummyService.CHANGE_STATE(tareaId, nuevoEstado).enqueue(new Callback<Tarea>() {
            @Override
            public void onResponse(Call<Tarea> call, Response<Tarea> response) {
                String msg;
                if (response.isSuccessful() && response.body() != null) {
                    Tarea tarea = response.body();
                    msg = "Tarea " + tarea.getTodo() + " " + (nuevoEstado ? "'Completada'" : "'No completada'");
                } else {
                    msg = "No se logró cambiar el estado de la tarea";
                }

                Intent intent1 = new Intent();
                intent1.putExtra("msg", msg);
                setResult(RESULT_OK, intent1);
            }

            @Override
            public void onFailure(Call<Tarea> call, Throwable throwable) {

            }
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