<%@ page language="java" import="java.util.*,twitter4j.Status" pageEncoding="UTF-8"%>
<%@page import="ttwapi.common.TweetParser"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML >
<html>
  <head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
    <title>HOME</title>
    
  </head>
  
  <body>
  	<a href="updateStatus.html">Make a Post</a>
  	<form action="/updatestatus" method="post" 	style="width:90%;">
  	
  	<input type="password" value="<%=request.getParameter("key") %>" name="key"/>
  	<br/>
  	<input type="submit" name="submit2" id="submit2" value="Refresh"/>
  	<br/>
    <textarea rows="4" cols="80" name="status"></textarea>
    <br/>
    <input type="submit" name="submit" id="submit" value="Post"/>
    <input type="reset" name="reset" id="reset" value="Reset"/>
    
    </form>
  	<br/>
  	Last update:
  	<br/>
  	<%Status ss = (Status)request.getSession().getAttribute("status"); %>
  	<img src="<%=ss.getUser().getProfileImageURL().toString() %>"/>
  	<%=ss.getUser().getScreenName() %><br/>
  	<%=ss.getText() %><br/>
  	Friends TimeLine:<br/>
  	<%
  		List<Status> l = (List<Status>)request.getSession().getAttribute("statusList");
  		
  		for(Status s : l)
  		{
  		%>
  			<div>
  			<%=s.getUser().getScreenName()%><br/>
  			<%=TweetParser.parseText(s.getText()) %><br/>
  			<hr/>
			</div>
  		<%	
  		}
  	 %>
  </body>
</html>
