package com.bsuir.digitalsignalanalyzer;

import android.app.Activity;
import android.widget.Toast;

import com.bsuir.digitalsignalanalyzer.model.Signal;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import java.io.IOException;

@EActivity(R.layout.activity_signal)
public class SignalActivity extends Activity {

    @Extra("signal path")
    String _signalPath;

    @AfterInject
    public void parseSignal() {
        try {
            Signal signal = Signal.fromAssets(getAssets(), _signalPath);
            Toast.makeText(this, signal.getSignature(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error parsing signal", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
