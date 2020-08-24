package com.ana.th.bluetooth;

/**
 * Excepción para indicar que no existe el hardware de Bluetooth en
 * el celular
 */
public class ExceptionBluetoothNotPresent extends Exception {
    public ExceptionBluetoothNotPresent(String msg) {
        super(msg);
    }
}
