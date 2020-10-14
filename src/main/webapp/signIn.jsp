<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" href="styleSI.css">
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
<script src="jparticle.jquery.min.js"></script>
<!-- <script src="https://kit.fontawesome.com/dbcc9507e2.js" crossorigin="anonymous"></script> -->
<title>Sign In</title>
</head>
<body id = "start">
	<div class="navbar">
		<div class="wrap">
			<h1>USC CS 310: Stock Portfolio Management</h1>
		</div>
	</div>
	
	<div id="signdiv">			
		<div id="login_p_div">
			<p id="loginp">Login</p>
		</div>
		<form name="loginForm" id="loginForm">
			<p id="usernametext" class="formobj">Username</p>
			<input type="text" name="username" id="username" class="inputbar" value="">
			<p id="passwordtext"class="formobj">Password</p>
			<input type="password" name="password" id="pass" class="inputbar">
			<p id="placeholder"><br></p>
			<input type="submit" name="submit" class="formobj" value="login" id="loginbutton">
		</form>
	</div>
	
	<div id="errordiv">
		<p id="errormessage"><br></p>
	</div>
	
	<div id="registerdiv">
		<p id="registerp">Don't have an account?</p>
		<button id="registerbutton" onclick="location.href='signup.jsp'">sign up</button>
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
	<script>
		let numFailed = 0;
		let url = "api/login";
		
		function parseResponse(response){
			let data = JSON.parse(response);
			console.log(data);
			let num = data; // to be gotten from data;
			if(num == 0){
				//error 1
				//document.body.id = "errorScreen";
				numFailed++;
				//document.querySelector("#pass").style.borderColor = "#ff0033";
				//document.querySelector("#username").style.borderColor = "#ff0033";
				document.getElementById("errormessage").innerHTML = "No user with this username exists";
				//document.querySelector(".login").style.height = "350px";
			}
			else if(num >= 1){
				window.location.href = "homepage.jsp"
			}
			else if(num == -2){
				//error 3
				//document.body.id = "errorScreen";
				numFailed++;
				//document.querySelector("#pass").style.borderColor = "#ff0033";
				//document.querySelector("#username").style.borderColor = "#ff0033";
				document.getElementById("errormessage").innerHTML = "The password you entered is invalid";
				//document.querySelector(".login").style.height = "350px";
			}
			
					
		}
		
		document.getElementById("loginForm").onsubmit = function(event){
			event.preventDefault();
			
			let userName = document.getElementById("username").value;
			let pass = document.getElementById("pass").value;
			
			if(numFailed > 2){
				document.getElementById("loginbutton").enabled = false;
				document.getElementById("errormessage").innerHTML = "You have been locked for failing to sign in three times";
				//document.querySelector(".login").style.height = "350px";
			}	
			else if(userName.length < 1){
				//document.body.id = "errorScreen";
				numFailed++;
				//document.querySelector("#pass").style.borderColor = "#ff0033";
				//document.querySelector("#username").style.borderColor = "#ff0033";
				if(pass.length < 8){
					document.getElementById("errormessage").innerHTML = "Username cannot be empty and password must be at least 8 characters";
					
				}
				else{
					document.getElementById("errormessage").innerHTML = "Username cannot be empty.";
				}
				//document.querySelector(".login").style.height = "350px";
			}
			else if(pass.length < 8){
				//document.body.id = "errorScreen";
				numFailed++;
				//document.querySelector("#pass").style.borderColor = "#ff0033";
				//document.querySelector("#username").style.borderColor = "#ff0033";
				document.getElementById("errormessage").innerHTML = "Please input a password of at least 8 characters";
				//document.querySelector(".login").style.height = "350px";
			}
			else{
				let httpRequest = new XMLHttpRequest();
				httpRequest.open("POST", url, true);
				

				// We will get alerted when backend gives back some kind of response
				httpRequest.onreadystatechange = function(){
					// This function runs when we get some kind of response back from iTunes
					console.log(httpRequest);
					// When we get back a DONE state (readyState == 4, let's do something with it)
					if(httpRequest.readyState == httpRequest.DONE) {
						// Check for errors - status code 200 means success
						if(httpRequest.status == 200) {
							console.log(httpRequest.responseText);

							// Display the results on the browser - a separate function is created for this purpose
							parseResponse(httpRequest.responseText);

						}
						else {
							console.log("AJAX Error!!");
							console.log(httpRequest.status);
							console.log(httpRequest.statusText);
						}
						
					}
				}
				
				httpRequest.send(JSON.stringify({
			        "username": userName,
			        "password": pass
			    })); 
			}
		}
	</script>
</body>
</html>
