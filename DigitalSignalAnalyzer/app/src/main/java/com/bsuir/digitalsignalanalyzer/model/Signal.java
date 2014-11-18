package com.bsuir.digitalsignalanalyzer.model;

import android.content.res.AssetManager;

import com.bsuir.digitalsignalanalyzer.utils.LittleEndianUtils;

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
        signal._channelsNumber = LittleEndianUtils.readInt(dis);
        signal._signalChannelsSelectionSize = LittleEndianUtils.readInt(dis);
        signal._spectralLinesCount = LittleEndianUtils.readInt(dis);
        signal._cutoffFrequency = LittleEndianUtils.readInt(dis);
        signal._frequencyResolution = LittleEndianUtils.readFloat(dis);
        signal._blockDataCaptureTime = LittleEndianUtils.readFloat(dis);
        signal._totalCaptureTime = LittleEndianUtils.readInt(dis);
        signal._capturedBlocksCount_userSpecified = dis.readInt();
        signal._dataSize = LittleEndianUtils.readInt(dis);
        signal._capturedBlocksCount_systemSpecified = dis.readInt();
        signal._maxSignalValue = LittleEndianUtils.readFloat(dis);
        signal._minSignalVale = LittleEndianUtils.readFloat(dis);

        signal._data = new float[signal._dataSize];
        for (int i = 0; i < signal._dataSize; ++i) {
            signal._data[i] = LittleEndianUtils.readFloat(dis);
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

    public int getChannelsNumber() {
        return _channelsNumber;
    }

    public int getSignalChannelsSelectionSize() {
        return _signalChannelsSelectionSize;
    }

    public int getSpectralLinesCount() {
        return _spectralLinesCount;
    }

    public int getCutoffFrequency() {
        return _cutoffFrequency;
    }

    public float getFrequencyResolution() {
        return _frequencyResolution;
    }

    public float getBlockDataCaptureTime() {
        return _blockDataCaptureTime;
    }

    public int getTotalCaptureTime() {
        return _totalCaptureTime;
    }

    public int getCapturedBlocksCount_userSpecified() {
        return _capturedBlocksCount_userSpecified;
    }

    public int getDataSize() {
        return _dataSize;
    }

    public int getCapturedBlocksCount_systemSpecified() {
        return _capturedBlocksCount_systemSpecified;
    }

    public float getMaxSignalValue() {
        return _maxSignalValue;
    }

    public float getMinSignalVale() {
        return _minSignalVale;
    }

    public float[] getData() {
        return  _data;
    }

    public float discretizationTime() {
        return getBlockDataCaptureTime() / (float) getChannelsNumber();
    }
}
