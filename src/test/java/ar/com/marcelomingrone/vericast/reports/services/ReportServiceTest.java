package ar.com.marcelomingrone.vericast.reports.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ar.com.marcelomingrone.vericast.reports.AbstractTest;
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
import ar.com.marcelomingrone.vericast.reports.model.UserNotAuthorizedException;
import ar.com.marcelomingrone.vericast.reports.model.ReportItem.TrackIdComparator;
import ar.com.marcelomingrone.vericast.reports.model.TimePeriod;
import ar.com.marcelomingrone.vericast.reports.model.User;
import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;
import ar.com.marcelomingrone.vericast.reports.model.dto.Track;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ReportServiceTest extends AbstractTest {
	
	private ReportService service;
	
	private VericastApiDelegate api;
	
	private SendMailService sendMailServiceMock;
	
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
	
	private List<Channel> channels;
	private List<Track> tracksChannel1;
	private List<Track> tracksChannel2;
	
	@Before
	public void initTest() {
		
		service = new ReportService();
		
		this.channels = new LinkedList<>();
		channels.add(new Channel("1","1"));
		channels.add(new Channel("2", "2"));
		
		api = Mockito.mock(VericastApiDelegate.class);
		Mockito.when(api.getChannelList(Mockito.any(User.class))).thenReturn(channels);
		
		sendMailServiceMock = Mockito.mock(SendMailService.class);
		
		tracksChannel1 = new LinkedList<>();
		tracksChannel1.add(builder.buildTrack("1", 1));
		tracksChannel1.add(builder.buildTrack("2", 2));
		tracksChannel1.add(builder.buildTrack("3", 3));
		
		tracksChannel2 = new LinkedList<>();
		tracksChannel2.add(builder.buildTrack("4", 4));
		tracksChannel2.add(builder.buildTrack("3", 2));
		tracksChannel2.add(builder.buildTrack("1", 6));
		
		Mockito.when(api.getTracksByChannel(Mockito.anyString(), Mockito.any(User.class), 
				Mockito.any(Date.class), Mockito.anyString()))
				.thenReturn(tracksChannel1).thenReturn(tracksChannel2);
		
		service.setVericastApiDelegate(api);
		service.setPlaycountByChannelDao(playcountByChannelDao);
		service.setReportDao(reportDao);
		service.setReportItemDao(reportItemDao);
		service.setUserDao(userDao);
		service.setChannelDao(channelDao);
		service.setSendMailService(sendMailServiceMock);
	}

	
	@Test
	public void buildPlaycountsByChannelVerify() {
		
		User user = builder.buildUser(USERNAME);
		Report report = builder.buildReport(user);
		Date endDate = new Date();
		
		service.buildPlaycountsByChannel(report, TimePeriod.WEEK.toString(), endDate);
		
		Mockito.verify(api, Mockito.times(1)).getChannelList(user);
		Mockito.verify(api, Mockito.times(1)).getTracksByChannel("1", user, endDate, TimePeriod.WEEK.toString());
		Mockito.verify(api, Mockito.times(1)).getTracksByChannel("2", user, endDate, TimePeriod.WEEK.toString());
	}
	
	@Test
	public void buildPlaycountsByChannelVerifySendMail() throws MessagingException {
		
		User user = builder.buildUser(USERNAME);
		Report report = builder.buildReport(user);
		Date endDate = new Date();
		
		service.buildPlaycountsByChannel(report, TimePeriod.WEEK.toString(), endDate);
		
		Mockito.verify(sendMailServiceMock, Mockito.times(1)).sendReport(report);
	}
	
	
	@Test
	public void buildPlaycountsByChannelItems() {
		
		User user = builder.buildUser(USERNAME);
		Report report = builder.buildReport(user);
		Date endDate = new Date();
		
		service.buildPlaycountsByChannel(report, TimePeriod.WEEK.toString(), endDate);
		
		Report result = reportDao.getByIdWithItems(report.getId());
		
		Collections.sort(result.getItems(), new TrackIdComparator());
		
		ReportItem firstItem = result.getItems().get(0);
		List<PlaycountByChannel> playcounts = firstItem.getPlaycounts();
		
		assertEquals(2, playcounts.size());
		for (PlaycountByChannel count : playcounts) {
			if (count.getChannel().getKeyname().equals("1")) {
				assertEquals(1, count.getPlaycount());
			}
			if (count.getChannel().getKeyname().equals("2")) {
				assertEquals(6, count.getPlaycount());
			}
		}
		
	}
	
	@Test
	public void buildPlaycountsByChannelTotalCount() {
		
		User user = builder.buildUser(USERNAME);
		Report report = builder.buildReport(user);
		Date endDate = new Date();
		
		service.buildPlaycountsByChannel(report, TimePeriod.WEEK.toString(), endDate);
		
		Report result = reportDao.getByIdWithItems(report.getId());
		
		Collections.sort(result.getItems(), new TrackIdComparator());
		
		assertEquals(new Long(7), result.getItems().get(0).getTotalPlayCount());
		assertEquals(new Long(2), result.getItems().get(1).getTotalPlayCount());
		assertEquals(new Long(5), result.getItems().get(2).getTotalPlayCount());
		assertEquals(new Long(4), result.getItems().get(3).getTotalPlayCount());
	}
	
	@Test
	public void buildPlaycountsByChannelFinishedState() {
		
		builder.buildUser(USERNAME);
		mockPrincipal(USERNAME);
		
		Report report = service.buildReport(TimePeriod.WEEK.toString(), new Date());
		
		assertEquals(State.IN_PROCESS, report.getState());
		
		service.buildPlaycountsByChannel(report, TimePeriod.WEEK.toString(), new Date());
		
		Report result = reportDao.getByIdWithItems(report.getId());
		
		assertEquals(State.FINISHED, result.getState());
	}
	
	@Test(expected=NoReportException.class)
	public void approveNullReport() throws LocalizedException {
		
		service.approveReport(1L, "user");
	}
	
	@Test(expected=NoReportException.class)
	public void rejectNullReport() throws LocalizedException {
		
		service.rejectReport(1L, "user");
	}
	
	@Test(expected=UserNotAuthorizedException.class)
	public void approveReportNotTheSameUser() throws LocalizedException {
		
		User user = builder.buildUser(USERNAME);
		Report report = builder.buildReport(user);
		
		service.approveReport(report.getId(), "otroUsuario");
	}
	
	@Test(expected=UserNotAuthorizedException.class)
	public void rejectReportNotTheSameUser() throws LocalizedException {
		
		User user = builder.buildUser(USERNAME);
		Report report = builder.buildReport(user);
		
		service.rejectReport(report.getId(), "otroUsuario");
	}
	
	@Test
	public void approveReportNotTheSameUserButAdministrator() throws LocalizedException {
		
		User user = builder.buildUser(USERNAME);
		Report report = builder.buildReport(user, State.FINISHED);
		
		User admin = builder.buildAdminUser("admin");
		mockPrincipal(admin.getUsername(), true);
		
		Report result = service.approveReport(report.getId(), admin.getUsername());
		
		assertEquals(State.APPROVED, result.getState());
	}
	
	@Test
	public void rejectReportNotTheSameUserButAdministrator() throws LocalizedException {
		
		User user = builder.buildUser(USERNAME);
		Report report = builder.buildReport(user, State.FINISHED);
		
		User admin = builder.buildAdminUser("admin");
		mockPrincipal(admin.getUsername(), true);
		
		service.rejectReport(report.getId(), admin.getUsername());
		
		Report result = service.getReportOrderedByPlaycounts(report.getId());
		assertNull(result);
	}
	
	@Test(expected=InvalidStateException.class)
	public void approveReportInvalidState() throws LocalizedException {
		
		User user = builder.buildUser(USERNAME);
		Report report = builder.buildReport(user, State.IN_PROCESS);
		
		service.approveReport(report.getId(), user.getUsername());
	}
	
	@Test(expected=InvalidStateException.class)
	public void rejectReportInvalidState() throws LocalizedException {
		
		User user = builder.buildUser(USERNAME);
		Report report = builder.buildReport(user, State.IN_PROCESS);
		
		service.rejectReport(report.getId(), user.getUsername());
	}
	
	@Test
	public void approveReportHappyPath() throws LocalizedException {
		
		User user = builder.buildUser(USERNAME);
		Report report = builder.buildReport(user, State.FINISHED);
		
		service.approveReport(report.getId(), user.getUsername());
		
		Report result = service.getReportOrderedByPlaycounts(report.getId());
		assertEquals(State.APPROVED, result.getState());
	}
	
	@Test
	public void rejectReportHappyPath() throws LocalizedException {
		
		User user = builder.buildUser(USERNAME);
		Report report = builder.buildReport(user, State.FINISHED);
		
		service.rejectReport(report.getId(), user.getUsername());
		
		Report result = service.getReportOrderedByPlaycounts(report.getId());
		assertNull(result);
	}
	
	@Test
	public void buildReportLeavesItInProcessState() {
		
		builder.buildUser(USERNAME);
		mockPrincipal(USERNAME);
		
		Report report = service.buildReport(TimePeriod.DAY.toString(), new Date());
		assertEquals(State.IN_PROCESS, report.getState());
	}
	
	@Test
	public void buildReportTakesCurrentUser() {
		
		User user = builder.buildUser(USERNAME);
		mockPrincipal(USERNAME);
		
		Report report = service.buildReport(TimePeriod.DAY.toString(), new Date());
		assertEquals(user, report.getOwner());
	}

}
