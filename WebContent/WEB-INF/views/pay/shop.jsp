<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
<meta name="apple-touch-fullscreen" content="yes">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">
<meta name="description" content="支付跳转">
</head>
<body>
	<table style="width: 90%" align="center">
		<tr>
			<td>
				<div id="jump">${orderno};</div>
			</td>
		</tr>
	</table>

<script type="text/javascript">
	var status = "${status}";
	var orderno = "${orderno}";
	var desc = "${desc}";
	var amount = "${money}";
	var img ="${img}";
	var imgurl = "${imgurl}";
	var ary = [];
	if(status=='0'){
		ary.push("订单["+orderno+"],金额["+amount+"]");
		document.getElementById("jump").innerHTML= ary.join("");
		document.location.href=imgurl;
	}else{
		ary.push("订单["+orderno+"],");
		ary.push(desc);
	}
	document.getElementById("jump").innerHTML= ary.join("");
</script>	
</body>
</html>