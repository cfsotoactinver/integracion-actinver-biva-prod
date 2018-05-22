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
package mx.com.actinver.utilidades;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.SimpleDateFormat;

 
/**
 * Provee métodos para convertir valores entre distintos formatos de acuerdo a la especificación ITCH.
 * @author Oscar Eduardo Castillo Nestor ocastill@redhat.com
 *
 */
public class Formateador implements Externalizable{
	
	private SimpleDateFormat simpleDateFormatDateTime = new SimpleDateFormat("yyyyMMdd-HH:mm:ss.S");
	private final static String FORMATO_4_DECIMALES = "%1.3f";
	private final static String DEFAULT_DECIMAL_NULO = "0.0";
	
	/**
	 * Interpreta un valor de tipo Double como una cadena de numeros con punto y 4 decimales.
	 * @param doubleParam El valor a interpretar.
	 * @return La representación del valor de entrada como cadena.
	 */
	public String getDoubleAsString(Double doubleParam) {
		return doubleParam != null ? String.format(FORMATO_4_DECIMALES, doubleParam) : DEFAULT_DECIMAL_NULO;
	}
	
	/**
	 * Interpreta un valor de tipo Long (8 bytes) como un valor de tipo Double (8 bytes).
	 * @param longParam El valor a interpretar.
	 * @return La representación del valor de entrada como doble.
	 */
	public Double getLongAsDouble(Long longParam) {
		return longParam != null ? Double.valueOf(longParam.doubleValue() / 1.0E8D) : null;
	}
	
	/**
	 * Interpreta un valor de tipo Integer (4 bytes) como un valor de tipo Double (8 bytes).
	 * @param integerParam El valor a interpretar.
	 * @return La represntación del valor de entrada como doble.
	 */
	public Double getIntegerAsDouble(Integer integerParam) {
		return integerParam != null ? Double.valueOf(integerParam.doubleValue() / 10000.0D) : null;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		
	}

}