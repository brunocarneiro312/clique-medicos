package com.hepta.cliquemedicos.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

	private static final long serialVersionUID = 1L;

	protected LocalDateTimeDeserializer() {
		super(LocalDateTime.class);
	}
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	
	@Override
	public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		LocalDateTime localDate;
		String data;
		data = parser.readValueAs(String.class);
		try {
			localDate =  LocalDateTime.parse(data, formatter);
		} catch (Exception e){
			formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
			localDate = LocalDateTime.parse(data, formatter);
		}
		return localDate;
	}
	
}