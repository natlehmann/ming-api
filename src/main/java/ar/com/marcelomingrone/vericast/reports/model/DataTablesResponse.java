package ar.com.marcelomingrone.vericast.reports.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;

public class DataTablesResponse {
	
	private static Log log = LogFactory.getLog(DataTablesResponse.class);
	
	private Long sEcho;
	
	private long iTotalRecords;
	
	private long iTotalDisplayRecords;
	
	private List<List<String>> aaData;
	
	
	public DataTablesResponse(List<? extends Listable> lista, String sEcho, long cantidadTotal,
			long cantidadFiltrada, MessageSource msgSource, Locale locale){
		
		this(lista, sEcho, cantidadTotal, cantidadFiltrada, msgSource, locale, null);
	}
	
	
	public DataTablesResponse(List<? extends Listable> lista, String sEcho, long cantidadTotal,
			long cantidadFiltrada, MessageSource msgSource, Locale locale, DataStrategy strategy) {
		
		if (!StringUtils.isEmpty(sEcho)) {
			try {
				this.sEcho = Long.parseLong(sEcho);
			
			} catch (NumberFormatException e) {
				log.error("Error al convertir a long a " + sEcho, e);
			}
			
		} else {
			this.sEcho = 1L;
		}
		
		this.iTotalRecords = cantidadTotal;
		this.iTotalDisplayRecords = cantidadFiltrada;
		
		this.aaData = new LinkedList<>();
		for (Listable entidad : lista) {
			
			if (strategy != null) {
				aaData.add(strategy.getData(entidad, msgSource, locale));
				
			} else {
				aaData.add(entidad.getFieldsAsList(msgSource, locale));
			}
		}
		
	}

	public Long getsEcho() {
		return sEcho;
	}

	public void setsEcho(Long sEcho) {
		this.sEcho = sEcho;
	}

	public long getiTotalRecords() {
		return iTotalRecords;
	}

	public void setiTotalRecords(long iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public long getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public List<List<String>> getAaData() {
		return aaData;
	}

	public void setAaData(List<List<String>> aaData) {
		this.aaData = aaData;
	}

}
