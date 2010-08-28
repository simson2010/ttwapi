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

public class AccessToken {
	private String accessToken;
	private String accessTokenSecret;

	public AccessToken(String requestToken, String requestTokenSecret, String oauth_verifier) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException {
		OAuthConsumer consumer = new DefaultOAuthConsumer(Configuration.getConsumerKey(), Configuration.getConsumerSecret());
		OAuthProvider provider = new DefaultOAuthProvider(Configuration.getRequestTokenURL(), Configuration.getAccessTokenURL(), Configuration.getAuthorizeURL());
		consumer.setTokenWithSecret(requestToken, requestTokenSecret);
		provider.setOAuth10a(true);
		provider.retrieveAccessToken(consumer, oauth_verifier);
		this.accessToken = consumer.getToken();
		this.accessTokenSecret = consumer.getTokenSecret();
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}
}