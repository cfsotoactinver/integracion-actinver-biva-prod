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

import java.util.List;

public class Venta {
	private String idventa;
	private String emisora;
	private List<Nivel> niveles;
	
	public String getEmisora() {
		return emisora;
	}
	
	public void setEmisora(String emisora) {
		this.emisora = emisora;
	}
	
	public List<Nivel> getNiveles() {
		return niveles;
	}
	
	public void setNiveles(List<Nivel> niveles) {
		this.niveles = niveles;
	}

	public String getIdventa() {
		return idventa;
	}

	public void setIdventa(String idventa) {
		this.idventa = idventa;
	}
}