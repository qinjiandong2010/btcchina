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
				<li><a href="javascript;,">市场数据</a> <span class="divider">&nbsp;</span></li>
				<li><a href="javascript;,">深度行情</a> <span class="divider-last">&nbsp;</span></li>
			</ul>
		</div>
	</div>
	<!-- BEGIN ADVANCED TABLE widget-->
	<div class="row-fluid">
		<div class="span12">
			<div class="widget">
				<div class="widget-header">
					<h5>深度行情</h5>
					<form id="depthForm" commandName="formParam" method="post" class="form-inline">
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
                </div>
				<div class="widget-body table-container">
					<div class="dataTables_wrapper">
						<table id="ordersTable" class="default-table stripped turquoise dataTable">
							<thead>
								<tr align="left">
								   <th>#</th>
			                       <th>类型</th>
			                       <th>价格(¥)</th>
			                       <th>比特币数量(฿)</th>
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
	$(function() {
		$("#submit").bind("click",function(){
		  	$.ajax({
                cache: true,
                type: "POST",
                url:"/api/data/depth",
                data:$('#depthForm').serialize(),// 你的formid
                async: false,
                error: function(request) {
                    alert("连接异常");
                },
                success: function(data) {
                	if( data["recode"] == 0 ){
	                    var asks = data["result"]["asks"];
	                    var bids = data["result"]["bids"];
	                    var htmlMessage = "";
	                    for( var i = 0; i < asks.length; i ++){
		                	htmlMessage += "<tr class=\"gradeX "+( i%2 == 0 ? 'odd':'even' )+"\">"+
							            			"<td class=\"sorting_1\">"+(i+1)+"</td>"+
							                        "<td>卖出</td>"+
							                        "<td>"+asks[i][0]+"</td>"+
							                        "<td>"+asks[i][1]+"</td>"+
							                    "</tr>";
	                    }
	                    for( var i = 0; i < bids.length; i ++){
		                	htmlMessage += "<tr class=\"gradeX "+( i%2 == 0 ? 'odd':'even' )+"\">"+
							            			"<td class=\"sorting_1\">"+(i+1)+"</td>"+
							                        "<td>买入</td>"+
							                        "<td>"+bids[i][0]+"</td>"+
							                        "<td>"+bids[i][1]+"</td>"+
							                    "</tr>";
	                    }
	                    $("#data").html(htmlMessage);
	                }else{
                    	alert(data["message"]);
	                }
                }
            });
		});
	});
</script>
</body>