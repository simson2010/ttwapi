package ttwapi.oauth;

import ttwapi.oauth.Configuration;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

public class RequestToken {
	private String authUrl = null;
	private String token = null;
	private String tokenSecret = null;

	public RequestToken(String callbackURL) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException {
		OAuthConsumer consumer = new DefaultOAuthConsumer(Configuration.getConsumerKey(), Configuration.getConsumerSecret());
		OAuthProvider provider = new DefaultOAuthProvider(Configuration.getRequestTokenURL(), Configuration.getAccessTokenURL(), Configuration.getAuthorizeURL());
		
		this.authUrl = provider.retrieveRequestToken(consumer, callbackURL);
		this.token = consumer.getToken();
		this.tokenSecret = consumer.getTokenSecret();
	}

	public String getAuthUrl() {
		return authUrl;
	}

	public String getToken() {
		return token;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}
}
