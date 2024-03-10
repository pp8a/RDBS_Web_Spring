package com.epam.rd.autotasks.springemployeecatalog.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.epam.rd.autotasks.springemployeecatalog.domain.Position;

@Component
public class StringToEnumConvertor implements Converter<String, Position> {

	@Override
	public Position convert(String source) {
		switch (source.toUpperCase()) {
		case "PRESIDENT":
			return Position.PRESIDENT;
		case "MANAGER":
			return Position.MANAGER;
		case "ANALYST":
			return Position.ANALYST;
		case "CLERK":
			return Position.CLERK;
		case "SALESMAN":
			return Position.SALESMAN;		
		}
		return null;
	}

}
