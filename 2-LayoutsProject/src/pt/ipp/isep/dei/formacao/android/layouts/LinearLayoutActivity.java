package pt.ipp.isep.dei.formacao.android.layouts;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

public class LinearLayoutActivity extends Activity implements OnItemSelectedListener, OnCheckedChangeListener{

    public static final String TAG = "LayoutsApplication";

    private Spinner mSpinner;
    private RadioGroup mRadioGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linearlayout);

        mSpinner = (Spinner) findViewById(R.id.spinnerLinearLayout);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MainActivity.items);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(this);

        mRadioGroup = (RadioGroup) findViewById(R.id.rGroupLinearLayout);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    public void onItemSelected(AdapterView<?> adapter, View arg1, int i, long lng) {
        Log.i(TAG, adapter.getItemAtPosition(i).toString());        
    }

    public void onNothingSelected(AdapterView<?> adapter) {
        // TODO Auto-generated method stub
    }

    public void onCheckedChanged(RadioGroup rGroup, int id) {

        if(id == R.id.op1LinearLayout)
            Log.i(TAG, "Opção 1 seleccionada");

        if(id == R.id.op2LinearLayout)
            Log.i(TAG, "Opção 2 seleccionada");
    }
}
