<%-- 
    Document   : index
    Created on : Jan 1, 2014, 2:12:21 PM
    Author     : Tom.Cocozzello
--%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.net.URL"%>
<%@page import="java.util.ArrayList"%>
<%@page import="beans.AddMusic"%>


<jsp:useBean id="beans" class="beans.AddMusic" scope="session"/> 
<jsp:setProperty name="beans" property="*"/> 


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="java.sql.*,java.net.*,java.io.*,java.lang.*,java.util.*"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
        <body background="partyPic.jpg">
            <% 
              //refresh page every 25 seconds to keep diplay queue up to date
              response.setHeader("Refresh", "25");
              if(beans.getfirstRunNumber() == 0)
              {
                  beans.setfirstRunNumber(1);
                  String[] args={};
                  beans.main(args); 
              }
  
               if (request.getParameter("queue") != null) 
               {                    
                   beans.addToQueue(request.getParameter("queue"));
                   //refresh url to original one so when page refresh comes are every 25 seconds 
                   //it does not re add the same song to the queue
                   String site = new String("http://localhost:8084/AddMusicToQueue/index1.jsp");
                   response.setStatus(response.SC_MOVED_TEMPORARILY);
                   response.setHeader("Location", site);
               }
               List<String> namesOfSongs = new ArrayList<String>();
              namesOfSongs = beans.getNames();
              int namesOfSongLength = beans.getNames().size();
              for(int i = 0; i < namesOfSongLength; i++)
              {
                  out.print(namesOfSongs.get(i) + "<BR/>");
              }

            %>
            
        </body>        
</html>
