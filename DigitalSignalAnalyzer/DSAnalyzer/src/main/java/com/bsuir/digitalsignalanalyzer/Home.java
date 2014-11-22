package com.bsuir.digitalsignalanalyzer;

import android.app.Activity;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@EActivity(R.layout.activity_home)
public class Home extends Activity {

    @ViewById(R.id.signals_list)
    ListView _signalsList;

    @AfterViews
    void initSignalsList() {
        try {
            List<String> signals = Arrays.asList(getAssets().list("Prim_Sign"));
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    signals );

            _signalsList.setAdapter(arrayAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ItemClick(R.id.signals_list)
    void listItemClicked(String signalPath) {
        SignalActivity_.intent(this)._signalPath("Prim_Sign" + File.separator + signalPath).start();
    }
}
