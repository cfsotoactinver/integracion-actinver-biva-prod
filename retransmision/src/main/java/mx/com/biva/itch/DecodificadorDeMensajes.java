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
package mx.com.biva.itch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.com.actinver.utilidades.TimeUtils;
import mx.com.biva.itch.modelo.*;

public class DecodificadorDeMensajes {

	final static Logger LOGGER = LoggerFactory.getLogger(DecodificadorDeMensajes.class);

	private final LectorDeConfiguracionYaml configuracion;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HH:mm:ss.SSS");
	private TimeUtils timeUtils;

	public DecodificadorDeMensajes(LectorDeConfiguracionYaml configuracion) {
		this.configuracion = configuracion;
	}

	/**
	 * 
	 * @param payload
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getString(byte[] payload, int length) throws UnsupportedEncodingException {
		byte[] bytes = new byte[length];
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.get(bytes);
		return new String(bytes, StandardCharsets.US_ASCII);
	}

	/**
	 * 
	 * @param payload
	 * @return
	 */
	public Object getLen(byte[] payload) {
		ByteBuffer length = ByteBuffer.wrap(payload);
		return (int) length.getShort(0);
	}

	/**
	 * 
	 * @param payload
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public long getInt(byte[] payload, int length) throws UnsupportedEncodingException {
		long value = 0L;
		try {
			for (int i = 0; i < payload.length; i++) {
				value = (value << 8) + (payload[i] & 0xFF);
			}

		} catch (Exception eee) {
			LOGGER.error("Malformed numeric: " + payload);
		}

		return value;
	}

	/**
	 * 
	 * @param payload
	 * @return
	 * @throws NumberFormatException
	 * @throws UnsupportedEncodingException
	 */
	public String getDouble(byte[] payload, int length) throws NumberFormatException, UnsupportedEncodingException {
		return ("" + (Double.parseDouble("" + getInt(payload, length)) / 10000));
	}

	/**
	 * 
	 * @param arr
	 * @param fieldArray
	 * @return
	 * @throws IOException
	 */
	public String parse(byte[] arr, int length, ArrayList<Object> fieldArray) throws IOException {
		String value = "";
		switch ((Integer) fieldArray.get(2)) {
		case 1:
			value = getString(arr, length);
			break;
		case 2:
			value = "" + Long.parseLong("" + getInt(arr, length));
			break;
		}
		return value.trim();
	}

