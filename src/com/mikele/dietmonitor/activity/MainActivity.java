package com.mikele.dietmonitor.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.mikele.dietmonitor.R;
import com.mikele.dietmonitor.modele.Data;
import com.mikele.dietmonitor.modele.Datas;
import com.mikele.dietmonitor.tools.DataSerializer;
import com.mikele.dietmonitor.tools.DateAdjuster;

public class MainActivity extends Activity {

	// LAOUT
	private EditText etInput;
	private Button btnAdd;
	private Button btnHideKB;
	private ListView lvItem;
	private Button mPickDate;
	private TextView mDateDisplay;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	private int mSecond;
	private String mYearS;
	private String mMonthS;
	private String mDayS;
	private String mHourS;
	private String mMinuteS;
	private String mSecondS;
	private String today;
	// VARIABLES
	private DataSerializer serialiser;
	private ArrayList<String> stringArray;
	private ArrayAdapter<String> itemAdapter;
	public SimpleDateFormat todayDate;
	public Date date;
	public String todayDateString;
	private Data myNewData;
	private ArrayList<Data> datas;
	private ArrayList<Data> myDatas = null;
	private Bundle extras;
	private String rId;
	private String rPoid;
	private String rMYear;
	private String rMMonth;
	private String rMDay;
	private Date rDate;
	private String rMMonthS;
	private String rMDayS;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		serialiser = DataSerializer.getInstance(getApplicationContext()); // getApplicationContext()
		setUpView();
		
		
		final Context context = this;

		todayDate = new SimpleDateFormat("dd-MM-yyyy"); // yyyy-MM-dd HH:mm:ss
		date = new Date();
		todayDateString = todayDate.format(date);

		mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
		mPickDate = (Button) findViewById(R.id.pickDate);

		mPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(0);
			}
		});

		final Calendar c = Calendar.getInstance();

		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		mSecond = c.get(Calendar.SECOND);

		mYearS = String.valueOf(mYear);
		
		mMonthS = DateAdjuster.adjustStringNumber((String.valueOf(mMonth + 1)));
		mDayS = DateAdjuster.adjustStringNumber(String.valueOf(mDay));
		mHourS = DateAdjuster.adjustStringNumber(String.valueOf(mHour));
		mMinuteS = DateAdjuster.adjustStringNumber(String.valueOf(mMinute));
		mSecondS = DateAdjuster.adjustStringNumber(String.valueOf(mSecond));

		today = mDayS + "/" + mMonthS + "/" + mYearS;
		updateDisplay();

		TableLayout table = null;
		((Button) findViewById(R.id.btn_simple))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startGraphActivity(ChartActivity.class);
						Log.d("DietMonitor", "Changing Intent");
					}
				});
	}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	
	// resultCode peut avoir 2 valeurs:
	// Activity.RESULT_CANCELED si bouton back in 2 act
	// Activity.RESULT_OK 
	
	// requestCode depend de la valeur passée dans le startActivityForResult
	if(requestCode==0){
		Log.d("Nombre de Datas: ", String.valueOf(datas.size()));
		extras = data.getExtras();
		if (extras != null) {
			Boolean b1 = extras.isEmpty();
			Boolean b2 = extras.containsKey("do");
			Log.d("L'extra do contient: ", extras.getString("do"));
			if(!extras.isEmpty()){
				if (extras.getString("do").equalsIgnoreCase("edit")) {
		
					rId = extras.getString("id");
					rPoid = extras.getString("poid");
					rMYear = extras.getString("mYear");
					rMMonth = DateAdjuster.adjustStringNumber(String.valueOf(Integer.parseInt(extras.getString("mMonth"))+1));
					rMDay = extras.getString("mDay");
		
					Log.d("Je recoit en retour =>", rId);
					Log.d("Je recoit en retour =>", rPoid);
					Log.d("Je recoit en retour =>", "y "+rMYear);
					Log.d("Je recoit en retour =>", "m "+rMMonth);
					Log.d("Je recoit en retour =>", "d "+rMDay);
					
					serialization(Float.parseFloat(rPoid), rMYear, rMMonth, rMDay, "edit");
				}
				Log.d("Nombre de Datas: ", String.valueOf(datas.size()));
				
			}
		}
	}
	retrieveData();
}


	protected void updateDisplay() {
		mDateDisplay.setText(new StringBuilder().append(mDay).append("-")
				.append(mMonth + 1).append("-").append(mYear).append(" "));
	}

	protected DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			
			//mYearS, mMonthS, mDayS
			mYearS = String.valueOf(year);
			mMonthS = DateAdjuster.adjustStringNumber(String.valueOf(monthOfYear+1));
			mDayS = DateAdjuster.adjustStringNumber(String.valueOf(dayOfMonth));
			updateDisplay();
		}
	};

	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
	}

	private void setUpView() {
		etInput = (EditText) this.findViewById(R.id.editText_input);
		btnAdd = (Button) this.findViewById(R.id.button_add);
		lvItem = (ListView) this.findViewById(R.id.listView_items);
		
		stringArray = new ArrayList<String>();
		stringArray.clear();
		itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArray);
		lvItem.setAdapter(itemAdapter);
		retrieveData();
		
		lvItem.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Intent intent = new Intent(MainActivity.this, activity);
				// intent.putExtra("type", "line");
				// startActivity(intent);

				Integer i = (int) (long) id;

				Data d = new Data();
				d = datas.get(i);
				Intent intent = new Intent(MainActivity.this,
						EditActivity.class);

				Log.d("Je passe =>", String.valueOf(id));
				Log.d("Je passe =>", String.valueOf(d.getWeight()));
				Log.d("Je passe =>", d.getDate().substring(6, 10));
				Log.d("Je passe =>", d.getDate().substring(3, 5));
				Log.d("Je passe =>", d.getDate().substring(0, 2));

				// Bundle extras = new Bundle();
				// extras.putString("id", String.valueOf(id));
				// extras.putString("poid", String.valueOf(d.getWeight()));
				// extras.putString("mYear", d.getDate().substring(6, 10));
				// extras.putString("mMonth", d.getDate().substring(3, 5));
				// extras.putString("mDay", d.getDate().substring(0, 2));

				intent.putExtra("id", String.valueOf(id));
				intent.putExtra("poid", String.valueOf(d.getWeight()));
				intent.putExtra("mYear", d.getDate().substring(6, 10));
				intent.putExtra("mMonth", d.getDate().substring(3, 5));
				intent.putExtra("mDay", d.getDate().substring(0, 2));
				
				Log.d("Objet numero: ", String.valueOf(i));
				deleteItemList(i);
				startActivityForResult(intent, 0);
			}

		});

		// addItemList(item)

		btnAdd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				float poid = Float.parseFloat(etInput.getText().toString());
				serialization(poid, mYearS, mMonthS, mDayS, "add"); // mYearS+"-"+mMonthS+"-"+mDayS
				retrieveData();
			}
		});

		btnHideKB = (Button) findViewById(R.id.btn_hideKB);
		btnHideKB.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mgr.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
			}
		});

		etInput.setOnKeyListener(new View.OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_DEL) {
					etInput.setText("");
				}
				return true;
			}
		});

	}

	private void serialization(float poid, String y, String m, String d, String mess) {
		myNewData = new Data();
		myNewData.setDate(d + "/" + m + "/" + y);
		myNewData.setDateTime(new Date(Integer.parseInt(y), Integer.parseInt(m), Integer.parseInt(d)));
		myNewData.setWeight(poid);
		String dateFormat = y + "-" + m + "-" + d + "T" + mHourS + ":" + mMinuteS + ":" + mSecondS + "Z";
		myNewData.setDateFormat(dateFormat);

		datas.add(myNewData);

//		for (Data data : datas) {
//			Log.i("Datas log after " + mess, String.valueOf(data.getWeight()));
//		}

		Datas myDataS = new Datas();
		myDataS.setDatas(datas);

		Log.d("New datas to save", myDataS.toString());

		serialiser.saveDatas(myDataS, getApplicationContext());
	}

	
	private void retrieveData() {
		try {
			datas = new ArrayList<Data>(serialiser.loadDatas().getDatas());
			
			Collections.sort(datas, new Comparator<Data>() {
				  public int compare(Data o1, Data o2) {
					  if (o1.getDateTime() == null || o2.getDateTime() == null) return 0;
				      return o2.getDateTime().compareTo(o1.getDateTime());
				  }
				});
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (datas == null) {
			datas = new ArrayList<Data>();
		} else {
			stringArray.clear();
			for (Data d : datas) {
				stringArray.add(d.getDate() + " => " + String.valueOf(d.getWeight()));
			}
		}
		itemAdapter.notifyDataSetChanged();
	}

	private void deleteItemList(Integer index) {
		Log.d("Datas before delete: ", String.valueOf(datas.size()));
		Data d = new Data();
		d = datas.get(index);
		datas.remove(d);
		Log.d("Datas after delete: ", String.valueOf(datas.size()));
		Datas myDataS = new Datas();
		myDataS.setDatas(datas);
		serialiser.saveDatas(myDataS, getApplicationContext());
		retrieveData();
	}

	protected boolean isInputValid(EditText etInput2) {

		int str = etInput2.getText().toString().trim().length();

		if (str < 1) {
			etInput2.setError("Please Enter Item");
			return false;
		} else {
			return true;
		}

	}

	private void startGraphActivity(Class<? extends Activity> activity) {
		Intent intent = new Intent(MainActivity.this, activity);
		intent.putExtra("type", "line");
		startActivity(intent);
	}
}
