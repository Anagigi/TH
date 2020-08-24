package com.ana.th.bluetooth;

import java.util.List;

public class Sensor {
    private String _nombre;
    private int _id;
    private List<DatosSensor> _datos;

    public List<DatosSensor> get_DatosSensor() {
        return _datos;
    }

    public Sensor(String nombre, int id){
        _nombre=nombre;
        _id=id;
    }

    public DatosSensor UltimoDato(){
        DatosSensor ultimo;
        if (_datos.size()>0){
            ultimo= _datos.get(_datos.size());
            return ultimo;
        }
        return new DatosSensor(0,0,null);
    }
}
