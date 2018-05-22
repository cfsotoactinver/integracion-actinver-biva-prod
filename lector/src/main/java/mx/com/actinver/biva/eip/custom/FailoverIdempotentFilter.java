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
package mx.com.actinver.biva.eip.custom;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FailoverIdempotentFilter {
	
	@Produce(uri = "seda:feedUnico")
	private ProducerTemplate feedUnico;
	private Long numeroDeSecuencia = null;
	private ConcurrentHashMap<Long, byte[]> listaProcesamiento = new ConcurrentHashMap<Long, byte[]>();
	private static final Logger LOGGER = LoggerFactory.getLogger(FailoverIdempotentFilter.class);
	private Boolean iniciado = false;
	
	/**
	 * Decide si debe agregar un mensaje al mapa para ser enviado al feed unico
	 * @param bytes
	 */
	public void agregaFeedA(byte[] bytes) {
		try {
			Long numeroDeMensajes = getLong(Arrays.copyOfRange(bytes, 18, 20));
			if(numeroDeMensajes > 0) {
				Long secuencia = this.getLong(Arrays.copyOfRange(bytes, 10, 18));
				if(!listaProcesamiento.containsKey(secuencia)) {
					if(numeroDeSecuencia == null || numeroDeSecuencia < secuencia) {
						numeroDeSecuencia = secuencia;
						listaProcesamiento.put(numeroDeSecuencia, bytes);
						if (!this.iniciado) {
							this.produceFeedUnico();
						}
					}
				}
			}
		} catch (UnsupportedEncodingException unsupportedEncodingException) {
			LOGGER.error("Eror al decodificar el mensaje", unsupportedEncodingException);
		}
	}
	
	/**
	 * Decide si debe agregar un mensaje al mapa para ser enviado al feed unico
	 * @param bytes
	 */
	public void agregaFeedB(byte[] bytes) {
		try {
			Long numeroDeMensajes = getLong(Arrays.copyOfRange(bytes, 18, 20));
			if(numeroDeMensajes > 0) {
				Long secuencia = this.getLong(Arrays.copyOfRange(bytes, 10, 18));
				if(!listaProcesamiento.containsKey(secuencia)) {
					if(numeroDeSecuencia == null || numeroDeSecuencia < secuencia) {
						numeroDeSecuencia = secuencia;
						listaProcesamiento.put(numeroDeSecuencia, bytes);
						if (!this.iniciado) {
							this.produceFeedUnico();
						}
					}
				}
			}
		} catch (UnsupportedEncodingException unsupportedEncodingException) {
			LOGGER.error("Eror al decodificar el mensaje", unsupportedEncodingException);
		}
	}
	
	/**
	 * Envia todos los mensajes del mapa al feed unico
	 *  
	 */
	private void produceFeedUnico()   {
	    	this.iniciado = true;
		for (Long secuencia : this.listaProcesamiento.keySet()) {
			try {
				feedUnico.start();
				feedUnico.sendBody(this.listaProcesamiento.get(secuencia));
				feedUnico.stop();
			} catch (Exception e) {
				LOGGER.error("Eror al enviar el mensaje", e);
			}
	
			listaProcesamiento.remove(secuencia);
		}
		this.iniciado = false;
	}
	
	/**
	 * Lee un numero de tipo long de un arreglo de bytes.
	 * @param bytes El arreglo de bytes.
	 * @return El valor de tipo long.
	 * @throws UnsupportedEncodingException
	 */
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