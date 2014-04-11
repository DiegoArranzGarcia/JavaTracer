package com.general.settings.model;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SettingsErrorHandler implements ErrorHandler{
	
	private Settings settings;
	private boolean error;
	
	public SettingsErrorHandler(Settings settings){
		this.settings = settings;
	}
	
	public void error(SAXParseException e) throws SAXException {
		error = true;
	}

	public void fatalError(SAXParseException e) throws SAXException {
		error = true;
	}

	public void warning(SAXParseException e) throws SAXException {
		error = true;
	}

	public boolean error() {
		return error;
	}

}
