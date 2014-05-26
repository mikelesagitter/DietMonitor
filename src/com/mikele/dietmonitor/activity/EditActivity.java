package com.mikele.dietmonitor.activity;

import java.util.Calendar;

import com.mikele.dietmonitor.R;
import com.mikele.dietmonitor.tools.DataSerializer;
import com.mikele.dietmonitor.tools.DateAdjuster;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class EditActivity extends Activity {
		
	private EditText etInput;
	private Button btnAdd;
	private Intent intent;
	private String id;
	private String poid;
	private int mYear;
	private int mMonth;
	private int mDay;
	private Bundle extras;
	private TextView mDateDisplay;
	private Button mPickDate;
	private Button btnDel;
	private Button btnEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_activity);
		extras = this.getIntent().getExtras();
		
		id = extras.getString("id");
		poid = extras.getString("poid");
		mYear = Integer.parseInt(extras.getString("mYear"));
		mMonth = Integer.parseInt(extras.getString("mMonth"));
		mMonth--;
		mDay = Integer.parseInt(extras.getString("mDay"));
		
		
		Log.d("Je recoit =>", id);
		Log.d("Je recoit =>", poid);
		Log.d("Je recoit =>", String.valueOf(mYear));
		Log.d("Je recoit =>", String.valueOf(mMonth));
		Log.d("Je recoit =>", String.valueOf(mDay));
		
		
		setUpView();
		updateDisplay();
//		Boolean b = extras.containsKey("poid");
//		if(!b) {Log.d("Extras", "Pas d'extras");}else{Log.d("Extras", "Il y a des extras!!");}

	}
	
	
	 protected void updateDisplay() {
	        mDateDisplay.setText(
	            new StringBuilder()
	            		.append(mDay).append("-")
	                    .append(mMonth + 1).append("-")
	                    .append(mYear).append(" "));
	}
	 
	 protected DatePickerDialog.OnDateSetListener mDateSetListener =
		        new DatePickerDialog.OnDateSetListener() {
		            public void onDateSet(DatePicker view, int year, 
		                                  int monthOfYear, int dayOfMonth) {
		                mYear = year;
		                mMonth = monthOfYear;
		                mDay = dayOfMonth;
		                updateDisplay();
		            }
		    };
	
		    
		protected Dialog onCreateDialog(int id) {
	            return new DatePickerDialog(this,
	                        mDateSetListener,
	                        mYear, mMonth, mDay);
	    }
		
	private void setUpView() {
        etInput = (EditText)this.findViewById(R.id.editText_input);
        btnDel = (Button)this.findViewById(R.id.button_delete);
        btnEdit = (Button)this.findViewById(R.id.button_edit);
        mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
        mPickDate = (Button) findViewById(R.id.pickDate);
        
        mPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(0);
            }
        });
        
        btnDel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(EditActivity.this, MainActivity.class);
            	
            	intent.putExtra("do", "delete");
//            	intent.putExtra("id", id);
//			    intent.putExtra("poid", etInput.getText().toString());
//			    intent.putExtra("mYear", DateAdjuster.adjustStringNumber(String.valueOf(mYear)));
//			    intent.putExtra("mMonth", DateAdjuster.adjustStringNumber(String.valueOf(mMonth)));
//			    intent.putExtra("mDay", DateAdjuster.adjustStringNumber(String.valueOf(mDay)));
				setResult(RESULT_OK, intent);
				finish();
            }
        });
        
        btnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(EditActivity.this, MainActivity.class);
            	
            	intent.putExtra("do", "edit");
            	intent.putExtra("id", id);
			    intent.putExtra("poid", etInput.getText().toString());
			    intent.putExtra("mYear", DateAdjuster.adjustStringNumber(String.valueOf(mYear)));
			    intent.putExtra("mMonth", DateAdjuster.adjustStringNumber(String.valueOf(mMonth)));
			    intent.putExtra("mDay", DateAdjuster.adjustStringNumber(String.valueOf(mDay)));
				setResult(RESULT_OK, intent);
				finish();
            }
        });
        
        etInput.setText(poid);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	        Log.d(this.getClass().getName(), "Back button pressed");
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
		Log.d(this.getClass().getName(), "Pysical back button pressed");
	}
}
