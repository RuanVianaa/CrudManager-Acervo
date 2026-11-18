package controller;

import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtil {

	public static String getAction(HttpServletRequest req) {
		return req.getRequestURI().substring(req.getContextPath().length());
	}

	public static boolean isBlank(String text) {
		return text == null || text.trim().isEmpty();
	}

	public static Integer parseInt(String text) {
		try {
			return Integer.valueOf(text.trim());
		} catch (NumberFormatException | NullPointerException e) {
			return null;
		}
	}

	public static Date parseDate(String text) {
		if (isBlank(text))
			return null;
		return ControllerUtil.formatDate(text.trim());
	}
}