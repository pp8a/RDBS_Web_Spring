package servlets;
import java.io.IOException;
import java.util.Map;
import java.util.Stack;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/calc")
public class CalcServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String expression = req.getParameter("expression");
		/**
		 * Если URL запроса выглядит так: /calc?expression=a+b&a=10&b=20
		 * Тогда parameters будет содержать следующие записи:
		 * "expression" -> ["a+b"]
		 * "a" -> ["10"]
		 * "b" -> ["20"]
		 */
		Map<String, String[]> parameters = req.getParameterMap();	
		
		try {
            int result = evaluateExpression(expression, parameters);
            resp.getWriter().write(String.valueOf(result));
        } catch (Exception e) {
            resp.getWriter().write("Error evaluating expression");
        }		
		
	}

	private int evaluateExpression(String expression, Map<String, String[]> parameters) {
        try {
            Stack<Integer> numbers = new Stack<>();
            Stack<Character> operators = new Stack<>();
            expression = expression.replaceAll("\\s+", "");            

            for (int i = 0; i < expression.length(); i++) {
                char token = expression.charAt(i);

                if (isVariable(String.valueOf(token))) {
                	
                	String value = parameters.get(String.valueOf(token))[0];
                	while(isVariable(value)) {
                		value = parameters.get(value)[0];
                	}
                    numbers.push(Integer.parseInt(value));
                } else if (token == '(') {
                	operators.push(token);
                } else if (token == ')') {
                	while(operators.peek()!='(') {
                		numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                	}
                	operators.pop();//delete '('                
                } else if (isOperator(String.valueOf(token))) {
                	while (!operators.isEmpty() && hasPrecedence(token, operators.peek())) {
                        numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                    }
                    operators.push(token);
                } 
            }            

            while (!operators.isEmpty()) {
                numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
            }

            return numbers.pop();
            
        } catch (Exception e) {
        	System.out.println(e.getMessage());
            e.printStackTrace();
            return 0;
            
        }
    }

	private boolean hasPrecedence(char token, Character peek) {		
		if ((token == '*' || token == '/') && (peek == '*' || peek == '/')) {
			return true;
		}
		
	    return false;
	}
	
//	private boolean hasPrecedence(char token, Character peek) {
//		if ((token == '*' || token == '/') && (peek == '+' || peek == '-')) {
//	        return false;
//	    } else if ((token == '+' || token == '-') && peek == '(') {
//	        return false;
//	    } else if ((token == '*' || token == '/') && (peek == '+' || peek == '-' || peek == '(')) {
//	        return false;
//	    }
//	    return true;
//	}

	private static boolean isVariable(String str) {
        return str.matches("[a-z]");
    }

    private static boolean isOperator(String str) {
        return str.matches("[+\\-*/]");
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
