package ttwapi.api;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import ttwapi.oauth.*;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.http.*;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;
import twitter4j.TwitterException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class RequestTokenServlet extends HttpServlet {

	static final Logger logger = Logger.getLogger(RequestTokenServlet.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		URLFetchService urlFetch = URLFetchServiceFactory.getURLFetchService();
		 
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Configuration.getConsumerKey(), Configuration.getConsumerSecret());
		RequestToken requestToken = null;
		AccessToken accessToken =null;
		try {
			requestToken = twitter.getOAuthRequestToken("http://ttwapi.appspot.com/callback");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		req.getSession().setAttribute("twitter", twitter);
		req.getSession().setAttribute("requestToken",requestToken);
		resp.sendRedirect(requestToken.getAuthenticationURL());
		HTTPResponse httpResponse = 
			urlFetch.fetch(new HTTPRequest(new URL(requestToken.getAuthorizationURL()),HTTPMethod.POST));
		//resp.getOutputStream().write(httpResponse.getContent());
//		List<HTTPHeader> headerList = httpResponse.getHeaders();
//		if(headerList.size()>1){
//			for(HTTPHeader hdr : headerList){
//				resp.getWriter().print(hdr.getName()) ;
//				resp.getWriter().print(" - ");
//				resp.getWriter().print(hdr.getValue());
//				resp.getWriter().print("<br/>");
//				
//			}
//		}
		return;
		
		
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		doGet(req, resp);
	}

}
