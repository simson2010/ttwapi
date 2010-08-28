package ttwapi.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ttwapi.oauth.Configuration;
import ttwapi.dao.TweetAccessTokenDao;
import ttwapi.dto.TweetAccessToken;
import ttwapi.gae.GCache;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

public class CallBackServlet extends HttpServlet {

	private TweetAccessTokenDao tatDao = new TweetAccessTokenDao();

	public CallBackServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		//Twitter twitter = new TwitterFactory().getInstance();
		Twitter twitter = (Twitter)request.getSession().getAttribute("twitter");
		//twitter.setOAuthConsumer(Configuration.getConsumerKey(), Configuration.getConsumerSecret());
		String verifier = request.getParameter("oauth_verifier");
		RequestToken requestToken = (RequestToken)request.getSession().getAttribute("requestToken");
		AccessToken accessToken =null;
//		try {
//			requestToken = twitter.getOAuthRequestToken();
//			
//		} catch (TwitterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String pin = request.getParameter("oauth_token");
		try {
			//accessToken = twitter.getOAuthAccessToken(requestToken, pin);
			accessToken = twitter.getOAuthAccessToken(requestToken, verifier);			
			GCache.put(verifier, accessToken);
			
			saveTokenToDB(verifier,accessToken);
			
			request.getSession().removeAttribute("requestToken");
		} catch (TwitterException e) {
			
			e.printStackTrace();
		}
		response.getWriter().print("AccessToken:" + accessToken.getToken()+"<br/>");
		response.getWriter().print("AccessTokenSecret:"+accessToken.getTokenSecret()+"<br/>");
		response.getWriter().print("verifier:"+verifier);
		
		response.sendRedirect("/showKey?key="+verifier);


	}

	//初步使用返回的oauth_verifier作为Key，以后可以修改其它易记忆的
	private void saveTokenToDB(String key, AccessToken accessToken) {
		TweetAccessToken tmp = new TweetAccessToken(key,accessToken.getToken(),accessToken.getTokenSecret());
		
		tatDao.save(tmp);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

			doGet(request,response);
	}


	public void init() throws ServletException {
		// Put your code here
	}

}
