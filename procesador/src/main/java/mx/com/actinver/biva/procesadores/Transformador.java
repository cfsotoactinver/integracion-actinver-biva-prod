package mx.com.actinver.biva.procesadores;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import mx.com.actinver.biva.error.ErrorDeBaseDeDatos;
import mx.com.actinver.biva.memoria. * ;
import mx.com.biva.itch.modelo.A;
import mx.com.biva.itch.modelo.C;
import mx.com.biva.itch.modelo.D;
import mx.com.biva.itch.modelo.E;
import mx.com.biva.itch.modelo.H;
import mx.com.biva.itch.modelo.L;
import mx.com.biva.itch.modelo.M;
import mx.com.biva.itch.modelo.P;
import mx.com.biva.itch.modelo.R;
import mx.com.biva.itch.modelo.T;
import mx.com.biva.itch.modelo.U;
import mx.com.biva.itch.modelo.X;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.com.actinver.biva.memoria. * ;
import mx.com.actinver.biva.memoria.Nivel;
import mx.com.actinver.terminal.notificaciones. * ;

public class Transformador implements ReglasDeNegocio {

	@Produce(uri = "seda:datosGrid")
	private ProducerTemplate feedUnico;
	@Produce(uri = "activemq:queue:BIVA.ITCH.INSERT")
	private ProducerTemplate insert;
	SimpleDateFormat format = new SimpleDateFormat("YYYYMMdd'-'HH:mm:ss.SSS");
	// Servicios inyectados
	private Memoria memoria;
	private List < mx.com.actinver.terminal.notificaciones.Nivel > profniveles;
	// Mensajes a consola
	private final static Logger LOGGER = LoggerFactory.getLogger(Transformador.class);
	private static final String ESPACIO = " ";
	private static final String ERROR_TRS001 = "No se puede obtener el valor de la emisora a partir del codigo de seguridad ";
	private static final String ERROR_TRS002 = "El campo secCode es nulo.";
	private static final String ERROR_TRS003 = "No se puede obtener el valor de la serie a partir del codigo de seguridad ";
	private static final String ERROR_TRS004 = "No existe el precio de apertura";
	private static final String ERROR_TRS005 = "No existe la orden especificada";
	private static final String INFO_TRS001 = "Se registro el precio de apertura de la Orden Correctamente";
	private static final String INFO_TRS002 = "Se elimino la Orden Correctamente";
	private final static String INSERT_ITCH_UHECHO =
		      "INSERT INTO PRODSQL.ITCH_UHECHO_RH (ORDERBOOK,FOLIO_ASIGNACION,CANTIDAD,PRECIO,TRADE_INDICATOR,HORAORIGEN) VALUES (";
		  
	/**
	 * Obtiene la emisora a partir de el campo codigo de seguridad
	 * 
	 * @param secCode
	 *            El campo codigo de seguridad
	 * @return El valor de la emisora
	 */
	private String obtenerEmisora(String secCode) {
		String respuesta = null;
		if (secCode != null && !secCode.isEmpty()) {
			try {
				respuesta = secCode.substring(0, secCode.indexOf(ESPACIO));
			} catch(IndexOutOfBoundsException indexOutOfBoundsException) {
				LOGGER.error(ERROR_TRS001 + secCode, indexOutOfBoundsException);
			}
		} else {
			LOGGER.error(ERROR_TRS002);
		}
		return respuesta;
	}

	/**
	 * Obtiene la serie a partir de el campo codigo de seguridad
	 * 
	 * @param secCode
	 *            El campo codigo de seguridad
	 * @return El valor de la serie
	 */
	private String obtenerSerie(String secCode) {
		String respuesta = null;
		if (secCode != null && !secCode.isEmpty()) {
			try {
				respuesta = secCode.substring(secCode.indexOf(ESPACIO) + 1, secCode.length());
			} catch(IndexOutOfBoundsException indexOutOfBoundsException) {
				LOGGER.error(ERROR_TRS003 + secCode, indexOutOfBoundsException);
			}
		} else {
			LOGGER.error(ERROR_TRS002);
		}
		return respuesta;
	}

	/**
	 * Procesa el mensaje A ejecutando todas las reglas de negocio para dicho
	 * mensaje
	 * 
	 * @return Verdadero si se proceso de manera exitosa o Falso si ocurrio
	 *         algun error
	 * @param mensajeA
	 *            es el objeto correspondiente al mensaje A recibido por BIVA
	 */
	@Override
	public Boolean procesaMensajeA(A mensajeA) throws Exception {
		Orden orden = memoria.buscarOrdenA(mensajeA.getOrderNumber());
		if (orden.getNumeroDeOrden() == null) {
			Emisora emisora = memoria.obtenerNotificacionDeEmisoraPorCartera(mensajeA.getOrderBook());
			Cartera cartera = memoria.buscarCartera(mensajeA.getOrderBook());
			if (cartera.getNumeroDeDecimales() != null) {
				Integer decimales = cartera.getNumeroDeDecimales();
				if (mensajeA.getOrderNumber() == 0 && mensajeA.getQuantity() == 0) {
					LOGGER.error("Orden Descartada");
					return false;
				} else {
					orden.setVerboDeLaOrden(mensajeA.getOrderVerb().toString());
					orden.setNumeroDeOrden(mensajeA.getOrderNumber());
					orden.setCantidad(mensajeA.getQuantity());
					orden.setCarteraDePedidos(mensajeA.getOrderBook());
					orden.setPrecio(Double.valueOf(getDecimal(
					mensajeA.getPrice(), decimales)));
					orden.setEmisora(cartera.getEmisora());
					orden.setSerie(cartera.getSerie());
					orden.setEliminaOrden("N");
					memoria.actualizarOrden(orden);
					List < Nivel > niveles = new ArrayList < Nivel > ();
					Nivel nivel = new Nivel();
					String idPostura = orden.getEmisora() + orden.getCarteraDePedidos() + orden.getVerboDeLaOrden();
					Integer posicion = 0;
					if (orden.getVerboDeLaOrden() != null) {
						if (orden.getVerboDeLaOrden().equals("B")) {
							Compra compra = memoria.buscarArregloCompra(orden.getEmisora(), orden.getCarteraDePedidos().toString());
							compra.setEmisora(orden.getEmisora());
							compra.setIdcompra(idPostura);
							if (compra.getNiveles() != null) {
								niveles = compra.getNiveles();
								Nivel precioABuscar = niveles.stream().filter(order->order.getPrecio().equals(orden.getPrecio())).findAny().orElse(null);
								posicion = niveles.indexOf(precioABuscar);
								if (posicion >= 0) {
									nivel = niveles.get(posicion);
									nivel.setPrecio(orden.getPrecio());
									nivel.setVolumen(nivel.getVolumen() + orden.getCantidad());
									nivel.setNumeroDeOrdenes(nivel.getNumeroDeOrdenes() + 1);
									niveles.remove(niveles.indexOf(precioABuscar));
									niveles.add(posicion, nivel);
								} else {
									nivel.setPrecio(orden.getPrecio());
									nivel.setVolumen(orden.getCantidad());
									nivel.setNumeroDeOrdenes(1);
									niveles.add(nivel);
								}
							} else {
								nivel.setPrecio(orden.getPrecio());
								nivel.setVolumen(orden.getCantidad());
								nivel.setNumeroDeOrdenes(1);
								niveles.add(nivel);
							}
							niveles.sort(Comparator.comparing(Nivel::getPrecio).reversed());
							compra.setNiveles(niveles);
							compra.setIdcompra(idPostura);
							memoria.atualizarArregoCompra(idPostura, compra);
							Date sysdate = new Date();
							emisora.setIssuer(cartera.getEmisora());
							emisora.setSeries(cartera.getSerie());
							emisora.setIsin(cartera.getIsin());
							emisora.setInstrumentNumber(orden.getCarteraDePedidos());
							emisora.setOpeningPrice(cartera.getPrecioDeApertura());
							emisora.setPostura("compra");
							emisora.setBidVolume(compra.getNiveles().get(0).getVolumen());
							emisora.setBestBid(compra.getNiveles().get(0).getPrecio());
							emisora.setBidTime(this.format.format(sysdate));
							memoria.actualizaEmisora(emisora);
							Profundidad profundidad = memoria.obtenerNotificacionDeProfundiodadPorCartera(orden.getCarteraDePedidos());
							compra = memoria.buscarArregloCompra(orden.getEmisora(), orden.getCarteraDePedidos().toString());
							niveles = compra.getNiveles();
							int i = 0;
							List < mx.com.actinver.terminal.notificaciones.Nivel > nprof = new ArrayList < mx.com.actinver.terminal.notificaciones.Nivel > ();
							mx.com.actinver.terminal.notificaciones.Nivel prof = new mx.com.actinver.terminal.notificaciones.Nivel();
							for (Nivel nivel2: niveles) {
								prof = new mx.com.actinver.terminal.notificaciones.Nivel();
								try {
									prof.setPrecio(nivel2.getPrecio());
									prof.setVolumen(nivel2.getVolumen());
									prof.setOrdenes(nivel2.getNumeroDeOrdenes());
									nprof.add(prof);
									i++;
								} catch(Exception e) {
									prof.setPrecio(0.0);
									prof.setVolumen(0L);
									prof.setOrdenes(0);
									nprof.add(prof);
									i++;
								}
								if (i == 20) {
									break;
								}
							}
							profundidad.setIssuer(cartera.getEmisora());
							profundidad.setSerie(cartera.getSerie());
							profundidad.setIsin(cartera.getIsin());
							profundidad.setNumeroDeInstrumento(orden.getCarteraDePedidos());
							profundidad.setProfundidad(nprof);
							profundidad.setLado(0);
							profundidad.setNiveles(nprof.size());
							profundidad.setFechaActualizacion(this.format.format(sysdate));
							if (profundidad.getInstrumentStatus() != null) {
								profundidad.setInstrumentStatus("N");
							}
							memoria.actualizaProfundidad(profundidad);
							feedUnico.sendBody(orden);
							feedUnico.sendBody(emisora);
							feedUnico.sendBody(profundidad);
							feedUnico.sendBody(compra);
						} else if (orden.getVerboDeLaOrden().equals("S")) {
							Venta venta = memoria.buscarArregloVenta(orden.getEmisora(), orden.getCarteraDePedidos().toString());
							venta.setEmisora(orden.getEmisora());
							venta.setIdventa(idPostura);
							if (venta.getNiveles() != null) {
								niveles = venta.getNiveles();
								Nivel precioABuscar = niveles.stream().filter(order->order.getPrecio().equals(orden.getPrecio())).findAny().orElse(null);
								posicion = niveles.indexOf(precioABuscar);
								if (posicion >= 0) {
									nivel = niveles.get(posicion);
									nivel.setPrecio(orden.getPrecio());
									nivel.setVolumen(nivel.getVolumen() + orden.getCantidad());
									nivel.setNumeroDeOrdenes(nivel.getNumeroDeOrdenes() + 1);
									niveles.remove(niveles.indexOf(precioABuscar));
									niveles.add(posicion, nivel);
								} else {
									nivel.setPrecio(orden.getPrecio());
									nivel.setVolumen(orden.getCantidad());
									nivel.setNumeroDeOrdenes(1);
									niveles.add(nivel);
								}
							} else {
								nivel.setPrecio(orden.getPrecio());
								nivel.setVolumen(orden.getCantidad());
								nivel.setNumeroDeOrdenes(1);
								niveles.add(nivel);
							}
							niveles.sort(Comparator.comparing(Nivel::getPrecio));
							venta.setNiveles(niveles);
							memoria.atualizarArregoVenta(idPostura, venta);
							Date sysdate = new Date();
							emisora.setIssuer(cartera.getEmisora());
							emisora.setSeries(cartera.getSerie());
							emisora.setIsin(cartera.getIsin());
							emisora.setInstrumentNumber(orden.getCarteraDePedidos());
							emisora.setOpeningPrice(cartera.getPrecioDeApertura());
							emisora.setPostura("venta");
							emisora.setOfferVolume(venta.getNiveles().get(0).getVolumen());
							emisora.setBestOffer(venta.getNiveles().get(0).getPrecio());
							emisora.setOfferTime(this.format.format(sysdate));
							memoria.actualizaEmisora(emisora);
							Profundidad profundidad = memoria.obtenerNotificacionDeProfundiodadPorCartera(orden.getCarteraDePedidos());
							int i = 0;
							List < mx.com.actinver.terminal.notificaciones.Nivel > nprof = new ArrayList < mx.com.actinver.terminal.notificaciones.Nivel > ();
							mx.com.actinver.terminal.notificaciones.Nivel prof = new mx.com.actinver.terminal.notificaciones.Nivel();
							for (Nivel nivel2: niveles) {
								prof = new mx.com.actinver.terminal.notificaciones.Nivel();
								try {
									prof.setPrecio(nivel2.getPrecio());
									prof.setVolumen(nivel2.getVolumen());
									prof.setOrdenes(nivel2.getNumeroDeOrdenes());
									nprof.add(prof);
									i++;
								} catch(Exception e) {
									prof.setPrecio(0.0);
									prof.setVolumen(0L);
									prof.setOrdenes(0);
									nprof.add(prof);
									i++;
								}
								if (i == 20) {
									break;
								}
							}
							profundidad.setIssuer(cartera.getEmisora());
							profundidad.setSerie(cartera.getSerie());
							profundidad.setIsin(cartera.getIsin());
							profundidad.setNumeroDeInstrumento(orden.getCarteraDePedidos());
							profundidad.setProfundidad(nprof);
							profundidad.setNiveles(nprof.size());
							profundidad.setFechaActualizacion(this.format.format(sysdate));
							profundidad.setLado(1);
							if (profundidad.getInstrumentStatus() != null) {
								profundidad.setInstrumentStatus("N");
							}
							memoria.actualizaProfundidad(profundidad);
							feedUnico.sendBody(orden);
							feedUnico.start();
							feedUnico.sendBody(emisora);
							feedUnico.start();
							feedUnico.sendBody(profundidad);
							feedUnico.sendBody(venta);
						}
						return true;
					} else {
						LOGGER.error("La orden no tiene postura(B/S)...");
						return false;
					}
				}
			} else {
				LOGGER.error("Mensaje descartado No existe Price Decimals de esta orden");
				return false;
			}
		}
		LOGGER.error("Ya existe esta Orden");
		return false;
	}

