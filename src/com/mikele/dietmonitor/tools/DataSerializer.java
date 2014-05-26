package com.mikele.dietmonitor.tools;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.Context;
import android.util.Log;

import com.mikele.dietmonitor.R;
import com.mikele.dietmonitor.modele.Data;
import com.mikele.dietmonitor.modele.Datas;

public class DataSerializer {
	private static final String FILE_NAME = "dietmonitor.xml";
	private static DataSerializer instance;
	private Context ctx;
	static SimpleDateFormat atomDateFormat = null;
	public static final Locale APP_LOCALE = Locale.FRANCE;
	
	private DataSerializer(Context context) {
		ctx = context;
	}
	
	public static DataSerializer getInstance(Context context) {
		if (instance == null) {
			instance = new DataSerializer(context);
		}
		return instance;
	}

	public Datas loadDatas() throws Exception {
		Datas datas = null;
		InputStream source = null;
		Serializer serializer = new Persister();

		source = ctx.openFileInput(FILE_NAME);

		datas = serializer.read(Datas.class, source);
		ArrayList<Data> data = new ArrayList<Data>();
		data.addAll(datas.getDatas());
		
		for(Data d : data){
			Date date = dateFromAtom(d.getDateFormat());
			d.setDateTime(date);
		}
		
		
		Collections.sort(data, new Comparator<Data>() {
		  public int compare(Data o1, Data o2) {
			  if (o1.getDateTime() == null || o2.getDateTime() == null)
			        return 0;
			      return o2.getDateTime().compareTo(o1.getDateTime()); //Inverser o2 et o1 pour inverser le tri
		  }
		});
		
		datas.setDatas(data);
		source.close();
		return datas;
	}

	public void saveDatas(Datas datas, Context c) {
		Serializer serializer = new Persister();
		try {
			FileOutputStream fileOS = c.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
			serializer.write(datas, fileOS);
			fileOS.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Date dateFromAtom(String atomDate) throws ParseException {
		if(atomDate == null || atomDate.equals("")) {
			return null;
		}
		if (atomDateFormat == null) {
			atomDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", APP_LOCALE);
			atomDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		}
		try {
			return atomDateFormat.parse(atomDate);
		} catch (ParseException e) {
			Log.e("DateUtils", "Date parsing error", e);
			throw e;
		}
	}
}
