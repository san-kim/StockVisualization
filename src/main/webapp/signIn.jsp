<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" href="styleSI.css">
<script src="https://kit.fontawesome.com/dbcc9507e2.js" crossorigin="anonymous"></script>
<title>Sign In</title>
</head>
<body id = "start">
	<div class="navbar">
		<div class="wrap">
			<h1>USC CS 310: Stock Portfolio Management</h1>
		</div>
	</div>
	<div class="login">
		<form id="logInForm">
			<h3>Login</h3>
			<div>
				<label for="username">Username:</label>
		    <input type="text" id="username" name="username">
			</div>
			<br/>
			<div>
				<label for="pass">Password (8 characters minimum):</label>
				<input type="password" id="pass" name="password">
			</div>
			<br/>
			<div id="subButton">
				<button type="submit" class="sign-in-button"><i class="fas fa-sign-out-alt"></i>&nbsp&nbspSign In</button>
			</div>
			<div id = "signUpLink">
				<a href = "signup.jsp">Don't have an account? Sign Up!</a>
			</div>
			<div id = "errorMessage">
				
			</div>
		</form>
		
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
				document.body.id = "errorScreen";
				numFailed++;
				document.querySelector("#pass").style.borderColor = "#ff0033";
				document.querySelector("#username").style.borderColor = "#ff0033";
				document.querySelector("#errorMessage").innerHTML = "<p id = \"Merror\">No user with this username exists</p>";
				document.querySelector(".login").style.height = "350px";
			}
			else if(num >= 1){
				window.location.href = "homepage.jsp"
			}
			else if(num == -2){
				//error 3
				document.body.id = "errorScreen";
				numFailed++;
				document.querySelector("#pass").style.borderColor = "#ff0033";
				document.querySelector("#username").style.borderColor = "#ff0033";
				document.querySelector("#errorMessage").innerHTML = "<p id = \"Merror\">The password you entered is invalid</p>";
				document.querySelector(".login").style.height = "350px";
			}
			
					
		}
		
		document.querySelector("#logInForm").onsubmit = function(event){
			event.preventDefault();
			
			let userName = document.querySelector("#username").value;
			let pass = document.querySelector("#pass").value;
			
			if(numFailed > 2){
				document.querySelector(".sign-in-button").enabled = false;
				document.querySelector("#errorMessage").innerHTML = "<p id = \"Merror\">You have been locked for failing to sign in three times</p>";
				document.querySelector(".login").style.height = "350px";
			}	
			else if(userName.length < 1){
				document.body.id = "errorScreen";
				numFailed++;
				document.querySelector("#pass").style.borderColor = "#ff0033";
				document.querySelector("#username").style.borderColor = "#ff0033";
				if(pass.length < 8){
					document.querySelector("#errorMessage").innerHTML = "<p id = \"Merror\">Please input a username of at least 1 character as well as a password of atleast 8 characters</p>";
					
				}
				else{
					document.querySelector("#errorMessage").innerHTML = "<p id = \"Merror\">Please input a username of at least 1 character</p>";
				}
				document.querySelector(".login").style.height = "350px";
			}
			else if(pass.length < 8){
				document.body.id = "errorScreen";
				numFailed++;
				document.querySelector("#pass").style.borderColor = "#ff0033";
				document.querySelector("#username").style.borderColor = "#ff0033";
				document.querySelector("#errorMessage").innerHTML = "<p id = \"Merror\">Please input a password of at least 8 characters</p>";
				document.querySelector(".login").style.height = "350px";
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
