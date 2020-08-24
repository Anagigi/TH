package com.ana.th;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ana.th.bluetooth.BluetoothSensor;
import com.ana.th.bluetooth.ExceptionBluetoothNotPresent;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import java.io.IOException;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Arrays;

public class BluetoothActivity extends MasterActivity {
    ArrayAdapter<String> dispConocidosArray;
    ArrayAdapter<String> dispNuevosArray;
    private BluetoothSensor _bluetoothSensor;
    Button _btnSincronizar;
    MaterialTextView _txtDatos;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizacion);
        // Inicializamos el boton de sincronizar
        _btnSincronizar = findViewById(R.id.BtnSync);
        _txtDatos=findViewById(R.id.txtDatosBluetooth);
        _txtDatos.setMovementMethod(new ScrollingMovementMethod());
        Handler _hdl=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what== BluetoothSensor.MessageConstants.MESSAGE_READ)
                {
                    char c=(char)msg.arg1;
                    // Obtenemos un array de bytes, estos son caracteres
                    _txtDatos.setText(new StringBuilder().append(_txtDatos.getText()).append(c));
                }
            }
        };

        try {
            _bluetoothSensor = new BluetoothSensor(_hdl);
        }
        catch (ExceptionBluetoothNotPresent exc) {
            // Mostrar el error
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Bluetooth error")
                    .setMessage(exc.getMessage())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Regresamos
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }

        //////////////////Comprobación persmiso localizan para android >=6.0////////////////////////
        // Comprobar permiso
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        // Si no hay permiso actualmente
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // Explicar permiso
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, R.string.JustificacionPermiso,
                        Toast.LENGTH_SHORT).show();
            }
            // Solicitar el permiso
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        habilitarBT();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            _bluetoothSensor.desconectar();
        } catch (IOException e) {
            e.printStackTrace();
            displayToast("Bluetooth desconectado");
        }

    }

    void habilitarBT(){
        // Verificamos si esta habilitado el Bluetooth
        if(!_bluetoothSensor.estaHabilitado()){
            startActivityForResult(_bluetoothSensor.getIntentHabilitarBluetooth(), BluetoothSensor.REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Obtenemos si el usuario habilito el Bluetooth
        if (requestCode == BluetoothSensor.REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED){
            displayToast("Debes habilitar la funcionalidad de Bluetooth");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // No mostramos esta opción
        MenuItem _mnSincronizar= menu.findItem(R.id.MenuSincronizar);
        _mnSincronizar.setVisible(false);
        MenuItem _mnSeleccionarBt=menu.findItem(R.id.MenuBluetooth);
        _mnSeleccionarBt.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                habilitarBT();
                if (_bluetoothSensor.estaHabilitado())
                    SeleccionarBt();
                return true;
            }
        });
        return true;
    }

    private CharSequence[] _cs;
    private void SeleccionarBt() {
        // Obtenemos la lista de bluetooths
        ArrayList<String> _btList = _bluetoothSensor.getNombresSincronizados();
        _cs=_btList.toArray(new CharSequence[_btList.size()]);
        //Creamos un dialogo de selección de los dispositivos sincronizados actualmente
        new MaterialAlertDialogBuilder(this)
                .setTitle("Bluetooth a sincronizar")
                .setItems(_cs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String _btSeleccionado = (String)_cs[which];
                        _bluetoothSensor.setBluetoothByString(_btSeleccionado);
                        _btnSincronizar.setEnabled(true);
                        _btnSincronizar.setText("Sincronizar con "+_bluetoothSensor.getNombre());
                    }
                })
                .setPositiveButton("Vincular nuevo BT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        emparejarBluetooth();
                    }
                })
                .setCancelable(true)
                .show();
    }

    public void sincronizarDatos(View view) {
        // Conectamos al dispositivo Bluetooth
        try {
            displayToast("Conectando con "+_bluetoothSensor.getNombre());
            _bluetoothSensor.conectar();
        } catch (IOException e) {
            e.printStackTrace();
            displayToast("Error en conexión Bluetooth");
        }
    }

    public void limpiarDatos(View view){
        _txtDatos.setText("");
    }

    void emparejarBluetooth(){
        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivityForResult(intent, BluetoothSensor.REQUEST_PAIR_DEVICE);
    }
}