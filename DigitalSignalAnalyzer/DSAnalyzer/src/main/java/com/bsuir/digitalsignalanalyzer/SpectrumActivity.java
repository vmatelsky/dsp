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
import org.androidannotations.annotations.ViewById;

import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;

@EActivity(R.layout.activity_spectrum)
public class SpectrumActivity extends Activity {

    @Extra("signal")
    Signal _signal;

    @ViewById(R.id.chart_place)
    FrameLayout _chartPlace;

    private LineGraphView graphView;

    @AfterViews
    public void drawSignal() {
        graphView = new LineGraphView(this, "");
        graphView.setScalable(true);
        graphView.setScrollable(true);
        graphView.setTitle("");

        graphView.getGraphViewStyle().setGridColor(Color.GRAY);

        FloatFFT_1D fftDo = new FloatFFT_1D(_signal.getData().length);

        float[] fft = new float[_signal.getData().length * 2];
        System.arraycopy(_signal.getData(), 0, fft, 0, _signal.getData().length);
        fftDo.realForwardFull(fft);

        GraphView.GraphViewData[] data = new GraphView.GraphViewData[fft.length];
        float deltaF = (float)_signal.getSpectralLinesCount() / (float)_signal.getDataSize();
        float valueX = 0;

        for (int i = 0; i < fft.length; ++i) {
            GraphView.GraphViewData dataItem = new GraphView.GraphViewData(valueX, fft[i]);
            valueX += deltaF;
            data[i] = dataItem;
        }

        graphView.setViewPort(0, 500);

        GraphViewSeries signalSeries = new GraphViewSeries(data);
        graphView.addSeries(signalSeries);

        _chartPlace.removeAllViews();
        _chartPlace.addView(graphView);
    }
}
