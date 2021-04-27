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
	<div>	<input type="text" />
	</div>

<script>
var foodTags = new Bloodhound({
  datumTokenizer: Bloodhound.tokenizers.obj.whitespace('text'),
  queryTokenizer: Bloodhound.tokenizers.whitespace,
  prefetch: 'http://localhost:9090/PepperNoodles/data/test.json'

});
foodTags.initialize();

var elt = $('input');
elt.tagsinput({
  itemValue: 'value',
  itemText: 'text',
  typeaheadjs: {
    name: 'foodTags',
    displayKey: 'text',
    source: foodTags.ttAdapter()
  }
});
elt.tagsinput('add', { "value": 1 , "text": "cruuy"   , "continent": "Asia"    });
elt.tagsinput('add', { "value": 2 , "text": "BBQ"  , "continent": "Asia"   });
elt.tagsinput('add', { "value": 3 , "text": "pizza"      , "continent": "Asia" });

</script>


</body>
</html>