	@Override
	public Boolean procesaMensajeD(D mensajeD) throws Exception {
		Orden orden = memoria.buscarOrden(mensajeD.getOrderNumber());
		if (orden != null) {
			Emisora emisora = memoria.obtenerNotificacionDeEmisoraPorCartera(orden.getCarteraDePedidos());
			Cartera cartera = this.memoria.buscarCartera(orden.getCarteraDePedidos());
			List < Nivel > niveles = new ArrayList < Nivel > ();
			Nivel nivel = new Nivel();
			String idPostura = orden.getEmisora() + orden.getCarteraDePedidos() + orden.getVerboDeLaOrden();
			Integer posicion = 0;
			if (orden.getVerboDeLaOrden() != null && orden.getEliminaOrden().equals("N")) {
				if (orden.getVerboDeLaOrden().equals("B")) {
					Compra compra = memoria.buscarArregloCompra(orden.getEmisora(), orden.getCarteraDePedidos().toString());
					if (compra.getNiveles() != null) {
						niveles = compra.getNiveles();
						Nivel precioABuscar = niveles.stream().filter(order->order.getPrecio().equals(
						orden.getPrecio())).findAny().orElse(null);
						posicion = niveles.indexOf(precioABuscar);

						if (posicion >= 0) {
							nivel = niveles.get(posicion);
							if (nivel.getNumeroDeOrdenes() >= 2) {
								nivel.setPrecio(orden.getPrecio());
								nivel.setVolumen(nivel.getVolumen() - orden.getCantidad());
								nivel.setNumeroDeOrdenes(nivel.getNumeroDeOrdenes() - 1);
								niveles.remove(niveles.indexOf(precioABuscar));
								niveles.add(posicion, nivel);
								niveles.sort(Comparator.comparing(
								Nivel::getPrecio).reversed());
							} else {
								niveles.remove(niveles.indexOf(precioABuscar));
								niveles.sort(Comparator.comparing(
								Nivel::getPrecio).reversed());
								compra.setNiveles(niveles);
								memoria.atualizarArregoCompra(idPostura, compra);
							}
							compra.setNiveles(niveles);
							memoria.atualizarArregoCompra(idPostura, compra);
							Date sysdate = new Date();
							emisora.setIssuer(cartera.getEmisora());
							emisora.setSeries(cartera.getSerie());
							emisora.setIsin(cartera.getIsin());
							emisora.setInstrumentNumber(orden.getCarteraDePedidos());
							emisora.setOpeningPrice(cartera.getPrecioDeApertura());
							emisora.setPostura("compra");
							if (compra.getNiveles().size() > 0) {
								emisora.setBidVolume((compra.getNiveles().get(0).getVolumen()));
								emisora.setBestBid(compra.getNiveles().get(0).getPrecio());
								emisora.setBidTime(format.format(sysdate));
							} else {
								emisora.setBidVolume(0l);
								emisora.setBestBid(0.0);
								emisora.setBidTime(format.format(sysdate));
							}

							emisora.setBidTime(format.format(sysdate));
							emisora.setInstrumentNumber(orden.getCarteraDePedidos());
							emisora.setOpeningPrice(cartera.getPrecioDeApertura());
							memoria.actualizaEmisora(emisora);
							Profundidad profundidad = memoria.obtenerNotificacionDeProfundiodadPorCartera(orden.getCarteraDePedidos());
							int i = 0;
							List < mx.com.actinver.terminal.notificaciones.Nivel > nprof = new ArrayList < mx.com.actinver.terminal.notificaciones.Nivel > ();
							mx.com.actinver.terminal.notificaciones.Nivel prof = new mx.com.actinver.terminal.notificaciones.Nivel();
							for (Nivel nivel2: niveles) {
								prof = new mx.com.actinver.terminal.notificaciones.Nivel();
								try {
									prof.setPrecio(nivel2.getPrecio());
									prof.setVolumen(nivel2.getVolumen());
									prof.setOrdenes(nivel2.getNumeroDeOrdenes());
									nprof.add(prof);
									i++;
								} catch(Exception e) {
									prof.setPrecio(0.0);
									prof.setVolumen(0L);
									prof.setOrdenes(0);
									nprof.add(prof);
									i++;
								}
								if (i == 20) {
									break;
								}

							}
							profundidad.setIssuer(cartera.getEmisora());
							profundidad.setSerie(cartera.getSerie());
							profundidad.setIsin(cartera.getIsin());
							profundidad.setNumeroDeInstrumento(orden.getCarteraDePedidos());
							profundidad.setProfundidad(nprof);
							profundidad.setLado(0);
							if (profundidad.getInstrumentStatus() != null) {
								profundidad.setInstrumentStatus("N");
							}
							profundidad.setNiveles(nprof.size());
							profundidad.setFechaActualizacion(format.format(sysdate));
							memoria.actualizaProfundidad(profundidad);
							feedUnico.sendBody(emisora);
							feedUnico.sendBody(compra);
							feedUnico.sendBody(profundidad);
						}
					}
				} else if (orden.getVerboDeLaOrden().equals("S")) {
					Venta venta = memoria.buscarArregloVenta(
					orden.getEmisora(), orden.getCarteraDePedidos().toString());
					if (venta.getNiveles() != null) {
						niveles = venta.getNiveles();
						Nivel precioABuscar = niveles.stream().filter(order->order.getPrecio().equals(
						orden.getPrecio())).findAny().orElse(null);
						posicion = niveles.indexOf(precioABuscar);

						if (posicion >= 0) {
							nivel = niveles.get(posicion);
							if (nivel.getNumeroDeOrdenes() >= 2) {
								nivel.setPrecio(orden.getPrecio());
								nivel.setVolumen(nivel.getVolumen() - orden.getCantidad());
								nivel.setNumeroDeOrdenes(nivel.getNumeroDeOrdenes() - 1);
								niveles.remove(niveles.indexOf(precioABuscar));
								niveles.add(posicion, nivel);
								niveles.sort(Comparator.comparing(Nivel::getPrecio));
							} else {
								niveles.remove(niveles.indexOf(precioABuscar));
								niveles.sort(Comparator.comparing(Nivel::getPrecio));
								venta.setNiveles(niveles);
								memoria.atualizarArregoVenta(idPostura, venta);
							}
							venta.setNiveles(niveles);
							memoria.atualizarArregoVenta(idPostura, venta);
							Date sysdate = new Date();
							emisora.setIssuer(cartera.getEmisora());
							emisora.setSeries(cartera.getSerie());
							emisora.setIsin(cartera.getIsin());
							emisora.setInstrumentNumber(orden.getCarteraDePedidos());
							emisora.setOpeningPrice(cartera.getPrecioDeApertura());
							emisora.setPostura("venta");
							if (venta.getNiveles().size() > 0) {
								emisora.setOfferVolume((venta.getNiveles().get(
								0).getVolumen()));
								emisora.setBestOffer(venta.getNiveles().get(0).getPrecio());
							} else {
								emisora.setOfferVolume(0l);
								emisora.setBestOffer(0.0);

							}
							emisora.setOfferTime(format.format(sysdate));
							emisora.setOpeningPrice(cartera.getPrecioDeApertura());
							memoria.actualizaEmisora(emisora);
							Profundidad profundidad = memoria.obtenerNotificacionDeProfundiodadPorCartera(orden.getCarteraDePedidos());
							int i = 0;
							List < mx.com.actinver.terminal.notificaciones.Nivel > nprof = new ArrayList < mx.com.actinver.terminal.notificaciones.Nivel > ();
							mx.com.actinver.terminal.notificaciones.Nivel prof = new mx.com.actinver.terminal.notificaciones.Nivel();
							for (Nivel nivel2: niveles) {
								prof = new mx.com.actinver.terminal.notificaciones.Nivel();
								try {
									prof.setPrecio(nivel2.getPrecio());
									prof.setVolumen(nivel2.getVolumen());
									prof.setOrdenes(nivel2.getNumeroDeOrdenes());
									nprof.add(prof);
									i++;
								} catch(Exception e) {
									prof.setPrecio(0.0);
									prof.setVolumen(0L);
									prof.setOrdenes(0);
									nprof.add(prof);
									i++;
								}
								if (i == 20) {
									break;
								}

							}
							profundidad.setIssuer(cartera.getEmisora());
							profundidad.setSerie(cartera.getSerie());
							profundidad.setIsin(cartera.getIsin());
							profundidad.setNumeroDeInstrumento(orden.getCarteraDePedidos());
							profundidad.setProfundidad(nprof);
							profundidad.setLado(1);
							if (profundidad.getInstrumentStatus() != null) {
								profundidad.setInstrumentStatus("N");
							}
							profundidad.setNiveles(nprof.size());
							profundidad.setFechaActualizacion(format.format(sysdate));
							memoria.actualizaProfundidad(profundidad);
							feedUnico.sendBody(emisora);
							feedUnico.sendBody(profundidad);
							feedUnico.sendBody(venta);
						}
					}
				}
			} else {
				return false;
			}
			return true;
		} else {
			LOGGER.error("No existe la orden a eliminar");
			return false;
		}

	}

