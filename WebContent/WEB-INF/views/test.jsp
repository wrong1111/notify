<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试接口方法</title>
<script type="text/javascript"
	src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
</head>
<body>

   <table>
    <tr><td></td><td>
	<form action="/interface/test/index" method="post">
	 <input type="text" value="${username}" name="username"/>
	 <input type="text" value="${token}" name="token"/>

		<select name="m">
			<c:forEach items="${showpara}" var='entry' >
				<option value="${entry.value}">name="${entry.key}"</option>
			</c:forEach>
		</select> <input type="submit" onclick="jump()">测试</input>
	</form>
	</td>
	</tr>
	<tr><td>传入参数==><td><h3 id="">${param}</h3></td>	</tr>
	<tr><td>返回结果<td><h4 id="">${result}</h4></td>	</tr>
	</table>
	<script type="text/javascript">
		function jump() {
			document.getElmentById("frm").submit();
		}
	</script>
</body>
</html>