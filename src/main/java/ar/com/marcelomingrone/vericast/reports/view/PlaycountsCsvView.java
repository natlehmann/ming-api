package ar.com.marcelomingrone.vericast.reports.view;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import ar.com.marcelomingrone.vericast.reports.model.PlaycountByChannel;
import ar.com.marcelomingrone.vericast.reports.model.Report;
import ar.com.marcelomingrone.vericast.reports.model.ReportItem;
import ar.com.marcelomingrone.vericast.reports.model.dto.Channel;
import ar.com.marcelomingrone.vericast.reports.view.ReportViewUtils.Extension;


public class PlaycountsCsvView extends AbstractView {
	
	private static final String FIELD_SEPARATOR = ",";

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map<String, Object> modelMap,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Report report = (Report) modelMap.get("report");
		List<Channel> channels = (List<Channel>) modelMap.get("channels");

	    BufferedWriter writer = new BufferedWriter(response.getWriter());

	    response.setHeader("Content-Disposition","attachment; filename=\"" 
	    		+ ReportViewUtils.getReportName(report, Extension.CSV) + "\"");

	    writeReport(report, channels, writer);

	    writer.flush(); 
	    writer.close();

	}

	public void writeReport(Report report, List<Channel> channels, BufferedWriter writer)
			throws IOException {
		
		Map<Channel, Integer> channelIndex = new HashMap<Channel, Integer>();
		int count = 0;
		
		for (Channel channel : channels) {
			channelIndex.put(channel, count++);
		}
		
		writeReportHeader(writer, channels);
		
		for (ReportItem item : report.getItems()) {
			
			for (PlaycountsHeaderColumn header : PlaycountsHeaderColumn.values()) {
				writer.write(header.getValueAsString(item));
				writer.write(FIELD_SEPARATOR);
			}
			
			long[] playcountsArray = new long[channels.size()];
			
			for (PlaycountByChannel playcount : item.getPlaycounts()) {
				int index = channelIndex.get(playcount.getChannel());
				playcountsArray[index] = playcount.getPlaycount();
			}
			
			String playcountsStr = Arrays.toString(playcountsArray).replaceAll("\\[|\\]|\\s", "");
	    	
	    	writer.write(playcountsStr);
	    	writer.newLine();
	    	
	    }
	}


	protected void writeReportHeader(BufferedWriter writer, List<Channel> channels) throws IOException {
		
		for (PlaycountsHeaderColumn header : PlaycountsHeaderColumn.values()) {
			writer.write(header.getHeader());
			writer.write(FIELD_SEPARATOR);
		}
		
		int count = 1;
		for (Channel channel : channels) {
			writer.write(channel.getName());
			if (count < channels.size()) {
				writer.write(FIELD_SEPARATOR);
			}
			count++;
		}
		
		writer.newLine();
		
	}

}
