<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<body>
	<div class="row-fluid">
		<div class="span12">
			<!-- END THEME CUSTOMIZER-->
			<ul class="breadcrumb">
				<li><a href="/"><i class="icon-home"></i></a><span
					class="divider">&nbsp;</span></li>
				<li><a href="javascript;,">交易面板</a> <span class="divider">&nbsp;</span></li>
				<li><a href="javascript;,">委托管理</a> <span class="divider-last">&nbsp;</span></li>
			</ul>
		</div>
	</div>
	<!-- BEGIN ADVANCED TABLE widget-->
	<div class="row-fluid">
		<div class="span12">
			<div class="widget">
				<div class="widget-header">
					<h5>委托管理</h5>
					<form id="ordersForm" commandName="formParam" method="post" class="form-inline">
						<ul class="widget-nav">
							<li class="search-col">交易市场:</li>
							<li class="search-col">
								<select name="market">
							    	<option value="btcchina">BTCChina</option>
							    	<option value="okcoin">OKCoin</option>
							    	<option value="chbtc">CHBTC</option>
		                        </select>
				            </li>
				            <li class="search-col">用户名:</li>
				            <li class="search-col"><input name="username" class="span12"  maxlength="20" /> </li>
				            <li class="search-col">密码:</li>
				            <li class="search-col"><input name="password" type="password" class="span12"  maxlength="20" /> </li>
							<li class="search-col"><button id="submit" type="button" class="button button-turquoise small-button">查询</button></li>
						</ul>
					</form>
				</div>
		        <div id="messages">
                </div>
				<div class="widget-body table-container">
					<div class="dataTables_wrapper">
						<table id="ordersTable" class="default-table stripped turquoise dataTable">
							<thead>
								<tr align="left">
								   <th>#</th>
			                       <th>委托时间</th>
			                       <th>买卖标志</th>
			                       <th>委托数量</th>
			                       <th>委托价格</th>
			                       <th>成交数量</th>
			                       <th>尚未成交</th>
			                       <th>操作/状态</th>
								</tr>
							</thead>
							<tbody id="data">
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
<script type="text/javascript">
	function cancelOrder(id){
			if( !confirm('确认撤单？') ) return;
			$.ajax({
                cache: true,
                type: "POST",
                url:"/api/trade/cancel/id/"+id,
                data:$('#ordersForm').serialize(),// 你的formid
                async: false,
                error: function(request) {
                    alert("连接异常");
                },
                success: function(data) {
	                alert(data["message"]);
                	if( data["recode"] == 0 ){
	                   	reloadOrder();
	                }
                }
            });
	}
	function reloadOrder(){
		$.ajax({
                cache: true,
                type: "POST",
                url:"/api/trade/orders",
                data:$('#ordersForm').serialize(),// 你的formid
                async: false,
                error: function(request) {
                    alert("连接异常");
                },
                success: function(data) {
                	if( data["recode"] == 0 ){
	                    var result = data["result"];
	                    var htmlMessage = "";
	                    for( var i = 0; i < result.length; i ++){
		                	htmlMessage += "<tr class=\"gradeX "+( i%2 == 0 ? 'odd':'even' )+"\">"+
							            			"<td class=\"sorting_1\">"+(i+1)+"</td>"+
							                        "<td>"+result[i]["date"]+"</td>"+
							                        "<td>"+result[i]["type"]+"</td>"+
							                        "<td>"+result[i]["amount_original"]+"</td>"+
							                        "<td>"+result[i]["price"]+"</td>"+
							                        "<td>"+result[i]["amount"]+"</td>"+
							                        "<td>"+result[i]["unfillamount"]+"</td>"+
							                        "<td>"+result[i]["status"]+"</td>"+
							                    "</tr>";
	                    }
	                    $("#data").html(htmlMessage);
	                }else{
                    	alert(data["message"]);
	                }
          	 }
          });
	}
	$(function() {
		$("#submit").bind("click",function(){
			reloadOrder();
		});
	});
</script>
</body>