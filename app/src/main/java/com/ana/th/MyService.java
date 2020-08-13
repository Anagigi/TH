package com.ana.th;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MyService extends Service {
    private final IBinder mBinder = new LocalBinder();
    //Devuelve la cadena contenida en readMessage
    public String getReadMessage() {
        return readMessage;
    }
    String readMessage="";

    //Se declaran los diferentes elementos de la conexion bluetooth
    private BluetoothAdapter mBtAdapter;
    private BluetoothSocket btSocket;
    private boolean isBtConnected = false;
    private ConnectedThread mConnectedThread;

    //UUID de dispositivos serie
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //Clase empleada para el Binder
    public class LocalBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }
    //TODO my service
    //public MyService() {
    //}

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // Cliente bluetooth
    public BluetoothSocket Socket(String addrress) {
        try {
            if (btSocket == null || !isBtConnected) {
                mBtAdapter = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice dispositivo = mBtAdapter.getRemoteDevice(addrress);
                btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
            }
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();

        }
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        try {
            btSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
        return btSocket;
    }

    //Envia al dispositivo bluetooth
    public void Enviar(String string) {
        if (btSocket != null) {
            mConnectedThread.write(string);
        }
    }
    //Estable la conexion con el dispositivo
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //Se crea el hilo de conexion
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            int bytes = 0;

            // Recibe caracteres hasta que se reciba el caracter final
            while (true) {
                try {
                    String msg = "";
                    char ch;
                    while((ch= (char) mmInStream.read())!='#') {
                        bytes++;
                        msg+=ch;
                    }
                    readMessage = msg;
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //Convierte la cadena en un buffer de bytes
            try {
                mmOutStream.write(msgBuffer);              //Envia el buffer
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "Error de conexion", Toast.LENGTH_LONG).show();
            }
        }
    }
}
