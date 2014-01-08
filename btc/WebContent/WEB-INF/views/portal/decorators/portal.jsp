<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!-->
<!DOCTYPE html>
<!--<![endif]-->
<html lang="en">
<head>
<title><decorator:title default="" /></title>
<%@include file="../includes/style.jsp"%>
<link type="text/css" rel="stylesheet" href="/css/main.css" media="screen">
<decorator:head />
</head>
<body class="fixed-top">
	<%@include file="../includes/header.jsp"%>
	<%@include file="../includes/leftside.jsp"%>
	<!-- BEGIN PAGE -->
	<div id="main-content">
		<div class="container-fluid">
			<!-- BEGIN PAGE HEADER-->
			<div class="row-fluid">
				<div class="span12">
					<decorator:body />
				</div>
			</div>
		</div>
	</div>
	<!-- END PAGE -->

	<%@include file="../includes/footer.jsp"%>
</body>
</html>