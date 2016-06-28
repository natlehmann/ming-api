package ar.com.marcelomingrone.vericast.reports.view;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;

import ar.com.marcelomingrone.vericast.reports.model.ReportItem;

public enum PlaycountsHeaderColumn {

	TRACK_NAME {
		public String getHeader() {
			return "Track";
		}

		@Override
		public String getValueAsString(ReportItem item) {
			return item.getTrackName();
		}

		@Override
		public CellStyle getStyle(ChartStyleManager styleManager) {
			return styleManager.getLeftCellStyle();
		}
	},

	ARTIST {
		@Override
		public String getHeader() {
			return "Artist";
		}

		@Override
		public String getValueAsString(ReportItem item) {
			return item.getArtistName();
		}

		@Override
		public CellStyle getStyle(ChartStyleManager styleManager) {
			return styleManager.getLeftCellStyle();
		}
	},

	LABEL {
		@Override
		public String getHeader() {
			return "Label";
		}

		@Override
		public String getValueAsString(ReportItem item) {
			return item.getLabelName();

		}

		@Override
		public CellStyle getStyle(ChartStyleManager styleManager) {
			return styleManager.getLeftCellStyle();
		}
	},

	TOTAL {
		@Override
		public String getHeader() {
			return "Total";
		}

		@Override
		public String getValueAsString(ReportItem item) {
			return String.valueOf(item.getTotalPlayCount());
		}

		@Override
		public CellStyle getStyle(ChartStyleManager styleManager) {
			return styleManager.getRightCellStyle();
		}
	};

	public abstract String getHeader();

	public RichTextString getValue(ReportItem item) {
		return new HSSFRichTextString(getValueAsString(item));
	}
	
	public abstract String getValueAsString(ReportItem item);

	public abstract CellStyle getStyle(ChartStyleManager styleManager);

}
