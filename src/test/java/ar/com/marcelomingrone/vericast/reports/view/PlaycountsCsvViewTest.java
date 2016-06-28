package ar.com.marcelomingrone.vericast.reports.view;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ar.com.marcelomingrone.vericast.reports.model.PlaycountByChannel;
import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.ReportItem;
import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;

public class PlaycountsCsvViewTest {
	
	private StringWriter stringWriter;
	private BufferedWriter writer;
	private PlaycountsCsvView csv;
	
	private List<Channel> channels;
	private Channel channel1;
	private Channel channel2;
	private Channel channel3;
	
	
	@Before
	public void initTest() {
		
		stringWriter = new StringWriter();
		writer = new BufferedWriter(stringWriter);
		csv = new PlaycountsCsvView();
		
		channel1 = new Channel(1L, "one", "keyname1");
		channel2 = new Channel(2L, "two", "keyname2");
		channel3 = new Channel(3L, "three", "keyname3");
		
		channels = new LinkedList<>();
		channels.add(channel1);
		channels.add(channel2);
		channels.add(channel3);
		
	}

	@Test
	public void writeReportHeader() throws IOException {
		
		csv.writeReportHeader(writer, channels);
		writer.flush();
		
		String result = "Track,Artist,Label,Total,one,two,three\n";
		
		assertEquals(result, stringWriter.getBuffer().toString());
		
	}
	
	@Test
	public void writeOneItem() throws IOException {
		
		Report report = new Report();
		
		ReportItem item1 = new ReportItem("trackName1", "artist1", "label1", 35L);
		item1.addPlaycount(new PlaycountByChannel(channel2, 21));
		item1.addPlaycount(new PlaycountByChannel(channel3, 10));
		item1.addPlaycount(new PlaycountByChannel(channel1, 4));
		report.addItem(item1);
		
		csv.writeItems(report, channels, writer);
		writer.flush();
		
		assertEquals("trackName1,artist1,label1,35,4,21,10\n", stringWriter.getBuffer().toString());
		
	}
	
	@Test
	public void writeOneItemWithBlanks() throws IOException {
		
		Report report = new Report();
		
		ReportItem item1 = new ReportItem("trackName1", "artist1", "label1", 21L);
		item1.addPlaycount(new PlaycountByChannel(channel2, 21));
		report.addItem(item1);
		
		csv.writeItems(report, channels, writer);
		writer.flush();
		
		assertEquals("trackName1,artist1,label1,21,0,21,0\n", stringWriter.getBuffer().toString());
		
	}
	
	@Test
	public void writeTwoItems() throws IOException {
		
		Report report = new Report();
		
		ReportItem item1 = new ReportItem("trackName1", "artist1", "label1", 35L);
		item1.addPlaycount(new PlaycountByChannel(channel2, 21));
		item1.addPlaycount(new PlaycountByChannel(channel3, 10));
		item1.addPlaycount(new PlaycountByChannel(channel1, 4));
		report.addItem(item1);
		
		ReportItem item2 = new ReportItem("trackName2", "artist2", "label2", 21L);
		item2.addPlaycount(new PlaycountByChannel(channel3, 21));
		report.addItem(item2);
		
		csv.writeItems(report, channels, writer);
		writer.flush();
		
		assertEquals("trackName1,artist1,label1,35,4,21,10\n" + "trackName2,artist2,label2,21,0,0,21\n", 
				stringWriter.getBuffer().toString());
		
	}

}
