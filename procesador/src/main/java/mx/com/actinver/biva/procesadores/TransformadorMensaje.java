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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import mx.com.biva.itch.modelo.Message;
import mx.com.biva.itch.modelo.P;
import mx.com.biva.itch.modelo.R;
import mx.com.biva.itch.modelo.U;
import mx.com.biva.itch.modelo.X;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformadorMensaje implements Processor {
	@Produce(uri = "activemq:queue:NOTIFICATION.RASTREO")
	private ProducerTemplate emisora;
	@Produce(uri = "activemq:queue:HECHOs.RASTREO")
	private ProducerTemplate hecho;
	@Produce(uri = "activemq:queue:PROFUNDIDAD.RASTREO")
	private ProducerTemplate profundidad;

	private Memoria memoria;
	private Transformador transformador;
	private DestinoObjeto destino = new DestinoObjeto();
	private Orden orden = new Orden();
	private Boolean valida;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TransformadorMensaje.class);
	SimpleDateFormat format = new SimpleDateFormat("YYYYMMdd'-'HH:mm:ss.SSS");

	public void process(Exchange exchange) throws Exception {
		Date sysdate = new Date();
		Message mensaje = (Message) exchange.getIn().getBody(Message.class);
		List<DestinoObjeto> destinosObjetos = new ArrayList();
		String result = "hora de recepcion mensaje de tipo "
				+ mensaje.getType().charValue() + ": " + format.format(sysdate);
		switch (mensaje.getType().charValue()) {
		case 'A':
			A mensajeA = (A) mensaje;
			mensajeA.setTimestamp(Integer.valueOf(mensaje.getTimestamp()
					.intValue() * 1000 + mensajeA.getTimestamp().intValue()));
			valida = transformador.procesaMensajeA(mensajeA);
			if (valida.booleanValue() == true) {
				orden = memoria.buscarOrdenA(mensajeA.getOrderNumber());
				destino.setEmisoraSerie(orden.getEmisora() + " "
						+ orden.getSerie());
				destino.setDestino("ACTV.MULTICAST.MEX.BIVA.TOP.PROFUNDIDAD.NACIONAL");
				destino.setObjeto(memoria
						.obtenerNotificacionDeProfundiodadPorCartera(
								orden.getCarteraDePedidos()).topipes());
				profundidad.sendBodyAndHeader(result + "Hora de fin de procesamiento: "
						+ format.format(new Date()) +" "+destino.getObjeto(), "emisoraserie", orden.getEmisora() + " " + orden.getSerie());

				destinosObjetos.add(destino);
				if (orden.getEmisora().equals("SMARTRC")) {
					LOGGER.info("PROFUNDIDAD:" + destino.getObjeto());
				}
				destino = new DestinoObjeto();
				destino.setDestino("ACTV.MULTICAST.MEX.BIVA.TOP.NOTIFICATION.NACIONAL");
				destino.setEmisoraSerie(orden.getEmisora() + " "
						+ orden.getSerie());
				destino.setObjeto(memoria
						.obtenerNotificacionDeEmisoraPorCartera(
								orden.getCarteraDePedidos()).topipes());
				emisora.sendBodyAndHeader(result + "Hora de fin de procesamiento: "
						+ format.format(new Date()) +" "+destino.getObjeto(), "emisoraserie", orden.getEmisora() + " " + orden.getSerie());
				
				 
				destinosObjetos.add(destino);
				if (orden.getEmisora().equals("SMARTRC")) {
					LOGGER.info("EMISORA:" + destino.getObjeto());
				}
			}
			break;
		case 'D':
			D mensajeD = (D) mensaje;
			mensajeD.setTimestamp(Integer.valueOf(mensaje.getTimestamp()
					.intValue() * 1000 + mensajeD.getTimestamp().intValue()));
			valida = transformador.procesaMensajeD(mensajeD);
			if (valida.booleanValue() == true) {
				orden = memoria.buscarOrden(mensajeD.getOrderNumber());
				destino.setDestino("ACTV.MULTICAST.MEX.BIVA.TOP.PROFUNDIDAD.NACIONAL");
				destino.setEmisoraSerie(orden.getEmisora() + " "
						+ orden.getSerie());
				destino.setObjeto(memoria
						.obtenerNotificacionDeProfundiodadPorCartera(
								orden.getCarteraDePedidos()).topipes());
				profundidad.sendBodyAndHeader(result + "Hora de fin de procesamiento: "
						+ format.format(new Date()) +" "+destino.getObjeto(), "emisoraserie", orden.getEmisora() + " " + orden.getSerie());
				destinosObjetos.add(destino);
				if (orden.getEmisora().equals("SMARTRC")) {
					LOGGER.info("PROFUNDIDAD:" + destino.getObjeto());
				}
				destino = new DestinoObjeto();
				destino.setDestino("ACTV.MULTICAST.MEX.BIVA.TOP.NOTIFICATION.NACIONAL");
				destino.setEmisoraSerie(orden.getEmisora() + " "
						+ orden.getSerie());
				destino.setObjeto(memoria

				.obtenerNotificacionDeEmisoraPorCartera(
						orden.getCarteraDePedidos()).topipes());
				emisora.sendBodyAndHeader(result + "Hora de fin de procesamiento: "
						+ format.format(new Date()) +" "+destino.getObjeto(), "emisoraserie", orden.getEmisora() + " " + orden.getSerie());
				destinosObjetos.add(destino);
				memoria.eliminarOrdenD(mensajeD);
				if (orden.getEmisora().equals("SMARTRC")) {
					LOGGER.info("EMISORA:" + destino.getObjeto());
				}
			}
			break;
		case 'U':
			U mensajeU = (U) mensaje;
			mensajeU.setTimestamp(Integer.valueOf(mensaje.getTimestamp()
					.intValue() * 1000 + mensajeU.getTimestamp().intValue()));

			valida = transformador.procesaMensajeU(mensajeU);
			if (valida.booleanValue() == true) {
				orden = memoria.buscarOrden(mensajeU.getNewOrderNumber());
				destino.setDestino("ACTV.MULTICAST.MEX.BIVA.TOP.PROFUNDIDAD.NACIONAL");
				destino.setEmisoraSerie(orden.getEmisora() + " "
						+ orden.getSerie());
				destino.setObjeto(memoria

				.obtenerNotificacionDeProfundiodadPorCartera(
						orden.getCarteraDePedidos()).topipes());
				profundidad.sendBodyAndHeader(result + "Hora de fin de procesamiento: "
						+ format.format(new Date()) +" "+destino.getObjeto(), "emisoraserie", orden.getEmisora() + " " + orden.getSerie());
				destinosObjetos.add(destino);
				if (orden.getEmisora().equals("SMARTRC")) {
					LOGGER.info("PROFUNDIDAD:" + destino.getObjeto());
				}
				destino = new DestinoObjeto();
				destino.setDestino("ACTV.MULTICAST.MEX.BIVA.TOP.NOTIFICATION.NACIONAL");
				destino.setEmisoraSerie(orden.getEmisora() + " "
						+ orden.getSerie());
				destino.setObjeto(memoria

				.obtenerNotificacionDeEmisoraPorCartera(
						orden.getCarteraDePedidos()).topipes());
				emisora.sendBodyAndHeader(result + "Hora de fin de procesamiento: "
						+ format.format(new Date()) +" "+destino.getObjeto(), "emisoraserie", orden.getEmisora() + " " + orden.getSerie());
				destinosObjetos.add(destino);
				if (orden.getEmisora().equals("SMARTRC")) {
					LOGGER.info("EMISORA:" + destino.getObjeto());
				}
			}
			break;
		case 'E':
			E mensajeE = (E) mensaje;
			mensajeE.setTimestamp(Integer.valueOf(mensaje.getTimestamp()
					.intValue() * 1000 + mensajeE.getTimestamp().intValue()));

			valida = transformador.procesaMensajeE(mensajeE);
			if (valida.booleanValue() == true) {
				orden = memoria.buscarOrden(mensajeE.getOrderNumber());
				destino.setDestino("ACTV.MULTICAST.MEX.BIVA.TOP.PROFUNDIDAD.NACIONAL");
				destino.setEmisoraSerie(orden.getEmisora() + " "
						+ orden.getSerie());
				destino.setObjeto(memoria

				.obtenerNotificacionDeProfundiodadPorCartera(
						orden.getCarteraDePedidos()).topipes());
				destinosObjetos.add(destino);
				if (orden.getEmisora().equals("SMARTRC")) {
					LOGGER.info("PROFUNDIDAD:" + destino.getObjeto());
				}
				destino = new DestinoObjeto();
				destino.setDestino("ACTV.MULTICAST.MEX.BIVA.TOP.NOTIFICATION.NACIONAL");
				destino.setEmisoraSerie(orden.getEmisora() + " "
						+ orden.getSerie());
				destino.setObjeto(memoria

				.obtenerNotificacionDeEmisoraPorCartera(
						orden.getCarteraDePedidos()).topipes());
				emisora.sendBodyAndHeader(result + "Hora de fin de procesamiento: "
						+ format.format(new Date()) +" "+destino.getObjeto(), "emisoraserie", orden.getEmisora() + " " + orden.getSerie());
				destinosObjetos.add(destino);
				if (orden.getEmisora().equals("SMARTRC")) {
					LOGGER.info("EMISORA:" + destino.getObjeto());
				}
				destino = new DestinoObjeto();
				destino.setDestino("ACTV.MULTICAST.MEX.BIVA.TOP.HECHOS.NACIONAL");
				destino.setEmisoraSerie(orden.getEmisora() + " "
						+ orden.getSerie());
				destino.setObjeto(memoria

				.obtenerNotificacionDeHechoPorCartera(
						orden.getCarteraDePedidos()).topipes());
				hecho.sendBodyAndHeader(result + "Hora de fin de procesamiento: "
						+ format.format(new Date()) +" "+destino.getObjeto(), "emisoraserie", orden.getEmisora() + " " + orden.getSerie());
				destinosObjetos.add(destino);
				if (orden.getEmisora().equals("SMARTRC")) {
					LOGGER.info("HECHO:" + destino.getObjeto());
				}
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
				destino.setDestino("ACTV.MULTICAST.MEX.BIVA.TOP.PROFUNDIDAD.NACIONAL");
				destino.setEmisoraSerie(orden.getEmisora() + " "
						+ orden.getSerie());
				destino.setObjeto(memoria

				.obtenerNotificacionDeProfundiodadPorCartera(
						orden.getCarteraDePedidos()).topipes());
				profundidad.sendBodyAndHeader(result + "Hora de fin de procesamiento: "
						+ format.format(new Date()) +" "+destino.getObjeto(), "emisoraserie", orden.getEmisora() + " " + orden.getSerie());

				destinosObjetos.add(destino);
				if (orden.getEmisora().equals("SMARTRC")) {
					LOGGER.info("PROFUNDIDAD:" + destino.getObjeto());
				}
				destino = new DestinoObjeto();
				destino.setDestino("ACTV.MULTICAST.MEX.BIVA.TOP.NOTIFICATION.NACIONAL");
				destino.setEmisoraSerie(orden.getEmisora() + " "
						+ orden.getSerie());
				destino.setObjeto(memoria

				.obtenerNotificacionDeEmisoraPorCartera(
						orden.getCarteraDePedidos()).topipes());
				emisora.sendBodyAndHeader(result + "Hora de fin de procesamiento: "
						+ format.format(new Date()) +" "+destino.getObjeto(), "emisoraserie", orden.getEmisora() + " " + orden.getSerie());

				destinosObjetos.add(destino);
				if (orden.getEmisora().equals("SMARTRC")) {
					LOGGER.info("EMISORA:" + destino.getObjeto());
				}
				destino = new DestinoObjeto();
				destino.setDestino("ACTV.MULTICAST.MEX.BIVA.TOP.HECHOS.NACIONAL");
				destino.setEmisoraSerie(orden.getEmisora() + " "
						+ orden.getSerie());
				destino.setObjeto(memoria.obtenerNotificacionDeHechoPorCartera(
						orden.getCarteraDePedidos()).topipes());
				hecho.sendBodyAndHeader(result + "Hora de fin de procesamiento: "
						+ format.format(new Date()) +" "+destino.getObjeto(), "emisoraserie", orden.getEmisora() + " " + orden.getSerie());
				destinosObjetos.add(destino);
				if (orden.getEmisora().equals("SMARTRC")) {
					LOGGER.info("HECHO:" + destino.getObjeto());
				}
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
			if (valida.booleanValue() == true) {
				Cartera cartera = memoria
						.buscarCartera(mensajeP.getOrderBook());
				destino = new DestinoObjeto();
				destino.setDestino("ACTV.MULTICAST.MEX.BIVA.TOP.NOTIFICATION.NACIONAL");
				destino.setEmisoraSerie(cartera.getEmisora() + " "
						+ cartera.getSerie());
				destino.setObjeto(memoria

						.obtenerNotificacionDeEmisoraPorCartera(
								mensajeP.getOrderBook()).topipes());
				emisora.sendBodyAndHeader(result + "Hora de fin de procesamiento: "
						+ format.format(new Date()) +" "+destino.getObjeto(), "emisoraserie", orden.getEmisora() + " " + orden.getSerie());
				destinosObjetos.add(destino);
				if (cartera.getEmisora().equals("SMARTRC")) {
					LOGGER.info("EMISORA:" + destino.getObjeto());
				}
				destino = new DestinoObjeto();
				destino.setDestino("ACTV.MULTICAST.MEX.BIVA.TOP.HECHOS.NACIONAL");
				destino.setEmisoraSerie(cartera.getEmisora() + " "
						+ cartera.getSerie());
				destino.setObjeto(memoria

				.obtenerNotificacionDeHechoPorCartera(mensajeP.getOrderBook())
						.topipes());
				hecho.sendBodyAndHeader(result + "Hora de fin de procesamiento: "
						+ format.format(new Date()) +" "+destino.getObjeto(), "emisoraserie", orden.getEmisora() + " " + orden.getSerie());
				destinosObjetos.add(destino);
				if (cartera.getEmisora().equals("SMARTRC")) {
					LOGGER.info("HECHO:" + destino.getObjeto());
				}
			}
			break;
		case 'H':
			H mensajeH = (H) mensaje;
			mensajeH.setTimestamp(Integer.valueOf(mensaje.getTimestamp()
					.intValue() * 1000 + mensajeH.getTimestamp().intValue()));

			valida = transformador.procesaMensajeH(mensajeH);
			if (valida.booleanValue() == true) {
				Cartera carteras = memoria.buscarCartera(mensajeH
						.getOrderBook());
				destino.setDestino("ACTV.MULTICAST.MEX.BIVA.TOP.PROFUNDIDAD.NACIONAL");
				destino.setEmisoraSerie(carteras.getEmisora() + " "
						+ carteras.getSerie());
				destino.setObjeto(memoria
						.obtenerNotificacionDeProfundiodadPorCartera(
								mensajeH.getOrderBook()).topipes());
				profundidad.sendBodyAndHeader(result + "Hora de fin de procesamiento: "
						+ format.format(new Date()) +" "+destino.getObjeto(), "emisoraserie", orden.getEmisora() + " " + orden.getSerie());
				destinosObjetos.add(destino);
				if (carteras.getEmisora().equals("SMARTRC")) {
					LOGGER.info("PROFUNDIDAD:" + destino.getObjeto());
				}
				destino = new DestinoObjeto();
				destino.setDestino("ACTV.MULTICAST.MEX.BIVA.TOP.NOTIFICATION.NACIONAL");
				destino.setEmisoraSerie(carteras.getEmisora() + " "
						+ carteras.getSerie());
				destino.setEmisoraSerie(carteras.getEmisora() + " "
						+ carteras.getSerie());
				destino.setEmisoraSerie(carteras.getEmisora() + " "
						+ carteras.getSerie());
				destino.setObjeto(memoria

				.obtenerNotificacionDeEmisoraPorCartera(
						orden.getCarteraDePedidos()).topipes());
				emisora.sendBodyAndHeader(result + "Hora de fin de procesamiento: "
						+ format.format(new Date()) +" "+destino.getObjeto(), "emisoraserie", orden.getEmisora() + " " + orden.getSerie());

				destinosObjetos.add(destino);
				if (carteras.getEmisora().equals("SMARTRC")) {
					LOGGER.info("EMISORA:" + destino.getObjeto());
				}
			}
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
