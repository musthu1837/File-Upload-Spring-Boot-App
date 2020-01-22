package com.musthafa.springboot.exceptions;

import java.util.Date;

public class ExceptionResponse {
	public Date date;
	public String message;
	public String description;

	public ExceptionResponse(Date date, String message, String description) {
		// TODO Auto-generated constructor stub
		this.date = date;
		this.message = message;
		this.description = description;
	}

	@Override
	public String toString() {
		return "ExceptionResponse [date=" + date + ", message=" + message + ", description=" + description + "]";
	}
}
