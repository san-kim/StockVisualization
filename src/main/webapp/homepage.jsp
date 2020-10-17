<%@ page import="csci310.*" %>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="style.css">
	<title>Home</title>
	<script src="https://kit.fontawesome.com/dbcc9507e2.js" crossorigin="anonymous"></script>
	<link rel="icon" href=https://www.daytrading.com/favicon32x32.png type="image/ico" />

	<script>
		// Log out user after 120 seconds of inactivity
		var userInactivity = function () {
		    var time;
		    window.onload = resetTimer;
		    // Reset timer whenever user moves mouse or presses key
		    document.onmousemove = resetTimer;
		    document.onkeypress = resetTimer;

		    function logout() {
		    	// Alert user logout
		        alert("You have been logged out due to two minutes of inactivity.")
		        // Redirect to sign in page
		        location.href = "signIn.jsp";
		    }
		    // Reset the timer
		    function resetTimer() {
		        clearTimeout(time);
		        time = setTimeout(logout, 12000000);
		    }
		};
		// Load timer on page load
		window.onload = function() {
			userInactivity();
		}
	</script>

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script>


</head>



<%--
<body>
<div class="col-md-9 col-sm-9 ">
  <!-- <div id="chart_plot_01" class="demo-placeholder"></div> -->



</div>
<div class="col-md-3 col-sm-3  bg-white">
  <div class="x_title">
    <h2>Toggle Graph</h2>
    <div class="clearfix"></div>
  </div>


</div>
</body> --%>






