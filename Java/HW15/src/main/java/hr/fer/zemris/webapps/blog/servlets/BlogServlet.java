package hr.fer.zemris.webapps.blog.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.webapps.blog.dao.DAOException;
import hr.fer.zemris.webapps.blog.dao.DAOProvider;
import hr.fer.zemris.webapps.blog.model.BlogComment;
import hr.fer.zemris.webapps.blog.model.BlogEntry;
import hr.fer.zemris.webapps.blog.model.BlogUser;
import hr.fer.zemris.webapps.blog.servlets.forms.BlogCommentForm;
import hr.fer.zemris.webapps.blog.servlets.forms.BlogEntryForm;
import hr.fer.zemris.webapps.blog.servlets.util.ErrorUtil;

/**
 * Processes the requests for displaying blogs. <br>
 * 
 *
 * @author Dan
 */
@WebServlet("/servlets/author/*")
public class BlogServlet extends HttpServlet {

	/** */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	/**
	 * Processes the request according to this servlet's job.
	 * 
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
	private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String reqURL = req.getPathInfo();
		if (reqURL == null || reqURL.length() == 1) {
			ErrorUtil.sendError("Author's nick is required to see their blog!", req, resp);
			return;
		}
		reqURL = reqURL.substring(1);
		String[] elems = reqURL.split("/", 2);

		BlogUser author;
		try {
			author = DAOProvider.getDAO().getBlogUser(elems[0]);
		} catch (DAOException e) {
			ErrorUtil.sendError(e.getMessage(), req, resp);
			return;
		}

		if (author == null) {
			ErrorUtil.sendError("Requested author doesn't exist!", req, resp);
			return;
		}

		req.setAttribute("author", author);
		boolean userIsAuthor = author.getNick().equals(req.getSession().getAttribute("user_nick"));
		req.setAttribute("userIsAuthor", userIsAuthor);

		if (elems.length == 1) {
			req.getRequestDispatcher("/WEB-INF/pages/blog.jsp").forward(req, resp);
			return;
		}

		String option = elems[1];
		if (option.equals("new") || option.equals("edit") || option.equals("save")) {
			if (!userIsAuthor) {
				ErrorUtil.sendError("You are not the owner of this blog!", req, resp);
				return;
			}
		}

		if (option.equals("new")) {
			processNew(req, resp);
		} else if (option.equals("edit")) {
			processEdit(req, resp);
		} else if (option.equals("save")) {
			processSave(author, req, resp);
		} else {
			processEntry(author, elems[1], req, resp);
		}
	}

	/**
	 * Creates a new {@link BlogEntryForm} and sends it to the user for filling
	 * out.
	 * 
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
	private void processNew(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BlogEntryForm form = new BlogEntryForm();
		form.fillFromBlogEntry(new BlogEntry());
		req.setAttribute("form", form);
		req.getRequestDispatcher("/WEB-INF/pages/blogEntryForm.jsp").forward(req, resp);
	}

	/**
	 * Processes the request for editing of an entry.
	 * 
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
	private void processEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		BlogEntry entry;
		try {
			entry = DAOProvider.getDAO().getBlogEntry(Long.parseLong(id));
		} catch (DAOException | NumberFormatException e) {
			ErrorUtil.sendError("Requested entry doesn't exist!", req, resp);
			return;
		}

		BlogEntryForm form = new BlogEntryForm();
		form.fillFromBlogEntry(entry);
		req.setAttribute("form", form);
		req.getRequestDispatcher("/WEB-INF/pages/blogEntryForm.jsp").forward(req, resp);
	}

	/**
	 * Processes the request for saving the entry (new or edited one).
	 * 
	 * @param author
	 *            author of the entry.
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
	private void processSave(BlogUser author, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!"Save".equals(req.getParameter("action"))) {
			resp.sendRedirect(req.getServletContext().getContextPath() + "/servlets/author/" + author.getNick());
			return;
		}

		BlogEntryForm form = new BlogEntryForm();
		form.fillFromHttpReq(req);
		form.validate();

		if (form.anyErrors()) {
			req.setAttribute("form", form);
			req.getRequestDispatcher("/WEB-INF/pages/blogEntryForm.jsp").forward(req, resp);
			return;
		}

		BlogEntry entry;
		if (form.getId().isEmpty()) {
			entry = new BlogEntry();
			form.fillToBlogEntry(entry);
			entry.setCreatedAt(new Date());
			entry.setCreator(author);

			try {
				DAOProvider.getDAO().addObject(entry);
			} catch (DAOException e) {
				ErrorUtil.sendError(e.getMessage(), req, resp);
				return;
			}
		} else {
			try {
				entry = DAOProvider.getDAO().getBlogEntry(Long.parseLong(form.getId()));
			} catch (DAOException e) {
				ErrorUtil.sendError(e.getMessage(), req, resp);
				return;
			}
			form.fillToBlogEntry(entry);
			entry.setLastModifiedAt(new Date());
		}

		author.getEntries().add(entry);

		resp.sendRedirect(req.getServletContext().getContextPath() + "/servlets/author/" + author.getNick());
	}

	/**
	 * Processes the request for displaying an entry and its comments.
	 * 
	 * @param author
	 *            author of the entry
	 * @param id
	 *            ID of the entry
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
	private void processEntry(BlogUser author, String id, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BlogEntry entry;
		try {
			entry = DAOProvider.getDAO().getBlogEntry(Long.parseLong(id));
		} catch (DAOException | NumberFormatException e) {
			ErrorUtil.sendError(e.getMessage(), req, resp);
			return;
		}

		if (entry == null) {
			ErrorUtil.sendError("Entry with given ID: " + id + " doesn't exist!", req, resp);
			return;
		}
		if (!entry.getCreator().equals(author)) {
			ErrorUtil.sendError(author.getNick() + " is not the author of this entry!", req, resp);
			return;
		}
		req.setAttribute("entry", entry);

		BlogCommentForm form = new BlogCommentForm();

		if ("Save".equals(req.getParameter("action"))) {
			form.fillFromHttpReq(req);
			form.validate();

			if (!form.anyErrors()) {
				BlogComment comment = new BlogComment();
				form.fillToBlogComment(comment);
				comment.setPostedOn(new Date());
				comment.setBlogEntry(entry);

				try {
					DAOProvider.getDAO().addObject(comment);
				} catch (DAOException e) {
					ErrorUtil.sendError(e.getMessage(), req, resp);
					return;
				}
				entry.getComments().add(comment);

				form = new BlogCommentForm();
			}
		}

		req.setAttribute("form", form);
		req.getRequestDispatcher("/WEB-INF/pages/blogEntry.jsp").forward(req, resp);
	}

}
