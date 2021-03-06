package ar.com.marcelomingrone.vericast.reports.services;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.marcelomingrone.vericast.reports.controllers.Utils;
import ar.com.marcelomingrone.vericast.reports.dao.ChannelDao;
import ar.com.marcelomingrone.vericast.reports.dao.PlaycountByChannelDao;
import ar.com.marcelomingrone.vericast.reports.dao.ReportDao;
import ar.com.marcelomingrone.vericast.reports.dao.ReportItemDao;
import ar.com.marcelomingrone.vericast.reports.dao.UserDao;
import ar.com.marcelomingrone.vericast.reports.model.InvalidStateException;
import ar.com.marcelomingrone.vericast.reports.model.LocalizedException;
import ar.com.marcelomingrone.vericast.reports.model.NoReportException;
import ar.com.marcelomingrone.vericast.reports.model.PlaycountByChannel;
import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.Report.State;
import ar.com.marcelomingrone.vericast.reports.model.ReportItem;
import ar.com.marcelomingrone.vericast.reports.model.User;
import ar.com.marcelomingrone.vericast.reports.model.UserNotAuthorizedException;
import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;
import ar.com.marcelomingrone.vericast.reports.model.dto.Track;

@Service
public class ReportService {
	
	private static Log log = LogFactory.getLog(ReportService.class);
	
	
	@Autowired
	private VericastApiDelegate api;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ReportDao reportDao;
	
	@Autowired
	private ReportItemDao reportItemDao;
	
	@Autowired
	private PlaycountByChannelDao playcountByChannelDao;
	
	@Autowired
	private ChannelDao channelDao;
	
	@Autowired
	private SendMailService sendMailService;
	

	@Transactional
	public void buildPlaycountsByChannel(Report report, String timePeriod, Date endDate) {
		
		try {
			log.info("Recuperando lista de canales para usuario " + report.getOwner());
	        List<Channel> channelList = api.getChannelList(report.getOwner());
	        
	        for (Channel channel : channelList) {
	        	
	        	log.info("Recuperando lista de track para el canal " + channel.getName());
	        	List<Track> trackList = api.getTracksByChannel(channel.getKeyname(), 
	        			report.getOwner(), endDate, timePeriod);
	        	
	        	for (Track track : trackList) {
	        		
	        		ReportItem item = reportItemDao.getByTrackId(report, track.getId());
	        		if (item == null) {
	        			item = new ReportItem();
	        			item.setArtistName(track.getArtist().getName());
	        			item.setLabelName(track.getLabel().getName());
	        			item.setTrackId(track.getId());
	        			item.setTrackName(track.getName());
	        			report.addItem(item);
	        			item = reportItemDao.save(item);
	        		}
	        		
	        		channel = channelDao.findOrSave(channel);
	        		
	        		PlaycountByChannel playcount = new PlaycountByChannel(channel, track.getPlaycount());
	        		item.addPlaycount(playcount);
	        		playcountByChannelDao.save(playcount);
	        	}
	        }
	        
	        calculateTotalPlaycounts(report);
	        
	        Report simplified = reportDao.getById(report.getId());
	        simplified.setState(State.FINISHED);
	        reportDao.save(simplified);
	        
	        sendMailService.sendReport( getReportOrderedByPlaycounts(report.getId()) );
	        
		} catch (Exception e) {
			
			// if it fails, delete the report
			log.info("Se elimina el reporte del usuario " + report.getOwner().getUsername());
			reportDao.delete(report.getId());
			
			sendMailService.sendErrorReport(report, e);
		}
        
	}
	


	private void calculateTotalPlaycounts(Report report) {
		
		Report withItems = reportDao.getByIdWithItems(report.getId());
		
		if (withItems.getItems() != null) {
			
			for (ReportItem item : withItems.getItems()){
				
				List<PlaycountByChannel> playcounts = playcountByChannelDao.getByReportItem(item);
				long total = 0;
				
				for (PlaycountByChannel playcount : playcounts) {
					total += playcount.getPlaycount();
				}
				
				item.setTotalPlayCount(total);
				reportItemDao.save(item);
			}
		}
		
	}


