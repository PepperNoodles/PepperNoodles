<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>AdminLTE 3 | 後台管理系統</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="<c:url value='/plugins/fontawesome-free/css/all.min.css' />">    
  <!-- Ionicons -->
  <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
  <!-- Tempusdominus Bbootstrap 4 -->
  <link rel="stylesheet" href="<c:url value='/plugins/tempusdominus-bootstrap-4/css/tempusdominus-bootstrap-4.min.css' />">
  <!-- iCheck -->
  <link rel="stylesheet" href="<c:url value='/plugins/icheck-bootstrap/icheck-bootstrap.min.css' />">
  <!-- JQVMap -->
  <link rel="stylesheet" href="<c:url value='/plugins/jqvmap/jqvmap.min.css' />">
  <!-- Theme style -->
  <link rel="stylesheet" href="<c:url value='/dist/css/adminlte.min.css' />">
  <!-- 登出bar -->
  <link rel="stylesheet" href="<c:url value='/dist/css/alt/adminlte.min.css' />">
  <link rel="stylesheet" href="<c:url value='/dist/css/alt/adminlte.css' />">
  <link rel="stylesheet" href="<c:url value='/dist/css/alt/adminlte.core.css' />">
  <link rel="stylesheet" href="<c:url value='/dist/css/alt/adminlte.core.min.css' />">
  
  <!-- overlayScrollbars -->
  <link rel="stylesheet" href="<c:url value='/plugins/overlayScrollbars/css/OverlayScrollbars.min.css' />">
  <!-- Daterange picker -->
  <link rel="stylesheet" href="<c:url value='/plugins/daterangepicker/daterangepicker.css' />">
  <!-- summernote -->
  <link rel="stylesheet" href="<c:url value='/plugins/summernote/summernote-bs4.css' />">
  <!-- Google Font: Source Sans Pro -->
  <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700" rel="stylesheet">
  
  <!-- DataTables 資料庫-->
  <link rel="stylesheet" href="<c:url value='/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css' />">
  <link rel="stylesheet" href="<c:url value='/plugins/datatables-responsive/css/responsive.bootstrap4.min.css' />">
</head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body class="hold-transition sidebar-mini layout-fixed">
<div class="wrapper">

  <footer class="main-footer">
    <strong>Copyright &copy; 2014-2019 <a href="http://adminlte.io">AdminLTE.io</a>.</strong>
    All rights reserved.
    <div class="float-right d-none d-sm-inline-block">
      <b>Version</b> 3.0.5
    </div>
  </footer>

</div>
<!-- ./wrapper -->

<!-- jQuery -->
<script src="<c:url value='/plugins/jquery/jquery.min.js' />"></script>
<!-- jQuery UI 1.11.4 -->
<script src="<c:url value='/plugins/jquery-ui/jquery-ui.min.js' />"></script>
<!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
<script>
  $.widget.bridge('uibutton', $.ui.button)
</script>
<!-- Bootstrap 4 -->
<script src="<c:url value='/plugins/bootstrap/js/bootstrap.bundle.min.js' />"></script>
<!-- ChartJS -->
<script src="<c:url value='/plugins/chart.js/Chart.min.js' />"></script>
<!-- Sparkline -->
<script src="<c:url value='/plugins/sparklines/sparkline.js' />"></script>
<!-- JQVMap -->
<script src="<c:url value='/plugins/jqvmap/jquery.vmap.min.js' />"></script>
<script src="<c:url value='/plugins/jqvmap/maps/jquery.vmap.usa.js' />"></script>
<!-- jQuery Knob Chart -->
<script src="<c:url value='/plugins/jquery-knob/jquery.knob.min.js' />"></script>
<!-- daterangepicker -->
<script src="<c:url value='/plugins/moment/moment.min.js' />"></script>
<script src="<c:url value='/plugins/daterangepicker/daterangepicker.js' />"></script>
<!-- Tempusdominus Bootstrap 4 -->
<script src="<c:url value='/plugins/tempusdominus-bootstrap-4/js/tempusdominus-bootstrap-4.min.js' />"></script>
<!-- Summernote -->
<script src="<c:url value='/plugins/summernote/summernote-bs4.min.js' />"></script>
<!-- overlayScrollbars -->
<script src="<c:url value='/plugins/overlayScrollbars/js/jquery.overlayScrollbars.min.js' />"></script>
<!-- AdminLTE App -->
<script src="<c:url value='/dist/js/adminlte.js' />"></script>
<!-- AdminLTE dashboard demo (This is only for demo purposes) -->
<script src="<c:url value='/dist/js/pages/dashboard.js' />"></script>
<!-- AdminLTE for demo purposes -->
<script src="<c:url value='/dist/js/demo.js' />"></script>

<!-- DataTables 資料庫-->
<script src="<c:url value='/plugins/datatables/jquery.dataTables.min.js' />"></script>
<script src="<c:url value='/plugins/datatables-bs4/js/dataTables.bootstrap4.min.js' />"></script>
<script src="<c:url value='/plugins/datatables-responsive/js/dataTables.responsive.min.js' />"></script>
<script src="<c:url value='/plugins/datatables-responsive/js/responsive.bootstrap4.min.js' />"></script>
<!-- page script 資料庫-->
<script>
  $(function () {
    $("#example1").DataTable({
      "responsive": true,
      "autoWidth": false,
    });
    $('#example2').DataTable({
      "paging": true,
      "lengthChange": false,
      "searching": false,
      "ordering": true,
      "info": true,
      "autoWidth": false,
      "responsive": true,
    });
  });
</script>
</body>
</html>
