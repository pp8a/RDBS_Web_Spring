package com.epam.rd.jsp.currencies;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class Currencies {
    private Map<String, BigDecimal> curs = new TreeMap<>();
    
    /**
     * adds a currency entry: name and universal weight. This method is for initializing.
     * @param currency - name
     * @param weight - universal weight
     */
    public void addCurrency(String currency, BigDecimal weight) {
       curs.put(currency, weight);//initialization
    }

    /**
     * get currency from TreeMap
     * @return all the currencies in alphabetical order.
     */
    public Collection<String> getCurrencies() {    	    	
    	return curs.keySet();        
    }
    
    /**
     * 
     * @param referenceCurrency - currency exchange rate
     * @return all the currencies in alphabetical order paired to their exchange rate to a currency given as param.
     */
    public Map<String, BigDecimal> getExchangeRates(String referenceCurrency) {
    	if(!curs.containsKey(referenceCurrency)) {
    		throw new IllegalArgumentException("Reference currency not found");
    	}
    	
    	BigDecimal refRange = curs.get(referenceCurrency);
    	
    	Map<String, BigDecimal> mapExchangeRates = new TreeMap<>();    	
    	for (Map.Entry<String, BigDecimal> entry : curs.entrySet()) {
			String currency = entry.getKey();
			BigDecimal exchangeRates = entry.getValue();
			mapExchangeRates.put(currency, refRange.divide(exchangeRates, 5, RoundingMode.HALF_UP));
			
		}
    	
    	return mapExchangeRates;
    }

    /**
     * Takes source and target currencies and amount of money in source currency.
     * @param amount
     * @param sourceCurrency
     * @param targetCurrency
     * @returns equivalent amount in target currency
     */
    public BigDecimal convert(BigDecimal amount, String sourceCurrency, String targetCurrency) {
    	if(!curs.containsKey(sourceCurrency) || !curs.containsKey(targetCurrency)) {
    		throw new IllegalArgumentException("Source or target currency not found");
    	}
    	
    	BigDecimal sourceRates = curs.get(sourceCurrency);
    	BigDecimal targetRates = curs.get(targetCurrency);
    	
    	return amount.multiply(sourceRates).divide(targetRates, 5, RoundingMode.HALF_UP);
    }
}