	@Override
	public Boolean procesaMensajeU(U mensajeU) throws Exception {
		Orden orden = memoria.buscarOrden(mensajeU.getOriginalOrderNumber());
		if (orden != null) {
			Cartera cartera = memoria.buscarCartera(orden.getCarteraDePedidos());
			if (cartera.getNumeroDeDecimales() != null && orden.getEliminaOrden().equals("N")) {
				Emisora emisora = memoria.obtenerNotificacionDeEmisoraPorCartera(orden.getCarteraDePedidos());
				memoria.eliminaOrdenU(mensajeU.getOriginalOrderNumber());
				Orden ordenNueva = new Orden();
				Integer decimales = cartera.getNumeroDeDecimales();
				memoria.eliminaOrdenU(mensajeU.getOriginalOrderNumber());
				ordenNueva.setCarteraDePedidos(Integer.valueOf(cartera.getCarteraDePedido()));
				ordenNueva.setCantidad(mensajeU.getQuantity());
				ordenNueva.setEmisora(cartera.getEmisora());
				ordenNueva.setNumeroDeOrden(mensajeU.getNewOrderNumber());
				ordenNueva.setPrecio(Double.valueOf(getDecimal(mensajeU.getPrice(), decimales)));
				ordenNueva.setSerie(cartera.getSerie());
				ordenNueva.setVerboDeLaOrden(orden.getVerboDeLaOrden());
				ordenNueva.setEliminaOrden("N");
				memoria.registrarOrdern(ordenNueva);
				List < Nivel > niveles = new ArrayList < Nivel > ();
				Nivel nivel = new Nivel();
				String idPostura = orden.getEmisora() + orden.getCarteraDePedidos() + orden.getVerboDeLaOrden();
				Integer posicion = 0;
				if (orden.getVerboDeLaOrden() != null) {
					if (orden.getVerboDeLaOrden().equals("B")) {
						Compra compra = memoria.buscarArregloCompra(orden.getEmisora(), orden.getCarteraDePedidos().toString());
						if (compra.getNiveles() != null) {
							niveles = compra.getNiveles();
							Nivel precioABuscar = niveles.stream().filter(order->order.getPrecio().equals(
							orden.getPrecio())).findAny().orElse(null);
							posicion = niveles.indexOf(precioABuscar);
							if (posicion >= 0) {
								nivel = niveles.get(posicion);
								if (nivel.getNumeroDeOrdenes() >= 2) {
									nivel.setPrecio(orden.getPrecio());
									nivel.setVolumen(nivel.getVolumen() - orden.getCantidad());
									nivel.setNumeroDeOrdenes(nivel.getNumeroDeOrdenes() - 1);
									niveles.remove(niveles.indexOf(precioABuscar));
									niveles.add(posicion, nivel);
								} else {
									niveles.remove(niveles.indexOf(precioABuscar));
								}
								nivel = new Nivel();
								precioABuscar = niveles.stream().filter(order->order.getPrecio().equals(ordenNueva.getPrecio())).findAny().orElse(null);
								posicion = niveles.indexOf(precioABuscar);
								if (posicion >= 0) {
									nivel = niveles.get(posicion);
									if (nivel.getNumeroDeOrdenes() >= 1) {
										nivel.setPrecio(ordenNueva.getPrecio());
										nivel.setVolumen(nivel.getVolumen() + ordenNueva.getCantidad());
										nivel.setNumeroDeOrdenes(nivel.getNumeroDeOrdenes() + 1);
										niveles.remove(niveles.indexOf(precioABuscar));
										niveles.add(posicion, nivel);
									} else {
										nivel.setPrecio(ordenNueva.getPrecio());
										nivel.setVolumen(ordenNueva.getCantidad());
										nivel.setNumeroDeOrdenes(1);

										niveles.add(posicion, nivel);
									}
									niveles.sort(Comparator.comparing(
									Nivel::getPrecio).reversed());
								} else {
									nivel.setPrecio(ordenNueva.getPrecio());
									nivel.setVolumen(ordenNueva.getCantidad());
									nivel.setNumeroDeOrdenes(1);
									niveles.add(nivel);
									niveles.sort(Comparator.comparing(
									Nivel::getPrecio).reversed());

								}
							}
						} else {
							LOGGER.error("No existe la orden en el arreglo de compra");
						}
						compra.setNiveles(niveles);
						memoria.atualizarArregoCompra(idPostura, compra);
						Date sysdate = new Date();
						emisora.setIssuer(cartera.getEmisora());
						emisora.setSeries(cartera.getSerie());
						emisora.setIsin(cartera.getIsin());
						emisora.setInstrumentNumber(orden.getCarteraDePedidos());
						emisora.setOpeningPrice(cartera.getPrecioDeApertura());
						emisora.setPostura("compra");
						emisora.setBidVolume(compra.getNiveles().get(0).getVolumen());
						emisora.setBestBid(compra.getNiveles().get(0).getPrecio());
						emisora.setBidTime(format.format(sysdate));
						memoria.actualizaEmisora(emisora);
						Profundidad profundidad = memoria.obtenerNotificacionDeProfundiodadPorCartera(orden.getCarteraDePedidos());
						int i = 0;
						List < mx.com.actinver.terminal.notificaciones.Nivel > nprof = new ArrayList < mx.com.actinver.terminal.notificaciones.Nivel > ();
						mx.com.actinver.terminal.notificaciones.Nivel prof = new mx.com.actinver.terminal.notificaciones.Nivel();
						for (Nivel nivel2: niveles) {
							prof = new mx.com.actinver.terminal.notificaciones.Nivel();
							try {
								prof.setPrecio(nivel2.getPrecio());
								prof.setVolumen(nivel2.getVolumen());
								prof.setOrdenes(nivel2.getNumeroDeOrdenes());
								nprof.add(prof);
								i++;
							} catch(Exception e) {
								prof.setPrecio(0.0);
								prof.setVolumen(0L);
								prof.setOrdenes(0);
								nprof.add(prof);
								i++;
							}
							if (i == 20) {
								break;
							}

						}
						profundidad.setIssuer(cartera.getEmisora());
						profundidad.setSerie(cartera.getSerie());
						profundidad.setIsin(cartera.getIsin());
						profundidad.setNumeroDeInstrumento(orden.getCarteraDePedidos());
						profundidad.setProfundidad(nprof);
						profundidad.setNumeroDeInstrumento(orden.getCarteraDePedidos());
						profundidad.setLado(0);
						if (profundidad.getInstrumentStatus() != null) {
							profundidad.setInstrumentStatus("N");
						}
						profundidad.setNiveles(nprof.size());
						profundidad.setFechaActualizacion(format.format(sysdate));
						memoria.actualizaProfundidad(profundidad);
						feedUnico.sendBody(emisora);
						feedUnico.sendBody(profundidad);
						feedUnico.sendBody(compra);
						return true;
					} else if (orden.getVerboDeLaOrden().equals("S")) {
						Venta venta = memoria.buscarArregloVenta(orden.getEmisora(), orden.getCarteraDePedidos().toString());
						if (venta.getNiveles() != null) {
							niveles = venta.getNiveles();
							Nivel precioABuscar = niveles.stream().filter(order->order.getPrecio().equals(
							orden.getPrecio())).findAny().orElse(null);
							posicion = niveles.indexOf(precioABuscar);
							if (posicion >= 0) {
								nivel = niveles.get(posicion);
								if (nivel.getNumeroDeOrdenes() >= 2) {
									nivel.setPrecio(orden.getPrecio());
									nivel.setVolumen(nivel.getVolumen() - orden.getCantidad());
									nivel.setNumeroDeOrdenes(nivel.getNumeroDeOrdenes() - 1);
									niveles.remove(niveles.indexOf(precioABuscar));
									niveles.add(posicion, nivel);
								} else {
									niveles.remove(niveles.indexOf(precioABuscar));
								}
								nivel = new Nivel();
								precioABuscar = niveles.stream().filter(order->order.getPrecio().equals(ordenNueva.getPrecio())).findAny().orElse(null);
								posicion = niveles.indexOf(precioABuscar);
								if (posicion >= 0) {
									nivel = niveles.get(posicion);
									if (nivel.getNumeroDeOrdenes() >= 1) {
										nivel.setPrecio(ordenNueva.getPrecio());
										nivel.setVolumen(nivel.getVolumen() + ordenNueva.getCantidad());
										nivel.setNumeroDeOrdenes(nivel.getNumeroDeOrdenes() + 1);
										niveles.remove(niveles.indexOf(precioABuscar));
										niveles.add(posicion, nivel);
									} else {
										nivel.setPrecio(ordenNueva.getPrecio());
										nivel.setVolumen(ordenNueva.getCantidad());
										nivel.setNumeroDeOrdenes(1);

										niveles.add(posicion, nivel);
									}
									niveles.sort(Comparator.comparing(
									Nivel::getPrecio));
								} else {
									nivel.setPrecio(ordenNueva.getPrecio());
									nivel.setVolumen(ordenNueva.getCantidad());
									nivel.setNumeroDeOrdenes(1);
									niveles.add(nivel);
									niveles.sort(Comparator.comparing(
									Nivel::getPrecio));

								}
							}
						} else {
							LOGGER.error("No existe la orden en el arreglo de venta");
						}
						venta.setNiveles(niveles);
						memoria.atualizarArregoVenta(idPostura, venta);
						Date sysdate = new Date();
						emisora.setIssuer(cartera.getEmisora());
						emisora.setSeries(cartera.getSerie());
						emisora.setIsin(cartera.getIsin());
						emisora.setInstrumentNumber(orden.getCarteraDePedidos());
						emisora.setOpeningPrice(cartera.getPrecioDeApertura());
						emisora.setPostura("venta");
						emisora.setOfferVolume(venta.getNiveles().get(0).getVolumen());
						emisora.setBestOffer(venta.getNiveles().get(0).getPrecio());
						emisora.setOfferTime(this.format.format(sysdate));
						memoria.actualizaEmisora(emisora);
						Profundidad profundidad = memoria.obtenerNotificacionDeProfundiodadPorCartera(orden.getCarteraDePedidos());
						int i = 0;
						List < mx.com.actinver.terminal.notificaciones.Nivel > nprof = new ArrayList < mx.com.actinver.terminal.notificaciones.Nivel > ();
						mx.com.actinver.terminal.notificaciones.Nivel prof = new mx.com.actinver.terminal.notificaciones.Nivel();
						for (Nivel nivel2: niveles) {
							prof = new mx.com.actinver.terminal.notificaciones.Nivel();
							try {
								prof.setPrecio(nivel2.getPrecio());
								prof.setVolumen(nivel2.getVolumen());
								prof.setOrdenes(nivel2.getNumeroDeOrdenes());
								nprof.add(prof);
								i++;
							} catch(Exception e) {
								prof.setPrecio(0.0);
								prof.setVolumen(0L);
								prof.setOrdenes(0);
								nprof.add(prof);
								i++;
							}
							if (i == 20) {
								break;
							}

						}
						profundidad.setIssuer(cartera.getEmisora());
						profundidad.setSerie(cartera.getSerie());
						profundidad.setIsin(cartera.getIsin());
						profundidad.setNumeroDeInstrumento(orden.getCarteraDePedidos());
						profundidad.setProfundidad(nprof);
						profundidad.setNumeroDeInstrumento(orden.getCarteraDePedidos());
						profundidad.setLado(1);
						if (profundidad.getInstrumentStatus() != null) {
							profundidad.setInstrumentStatus("N");
						}
						profundidad.setNiveles(nprof.size());
						profundidad.setFechaActualizacion(format.format(sysdate));
						memoria.actualizaProfundidad(profundidad);
						feedUnico.sendBody(emisora);
						feedUnico.sendBody(profundidad);
						feedUnico.sendBody(venta);
						return true;
					}
				} else {
					return false;
				}
			}
			return true;
		} else {
			LOGGER.error("No existe la orden a Actualizar");
			return false;
		}
	}

