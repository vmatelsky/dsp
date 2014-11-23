package com.bsuir.digitalsignalanalyzer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bsuir.digitalsignalanalyzer.model.Signal;
import com.bsuir.digitalsignalanalyzer.model.SignalFFT;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;

@EActivity(R.layout.activity_spectrum)
@OptionsMenu(R.menu.spectrum_activity_menu)
public class SpectrumActivity extends Activity {

    final static int SELECT_PERIODIC_COMPONENT_REQUEST_CODE = 100;

    @Extra("signal")
    Signal _signal;

    SignalFFT _signalFFT;

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

        _signalFFT = new SignalFFT(fft);

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

    @OptionsItem(R.id.select_periodic_component)
    public void selectPeriodicComponent() {
        ChoosePeriodicComponentActivity_.intent(this).startForResult(SELECT_PERIODIC_COMPONENT_REQUEST_CODE);
    }

    @OnActivityResult(SELECT_PERIODIC_COMPONENT_REQUEST_CODE)
    void onResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            List<Integer> periodicComponents = data.getIntegerArrayListExtra(ChoosePeriodicComponentActivity.PERIODIC_COMPONENTS_KEY);
            Toast.makeText(this, periodicComponents.toString(), Toast.LENGTH_LONG).show();
            PeriodicSpectrumActivity_.intent(this)
                    ._periodicComponents(periodicComponents)
                    ._signal(_signal)
                    ._signalFFT(_signalFFT)
                    .start();
        }
    }
}
