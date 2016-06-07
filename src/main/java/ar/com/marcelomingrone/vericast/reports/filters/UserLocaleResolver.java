package ar.com.marcelomingrone.vericast.reports.filters;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import ar.com.marcelomingrone.vericast.reports.dao.UserDao;
import ar.com.marcelomingrone.vericast.reports.model.User;

public class UserLocaleResolver extends SessionLocaleResolver {
	
	private static Log log = LogFactory.getLog(UserLocaleResolver.class);
	
	@Autowired
	private UserDao userDao;

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		
		Locale locale = (Locale) request.getSession().getAttribute(LOCALE_SESSION_ATTRIBUTE_NAME);
		
		if (locale == null) {
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null) {
				
				User user = userDao.getByUsername(auth.getName());
				if (user != null) {
					
					locale = new Locale(user.getLanguage());
					request.getSession().setAttribute(LOCALE_SESSION_ATTRIBUTE_NAME, locale);
					log.debug("Seteando locale " + locale);
				}
			}
		}
		
		return locale;
	}
	
	
}
