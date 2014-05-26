package com.mikele.dietmonitor.modele;

import java.util.Collection;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Datas {
	
	@ElementList(inline = true)
	private Collection<Data> datas;

	public Collection<Data> getDatas() {
		return datas;
	}

	public void setDatas(Collection<Data> datas) {
		this.datas = datas;
	}
	
	@Override
	public String toString(){
		String str = null;
		for(Data d : datas){
			str = String.valueOf(d.getDate()+" - "+d.getDateFormat()+" - "+d.getWeight()+" - "+d.getDateTime().toString());
		}
		return str;
		
	}
}


