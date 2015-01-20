package hr.fer.tel.moovis.web.controllers;

import java.util.Date;

public class Logger {

	public static String getLogString(long time, String message) {
		return new Date(time).toString() + "\t" + message;
	}

}
