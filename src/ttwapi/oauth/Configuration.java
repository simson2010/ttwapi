package ttwapi.oauth;

public class Configuration {

	public static String getConsumerKey() {
		return System.getProperty("twitter.api.key");
	}

	public static String getConsumerSecret() {
		return System.getProperty("twitter.api.secret");
	}

	public static String getAuthorizeURL() {
		return System.getProperty("twitter.api.authorize");
	}

	public static String getAccessTokenURL() {
		return System.getProperty("twitter.api.access_token");
	}

	public static String getRequestTokenURL() {
		return System.getProperty("twitter.api.request_token");
	}
}
