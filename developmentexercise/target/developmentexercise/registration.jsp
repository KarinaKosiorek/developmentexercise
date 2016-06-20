<form method="post" action="/developmentexercise/registration">
  	User name:<br>
  	<input type="text" name="username" required pattern="[a-zA-Z0-9]+" placeHolder="Accepts alphanumeric characters only" autofocus="autofocus" width="20%"
  	<%
		Object usernameValue = request.getAttribute("username");
			if (usernameValue != null) {
	%>
				value=<%= request.getAttribute("username").toString() %>
	<%
			}
		    else {
	%>
				value=""
	<%
		    }
	%>><br><br>
	User password:<br>
  	<input type="password" name="password" autocomplete="off" width="20%"
  	<%
		Object passwordValue = request.getAttribute("password");
			if (passwordValue != null) {
	%>
				value=<%= request.getAttribute("password").toString() %>
	<%
			}
		    else{
	%>
				value=""
	<%
		    }
	%>><br><br>
  <input type="submit" value="Submit"/>
</form>
<br>
<%
	Object errorValue = request.getAttribute("error");
		if (errorValue != null) {
%>				
			<h3 style="color:red;"><%= request.getAttribute("error").toString() %></h3>
			<br>
<%			
		}
%>
<%
	Object messageValue = request.getAttribute("message");
		if (messageValue != null) {
%>				
			<h3 style="color:green;"><%= request.getAttribute("message").toString() %></h3>
<%			
		}
%>