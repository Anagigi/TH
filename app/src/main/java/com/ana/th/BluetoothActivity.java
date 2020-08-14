package com.ana.th;

import android.Manifest;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BluetoothActivity extends MainActivity {

    //Elementos que forman parte del interfaz usando las funciones de butterknife
    ArrayAdapter<String> dispConocidosArray;
    ArrayAdapter<String> dispNuevosArray;

    public static String addressEnv;
    @BindView(R.id.disp_conocidos_titulo)
    TextView dispConocidosTitulo;
    @BindView(R.id.disp_conocidos_lista)
    ListView dispConocidosLista;
    @BindView(R.id.disp_nuevos_titulo)
    TextView dispNuevosTitulo;
    @BindView(R.id.disp_nuevos_lista)
    ListView dispNuevosLista;
    @BindView(R.id.boton_buscar)
    Button botonBuscar;


    private BluetoothAdapter mBtAdapter;
    private BluetoothSocket btSocket;
    private boolean isBtConnected = false;

    int Cancelar_buscar = 0;

    MyService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Selecciona el conector bluetooth
        try {
            mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        catch (Exception a){

        }
        if(mBtAdapter==null){

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            // Handle the slideshow action (for now display a toast).
            drawer.closeDrawer(GravityCompat.START);
            displayToast(getString(R.string.finish_activity));
            this.finish();
        }



        //Iniciar los array.
        dispConocidosArray = new ArrayAdapter<String>(this, R.layout.nombre_dispositivos);
        dispNuevosArray = new ArrayAdapter<String>(this, R.layout.nombre_dispositivos);

        //Se asigna la vista
        dispConocidosLista.setOnItemClickListener(mDeviceClickListener);
        dispNuevosLista.setOnItemClickListener(mDeviceClickListener);

        //Mostramos los dispositivos conocidos
        //BluetoothONOFF();
        //dispConocidosShow();


        //
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        //////////////////Compraobacion persmiso localizan para android >=6.0////////////////////////
        // Comprobar permiso
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //
        } else {
            // Explicar permiso
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, R.string.JustificacionPermiso,
                        Toast.LENGTH_SHORT).show();
            }

            // Solicitar el permiso
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            ////////////////////////////////////////////////////////
        }
    }

    @OnClick(R.id.boton_buscar)
    public void BuscarDispositivos(View view) {
        //Activa bluetooth
        if (!mBtAdapter.isDiscovering() && Cancelar_buscar == 0) {
            mBtAdapter.startDiscovery();
            botonBuscar.setText(R.string.Boton_detener);
            Cancelar_buscar = 1;
        }

        if (mBtAdapter.isDiscovering() && Cancelar_buscar == 2) {
            mBtAdapter.cancelDiscovery();
            botonBuscar.setText(R.string.boton_inicio);
            Cancelar_buscar = 0;
        }

        if (Cancelar_buscar == 1) {
            Cancelar_buscar = 2;
        }
    }

    //Muestra los dispositivos conocidos
    public void dispConocidosShow() {
        dispConocidosTitulo.setVisibility(View.VISIBLE);
        dispConocidosLista.setAdapter(dispConocidosArray);

        Set<BluetoothDevice> dispConocidos = mBtAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (dispConocidos.size() > 0) {
            for (BluetoothDevice device : dispConocidos) {
                dispConocidosArray.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            dispConocidosArray.add("No hay dispositivos conocidos");
        }
    }

    //Activa el bluetooth
    private void BluetoothONOFF() {
        // Check device has Bluetooth and that it is turned on
        if (mBtAdapter == null) {
            Toast.makeText(getBaseContext(), R.string.DispNoTieneBluetooth, Toast.LENGTH_SHORT).show();
        } else {
            if (mBtAdapter.isEnabled()) {
                Toast.makeText(getBaseContext(), R.string.BluetoothActiv, Toast.LENGTH_SHORT).show();
                mBtAdapter.startDiscovery();
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
                mBtAdapter.startDiscovery();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    //
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dispNuevosTitulo.setVisibility(View.VISIBLE);
            dispNuevosLista.setAdapter(dispNuevosArray);
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    dispNuevosArray.add(device.getName() + "\n" + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Toast.makeText(getBaseContext(), R.string.BusquedaAcabada, Toast.LENGTH_SHORT).show();
                botonBuscar.setText(R.string.boton_inicio);
                if (dispNuevosArray.getCount() == 0) {
                    dispNuevosArray.add("No hay dispositivos nuevos");
                }

            }
        }
    };
    //Detecta la pulsacion sobre la lista de dispositivos bluetooth
    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            mBtAdapter.cancelDiscovery();
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            addressEnv = address;
            new ConnectBT().execute();

        }
    };


    //Estable la conexion con el dispositivo bluetooth
    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true;

        @Override
        protected Void doInBackground(Void... devices) {

            btSocket = mService.Socket(addressEnv);

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                msg("Connected.");
                isBtConnected = true;
            }

        }
    }

    //Muestra mensajes en pantalla
    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
     /*    if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }*/
    }

    //Estable conexion con el servicio
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


}