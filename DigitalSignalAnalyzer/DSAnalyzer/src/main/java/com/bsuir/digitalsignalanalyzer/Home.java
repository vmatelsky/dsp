package com.bsuir.digitalsignalanalyzer;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bsuir.digitalsignalanalyzer.model.Signal;
import com.lamerman.FileDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@EActivity(R.layout.activity_home)
@OptionsMenu(R.menu.menu_home)
public class Home extends Activity {

    @ViewById(R.id.signals_list)
    ListView _signalsList;
    private static final int REQUEST_SELECT_SIGNAL = 1;

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
        startSignalViewActivity(parseSignalFromAssets("Prim_Sign" + File.separator + signalPath));
    }

    @OptionsItem(R.id.select_signal)
    public void selectSignal() {
        Intent intent = new Intent(getBaseContext(), FileDialog.class);
        intent.putExtra(FileDialog.START_PATH, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        intent.putExtra(FileDialog.CAN_SELECT_DIR, false);
        intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { "bin" });
        startActivityForResult(intent, REQUEST_SELECT_SIGNAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_SELECT_SIGNAL) {
                String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
                startSignalViewActivity(parseSignal(filePath));
            }
        }
    }

    private void startSignalViewActivity(Signal signal) {
        if (signal != null) {
            SignalActivity_.intent(this)._signal(signal).start();
        }
    }

    private Signal parseSignalFromAssets(String path) {
        try {
            return Signal.fromAssets(getAssets(), path);
        } catch (IOException e) {
            Toast.makeText(this, "Error parsing signal", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return null;
    }

    private Signal parseSignal(String path) {
        try {
            return Signal.fromFile(path);
        } catch (IOException e) {
            Toast.makeText(this, "Error parsing signal", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return null;
    }

}
