package ar.com.marcelomingrone.vericast.reports.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import antlr.Utils;
import ar.com.marcelomingrone.vericast.reports.model.TimePeriod;

@Controller
@RequestMapping("/report/byChannel")
public class ReportByChannelController {
	
	private static Log log = LogFactory.getLog(ReportByChannelController.class);
	
	public static final String EXPORT_ACTION = "Generar reporte";
	public static final String SAVE_ACTION = "Archivar reporte";
	
	@InitBinder
	private void dateBinder(WebDataBinder binder) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
	    binder.registerCustomEditor(Date.class, editor);
	}
	
	
	@RequestMapping("/filters")
	public ModelAndView initReportFilters(ModelMap model) {
		
		model.addAttribute("timePeriods", TimePeriod.values());	
		model.addAttribute("endDate", new Date());
		
		return new ModelAndView("report/byChannel/filters", model);
	}
	
	/*
	@RequestMapping("/create")
	public ModelAndView createReport(@RequestParam("country") Long countryId, 
			@RequestParam("year") Integer year,
			@RequestParam(value="weekFrom", required=false, defaultValue="") Integer weekFrom,
			@RequestParam(value="weekTo", required=false, defaultValue="") Integer weekTo,
			@RequestParam(value="month", required=false, defaultValue="") Integer month,
			@RequestParam("right") Long rightId,
			@RequestParam(value="source", required=false, defaultValue="") Long sourceId,
			@RequestParam("action") String action,
			ModelMap model, HttpSession session) {

		setConversationParameters(model, year, countryId, weekFrom, weekTo, rightId, sourceId, month);
		
		if (!validFilters(model, weekFrom, weekTo, month)) {
			return initReportFilters(model);
		}
		
		if (action.equals(EXPORT_ACTION)) {
			return getExcel(countryId, year, weekFrom, weekTo, 
					month, rightId, sourceId, model, session);
		}
		
		if (action.equals(SAVE_ACTION)) {
			return saveReport(model, session);
		}
		
		return null;
	}


	private ModelAndView saveReport(ModelMap model, HttpSession session) {
		
		SummaryReport report = (SummaryReport) session.getAttribute(
				Utils.SessionParams.ACTIVE_REPORT.toString());
		
		if (report != null) {
		
			String msg = service.saveReport(report);
			
			setConversationParameters(model, report.getYear(), report.getCountry().getId(),
					report.getWeekFrom(), report.getWeekTo(), report.getRight().getId(),
					report.getFilteredBySource() != null ? report.getFilteredBySource().getId() :  null, 
					report.getMonth());
			model.put("msg", msg);
			
			session.removeAttribute(Utils.SessionParams.ACTIVE_REPORT.toString());
			
		} else {
			model.put("msg", "Genere un reporte a partir de estos filtros.");
		}
		
		return initReportFilters(model);
	}


	private void setConversationParameters(ModelMap model, Integer year, Long countryId, Integer weekFrom,
			Integer weekTo, Long rightId, Long sourceId, Integer month) {
		model.put("selectedYear", year);
		model.put("selectedCountry", countryId);
		model.put("selectedWeekFrom", weekFrom);
		model.put("selectedWeekTo", weekTo);
		model.put("selectedRight", rightId);
		model.put("selectedMonth", month);
		model.put("selectedSource", sourceId);		
		model.put("selectedIsMonthly", month != null);
	}


	public ModelAndView getExcel(Long countryId, Integer year, Integer weekFrom,
			Integer weekTo, Integer month, Long rightId,
			Long sourceId, ModelMap model, HttpSession session) {

		SummaryReport report = service.getSummaryReport(
				countryId, year, weekFrom, weekTo, month, rightId, sourceId);
		
		session.setAttribute(Utils.SessionParams.ACTIVE_REPORT.toString(), report);
		model.put("summaryReport", report);
		return new ModelAndView("chartSummaryExcelView", model);
	}
	
	@RequestMapping("/isReady")
	@ResponseBody
	public boolean isReady(HttpSession session) {
		
		return session.getAttribute(Utils.SessionParams.ACTIVE_REPORT.toString()) != null;
	}

	
	@ResponseBody
	@RequestMapping("/weekly/list_ajax")
	public DataTablesResponse listWeekly(HttpServletRequest request) {
		
		
		Map<Params, Object> params = Utils.getParametrosDatatables(request);
		
		String campoOrdenamiento = WeeklyReport.getOrderingField( 
				Utils.getInt(request.getParameter("iSortCol_0"), 0) );
		
		
		List<SummaryReport> reports = service.getWeeklyReportsPaginatedAndFiltered(
				(int)params.get(Params.INICIO),
				(int)params.get(Params.CANTIDAD_RESULTADOS),
				(String)params.get(Params.FILTRO),
				campoOrdenamiento,
				(String)params.get(Params.DIRECCION_ORDENAMIENTO));
		
		long totalFiltrados = service.getWeeklyReportsCount(
				(String)params.get(Params.FILTRO));
		
		long total = totalFiltrados;
		if (!StringUtils.isEmpty((String)params.get(Params.FILTRO))) {
			total = service.getWeeklyReportsCount(null);
		}
		
		DataTablesResponse resultado = new DataTablesResponse(
				reports, request.getParameter("sEcho"), total, totalFiltrados);
		
		return resultado;
	}
	
	
	@RequestMapping(value="/weekly/delete", method={RequestMethod.POST})
	public ModelAndView deleteWeeklyReport(@RequestParam("id") Long id, ModelMap model) {
		
		try {
			service.deleteWeeklyReport(id);
			model.addAttribute("msg", "El reporte semanal se ha eliminado con éxito.");
			
		} catch (Exception e) {
			log.error("Error al eliminar reporte semanal.", e);
			model.addAttribute("msg", "No se ha podido eliminar el reporte semanal. " 
					+ "Si el problema persiste consulte al administrador del sistema.");
		}
		return initReportFilters(model);
	}
	
	
	@ResponseBody
	@RequestMapping("/monthly/list_ajax")
	public DataTablesResponse listMonthly(HttpServletRequest request) {
		
		
		Map<Params, Object> params = Utils.getParametrosDatatables(request);
		
		String campoOrdenamiento = MonthlyReport.getOrderingField( 
				Utils.getInt(request.getParameter("iSortCol_0"), 0) );
		
		
		List<SummaryReport> reports = service.getMonthlyReportsPaginatedAndFiltered(
				(int)params.get(Params.INICIO),
				(int)params.get(Params.CANTIDAD_RESULTADOS),
				(String)params.get(Params.FILTRO),
				campoOrdenamiento,
				(String)params.get(Params.DIRECCION_ORDENAMIENTO));
		
		long totalFiltrados = service.getMonthlyReportsCount(
				(String)params.get(Params.FILTRO));
		
		long total = totalFiltrados;
		if (!StringUtils.isEmpty((String)params.get(Params.FILTRO))) {
			total = service.getMonthlyReportsCount(null);
		}
		
		DataTablesResponse resultado = new DataTablesResponse(
				reports, request.getParameter("sEcho"), total, totalFiltrados);
		
		return resultado;
	}
	
	
	@RequestMapping(value="/monthly/delete", method={RequestMethod.POST})
	public ModelAndView deleteMonthlyReport(@RequestParam("id") Long id, ModelMap model) {
		
		try {
			service.deleteMonthlyReport(id);
			model.addAttribute("msg", "El reporte mensual se ha eliminado con éxito.");
			
		} catch (Exception e) {
			log.error("Error al eliminar reporte mensual.", e);
			model.addAttribute("msg", "No se ha podido eliminar el reporte mensual. " 
					+ "Si el problema persiste consulte al administrador del sistema.");
		}
		return initReportFilters(model);
	}
	
	
	@ResponseBody
	@RequestMapping("/exists")
	public String checkIfReportExists(@RequestParam("country") Long countryId, 
			@RequestParam("year") Integer year,
			@RequestParam(value="weekFrom", required=false, defaultValue="") Integer weekFrom,
			@RequestParam(value="weekTo", required=false, defaultValue="") Integer weekTo,
			@RequestParam(value="month", required=false, defaultValue="") Integer month,
			@RequestParam("right") Long rightId,
			@RequestParam(value="source", required=false, defaultValue="") Long sourceId,
			@RequestParam("action") String action,
			ModelMap model, HttpSession session) {
		
		SummaryReport report = service.getExistingReport(
				countryId, year, weekFrom, weekTo, month, rightId, sourceId);
		
		if (report == null) {
			return "";
			
		} else {
			return "Ya existe un reporte correspondiente al mismo período (ID: " + report.getId() 
					+ "). ¿Está seguro que desea reemplazarlo?";
		}
		
	}
	
	
	
	@Transactional
	@RequestMapping(value="/csv")
	public ModelAndView getWeeklyCsvReport(@RequestParam("id") Long id, ModelMap model) {
		
		SummaryReport report = null;
		
		try {
			report = service.getWeeklyCsvReport(id);
			model.put("summaryReport", report);
			
		} catch (BmatSourceUriException e) {
			model.addAttribute("msg",e.getMessage());
			return initReportFilters(model);
		}
		
		return new ModelAndView("chartSummaryCsvView", model);
	}
	

	
	*/
}