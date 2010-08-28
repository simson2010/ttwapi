package ttwapi.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ttwapi.dao.TweetAccessTokenDao;
import ttwapi.dto.TweetAccessToken;
import ttwapi.gae.GCache;
import twitter4j.http.AccessToken;

public class ShowKeyServlet extends HttpServlet {

	private TweetAccessTokenDao tatDao = new TweetAccessTokenDao();
	
	/**
	 * Constructor of the object.
	 */
	public ShowKeyServlet() {
		super();
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String key = request.getParameter("key");
		String type = request.getParameter("type");
		String preKey = request.getParameter("preKey");
		
		PrintWriter out = response.getWriter();
		if(type!=null&&type.equalsIgnoreCase("POST")){
			TweetAccessToken tat = loadTweetAccessToken(preKey);
			String token = tat.getAccessToken();
			String tokenSecret = tat.getAccessTokenSecret();
			if(tat!=null){
				deleteTokenFromDB(tat);
				saveTokenToDB(key, token,tokenSecret);		
				response.sendRedirect("/showKey?key="+key);
				return ;
			}
//			if(GCache.findKey(preKey))
//			{
//				Object o = GCache.get(preKey);
//				GCache.clean(preKey);
//				GCache.put(key, o);
//				response.sendRedirect("/showKey?key="+key);
//				return;
//			}
		}
		else
		{
			out.print("<form action='/showKey' method='Post' >");
			
			out.print("<br/><input type='submit' name='submit' value='save new key'/>");
			out.print("<br/><input type='text' name='key' value='" + key + "' style='width:120px;'/>");
			out.print("<input type='hidden' name='preKey' value='" + key + "'/>");
			out.print("<input type='hidden' name='type' value='post'/>");
			
			out.print("</form>");
			return;
		}
		
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request,response);
	}
	
	private TweetAccessToken loadTweetAccessToken(String key){
		TweetAccessToken tmp = tatDao.getByKey(key);
		
		return tmp;
	}
	
	private void saveTokenToDB(String newKey, String token, String tokenSecret) {
		TweetAccessToken tmp = 
			new TweetAccessToken(newKey, token, tokenSecret);
		
		tatDao.save(tmp);
		
	}
	private void deleteTokenFromDB(TweetAccessToken o){
		
		tatDao.deleteObject(o);
	}

}
