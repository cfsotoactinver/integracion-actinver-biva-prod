package mx.com.actinver.biva.memoria;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import mx.com.actinver.terminal.notificaciones.Emisora;
import mx.com.actinver.terminal.notificaciones.Hecho;
import mx.com.actinver.terminal.notificaciones.Profundidad;
import mx.com.biva.itch.modelo.D;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Almacen implements Memoria {

	@Produce(uri = "seda:datosGrid2")
	private ProducerTemplate actualizaGrid2;
	private ConcurrentHashMap<String, Compra> registrosDeCompras;
	private ConcurrentHashMap<String, Venta> registrosDeVentas;
	private ConcurrentHashMap<String, Orden> registrosDeOrdenes;
	private ConcurrentHashMap<String, Cartera> registroDeCarteras;
	private ConcurrentHashMap<String, Emisora> notificacionesDeEmisoras;
	private ConcurrentHashMap<String, Hecho> notificacionesDeHechos;
	private ConcurrentHashMap<String, Profundidad> notificacionesDeProfundidad;
	private static final Logger LOGGER = LoggerFactory.getLogger(Almacen.class);
	private static final String MSG_MEM001 = "Inicializando la memoria, sincronizando con el grid de datos";
	private static final String ERROR_MEM001 = "Error al intentar sincronizar la memoria local con el grid de datos.";

	public void inicializar() {
		try {
			LOGGER.info("Inicializando la memoria, sincronizando con el grid de datos");
			registrosDeCompras = new ConcurrentHashMap<String, Compra>();
			registrosDeVentas = new ConcurrentHashMap<String, Venta>();
			registrosDeOrdenes = new ConcurrentHashMap<String, Orden>();
			registroDeCarteras = new ConcurrentHashMap<String, Cartera>();
			notificacionesDeHechos = new ConcurrentHashMap<String, Hecho>();
			notificacionesDeEmisoras = new ConcurrentHashMap<String, Emisora>();
			notificacionesDeProfundidad = new ConcurrentHashMap<String, Profundidad>();
		} catch (Exception e) {
			LOGGER.error(
					"Error al intentar sincronizar la memoria local con el grid de datos.",
					e);
			registrosDeCompras = new ConcurrentHashMap<String, Compra>();
			registrosDeVentas = new ConcurrentHashMap<String, Venta>();
			registrosDeOrdenes = new ConcurrentHashMap<String, Orden>();
			registroDeCarteras = new ConcurrentHashMap<String, Cartera>();
			notificacionesDeHechos = new ConcurrentHashMap<String, Hecho>();
			notificacionesDeEmisoras = new ConcurrentHashMap<String, Emisora>();
			notificacionesDeProfundidad = new ConcurrentHashMap<String, Profundidad>();
		}
	}

	public Double obtenerMejorRegistroDeVentas(String emisora, String orderBook)
			throws Exception {
		Venta vent = (Venta) registrosDeVentas.get(emisora + orderBook);
		double bestOffer = 0.0D;
		Integer localInteger1;
		Integer localInteger2;
		for (Integer i = Integer.valueOf(0); i.intValue() < vent.getNiveles()
				.size(); localInteger2 = i = Integer.valueOf(i.intValue() + 1)) {
			if (((Nivel) vent.getNiveles().get(i.intValue())).getPrecio()
					.doubleValue() > bestOffer) {
				bestOffer = ((Nivel) vent.getNiveles().get(i.intValue()))
						.getPrecio().doubleValue();
			}
			localInteger1 = i;
		}
		return Double.valueOf(bestOffer);
	}

	public Double obtenerMejorRegistroDeVentasPosicion(String emisora,
			String orderBook) throws Exception {
		Venta vent = (Venta) registrosDeVentas.get(emisora + orderBook);
		double bestOffer = ((Nivel) vent.getNiveles().get(0)).getPrecio()
				.doubleValue();
		return Double.valueOf(bestOffer);
	}

	public Double obtenerMejorRegistroDeCompraPorVolumen(String emisora,
			String orderBook) throws Exception {
		Compra comp = (Compra) registrosDeCompras.get(emisora + orderBook);
		double maxvol = 0.0D;
		Integer localInteger1;
		Integer localInteger2;
		for (Integer i = Integer.valueOf(0); i.intValue() < comp.getNiveles()
				.size(); localInteger2 = i = Integer.valueOf(i.intValue() + 1)) {
			if (((Nivel) comp.getNiveles().get(i.intValue())).getVolumen()
					.longValue() > maxvol) {
				maxvol = ((Nivel) comp.getNiveles().get(i.intValue()))
						.getVolumen().longValue();
			}
			localInteger1 = i;
		}
		return Double.valueOf(maxvol);
	}

	public Double obtenerMejorRegistroDeCompraPorVolumenPosicion(
			String emisora, String orderBook) throws Exception {
		Compra comp = (Compra) registrosDeCompras.get(emisora + orderBook);
		double maxvol = ((Nivel) comp.getNiveles().get(0)).getVolumen()
				.longValue();
		return Double.valueOf(maxvol);
	}

	public Orden obtenerOrden(String emisora, String serie,
			String vervoDeLaOrden, Integer numeroDeOrden) throws Exception {
		return null;
	}

	public Emisora obtenerNotificacionDeEmisora(String emisora, String serie)
			throws Exception {
		return null;
	}

	public Profundidad obtenerNotificacionDeProfundiodad(String emisora,
			String serie) throws Exception {
		return null;
	}

	public Hecho obtenerNotificacionDeHecho(String emisora, String serie)
			throws Exception {
		return null;
	}

	public void registrarOrdern(Orden orden) throws Exception {
		registrosDeOrdenes.put(orden.getNumeroDeOrden().toString(), orden);
	}

	public Compra buscarArregloCompra(String emisora, String carteraDePedidos)
			throws Exception {
		if (registrosDeCompras.get(emisora + carteraDePedidos + "B") == null) {
			registrosDeCompras.put(emisora + carteraDePedidos + "B",
					new Compra());
			return new Compra();
		}
		return (Compra) registrosDeCompras
				.get(emisora + carteraDePedidos + "B");
	}

	public Venta buscarArregloVenta(String emisora, String carteraDePedidos)
			throws Exception {
		if (registrosDeVentas.get(emisora + carteraDePedidos + "S") == null) {
			registrosDeVentas
					.put(emisora + carteraDePedidos + "S", new Venta());
			return new Venta();
		}
		return registrosDeVentas.get(emisora + carteraDePedidos + "S");
	}

	public void atualizarArregoCompra(String idPostura, Compra compra)
			throws Exception {
		if (registrosDeCompras.get(idPostura) != null) {
			registrosDeCompras.remove(idPostura);
			registrosDeCompras.put(idPostura, compra);
		} else {
			registrosDeCompras.put(idPostura, compra);
		}
	}

	public void atualizarArregoVenta(String idPostura, Venta venta)
			throws Exception {
		if (registrosDeVentas.get(idPostura) != null) {
			registrosDeVentas.remove(idPostura);
			registrosDeVentas.put(idPostura, venta);
		} else {
			registrosDeVentas.put(idPostura, venta);
		}
	}

	public void eliminarOrdenD(D mensajeD) throws Exception {
		if (registrosDeOrdenes.get(mensajeD.getOrderNumber().toString()) != null) {
			registrosDeOrdenes.remove(mensajeD.getOrderNumber().toString());
		}
	}

	public Orden buscarOrdenA(Long orderNumber) throws Exception {
		if (registrosDeOrdenes.get(orderNumber.toString()) == null) {
			registrosDeOrdenes.put(orderNumber.toString(), new Orden());
			return new Orden();
		}
		return (Orden) registrosDeOrdenes.get(orderNumber.toString());
	}

	public void actualizarOrden(Orden orden) throws Exception {
		if (registrosDeOrdenes.get(orden.getNumeroDeOrden().toString()) == null) {
			registrosDeOrdenes.remove(orden.getNumeroDeOrden().toString());
			registrosDeOrdenes.put(orden.getNumeroDeOrden().toString(), orden);
		} else {
			registrosDeOrdenes.put(orden.getNumeroDeOrden().toString(), orden);
		}
	}

	public Cartera buscarCartera(Integer orderBook) throws Exception {
		if (registroDeCarteras.get(orderBook.toString()) == null) {
			registroDeCarteras.put(orderBook.toString(), new Cartera());
			return new Cartera();
		}
		Cartera cartera = (Cartera) registroDeCarteras
				.get(orderBook.toString());

		return cartera;
	}

	public void actualizaCartera(Cartera cartera) throws Exception {
		if (registroDeCarteras.get(cartera.getCarteraDePedido().toString()) == null) {
			registroDeCarteras.remove(cartera.getCarteraDePedido().toString());
			registroDeCarteras.put(cartera.getCarteraDePedido().toString(),
					cartera);
		} else {
			registroDeCarteras.put(cartera.getCarteraDePedido().toString(),
					cartera);
		}
	}

	public Orden buscarOrden(Long orderNumber) throws Exception {
		if (registrosDeOrdenes.get(orderNumber.toString()) == null) {
			return null;
		}
		return (Orden) registrosDeOrdenes.get(orderNumber.toString());
	}

	public void eliminaOrdenU(Long originalOrderNumber) {
		if (registrosDeOrdenes.get(originalOrderNumber.toString()) != null) {
			registrosDeOrdenes.remove(originalOrderNumber.toString());
		}
	}

	public Emisora obtenerNotificacionDeEmisoraPorCartera(Integer orderBook) {
		if (notificacionesDeEmisoras.get("Emisora" + orderBook.toString()) == null) {
			notificacionesDeEmisoras.put("Emisora" + orderBook.toString(),
					new Emisora());
			return new Emisora();
		}
		Emisora emisora = (Emisora) notificacionesDeEmisoras.get("Emisora"
				+ orderBook.toString());
		return emisora;
	}

	public Profundidad obtenerNotificacionDeProfundiodadPorCartera(
			Integer orderBook) {
		if (notificacionesDeProfundidad.get("Profundidad"
				+ orderBook.toString()) == null) {
			notificacionesDeProfundidad.put(
					"Profundidad" + orderBook.toString(), new Profundidad());
			return new Profundidad();
		}
		Profundidad profundidad = (Profundidad) notificacionesDeProfundidad
				.get("Profundidad" + orderBook.toString());
		return profundidad;
	}

	public Hecho obtenerNotificacionDeHechoPorCartera(Integer orderBook) {
		if (notificacionesDeHechos.get("Hecho" + orderBook.toString()) == null) {
			notificacionesDeHechos.put("Hecho" + orderBook.toString(),
					new Hecho());
			return new Hecho();
		}
		Hecho hecho = (Hecho) notificacionesDeHechos.get("Hecho"
				+ orderBook.toString());
		return hecho;
	}

	public Compra obtenerArregloCompra(String emisora, String orderBook) {
		Compra comp = (Compra) registrosDeCompras.get(emisora + orderBook);
		return comp;
	}

	public Venta obtenerArregloVenta(String emisora, String orderBook) {
		Venta vent = (Venta) registrosDeVentas.get(emisora + orderBook);
		return vent;
	}

	public Orden obtenerArregloOrden(String orderNumber) {
		Orden ord = (Orden) registrosDeOrdenes.get(orderNumber);
		return ord;
	}

	public void actualizaEmisora(Emisora emisora) {
		if (notificacionesDeEmisoras.get("Emisora"
				+ emisora.getInstrumentNumber().toString()) != null) {
			notificacionesDeEmisoras.remove("Emisora"
					+ emisora.getInstrumentNumber().toString());
			notificacionesDeEmisoras.put("Emisora"
					+ emisora.getInstrumentNumber().toString(), emisora);
		} else {
			notificacionesDeEmisoras.put("Emisora"
					+ emisora.getInstrumentNumber().toString(), emisora);
		}
	}

	public void actualizaHecho(Hecho hecho) {
		if (notificacionesDeHechos
				.get("Hecho" + hecho.getNumeroDeInstrumento()) != null) {
			notificacionesDeHechos.remove("Hecho"
					+ hecho.getNumeroDeInstrumento());
			notificacionesDeHechos.put(
					"Hecho" + hecho.getNumeroDeInstrumento(), hecho);
		} else {
			notificacionesDeHechos.put(
					"Hecho" + hecho.getNumeroDeInstrumento(), hecho);
		}
	}

	public void actualizaProfundidad(Profundidad profundidad) {
		if (notificacionesDeProfundidad.get("Profundidad"
				+ profundidad.getNumeroDeInstrumento().toString()) != null) {
			notificacionesDeProfundidad.remove("Profundidad"
					+ profundidad.getNumeroDeInstrumento().toString());
			notificacionesDeProfundidad.put("Profundidad"
					+ profundidad.getNumeroDeInstrumento().toString(),
					profundidad);
		} else {
			notificacionesDeProfundidad.put("Profundidad"
					+ profundidad.getNumeroDeInstrumento().toString(),
					profundidad);
		}
	}

	public void eliminaOrdenE(Long orderNumber) throws Exception {
		Orden orden = buscarOrden(orderNumber);
		registrosDeOrdenes.remove(orderNumber.toString());
		orden.setEliminaOrden("E");
		registrosDeOrdenes.put(orderNumber.toString(), orden);
	}

	public void agregarRegistroEmisora(Emisora emisora) {
		notificacionesDeEmisoras.put("Emisora"
				+ emisora.getInstrumentNumber().toString(), emisora);
	}

	public void agregarRegistroProfundidad(Profundidad profundidad) {
		notificacionesDeProfundidad.put("Profundidad"
				+ profundidad.getNumeroDeInstrumento().toString(), profundidad);
	}

	public void agregarRegistroHecho(Hecho hecho) {
		notificacionesDeHechos.put("Hecho" + hecho.getNumeroDeInstrumento(),
				hecho);
	}

	public void agregarRegistroCompra(Compra compra) {
		registrosDeCompras.put(compra.getIdcompra(), compra);
	}

	public void agregarRegistroVenta(Venta venta) {
		registrosDeVentas.put(venta.getIdventa(), venta);
	}

	public void agregarRegistroCartera(Cartera cartera) {
		registroDeCarteras.put(cartera.getCarteraDePedido(), cartera);
	}

	public void agregarRegistroOrden(Orden orden) {
		registrosDeOrdenes.put(orden.getNumeroDeOrden().toString(), orden);
	}

	public void borrarNotificacionesDiarias() throws Exception {
		actualizaGrid2.sendBody("reiniciar");
		for (Object key : notificacionesDeEmisoras.keySet()) {
			if (((Emisora) notificacionesDeEmisoras.get(key)).getClass()
					.toString().contains("Emisora")) {
				Emisora emisora = new Emisora();
				Emisora emisoraLimpia = new Emisora();
				emisora = (Emisora) notificacionesDeEmisoras.get(key);
				notificacionesDeEmisoras.remove(key);
				emisoraLimpia
						.setInstrumentNumber(emisora.getInstrumentNumber());
				actualizaGrid2.sendBody(emisoraLimpia);
			}
		}
		for (Object key : notificacionesDeHechos.keySet()) {
			if (((Hecho) notificacionesDeHechos.get(key)).getClass().toString()
					.contains("Hecho")) {
				Hecho hecho = new Hecho();
				Hecho hecho2 = new Hecho();
				hecho = (Hecho) notificacionesDeHechos.get(key);
				notificacionesDeHechos.remove(key);
				hecho2.setNumeroDeInstrumento(hecho.getNumeroDeInstrumento());
				actualizaGrid2.sendBody(hecho2);
			}
		}
		for (Object key : notificacionesDeProfundidad.keySet()) {
			if (((Profundidad) notificacionesDeProfundidad.get(key)).getClass()
					.toString().contains("Profundidad")) {
				Profundidad profundidad = new Profundidad();
				Profundidad profundidad2 = new Profundidad();
				profundidad = (Profundidad) notificacionesDeProfundidad
						.get(key);
				notificacionesDeProfundidad.remove(key);
				profundidad2.setNumeroDeInstrumento(profundidad
						.getNumeroDeInstrumento());
				actualizaGrid2.sendBody(profundidad2);
			}
		}

		for (Object key : registrosDeCompras.keySet()) {
			if (((Compra) registrosDeCompras.get(key)).getClass().toString()
					.contains("Compra")) {
				Compra compra = new Compra();
				Compra compra2 = new Compra();
				compra = (Compra) registrosDeCompras.get(key);
				registrosDeCompras.remove(key);
				compra2.setIdcompra(compra.getIdcompra());
				actualizaGrid2.sendBody(compra2);
			}
		}

		for (Object key : registrosDeVentas.keySet()) {
			if (((Venta) registrosDeVentas.get(key)).getClass().toString()
					.contains("Venta")) {
				Venta venta = new Venta();
				Venta venta2 = new Venta();
				venta = (Venta) registrosDeVentas.get(key);
				registrosDeVentas.remove(key);
				venta2.setIdventa(venta.getIdventa());
				actualizaGrid2.sendBody(venta2);
			}
		}

		for (Object key : registrosDeOrdenes.keySet()) {
			if (((Orden) registrosDeOrdenes.get(key)).getClass().toString()
					.contains("Orden")) {
				Orden orden = new Orden();
				Orden orden2 = new Orden();
				orden = (Orden) registrosDeOrdenes.get(key);
				registrosDeOrdenes.remove(key);
				orden2.setNumeroDeOrden(orden.getNumeroDeOrden());
				actualizaGrid2.sendBody(orden2);
			}
		}
		for (Object key : registroDeCarteras.keySet()) {
			if (((Cartera) registroDeCarteras.get(key)).getClass().toString()
					.contains("Orden")) {
				Cartera cartera = new Cartera();
				cartera = (Cartera) registroDeCarteras.get(key);
				actualizaGrid2.sendBody(cartera);
			}
		}
	}
}