	@Override
	public Boolean procesaMensajeE(E mensajeE) throws Exception {
		Orden orden = memoria.buscarOrden(mensajeE.getOrderNumber());
		if (orden != null) {
			String sql=registraEnUhecho(mensajeE, null, null);
			insert.sendBody(sql);
			if (orden.getVerboDeLaOrden() != null  && !orden.getEliminaOrden().equals("E")) {
				Emisora emisora = memoria.obtenerNotificacionDeEmisoraPorCartera(orden.getCarteraDePedidos());
				Cartera cartera = memoria.buscarCartera(orden.getCarteraDePedidos());
				if (orden.getVerboDeLaOrden().equals("B")) {
					Compra compra = memoria.buscarArregloCompra(cartera.getEmisora(), cartera.getCarteraDePedido());
					if (compra.getNiveles() != null) {
						List < Nivel > niveles = compra.getNiveles();
						Nivel nivel = new Nivel();
						String idPostura = orden.getEmisora() + orden.getCarteraDePedidos() + orden.getVerboDeLaOrden();
						Nivel precioABuscar = niveles.stream().filter(order->order.getPrecio().equals(
						orden.getPrecio())).findAny().orElse(null);
						Integer posicion = niveles.indexOf(precioABuscar);
						if (posicion >= 0) {
							if (orden.getCantidad() == mensajeE.getExecutedQuantity()) {
								if (niveles.get(niveles.indexOf(precioABuscar)).getNumeroDeOrdenes() == 1) {
									niveles.remove(niveles.indexOf(precioABuscar));
									memoria.eliminaOrdenE(mensajeE.getOrderNumber());
								} else {
									nivel = niveles.get(niveles.indexOf(precioABuscar));
									nivel.setPrecio(orden.getPrecio());
									nivel.setVolumen(nivel.getVolumen() - mensajeE.getExecutedQuantity());
									nivel.setNumeroDeOrdenes(nivel.getNumeroDeOrdenes() - 1);
									niveles.remove(niveles.indexOf(precioABuscar));
									niveles.add(posicion, nivel);
									memoria.eliminaOrdenE(mensajeE.getOrderNumber());

								}
							} else if (orden.getCantidad() > mensajeE.getExecutedQuantity()) {
								nivel = niveles.get(niveles.indexOf(precioABuscar));
								nivel.setPrecio(orden.getPrecio());
								nivel.setVolumen(nivel.getVolumen() - mensajeE.getExecutedQuantity());
								niveles.remove(niveles.indexOf(precioABuscar));
								niveles.add(posicion, nivel);
								orden.setCantidad(orden.getCantidad() - mensajeE.getExecutedQuantity());
								memoria.actualizarOrden(orden);

							}
							niveles.sort(Comparator.comparing(Nivel::getPrecio).reversed());
							compra.setNiveles(niveles);
							memoria.atualizarArregoCompra(idPostura, compra);
							Date sysdate = new Date();
			                emisora.setIssuer(cartera.getEmisora());
			                emisora.setSeries(cartera.getSerie());
			                emisora.setIsin(cartera.getIsin());
			                emisora.setInstrumentNumber(orden.getCarteraDePedidos());
			                emisora.setOpeningPrice(cartera.getPrecioDeApertura());
			                emisora.setPostura("compra");
							if (compra.getNiveles().size() > 0) {
								emisora.setBidVolume((compra.getNiveles().get(0).getVolumen()));
								emisora.setBestBid(compra.getNiveles().get(0).getPrecio());
								emisora.setBidTime(format.format(sysdate));
							} else {
								emisora.setBidVolume(0l);
								emisora.setBestBid(0.0);
								emisora.setBidTime(format.format(sysdate));
							}
							emisora.setPreviousSettlementPrice(emisora.getLastPrice());
							emisora.setLastPrice(orden.getPrecio());
							emisora.setLastVolume(mensajeE.getExecutedQuantity());
							emisora.setLastTime(mensajeE.getFechaHoraEmision());
							if (emisora.getMaxPriceOfTheDay() != null) {
								if (emisora.getMaxPriceOfTheDay() < orden.getPrecio()) {
									emisora.setMaxPriceOfTheDay(orden.getPrecio());
									emisora.setMaxTime(mensajeE.getFechaHoraEmision());

								}
							} else {
								emisora.setMaxPriceOfTheDay(orden.getPrecio());
								emisora.setMaxTime(mensajeE.getFechaHoraEmision());

							}

							if (emisora.getLowPriceOfTheDay() != null) {
								if (emisora.getLowPriceOfTheDay() > orden.getPrecio()) {
									emisora.setLowPriceOfTheDay(orden.getPrecio());
									emisora.setLowTime(mensajeE.getFechaHoraEmision());

								}
							} else {
								emisora.setLowPriceOfTheDay(orden.getPrecio());
								emisora.setLowTime(mensajeE.getFechaHoraEmision());

							}

							if (emisora.getVolumenAcumulado() != null) {
								emisora.setVolumenAcumulado(emisora.getVolumenAcumulado() + mensajeE.getExecutedQuantity());
							} else {
								emisora.setVolumenAcumulado(mensajeE.getExecutedQuantity());
							}
							if (emisora.getPrecioAcumulado() != null && emisora.getPrecioAcumulado() > 0) {

								Double monto = (orden.getPrecio() * mensajeE.getExecutedQuantity());
								emisora.setPrecioAcumulado(emisora.getPrecioAcumulado() + Double.valueOf(getDecimalPoint(
								monto.toString(), cartera.getNumeroDeDecimales())));

							} else {
								Double monto = (orden.getPrecio() * mensajeE.getExecutedQuantity());
								emisora.setPrecioAcumulado(Double.valueOf(getDecimalPoint(
								monto.toString(), cartera.getNumeroDeDecimales())));

							}
							Double average = emisora.getPrecioAcumulado() / emisora.getVolumenAcumulado();
							emisora.setAveragePriceDay(getDecimalPoint(
							average.toString(), cartera.getNumeroDeDecimales()));
							if (emisora.getTradedAmount() != null) {
								emisora.setTradedAmount(emisora.getTradedAmount() + (orden.getPrecio() * mensajeE.getExecutedQuantity()));
							} else {
								emisora.setTradedAmount((orden.getPrecio() * mensajeE.getExecutedQuantity()));
							}
							emisora.setTradedAmount(emisora.getPrecioAcumulado());
							emisora.setLastUpdateTime(format.format(sysdate));
							emisora.setLastPriceOfTheDay(orden.getPrecio());
							emisora.setTradedVolume(emisora.getVolumenAcumulado());
							memoria.actualizaEmisora(emisora);
							Hecho hecho = memoria.obtenerNotificacionDeHechoPorCartera(orden.getCarteraDePedidos());
			                hecho.setNumeroDeInstrumento(cartera.getCarteraDePedido());
			                hecho.setEmisora(cartera.getEmisora());
			                hecho.setSerie(cartera.getSerie());
			                hecho.setVolumen(mensajeE.getExecutedQuantity());
			                hecho.setPrecio(orden.getPrecio().toString());
			                hecho.setFolioDelHecho(mensajeE.getMatchNumber().toString());
							Character tipoDeConcertacion = mensajeE.getTradeIndicator();
							hecho.setHoraDelHecho(mensajeE.getFechaHoraEmision());
							if (tipoDeConcertacion.equals('U')) {
								tipoDeConcertacion = 'C';
							} else if (tipoDeConcertacion.equals('R')) {
								tipoDeConcertacion = 'O';
							}
							hecho.setTipoDeConcertacion(tipoDeConcertacion.toString());

							hecho.setFijaPrecio("1");
							hecho.setTipoDeOperacion("C");
							Double importe = mensajeE.getExecutedQuantity() * orden.getPrecio();
							hecho.setImporte(importe.toString());
							hecho.setCompra("BIVA");
							hecho.setVende("BIVA");
							hecho.setLiquidacion(4);
							hecho.setIndicadorDeSubasta("     ");
							hecho.setHoraDelHecho(mensajeE.getFechaHoraEmision());
							memoria.actualizaHecho(hecho);
							Profundidad profundidad = memoria.obtenerNotificacionDeProfundiodadPorCartera(orden.getCarteraDePedidos());
							int i = 0;
							List < mx.com.actinver.terminal.notificaciones.Nivel > nprof = new ArrayList < mx.com.actinver.terminal.notificaciones.Nivel > ();
							mx.com.actinver.terminal.notificaciones.Nivel prof = new mx.com.actinver.terminal.notificaciones.Nivel();
							for (Nivel nivel2: niveles) {
								prof = new mx.com.actinver.terminal.notificaciones.Nivel();
								try {
									prof.setPrecio(nivel2.getPrecio());
									prof.setVolumen(nivel2.getVolumen());
									prof.setOrdenes(nivel2.getNumeroDeOrdenes());
									nprof.add(prof);
									i++;
								} catch(Exception e) {
									prof.setPrecio(0.0);
									prof.setVolumen(0L);
									prof.setOrdenes(0);
									nprof.add(prof);
									i++;
								}
								if (i == 20) {
									break;
								}

							}
							profundidad.setProfundidad(nprof);
			                profundidad.setIssuer(cartera.getEmisora());
			                profundidad.setSerie(cartera.getSerie());
			                profundidad.setIsin(cartera.getIsin());
			                profundidad.setNumeroDeInstrumento(orden.getCarteraDePedidos());
			                profundidad.setLado(0);
			                profundidad.setNiveles(nprof.size());
							if (profundidad.getInstrumentStatus() != null) {
								profundidad.setInstrumentStatus("N");
							}
							memoria.actualizaProfundidad(profundidad);
 							feedUnico.sendBody(emisora);
							feedUnico.sendBody(profundidad);
							feedUnico.sendBody(hecho);
							feedUnico.sendBody(compra);
 							return true;
						}
						return true;
					} else {
						LOGGER.error("No se encuentra el precio de la orden en la lista de niveles");
						return false;
					}
				} else if (orden.getVerboDeLaOrden().equals("S")) {
					Venta venta = memoria.buscarArregloVenta(
					orden.getEmisora(), orden.getCarteraDePedidos().toString());
					if (venta.getNiveles() != null) {
						List < Nivel > niveles = venta.getNiveles();
						Nivel nivel = new Nivel();
						String idPostura = orden.getEmisora() + orden.getCarteraDePedidos() + orden.getVerboDeLaOrden();
						Nivel precioABuscar = niveles.stream().filter(order->order.getPrecio().equals(
						orden.getPrecio())).findAny().orElse(null);
						Integer posicion = niveles.indexOf(precioABuscar);
						if (posicion >= 0) {
							if (orden.getCantidad() == mensajeE.getExecutedQuantity()) {
								if (niveles.get(niveles.indexOf(precioABuscar)).getNumeroDeOrdenes() == 1) {
									niveles.remove(niveles.indexOf(precioABuscar));
									memoria.eliminaOrdenE(mensajeE.getOrderNumber());

								} else {
									nivel = niveles.get(niveles.indexOf(precioABuscar));
									nivel.setPrecio(orden.getPrecio());
									nivel.setVolumen(nivel.getVolumen() - mensajeE.getExecutedQuantity());
									nivel.setNumeroDeOrdenes(nivel.getNumeroDeOrdenes() - 1);
									niveles.remove(niveles.indexOf(precioABuscar));
									niveles.add(niveles.indexOf(precioABuscar), nivel);
									memoria.eliminaOrdenE(mensajeE.getOrderNumber());

								}
							} else if (orden.getCantidad() > mensajeE.getExecutedQuantity()) {
								nivel = niveles.get(niveles.indexOf(precioABuscar));
								nivel.setPrecio(orden.getPrecio());
								nivel.setVolumen(nivel.getVolumen() - mensajeE.getExecutedQuantity());
								niveles.remove(niveles.indexOf(precioABuscar));
								niveles.add(niveles.indexOf(precioABuscar), nivel);
								orden.setCantidad(orden.getCantidad() - mensajeE.getExecutedQuantity());
								memoria.actualizarOrden(orden);
							}
							niveles.sort(Comparator.comparing(Nivel::getPrecio));
							venta.setNiveles(niveles);
							memoria.atualizarArregoVenta(idPostura, venta);
							Date sysdate = new Date();
                            emisora.setIssuer(cartera.getEmisora());
                            emisora.setSeries(cartera.getSerie());
                            emisora.setIsin(cartera.getIsin());
                            emisora.setInstrumentNumber(orden.getCarteraDePedidos());
                            emisora.setOpeningPrice(cartera.getPrecioDeApertura());
                            emisora.setPostura("venta");
							if (venta.getNiveles().size() > 0) {
								emisora.setOfferVolume((venta.getNiveles().get(
								0).getVolumen()));
								emisora.setBestOffer(venta.getNiveles().get(0).getPrecio());
							} else {
								emisora.setOfferVolume(0l);
								emisora.setBestOffer(0.0);

							}

							emisora.setOfferTime(format.format(sysdate));
							emisora.setOpeningPrice(cartera.getPrecioDeApertura());
							emisora.setPreviousSettlementPrice(emisora.getLastPrice());

							emisora.setLastPrice(orden.getPrecio());
							emisora.setLastVolume(mensajeE.getExecutedQuantity());
							emisora.setLastTime(mensajeE.getFechaHoraEmision());

							if (emisora.getMaxPriceOfTheDay() != null) {
								if (emisora.getMaxPriceOfTheDay() < orden.getPrecio()) {
									emisora.setMaxPriceOfTheDay(orden.getPrecio());
									emisora.setMaxTime(mensajeE.getFechaHoraEmision());

								}
							} else {
								emisora.setMaxPriceOfTheDay(orden.getPrecio());

								emisora.setMaxTime(mensajeE.getFechaHoraEmision());

							}
							if (emisora.getLowPriceOfTheDay() != null) {
								if (emisora.getLowPriceOfTheDay() > orden.getPrecio()) {
									emisora.setLowPriceOfTheDay(orden.getPrecio());
									emisora.setLowTime(mensajeE.getFechaHoraEmision());

								}
							} else {
								emisora.setLowPriceOfTheDay(orden.getPrecio());
								emisora.setLowTime(mensajeE.getFechaHoraEmision());

							}

							emisora.setLastPrice(orden.getPrecio());
							if (emisora.getVolumenAcumulado() != null) {
								emisora.setVolumenAcumulado(emisora.getVolumenAcumulado() + mensajeE.getExecutedQuantity());
							} else {
								emisora.setVolumenAcumulado(mensajeE.getExecutedQuantity());
							}

							emisora.setLastUpdateTime(format.format(sysdate));
							emisora.setTradedVolume(emisora.getVolumenAcumulado());
							if (emisora.getPrecioAcumulado() != null && emisora.getPrecioAcumulado() > 0) {

								Double monto = (orden.getPrecio() * mensajeE.getExecutedQuantity());
								emisora.setPrecioAcumulado(emisora.getPrecioAcumulado() + Double.valueOf(getDecimalPoint(
								monto.toString(), cartera.getNumeroDeDecimales())));

							} else {
								Double monto = (orden.getPrecio() * mensajeE.getExecutedQuantity());
								emisora.setPrecioAcumulado(Double.valueOf(getDecimalPoint(
								monto.toString(), cartera.getNumeroDeDecimales())));

							}
							Double average = emisora.getPrecioAcumulado() / emisora.getVolumenAcumulado();
							emisora.setAveragePriceDay(getDecimalPoint(
							average.toString(), cartera.getNumeroDeDecimales()));

							if (emisora.getTradedAmount() != null) {
								Double monto = (orden.getPrecio() * mensajeE.getExecutedQuantity());
								emisora.setTradedAmount(emisora.getTradedAmount() + Double.valueOf(getDecimalPoint(
								monto.toString(), cartera.getNumeroDeDecimales())));
							} else {
								Double monto = (orden.getPrecio() * mensajeE.getExecutedQuantity());
								emisora.setTradedAmount(Double.valueOf(getDecimalPoint(
								monto.toString(), cartera.getNumeroDeDecimales())));
							}

							emisora.setTradedAmount(emisora.getPrecioAcumulado());
							emisora.setLastPriceOfTheDay(orden.getPrecio());
							memoria.actualizaEmisora(emisora);
							Hecho hecho = memoria.obtenerNotificacionDeHechoPorCartera(orden.getCarteraDePedidos());
							hecho.setNumeroDeInstrumento(orden.getCarteraDePedidos().toString());
							hecho.setEmisora(orden.getEmisora());
							hecho.setSerie(orden.getSerie());
							hecho.setVolumen(mensajeE.getExecutedQuantity());
							hecho.setPrecio(orden.getPrecio().toString());
							hecho.setFolioDelHecho(mensajeE.getMatchNumber().toString());
							Character tipoDeConcertacion = mensajeE.getTradeIndicator();
							hecho.setHoraDelHecho(mensajeE.getFechaHoraEmision());
							if (tipoDeConcertacion.equals('U')) {
								tipoDeConcertacion = 'C';
							} else if (tipoDeConcertacion.equals('R')) {
								tipoDeConcertacion = 'O';
							}
							hecho.setTipoDeConcertacion(tipoDeConcertacion.toString());

							hecho.setFijaPrecio("1");
							hecho.setTipoDeOperacion("C");
							Double importe = mensajeE.getExecutedQuantity() * orden.getPrecio();
							hecho.setImporte(importe.toString());
							hecho.setCompra("BIVA");
							hecho.setVende("BIVA");
							hecho.setLiquidacion(4);
							hecho.setIndicadorDeSubasta("     ");
							hecho.setHoraDelHecho(mensajeE.getFechaHoraEmision());
							memoria.actualizaHecho(hecho);
							Profundidad profundidad = memoria.obtenerNotificacionDeProfundiodadPorCartera(orden.getCarteraDePedidos());
							int i = 0;
							List < mx.com.actinver.terminal.notificaciones.Nivel > nprof = new ArrayList < mx.com.actinver.terminal.notificaciones.Nivel > ();
							mx.com.actinver.terminal.notificaciones.Nivel prof = new mx.com.actinver.terminal.notificaciones.Nivel();
							for (Nivel nivel2: niveles) {
								prof = new mx.com.actinver.terminal.notificaciones.Nivel();
								try {
									prof.setPrecio(nivel2.getPrecio());
									prof.setVolumen(nivel2.getVolumen());
									prof.setOrdenes(nivel2.getNumeroDeOrdenes());
									nprof.add(prof);
									i++;
								} catch(Exception e) {
									prof.setPrecio(0.0);
									prof.setVolumen(0L);
									prof.setOrdenes(0);
									nprof.add(prof);
									i++;
								}
								if (i == 20) {
									break;
								}

							}
							profundidad.setIssuer(cartera.getEmisora());
							profundidad.setSerie(cartera.getSerie());
							profundidad.setIsin(cartera.getIsin());
							profundidad.setNumeroDeInstrumento(orden.getCarteraDePedidos());
							profundidad.setProfundidad(nprof);
							profundidad.setNumeroDeInstrumento(orden.getCarteraDePedidos());
							profundidad.setLado(1);
							if (profundidad.getInstrumentStatus() != null) {
								profundidad.setInstrumentStatus("N");
							}
							profundidad.setNiveles(nprof.size());
							profundidad.setFechaActualizacion(format.format(sysdate));
							memoria.actualizaProfundidad(profundidad);
							feedUnico.sendBody(emisora);
							feedUnico.sendBody(profundidad);
							feedUnico.sendBody(hecho);
							feedUnico.sendBody(venta);
							return true;
						}
						return true;
					} else {
						LOGGER.error("No se encuentra el precio de la orden en la lista de niveles");
						return false;
					}
				}
				return true;
			} else {
				LOGGER.error("El verbo de la orden esta vacio");

				return false;
			}
		} else {
			LOGGER.error("No existe la orden a Ejecutar");
			return false;
		}

	}

