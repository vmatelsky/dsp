package com.bsuir.digitalsignalanalyzer;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;

import com.bsuir.digitalsignalanalyzer.model.Signal;
import com.bsuir.digitalsignalanalyzer.model.SignalFFT;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_periodic_spectrum)
@OptionsMenu(R.menu.menu_periodic_spectrum)
public class PeriodicSpectrumActivity extends Activity {

    @Extra("Periodic component")
    @NonConfigurationInstance
    List<Integer> _periodicComponents = new ArrayList<Integer>();

    @Extra("signal")
    @NonConfigurationInstance
    Signal _signal;

    @Extra("signal fast fourier transform")
    @NonConfigurationInstance
    SignalFFT _signalFFT;

    @ViewById(R.id.raw_signal_place)
    FrameLayout _rawSignalPlace;

    @ViewById(R.id.periodic_signal_place)
    FrameLayout _periodicSignalPlace;

    @ViewById(R.id.noise_signal_place)
    FrameLayout _noiseSignalPlace;


    @AfterViews
    void drawSpectrumCharts() {
        clearChartPlace();
        _rawSignalPlace.addView(createRawSpectrumGraph());
        _periodicSignalPlace.addView(createPeriodicSpectrumGraph());
        _noiseSignalPlace.addView(createNoiseSpectrumGraph());
    }

    private View createNoiseSpectrumGraph() {
        SignalFFT noiseSignal = obtainNoiseSignal(_signalFFT, _periodicComponents);
        return createSpectrumGraph(noiseSignal.getData(), "Noise spectrum");
    }

    private LineGraphView createPeriodicSpectrumGraph() {
        SignalFFT periodicSpectrum = obtainPeriodicSignal(_signalFFT, _periodicComponents);
        return createSpectrumGraph(periodicSpectrum.getData(), "Periodic spectrum");
    }

    private LineGraphView createRawSpectrumGraph() {
        return createSpectrumGraph(_signalFFT.getData(), "Raw spectrum");
    }

    private LineGraphView createSpectrumGraph(float[] fft, String title) {
        LineGraphView graphView = new LineGraphView(this, title);
        graphView.setScalable(true);
        graphView.setScrollable(true);

        graphView.getGraphViewStyle().setGridColor(Color.GRAY);

        GraphView.GraphViewData[] data = new GraphView.GraphViewData[fft.length];
        float deltaF = (float)_signal.getSpectralLinesCount() / (float)_signal.getDataSize();
        float valueX = 0;

        for (int i = 0; i < fft.length; ++i) {
            GraphView.GraphViewData dataItem = new GraphView.GraphViewData(valueX, fft[i]);
            valueX += deltaF;
            data[i] = dataItem;
        }

        GraphViewSeries signalSeries = new GraphViewSeries(data);
        graphView.addSeries(signalSeries);

        graphView.setViewPort(0, 500);

        return graphView;

    }

    private void clearChartPlace() {
        _rawSignalPlace.removeAllViews();
        _periodicSignalPlace.removeAllViews();
        _noiseSignalPlace.removeAllViews();
    }

    private SignalFFT obtainPeriodicSignal(SignalFFT rawSignal, List<Integer> periodicComponents) {
        float[] fft = rawSignal.getData();
        float[] result = new float[fft.length];
        float deltaF = (float)_signal.getSpectralLinesCount() / (float)_signal.getDataSize();

        for (Integer frequency : periodicComponents) {
            int index = (int) (frequency / deltaF);
            if (index < fft.length) {
                result[index] = fft[index];
            }
        }
        return new SignalFFT(result);
    }

    private SignalFFT obtainNoiseSignal(SignalFFT rawSignal, List<Integer> periodicComponents) {
        float[] result = rawSignal.getData().clone();
        float deltaF = (float)_signal.getSpectralLinesCount() / (float)_signal.getDataSize();

        for (Integer frequency : periodicComponents) {
            int index = (int) (frequency / deltaF);
            if (index < result.length) {
                result[index] = 0;
            }
        }
        return new SignalFFT(result);
    }

}
