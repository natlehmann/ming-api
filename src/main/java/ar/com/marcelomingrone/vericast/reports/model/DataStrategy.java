package ar.com.marcelomingrone.vericast.reports.model;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;

public interface DataStrategy {
	
	List<String> getData(Listable listable, MessageSource msgSource, Locale locale);

}