	/**
	 * 
	 * @param payload
	 * @return
	 * @throws IOException
	 */
	public List<Message> messageIn(byte[] payload) throws IOException {
		List<Message> mensajes = new ArrayList<Message>();
		ArrayList<String> messageArray = null;
		String sesion = getString(Arrays.copyOfRange(payload, 0, 10), 10);
		long secuencia = getLong(Arrays.copyOfRange(payload, 10, 18));
		long numeroDeMensajes = getInt(Arrays.copyOfRange(payload, 18, 20), 2);
		long longitudDelPrimerMensaje = getInt(Arrays.copyOfRange(payload, 20, 22), 2);
		Message message = null;
		LOGGER.debug("SESION::" + sesion + " SECUENCIA::" + secuencia + " NUMERO DE MENSAJES::" + numeroDeMensajes + " LONGITUD::" + longitudDelPrimerMensaje + " LONGITUD TOTAL DEL PAYLOAD " + payload.length);
		int messagePointer = 0;
		int offset = 22;
		for (long contador = 0; contador < numeroDeMensajes; contador=contador+1) {
			messageArray = new ArrayList<String>();
			payload = Arrays.copyOfRange(payload, offset, payload.length);
			LOGGER.debug("PAYLOAD"+payload);
			String tipoDeMensaje = this.getString(Arrays.copyOfRange(payload, 0, 1), 1);
			LOGGER.debug("TIPO"+tipoDeMensaje);
			messageArray.add(tipoDeMensaje);
			messagePointer = 1;
			ArrayList<Object> fieldsArray = null;
			try {
				fieldsArray = this.configuracion.getFields(tipoDeMensaje);
				LOGGER.debug("Longitud de la lista de campos " + fieldsArray.size());
			} catch (Exception exception) {
				secuencia=secuencia+1;
				LOGGER.error("Error al obtener la configuración del mensaje con tipo " + tipoDeMensaje,exception);
			}
			LOGGER.debug("Decodificacion" + fieldsArray);
			if (fieldsArray != null) {
				int messagePointerFinal = 0;
				for (int i = 1; i < fieldsArray.size(); i++) {
					ArrayList<Object> fieldArray = this.configuracion.getFormat((String) fieldsArray.get(i));
					messagePointerFinal = (messagePointer + ((Integer) fieldArray.get(1)));
					//LOGGER.debug("INITIAL::" + messagePointer + "::FINAL::" + messagePointerFinal + ":: RANGE::" + (messagePointerFinal - messagePointer));
					messageArray.add(parse(Arrays.copyOfRange(payload, messagePointer, messagePointerFinal),messagePointerFinal - messagePointer, fieldArray));
					messagePointer = messagePointer + ((Integer) fieldArray.get(1));
				}
				offset = messagePointerFinal + 2;
				message = buildBean(messageArray);
				message.setSecuencia(secuencia);
				LOGGER.debug("Secuencia a asignada " + secuencia);
				secuencia = secuencia + 1;
				if (message.getType().charValue() == 'T') {
					this.timeUtils.setLastTimeStamp(((T) message).getTimestamp().longValue());
				}
				message.setFechaHoraEmision(this.timeUtils.getTimeFromMidnightPlusSecondsAndNanoSeconds(this.timeUtils.getLastTimeStamp(), message.getTimestamp().longValue()));
				message.setFechaHoraRecepcion(this.simpleDateFormat.format(new Date()));
				message.setSession(sesion);
				mensajes.add(message);
			} else {
				LOGGER.error("No se encontraron campos definidos en el archivo YAML para el tipo de mensaje: " + tipoDeMensaje);
			}
		}
		return mensajes;
	}

	/**
	 * 
	 * @param messageArray
	 * @return
	 */
	public Message buildBean(ArrayList<String> messageArray) {
		Message message = null;
		switch (messageArray.get(0)) {
		case "A":
			message = new A(messageArray);
			break;
		case "B":
			message = new B(messageArray);
			break;
		case "C":
			message = new C(messageArray);
			break;
		case "D":
			message = new D(messageArray);
			break;
		case "E":
			message = new E(messageArray);
			break;
		case "F":
			message = new F(messageArray);
			break;
		case "G":
			message = new G(messageArray);
			break;
		case "H":
			message = new H(messageArray);
			break;
		case "I":
			message = new I(messageArray);
			break;
		case "L":
			message = new L(messageArray);
			break;
		case "a":
			message = new LowerCaseA(messageArray);
			break;
		case "e":
			message = new LowerCaseE(messageArray);
			break;
		case "M":
			message = new M(messageArray);
			break;
		case "N":
			message = new N(messageArray);
			break;
		case "P":
			message = new P(messageArray);
			break;
		case "Q":
			message = new Q(messageArray);
			break;
		case "R":
			message = new R(messageArray);
			break;
		case "S":
			message = new S(messageArray);
			break;
		case "T":
			message = new T(messageArray);
			break;
		case "U":
			message = new U(messageArray);
			break;
		case "X":
			message = new X(messageArray);
			break;
		default:
			LOGGER.error("Not Found MessageType Builder:" + messageArray);
		}
		return message;
	}

	public TimeUtils getTimeUtils() {
		return timeUtils;
	}

	public void setTimeUtils(TimeUtils timeUtils) {
		this.timeUtils = timeUtils;
	}
	private Long getLong(byte[] bytes) throws UnsupportedEncodingException {
		long value = 0L;
		try {
			for (int i = 0; i < bytes.length; i++) {
				value = (value << 8) + (bytes[i] & 0xFF);
			}
		} catch (Exception exception) {
			LOGGER.error("No se puede obtener un long de: " + bytes, exception);
		}
		return value;
	}
}