<body>
	<div class="navbar">
		<div class="wrap">
			<h1>USC CS 310: Stock Portfolio Management</h1>
			<a href="signIn.jsp" class="button"><i class="fas fa-sign-out-alt"></i>&nbsp&nbspSign Out</a>
		</div>
	</div>
    <div class="wrap">
    	<div class="grid-container">

	    	<div class="homepage-container" id="graph-container">
	    		<div class="graph-header">
	    			<span id="portfolio-value">$1,349.32</span>
	    			<div id="portfolio-value-change" style="color: #51C58E;">
    					<span id="arrow"></span>+3.25% Today
	    			</div>
	    		</div>
					<%-- &#9650 --%>
					<div style="position:relative;">
						<div style="height:300px !important;">
				    	<canvas id="myChart"  style="top: 0px; left: 0px;"></canvas>
						</div>
				    <div style="height:50px;width:20px;position:absolute;bottom:0;right:0;margin-bottom:32px;margin-right:44px;background:white;border-radius:5px;border: 2px solid #51C58E;">
				      <div style="height:25px;width:20px;text-align:center;font-weight:bolder;">
				        <button type="button" id="graphzoomin" style="border:none;text-align:center;margin-left:-1px;margin-top:3px;background-color:transparent;font-size:13pt;font-weight:bolder;color:#51C58E;">+</button>
				      </div>
				      <hr style="margin-top:0px;margin-bottom:0px;border:1px solid #51C58E;">
				      <div style="height:25px;width:20px;text-align:center;font-weight:bolder;">
				        <button type="button" id="graphzoomout" style="border:none;text-align:center;margin-left:-1px;margin-top:-2px;background-color:transparent;font-size:16pt;font-weight:bolder;color:#51C58E;">-</button>
				      </div>
				    </div>
				  </div>
					<br>
					<button type="button" class="button" id="spytoggle" >Remove S&amp;P</button>
				  <button type="button" class="button" id="stocktoggle" >Add Stock</button>
					<input type="date" class="graph-date" id="date-start-graph" placeholder="StartDate">
					<input type="date" class="graph-date" id="date-end-graph" placeholder="EndDate">
					<button type="button" class="button" id="render-new-dates" >Update Graph</button>
					<br>
					<br>
					<form>
					  <label for="cars">Change frequency:</label>
					  <select class="graph-date" id="data-per-month-select">
					    <option value="monthly">Monthly</option>
					    <option value="semi-weekly">Semi-weekly</option>
					    <option value="weekly">Weekly</option>
					  </select>
						<button type="button" class="button" id="render-new-data-per-month" >Submit</button>
					</form>
					<script>
						$(document).ready(function(){

						});
					</script>
	    	</div> <!-- #graph-container -->

				<script>
				// Creating the original chart

				var lineColors = ["#f5f5dc", "#000000", "#0000ff", "#a52a2a", "#00ffff", "#00008b", "#008b8b", "#a9a9a9", "#006400", "#bdb76b", "#8b008b", "#ff8c00", "#9932cc", "#8b0000", "#e9967a", "#9400d3", "#008000", "#f0e68c", "#add8e6", "#e0ffff", "#90ee90", "#d3d3d3", "#800000", "#000080", "#808000", "#ffa500", "#ffc0cb", "#800080", "#800080", "#ff0000", "#c0c0c0", "#00ffff", "#ffb6c1", "#00ff00", "#ff00ff", "#556b2f", "#ffd700", "#4b0082"]

				var ctx = document.getElementById('myChart').getContext('2d');
				var months_to_show = 3
				var data_per_month = 2
				var offset_end_date_from_today = 0
				var show_portfolio = true
				var show_spy = true

				var chartDatasets = [
					{
						label: 'Porfolio Value',
						data: generateValues('Portfolio'),
						backgroundColor: 'rgba(255, 99, 132, 0)',
						borderColor: takeRandomColor(),
						borderWidth: 3
				},
				{
						label: 'NYSE:SPY',
						data: generateValues('NYSE:SPY'),
						backgroundColor: 'rgba(255, 99, 132, 0)',
						borderColor: takeRandomColor(),
						borderWidth: 3
				}]


				function takeRandomColor() {
					return lineColors.pop();
				}

				function addBackRandomColor(color) {
					lineColors.push(color);
				}

				var myChart = new Chart(ctx, {
						type: 'line',
						data: {
								labels: generateLabels(),
								datasets: chartDatasets
						},
						options: {
								scales: {
										yAxes: [{
												ticks: {
														beginAtZero: true
												}
										}]
								},
								responsive:true,
    						maintainAspectRatio: false
						}
				});

				function renderGraph() {

					// Before we re-render the graph we need to update the data
					var i;
					for (i = 0; i < chartDatasets.length; i++) {
						label = chartDatasets[i]["label"];
						chartDatasets[i]["data"] = generateValues(label);
					}

					myChart = new Chart(ctx, {
							type: 'line',
							data: {
									labels: generateLabels(),
									datasets: chartDatasets
							},
							options: {
									scales: {
											yAxes: [{
													ticks: {
															beginAtZero: true
													}
											}]
									},
									animation: {
											duration: 0
									},
									responsive:true,
    							maintainAspectRatio: false
							}
					});
					myChart.update();
					console.log(myChart);
				}

				function generateLabels() {
					const monthNames = ["January-16", "February-16", "March-16", "April-16", "May-16", "June-16", "July-16", "August-16", "September-16", "October-16", "November-16", "December-16", "January-17", "February-17", "March-17", "April-17", "May-17", "June-17", "July-17", "August-17", "September-17", "October-17", "November-17", "December-17", "January-18", "February-18", "March-18", "April-18", "May-18", "June-18", "July-18", "August-18", "September-18", "October-18", "November-18", "December-18", "January-19", "February-19", "March-19", "April-19", "May-19", "June-19", "July-19", "August-19", "September-19", "October-19", "November-19", "December-19", "January-20", "February-20", "March-20", "April-20", "May-20", "June-20", "July-20", "August-20", "September-20", "October-20", "November-20", "December-20"];
					const d = new Date();
					var thisMonth = d.getMonth() + 48 - months_to_show - offset_end_date_from_today;
					var label = [];
					var i;
					for (i = 0; i < months_to_show; i++) {
						label.push(monthNames[thisMonth]);
						thisMonth = thisMonth + 1;

						var j;
						for (j = 1; j < data_per_month; j++) {
							label.push("");
						}
					}
					return label;
				}

				function generateValues(label) {
					var values = [];
					var datapoints = data_per_month * months_to_show;
					console.log(datapoints)
					var i;
					var last_val = Math.floor(Math.random() * (20 - 10 + 1)) + 10;
					for (i = 0; i < datapoints; i++) {
						last_val = last_val + Math.floor(Math.random() * 2 - .5);
						values.push(last_val);
					}
					return values;
				}

				console.log(myChart.data.datasets);





				// Actual chart updating triggers
				$(document).ready(function(){

					// Getting a random color
					function takeRandomColor() {
						return lineColors.pop();
					}

					// Chart updating functions
					function addData(chart, label, data) {
						// chart.data.labels.push(label);
						chartDatasets.push(data);
						renderGraph();
					}

					function removeData(chart, label) {
						var i;
						for (i = 0; i < chartDatasets.length; i++) {
							if(chartDatasets[i]["label"] == label){
								addBackRandomColor(chartDatasets[i]["borderColor"]);
								chartDatasets.splice(i,1);
							}
						}
						renderGraph();
					}

					function addStock(ticker) {
						console.log("adding stock:");
						console.log(ticker);

						var viewed_stock_string = "Viewed Stock: ";

						var labels = generateLabels();

						var chartData = {
								label: viewed_stock_string.concat(ticker),
								data: generateValues(ticker),
								backgroundColor: 'rgba(255, 99, 132, 0)',
								borderColor: takeRandomColor(),
								borderWidth: 3
						};
						addData(myChart, labels, chartData);
					}

					function removeStock(ticker) {
						console.log("removing stock:");
						console.log(ticker);

						var viewed_stock_string = "Viewed Stock: ";
						removeData(myChart, viewed_stock_string.concat(ticker));
					}



					$("#spytoggle").click(function(){
						console.log("spy toggling")
						var buttonText = $("#spytoggle").text();
						console.log(buttonText);
						var addSPY = false;
						if(buttonText == "Add S&P"){
							addSPY = true;
						}
						if(buttonText == "Remove S&P"){
							addSPY = false;
						}

						labels = generateLabels()

						if(addSPY){
							var chartData = {
									label: 'NYSE:SPY',
									data: generateValues('NYSE:SPY'),
									backgroundColor: 'rgba(255, 99, 132, 0)',
									borderColor: takeRandomColor(),
									borderWidth: 3
							};
							addData(myChart, labels, chartData);
							$("#spytoggle").html("Remove S&P");
							console.log("add spy");
							// document.getElementById("spytoggle").style.background='#fff';
							// document.getElementById("spytoggle").style.color='#007bff';
						}else{
							removeData(myChart, "NYSE:SPY");
							$("#spytoggle").html("Add S&P");
							console.log("remove spy");
							// document.getElementById("spytoggle").style.background='#007bff';
							// document.getElementById("spytoggle").style.color='#fff';
						}



					});

					$("#stocktoggle").click(function(){
						console.log("stock toggling")
						var buttonText = $("#stocktoggle").text();
						console.log(buttonText);
						var addStock = false;
						if(buttonText == "Add Stock"){
							addStock = true;
						}
						if(buttonText == "Remove Stock"){
							addStock = false;
						}

						labels = generateLabels()

						if(addStock){
							var chartData = {
									label: 'Stock Value ($ mil USD)',
									data: generateValues('Stock'),
									backgroundColor: 'rgba(255, 99, 132, 0)',
									borderColor: takeRandomColor(),
									borderWidth: 3
							};
							addData(myChart, labels, chartData);
							$("#stocktoggle").html("Remove Stock");
							console.log("add stock");
							// document.getElementById("stocktoggle").style.background='#fff';
							// document.getElementById("stocktoggle").style.color='#007bff';
						}else{
							removeData(myChart, "Stock Value ($ mil USD)");
							$("#stocktoggle").html("Add Stock");
							console.log("remove stock");
							// document.getElementById("stocktoggle").style.background='#007bff';
							// document.getElementById("stocktoggle").style.color='#fff';
						}



					});

					$("#graphzoomin").click(function(){
						months_to_show = months_to_show/2;
						if(months_to_show < 6){
							data_per_month = data_per_month*2;
						}

						renderGraph();
					});

					$("#graphzoomout").click(function(){
						months_to_show = months_to_show*2;
						if(data_per_month > 1){
							data_per_month = data_per_month/2;
						}
						renderGraph();
					});

					$("#render-new-dates").click(function(){
						var date_start = new Date($('#date-start-graph').val());
						var date_end = new Date($('#date-end-graph').val());
						const today = new Date();
						var time_difference = date_end.getTime() - date_start.getTime();
						var day_difference = time_difference / (1000 * 3600 * 24);
						months_to_show = Math.round(day_difference / 30);
						offset_end_date_from_today = Math.round((today.getTime()- date_end.getTime()) / (1000 * 3600 * 24 * 30))-1;

						renderGraph();

						console.log(months_to_show);

					});

					$("#render-new-data-per-month").click(function(){
						var frequencyLabel = $('#data-per-month-select').val();

						if(frequencyLabel == "monthly"){
							data_per_month = 1;
						}
						if(frequencyLabel == "semi-weekly"){
							data_per_month = 2;
						}
						if(frequencyLabel == "weekly"){
							data_per_month = 4;
						}

						renderGraph();

						console.log(frequencyLabel);

					});



					// This controls calls to viewed stock checks
					$('.viewed_stock').click(function(){
				    if($(this).is(':checked')){
				        var id = $(this).attr('id');
								addStock(id);
				    } else {
							var id = $(this).attr('id');
							removeStock(id);
				    }
					});





				});





				</script>







	    	<div class="homepage-container" id="portfolio-container">
	    		<div class="container-header">
	    			Your Portfolio
	    			<div class="add-stocks-container">

	    				<button class="button" id="add-stock-button">Add Stock</button>
	    				<div class="modal" id="add-stock-modal">
	    					<div class="modal-box">
	    						<div class="popup-header">Add Stock</div>
	    						<div class="popup-section">
	    							<form id="add-stock-form">
	    								<div class="form-row">
	    									<label for="ticker">Stock Ticker</label>
	    									<input type="text" id="ticker">
	    								</div>
	    								<div class="form-row">
	    									<label for="ticker"># of Shares</label>
	    									<input type="number" id="shares">
	    								</div>
	    								<div class="form-row">
	    									<label for="date-purchased">Date Purchased</label>
	    									<input type="date" id="date-purchased" placeholder="yyyy-mm-dd">
	    								</div>
	    								<div class="form-row">
	    									<label for="date-sold">Date Sold</label>
	    									<input type="date" id="date-sold" placeholder="yyyy-mm-dd">
	    								</div>
	    								<div class="form-row">
	    									<span class="error-msg">Test error message</span>
	    								</div>

	    								<button type="submit" class="button" id="add-stock-submit">Add Stock</button>
	    							</form>
	    						</div>
	    						<div class="popup-section">
	    							<button class="button" id="add-stock-cancel">Cancel</button>
	    						</div>
	    					</div>
	    				</div>

	    				<button class="button" id="import-stock-button">Import CSV</button>
	    				<div class="modal" id="import-stock-modal">
	    					<div class="modal-box">
	    						<div class="popup-header">Import Stocks</div>
	    						<div class="popup-section">
	    							<form id="import-stock-form">
	    								<div class="form-row">
	    									<label for=""csvImport"">Upload a .csv file</label>
	    									<input type="file" id="csvImport" accept=".csv">
	    								</div>
	    								<div class="form-row">
	    									<span class="error-msg">Test error message</span>
	    								</div>
	    								<button type="submit" class="button" id="import-stock-submit">Import Stocks</button>
	    							</form>
	    						</div>
	    						<div class="popup-section">
	    							<button class="button" id="import-stock-cancel">Cancel</button>
	    						</div>
	    					</div>
	    				</div>

	    			</div>
	    		</div> <!-- .container-header -->

	    		<table id="stock-list">
	    			<tr>
	    				<td>Apple</td>
	    				<td>AAPL</td>
	    				<td>
	    					<label class="switch">
	    						<input id="AAPL" class="portfolio_stock" type="checkbox" unchecked>
							  	<span class="slider round"></span>
							</label>
						</td>
	    				<td><a href=""><i class="fas fa-trash"></i></a></td>
	    			</tr>
	    			<tr>
	    				<td>Tesla</td>
	    				<td>TSLA</td>
	    				<td>
	    					<label class="switch">
	    						<input id="TSLA" class="portfolio_stock" type="checkbox" unchecked>
							  	<span class="slider round"></span>
							</label>
						</td>
	    				<td><a href=""><i class="fas fa-trash"></i></a></td>
	    			</tr>
	    			<tr>
	    				<td>Ford Motor</td>
	    				<td>F</td>
	    				<td>
	    					<label class="switch">
	    						<input id="F" class="portfolio_stock" type="checkbox" unchecked>
							  	<span class="slider round"></span>
							</label>
						</td>
	    				<td><a href=""><i class="fas fa-trash"></i></a></td>
	    			</tr>
	    		</table>
	    	</div>  <!-- .homepage-container -->
	    	<div class="homepage-container" id="viewed-container">
	    		<div class="container-header">
	    			Viewed Stocks
	    			<div class="add-stocks-container">

	    				<button class="button" id="view-stock-button">View Stock</button>
	    				<div class="modal" id="view-stock-modal">
	    					<div class="modal-box">
	    						<div class="popup-header">View Stock</div>
	    						<div class="popup-section">
	    							<form id="view-stock-form">
	    								<div class="form-row">
	    									<label for="ticker">Stock Ticker</label>
	    									<input type="text" id="ticker-two">
	    								</div>
	    								<div class="form-row">
	    									<label for="ticker"># of Shares</label>
	    									<input type="number" id="shares-two">
	    								</div>
	    								<div class="form-row">
	    									<label for="date-purchased">Date Purchased</label>
	    									<input type="date" id="date-purchased-two" placeholder="yyyy-mm-dd">
	    								</div>
	    								<div class="form-row">
	    									<label for="date-sold">Date Sold</label>
	    									<input type="date" id="date-sold-two" placeholder="yyyy-mm-dd">
	    								</div>
	    								<div class="form-row">
	    									<span class="error-msg">Test error message</span>
	    								</div>
	    								<button type="submit" class="button" id="view-stock-submit">View Stock</button>
	    							</form>
	    						</div>
	    						<div class="popup-section">
	    							<button class="button" id="view-stock-cancel">Cancel</button>
	    						</div>
	    					</div>
	    				</div>

	    			</div>
	    		</div> <!-- .container-header -->

	    		<table id="stock-list">
	    			<tr>
	    				<td>Apple</td>
	    				<td>AAPL</td>
	    				<td>
	    					<label class="switch">
	    						<input id="AAPL" class="viewed_stock" type="checkbox" unchecked>
							  	<span class="slider round"></span>
							</label>
						</td>
	    				<td><a href=""><i class="fas fa-trash"></i></a></td>
	    			</tr>
	    			<tr>
	    				<td>Tesla</td>
	    				<td>TSLA</td>
	    				<td>
	    					<label class="switch">
	    						<input id="TSLA" class="viewed_stock" type="checkbox" unchecked>
							  	<span class="slider round"></span>
							</label>
						</td>
	    				<td><a href=""><i class="fas fa-trash"></i></a></td>
	    			</tr>
	    			<tr>
	    				<td>Ford Motor</td>
	    				<td>F</td>
	    				<td>
	    					<label class="switch">
	    						<input id="F" class="viewed_stock" type="checkbox" unchecked>
							  	<span class="slider round"></span>
							</label>
						</td>
	    				<td><a href=""><i class="fas fa-trash"></i></a></td>
	    			</tr>
	    		</table>

	    	</div>  <!-- .homepage-container -->
    	</div>
    </div>



    <!-- Add stock popup box -->
	<script>
		var addStockModal = document.getElementById("add-stock-modal");
		var addStockButton = document.getElementById("add-stock-button");
		var addStockCancelButton = document.getElementById("add-stock-cancel");

		// When user clicks add stock button
		addStockButton.onclick = function() {
			document.getElementById("add-stock-form").reset();
			addStockModal.style.display = "flex";
		}
		// When user cancels adding a stock
		addStockCancelButton.onclick = function() {
			addStockModal.style.display = "none";
		}
		// When the user clicks anywhere outside of the box, close it
		window.onclick = function(event) {
			if (event.target == addStockModal) {
				addStockModal.style.display = "none";
			} else if (event.target == importStockModal) {
				importStockModal.style.display = "none";
			} else if (event.target == viewStockModal) {
				viewStockModal.style.display = "none";
			}
		}
	</script>

	<!-- Import stocks popup box -->
	<script>
		var importStockModal = document.getElementById("import-stock-modal");
		var importStockButton = document.getElementById("import-stock-button");
		var importStockCancelButton = document.getElementById("import-stock-cancel");

		// When user clicks add stock button
		importStockButton.onclick = function() {
			document.getElementById("import-stock-form").reset();
			importStockModal.style.display = "flex";
		}
		// When user cancels adding a stock
		importStockCancelButton.onclick = function() {
			importStockModal.style.display = "none";
		}
	</script>

	<!-- View stocks popup box -->
	<script>
		var viewStockModal = document.getElementById("view-stock-modal");
		var viewStockButton = document.getElementById("view-stock-button");
		var viewStockCancelButton = document.getElementById("view-stock-cancel");

		// When user clicks add stock button
		viewStockButton.onclick = function() {
			document.getElementById("view-stock-form").reset();
			viewStockModal.style.display = "flex";
		}
		// When user cancels adding a stock
		viewStockCancelButton.onclick = function() {
			viewStockModal.style.display = "none";
		}
	</script>

</body>
</html>
