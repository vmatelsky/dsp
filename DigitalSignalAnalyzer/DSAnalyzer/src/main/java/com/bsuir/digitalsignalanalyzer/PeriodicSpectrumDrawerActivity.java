package com.bsuir.digitalsignalanalyzer;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
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
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;


@EActivity
public class PeriodicSpectrumDrawerActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    @Extra("Periodic component")
    @NonConfigurationInstance
    List<Integer> _periodicComponents = new ArrayList<Integer>();

    @Extra("signal")
    @NonConfigurationInstance
    Signal _signal;

    @Extra("signal fast fourier transform")
    @NonConfigurationInstance
    SignalFFT _signalFFT;

    @ViewById(R.id.chart_place)
    FrameLayout _chartPlace;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    private GraphView _currentGraphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodic_spectrum_drawer);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case 0:
                mTitle = getString(R.string.raw_spectrum);
                _currentGraphView = createRawSpectrumGraph();
                break;
            case 1:
                mTitle = getString(R.string.periodic_spectrum);
                _currentGraphView = createPeriodicSpectrumGraph();
                break;
            case 2:
                mTitle = getString(R.string.noise_spectrum);
                _currentGraphView = createNoiseSpectrumGraph();
                break;
        }

        displayCurrentGraphView();
    }

    @AfterViews
    void displayCurrentGraphView() {
        if (_chartPlace != null) {
            if (_currentGraphView != null) {
                clearChartPlace();
                _chartPlace.addView(_currentGraphView);
            }
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.periodic_spectrum_drawer, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    private LineGraphView createNoiseSpectrumGraph() {
        SignalFFT noiseSignal = obtainNoiseSignal(_signalFFT, _periodicComponents);
        return createSpectrumGraph(noiseSignal.getData(), mTitle.toString());
    }

    private LineGraphView createPeriodicSpectrumGraph() {
        SignalFFT periodicSpectrum = obtainPeriodicSignal(_signalFFT, _periodicComponents);
        return createSpectrumGraph(periodicSpectrum.getData(), mTitle.toString());
    }

    private LineGraphView createRawSpectrumGraph() {
        return createSpectrumGraph(_signalFFT.getData(), mTitle.toString());
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
        _chartPlace.removeAllViews();
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