	@Override
	public Boolean procesaMensajeC(C mensajeC) throws Exception {
		Orden orden = memoria.buscarOrden(mensajeC.getOrderNumber());
		if (orden != null) {
			String sql=registraEnUhecho(null, mensajeC, null);
			insert.sendBody(sql);
			Integer decimales = 0;
			Cartera cartera = memoria.buscarCartera(orden.getCarteraDePedidos());

			if (cartera.getNumeroDeDecimales() != null) {
				decimales = cartera.getNumeroDeDecimales();
			}
			if (orden.getVerboDeLaOrden() != null && !orden.getEliminaOrden().equals("E")) {
				Emisora emisora = memoria.obtenerNotificacionDeEmisoraPorCartera(orden.getCarteraDePedidos());
				if (orden.getVerboDeLaOrden().equals("B")) {
					Compra compra = memoria.buscarArregloCompra(orden.getEmisora(), orden.getCarteraDePedidos().toString());
					if (compra.getNiveles() != null) {
						List < Nivel > niveles = compra.getNiveles();
						Nivel nivel = new Nivel();
						String idPostura = orden.getEmisora() + orden.getCarteraDePedidos() + orden.getVerboDeLaOrden();
						Nivel precioABuscar = niveles.stream().filter(order->order.getPrecio().equals(
						orden.getPrecio())).findAny().orElse(null);
						Integer posicion = niveles.indexOf(precioABuscar);
						if (posicion >= 0) {
							if (orden.getCantidad() == mensajeC.getExecutedQuantity()) {
								if (niveles.get(niveles.indexOf(precioABuscar)).getNumeroDeOrdenes() == 1) {
									niveles.remove(niveles.indexOf(precioABuscar));
									memoria.eliminaOrdenE(mensajeC.getOrderNumber());

								} else {
									nivel = niveles.get(posicion);
									nivel.setPrecio(orden.getPrecio());
									nivel.setVolumen(nivel.getVolumen() - mensajeC.getExecutedQuantity());
									nivel.setNumeroDeOrdenes(nivel.getNumeroDeOrdenes() - 1);
									niveles.remove(niveles.indexOf(precioABuscar));
									niveles.add(posicion, nivel);
									memoria.eliminaOrdenE(mensajeC.getOrderNumber());

								}
							} else if (orden.getCantidad() > mensajeC.getExecutedQuantity()) {
								nivel = niveles.get(posicion);
								nivel.setPrecio(orden.getPrecio());
								nivel.setVolumen(nivel.getVolumen() - mensajeC.getExecutedQuantity());
								niveles.remove(niveles.indexOf(precioABuscar));
								niveles.add(posicion, nivel);

								orden.setCantidad(orden.getCantidad() - mensajeC.getExecutedQuantity());
								memoria.actualizarOrden(orden);
							}
							niveles.sort(Comparator.comparing(Nivel::getPrecio).reversed());
							compra.setNiveles(niveles);
							memoria.atualizarArregoCompra(idPostura, compra);
							Date sysdate = new Date();
							emisora.setIssuer(cartera.getEmisora());
							emisora.setSeries(cartera.getSerie());
							emisora.setIsin(cartera.getIsin());
							emisora.setInstrumentNumber(orden.getCarteraDePedidos());
							emisora.setOpeningPrice(cartera.getPrecioDeApertura());
							emisora.setPostura("compra");
							if (compra.getNiveles().size() > 0) {
								emisora.setBidVolume((compra.getNiveles().get(0).getVolumen()));
								emisora.setBestBid(compra.getNiveles().get(0).getPrecio());
								emisora.setBidTime(format.format(sysdate));
							} else {
								emisora.setBidVolume(0l);
								emisora.setBestBid(0.0);
								emisora.setBidTime(format.format(sysdate));
							}

							emisora.setBidTime(format.format(sysdate));
							emisora.setOpeningPrice(cartera.getPrecioDeApertura());

							Double precio = Double.valueOf(getDecimal(
							mensajeC.getExecutionPrice(), decimales));
							emisora.setPreviousSettlementPrice(emisora.getLastPrice());

							if (mensajeC.getPrintable() == 'Y') {

								emisora.setLastPrice(precio);
								emisora.setLastVolume(mensajeC.getExecutedQuantity());
								emisora.setLastTime(mensajeC.getFechaHoraEmision());
								if (emisora.getVolumenAcumulado() != null) {
									emisora.setVolumenAcumulado(emisora.getVolumenAcumulado() + mensajeC.getExecutedQuantity());
								} else {
									emisora.setVolumenAcumulado(mensajeC.getExecutedQuantity());
								}
								if (emisora.getPrecioAcumulado() != null && emisora.getPrecioAcumulado() > 0) {

									Long monto = (mensajeC.getExecutionPrice() * mensajeC.getExecutedQuantity());
									emisora.setPrecioAcumulado(emisora.getPrecioAcumulado() + Double.valueOf(getDecimalPoint(
									monto.toString(), cartera.getNumeroDeDecimales())));

								} else {
									Long monto = (mensajeC.getExecutionPrice() * mensajeC.getExecutedQuantity());
									emisora.setPrecioAcumulado(Double.valueOf(getDecimalPoint(monto.toString(), cartera.getNumeroDeDecimales())));

								}
								Double average = emisora.getPrecioAcumulado() / emisora.getVolumenAcumulado();
								emisora.setAveragePriceDay(getDecimalPoint(
								average.toString(), cartera.getNumeroDeDecimales()));

							}

							if (emisora.getMaxPriceOfTheDay() != null) {
								if (emisora.getMaxPriceOfTheDay() < precio) {
									emisora.setMaxPriceOfTheDay(precio);
									emisora.setMaxTime(mensajeC.getFechaHoraEmision());
								}
							} else {
								emisora.setMaxPriceOfTheDay(precio);
								emisora.setMaxTime(mensajeC.getFechaHoraEmision());
							}
							if (emisora.getLowPriceOfTheDay() != null) {
								if (emisora.getLowPriceOfTheDay() > precio) {
									emisora.setLowPriceOfTheDay(precio);
									emisora.setLowTime(mensajeC.getFechaHoraEmision());
								}
							} else {
								emisora.setLowPriceOfTheDay(precio);
								emisora.setLowTime(mensajeC.getFechaHoraEmision());
							}
							emisora.setLastPrice(orden.getPrecio());

							emisora.setLastUpdateTime(format.format(sysdate));
							emisora.setTradedVolume(emisora.getVolumenAcumulado());

							emisora.setPreviousSettlementPrice(emisora.getLastPrice());
							emisora.setLastPriceOfTheDay(Double.valueOf(getDecimal(
							mensajeC.getExecutionPrice(), cartera.getNumeroDeDecimales())));

							if (emisora.getTradedAmount() != null) {
								Long monto = (mensajeC.getExecutionPrice() * mensajeC.getExecutedQuantity());
								emisora.setTradedAmount(emisora.getTradedAmount() + Double.valueOf(getDecimalPoint(
								monto.toString(), cartera.getNumeroDeDecimales())));
							} else {
								Long monto = (mensajeC.getExecutionPrice() * mensajeC.getExecutedQuantity());
								emisora.setTradedAmount(Double.valueOf(getDecimalPoint(
								monto.toString(), cartera.getNumeroDeDecimales())));
							}

							emisora.setTradedAmount(emisora.getPrecioAcumulado());

							memoria.actualizaEmisora(emisora);
							Hecho hecho = memoria.obtenerNotificacionDeHechoPorCartera(orden.getCarteraDePedidos());
							hecho.setNumeroDeInstrumento(orden.getCarteraDePedidos().toString());
							hecho.setEmisora(orden.getEmisora());
							hecho.setSerie(orden.getSerie());
							hecho.setVolumen(mensajeC.getExecutedQuantity());
							hecho.setPrecio(precio.toString());
							hecho.setFolioDelHecho(mensajeC.getMatchNumber().toString());

							Character tipoDeConcertacion = mensajeC.getTradeIndicator();
							if (tipoDeConcertacion.equals('U')) {
								tipoDeConcertacion = 'C';
							} else if (tipoDeConcertacion.equals('R')) {
								tipoDeConcertacion = 'O';
							}
							hecho.setTipoDeConcertacion(tipoDeConcertacion.toString());

							hecho.setFijaPrecio("1");
							hecho.setTipoDeOperacion("C");
							Double importe = mensajeC.getExecutedQuantity() * precio;
							hecho.setImporte(importe.toString());
							hecho.setCompra("BIVA");
							hecho.setVende("BIVA");
							hecho.setLiquidacion(4);
							hecho.setIndicadorDeSubasta("     ");
							hecho.setHoraDelHecho(mensajeC.getFechaHoraEmision());
							memoria.actualizaHecho(hecho);
							Profundidad profundidad = memoria.obtenerNotificacionDeProfundiodadPorCartera(orden.getCarteraDePedidos());
							int i = 0;
							List < mx.com.actinver.terminal.notificaciones.Nivel > nprof = new ArrayList < mx.com.actinver.terminal.notificaciones.Nivel > ();
							mx.com.actinver.terminal.notificaciones.Nivel prof = new mx.com.actinver.terminal.notificaciones.Nivel();
							for (Nivel nivel2: niveles) {
								prof = new mx.com.actinver.terminal.notificaciones.Nivel();
								try {
									prof.setPrecio(nivel2.getPrecio());
									prof.setVolumen(nivel2.getVolumen());
									prof.setOrdenes(nivel2.getNumeroDeOrdenes());
									nprof.add(prof);
									i++;
								} catch(Exception e) {
									prof.setPrecio(0.0);
									prof.setVolumen(0L);
									prof.setOrdenes(0);
									nprof.add(prof);
									i++;
								}
								if (i == 20) {
									break;
								}

							}
							profundidad.setLado(0);
                            profundidad.setProfundidad(nprof);
                            profundidad.setIssuer(cartera.getEmisora());
                            profundidad.setSerie(cartera.getSerie());
                            profundidad.setIsin(cartera.getIsin());
							profundidad.setNumeroDeInstrumento(orden.getCarteraDePedidos());
							if (profundidad.getInstrumentStatus() != null) {
								profundidad.setInstrumentStatus("N");
							}
							profundidad.setNiveles(nprof.size());
							profundidad.setFechaActualizacion(format.format(sysdate));
							memoria.actualizaProfundidad(profundidad);
							feedUnico.sendBody(emisora);
							feedUnico.sendBody(profundidad);
							feedUnico.sendBody(hecho);
							feedUnico.sendBody(compra);
						}
						return true;
					} else {
						LOGGER.error("No se encuentra el precio de la orden en la lista de niveles");
						return false;
					}
				} else if (orden.getVerboDeLaOrden().equals("S")) {
					Venta venta = memoria.buscarArregloVenta(
					orden.getEmisora(), orden.getCarteraDePedidos().toString());
					if (venta.getNiveles() != null) {
						List < Nivel > niveles = venta.getNiveles();
						Nivel nivel = new Nivel();
						String idPostura = orden.getEmisora() + orden.getCarteraDePedidos() + orden.getVerboDeLaOrden();
						Nivel precioABuscar = niveles.stream().filter(order->order.getPrecio().equals(
						orden.getPrecio())).findAny().orElse(null);
						Integer posicion = niveles.indexOf(precioABuscar);
						if (posicion >= 0) {
							if (orden.getCantidad() == mensajeC.getExecutedQuantity()) {
								if (niveles.get(niveles.indexOf(precioABuscar)).getNumeroDeOrdenes() == 1) {
									niveles.remove(niveles.indexOf(precioABuscar));
									memoria.eliminaOrdenE(mensajeC.getOrderNumber());

								} else {
									nivel = niveles.get(posicion);
									nivel.setPrecio(orden.getPrecio());
									nivel.setVolumen(nivel.getVolumen() - mensajeC.getExecutedQuantity());
									nivel.setNumeroDeOrdenes(nivel.getNumeroDeOrdenes() - 1);
									niveles.remove(niveles.indexOf(precioABuscar));
									niveles.add(posicion, nivel);
									memoria.eliminaOrdenE(mensajeC.getOrderNumber());

								}
							} else if (orden.getCantidad() > mensajeC.getExecutedQuantity()) {
								nivel = niveles.get(posicion);
								nivel.setPrecio(orden.getPrecio());
								nivel.setVolumen(nivel.getVolumen() - mensajeC.getExecutedQuantity());
								niveles.remove(niveles.indexOf(precioABuscar));
								niveles.add(posicion, nivel);
								orden.setCantidad(orden.getCantidad() - mensajeC.getExecutedQuantity());
								memoria.actualizarOrden(orden);
							}
							niveles.sort(Comparator.comparing(Nivel::getPrecio));
							venta.setNiveles(niveles);
							memoria.atualizarArregoVenta(idPostura, venta);
							Date sysdate = new Date();
                            emisora.setIssuer(cartera.getEmisora());
                            emisora.setSeries(cartera.getSerie());
                            emisora.setIsin(cartera.getIsin());
                            emisora.setInstrumentNumber(orden.getCarteraDePedidos());
                            emisora.setOpeningPrice(cartera.getPrecioDeApertura());
                            emisora.setPostura("venta");
							if (venta.getNiveles().size() > 0) {
								emisora.setOfferVolume((venta.getNiveles().get(
								0).getVolumen()));
								emisora.setBestOffer(venta.getNiveles().get(0).getPrecio());
							} else {
								emisora.setOfferVolume(0l);
								emisora.setBestOffer(0.0);

							}

							emisora.setOfferTime(format.format(sysdate));
							emisora.setOpeningPrice(cartera.getPrecioDeApertura());
							emisora.setPreviousSettlementPrice(emisora.getLastPrice());

							Double precio = Double.valueOf(getDecimal(
							mensajeC.getExecutionPrice(), decimales));
							if (mensajeC.getPrintable() == 'Y') {
								emisora.setLastPrice(precio);
								emisora.setLastVolume(mensajeC.getExecutedQuantity());
								emisora.setLastTime(mensajeC.getFechaHoraEmision());
								if (emisora.getVolumenAcumulado() != null && emisora.getVolumenAcumulado() != 0) {
									emisora.setVolumenAcumulado(emisora.getVolumenAcumulado() + mensajeC.getExecutedQuantity());
								} else {
									emisora.setVolumenAcumulado(mensajeC.getExecutedQuantity());
								}

								if (emisora.getPrecioAcumulado() != null && emisora.getPrecioAcumulado() > 0) {

									Long monto = (mensajeC.getExecutionPrice() * mensajeC.getExecutedQuantity());
									emisora.setPrecioAcumulado(emisora.getPrecioAcumulado() + Double.valueOf(getDecimalPoint(
									monto.toString(), cartera.getNumeroDeDecimales())));

								} else {
									Long monto = (mensajeC.getExecutionPrice() * mensajeC.getExecutedQuantity());
									emisora.setPrecioAcumulado(Double.valueOf(getDecimalPoint(monto.toString(), cartera.getNumeroDeDecimales())));

								}
								Double average = emisora.getPrecioAcumulado() / emisora.getVolumenAcumulado();
								emisora.setAveragePriceDay(getDecimalPoint(
								average.toString(), cartera.getNumeroDeDecimales()));

							}

							if (emisora.getMaxPriceOfTheDay() != null && emisora.getMaxPriceOfTheDay() != 0) {
								if (emisora.getMaxPriceOfTheDay() < precio) {
									emisora.setMaxPriceOfTheDay(precio);
									emisora.setMaxTime(mensajeC.getFechaHoraEmision());
								}
							} else {
								emisora.setMaxPriceOfTheDay(precio);
								emisora.setMaxTime(mensajeC.getFechaHoraEmision());
							}
							if (emisora.getLowPriceOfTheDay() != null && emisora.getLowPriceOfTheDay() != 0) {
								if (emisora.getLowPriceOfTheDay() > precio) {
									emisora.setLowPriceOfTheDay(precio);
									emisora.setLowTime(mensajeC.getFechaHoraEmision());

								}
							} else {
								emisora.setLowPriceOfTheDay(precio);
								emisora.setLowTime(mensajeC.getFechaHoraEmision());
							}
							emisora.setLastPrice(orden.getPrecio());

							emisora.setLastUpdateTime(format.format(sysdate));
							emisora.setTradedVolume(emisora.getVolumenAcumulado());

							emisora.setPreviousSettlementPrice(emisora.getLastPrice());
							emisora.setLastPriceOfTheDay(Double.valueOf(getDecimal(
							mensajeC.getExecutionPrice(), cartera.getNumeroDeDecimales())));

							if (emisora.getTradedAmount() != null) {
								Long monto = (mensajeC.getExecutionPrice() * mensajeC.getExecutedQuantity());
								emisora.setTradedAmount(emisora.getTradedAmount() + Double.valueOf(getDecimalPoint(
								monto.toString(), cartera.getNumeroDeDecimales())));
							} else {
								Long monto = (mensajeC.getExecutionPrice() * mensajeC.getExecutedQuantity());
								emisora.setTradedAmount(Double.valueOf(getDecimalPoint(
								monto.toString(), cartera.getNumeroDeDecimales())));
							}
							emisora.setTradedAmount(emisora.getPrecioAcumulado());
							memoria.actualizaEmisora(emisora);
							Hecho hecho = memoria.obtenerNotificacionDeHechoPorCartera(orden.getCarteraDePedidos());
							hecho.setNumeroDeInstrumento(orden.getCarteraDePedidos().toString());
							hecho.setEmisora(orden.getEmisora());
							hecho.setSerie(orden.getSerie());
							hecho.setVolumen(mensajeC.getExecutedQuantity());
							hecho.setPrecio(precio.toString());
							hecho.setFolioDelHecho(mensajeC.getMatchNumber().toString());

							hecho.setHoraDelHecho(mensajeC.getFechaHoraEmision());
							Character tipoDeConcertacion = mensajeC.getTradeIndicator();
							if (tipoDeConcertacion.equals('U')) {
								tipoDeConcertacion = 'C';
							} else if (tipoDeConcertacion.equals('R')) {
								tipoDeConcertacion = 'O';
							}
							hecho.setTipoDeConcertacion(tipoDeConcertacion.toString());

							hecho.setFijaPrecio("1");
							hecho.setTipoDeOperacion("C");
							Double importe = mensajeC.getExecutedQuantity() * precio;
							hecho.setImporte(importe.toString());
							hecho.setCompra("BIVA");
							hecho.setVende("BIVA");
							hecho.setLiquidacion(4);
							hecho.setIndicadorDeSubasta("     ");
							hecho.setHoraDelHecho(mensajeC.getFechaHoraEmision());
							memoria.actualizaHecho(hecho);
							Profundidad profundidad = memoria.obtenerNotificacionDeProfundiodadPorCartera(orden.getCarteraDePedidos());
							int i = 0;
							List < mx.com.actinver.terminal.notificaciones.Nivel > nprof = new ArrayList < mx.com.actinver.terminal.notificaciones.Nivel > ();
							mx.com.actinver.terminal.notificaciones.Nivel prof = new mx.com.actinver.terminal.notificaciones.Nivel();
							for (Nivel nivel2: niveles) {
								prof = new mx.com.actinver.terminal.notificaciones.Nivel();
								try {
									prof.setPrecio(nivel2.getPrecio());
									prof.setVolumen(nivel2.getVolumen());
									prof.setOrdenes(nivel2.getNumeroDeOrdenes());
									nprof.add(prof);
									i++;
								} catch(Exception e) {
									prof.setPrecio(0.0);
									prof.setVolumen(0L);
									prof.setOrdenes(0);
									nprof.add(prof);
									i++;
								}
								if (i == 20) {
									break;
								}

							}
							profundidad.setProfundidad(nprof);
							profundidad.setIssuer(cartera.getEmisora());
							profundidad.setSerie(cartera.getSerie());
							profundidad.setIsin(cartera.getIsin());
							profundidad.setNumeroDeInstrumento(orden.getCarteraDePedidos());
							profundidad.setNiveles(nprof.size());
							profundidad.setFechaActualizacion(format.format(sysdate));
							profundidad.setLado(1);
							if (profundidad.getInstrumentStatus() != null) {
								profundidad.setInstrumentStatus("N");
							}
							memoria.actualizaProfundidad(profundidad);
							feedUnico.sendBody(emisora);
							feedUnico.sendBody(profundidad);
							feedUnico.sendBody(hecho);
							feedUnico.sendBody(venta);
							return true;
						}
						return true;
					} else {
						LOGGER.error("No se encuentra el precio de la orden en la lista de niveles");
						return false;
					}
				}
				return true;
			} else {
				LOGGER.error("El verbo de la orden esta vacio");

				return false;
			}
		} else {
			LOGGER.error("No existe la orden a Ejecutar");
			return false;
		}

	}

