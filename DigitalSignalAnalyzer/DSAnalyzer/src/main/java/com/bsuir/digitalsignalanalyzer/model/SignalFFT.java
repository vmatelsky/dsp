package com.bsuir.digitalsignalanalyzer.model;

import java.io.Serializable;

public class SignalFFT implements Serializable{

    private final float[] _data;

    public SignalFFT(float[] data) {
        _data = data;
    }

    public float[] getData() {
        return _data;
    }
}
