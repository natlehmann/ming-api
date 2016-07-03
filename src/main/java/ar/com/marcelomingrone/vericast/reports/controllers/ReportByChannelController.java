package ar.com.marcelomingrone.vericast.reports.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ar.com.marcelomingrone.vericast.reports.controllers.Utils.Params;
import ar.com.marcelomingrone.vericast.reports.model.DataTablesResponse;
import ar.com.marcelomingrone.vericast.reports.model.LocalizedException;
import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.TimePeriod;
import ar.com.marcelomingrone.vericast.reports.services.ReportService;

@Controller
@RequestMapping("/report/byChannel")
public class ReportByChannelController {
	
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(ReportByChannelController.class);
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ReportService service;
	
	@InitBinder
	private void dateBinder(WebDataBinder binder) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
	    binder.registerCustomEditor(Date.class, editor);
	}
	
	
	@RequestMapping("/filters")
	public ModelAndView initReportFilters(ModelMap model) {
		
		model.addAttribute("timePeriods", TimePeriod.values());	
		if (model.get("selectedPeriod") == null) {
			model.put("selectedPeriod", TimePeriod.WEEK);
		}
		
		return new ModelAndView("report/byChannel/filters", model);
	}
	
	@RequestMapping("/create")
	@ResponseBody
	public String createReport(
			@RequestParam(value="timePeriod", required=true) String timePeriod, 
			@RequestParam(value="endDate", required=true) Date endDate, ModelMap model, 
			HttpSession session, Locale locale) {
		
		if (StringUtils.isEmpty(timePeriod) || endDate == null) {
			return messageSource.getMessage("error.missing.filters", null, locale);
		}

		setConversationParameters(model, timePeriod, endDate);
		
		Report report = service.buildReport(timePeriod, endDate);
		session.setAttribute(Utils.SessionParams.ACTIVE_REPORT.toString(), report);
		
		service.buildPlaycountsByChannel(report, timePeriod, endDate);
		
		return messageSource.getMessage("report.ready", null, locale);

	}
	
	@RequestMapping("/download")
	public ModelAndView downloadReport(ModelMap model, 
			HttpSession session, Locale locale) {
		
		Report report = (Report) session.getAttribute(Utils.SessionParams.ACTIVE_REPORT.toString());
		report = service.getReportOrderedByPlaycounts(report.getId());
		
		model.put("report", report);
		return new ModelAndView("playcountsExcelView", model);
	
	}
	
	
	@RequestMapping("/approve")
	public ModelAndView approveReport(ModelMap model, Locale locale, 
			@RequestParam("id")long id, @RequestParam("user")String user) {
		
		try {
			service.approveReport(id, user);
			model.put("msg", messageSource.getMessage("report.approved", null, locale));
			
		} catch (LocalizedException e){
			model.put("msg", messageSource.getMessage(e.getCode(), null, locale));
		}
		
		return initReportFilters(model);
	}
	
	
	@RequestMapping("/delete")
	public ModelAndView rejectReport(ModelMap model, Locale locale, 
			@RequestParam("id")long id, @RequestParam("user")String user) {
		
		try {
			service.rejectReport(id, user);
			model.put("msg", messageSource.getMessage("report.rejected", null, locale));
			
		} catch (LocalizedException e){
			model.put("msg", messageSource.getMessage(e.getCode(), null, locale));
		}
		
		return initReportFilters(model);
	}


	private void setConversationParameters(ModelMap model, String timePeriod, Date endDate) {
		model.put("selectedPeriod", timePeriod);
		model.put("selectedEndDate", endDate);
	}

	
	@ResponseBody
	@RequestMapping("/list_ajax")
	public DataTablesResponse list(HttpServletRequest request, Locale locale) {
		
		
		Map<Params, Object> params = Utils.getParametrosDatatables(request);
		
		String campoOrdenamiento = Report.getOrderingField( 
				Utils.getInt(request.getParameter("iSortCol_0"), 0) );
		
		
		List<Report> reports = service.getReportsForCurrentUser(
				(int)params.get(Params.INICIO),
				(int)params.get(Params.CANTIDAD_RESULTADOS),
				(String)params.get(Params.FILTRO),
				campoOrdenamiento,
				(String)params.get(Params.DIRECCION_ORDENAMIENTO));
		
		long totalFiltrados = service.getReportsForCurrentUserCount(
				(String)params.get(Params.FILTRO));
		
		long total = totalFiltrados;
		if (!StringUtils.isEmpty((String)params.get(Params.FILTRO))) {
			total = service.getReportsForCurrentUserCount(null);
		}
		
		DataTablesResponse resultado = new DataTablesResponse(reports, 
				request.getParameter("sEcho"), total, totalFiltrados, messageSource, locale);
		
		return resultado;
	}
	
	
	@ResponseBody
	@RequestMapping("/exists")
	public String checkIfReportExists(@RequestParam("timePeriod") String timePeriod, 
			@RequestParam("endDate") Date endDate, ModelMap model, HttpSession session, Locale locale) {
		
		if (StringUtils.isEmpty(timePeriod) || endDate == null) {
			return messageSource.getMessage("error.missing.filters", null, locale);
		}
		
		if (service.userCanBuildReport()) {
			
			Report existing = service.getSameReport(timePeriod, endDate);
			if (existing != null) {
				return messageSource.getMessage("error.same.report.exists", null, locale);
			}
			
		} else {
			return messageSource.getMessage("error.report.in.process", null, locale);
		}
		
		return "";
		
	}
	
	
	/*
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
