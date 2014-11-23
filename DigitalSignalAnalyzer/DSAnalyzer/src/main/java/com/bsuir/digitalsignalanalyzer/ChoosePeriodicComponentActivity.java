package com.bsuir.digitalsignalanalyzer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;


@EActivity(R.layout.activity_choose_periodic_component)
@OptionsMenu(R.menu.menu_choose_periodic_component)
public class ChoosePeriodicComponentActivity extends Activity {

    public static final String PERIODIC_COMPONENTS_KEY = "Periodic components";

    @ViewById(R.id.select_periodic_components)
    ListView _selectedPeriodicComponents;

    @ViewById(R.id.frequency_to_add)
    EditText _frequencyToAdd;

    @Extra(PERIODIC_COMPONENTS_KEY)
    @NonConfigurationInstance
    ArrayList<Integer> _periodicComponents = new ArrayList<Integer>();

    @AfterViews
    void initListView() {
        loadPeriodicComponentsToListView();
    }

    @ItemLongClick(R.id.select_periodic_components)
    void periodicFrequencyLongClick(final Integer frequency) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Delete?");
        adb.setMessage("Are you sure you want to delete " + String.valueOf(frequency) + " Hz");
        adb.setNegativeButton("Cancel", null);
        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                _periodicComponents.remove(frequency);
                loadPeriodicComponentsToListView();
            }});
        adb.show();
    }

    @Click(R.id.add_frequency)
    void addFrequencyButtonClicked() {
        try {
            if (addFrequencyToList(getFrequencyFromEditText())) {
                sortPeriodicComponents();
                loadPeriodicComponentsToListView();
                clearTextInEditText();
            }
        } catch (NumberFormatException ex) {
            Toast.makeText(this, "Unable to parse frequency", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

    }

    private void clearTextInEditText() {
        _frequencyToAdd.getText().clear();
    }

    @OptionsItem(R.id.confirm_selection)
    void confirmSelection() {
        Intent intent = new Intent();
        intent.putIntegerArrayListExtra(PERIODIC_COMPONENTS_KEY, _periodicComponents);
        setResult(RESULT_OK, intent);
        finish();
    }

    private Integer getFrequencyFromEditText() throws java.lang.NumberFormatException {
        String stringFrequency = _frequencyToAdd.getText().toString().trim();
        return Integer.valueOf(stringFrequency);
    }

    private boolean addFrequencyToList(Integer frequency) {
        boolean result = false;
        if (!_periodicComponents.contains(frequency)) {
             result = _periodicComponents.add(frequency);
        }
        return result;
    }

    private void sortPeriodicComponents() {
        Collections.sort(_periodicComponents);
    }

    private void loadPeriodicComponentsToListView() {
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_list_item_1,
                _periodicComponents);
        _selectedPeriodicComponents.setAdapter(adapter);
    }

}
