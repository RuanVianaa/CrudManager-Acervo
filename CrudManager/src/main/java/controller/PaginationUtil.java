package controller;

import jakarta.servlet.http.HttpServletRequest;

public class PaginationUtil {

	public static final int PAGE_SIZE = 5;

	public static int getPage(HttpServletRequest req) {
		Integer page = RequestUtil.parseInt(req.getParameter("page"));
		if (page == null || page < 1)
			return 1;
		return page;
	}

	public static int totalPages(int totalRecords) {
		int pages = (int) Math.ceil(totalRecords / (double) PAGE_SIZE);
		return pages < 1 ? 1 : pages;
	}

	public static void setAttributes(HttpServletRequest req, int currentPage, int totalPages) {
		req.setAttribute("currentPage", currentPage);
		req.setAttribute("totalPages", totalPages);
	}
}