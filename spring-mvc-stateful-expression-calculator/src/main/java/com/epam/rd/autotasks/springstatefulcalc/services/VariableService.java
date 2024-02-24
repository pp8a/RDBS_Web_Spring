package com.epam.rd.autotasks.springstatefulcalc.services;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

@Service
public class VariableService {

	private static final String SESSION_VARIABLES_KEY = "variables";

    public Map<String, String> getVariablesFromSession(HttpSession session) {
        return (Map<String, String>) session.getAttribute(SESSION_VARIABLES_KEY);
    }

    public void setVariable(HttpSession session, String name, String value) {
        Map<String, String> variables = getVariablesFromSession(session);
        if (variables == null) {
            variables = new HashMap<>();
            session.setAttribute(SESSION_VARIABLES_KEY, variables);
        }
        variables.put(name, value);
    }

    public boolean deleteVariable(HttpSession session, String variableName) {
        Map<String, String> variables = getVariablesFromSession(session);
        if (variables != null && variables.containsKey(variableName)) {
            variables.remove(variableName);
            return true; // Успешно удалено
        } else {
            return false; // Переменная не найдена
        }
    }
    
    public boolean isVariable(String str) {
        return str.matches("[a-z]");
    }
	
}
