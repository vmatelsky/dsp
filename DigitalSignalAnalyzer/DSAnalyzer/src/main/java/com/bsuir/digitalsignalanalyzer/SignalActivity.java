package com.bsuir.digitalsignalanalyzer;

import android.app.Activity;
import android.graphics.Color;
import android.widget.FrameLayout;

import com.bsuir.digitalsignalanalyzer.model.Signal;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_signal)
@OptionsMenu(R.menu.signal_activity_menu)
public class SignalActivity extends Activity {

    @Extra("signal")
    Signal _signal;

    @ViewById(R.id.chart_place)
    FrameLayout _chartPlace;

    private LineGraphView graphView;

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
