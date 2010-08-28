package ttwapi.oauth;

import javax.servlet.http.HttpServletRequest;

public class Utils {
	public static boolean isEmptyOrNull(String value) {
		if (value == null || value.equals(""))
			return true;

		return false;
	}

	public static String getBaseURL(HttpServletRequest request) {
		if ((request.getServerPort() == 80) || (request.getServerPort() == 443))
			return request.getScheme() + "://" + request.getServerName() + request.getContextPath();
		else
			return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}