package ar.com.marcelomingrone.vericast.reports.controllers;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.marcelomingrone.vericast.reports.controllers.Utils.Params;
import ar.com.marcelomingrone.vericast.reports.dao.UserDao;
import ar.com.marcelomingrone.vericast.reports.model.DataTablesResponse;
import ar.com.marcelomingrone.vericast.reports.model.User;


@Controller
@RequestMapping("/admin/user")
public class UserController {
	
	private static Log log = LogFactory.getLog(UserController.class);
	
	@Autowired
	private UserDao dao;
	
	@Resource
	private MessageSource msgSource;
	
	
	@RequestMapping("/list") 
	public String list(ModelMap model) {
		return "admin/user_list";
	}
	
	@ResponseBody
	@RequestMapping("/list_ajax")
	public DataTablesResponse list(HttpServletRequest request, Locale locale) {		
		
		Map<Params, Object> params = Utils.getParametrosDatatables(request);
		
		String orderField = User.getOrderingField( 
				Utils.getInt(request.getParameter("iSortCol_0"), 0) );
		
		
		List<User> users = dao.getAllPaginatedAndFiltered(
				(int)params.get(Params.INICIO),
				(int)params.get(Params.CANTIDAD_RESULTADOS),
				orderField,
				(String)params.get(Params.DIRECCION_ORDENAMIENTO),
				(String)params.get(Params.FILTRO));
		
		long count = dao.getCount(
				(String)params.get(Params.FILTRO));
		
		long total = count;
		if (!StringUtils.isEmpty((String)params.get(Params.FILTRO))) {
			total = dao.getCount(null);
		}
		
		DataTablesResponse resultado = new DataTablesResponse(
				users, request.getParameter("sEcho"), total, count, msgSource, locale);
		
		return resultado;
	}
	
	@RequestMapping("/create")
	public String crear(ModelMap model) {
		
		User user = new User();
		return prepareForm(user, model);
	}
	
	@RequestMapping("/update")
	public String update(@RequestParam("id") Long id, ModelMap model) {
		
		User user = dao.getById(id);
		return prepareForm(user, model);
	}

	private String prepareForm(User user, ModelMap model) {
		
		model.addAttribute("selectedLanguage", user != null ? user.getLanguage() : null);
		
		model.addAttribute("user", user);
		return "admin/user_edit";
	}
	
	@RequestMapping(value="/accept", method={RequestMethod.POST})
	public String acceptUpdate(@Valid User user,
			BindingResult result, ModelMap model, Locale locale, 
			@RequestParam(value="isAdmin", required=false, defaultValue="false") boolean isAdmin){
		
		if (!result.hasErrors()) {
			
			try {
				
				String passMessage = "";
				
				if (user.getId() == null) {
					String password = Utils.createRandomString(8);
					user.setPassword(Utils.encryptPassword(password));
					passMessage = "<br/>" + msgSource.getMessage(
							"new.password.set", new String[]{password}, locale);
					
				} else {
					User existing = dao.getById(user.getId());
					user.setPassword(existing.getPassword());
				}
				
				dao.save(user);
				model.addAttribute("msg", msgSource.getMessage("user.saved", null, locale) + passMessage);
				
			} catch (Exception e) {
				log.error("Se produjo un error guardando el usuario.", e);
				model.addAttribute("msg", msgSource.getMessage("user.save.error", null, locale));
			}
			
			return list(model);
		}
		
		return prepareForm(user, model);
	}
	
	
	@RequestMapping(value="/delete", method={RequestMethod.POST})
	public String delete(@RequestParam("id") Long id, ModelMap model, Locale locale) {
		
		try {
			dao.delete(id);
			model.addAttribute("msg", msgSource.getMessage("user.deleted", null, locale));
			
		} catch (Exception e) {
			log.error("Error al eliminar el usuario.", e);
			model.addAttribute("msg", msgSource.getMessage("user.delete.error", null, locale));
		}
		return list(model);
	}
}
