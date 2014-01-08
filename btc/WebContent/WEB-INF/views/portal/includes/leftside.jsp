<%@ page language="java" pageEncoding="UTF-8"%>
<div id="sidebar" class="nav-collapse collapse"><!-- BEGIN SIDEBAR TOGGLER BUTTON -->
<div class="sidebar-toggler hidden-phone"></div>
<!-- BEGIN SIDEBAR TOGGLER BUTTON --> <!-- BEGIN RESPONSIVE QUICK SEARCH FORM -->
<div class="navbar-inverse">
<form class="navbar-search visible-phone"><input type="text"
	class="search-query" placeholder="Search"></form>
</div>
<ul class="sidebar-menu">
	<li class="has-sub  open"><a href="javascript:void(0);" class=""><span
		class="icon-box"><i class="icon-file-alt"></i></span>市场数据<span
		class="arrow open"></span></a>
	<ul class="sub" style="display: block;">
		<li><a href="/data/ticker.html">实时行情</a></li>
		<li><a href="/data/depth.html">深度行情</a></li>
	</ul>
	</li>
	<li class="has-sub  open"><a href="javascript:void(0);" class=""><span
		class="icon-box"><i class="icon-file-alt"></i></span>交易面板<span
		class="arrow open"></span></a>
	<ul class="sub" style="display: block;">
		<li><a href="/trade/buy.html">买入比特币</a></li>
		<li><a href="/trade/sell.html">卖出比特币</a></li>
		<li><a href="/trade/orders.html">委托管理</a></li>
	</ul>
	</li>
</ul>
</div>