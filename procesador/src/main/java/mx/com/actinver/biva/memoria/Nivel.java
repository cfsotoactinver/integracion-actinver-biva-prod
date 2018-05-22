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
package mx.com.actinver.biva.memoria;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import mx.com.actinver.utilidades.Formateador;


public class Nivel {

	private Double precio;
	private Long volumen;
	private Integer numeroDeOrdenes;
	

	
	public Double getPrecio() {
		return precio;
	}
	
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	
	public Long getVolumen() {
	    return volumen;
	}

	public void setVolumen(Long volumen) {
	    this.volumen = volumen;
	}

	public Integer getNumeroDeOrdenes() {
		return numeroDeOrdenes;
	}

	public void setNumeroDeOrdenes(Integer numeroDeOrdenes) {
		this.numeroDeOrdenes = numeroDeOrdenes;
	}


	
}