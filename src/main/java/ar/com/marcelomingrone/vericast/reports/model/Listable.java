package ar.com.marcelomingrone.vericast.reports.model;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;

public interface Listable {
	
	List<String> getFieldsAsList(MessageSource msgSource, Locale locale);

}
