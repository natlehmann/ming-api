package ar.com.marcelomingrone.vericast.reports.controllers;

import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	@Resource(name = "messageSource")
    private MessageSource messageSource;

	@RequestMapping("/login")
	public String login() {
		return "admin/login";
	}
	
	@RequestMapping("/login-error")
	public String loginError(Locale locale, ModelMap model) {
		
		model.addAttribute("msg", messageSource.getMessage("login.invalid", null, locale));
		return "admin/login";
	}

}
