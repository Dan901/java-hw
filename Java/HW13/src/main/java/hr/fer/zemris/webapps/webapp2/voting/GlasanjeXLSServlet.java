package hr.fer.zemris.webapps.webapp2.voting;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * This servlet creates a Microsoft Excel ({@code XLS}) document containing
 * voting results.
 *
 * @author Dan
 */
@WebServlet(urlPatterns = { "/glasanje-xls" })
public class GlasanjeXLSServlet extends HttpServlet {

	/** */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Path bandsFile = Paths.get(req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt"));
		Path votesFile = Paths.get(req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt"));
		Map<Integer, BandInfo> bandsMap = BandInfo.getBandsWithVotes(bandsFile, votesFile);

		HSSFWorkbook hwb = new HSSFWorkbook();

		HSSFCellStyle titleStyle = hwb.createCellStyle();
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

		HSSFFont titleFont = hwb.createFont();
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 14);
		titleStyle.setFont(titleFont);

		HSSFCellStyle nameRowStyle = hwb.createCellStyle();
		nameRowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		nameRowStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		nameRowStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		nameRowStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

		HSSFFont nameFont = hwb.createFont();
		nameFont.setBold(true);
		nameFont.setFontHeightInPoints((short) 12);
		nameRowStyle.setFont(nameFont);

		HSSFCellStyle cellStyle = hwb.createCellStyle();
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

		HSSFSheet sheet = hwb.createSheet("results");
		HSSFCell title = sheet.createRow(0).createCell(0);
		title.setCellStyle(titleStyle);
		title.setCellValue("Voting results");
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

		HSSFRow names = sheet.createRow(1);
		HSSFCell idCell = names.createCell(0);
		idCell.setCellStyle(nameRowStyle);
		idCell.setCellValue("ID");
		HSSFCell nameCell = names.createCell(1);
		nameCell.setCellStyle(nameRowStyle);
		nameCell.setCellValue("Name");
		HSSFCell votesCell = names.createCell(2);
		votesCell.setCellStyle(nameRowStyle);
		votesCell.setCellValue("Votes");

		int i = 2;
		for (BandInfo band : bandsMap.values()) {
			HSSFRow row = sheet.createRow(i);
			HSSFCell cell0 = row.createCell(0);
			cell0.setCellStyle(cellStyle);
			cell0.setCellValue(band.getId());
			HSSFCell cell1 = row.createCell(1);
			cell1.setCellStyle(cellStyle);
			cell1.setCellValue(band.getName());
			HSSFCell cell2 = row.createCell(2);
			cell2.setCellStyle(cellStyle);
			cell2.setCellValue(band.getVotes());
			i++;
		}
		sheet.autoSizeColumn(1);

		resp.setContentType("application/octet-stream");
		resp.addHeader("Content-Disposition", "filename=\"results.xls\"");
		hwb.write(resp.getOutputStream());
		hwb.close();
	}
}
