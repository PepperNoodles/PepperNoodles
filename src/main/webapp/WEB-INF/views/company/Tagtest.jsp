<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Document</title>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/typeahead.js/0.11.1/typeahead.bundle.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-tagsinput/0.8.0/bootstrap-tagsinput.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-tagsinput/0.8.0/bootstrap-tagsinput.css">

</head>
<body>
	<div>
		<input id="input1" type="text" />
	</div>
	<button id="click1">click1</button>
	<div>
	<select multiple data-role="tagsinput">
		<option value="Amsterdam">Amsterdam</option>
		<option value="Washington">Washington</option>
		<option value="Sydney">Sydney</option>
		<option value="Beijing">Beijing</option>
		<option value="Cairo">Cairo</option>
	</select>
	
	<button id="select1">select1</button>
	</div>


	<script>
		var foodTags = new Bloodhound({

			datumTokenizer : Bloodhound.tokenizers.obj.whitespace('text'),
			queryTokenizer : Bloodhound.tokenizers.whitespace,
			//   prefetch: `${pageContext.request.contextPath}/data/test.json`
			remote : `${pageContext.request.contextPath}/foodTagJson`

		});

		foodTags.initialize();

		var elt = $('input');
		elt.tagsinput({
			maxTags: 5,
			itemValue : 'value',
			itemText : 'text',

			typeaheadjs : {
				limit:100,
				name : 'foodTags',
				displayKey : 'text',
				source : foodTags.ttAdapter()
			}
	
		});
		elt.tagsinput('add', {
			"value" : 1,
			"text" : "curry",
		});
		elt.tagsinput('add', {
			"value" : 2,
			"text" : "BBQ",
		});
		elt.tagsinput('add', {
			"value" : 3,
			"text" : "pizza",
		});
		elt.tagsinput('add', {
			"value" : 13,
			"text" : "約會餐廳",
		});

	</script>

	<script type="text/javascript">
		$(document).ready(function() {
			$('#click1').click(function() {
				console.log('tag1='+$("#input1").val());

			});
			
			$('#select1').click(function() {
				console.log('select1='+$("select").val());

			});
		});
	</script>


</body>
</html>