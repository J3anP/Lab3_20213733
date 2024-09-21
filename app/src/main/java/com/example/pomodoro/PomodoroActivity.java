package com.example.pomodoro;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.pomodoro.databinding.ActivityMainBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PomodoroActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    DummyService dummyService;
    private UserLogin userPom;
    private TextView name;
    private TextView email;
    private ImageView icUser;
    private ActivityMainBinding binding;
    private MaterialButton btPlay;
    private MaterialButton btRestart;
    private TextView timer;
    private TextView rest;
    private ActivityResultLauncher<Intent> launcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pomodoro_pucp);

        /*
        binding = ActivityMainBinding.inflate(this.getLayoutInflater());
        setContentView(binding.getRoot());

         */

        //Lógica para el logout con el top app bar, esto lo había hecho en mi proyecto así que lo reutilice
        topAppBar = findViewById(R.id.appbar);
        setSupportActionBar(topAppBar);

        topAppBar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(PomodoroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        //Manejo de retrofit
        DummyService dummyService = new Retrofit.Builder()
                .baseUrl("https://dummyjson.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DummyService.class);

        Intent intent = getIntent();
        userPom = (UserLogin) intent.getSerializableExtra("user");

        name = findViewById(R.id.t_username);
        email = findViewById(R.id.t_email);
        icUser = findViewById(R.id.ic_user);
        timer = findViewById(R.id.t_timer);
        rest = findViewById(R.id.t_rest);
        btPlay = findViewById(R.id.bt_start);
        btRestart = findViewById(R.id.bt_restart);

        //Mostrar sus datos y el icono en el card
        cardUserData(name,email,icUser,userPom);


        btPlay.setVisibility(View.VISIBLE);
        btRestart.setVisibility(View.GONE);

        //Manejo del timer
        btPlay.setOnClickListener(view -> {
            btPlay.setVisibility(View.GONE);
            btRestart.setVisibility(View.VISIBLE);

            Data dataBuilder = new Data.Builder()
                    .putInt("minutes", 25)
                    .build();

            WorkRequest workRequest = new OneTimeWorkRequest.Builder(TimerWorker.class)
                    .setInputData(dataBuilder)
                    .build();

            WorkManager
                    .getInstance(this)
                    .enqueue(workRequest);

            WorkManager.getInstance(this)
                    .getWorkInfoByIdLiveData(workRequest.getId())
                    .observe(PomodoroActivity.this, workInfo -> {
                        if (workInfo != null) {
                            //Manejo del countdown
                            if (workInfo.getState() == WorkInfo.State.RUNNING) {
                                Data data = workInfo.getProgress();

                                int minRemaining = data.getInt("minRemaining", 0);
                                int secRemaining= data.getInt("secRemaining", 0); // Obtiene los segundos correctamente

                                String timeCorrect= correctorTimer(minRemaining, secRemaining);
                                timer.setText(timeCorrect);
                            }else
                                if(workInfo.getState()== WorkInfo.State.SUCCEEDED){
                                rest.setText("En descanso");
                                //rest.setTextColor("Color.parseColor(\"#FF00FF\")");

                                //Otro workmanager para el tiempo de descanso = 5 min
                                Data dataBuilder2 = new Data.Builder()
                                        .putInt("minutes", 5)
                                        .build();

                                WorkRequest workRequest2 = new OneTimeWorkRequest.Builder(TimerWorker.class)
                                        .setInputData(dataBuilder2)
                                        .build();

                                WorkManager
                                        .getInstance(this)
                                        .enqueue(workRequest2);

                                WorkManager.getInstance(this)
                                        .getWorkInfoByIdLiveData(workRequest2.getId())
                                        .observe(PomodoroActivity.this,workInfo2 -> {
                                            if(workInfo2!=null){
                                                manejarEstadoTrabajo(workInfo2);
                                            }

                                        });

                                //No sé porque se bugea horrible con el request de internet
                                //if(haveInternet()){
                                    getListaTareas(userPom);
                                //}
                            }
                        } else {
                            Log.d("msg-test", "work info == null ");
                        }
                    });
        });

        btRestart.setOnClickListener(view -> {
            btPlay.setVisibility(View.GONE);
            btRestart.setVisibility(View.VISIBLE);

            rest.setText("Descanso: 05:00");
            WorkManager.getInstance(this).cancelAllWork();

            Data dataBuilder = new Data.Builder()
                    .putInt("minutes", 25)
                    .build();

            WorkRequest workRequest = new OneTimeWorkRequest.Builder(TimerWorker.class)
                    .setInputData(dataBuilder)
                    .build();

            WorkManager
                    .getInstance(this)
                    .enqueue(workRequest);

            WorkManager.getInstance(this)
                    .getWorkInfoByIdLiveData(workRequest.getId())
                    .observe(PomodoroActivity.this, workInfo -> {
                        if (workInfo != null) {
                            //Manejo del countdown
                            if (workInfo.getState() == WorkInfo.State.RUNNING) {
                                Data data = workInfo.getProgress();

                                int minRemaining = data.getInt("minRemaining", 0);
                                int secRemaining= data.getInt("secRemaining", 0); // Obtiene los segundos correctamente

                                String timeCorrect= correctorTimer(minRemaining, secRemaining);
                                timer.setText(timeCorrect);
                            }else
                                if(workInfo.getState()== WorkInfo.State.SUCCEEDED){
                                rest.setText("En descanso");
                                //rest.setTextColor("Color.parseColor(\"#FF00FF\")");

                                //Otro workmanager para el tiempo de descanso = 5 min
                                Data dataBuilder2 = new Data.Builder()
                                        .putInt("minutes", 5)
                                        .build();

                                WorkRequest workRequest2 = new OneTimeWorkRequest.Builder(TimerWorker.class)
                                        .setInputData(dataBuilder2)
                                        .build();

                                WorkManager
                                        .getInstance(this)
                                        .enqueue(workRequest2);

                                WorkManager.getInstance(this)
                                        .getWorkInfoByIdLiveData(workRequest2.getId())
                                        .observe(PomodoroActivity.this,workInfo2 -> {
                                            if(workInfo2!=null){
                                                manejarEstadoTrabajo(workInfo2);
                                            }

                                        });

                                //No sé porque se bugea horrible con el request de internet
                                //if(haveInternet()){
                                getListaTareas(userPom);
                                //}
                            }
                        } else {
                            Log.d("msg-test", "work info == null ");
                        }
                    });
        });
    }

    private void getListaTareas(UserLogin u) {
        dummyService.LISTA_TAREAS(u.getId()).enqueue(new Callback<TareaStats>() {
            @Override
            public void onResponse(Call<TareaStats> call, Response<TareaStats> response) {
                if (response.isSuccessful() && response.body() != null) {
                    procesarRespuestaTareas(response.body(), u);
                } else {
                    mostrarError("Error al obtener la lista de tareas");
                }
            }

            @Override
            public void onFailure(Call<TareaStats> call, Throwable throwable) {
                mostrarError("Error de conexión: " + throwable.getMessage());
            }
        });
    }

    private void procesarRespuestaTareas(TareaStats tareaStats, UserLogin u) {
        if (tareaStats.getTodos().length == 0) {
            dialogoRest();
        } else {
            lanzarActividadTareas(u, tareaStats.getTodos());
        }
    }

    private void dialogoRest() {
        MaterialAlertDialogBuilder dialogStart = new MaterialAlertDialogBuilder(PomodoroActivity.this)
                .setTitle("¡Felicidades!")
                .setMessage("Empezó el tiempo de descanso!")
                .setPositiveButton("Entendido", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .setCancelable(false);

        Dialog dialog = dialogStart.create();
        aplicarAnimacionAlDialogo(dialog);
        dialog.show();
    }

    private void lanzarActividadTareas(UserLogin u, Tarea[] tareas) {
        Intent intent = new Intent(PomodoroActivity.this, PomodoroTareasActivity.class);
        intent.putExtra("list", tareas);
        intent.putExtra("user", u);
        launcher.launch(intent);
    }

    private void mostrarError(String mensaje) {
        Toast.makeText(PomodoroActivity.this, mensaje, Toast.LENGTH_SHORT).show();
    }

    //Maneje la lógica, pero había problemas de carga, así que decidí ponerlo en una AI para ver que me dice y me recomendo
    //separarlo
    private void manejarEstadoTrabajo(WorkInfo workInfo2) {
        if (workInfo2.getState() == WorkInfo.State.RUNNING) {
            actualizarTemporizador(workInfo2);
        } else if (workInfo2.getState() == WorkInfo.State.SUCCEEDED) {
            dialogFinRest();
        }
    }

    private void actualizarTemporizador(WorkInfo workInfo) {
        Data data = workInfo.getProgress();

        int minRemaining = data.getInt("minRemaining", 0);
        int secRemaining= data.getInt("secRemaining", 0); // Obtiene los segundos correctamente

        String timeCorrect= correctorTimer(minRemaining, secRemaining);
        timer.setText(timeCorrect);
    }

    private void dialogFinRest() {
        rest.setText("Fin del descanso");
        timer.setText("00:00");

        MaterialAlertDialogBuilder dialogFin = new MaterialAlertDialogBuilder(PomodoroActivity.this)
                .setTitle("Atención")
                .setMessage("Terminó el tiempo de descanso. Dale al botón de reinicio para empezar otro ciclo")
                .setPositiveButton("Entendido", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false);

        Dialog dialog = dialogFin.create();
        aplicarAnimacionAlDialogo(dialog);
        dialog.show();
    }

    private void aplicarAnimacionAlDialogo(Dialog dialog) {
        dialog.setOnShowListener(dialogInterface -> {
            Animation scaleAnimation = AnimationUtils.loadAnimation(PomodoroActivity.this, R.anim.celebration_scale);
            dialog.getWindow().getDecorView().startAnimation(scaleAnimation);
        });
    }
    /*---------------------------------------------*/

    public String correctorTimer(int min, int sec) {
        //Para que que siempre se muestre con dos dígitos en el textView
        //Sacado con AI
        String minStr = min < 10 ? "0" + min : String.valueOf(min);
        String secStr = sec < 10 ? "0" + sec : String.valueOf(sec);

        return minStr + ":" + secStr;
    }

    public void cardUserData(TextView name, TextView email, ImageView icUser, UserLogin userPom) {

        String copyName;
        if (userPom.getMaidenName() != null) {
            copyName = userPom.getFirstName() + " " + userPom.getLastName() + " " + userPom.getMaidenName();
        } else {
            copyName = userPom.getFirstName() + " " + userPom.getLastName();
        }
        name.setText(copyName);

        email.setText(userPom.getEmail());

        if (userPom.getGender().equals("female")) {
            icUser.setImageResource(R.drawable.baseline_woman_24);
        } else {
            icUser.setImageResource(R.drawable.baseline_man_24);
        }
    }

    public boolean haveInternet(){
        //Manejo de la conexión de internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean haveInternet = false;
        if(connectivityManager!=null){
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if(capabilities !=null){
                if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                    Log.i("msg-Internet","NetworkCapabilities.TRANSPORT_CELLULAR");
                    haveInternet= true;
                }
            } else if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                Log.i("msg-Internet","NetworkCapabilities.TRANSPORT_WIFI");
                haveInternet= true;
            }else if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                Log.i("msg-Internet","NetworkCapabilities.TRANSPORT_ETHERNET");
                haveInternet= true;
            }
        }
        return haveInternet;
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
