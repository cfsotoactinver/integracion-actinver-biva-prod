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
package mx.com.actinver.biva.persistencia.basededatos;

import mx.com.actinver.terminal.notificaciones.*;
import mx.com.actinver.biva.error.ErrorDeBaseDeDatos;
import mx.com.biva.itch.modelo.C;
import mx.com.biva.itch.modelo.E;
import mx.com.biva.itch.modelo.H;
import mx.com.biva.itch.modelo.L;
import mx.com.biva.itch.modelo.M;
import mx.com.biva.itch.modelo.P;
import mx.com.biva.itch.modelo.R;

public interface PersistenciaBaseDeDatos {

	public void registraEnTrmCorro(Emisora notificacionDeEmisora) throws ErrorDeBaseDeDatos;
	public void registraEnTrmpRfc(Profundidad notificacionDeProfundidad) throws ErrorDeBaseDeDatos;
	public void registraEnTrmpRfv(Profundidad notificacionDeProfundidad) throws ErrorDeBaseDeDatos;
	public void registraEnTrmUhech(Emisora notificacionDeEmisora) throws ErrorDeBaseDeDatos;
	public void registraEnCorroCompra(Emisora notificacionDeEmisora) throws ErrorDeBaseDeDatos;
	public void registraEnCorroVenta(Emisora notificacionDeEmisora) throws ErrorDeBaseDeDatos;
	public void registraEnGrupo(R mensajeR) throws ErrorDeBaseDeDatos;
	public void registraEnTables(L mensajeL, M mensajeM) throws ErrorDeBaseDeDatos;
	public void registraEnUhecho(E mensajeE, C mensajeC, P mensajeP) throws ErrorDeBaseDeDatos, Exception;
	public void registraEnEstadoInstrumento(H mensajeH) throws ErrorDeBaseDeDatos;
	
}