package com.ana.th;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class InicioActivity extends AppCompatActivity {

    //Esta constante utiliza el nombre de la clase en sí como etiqueta
    private static final String LOG_TAG = MasterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
    }

    public void SincronizarDatos(View view) {
        Log.i(LOG_TAG, "Abrir Sincronizar datos...");
        Intent intent = new Intent(this, BluetoothActivity.class);
        //TODO sustituir por la clase que se encarga de sincronizar datos
        startActivity(intent);
    }

    public void MostrarSensores(View view) {
        Log.i(LOG_TAG, "Abrir Lista de Sensores");
        Intent intent = new Intent(this, ListaSensoresActivity.class);
        startActivity(intent);
    }
}