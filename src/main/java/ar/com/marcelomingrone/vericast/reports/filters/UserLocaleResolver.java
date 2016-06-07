package ar.com.marcelomingrone.vericast.reports.filters;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

public class UserLocaleResolver extends SessionLocaleResolver {

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		
		Locale locale = (Locale) request.getSession().getAttribute(LOCALE_SESSION_ATTRIBUTE_NAME);
		
		if (locale == null) {
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null) {
				
				System.out.println("----------------------------------- " + auth.getName());
				locale = new Locale("en");
				request.getSession().setAttribute(LOCALE_SESSION_ATTRIBUTE_NAME, locale);
				System.out.println("SETEE EL " + locale);
			}
		}
		
		return locale;
	}
	
	
}
