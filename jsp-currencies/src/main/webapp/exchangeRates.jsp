<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, java.text.*, com.epam.rd.jsp.currencies.CurrenciesOfCurrentTestCase" %>

<jsp:useBean id="currencies" class="com.epam.rd.jsp.currencies.CurrenciesOfCurrentTestCase" scope="request"/>

<html>
	<head>
		<title>Exchange Rates for <%= request.getParameter("from") %></title>
		<style>
			body {
			    font-family: "Courier New", serif;
			    font-size: 16pt;
			}
		</style>
	</head>
	<body>
		<h1>Exchange Rates for ${param.from}</h1>
		<ul>
			<c:forEach var="entry" items="${currencies.getExchangeRates(param.from)}">
			<li>
				1 ${request.getParameter("from")} = ${entry.value} ${entry.key}
			</li>
			</c:forEach>
		</ul>
	</body>
</html>
