package com.bsuir.digitalsignalanalyzer;

import android.app.Activity;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.Toast;

import com.bsuir.digitalsignalanalyzer.model.Signal;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;

@EActivity(R.layout.activity_signal)
@OptionsMenu(R.menu.signal_activity_menu)
public class SignalActivity extends Activity {

    @Extra("signal path")
    String _signalPath;

    Signal _signal;

    @ViewById(R.id.chart_place)
    FrameLayout _chartPlace;

    private LineGraphView graphView;

    @AfterInject
    public void parseSignal() {
        try {
            _signal = Signal.fromAssets(getAssets(), _signalPath);
        } catch (IOException e) {
            Toast.makeText(this, "Error parsing signal", Toast.LENGTH_LONG).show();
            finish();
            e.printStackTrace();
        }
    }

    @AfterViews
    public void setActivityTitle() {
        setTitle(_signalPath);
    }

    @OptionsItem(R.id.calculate_spectrum)
    public void calculateSpectrum() {
        SpectrumActivity_.intent(this)._signal(_signal).start();
    }

    @AfterViews
    public void drawSignal() {
        graphView = new LineGraphView(this, "");
        graphView.setScalable(true);
        graphView.setScrollable(true);
        graphView.setTitle("");

        graphView.getGraphViewStyle().setGridColor(Color.GRAY);

        graphView.setViewPort(0, 1000);

        GraphView.GraphViewData[] data = new GraphView.GraphViewData[_signal.getDataSize()];

        float discretizationTime = _signal.discretizationTime();
        float valueX = 0;
        for (int i = 0; i < _signal.getDataSize(); ++i) {
            GraphView.GraphViewData dataItem = new GraphView.GraphViewData(valueX, _signal.getData()[i]);
            valueX += discretizationTime;
            data[i] = dataItem;
        }

        GraphViewSeries signalSeries = new GraphViewSeries(data);
        graphView.addSeries(signalSeries);

        _chartPlace.removeAllViews();
        _chartPlace.addView(graphView);
    }

}
