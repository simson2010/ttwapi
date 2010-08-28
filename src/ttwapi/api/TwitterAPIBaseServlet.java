package ttwapi.api;

import javax.servlet.http.HttpServlet;
import ttwapi.oauth.Configuration;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

public class TwitterAPIBaseServlet extends HttpServlet {
	Twitter twitter = null;
	AccessToken accessToken = null;
	
	protected void getTwitter(){
		twitter = new TwitterFactory().getInstance();
		
	}
	protected void getAccessToken()	{
		
	}
}
