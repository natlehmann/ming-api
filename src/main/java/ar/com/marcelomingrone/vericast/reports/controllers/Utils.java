package ar.com.marcelomingrone.vericast.reports.controllers;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.StringUtils;

import ar.com.marcelomingrone.vericast.reports.model.RoleNames;

public class Utils {

	private static Log log = LogFactory.getLog(Utils.class);
	
	private static NumberFormat formateadorNumeros;
	
	public static enum Params {
		INICIO,
		CANTIDAD_RESULTADOS,
		FILTRO,
		DIRECCION_ORDENAMIENTO;
	}
	
	
	public static enum SessionParams {
		ACTIVE_REPORT;
	}
	
	
	public static int getInt(String valor, int valorDefault) {
		
		int resultado = valorDefault;
		
		if (!StringUtils.isEmpty(valor)) {
			
			try {
				resultado = Integer.parseInt(valor);
				
			} catch(NumberFormatException e) {
				log.error("Error de conversion a enteros", e);
			}
		}
		
		return resultado;
	}

	public static String getDireccionOrdenamiento(String valor) {
		
		if (!StringUtils.isEmpty(valor)) {
			if (valor.equalsIgnoreCase("asc")) {
				return "asc";
				
			} else {
				return "desc";
			}
		}
		
		return "asc";
	}
	
	public static Map<Params, Object> getParametrosDatatables(HttpServletRequest request) {
		
		int inicio = getInt(request.getParameter("iDisplayStart"), 0);
		int cantidadResultados = getInt(request.getParameter("iDisplayLength"), 10);
		
		String filtro = request.getParameter("sSearch");
		
		String direccionOrdenamiento = getDireccionOrdenamiento(
				request.getParameter("sSortDir_0"));
		
		Map<Params, Object> params = new HashMap<Utils.Params, Object>();
		
		params.put(Params.INICIO, inicio);
		params.put(Params.CANTIDAD_RESULTADOS, cantidadResultados);
		params.put(Params.FILTRO, filtro);
		params.put(Params.DIRECCION_ORDENAMIENTO, direccionOrdenamiento);
		
		return params;
	}
	
	public static String formatear(double numero) {
		
		if (formateadorNumeros == null) {
			formateadorNumeros = NumberFormat.getInstance(new Locale("es"));
			formateadorNumeros.setMaximumFractionDigits(2);
		}
		
		return formateadorNumeros.format(numero);
	}
	
	
	public static boolean isCurrentUserAdministrator() {

		SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return false;
        }

        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return false;
        }

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if (RoleNames.ADMINISTRATOR.toString().equals(auth.getAuthority())) {
                return true;
            }
        }

        return false;
    }

	public static String createRandomString(int charCount) {
		
		StringBuffer buffer = new StringBuffer();
		
		for (int i = 0; i < charCount; i++) {
			buffer.append((char)getRandomAsciiNumber());
		}
		
		return buffer.toString();
	}

	private static int getRandomAsciiNumber() {
		Integer number = null;
		while (number == null) {
			
			number = (int)(Math.round(Math.random() * 57) + 65);
			
			if (number.intValue() >= 91 && number.intValue() <= 96) {
				number = null;
			}
		}
		
		return number;
	}

	public static String encryptPassword(String password) {
		
		StandardPasswordEncoder encoder = new StandardPasswordEncoder();
		return encoder.encode(password);
	}

}
