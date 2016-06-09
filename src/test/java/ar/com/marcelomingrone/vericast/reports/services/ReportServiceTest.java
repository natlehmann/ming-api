package ar.com.marcelomingrone.vericast.reports.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ar.com.marcelomingrone.vericast.reports.AbstractTest;
import ar.com.marcelomingrone.vericast.reports.model.User;
import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;
import ar.com.marcelomingrone.vericast.reports.model.dto.ChannelList;
import ar.com.marcelomingrone.vericast.reports.model.dto.Track;
import ar.com.marcelomingrone.vericast.reports.model.dto.TrackList;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ReportServiceTest extends AbstractTest {
	
	@Autowired
	private ReportService service;

	@Test
	public void getChannelListNotNull() {
		
		User user = builder.buildUser(USERNAME, API_KEY);
		mockPrincipal(USERNAME);
		
		ChannelList list = service.getChannelList(user);
		assertNotNull(list);
		assertFalse(list.getChannels().isEmpty());
		
		for (Channel channel : list.getChannels()) {
			assertNotNull(channel.getKeyname());
			assertNotNull(channel.getName());
		}
		
	}
	
	@Test
	public void getTrackListByChannelNotNull() {
		
		User user = builder.buildUser(USERNAME, API_KEY);
		mockPrincipal(USERNAME);
		
		TrackList list = service.getTrackListByChannel("rockandpop--------------------01", user);
		assertNotNull(list);
		assertFalse(list.getTracks().isEmpty());
		
		for (Track track : list.getTracks()) {
			
			assertNotNull(track.getName());
			assertNotNull("Artista nulo para el track " + track.getName(), track.getArtist().getName());
			assertNotNull("Label nulo para el track " + track.getName(), track.getLabel().getName());
			assertNotNull("Playcount nulo para el track " + track.getName(), track.getPlaycount());
			assertTrue("Playcount negativo para el track " + track.getName(), track.getPlaycount() >= 0);
		}
	}

}
