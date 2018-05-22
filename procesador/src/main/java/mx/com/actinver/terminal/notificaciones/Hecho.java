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

public class Hecho {
	
	private String emisora;
	private String serie;
	private String numeroDeInstrumento;
	private String horaDelHecho;
	private Long volumen;
	private String precio;
	private String tipoDeConcertacion;
	private String folioDelHecho;
	private String fijaPrecio;
	private String tipoDeOperacion;
	private String importe;
	private String compra;
	private String vende;
	private Integer liquidacion;
	private String indicadorDeSubasta;
	
	private static final String PIPE = "|";
	
	public String topipes() {
		StringBuilder resultado = new StringBuilder();
		resultado.append(emisora == null ? "" : emisora);
		resultado.append(PIPE);
		resultado.append(serie == null ? "" : serie);
		resultado.append(PIPE);
		resultado.append(numeroDeInstrumento == null ? "" : numeroDeInstrumento);
		resultado.append(PIPE);
		resultado.append(horaDelHecho == null ? "19691231-18:00:00.000" : horaDelHecho);
		resultado.append(PIPE);
		resultado.append(volumen == null ? "0" : volumen);
		resultado.append(PIPE);
		resultado.append(precio == null ? "0" : precio);
		resultado.append(PIPE);
		resultado.append(tipoDeConcertacion == null ? "" : tipoDeConcertacion);
		resultado.append(PIPE);
		resultado.append(folioDelHecho == null ? "" : folioDelHecho);
		resultado.append(PIPE);
		resultado.append(fijaPrecio == null ? "" : fijaPrecio);
		resultado.append(PIPE);
		resultado.append(tipoDeOperacion == null ? "" : tipoDeOperacion);
		resultado.append(PIPE);
		resultado.append(importe == null ? "0" : importe);
		resultado.append(PIPE);
		resultado.append(compra == null ? "" : compra);
		resultado.append(PIPE);
		resultado.append(vende == null ? "" : vende);
		resultado.append(PIPE);
		resultado.append(liquidacion == null ? "" : liquidacion);
		resultado.append(PIPE);
		resultado.append(indicadorDeSubasta == null ? "" : indicadorDeSubasta);
		return resultado.toString();
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

	public String getNumeroDeInstrumento() {
		return numeroDeInstrumento;
	}

	public void setNumeroDeInstrumento(String numeroDeInstrumento) {
		this.numeroDeInstrumento = numeroDeInstrumento;
	}

	public String getHoraDelHecho() {
		return horaDelHecho;
	}

	public void setHoraDelHecho(String horaDelHecho) {
		this.horaDelHecho = horaDelHecho;
	}

	public Long getVolumen() {
	    return volumen;
	}

	public void setVolumen(Long volumen) {
	    this.volumen = volumen;
	}

	public static String getPipe() {
	    return PIPE;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public String getTipoDeConcertacion() {
		return tipoDeConcertacion;
	}

	public void setTipoDeConcertacion(String tipoDeConcertacion) {
		this.tipoDeConcertacion = tipoDeConcertacion;
	}

	public String getFolioDelHecho() {
		return folioDelHecho;
	}

	public void setFolioDelHecho(String folioDelHecho) {
		this.folioDelHecho = folioDelHecho;
	}

	public String getFijaPrecio() {
		return fijaPrecio;
	}

	public void setFijaPrecio(String fijaPrecio) {
		this.fijaPrecio = fijaPrecio;
	}

	public String getTipoDeOperacion() {
		return tipoDeOperacion;
	}

	public void setTipoDeOperacion(String tipoDeOperacion) {
		this.tipoDeOperacion = tipoDeOperacion;
	}

	public String getImporte() {
		return importe;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}

	public String getCompra() {
		return compra;
	}

	public void setCompra(String compra) {
		this.compra = compra;
	}

	public String getVende() {
		return vende;
	}

	public void setVende(String vende) {
		this.vende = vende;
	}

	public Integer getLiquidacion() {
		return liquidacion;
	}

	public void setLiquidacion(Integer liquidacion) {
		this.liquidacion = liquidacion;
	}

	public String getIndicadorDeSubasta() {
		return indicadorDeSubasta;
	}

	public void setIndicadorDeSubasta(String indicadorDeSubasta) {
		this.indicadorDeSubasta = indicadorDeSubasta;
	}
		
}