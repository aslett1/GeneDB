package org.genedb.query.params;

import org.springframework.validation.Errors;

/**
 * Param which contains a String
 * 
 * @author art
 */
public class StringParam extends AbstractParam {

	private String value;
	private boolean set = false;
	private String validation;
	
	public void setValidation(String validation) {
	    this.validation = validation;
	}
	
	public void setValue(String value) {
		this.value = value;
		this.set = true;
	}



	@Override
	public String getValue() {
		return value;
	}

	public boolean supports(Class clazz) {
	    return Boolean.class.isAssignableFrom(clazz);
	}

	public void validate(Object value, Errors errors) {
	    
	}

	public boolean isSet() {
	    return this.set;
	}
}
