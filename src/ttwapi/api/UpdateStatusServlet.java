package ttwapi.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ttwapi.dao.TweetAccessTokenDao;
import ttwapi.dto.TweetAccessToken;
import ttwapi.gae.GCache;
import ttwapi.oauth.Configuration;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;


public class UpdateStatusServlet extends HttpServlet {

	private TweetAccessTokenDao tatDao = new TweetAccessTokenDao();
	
	static final Logger logger = Logger.getLogger(UpdateStatusServlet.class.getName());
	
	
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String key = request.getParameter("key");

		Twitter twitter = new TwitterFactory().getInstance();
		//twitter.setOAuthAccessToken("133816406-X1qKnkaFNdNfupndQJ6RkoVczMwr0A17z7KSWdrZ", "QDepl1K0slJVHZLV8c1PWu48Fo9AdBhwz6cLIR0Ozo");
		twitter.setOAuthConsumer(Configuration.getConsumerKey(), Configuration.getConsumerSecret());
	
		//AccessToken accessToken = loadAccessToken(1);
		//AccessToken accessToken = loadAccessToken(key);
		AccessToken accessToken = loadAccessTokenFromDB(key);
		logger.info("Get AccessToken with Key:"+key);
		if(accessToken==null){
			response.sendRedirect("/updateStatus.html");
			return;
		}
		
		twitter.setOAuthAccessToken(accessToken);
		Status status=  null;
		try {
			if(request.getParameter("submit2")==null){				
				status = twitter.updateStatus(request.getParameter("status"));
			}	else{
				List<Status> tmpList= twitter.getUserTimeline(new Paging(1,1));
				status = tmpList.get(0);
			}
			
		} catch (TwitterException e) {
			response.getWriter().print(e.getMessage());	
			return;
		}
		
		request.getSession().setAttribute("status", status);
		Paging paging = new Paging(1,20);
		List<Status> statusList = null;
		try {
			 statusList = twitter.getFriendsTimeline(paging);
		} catch (TwitterException e) {
			logger.warning(e.getMessage());
			e.printStackTrace();
		}
		
		request.getSession().setAttribute("statusList", statusList);
		request.getRequestDispatcher("/home.jsp").forward(request, response);
		return;
		
	}
	
	private static AccessToken loadAccessToken(int useId){
	    String token = "133816406-X1qKnkaFNdNfupndQJ6RkoVczMwr0A17z7KSWdrZ";
	    String tokenSecret = "QDepl1K0slJVHZLV8c1PWu48Fo9AdBhwz6cLIR0Ozo";
	    return new AccessToken(token, tokenSecret);
	  }
	
	private static AccessToken loadAccessToken(String key){
		AccessToken ac = (AccessToken)GCache.get(key);
		
	    return new AccessToken(ac.getToken(), ac.getTokenSecret());
	  }
	private  AccessToken loadAccessTokenFromDB(String key){
		AccessToken ac = null;
		TweetAccessToken tat = tatDao.getByKey(key);
		
		if(tat==null)
		{
			logger.info("Cannot get from DB");
			return null;
		}
		else
		{
			ac = new AccessToken(tat.getAccessToken(),tat.getAccessTokenSecret());
			if(ac!=null){
				logger.info("New AccessToken Created!");
			}
			return ac;
		}
	}
	
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
