<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="styles.css">
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
<script src="jparticle.jquery.min.js"></script>
<title>Sign Up</title>
</head>
<body>
	<div class="navbar">
		<div class="wrap">
			<h1>USC CS 310: Stock Portfolio Management</h1>
		</div>
	</div>
	
	<script>
		function checkUsernamePassword()
		{
			var username = document.getElementById("username").value;
			var password = document.getElementById("password").value;
			var confirmpassword = document.getElementById("confirmpassword").value;
			
			//sends back the error response or creates new account if successful
			var xhttp = new XMLHttpRequest();
			xhttp.open("POST", "SignupServlet?username=" + username + "&password=" + password + "&confirmpassword=" + confirmpassword, false);
			xhttp.send();
			
			
			if(xhttp.responseText.trim() != "success"){
				document.getElementById("errormessage").innerHTML = xhttp.responseText;
				return false;
			}
			else
			{
				window.location.href = "http://localhost:8080/signIn.jsp";
				return false;
			}
		}
	</script>
	
	<div id="signdiv">	
			<div id="register_p_div">
				<p id="registerp">Sign Up</p>
			</div>
			<form name="signUpForm" method="post" onsubmit="return checkUsernamePassword();" action="">
				<p id="usernametext" class="formobj">Username</p>
				<input type="text" name="username" id="username" class="inputbar" value="">
				<p id="passwordtext" class="formobj">Password</p>
				<input type="password" name="password" id="password" class="inputbar">
				<p id="confirmpasswordtext" class="formobj">Confirm Password</p>
				<input type="password" name="confirmpassword" id="confirmpassword" class="inputbar">
				<p id="placeholder"><br></p>
 				<input type="submit" name="submit" value="sign up" id="registerbutton" class="formobj">
			</form>
		</div>
		
		<div id="errordiv">
			<p id="errormessage"><br></p>
		</div>
		
		<div id="logindiv">
			<p id="loginp">Already have an account?</p>
			<button id="loginbutton" onclick="location.href='signIn.jsp'">login</button>
		</div>
		
		
		
		<!-- sparkly things -->
		<div id="background">
				<script>
					$(function(){
						$("body").jParticle({
							particlesNumber: 150,
				            linkDist: 50,
				            disableLinks: false,
				            disableMouse: false,
				            createLinkDist: 200,
				            color: '#6BBDDF',
				            width:null,
				            height:null,
				            background: '#fcfcfc',
				            linksWidth: 0.5,
				            particle: 
				            {
				                color: "white",
				                minSize: 1,
				                maxSize: 1,
				                speed: 30
				            }
						});
					});
					</script><script type="text/javascript">
				</script>
			</div>
</body>
</html>
