package com.ana.th;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;

public class ListaSensoresActivity extends AppCompatActivity {
    //Declaracion de variables de la grafica
    private BarChart barChart;
    //Se crea arrays necesarios para ver los datos que se posicionan en el eje x y el eje y
    //private String[] tempandhum = new String[]{"T1", "H1", "T2", "H2", "T3", "H3"};
    //private int[] temperatura = new int[]{12, -5, 6};
    //private int[] humedad = new int[]{36, 66, 21};
    //Se crea un array para guardar los colores que se veran en la grafica
    private int[] colors = new int[]{R.color.indigo, Color.YELLOW};
    private String[] nodo = new String[]{"Nodo 1", "Nodo 2", "Nodo 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensores);
        barChart = findViewById(R.id.lineChart3);
        BarDataSet barDataSet1 = new BarDataSet(barEntries1(), "Temperatura");
        barDataSet1.setColors(Color.GREEN);
        BarDataSet barDataSet2 = new BarDataSet(barEntries2(), "Humedad");
        barDataSet1.setColors(R.color.indigo3);

        setTitle("Bar Chart Nodes");
        BarData data = new BarData(barDataSet1, barDataSet2);
        data.setValueFormatter(new LargeValueFormatter());
        barChart.setData(data);


        XAxis xAxis = barChart.getXAxis();
        YAxis yAxis = barChart.getAxisLeft();
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);
        yAxis.setSpaceMax(40);
        yAxis.setAxisMinimum(-40);
        yAxis.setGranularityEnabled(true);
        yAxis.setEnabled(false);
        yAxis.setDrawZeroLine(true);
        yAxis.setDrawLabels(false);
        yAxis.setSpaceTop(25f);
        yAxis.setSpaceBottom(25f);
        yAxis.setDrawAxisLine(true);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawZeroLine(true); // draw a zero line
        yAxis.setZeroLineColor(Color.GRAY); //TODO? no pone ningun color
        yAxis.setZeroLineWidth(0.7f);//TODO NO CAMBIA NADA
        yAxisRight.setEnabled(false); //elimina los valores en el eje y derecho


        xAxis.setValueFormatter(new IndexAxisValueFormatter(nodo));
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(tfRegular);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(Color.LTGRAY);
        xAxis.setTextSize(13f);
        xAxis.setLabelCount(5);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);

        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(3);

        barChart.setBackgroundColor(Color.WHITE);
        //barChart.setExtraTopOffset(-30f);
        //barChart.setExtraBottomOffset(10f);
        barChart.setExtraLeftOffset(30f);
        barChart.setExtraRightOffset(30f);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(true);
        barChart.setDrawGridBackground(false);
        Legend legend = barChart.getLegend();
        legend.setEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(true);
        //legend.setTypeface(tfLight);
        legend.setYOffset(0f);
        legend.setXOffset(10f);
        legend.setYEntrySpace(0f);
        legend.setTextSize(8f);

        float barSpace = 0.08f;
        float groupSpace = 0.45f;
        data.setBarWidth(0.45f);

        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * 3);


        barChart.groupBars(0, groupSpace, barSpace);
        barChart.invalidate();
    }

    private ArrayList<BarEntry> barEntries1() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, 15)); //Grados nodo 1
        barEntries.add(new BarEntry(2, 5));//Grados nodo 2
        barEntries.add(new BarEntry(3, -3));//Grados nodo 3
        return barEntries;

    }

    private ArrayList<BarEntry> barEntries2() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, 80)); //Humedad nodo 1
        barEntries.add(new BarEntry(2, 20));//Humedad nodo 2
        barEntries.add(new BarEntry(3, 60));//Humedad nodo 3
        return barEntries;
    }
}