package com.example.pomodoro;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pomodoro.databinding.ActivityMainBinding;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    DummyService dummyService;
    private TextInputEditText username;
    private TextInputEditText password;
    Button bt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Se busca el usuario y contraseña ingresados
        username = findViewById(R.id.edit_user);
        password = findViewById(R.id.edit_password);

        // Utilizando Retrofit
        dummyService = new Retrofit.Builder()
                .baseUrl("https://dummyjson.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DummyService.class);

        //binding.bt_login.setOnClickListener(view -> irPomodoro()); La verdad no sé porque no me funciono el binding xd

        bt_login = findViewById(R.id.bt_login);

        //Se debe validar que siempre tenga conexión a internet para que pueda hacer consultas
        //if(haveInternet()){ //Solo funciona si le quito el have internet, pipipi es raro
        //No me jale profe :c
            bt_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dummyService.LOGIN_CALL(username.getText().toString(),password.getText().toString()).enqueue(new Callback<UserLogin>() {
                        @Override
                        public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                            if (response.isSuccessful()){
                                UserLogin userPom = response.body();
                                Intent intent = new Intent(MainActivity.this, PomodoroActivity.class);
                                Log.d("usuarioUsername",userPom.getUsername());
                                intent.putExtra("user",userPom);
                                startActivity(intent);
                            }else{
                                Toast.makeText(MainActivity.this,"Authentication problem",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserLogin> call, Throwable trowable) {
                            trowable.printStackTrace();
                        }
                    });
                }
            });
        //}

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


}