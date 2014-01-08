<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head><style type="text/css">.navbar .span2{width:14%;}</style></head>
<body>
	<ul class="breadcrumb">
		<li><a href="/"><i class="icon-home"></i></a><span
			class="divider">&nbsp;</span></li>
		<li><a href="#">交易面板</a> <span class="divider">&nbsp;</span></li>
		<li><a href="#">买入比特币</a><span class="divider-last">&nbsp;</span></li>
	</ul>

	<div class="widget">
		<form id="buyOrderForm" method="POST" class="form-horizontal form-wizard">
			<div class="widget-header">
				<h5>买入比特币</h5>
			</div>
			<div class="widget-content no-padding">
                <div id="messages">
                </div>
                </div>
                <div class="form-row">
					<label class="field-name" for="market">交易市场：</label>
					<div class="field">
					    <select name="market" >
					    	<option value="btcchina">BTCChina</option>
					    	<option value="okcoin">OKCoin</option>
					    	<option value="chbtc">CHBTC</option>
                        </select>
					</div>
				</div>
				<div class="form-row">
					<label class="field-name" for="username">用户名：</label>
					<div class="field">
						<div class="input-prepend input-append">
							<input name="username" class="span12"  maxlength="20" /> 
							<span class="add-on">在这里输入所在交易市场登录用户名</span>
						</div>
					</div>
				</div>
				<div class="form-row">
					<label class="field-name" for="password">密码：</label>
					<div class="field">
						<div class="input-prepend input-append">
							<input name="password" type="password" class="span12"  maxlength="20" /> 
							<span class="add-on">在这里输入所在交易市场登录密码</span>
						</div>
					</div>
				</div>
				<div class="form-row">
					<label class="field-name" for="amount">购买数额 (฿)：</label>
					<div class="field">
						<div class="input-prepend input-append">
							<input name="amount" class="span12"  maxlength="20" /> 
							<span class="add-on">在这里输入您想购买的比特币的数额。</span>
						</div>
					</div>
				</div>
				<div class="form-row">
					<label class="field-name" for="price">出价 (¥ /BTC)：</label>
					<div class="field">
						<div class="input-prepend input-append">
							<input name="price" class="span12"  maxlength="20" /> 
							<span class="add-on">在这里输入您的人民币出价，此出价为฿1.00个比特币的买入价格。</span>
						</div>
					</div>
				</div>
				<div class="form-row">
					<label class="field-name" for="tradepwd">交易密码：</label>
					<div class="field">
						<div class="input-prepend input-append">
							<input name="tradepwd" type="password" class="span12"  maxlength="20" /> 
							<span class="add-on">在这里输入所在交易市场交易密码</span>
						</div>
					</div>
				</div>
				<div class="form-row" style="padding-left: 180px;">
					<button type="button" id="submit" class="button button-blue">下买单</button>
				</div>
			</div>
		</form>
	</div>
<script type="text/javascript">
	$(function() {
		$("#submit").bind("click",function(){
		  	$.ajax({
                cache: true,
                type: "POST",
                url:"/api/trade/buy",
                data:$('#buyOrderForm').serialize(),// 你的formid
                async: false,
                error: function(request) {
                    alert("连接异常");
                },
                success: function(data) {
                	var htmlMessage = "<div class=\"note  "+(data["recode"]==0 ? "note-success" : "note-danger")+" \" style=\"margin: 20px 30px;\">"
					                    +"<strong>提示</strong> "+data["message"];
                    $("#messages").html(htmlMessage);
                }
            });
		});
	});
</script>
</body>