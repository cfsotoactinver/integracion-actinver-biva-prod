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

public class DestinoObjeto {
	
	private String emisoraSerie;
	private String destino;
	private String objeto;
	
	public String getEmisoraSerie() {
		return emisoraSerie;
	}

	public void setEmisoraSerie(String emisoraSerie) {
		this.emisoraSerie = emisoraSerie;
	}
	
	public String toString() {
		return destino + objeto;
	}
	
	public String getObjeto() {
		return objeto;
	}

	public void setObjeto(String objeto) {
		this.objeto = objeto;
	}

	public String getDestino() {
		return destino;
	}
	
	public void setDestino(String destino) {
		this.destino = destino;
	}
}