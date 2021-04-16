<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>BackStage</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/fontawesome/css/all.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/bootstrap4.6/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script src="bootstrap4.6/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">

<style>
img {
	height: 150px;
}

.imgtr {
	height: 150px;
}
</style>

</head>
<body>
	<!-- TOP nav -->
	<nav
		class="navbar navbar-expand-lg navbar-light bg-secondary sticky-top"
		id="headerBar">
		<a class="navbar-brand" href="#">
			<p class="mb-0 h2 text-white">後台管理系統</p>
		</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbarSupportedContent"
			aria-controls="navbarSupportedContent" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse " id="navbarSupportedContent">
			<!--.mr-auto (push items to the right), or by using .ml-auto (push items to the left):-->
			<ul class="navbar-nav ml-auto">
				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle text-white" href="#"
					id="navbarDropdown" role="button" data-toggle="dropdown"
					aria-haspopup="true" aria-expanded="false"> 現在使用者${loginUser} </a>
					<div class="dropdown-menu" aria-labelledby="navbarDropdown">
						<a class="dropdown-item" href="#">Action</a> <a
							class="dropdown-item" href="#">Another action</a>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item" href=<c:url value="logout.controller"/>>登出</a>
					</div></li>

				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
					role="button" data-toggle="dropdown" aria-haspopup="true"
					aria-expanded="false"> <i class="text-white fas fa-bell"></i>
				</a>
					<div class="dropdown-menu" aria-labelledby="navbarDropdown">
						<a class="dropdown-item" href="#">Action</a> <a
							class="dropdown-item" href="#">Another action</a>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item" href="#"></a>
					</div></li>
			</ul>
		</div>
		</div>
	</nav>


	<div class="wrapper">
		<!-- side bar -->
		<nav id="sidebar">


			<ul class="lisst-unstyled components">
				<li><a href="#">Home</a></li>

				<li class="active">
					<!--bootStrap collapse用法 --> <a data-toggle="collapse"
					href="#homeSubmenu" aria-expanded="false"
					aria-controls="collapseExample"> 會員管理</a>
					<ul class="collapse lisst-unstyled list-group" id="homeSubmenu">
						<li class="list-group-item"><a href="#">一般會員</a></li>
						<li class="list-group-item"><a href="#">企業會員</a></li>
					</ul>
				</li>
				<li><a href="#">檢舉系統</a></li>

				<li><a hred="#">數據分析</a></li>
				<li><a href="">待討論功能</a></li>

			</ul>

		</nav>

		<div id="content">
			<nav class="navbar navbar-expand-lg navbar-light bg-light"></nav>
			<div class="container-fluid">
				<button type="button" id="sidebarCollapse" class="btn btn-info">
					<i class="fas fa-align-left"></i>
					<!--那個照片-->
					<span>toggle sidebar</span>
				</button>
			</div>

			<a href="<c:url value='accountMemberAdd'/>">add new member</a> <br>
			<br>
			<table border="1">
				<tr>
					<th>AccountID
					<th>Account
					<th>pwd
					<th>realname
					<th>birthday
					<th>sex
					<th>userPortrait(from file)
					<th>userPortrait(from DB)
					<th>update
					<th>delete
				</tr>
				<c:forEach items="${allmember}" var="accMember">
					<tr class="imgtr">
						<td>${accMember.accountID}
						<td>${accMember.account}
						<td>${accMember.password}
						<td>${accMember.realName}
						<td>${accMember.birthday}
						<td>${accMember.sex}
						<td><img
							src="${pageContext.request.contextPath}/images/${accMember.protraitName}">
						<td><img
							src="<c:url value='/accountMember.pic'/>?accountId=${accMember.accountID}">
						<td>
							<form action="accMemUpdateGet" method="get">
								<input id="update" type="hidden" name="accMemId"
									value="${accMember.accountID}">
								<button type="submit">修改</button>
							</form>
						<td><a
							href="<c:url value='/delectAccountMember'/>?accountId=${accMember.accountID}"
							onclick="javascript:return del()">刪除</a>
					</tr>
				</c:forEach>

			</table>

		</div>

	</div>

	<script>
		$(document).ready(function() {
			$('#sidebarCollapse').on('click', function() {
				$('#sidebar').toggleClass('leftslide', 2);
			})

		})

		function del() {
			let msg = "您真的確定要刪除嗎？\n\n請確認！";
			if (confirm(msg) == true) {
				return true;
			} else {
				return false;
			}
		}
	</script>

</body>
</html>