	@Override
	public Boolean procesaMensajeP(P mensajeP) throws Exception {
		if (memoria.buscarCartera(mensajeP.getOrderBook()).getNumeroDeDecimales() != null) {
			Hecho hecho = memoria.obtenerNotificacionDeHechoPorCartera(mensajeP.getOrderBook());
			Emisora emisora = memoria.obtenerNotificacionDeEmisoraPorCartera(mensajeP.getOrderBook());
			Cartera cartera = memoria.buscarCartera(mensajeP.getOrderBook());
			Double precio = Double.valueOf(getDecimal(
			mensajeP.getExecutionPrice(), cartera.getNumeroDeDecimales()));
			Character tipoDeConcertacion = mensajeP.getTradeIndicator();
			emisora.setIssuer(cartera.getEmisora());
			emisora.setSeries(cartera.getSerie());
			emisora.setIsin(cartera.getIsin());
			emisora.setInstrumentNumber(Integer.valueOf(cartera.getCarteraDePedido()));
			emisora.setOpeningPrice(cartera.getPrecioDeApertura());
			Date sysdate = new Date();
			emisora.setPreviousSettlementPrice(emisora.getLastPrice());

			if (mensajeP.getPrintable() == 'Y') {
				emisora.setLastPrice(precio);
				emisora.setLastVolume(mensajeP.getExecutedQuantity());
				emisora.setLastTime(mensajeP.getFechaHoraEmision());
				if (emisora.getVolumenAcumulado() != null) {
					emisora.setVolumenAcumulado(emisora.getVolumenAcumulado() + mensajeP.getExecutedQuantity());
				} else {
					emisora.setVolumenAcumulado(mensajeP.getExecutedQuantity());
				}
				if (emisora.getPrecioAcumulado() != null && emisora.getPrecioAcumulado() > 0) {

					Long monto = (mensajeP.getExecutionPrice() * mensajeP.getExecutedQuantity());
					emisora.setPrecioAcumulado(emisora.getPrecioAcumulado() + Double.valueOf(getDecimalPoint(monto.toString(), cartera.getNumeroDeDecimales())));

				} else {
					Long monto = (mensajeP.getExecutionPrice() * mensajeP.getExecutedQuantity());
					emisora.setPrecioAcumulado(Double.valueOf(getDecimalPoint(
					monto.toString(), cartera.getNumeroDeDecimales())));

				}
				Double average = emisora.getPrecioAcumulado() / emisora.getVolumenAcumulado();
				emisora.setAveragePriceDay(getDecimalPoint(average.toString(), cartera.getNumeroDeDecimales()));
			}

			if (emisora.getMaxPriceOfTheDay() != null) {
				if (emisora.getMaxPriceOfTheDay() < precio) {
					emisora.setMaxPriceOfTheDay(precio);
					emisora.setMaxTime(mensajeP.getFechaHoraEmision());

				}
			} else {
				emisora.setMaxPriceOfTheDay(precio);
				emisora.setMaxTime(mensajeP.getFechaHoraEmision());

			}
			if (emisora.getLowPriceOfTheDay() != null) {
				if (emisora.getLowPriceOfTheDay() > precio) {
					emisora.setLowPriceOfTheDay(precio);

					emisora.setLowTime(mensajeP.getFechaHoraEmision());
				}
			} else {
				emisora.setLowPriceOfTheDay(precio);
				emisora.setLowTime(mensajeP.getFechaHoraEmision());

			}
			emisora.setLastPrice(precio);

			emisora.setLastUpdateTime(format.format(sysdate));
			emisora.setTradedVolume(emisora.getVolumenAcumulado());

			emisora.setPreviousSettlementPrice(emisora.getLastPrice());
			emisora.setLastPriceOfTheDay(precio);
			if (emisora.getTradedAmount() != null) {
				Long monto = (mensajeP.getExecutionPrice() * mensajeP.getExecutedQuantity());
				emisora.setTradedAmount(emisora.getTradedAmount() + Double.valueOf(getDecimalPoint(monto.toString(), cartera.getNumeroDeDecimales())));
			} else {
				Long monto = (mensajeP.getExecutionPrice() * mensajeP.getExecutedQuantity());
				emisora.setTradedAmount(Double.valueOf(getDecimalPoint(
				monto.toString(), cartera.getNumeroDeDecimales())));
			}

			emisora.setTradedAmount(emisora.getPrecioAcumulado());
			memoria.actualizaEmisora(emisora);
			hecho.setHoraDelHecho(mensajeP.getFechaHoraEmision());
			hecho.setVolumen(mensajeP.getExecutedQuantity());
			hecho.setPrecio(precio.toString());
			hecho.setFolioDelHecho(mensajeP.getMatchNumber().toString());

			if (tipoDeConcertacion.equals('U')) {
				tipoDeConcertacion = 'C';
			} else if (tipoDeConcertacion.equals('R')) {
				tipoDeConcertacion = 'O';
			}
			hecho.setTipoDeConcertacion(tipoDeConcertacion.toString());
			hecho.setFolioDelHecho(String.valueOf(mensajeP.getTradeIndicator()));
			hecho.setFijaPrecio("1");
			hecho.setTipoDeOperacion("C");
			Double importe = mensajeP.getExecutedQuantity() * precio;
			hecho.setImporte(importe.toString());
			hecho.setCompra("BIVA");
			hecho.setVende("BIVA");
			hecho.setLiquidacion(4);
			hecho.setIndicadorDeSubasta("     ");
			hecho.setHoraDelHecho(mensajeP.getFechaHoraEmision());
			memoria.actualizaHecho(hecho);
			String sql=registraEnUhecho(null, null, mensajeP);
			insert.sendBody(sql);
			feedUnico.sendBody(emisora);
			feedUnico.sendBody(hecho);

			return true;
		} else {
			LOGGER.error("Mensaje Descartado No se encuentra la cartera de pedidos");
			return false;
		}
	}

