package com.bsuir.digitalsignalanalyzer.model;

import android.content.res.AssetManager;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class Signal implements Serializable {

    public static Signal fromAssets(final AssetManager assetManager, final String path) throws IOException {
        InputStream is = assetManager.open(path);
        DataInputStream dis = new DataInputStream(new BufferedInputStream((is)));

        byte signature[] = new byte[4];
        dis.read(signature);

        Signal signal = new Signal();
        signal._signature = new String(signature, "US-ASCII");
        signal._channelsNumber = dis.readInt();
        signal._signalChannelsSelectionSize = dis.readInt();
        signal._spectralLinesCount = dis.readInt();
        signal._cutoffFrequency = dis.readInt();
        signal._frequencyResolution = dis.readFloat();
        signal._blockDataCaptureTime = dis.readFloat();
        signal._totalCaptureTime = dis.readInt();
        signal._capturedBlocksCount_userSpecified = dis.readInt();
        signal._dataSize = dis.readInt();
        signal._capturedBlocksCount_systemSpecified = dis.readInt();
        signal._maxSignalValue = dis.readFloat();
        signal._minSignalVale = dis.readFloat();

        signal._data = new float[8192];
        for (int i = 0; i < 8192; ++i) {
            signal._data[i] = dis.readFloat();
        }

        return signal;
    }

    private String _signature;
    private int _channelsNumber;
    private int _signalChannelsSelectionSize;
    private int _spectralLinesCount;
    private int _cutoffFrequency;
    private float _frequencyResolution;
    private float _blockDataCaptureTime;
    private int _totalCaptureTime;
    private int _capturedBlocksCount_userSpecified;
    private int _dataSize;
    private int _capturedBlocksCount_systemSpecified;
    private float _maxSignalValue;
    private float _minSignalVale;

    private float[] _data;

    private Signal() {
    }

    public String getSignature() {
        return _signature;
    }

}
