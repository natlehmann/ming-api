package ar.com.marcelomingrone.vericast.reports.controllers;

import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {
	
	@Resource(name = "messageSource")
    private MessageSource messageSource;

	@RequestMapping("/no-autorizado")
	public String noAutorizado(Locale locale, ModelMap model) {
		
		model.addAttribute("msg", messageSource.getMessage("user.not.authorized", null, locale));
		model.addAttribute("titulo", messageSource.getMessage("unauthorized", null, locale));
		return "error";
	}
	
	@RequestMapping("/no-existe")
	public String noExiste(Locale locale, ModelMap model) {
		model.addAttribute("msg", messageSource.getMessage("page.does.not.exist", null, locale));
		model.addAttribute("titulo", messageSource.getMessage("not.found", null, locale));
		return "error";
	}

}