	public Report getReportOrderedByPlaycounts(Long id) {
		return reportDao.getByIdWithItemsAndPlaycounts(id);
	}

	public Report buildReport(String timePeriod, Date endDate) {
		
		User currentUser = userDao.getCurrentUser();
		
		Report report = new Report();
        report.setEndDate(endDate);
        report.setOwner(currentUser);
        report.setTimePeriod(timePeriod);
        report.setState(State.IN_PROCESS);
        
        report = reportDao.save(report);
        return report;
	}
	
	
	public Report approveReport(long id, String user) throws LocalizedException {
		
		Report report = reportDao.getById(id);
		
		if (report == null) {
			throw new NoReportException();
		}
		
		if (!report.getOwner().getUsername().equalsIgnoreCase(user) 
				&& !Utils.isCurrentUserAdministrator()){
			throw new UserNotAuthorizedException();
		}
		
		if (report.getState() == State.IN_PROCESS) {
			throw new InvalidStateException();
		}
		
		if (report.getState() == State.FINISHED) {
			report.setState(State.APPROVED);
			reportDao.save(report);
		}
		
		return report;
	}
	
	
	public void rejectReport(long id, String user) throws LocalizedException {
		
		Report report = reportDao.getById(id);
		
		if (report == null) {
			throw new NoReportException();
		}
		
		if (!report.getOwner().getUsername().equalsIgnoreCase(user) 
				&& !Utils.isCurrentUserAdministrator()){
			throw new UserNotAuthorizedException();
		}
		
		if (report.getState() == State.IN_PROCESS) {
			throw new InvalidStateException();
		}
		
		reportDao.delete(id);
	}
	
	
	
	public boolean userCanBuildReport() {
		
		User currentUser = userDao.getCurrentUser();
		List<Report> reports = reportDao.getUnfinishedReports(currentUser);
		
		return reports.isEmpty();
	}
	
	
	public Report getSameReport(String timePeriod, Date endDate) {
		
		User currentUser = userDao.getCurrentUser();
		return reportDao.getReport(currentUser, timePeriod, endDate);
	}
	
	
	
	public void setVericastApiDelegate(VericastApiDelegate api) {
		this.api = api;
	}
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setReportDao(ReportDao reportDao) {
		this.reportDao = reportDao;
	}
	
	public void setReportItemDao(ReportItemDao reportItemDao) {
		this.reportItemDao = reportItemDao;
	}
	
	public void setPlaycountByChannelDao(
			PlaycountByChannelDao playcountByChannelDao) {
		this.playcountByChannelDao = playcountByChannelDao;
	}
	
	public void setChannelDao(ChannelDao channelDao) {
		this.channelDao = channelDao;
	}
	
	public void setSendMailService(SendMailService sendMailService) {
		this.sendMailService = sendMailService;
	}



	public List<Report> getReportsForCurrentUser(int start, int count, String filter,
			String orderField, String orderDirection) {
		
		
		if (Utils.isCurrentUserAdministrator()) {
			return reportDao.getAllPaginatedAndFiltered(
					start, count, orderField, orderDirection, filter);
			
		} else {
			User currentUser = userDao.getCurrentUser();
			return reportDao.getReportsForCurrentUser(
					currentUser, start, count, filter, orderField, orderDirection);
		}
	}

	
	public long getReportsForCurrentUserCount(String filter) {
		
		if (Utils.isCurrentUserAdministrator()) {
			return reportDao.getCount(filter);
			
		} else {
		
			User currentUser = userDao.getCurrentUser();
			return reportDao.getReportsForCurrentUserCount(currentUser, filter);
		}
	}



	public Report getReportForDownload(long id) throws LocalizedException {
		
		User user = userDao.getCurrentUser();
		Report report = reportDao.getById(id);
		
		if (report == null) {
			throw new NoReportException();
		}
		
		if (!report.getOwner().equals(user) && !Utils.isCurrentUserAdministrator()){
			throw new UserNotAuthorizedException();
		}
		
		if (report.getState() != State.APPROVED) {
			throw new InvalidStateException("error.cannot.download.unapproved.report");
		}
		
		return getReportOrderedByPlaycounts(id);
	}

}
