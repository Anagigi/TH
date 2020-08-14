package com.ana.th;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Inicio extends AppCompatActivity {

    //Esta constante utiliza el nombre de la clase en sí como etiqueta
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        //TODO intent en la actividad que queremos abrir al clicar en cada boton
        // Intent intent = getIntent();
    }

    public void SincronizarDatos(View view) {
        Log.d(LOG_TAG, "Sincronizando datos...");
        Intent intent = new Intent(this, Inicio.class);
        //TODO sustituir por la clase que se encarga de sincronizar datos
        startActivity(intent);

    }

    public void MostrarSensores(View view) {
        Log.d(LOG_TAG, "Mostrando sensores");
        Intent intent = new Intent(this, Sensores.class);
        startActivity(intent);
    }


}