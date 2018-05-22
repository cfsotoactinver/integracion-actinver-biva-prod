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
package mx.com.actinver.biva.procesadores;

import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.com.actinver.utilidades.TimeUtils;
import mx.com.biva.itch.DecodificadorDeMensajes;
import mx.com.biva.itch.LectorDeConfiguracionYaml;
import mx.com.biva.itch.modelo.Message;
import io.netty.buffer.ByteBuf;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Oscar Castillo ocastill@redhat.com Procesador para decodificar los
 *         grupos de mensajes que se reciben desde los feeds a modo de arreglos
 *         de bytes.
 *
 */
public class Decodificador implements Processor {
	// Archivo de configuración
	private final static String YAML_CONFIGURATION_FILE = "itchBiva.yaml";
	// Mensajes a consola
	private final static Logger LOGGER = LoggerFactory.getLogger(Decodificador.class);
	private static final String ERROR_DEC001 = "No se logro crear la instancia del decodificador debido a que no se encuentra el archivo de configuracion YAML: ";
	private static final String ERROR_DEC002 = "No se logro decodificar el arreglo de bytes.";
	// Servicios
	private DecodificadorDeMensajes decodificadorDeMensajes;
	private TimeUtils timeUtils;
	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Lee la configuración del archivo YAML e inicializa el sevicio de
	 * decodificación.
	 */
	public void inicializar() {
		try {
			LectorDeConfiguracionYaml configuracion = new LectorDeConfiguracionYaml(this.getClass().getClassLoader().getResourceAsStream(YAML_CONFIGURATION_FILE));
			this.decodificadorDeMensajes = new DecodificadorDeMensajes(configuracion);
			this.decodificadorDeMensajes.setTimeUtils(timeUtils);
		} catch (FileNotFoundException fileNotFoundException) {
			LOGGER.error(ERROR_DEC001 + YAML_CONFIGURATION_FILE, fileNotFoundException);
		}
	}

	/**
	 * Interpreta arreglos de bytes como mensajes de la especificación ITCH
	 * 
	 * @param exchange
	 *            El objeto con arreglo de bytes en el atributo body
	 */
	public void process(Exchange exchange) throws Exception {
		byte[] bytes;
		try {
			ByteBuf buffer = (ByteBuf) exchange.getIn().getBody();
			int length = buffer.readableBytes();
			if (buffer.hasArray()) {
				bytes = buffer.array();
			} else {
				bytes = new byte[length];
				buffer.getBytes(buffer.readerIndex(), bytes);
			}
		} catch(ClassCastException classCastException) {
			bytes = (byte[]) exchange.getIn().getBody();
		}
		try {
			List<Message> mensajes = this.decodificadorDeMensajes.messageIn(bytes);
			StringWriter stringWriter = null;
			List<String> mensajesJSON = new ArrayList<String>();
			for(Message mensaje : mensajes) {
				stringWriter = new StringWriter();
				objectMapper.writeValue(stringWriter, mensaje);
				mensajesJSON.add(stringWriter.toString());
				LOGGER.info(stringWriter.toString());
			}
			exchange.getIn().setBody(mensajesJSON);
		} catch (Exception e) {
			LOGGER.error(ERROR_DEC002, e);
		}
	}

	public TimeUtils getTimeUtils() {
		return timeUtils;
	}

	public void setTimeUtils(TimeUtils timeUtils) {
		this.timeUtils = timeUtils;
	}
}