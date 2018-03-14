package hr.fer.zemris.webapps.webapp_baza;

import java.io.IOException;
import java.util.List;

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

import hr.fer.zemris.webapps.webapp_baza.dao.DAO;
import hr.fer.zemris.webapps.webapp_baza.dao.DAOException;
import hr.fer.zemris.webapps.webapp_baza.dao.DAOProvider;
import hr.fer.zemris.webapps.webapp_baza.polls.PollOption;

/**
 * Creates a Microsoft Excel ({@code XLS}) document containing voting results
 * for a poll whose ID is received as a parameter.
 *
 * @author Dan
 */
@WebServlet("/voting-xls")
public class VotingXLSServlet extends HttpServlet {

	/** */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pollIDStr = req.getParameter("pollID");
		if (pollIDStr == null) {
			ErrorUtil.sendError("PollID parameter is missing!", req, resp);
			return;
		}
		long pollID = Long.parseLong(pollIDStr);

		List<PollOption> options;
		DAO dao = DAOProvider.getDao();
		try {
			options = dao.getPollOptions(pollID);
		} catch (DAOException e) {
			ErrorUtil.sendError(e.getMessage(), req, resp);
			return;
		}

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
		nameCell.setCellValue("Option");
		HSSFCell votesCell = names.createCell(2);
		votesCell.setCellStyle(nameRowStyle);
		votesCell.setCellValue("Votes");

		int i = 2;
		for (PollOption option : options) {
			HSSFRow row = sheet.createRow(i);
			HSSFCell cell0 = row.createCell(0);
			cell0.setCellStyle(cellStyle);
			cell0.setCellValue(option.getId());
			HSSFCell cell1 = row.createCell(1);
			cell1.setCellStyle(cellStyle);
			cell1.setCellValue(option.getTitle());
			HSSFCell cell2 = row.createCell(2);
			cell2.setCellStyle(cellStyle);
			cell2.setCellValue(option.getVotesCount());
			i++;
		}
		sheet.autoSizeColumn(1);

		resp.setContentType("application/octet-stream");
		resp.addHeader("Content-Disposition", "filename=\"results.xls\"");
		hwb.write(resp.getOutputStream());
		hwb.close();
	}
}
