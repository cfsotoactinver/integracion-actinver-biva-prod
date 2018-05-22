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
package mx.com.biva.itch.modelo;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Message {

	final static Logger LOGGER = LoggerFactory.getLogger(Message.class);
	
	private final static String ERROR_MSG001 = "Error al instanciar el mensaje.";

	private Character type;
	private Integer timestamp;
	private Long secuencia;
	private String fechaHoraEmision;
	private String fechaHoraRecepcion;
    private String session;
	
	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public Message() {}
	
	public Message(ArrayList<String> elements) {
		try {
			this.setType(Character.valueOf(elements.get(0).charAt(0)));
			this.setTimestamp(Integer.valueOf(elements.get(1)));
		} catch (Exception exception) {
			LOGGER.error(ERROR_MSG001,exception);
		}
	}

	public Character getType() {
		return type;
	}

	public void setType(Character type) {
		this.type = type;
	}

	public Integer getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Integer timestamp) {
		this.timestamp = timestamp;
	}

	public Long getSecuencia() {
		return secuencia;
	}

	public void setSecuencia(Long secuencia) {
		this.secuencia = secuencia;
	}

	public String getFechaHoraEmision() {
		return fechaHoraEmision;
	}

	public void setFechaHoraEmision(String fechaHoraEmision) {
		this.fechaHoraEmision = fechaHoraEmision;
	}

	public String getFechaHoraRecepcion() {
		return fechaHoraRecepcion;
	}

	public void setFechaHoraRecepcion(String fechaHoraRecepcion) {
		this.fechaHoraRecepcion = fechaHoraRecepcion;
	}

}