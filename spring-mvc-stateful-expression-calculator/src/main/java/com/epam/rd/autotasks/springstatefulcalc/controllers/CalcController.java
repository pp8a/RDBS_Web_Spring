package com.epam.rd.autotasks.springstatefulcalc.controllers;


import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import com.epam.rd.autotasks.springstatefulcalc.services.CalculationService;
import com.epam.rd.autotasks.springstatefulcalc.services.VariableService;

@Controller
@RequestMapping(path = "/calc")
public class CalcController {
	
	@Autowired
	private CalculationService calculationService;
	@Autowired
	private VariableService variableService;
		
	@Autowired
	private HttpSession session;
	
	/**
	 * Установка нового или обновление выражения.
	 * @param expression
	 * @return HttpStatus
	 */
	@PutMapping(path = "/expression")
	public ResponseEntity<?> putExpression(@RequestBody String expression) {
		if (expression == null || expression.trim().isEmpty()) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Expression is empty");
	    }
	    
		if(calculationService.isValidExpression(expression)) {
			ResponseEntity<?> entity = session.getAttribute(CalculationService.SESSION_EXPRESSION_KEY) == null ? 
				    ResponseEntity.status(HttpStatus.CREATED).header("Location", expression).build() : // Первое добавление
				    ResponseEntity.status(HttpStatus.OK).header("Location", expression).build(); // Обновление существующего
				session.setAttribute(CalculationService.SESSION_EXPRESSION_KEY, expression);
				calculationService.setExpression(session, expression);
				return entity;

	        
	     } else {
	    	 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad expression format");//400
	     }
	}
	
	
	/**
	 * Установка нового или обновление значения переменной.
	 * @param variableName
	 * @param value
	 * @param uriBuilder
	 * @return
	 */
	@PutMapping(path = "/{variableName}")
	public ResponseEntity<?> putVariable(@PathVariable String variableName, @RequestBody String value) {
		
		
		if(variableService.isVariable(value)) {			
			Map<String, String> variables = variableService.getVariablesFromSession(session);
	        value = variables.get(value);			
		}
		
		if(value == null || !value.matches("-?\\d+")) {
	        return ResponseEntity.badRequest().body("Value is not an integer");
	    }
		
		// Проверяем ограничения перед обновлением переменной
        int intValue = Integer.parseInt(value);
        if (intValue < -10000 || intValue > 10000) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Variable value out of range");
        }
		
		try {	
			int statusCode =
                    (variableService.getVariablesFromSession(session) == null ||
                    !variableService.getVariablesFromSession(session).containsKey(variableName))
                    ? 201 : 200;
			variableService.setVariable(session, variableName, value);			
			return ResponseEntity.status(statusCode).header("Location", variableName, value).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
	}

	/**
	 * Удаление переменной.
	 * @param name
	 * @return
	 */
	@DeleteMapping(path = "/{name}")
	public ResponseEntity<Void> deleteVariable(@PathVariable String name) {	   
	    return variableService.deleteVariable(session, name) ? 
	    		ResponseEntity.noContent().build() : 
	    			ResponseEntity.notFound().build();
	}


	/**
	 * Получение результата вычисления выражения.
	 * @return
	 */
	@GetMapping(path = "/result")	
	public ResponseEntity<?> getResult() {
		String expression = calculationService.getExpression(session);
		Map<String, String> variables = variableService.getVariablesFromSession(session);
		try {
			Integer result =  calculationService.evaluateExpression(expression, variables);
			return ResponseEntity.ok(result);
		} catch (IllegalArgumentException e) {
			  return ResponseEntity.status(409).body("Can't find variable value");
		}
		
				
	}	

}
