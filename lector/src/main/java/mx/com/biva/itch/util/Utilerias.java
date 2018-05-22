/**
 * Copyright 2018 Actinver
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package mx.com.biva.itch.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilerias {

	private static Integer minutos = Integer.valueOf(0);
	private static Float decimal = Float.valueOf(0.0F);
	private static Integer seconds = Integer.valueOf(0);
	private static Integer horas = Integer.valueOf(0);
	private static Integer tiempo = Integer.valueOf(0);
	private static String hhmmss;

	/**
	 * 
	 * @param buffer
	 * @param length
	 * @return
	 */
	public static String getAlphanumeric(ByteBuffer buffer, int length) {
		byte[] bytes = new byte[length];
		buffer.get(bytes);
		return new String(bytes, StandardCharsets.US_ASCII);
	}
	
	
	/**
	 * 
	 * @param buffer
	 * @param length
	 * @return
	 * @throws IOException
	 */
	public static long getNumeric(ByteBuffer buffer, int length) throws IOException {
		String alphanumeric = getAlphanumeric(buffer, length).trim();
		long value = 0L;
		try {
			value = Long.parseLong(alphanumeric);
			if (value < 0L) {
				System.out.println("Negative numeric: " + alphanumeric);
			}
		} catch (NumberFormatException e) {
			System.out.println("Malformed numeric: " + alphanumeric);
		}
		return value;
	}

	/**
	 * 
	 * @param buffer
	 * @param value
	 * @param length
	 */
	public static void putAlphanumericPadLeft(ByteBuffer buffer, String value, int length) {
		byte[] bytes = value.getBytes(StandardCharsets.US_ASCII);
		int i;
		for (i = 0; i < length - bytes.length; i++) {
			buffer.put((byte) 32);
		}
		for (i = 0; i < bytes.length - length; i++) {
		}
		while (i < bytes.length) {
			buffer.put(bytes[(i++)]);
		}
	}

	/**
	 * 
	 * @param buffer
	 * @param value
	 * @param length
	 */
	public static void putAlphanumericPadRight(ByteBuffer buffer, String value, int length) {
		byte[] bytes = value.getBytes(StandardCharsets.US_ASCII);
		int i;
		for (i = 0; i < Math.min(bytes.length, length); i++) {
			buffer.put(bytes[i]);
		}
		for (; i < length; i++) {
			buffer.put((byte) 32);
		}
	}

	/**
	 * 
	 * @param buffer
	 * @param value
	 * @param length
	 */
	public static void putNumeric(ByteBuffer buffer, long value, int length) {
		putAlphanumericPadLeft(buffer, Long.toString(value), length);
	}

	/**
	 * 
	 * @param segundos
	 * @return
	 */
	public static String getTime(int segundos) {
		minutos = Integer.valueOf(segundos / 60);
		decimal = Float.valueOf(segundos / 60.0F - minutos.floatValue());
		decimal = Float.valueOf(decimal.floatValue() * 60.0F);
		seconds = Integer.valueOf(decimal.intValue());
		horas =   Integer.valueOf(minutos.intValue() / 60);
		decimal = Float.valueOf(minutos.intValue() / 60.0F - horas.floatValue());
		decimal = Float.valueOf(decimal.floatValue() * 60.0F);
		minutos = Integer.valueOf(decimal.intValue());
		tiempo =  Integer.valueOf(horas.intValue() * 10000 + minutos.intValue() * 100 + seconds.intValue());
		if (tiempo.intValue() < 100000) {
			hhmmss = "0" + tiempo.toString();
		} else {
			hhmmss = tiempo.toString();
		}
		return hhmmss.substring(0, 2) + ":" + hhmmss.substring(2, 4) + ":" + hhmmss.substring(4);
	}

	/**
	 * 
	 * @param fecha
	 * @return
	 */
	public static String ToTimestamp(Date fecha) {
		DateFormat formatter = new SimpleDateFormat("yyyyMMdd-HH:mm:ss.SSS");
		return formatter.format(fecha);
	}

	/**
	 * 
	 * @param valor
	 * @param priceDecimals
	 * @return
	 */
	public static Float getFloat(Integer valor, Integer priceDecimals) {
		switch (priceDecimals.intValue()) {
		case 1:
			return Float.valueOf(valor.floatValue() / 10.0F);
		case 2:
			return Float.valueOf(valor.floatValue() / 100.0F);
		case 3:
			return Float.valueOf(valor.floatValue() / 1000.0F);
		case 4:
			return Float.valueOf(valor.floatValue() / 10000.0F);
		case 5:
			return Float.valueOf(valor.floatValue() / 100000.0F);
		case 6:
			return Float.valueOf(valor.floatValue() / 1000000.0F);
		}
		return Float.valueOf(valor.floatValue());
	}
}