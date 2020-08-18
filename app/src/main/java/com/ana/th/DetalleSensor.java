package com.ana.th;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.LinkedList;

public class DetalleSensor extends AppCompatActivity {

    private LineChart lineChart;
    private LineDataSet lineDataSet;

    private final LinkedList<String> mWordList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sensores);
        Intent intent = getIntent();
        ///////GRAFICA ALEATORIA PROVISIONAL///////
        // Enlazamos al XML
        lineChart = findViewById(R.id.lineChart);
        // Creamos un set de datos
        ArrayList<Entry> lineEntries = new ArrayList<Entry>();
        for (int i = 0; i < 11; i++) {
            float y = (int) (Math.random() * 8) + 1;
            lineEntries.add(new Entry((float) i, (float) y));
        }

        // Unimos los datos al data set
        lineDataSet = new LineDataSet(lineEntries, "Platzi");

        // Asociamos al gráfico
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        lineChart.setData(lineData);

        ////LISTA RECYCLERVIEW PARA LOS NOMBRES DE LOS SENSORES ///////
        //Nombre de los sensores
        // Put initial data into the node list.
        for (int i = 1; i < 6; i++) {
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

}