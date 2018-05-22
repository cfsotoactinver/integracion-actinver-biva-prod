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
package mx.com.actinver.terminal.notificaciones;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

 
public class Nivel    {

	private Double precio;
	private Integer ordenes;
	private Long volumen;

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Integer getOrdenes() {
		return ordenes;
	}

	public void setOrdenes(Integer ordenes) {
		this.ordenes = ordenes;
	}



	public Long getVolumen() {
	    return volumen;
	}

	public void setVolumen(Long volumen) {
	    this.volumen = volumen;
	}


}