	@Override
	public Boolean procesaMensajeR(R mensajeR) throws Exception {
		// baseDeDatos.registraEnGrupo(mensajeR);
		Hecho hecho = memoria.obtenerNotificacionDeHechoPorCartera(mensajeR.getOrderBook());
		Emisora emisora = memoria.obtenerNotificacionDeEmisoraPorCartera(mensajeR.getOrderBook());
		Profundidad profundidad = memoria.obtenerNotificacionDeProfundiodadPorCartera(mensajeR.getOrderBook());
		profundidad.setNumeroDeInstrumento(mensajeR.getOrderBook());
		profundidad.setIssuer(obtenerEmisora(mensajeR.getSecCode()));
		if (profundidad.getInstrumentStatus() != null) {
			if (profundidad.getInstrumentStatus().equals("T")) {
				profundidad.setInstrumentStatus(String.valueOf("N"));
			} else {
				profundidad.setInstrumentStatus(String.valueOf("V"));
			}
		}
		profundidad.setSerie(obtenerSerie(mensajeR.getSecCode()));
		profundidad.setIsin(mensajeR.getIsin());
		memoria.actualizaProfundidad(profundidad);
		hecho.setEmisora(obtenerEmisora(mensajeR.getSecCode()));
		hecho.setSerie(obtenerSerie(mensajeR.getSecCode()));
		hecho.setNumeroDeInstrumento(mensajeR.getOrderBook().toString());
		hecho.setHoraDelHecho(mensajeR.getFechaHoraEmision());
		memoria.actualizaHecho(hecho);
		emisora.setSeries(obtenerSerie(mensajeR.getSecCode()));
		emisora.setIssuer(obtenerEmisora(mensajeR.getSecCode()));
		emisora.setInstrumentNumber(mensajeR.getOrderBook());
		emisora.setIsin(mensajeR.getIsin());
		memoria.actualizaEmisora(emisora);
		Cartera cartera = memoria.buscarCartera(mensajeR.getOrderBook());
		cartera.setCarteraDePedido(mensajeR.getOrderBook().toString());
		cartera.setEmisora(obtenerEmisora(mensajeR.getSecCode()));
		cartera.setSerie(obtenerSerie(mensajeR.getSecCode()));
		cartera.setSec_code(mensajeR.getSecCode());
		cartera.setIsin(mensajeR.getIsin());
		cartera.setNumeroDeDecimales(mensajeR.getPriceDecimals());
		memoria.actualizaCartera(cartera);
		feedUnico.sendBody(mensajeR);
		feedUnico.sendBody(cartera);
		return true;
	}

