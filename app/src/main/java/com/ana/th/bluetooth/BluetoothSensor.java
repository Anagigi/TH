package com.ana.th.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Clase para instancias objetos que establecen una conexión
 * al módulo HC-05 y obtienen información de los sensores
 */
public class BluetoothSensor {
    BluetoothAdapter _bluetoothAdapterDeMiCel;
    BluetoothDevice _bluetoothDevice;
    BluetoothSocket _bluetoothSocket;
    String MacBluetoothServidor;
    ArrayList<Sensor> _misSensores;
    private static final String TAG = "BLUETOOTH_CONEXION";
    ConnectedThread _miConexionThread;
    private static Handler _handlerBluetooth; // handler that gets info from Bluetooth service

    public final static int REQUEST_ENABLE_BT = 108;
    public final static int REQUEST_PAIR_DEVICE = 109;
    // Especificamos el perfil de servicio que utilizamos para conectarnos
    // info->(https://www.bluetooth.com/specifications/assigned-numbers/service-discovery/)
    private final static UUID BT_MODULO_ID=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;
    }

    /**
     * Indica si existe el hardware de conexión de bluetooth
     * en el celular.
     * @return
     */
    public boolean getExisteBluetooth(){
        if(_bluetoothAdapterDeMiCel ==null)
            return false;
        return true;
    }

    public BluetoothSensor(Handler handler) throws ExceptionBluetoothNotPresent {
        this("AA:BB:CC:DD:EE",handler);
    }

    @SuppressLint("HandlerLeak")
    public BluetoothSensor(String BluetoothMac, Handler handler) throws ExceptionBluetoothNotPresent {
       // Obtenemos acceso al dispositivo Bluetooth del móvil
       _bluetoothAdapterDeMiCel =BluetoothAdapter.getDefaultAdapter();
       // Si no soporta Bluetooth lanzamos una excepción
        if (_bluetoothAdapterDeMiCel ==null)
            throw new ExceptionBluetoothNotPresent("Hardware bluetooth no encontrado en el telefóno");
        _misSensores= new ArrayList<Sensor>();
        _misSensores.add(new Sensor("sensorPrueba",1));
        _handlerBluetooth=handler;
    }

    /**
     * Se establece
     * @return
     */
    public boolean conectar() throws IOException {
        if (estaHabilitado())
        {
            Log.i(TAG,"Conectando");
            // Según la documentación, debemos cancelar el descubrimiento antes de conectarnos a un
            // dispositivo remoto
            _bluetoothAdapterDeMiCel.cancelDiscovery();
            // Iniciamos la conexión con el otro bluetooth
            try {
                // Abrimos un socket de comunicación
                _bluetoothSocket=_bluetoothDevice.createRfcommSocketToServiceRecord(BT_MODULO_ID);
                // Una vez abierto, nos conectamos con el socket
                _bluetoothSocket.connect();
                Log.i(TAG,"Socket abierto: "+_bluetoothSocket.toString());
                // Abrimos un subproceso para leer y escribir al socket
                _miConexionThread = new ConnectedThread(_bluetoothSocket);
                Log.i(TAG,"Thread iniciando");
                _miConexionThread.start();
                Log.i(TAG,"Thread Corriendo");
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
            // Si todo fue correcto, se ha conectado al socket
        }
        return true;
    }

    public void desconectar() throws IOException {
        // Desconectamos el socket de comunicación
        if(_bluetoothSocket!=null){
            _bluetoothSocket.close();
        }
    }

    public boolean estaHabilitado(){
        return _bluetoothAdapterDeMiCel.isEnabled();
    }

    public String getNombre(){
        return _bluetoothDevice.getName();
    }

    public String getMac(){
        return _bluetoothDevice.getAddress();
    }

    public ArrayList<String> getNombresSincronizados(){
        ArrayList<String> lista=new ArrayList<>();
        for (BluetoothDevice bt: _bluetoothAdapterDeMiCel.getBondedDevices()) {
            lista.add(bt.getName()+" - "+ bt.getAddress());
        }
        return lista;
    }

    public BluetoothDevice getBluetoothByString(String nombre_Mac){
        for (BluetoothDevice bt: _bluetoothAdapterDeMiCel.getBondedDevices()) {
            if((bt.getName()+" - "+ bt.getAddress()).equals(nombre_Mac)){
                return bt;
            }
        }
        return null;
    }

    public void setBluetoothByString(String nombre_Mac){
        for (BluetoothDevice bt: _bluetoothAdapterDeMiCel.getBondedDevices()) {
            if((bt.getName()+" - "+ bt.getAddress()).equals(nombre_Mac)){
                _bluetoothDevice=bt;
                MacBluetoothServidor=bt.getAddress();
                break;
            }
        }
    }

    public Intent getIntentHabilitarBluetooth(){
        return new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    }

    /**
     * Obtiene la lista de Sensores con sus datos
     * @return  List de Sensores del proyecto
     */
    public List<Sensor> getListaSensores(){
        return _misSensores;
    }

    /**
     * Creamos una clase privada para levantar un subproceso y que
     * este lea y escriba al bluetooth usando al especificación
     * de comunicación Serial
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; //mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read();
                    // Send the obtained bytes to the UI activity.
                    Message readMsg = _handlerBluetooth.obtainMessage(
                            MessageConstants.MESSAGE_READ, numBytes,-1);
                    readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
                // Share the sent message with the UI activity.
                Message writtenMsg = _handlerBluetooth.obtainMessage(
                        MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
                writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);

                // Send a failure message back to the activity.
                Message writeErrorMsg =
                        _handlerBluetooth.obtainMessage(MessageConstants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("toast",
                        "Couldn't send data to the other device");
                writeErrorMsg.setData(bundle);
                _handlerBluetooth.sendMessage(writeErrorMsg);
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }
}
