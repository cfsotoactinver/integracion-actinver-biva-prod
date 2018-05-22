/**
 * Copyright 2018 Actinver
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 **/
package mx.com.actinver.biva.procesadores;

import java.util.ArrayList;
import java.util.List;
import mx.com.actinver.biva.memoria.Cartera;
import mx.com.actinver.biva.memoria.Memoria;
import mx.com.actinver.biva.memoria.Orden;
import mx.com.actinver.terminal.notificaciones.DestinoObjeto;
import mx.com.actinver.terminal.notificaciones.Emisora;
import mx.com.actinver.terminal.notificaciones.Hecho;
import mx.com.actinver.terminal.notificaciones.Profundidad;
import mx.com.biva.itch.modelo.A;
import mx.com.biva.itch.modelo.C;
import mx.com.biva.itch.modelo.D;
import mx.com.biva.itch.modelo.E;
import mx.com.biva.itch.modelo.H;
import mx.com.biva.itch.modelo.L;
import mx.com.biva.itch.modelo.M;
import mx.com.biva.itch.modelo.P;
import mx.com.biva.itch.modelo.R;
import mx.com.biva.itch.modelo.U;
import mx.com.biva.itch.modelo.X;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformadorMensajeRetransmision implements Processor {
	private Memoria memoria;
	private Transformador transformador;
	private Orden orden = new Orden();
	private Boolean valida;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TransformadorMensajeRetransmision.class);

	public void process(Exchange exchange) throws Exception {
		mx.com.biva.itch.modelo.Message mensaje = (mx.com.biva.itch.modelo.Message) exchange
				.getIn().getBody(mx.com.biva.itch.modelo.Message.class);
		List<DestinoObjeto> destinosObjetos = new ArrayList();
		switch (mensaje.getType().charValue()) {
		case 'A':
			A mensajeA = (A) mensaje;
			mensajeA.setTimestamp(Integer.valueOf(mensaje.getTimestamp()
					.intValue() * 1000 + mensajeA.getTimestamp().intValue()));
			valida = transformador.procesaMensajeA(mensajeA);

			break;
		case 'D':
			D mensajeD = (D) mensaje;
			mensajeD.setTimestamp(Integer.valueOf(mensaje.getTimestamp()
					.intValue() * 1000 + mensajeD.getTimestamp().intValue()));
			valida = transformador.procesaMensajeD(mensajeD);
			break;
		case 'U':
			U mensajeU = (U) mensaje;
			mensajeU.setTimestamp(Integer.valueOf(mensaje.getTimestamp()
					.intValue() * 1000 + mensajeU.getTimestamp().intValue()));

			valida = transformador.procesaMensajeU(mensajeU);
			break;
		case 'E':
			E mensajeE = (E) mensaje;
			mensajeE.setTimestamp(Integer.valueOf(mensaje.getTimestamp()
					.intValue() * 1000 + mensajeE.getTimestamp().intValue()));

			valida = transformador.procesaMensajeE(mensajeE);
			if (valida.booleanValue() == true) {
				orden = memoria.buscarOrden(mensajeE.getOrderNumber());
				if (orden.getEliminaOrden().equals("E")) {
					memoria.eliminaOrdenU(mensajeE.getOrderNumber());
				}
			}
			break;
		case 'C':
			C mensajeC = (C) mensaje;
			mensajeC.setTimestamp(Integer.valueOf(mensaje.getTimestamp()
					.intValue() * 1000 + mensajeC.getTimestamp().intValue()));

			valida = transformador.procesaMensajeC(mensajeC);
			if (valida.booleanValue() == true) {
				orden = memoria.buscarOrden(mensajeC.getOrderNumber());
				if (orden.getEliminaOrden().equals("E")) {
					memoria.eliminaOrdenU(mensajeC.getOrderNumber());
				}
			}
			break;
		case 'P':
			P mensajeP = (P) mensaje;
			mensajeP.setTimestamp(Integer.valueOf(mensaje.getTimestamp()
					.intValue() * 1000 + mensajeP.getTimestamp().intValue()));

			valida = transformador.procesaMensajeP(mensajeP);
			break;
		case 'H':
			H mensajeH = (H) mensaje;
			mensajeH.setTimestamp(Integer.valueOf(mensaje.getTimestamp()
					.intValue() * 1000 + mensajeH.getTimestamp().intValue()));

			valida = transformador.procesaMensajeH(mensajeH);
			break;
		case 'R':
			R mensajeR = (R) mensaje;
			mensajeR.setTimestamp(Integer.valueOf(mensaje.getTimestamp()
					.intValue() * 1000 + mensajeR.getTimestamp().intValue()));
			valida = transformador.procesaMensajeR(mensajeR);
			break;
		case 'L':
			L mensajeL = (L) mensaje;
			valida = transformador.procesaMensajeL(mensajeL);
			break;
		case 'M':
			M mensajeM = (M) mensaje;
			valida = transformador.procesaMensajeM(mensajeM);
			break;
		case 'X':
			X mensajeX = (X) mensaje;
			valida = transformador.procesaMensajeX(mensajeX);
		}
		exchange.getIn().setBody(destinosObjetos);
	}

	public Transformador getTransformador() {
		return this.transformador;
	}

	public void setTransformador(Transformador transformador) {
		this.transformador = transformador;
	}

	public Memoria getMemoria() {
		return this.memoria;
	}

	public void setMemoria(Memoria memoria) {
		this.memoria = memoria;
	}
}
