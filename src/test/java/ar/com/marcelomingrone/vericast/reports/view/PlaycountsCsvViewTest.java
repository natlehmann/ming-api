package ar.com.marcelomingrone.vericast.reports.view;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

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
		
		channel1 = new Channel("one", "keyname1");
		channel2 = new Channel("two", "keyname2");
		channel3 = new Channel("three", "keyname3");
		
		channels = new LinkedList<>();
		channels.add(channel1);
		channels.add(channel2);
		channels.add(channel3);
		
	}

	@Test
	public void test() throws IOException {
		
		csv.writeReportHeader(writer, channels);
		writer.flush();
		
		String result = "Track,Artist,Label,Total,one,two,three\n";
		
		assertEquals(result, stringWriter.getBuffer().toString());
		
	}

}
