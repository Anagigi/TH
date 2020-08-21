package com.ana.th;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;

public class DetalleSensor extends AppCompatActivity {


    private String mOrderMessage;


    //Declaracion de variables de la grafica

    private LineChart lineChart;
    private LineChart lineChart1;
    //int[] colorArray = {R.color.indigo, R.color.indigo1, R.color.indigo2, R.color.indigo3, R.color.indigo4}; //añadir mas colores si se desea

    //Variables para recycler view
    private final LinkedList<String> mWordList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_sensor);
        ///////GRAFICA ALEATORIA PROVISIONAL///////
        // Enlazamos al XML
        lineChart = findViewById(R.id.lineChart);
        lineChart1 = findViewById(R.id.lineChart1);


        //TODO DATOS QUE SE DEBEN SUSTITUIR POR LOS SINCRONIZADOS

        // Creamos un set de datos grafica
        ArrayList<Entry> lineEntries = new ArrayList<Entry>();
        for (int i = 0; i < 11; i++) {
            float y = (int) (Math.random() * (-40)) + 30;
            lineEntries.add(new Entry((float) i, (float) y));
        }

        //TODO DATOS QUE SE DEBEN SUSTITUIR POR LOS SINCRONIZADOS

        // Creamos un set de datos grafica 1
        ArrayList<Entry> lineEntries1 = new ArrayList<Entry>();
        for (int i = 0; i < 11; i++) {
            float y = (int) (Math.random() * 8) + 1;
            lineEntries1.add(new Entry((float) i, (float) y));
        }



            /*POSIBLES MODIFICACIONES PARA UTILIZAR EN LA GRAFICA LINEAL PARA EL SIGUIENTE CODIGO
            set1.setDrawCircleHole(true);
            set1.setCircleColor(android.R.color.holo_blue_dark);
            set1.setCircleHoleColor(android.R.color.holo_purple);
            set1.setCircleRadius(10);
            set1.setCircleHoleRadius(10);
            set1.setValueTextSize(10); //tamaño del contorno
            set1.enableDashedLine(5, 10, 0);
            //Color a la linea, se pueden poner diversos colores, tantos como se quieran meter
            //en el colorArray
            set1.setColors(colorArray, MainActivity.this);
            set1.setDrawValues(false); //Se ven o no los valores
            set1.getCubicIntensity(4);
            */

        LineDataSet set = new LineDataSet(lineEntries, "Temperatura");
        set.setDrawFilled(true);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
            set.setFillDrawable(drawable);
            set.setLineWidth(2);
            set.setDrawCircles(true);
            set.setColor(R.color.indigo0);
            set.setValueTextColor(Color.BLUE);
            set.setValueTextSize(10);
            set.setDrawHighlightIndicators(true);
            set.setDrawCircleHole(true);
            set.setCircleColor(Color.WHITE);
            set.setCircleHoleColor(R.color.indigo3);
            set.setCircleRadius(3);
            set.setCircleHoleRadius(2);
            set.enableDashedHighlightLine(5, 10, 0);
        } else {
            set.setFillColor(Color.BLACK);
        }

        LineDataSet set1 = new LineDataSet(lineEntries1, "Humedad");
        set1.setDrawFilled(true);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
            set1.setFillDrawable(drawable);
            set1.setLineWidth(2);
            set1.setDrawCircles(true);
            set1.setColor(R.color.indigo0);
            set1.setValueTextColor(Color.BLUE);
            set1.setValueTextSize(10);
            set1.setDrawHighlightIndicators(true);
            set1.setDrawCircleHole(true);
            set1.setCircleColor(Color.WHITE);
            set1.setCircleHoleColor(R.color.indigo3);
            set1.setCircleRadius(3);
            set1.setCircleHoleRadius(2);
            set1.enableDashedHighlightLine(5, 10, 0);

        } else {
            set1.setFillColor(Color.BLACK);
        }

        // Asociamos al gráfico

        LineData lineData = new LineData();
        lineData.addDataSet(set);
        lineChart.setData(lineData);

        // Asociamos al gráfico 1
        LineData lineData1 = new LineData();
        lineData1.addDataSet(set1);
        lineChart1.setData(lineData1);

        createCharts();

        ////LISTA RECYCLERVIEW PARA LOS NOMBRES DE LOS SENSORES ///////
        //Nombre de los sensores
        // Put initial data into the node list.
        for (int i = 1; i < 7; i++) {
            mWordList.addLast("Nodo " + i);
        }

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new WordListAdapter(this, mWordList);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }


    private Chart getSameChart(Chart chart, String description, int textColor, int background, int animateY) {
        chart.getDescription().setText(description);
        chart.getDescription().setTextSize(15);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.animateY((animateY));
        chart.getXAxis().setDrawGridLines(false);
        //chart.getXAxis().setAxisLineDashedLine();TODO ???????????? para hacer discontinuo el grid


        return chart;
    }

    private void axisX(XAxis axis) {
        axis.setGranularityEnabled(false); //de cuanto en cuanto se muestran los datos
        axis.setPosition(XAxis.XAxisPosition.TOP);

    }


    private void axisLeft(YAxis axis) {
        axis.setSpaceMax(90); //se agrega mas espacio en la parte de arriba para que los datos no queden muy pegados
        axis.setAxisMinimum(-40);
    }

    private void axisLeft1(YAxis axis) {
        axis.setSpaceMax(100); //se agrega mas espacio en la parte de arriba para que los datos no queden muy pegados
        axis.setAxisMinimum(0);
    }

    //Deshabilitamos el lado derecho para que no aparezcan los numeros en las dos partes
    private void axisRight(YAxis axis) {
        axis.setEnabled(false);
    }


    //ahora se crea la grafica
    public void createCharts() {
        lineChart = (LineChart) getSameChart(lineChart, "Temperatura", Color.GRAY,
                Color.WHITE, 3000);
        lineChart.setDrawGridBackground(false); //se muestran lineas de fondo
        //lineChart.setData(getLineData());
        lineChart.setDrawBorders(false);
        lineChart.invalidate();
        axisX(lineChart.getXAxis());
        axisLeft(lineChart.getAxisLeft());
        axisRight(lineChart.getAxisRight());

        lineChart1 = (LineChart) getSameChart(lineChart1, "Humedad", Color.BLUE,
                Color.WHITE, 3000);
        lineChart1.setDrawGridBackground(false); //se muestran lineas de fondo
        //lineChart.setData(getLineData());
        lineChart1.setDrawBorders(false);
        lineChart1.invalidate();
        axisX(lineChart1.getXAxis());
        axisLeft1(lineChart1.getAxisLeft());
        axisRight(lineChart1.getAxisRight());
    }


    //agregamos un display al final porque es una buena practica poner los propios metodos
    //debajo de los metodos ya proporcionados en MainActivity por la plantilla
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();

    }

    /**
     * Shows a message that the donut image was clicked.
     */
    public void TemperaturaNodo(View view) {
        //TODO al pulsar la imagen del termometro, en textview con id: entradatemperatura se debe
        // cargar el dato de temperatura del nodo presente en la pantalla
        mOrderMessage = getString(R.string.selecciontemperatura);
        displayToast(mOrderMessage);
    }

    public void HumedadNodo(View view) {
        //TODO al pulsar la imagen de las gotas de humedad, en textview con id: entradahumedad
        // se debe cargar el dato de humedad del nodo presente en la pantalla
        mOrderMessage = getString(R.string.seleccionhumedad);
        displayToast(mOrderMessage);
    }

}