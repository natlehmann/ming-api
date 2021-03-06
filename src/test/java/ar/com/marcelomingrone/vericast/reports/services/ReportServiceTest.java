package ar.com.marcelomingrone.vericast.reports.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
import ar.com.marcelomingrone.vericast.reports.model.VericastApiException;
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
	public void initTest() throws VericastApiException {
		
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
	public void buildPlaycountsByChannelVerify() throws VericastApiException {
		
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
	public void buildPlaycountsByChannelWithError() throws MessagingException {
		
		User user = builder.buildUser(USERNAME);
		Report report = builder.buildReport(user);
		Date endDate = new Date();
		
		service.setVericastApiDelegate(null);// to simulate error
		
		service.buildPlaycountsByChannel(report, TimePeriod.WEEK.toString(), endDate);
		
		// Report is deleted
		Report result = service.getReportOrderedByPlaycounts(report.getId());
		assertNull(result);
		
		Mockito.verify(sendMailServiceMock, Mockito.times(1)).sendErrorReport(
				Mockito.any(Report.class), Mockito.any(Exception.class));
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
	
	@Test
	public void userCanBuildReportSimplePath() {
		
		builder.buildUser(USERNAME);
		mockPrincipal(USERNAME);
		
		assertTrue(service.userCanBuildReport());
	}
	
	@Test
	public void userHasReportInProcess() {
		
		User user = builder.buildUser(USERNAME);
		mockPrincipal(USERNAME);
		builder.buildReport(user, State.IN_PROCESS);
		
		assertFalse(service.userCanBuildReport());
	}
	
	@Test
	public void userHasReportUnapproved() {
		
		User user = builder.buildUser(USERNAME);
		mockPrincipal(USERNAME);
		builder.buildReport(user, State.FINISHED);
		
		assertFalse(service.userCanBuildReport());
	}
	
	@Test
	public void userHasApprovedReports() {
		
		User user = builder.buildUser(USERNAME);
		mockPrincipal(USERNAME);
		builder.buildReport(user, State.APPROVED);
		builder.buildReport(user, State.APPROVED);
		
		assertTrue(service.userCanBuildReport());
	}
	
	@Test
	public void getSameReportNoResults() {
		
		Date endDate = new Date();
		Report report = service.getSameReport(TimePeriod.WEEK.toString(), endDate);
		assertNull(report);
	}
	
	@Test
	public void getSameReportSameParameters() {
		
		Date endDate = new Date();
		
		User user = builder.buildUser(USERNAME);
		mockPrincipal(USERNAME);
		Report report = builder.buildReport(user, State.APPROVED, TimePeriod.WEEK.toString(), endDate);
		
		Report result = service.getSameReport(TimePeriod.WEEK.toString(), endDate);
		assertEquals(report, result);
	}
	
	@Test
	public void getSameReportSameParametersOtherState() {
		
		Date endDate = new Date();
		
		User user = builder.buildUser(USERNAME);
		mockPrincipal(USERNAME);
		Report report = builder.buildReport(user, State.FINISHED, TimePeriod.WEEK.toString(), endDate);
		
		Report result = service.getSameReport(TimePeriod.WEEK.toString(), endDate);
		assertEquals(report, result);
	}
	
	@Test
	public void getSameReportSameParametersOtherState2() {
		
		Date endDate = new Date();
		
		User user = builder.buildUser(USERNAME);
		mockPrincipal(USERNAME);
		Report report = builder.buildReport(user, State.IN_PROCESS, TimePeriod.WEEK.toString(), endDate);
		
		Report result = service.getSameReport(TimePeriod.WEEK.toString(), endDate);
		assertEquals(report, result);
	}
	
	@Test
	public void getSameReportSameParametersDifferentUser() {
		
		Date endDate = new Date();
		
		builder.buildUser(USERNAME);
		mockPrincipal(USERNAME);
		
		User other = builder.buildUser("other");
		builder.buildReport(other, State.APPROVED, TimePeriod.WEEK.toString(), endDate);
		
		Report result = service.getSameReport(TimePeriod.WEEK.toString(), endDate);
		assertNull(result);
	}
	
	
	@Test
	public void getReportsForMyUserNoFilter() {
		
		User user = builder.buildUser("username");
		mockPrincipal("username");
		Report report = builder.buildReport(user, State.FINISHED);
		Report report2 = builder.buildReport(user, State.IN_PROCESS);
		
		User otherUser = builder.buildUser("otherUser");
		builder.buildReport(otherUser);
		
		List<Report> result = service.getReportsForCurrentUser(0, 100, null, null, null);
		
		assertEquals(2, result.size());
		assertTrue(result.contains(report));
		assertTrue(result.contains(report2));
		
		assertEquals(2, service.getReportsForCurrentUserCount(null));
	}
	
	@Test
	public void getReportsForMyUserWithFilter() {
		
		User user = builder.buildUser("username");
		mockPrincipal("username");
		Report report = builder.buildReport(user, State.FINISHED);
		builder.buildReport(user, State.IN_PROCESS);
		
		User otherUser = builder.buildUser("otherUser");
		builder.buildReport(otherUser);
		
		List<Report> result = service.getReportsForCurrentUser(0, 100, "FIN", null, null);
		
		assertEquals(1, result.size());
		assertEquals(report, result.get(0));
		
		assertEquals(1, service.getReportsForCurrentUserCount("FIN"));
	}
	
	@Test
	public void getReportsForAdminUserNoFilter() {
		
		User user = builder.buildUser("username");
		Report report = builder.buildReport(user, State.FINISHED);
		Report report2 = builder.buildReport(user, State.IN_PROCESS);
		
		User otherUser = builder.buildAdminUser("admin");
		Report report3 = builder.buildReport(otherUser, State.APPROVED);
		
		mockPrincipal("admin", true);
		
		List<Report> result = service.getReportsForCurrentUser(0, 100, null, "state", "ASC");
		
		assertEquals(3, result.size());
		assertEquals(report3, result.get(0));
		assertEquals(report, result.get(1));
		assertEquals(report2, result.get(2));
		
		assertEquals(3, service.getReportsForCurrentUserCount(null));
	}

	
	@Test
	public void getReportsForAdminUserWithFilter() {
		
		User user = builder.buildUser("username");
		builder.buildReport(user, State.FINISHED);
		Report report2 = builder.buildReport(user, State.IN_PROCESS);
		
		User otherUser = builder.buildAdminUser("admin");
		Report report3 = builder.buildReport(otherUser, State.APPROVED);
		
		mockPrincipal("admin", true);
		
		List<Report> result = service.getReportsForCurrentUser(0, 100, "O", "state", "DESC");
		
		assertEquals(2, result.size());
		assertEquals(report2, result.get(0));
		assertEquals(report3, result.get(1));
		
		assertEquals(2, service.getReportsForCurrentUserCount("O"));
	}
	
	
	@Test(expected=NoReportException.class)
	public void getReportForDownloadDoesNotExist() throws LocalizedException {
		
		builder.buildUser("username");
		mockPrincipal("username");
		service.getReportForDownload(1L);
	}
	
	@Test(expected=InvalidStateException.class)
	public void getReportForDownloadInvalidState() throws LocalizedException {
		
		User user = builder.buildUser("username");
		Report report = builder.buildReport(user, State.IN_PROCESS);
		mockPrincipal("username");
		
		service.getReportForDownload(report.getId());
	}
	
	@Test(expected=UserNotAuthorizedException.class)
	public void getReportForDownloadUserIsNotOwner() throws LocalizedException {
		
		User user = builder.buildUser("username");
		Report report = builder.buildReport(user, State.APPROVED);
		
		builder.buildUser("other");
		mockPrincipal("other");
		
		service.getReportForDownload(report.getId());
	}
	
	@Test
	public void getReportForDownloadUserIsNotOwnerButIsAdmin() throws LocalizedException {
		
		User user = builder.buildUser("username");
		Report report = builder.buildReport(user, State.APPROVED);
		
		builder.buildAdminUser("other");
		mockPrincipal("other", true);
		
		Report result = service.getReportForDownload(report.getId());
		assertEquals(report, result);
	}
	
	@Test
	public void getReportForDownloadUserIsOwner() throws LocalizedException {
		
		User user = builder.buildUser("username");
		Report report = builder.buildReport(user, State.APPROVED);
		
		mockPrincipal("username");
		
		Report result = service.getReportForDownload(report.getId());
		assertEquals(report, result);
	}
}
