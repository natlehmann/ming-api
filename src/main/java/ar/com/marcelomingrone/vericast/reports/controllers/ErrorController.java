package ar.com.marcelomingrone.vericast.reports.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

	@RequestMapping("/no-autorizado")
	public String noAutorizado(ModelMap model) {
		model.addAttribute("msg", "El usuario no está autorizado para ver este contenido.");
		model.addAttribute("titulo", "Sin permisos");
		return "error";
	}
	
	@RequestMapping("/no-existe")
	public String noExiste(ModelMap model) {
		model.addAttribute("msg", "Esta página no existe.");
		model.addAttribute("titulo", "No existe");
		return "error";
	}

}
