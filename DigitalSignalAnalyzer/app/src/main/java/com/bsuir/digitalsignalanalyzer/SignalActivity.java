package com.bsuir.digitalsignalanalyzer;

import android.app.Activity;
import android.widget.Toast;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

@EActivity(R.layout.activity_signal)
public class SignalActivity extends Activity {

    @Extra("signal path")
    String _signalPath;

    @AfterInject
    public void doSomethingAfterExtrasInjection() {
        Toast.makeText(this, _signalPath, Toast.LENGTH_LONG).show();
    }

}