	@Override
	public Boolean procesaMensajeH(H mensajeH) throws Exception {
		Emisora emisora = memoria.obtenerNotificacionDeEmisoraPorCartera(mensajeH.getOrderBook());
		char status = mensajeH.getTradingState();
		if (String.valueOf(status).equals("T")) {
			emisora.setInstrumentStatus("N");
		} else {
			emisora.setInstrumentStatus("V");
		}
		memoria.actualizaEmisora(emisora);
		Profundidad profundidad = memoria.obtenerNotificacionDeProfundiodadPorCartera(mensajeH.getOrderBook());
		if (String.valueOf(status).equals("T")) {
			profundidad.setInstrumentStatus(String.valueOf("N"));
		} else {
			profundidad.setInstrumentStatus(String.valueOf("V"));
		}
		memoria.actualizaProfundidad(profundidad);
		feedUnico.sendBody(mensajeH);
		feedUnico.sendBody(emisora);
		feedUnico.sendBody(profundidad);
		return true;
	}

	@Override
	public Boolean procesaMensajeL(L mensajeL) throws Exception {
		feedUnico.sendBody(mensajeL);
		return true;
	}

	@Override
	public Boolean procesaMensajeM(M mensajeM) throws Exception {
		feedUnico.sendBody(mensajeM);
		return true;
	}

	@Override
	public Boolean procesaMensajeX(X mensajeX) throws Exception {

		Cartera cartera = memoria.buscarCartera(mensajeX.getOrderBook());
		if (cartera.getEmisora() != null) {
			Emisora emisora = memoria.obtenerNotificacionDeEmisoraPorCartera(mensajeX.getOrderBook());
			emisora.setInstrumentNumber(mensajeX.getOrderBook());
			emisora.setOpeningPrice(Double.valueOf(getDecimal(
			mensajeX.getReferencePrice(), cartera.getNumeroDeDecimales())));
			cartera.setPrecioDeApertura(Double.valueOf(getDecimal(
			mensajeX.getReferencePrice(), cartera.getNumeroDeDecimales())));
			memoria.actualizaCartera(cartera);
			memoria.actualizaEmisora(emisora);
			feedUnico.sendBody(cartera);
			feedUnico.sendBody(emisora);

			return true;
		} else {
			LOGGER.error("No existe la emisora");
			return false;
		}

	}

	@Override
	public Boolean limpiadorNotificaciones() throws Exception {
		memoria.borrarNotificacionesDiarias();
		return null;
	}

	public String getDecimal(Integer valor, Integer priceDecimals) {
		switch (priceDecimals.intValue()) {
		case 1:
			return Float.valueOf(valor.floatValue() / 10.0F).toString();
		case 2:
			return Float.valueOf(valor.floatValue() / 100.0F).toString();
		case 3:
			return Float.valueOf(valor.floatValue() / 1000.0F).toString();
		case 4:
			return Float.valueOf(valor.floatValue() / 10000.0F).toString();
		case 5:
			return Float.valueOf(valor.floatValue() / 100000.0F).toString();
		case 6:
			return Float.valueOf(valor.floatValue() / 1000000.0F).toString();
		}
		return Float.valueOf(valor.floatValue()).toString();
	}

	public static String getDecimalPoint(String valor, Integer priceDecimals) {
		DecimalFormat formateador = new DecimalFormat("##############.##");
		switch (priceDecimals.intValue()) {
		case 1:
			formateador = new DecimalFormat("##############.0");
			return formateador.format(Double.valueOf(valor));
		case 2:
			formateador = new DecimalFormat("##############.00");

			return formateador.format(Double.valueOf(valor));
		case 3:
			formateador = new DecimalFormat("##############.000");

			return formateador.format(Double.valueOf(valor));
		case 4:
			formateador = new DecimalFormat("##############.0000");

			return formateador.format(Double.valueOf(valor));
		case 5:
			formateador = new DecimalFormat("##############.00000");

			return formateador.format(Double.valueOf(valor));
		case 6:
			formateador = new DecimalFormat("##############.0000000");

			return formateador.format(Double.valueOf(valor));
		}
		return valor;
	}
	
	  /*
	   * (non-Javadoc)
	   * 
	   * @see mx.com.actinver.biva.persistencia.PersistenciaBaseDeDatos#registraEnUhecho (mx
	   * .com.biva.model.E, mx.com.biva.model.C, mx.com.biva.model.P)
	   */
	  public String registraEnUhecho(E mensajeE, C mensajeC, P mensajeP) throws Exception {

	    Orden ord = new Orden();
	    String em = null;
	    if (mensajeE == null && mensajeC == null && mensajeP == null) {
	      throw new ErrorDeBaseDeDatos("ERROR");
	    } else {
	      StringBuilder miString = new StringBuilder();

	      miString.append(INSERT_ITCH_UHECHO);
	      if (mensajeE != null) {
	        em = mensajeE.getOrderNumber().toString();
	        ord = memoria.buscarOrden(mensajeE.getOrderNumber());

	        if (ord.getCarteraDePedidos() != null) {
	          miString.append("'" + ord.getCarteraDePedidos() + "'").append(",");
	        } else {
	          miString.append("0").append(",");
	        }
	        if (mensajeE.getMatchNumber() != null) {
	          miString.append("'" + mensajeE.getMatchNumber() + "'").append(",");
	        } else {
	          miString.append("' '").append(",");
	        }
	        if (mensajeE.getExecutedQuantity() != null) {
	          miString.append("'" + mensajeE.getExecutedQuantity() + "'").append(",");
	        } else {
	          miString.append("0").append(",");
	        }
	        if (ord.getPrecio() != null) {
	          miString.append("'" + ord.getPrecio() + "'").append(",");
	        } else {
	          miString.append("'0.0'").append(",");
	        }
	        if (mensajeE.getTradeIndicator() != null) {
	          miString.append("'" + mensajeE.getTradeIndicator() + "'").append(",");
	        } else {
	          miString.append("' '").append(",");
	        }
	      }
	      if (mensajeC != null) {
	        ord = memoria.buscarOrden(mensajeC.getOrderNumber());

	        if (ord.getCarteraDePedidos() != null) {
	          miString.append("'" + ord.getCarteraDePedidos() + "'").append(",");
	        } else {
	          miString.append("0").append(",");
	        }
	        if (mensajeC.getMatchNumber() != null) {
	          miString.append("'" + mensajeC.getMatchNumber() + "'").append(",");
	        } else {
	          miString.append("' '").append(",");
	        }
	        if (mensajeC.getExecutedQuantity() != null) {
	          miString.append("'" + mensajeC.getExecutedQuantity() + "'").append(",");
	        } else {
	          miString.append("0").append(",");
	        }
	        if (mensajeC.getExecutionPrice() != null) {
	          miString.append("'" + mensajeC.getExecutionPrice() + "'").append(",");
	        } else {
	          miString.append("'0.0'").append(",");
	        }
	        if (mensajeC.getTradeIndicator() != null) {
	          miString.append("'" + mensajeC.getTradeIndicator() + "'").append(",");
	        } else {
	          miString.append("' '").append(",");
	        }

	      }
	      if (mensajeP != null) {

	        if (mensajeP.getOrderBook() != null) {
	          miString.append("'" + mensajeP.getOrderBook() + "'").append(",");
	        } else {
	          miString.append("0").append(",");
	        }
	        if (mensajeP.getMatchNumber() != null) {
	          miString.append("'" + mensajeP.getMatchNumber() + "'").append(",");
	        } else {
	          miString.append("' '").append(",");
	        }
	        if (mensajeP.getExecutedQuantity() != null) {
	          miString.append("'" + mensajeP.getExecutedQuantity() + "'").append(",");
	        } else {
	          miString.append("0").append(",");
	        }
	        if (mensajeP.getExecutionPrice() != null) {
	          miString.append("'" + mensajeP.getExecutionPrice() + "'").append(",");
	        } else {
	          miString.append("'0.0'").append(",");
	        }
	        if (mensajeP.getTradeIndicator() != ' ') {
	          miString.append("'" + mensajeP.getTradeIndicator() + "'").append(",");
	        } else {
	          miString.append("' '").append(",");
	        }
	      }
	      miString.append("CURRENT_TIMESTAMP)");
	      return (miString.toString());

	    }

	  }

	public Memoria getMemoria() {
		return memoria;
	}

	public void setMemoria(Memoria memoria) {
		this.memoria = memoria;
	}

}