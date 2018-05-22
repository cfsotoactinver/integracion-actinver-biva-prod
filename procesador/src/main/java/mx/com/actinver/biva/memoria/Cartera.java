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

public class Cartera {
	private String carteraDePedido;
	private String emisora;
	private String serie;
	private String isin;
	private String sec_code;
	private Double precioDeApertura;
	private Integer numeroDeDecimales;

	
	
	public String getCarteraDePedido() {
		return carteraDePedido;
	}

	public void setCarteraDePedido(String carteraDePedido) {
		this.carteraDePedido = carteraDePedido;
	}

	public String getEmisora() {
		return emisora;
	}

	public void setEmisora(String emisora) {
		this.emisora = emisora;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getSec_code() {
		return sec_code;
	}

	public void setSec_code(String sec_code) {
		this.sec_code = sec_code;
	}

	public Integer getNumeroDeDecimales() {
		return numeroDeDecimales;
	}

	public void setNumeroDeDecimales(Integer numeroDeDecimales) {
		this.numeroDeDecimales = numeroDeDecimales;
	}

	public Double getPrecioDeApertura() {
	    return precioDeApertura;
	}

	public void setPrecioDeApertura(Double precioDeApertura) {
	    this.precioDeApertura = precioDeApertura;
	}

}