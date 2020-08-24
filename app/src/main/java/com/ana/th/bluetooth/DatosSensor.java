package com.ana.th.bluetooth;

import java.util.Date;

public class DatosSensor {
    private float _temperatura;
    private float _humedad;
    private Date _fecha;

    public DatosSensor(float temperatura, float humedad, Date fecha){
        _temperatura=temperatura;
        _humedad=humedad;
        _fecha=fecha;
    }

    public Date get_fecha() {
        return _fecha;
    }

    public float get_humedad() {
        return _humedad;
    }

    public void set_temperatura(float _temperatura) {
        this._temperatura = _temperatura;
    }
}
