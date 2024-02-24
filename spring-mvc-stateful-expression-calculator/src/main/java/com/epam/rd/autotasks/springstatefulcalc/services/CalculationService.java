package com.epam.rd.autotasks.springstatefulcalc.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

@Service
public class CalculationService {	
	public static final String SESSION_EXPRESSION_KEY = "expression";
	
	public void setExpression(HttpSession session, String expression) {
        session.setAttribute(SESSION_EXPRESSION_KEY, expression);
    }

    public String getExpression(HttpSession session) {
        return (String) session.getAttribute(SESSION_EXPRESSION_KEY);
    }
	
	// Метод для проверки корректности выражения
	public boolean isValidExpression(String expression) {
		// Проверяем, содержит ли выражение две или более буквы подряд
		return !expression.matches(".*[A-z]{2,}.*");
		// Если нашли две или более буквы подряд, возвращаем false, иначе true
	}	
	
	public int evaluateExpression(String expression, Map<String, String> variableMap) {
	    try {
	        Stack<Integer> numbers = new Stack<>();
	        Stack<Character> operators = new Stack<>();
	        expression = expression.replaceAll("\\s+", "");

	        for (int i = 0; i < expression.length(); i++) {
	            char token = expression.charAt(i);

	            if (isVariable(String.valueOf(token))) {
	                String value = variableMap.get(String.valueOf(token));
	                while (value != null && isVariable(String.valueOf(value))) {
	                    value = variableMap.get(String.valueOf(value));
	                }
	                if (value != null) {
	                    numbers.push(Integer.valueOf(value));
	                } else {
	                    throw new IllegalArgumentException("Variable not found: " + token);	 
	                }
	            } else if (token == '(') {
	                operators.push(token);
	            } else if (token == ')') {
	                while (operators.peek() != '(') {
	                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
	                }
	                operators.pop(); // delete '('
	            } else if (isOperator(String.valueOf(token))) {
	                while (!operators.isEmpty() && hasPrecedence(token, operators.peek())) {
	                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
	                }
	                operators.push(token);
	                
	            } else if (isNumeric(String.valueOf(token))) {
                    numbers.push(Integer.parseInt(String.valueOf(token)));
                }    

	        }

	        while (!operators.isEmpty()) {
	            numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
	        }

	        return numbers.pop();

	    } catch (Exception e) {
	        System.out.println("Invaled get result " + e.getMessage());
	        throw new IllegalArgumentException("Can't get result");

	    }
	}
	
	private boolean hasPrecedence(char token, Character peek) {
        if ((token == '*' || token == '/') && (peek == '+' || peek == '-')) {
            return false;
        } else if ((token == '+' || token == '-') && peek == '(') {
            return false;
        } else if ((token == '*' || token == '/') && (peek == '+' || peek == '-' || peek == '(')) {
            return false;
        }
        
        return true;
    }

	private static boolean isVariable(String str) {
        return str.matches("[a-z]");
    }

    private static boolean isOperator(String str) {
        return str.matches("[+\\-*/]");
    }  
    
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+");
    }
	
    private static int applyOperator(char operator, int b, int a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b != 0) {
                    return a / b;
                } else {
                    throw new ArithmeticException("Division by zero");
                }
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
}
