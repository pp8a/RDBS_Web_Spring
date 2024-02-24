package servlets;

import java.io.IOException;
import java.security.Policy.Parameters;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/calc/*")
public class CalculatorServlets extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	private Map<String, Map<String, Integer>> variables = new HashMap<>();
    private Map<String, String> expressions = new HashMap<>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		
		if("/result".equals(pathInfo)) {			
			resultGet(req, resp);
		} else {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
			resp.getWriter().write("Invalid GET request");
		}
		
	}
	
	private void resultGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String sessionId = req.getSession().getId();		
		String expression = expressions.get(sessionId);		
		
		// Проверяем, существует ли внешний ключ (sessionId) в variables
	    variables.computeIfAbsent(sessionId, k -> new HashMap<>());

	    // Получаем внутренний Map для данного sessionId
	    Map<String, Integer> variableMap = variables.get(sessionId);
		
	    if (expression != null && !expression.trim().isEmpty() && !variableMap.isEmpty()) {
	    	try {
                int result = evaluateExpression(expression, variableMap);
                resp.setStatus(HttpServletResponse.SC_OK);//200
                resp.getWriter().write(Integer.toString(result));
            } catch (IllegalArgumentException e) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);//409
                resp.getWriter().write("Cannot calculate result due to lack of data");
            }
		} else {
			resp.setStatus(HttpServletResponse.SC_CONFLICT);//409
			resp.getWriter().write("Cannot calculate result due to lack of data");
		}
		
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();			
		String[] pathSegments = pathInfo.split("/");
		
		if(pathSegments.length == 2 && pathSegments[1].equals("expression")) {			
			expressionPut(req, resp);
		} else if (pathSegments.length == 2 && pathSegments[1].matches("[a-z]")) {
			variablePut(req, resp);
		} else {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
			resp.getWriter().write("Invalid PUT request");
		}
	}

	private void variablePut(HttpServletRequest req, HttpServletResponse resp){
		try {
			String variableName = req.getPathInfo().substring(1);						
			String variable = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
			
			String sessionId = req.getSession().getId();
			
			// Если переменная представляет собой имя другой переменной, использовать ее значение
		    variable = isVariable(variable) ? String.valueOf(variables.get(sessionId).get(variable)) : variable;
			
			if(variable != null && variable.matches("-?\\d+")) {
				int intValue = 0;
				
				try {
					intValue = Integer.parseInt(variable);
				} catch (NumberFormatException e) {
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
					resp.getWriter().write("Invalid variable value" + e.getMessage());
					return;
				}
				
				variables.computeIfAbsent(sessionId, k -> new HashMap<>());
				
				
				// Проверяем ограничения перед обновлением переменной
	            if (intValue < -10000 || intValue > 10000) {
	                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);//403
	                resp.getWriter().write("Variable value out of range");
	            } else {
	                if (variables.get(sessionId).containsKey(variableName)) {
	                    // Если переменная уже существует, обновляем ее
	                    variables.get(sessionId).put(variableName, intValue);
	                    resp.setStatus(HttpServletResponse.SC_OK);
	                } else {
	                    // Если переменная только создается, возвращаем 201
	                    variables.get(sessionId).put(variableName, intValue);
	                    resp.setStatus(HttpServletResponse.SC_CREATED);
	                    resp.setHeader("Location", req.getRequestURI());
	                }
	            }
			}	
			
		} catch (IOException e) {
			System.out.println("Internal Server Error" + e.getMessage());
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//500
			try {
				resp.getWriter().write("Internal server error");
			} catch (IOException e1) {
				System.out.println("Error writer " + e.getMessage());
			}
		}
		
	}

	private void expressionPut(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String expression = req.getReader().lines().reduce("", (acc, actual) -> acc + actual);
			
			if(expression != null && !expression.trim().isEmpty()) {
				String sessionId = req.getSession().getId();	
				
				if(isValidExpression(expression)) {
					if (expressions.containsKey(sessionId)) {
		                // Если ресурс уже существует, обновляем его
		                expressions.put(sessionId, expression);
		                resp.setStatus(HttpServletResponse.SC_OK);//200
		            } else {
		                // Если ресурс только создается, возвращаем 201
		                expressions.put(sessionId, expression);
		                resp.setStatus(HttpServletResponse.SC_CREATED);//201
		                resp.setHeader("Location", req.getRequestURI());
		            }		
					
				} else {
	                // Если выражение имеет неверный формат, возвращаем 400
	                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	                resp.getWriter().write("Bad expression format");
	            }
			}
			
		} catch (IOException e) {
			System.out.println("Internal server error " + e.getMessage());
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//500
			try {
				resp.getWriter().write("Internal server error");
			} catch (IOException e1) {
				System.out.println("Error writer " + e.getMessage());
			}
		}
		
	}
	
	private boolean isValidExpression(String expression) {			    
	    // Проверяем, содержит ли выражение две или более буквы подряд
	    return !expression.matches(".*[A-z]{2,}.*");
	    // Если нашли две или более буквы подряд, возвращаем false, иначе true
	}


	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		String pathInfo = req.getPathInfo();
		String[] pathSegments = pathInfo.split("/");
		if(pathSegments.length == 2 && pathSegments[1].matches("[a-z]")) {
			variableDelete(req, resp);
		} else {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
			resp.getWriter().write("Invalid DELETE request");
		}
	}
	
	private void variableDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {		       
		String sessionId = req.getSession().getId();
        String pathInfo = req.getPathInfo();
        String[] pathSegments = pathInfo.split("/");

        if (variables.containsKey(sessionId)) {
            variables.get(sessionId).remove(pathSegments[1]);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("Variable not found");
        }
		
	}

	private int evaluateExpression(String expression, Map<String, Integer> variableMap) {
	    try {
	        Stack<Integer> numbers = new Stack<>();
	        Stack<Character> operators = new Stack<>();
	        expression = expression.replaceAll("\\s+", "");

	        for (int i = 0; i < expression.length(); i++) {
	            char token = expression.charAt(i);

	            if (isVariable(String.valueOf(token))) {
	                Integer value = variableMap.get(String.valueOf(token));
	                while (value != null && isVariable(String.valueOf(value))) {
	                    value = variableMap.get(String.valueOf(value));
	                }
	                if (value != null) {
	                    numbers.push(value);
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
    
    
    
//	private Set<String> extractUniqueVariables(String expression) {
//    Set<String> uniqueVariables = new LinkedHashSet<>();
//    
//    // Проходимся по выражению и извлекаем уникальные переменные
//    for (char token : expression.toCharArray()) {
//        if (Character.isLetter(token)) {
//            uniqueVariables.add(String.valueOf(token));
//        }
//    }
//    
//    return uniqueVariables;
//}

	

}
