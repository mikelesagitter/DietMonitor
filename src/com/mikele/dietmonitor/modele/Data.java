package com.mikele.dietmonitor.modele;

import java.util.Date;

import org.simpleframework.xml.Attribute;

public class Data implements Comparable<Data> {

	@Attribute
	private String date;
	
	@Attribute
	private float weight;
	
	@Attribute
	private String dateFormat;
	
	@Attribute
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private Date dateTime;
	
	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public int compareTo(Data o) {
		if (getDateTime() == null || o.getDateTime() == null)
		      return 0;
		    return getDateTime().compareTo(o.getDateTime());
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String date) {
		this.dateFormat = date;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

}
