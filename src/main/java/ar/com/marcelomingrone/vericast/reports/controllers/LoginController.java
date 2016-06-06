package ar.com.marcelomingrone.vericast.reports.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

	@RequestMapping("/login")
	public String login() {
		return "admin/login";
	}
	
	@RequestMapping("/login-error")
	public String loginError(ModelMap model) {
		
		model.addAttribute("msg", "El usuario o la contraseña no son válidos. Intente nuevamente por favor.");
		return "admin/login";
	}

}
