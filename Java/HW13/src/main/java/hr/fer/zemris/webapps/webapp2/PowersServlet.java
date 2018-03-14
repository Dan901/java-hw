package hr.fer.zemris.webapps.webapp2;

import java.io.IOException;

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

/**
 * Dynamically creates a Microsoft Excel ({@code XLS}) document with {@code n}
 * pages.<br>
 * Page {@code i} contains a table with two columns, first column has values
 * from interval [{@code a}, {@code b}] and second their respective {@code i}-th
 * powers.<br>
 * Parameters {@code n, a, b} are expected to be received as a part of the URL.
 * 
 * @author Dan
 */
@WebServlet(urlPatterns = { "/powers" })
public class PowersServlet extends HttpServlet {

	/** */
	private static final long serialVersionUID = 1L;

	/** Minimum value for parameter 'a'. */
	private static final int A_LOWER_LIMIT = -100;

	/** Maximum value for parameter 'a'. */
	private static final int A_UPPER_LIMIT = 100;

	/** Minimum value for parameter 'b'. */
	private static final int B_LOWER_LIMIT = -100;

	/** Maximum value for parameter 'b'. */
	private static final int B_UPPER_LIMIT = 100;

	/** Minimum value for parameter 'n'. */
	private static final int N_LOWER_LIMIT = 1;

	/** Maximum value for parameter 'n'. */
	private static final int N_UPPER_LIMIT = 5;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String aStr = req.getParameter("a");
		String bStr = req.getParameter("b");
		String nStr = req.getParameter("n");
		if (aStr == null || bStr == null || nStr == null) {
			sendError("Invalid request. Expected parameters: 'a', 'b' and 'n'.", req, resp);
			return;
		}

		int a, b, n;
		try {
			a = Integer.parseInt(aStr);
			b = Integer.parseInt(bStr);
			n = Integer.parseInt(nStr);
		} catch (NumberFormatException e) {
			sendError("Parametrs have to be integers.", req, resp);
			return;
		}

		if (a < A_LOWER_LIMIT || a > A_UPPER_LIMIT) {
			sendError("Given parameter 'a' is invalid: " + a + ". It should be in range: [" + A_LOWER_LIMIT + ", "
					+ A_UPPER_LIMIT + "].", req, resp);
			return;
		}
		if (b < B_LOWER_LIMIT || b > B_UPPER_LIMIT) {
			sendError("Given parameter 'b' is invalid: " + b + ". It should be in range: [" + B_LOWER_LIMIT + ", "
					+ B_UPPER_LIMIT + "].", req, resp);
			return;
		}
		if (n < N_LOWER_LIMIT || n > N_UPPER_LIMIT) {
			sendError("Given parameter 'n' is invalid: " + n + ". It should be in range: [" + N_LOWER_LIMIT + ", "
					+ N_UPPER_LIMIT + "].", req, resp);
			return;
		}

		HSSFWorkbook hwb = createDocument(n, a, b);
		resp.setContentType("application/octet-stream");
		resp.addHeader("Content-Disposition", "filename=\"powers.xls\"");
		hwb.write(resp.getOutputStream());
		hwb.close();
	}

	/**
	 * Creates a {@code HSSFWorkbook} document with this servlet's
	 * specification.
	 * 
	 * @param n
	 *            number of pages
	 * @param a
	 *            lower interval limit
	 * @param b
	 *            upper interval limit
	 * @return a generated {@code XLS} document as a {@code HSSFWorkbook}
	 */
	private HSSFWorkbook createDocument(int n, int a, int b) {
		if (a > b) {
			int temp = a;
			a = b;
			b = temp;
		}

		HSSFWorkbook hwb = new HSSFWorkbook();

		HSSFCellStyle nameRowStyle = hwb.createCellStyle();
		nameRowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		nameRowStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		nameRowStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

		HSSFFont font = hwb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 12);
		nameRowStyle.setFont(font);

		HSSFCellStyle cellStyle = hwb.createCellStyle();
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

		for (int i = 1; i <= n; i++) {
			HSSFSheet sheet = hwb.createSheet("sheet" + i);

			HSSFRow nameRow = sheet.createRow(0);
			HSSFCell firstColName = nameRow.createCell(0);
			firstColName.setCellValue("x");
			firstColName.setCellStyle(nameRowStyle);
			HSSFCell secondColName = nameRow.createCell(1);
			secondColName.setCellValue("x^" + i);
			secondColName.setCellStyle(nameRowStyle);

			for (int j = a, nRow = 1; j <= b; j++, nRow++) {
				HSSFRow row = sheet.createRow(nRow);
				HSSFCell cell0 = row.createCell(0);
				cell0.setCellStyle(cellStyle);
				cell0.setCellValue(j);
				HSSFCell cell1 = row.createCell(1);
				cell1.setCellStyle(cellStyle);
				cell1.setCellValue(Math.pow(j, i));
			}
		}

		return hwb;
	}

	/**
	 * Sends an error message as a response.
	 * 
	 * @param message
	 *            error message
	 * @param req
	 *            an {@code HttpServletRequest} object that contains the request
	 *            the client has made of the servlet
	 * @param resp
	 *            an {@code HttpServletResponse} object that contains the
	 *            response the servlet sends to the client
	 * @throws ServletException
	 *             if the request could not be handled
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private void sendError(String message, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("message", message);
		req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
	}
}
