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

import java.util.List;

public class Profundidad {
	
	private String issuer;
	private String serie;
	private Integer numeroDeInstrumento;
	private String isin;
	private String instrumentStatus;
	private Integer lado;
	private String fechaActualizacion;
	private Integer niveles;
	private List<Nivel> profundidad;
	
	private static final String PIPE = "|";

	public String topipes() {
		StringBuilder resultado = new StringBuilder();
		resultado.append(issuer == null ? "" : issuer);
		resultado.append(PIPE);
		resultado.append(serie == null ? "" : serie);
		resultado.append(PIPE);
		resultado.append(numeroDeInstrumento == null ? " " : numeroDeInstrumento);
		resultado.append(PIPE);
		resultado.append(isin == null ? "US2546871060" : isin);
		resultado.append(PIPE);
		resultado.append(instrumentStatus == null ? "N" : instrumentStatus);
		resultado.append(PIPE);
		resultado.append(lado == null ? "0" : lado);
		resultado.append(PIPE);
		resultado.append(fechaActualizacion == null ? "19691231-18:00:00.000" : fechaActualizacion);
		resultado.append(PIPE);
		resultado.append(niveles == null ? "0" : niveles);
		if(profundidad != null && Integer.valueOf(niveles).intValue() == profundidad.size()) {
			StringBuilder precios = new StringBuilder();
			StringBuilder numeroDeOrdenes = new StringBuilder();
			StringBuilder volumen = new StringBuilder();
			if(profundidad != null) {
				for(Nivel nivel : profundidad) {
					precios.append(PIPE);
					precios.append(nivel.getPrecio() == null ? "0" : nivel.getPrecio());
					numeroDeOrdenes.append(PIPE);
					numeroDeOrdenes.append(nivel.getOrdenes() == null ? "0" : nivel.getOrdenes());
					volumen.append(PIPE);
					volumen.append(nivel.getVolumen() == null ? "0" : nivel.getVolumen());
				}
			}
			resultado.append(precios.toString());
			resultado.append(numeroDeOrdenes.toString());
			resultado.append(volumen.toString());
		} else {
			StringBuilder precios = new StringBuilder();
			StringBuilder numeroDeOrdenes = new StringBuilder();
			StringBuilder volumen = new StringBuilder();
			if(profundidad != null) {
				for(int i=0;i<20;i++) {
					precios.append(PIPE);
					precios.append(0.0);
					numeroDeOrdenes.append(PIPE);
					numeroDeOrdenes.append(0);
					volumen.append(PIPE);
					volumen.append(0);
				}
			}
			resultado.append(precios.toString());
			resultado.append(numeroDeOrdenes.toString());
			resultado.append(volumen.toString());
		}

		return resultado.toString();
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public Integer getNumeroDeInstrumento() {
		return numeroDeInstrumento;
	}

	public void setNumeroDeInstrumento(Integer numeroDeInstrumento) {
		this.numeroDeInstrumento = numeroDeInstrumento;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getInstrumentStatus() {
		return instrumentStatus;
	}

	public void setInstrumentStatus(String instrumentStatus) {
		this.instrumentStatus = instrumentStatus;
	}

	public Integer getLado() {
		return lado;
	}

	public void setLado(Integer lado) {
		this.lado = lado;
	}

	public String getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(String fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public Integer getNiveles() {
		return niveles;
	}

	public void setNiveles(Integer niveles) {
		this.niveles = niveles;
	}

	public List<Nivel> getProfundidad() {
		return profundidad;
	}

	public void setProfundidad(List<Nivel> profundidad) {
		this.profundidad = profundidad;
	}

}