<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, java.text.*, com.epam.rd.jsp.currencies.CurrenciesOfCurrentTestCase" %>

<jsp:useBean id="currencies" class="com.epam.rd.jsp.currencies.CurrenciesOfCurrentTestCase" scope="request"/>

<html>
	<head>
		<title>Currencies</title>
		<style>
			body {
			    font-family: "Courier New", serif;
			    font-size: 16pt;
			}
		</style>
	</head>	
	<body>
		<h1>Currencies</h1>
		<ul>
			<c:forEach var="currency" items="<%= currencies.getCurrencies() %>">
				<li>${currency}</li>
			</c:forEach>
		</ul>
		
		<h1>Currencies</h1>
		<ul>
		    <c:forEach var="currency" items="${currencies}">
		        <li>${currency}</li>
		    </c:forEach>
		</ul>
	</body>
</html>
