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

public class Almacen
  implements Memoria
{

  @Produce(uri="seda:datosGrid2")
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
  
  public void inicializar()
  {
    try
    {
      LOGGER.info("Inicializando la memoria, sincronizando con el grid de datos");
      this.registrosDeCompras = new ConcurrentHashMap();
      this.registrosDeVentas = new ConcurrentHashMap();
      this.registrosDeOrdenes = new ConcurrentHashMap();
      this.registroDeCarteras = new ConcurrentHashMap();
      this.notificacionesDeHechos = new ConcurrentHashMap();
      this.notificacionesDeEmisoras = new ConcurrentHashMap();
      this.notificacionesDeProfundidad = new ConcurrentHashMap();
    }
    catch (Exception e)
    {
      LOGGER.error("Error al intentar sincronizar la memoria local con el grid de datos.", e);
      this.registrosDeCompras = new ConcurrentHashMap();
      this.registrosDeVentas = new ConcurrentHashMap();
      this.registrosDeOrdenes = new ConcurrentHashMap();
      this.registroDeCarteras = new ConcurrentHashMap();
      this.notificacionesDeEmisoras = new ConcurrentHashMap();
      this.notificacionesDeHechos = new ConcurrentHashMap();
      this.notificacionesDeProfundidad = new ConcurrentHashMap();
    }
  }
  
  public Double obtenerMejorRegistroDeVentas(String emisora, String orderBook)
    throws Exception
  {
    Venta vent = (Venta)this.registrosDeVentas.get(emisora + orderBook);
    double bestOffer = 0.0D;
    Integer localInteger1;
    Integer localInteger2;
    for (Integer i = Integer.valueOf(0); i.intValue() < vent.getNiveles().size(); localInteger2 = i = Integer.valueOf(i.intValue() + 1))
    {
      if (((Nivel)vent.getNiveles().get(i.intValue())).getPrecio().doubleValue() > bestOffer) {
        bestOffer = ((Nivel)vent.getNiveles().get(i.intValue())).getPrecio().doubleValue();
      }
      localInteger1 = i;
    }
    return Double.valueOf(bestOffer);
  }
  
  public Double obtenerMejorRegistroDeVentasPosicion(String emisora, String orderBook)
    throws Exception
  {
    Venta vent = (Venta)this.registrosDeVentas.get(emisora + orderBook);
    double bestOffer = ((Nivel)vent.getNiveles().get(0)).getPrecio().doubleValue();
    return Double.valueOf(bestOffer);
  }
  
  public Double obtenerMejorRegistroDeCompraPorVolumen(String emisora, String orderBook)
    throws Exception
  {
    Compra comp = (Compra)this.registrosDeCompras.get(emisora + orderBook);
    double maxvol = 0.0D;
    Integer localInteger1;
    Integer localInteger2;
    for (Integer i = Integer.valueOf(0); i.intValue() < comp.getNiveles().size(); localInteger2 = i = Integer.valueOf(i.intValue() + 1))
    {
      if (((Nivel)comp.getNiveles().get(i.intValue())).getVolumen().longValue() > maxvol) {
        maxvol = ((Nivel)comp.getNiveles().get(i.intValue())).getVolumen().longValue();
      }
      localInteger1 = i;
    }
    return Double.valueOf(maxvol);
  }
  
  public Double obtenerMejorRegistroDeCompraPorVolumenPosicion(String emisora, String orderBook)
    throws Exception
  {
    Compra comp = (Compra)this.registrosDeCompras.get(emisora + orderBook);
    double maxvol = ((Nivel)comp.getNiveles().get(0)).getVolumen().longValue();
    return Double.valueOf(maxvol);
  }
  
  public Orden obtenerOrden(String emisora, String serie, String vervoDeLaOrden, Integer numeroDeOrden)
    throws Exception
  {
    return null;
  }
  
  public Emisora obtenerNotificacionDeEmisora(String emisora, String serie)
    throws Exception
  {
    return null;
  }
  
  public Profundidad obtenerNotificacionDeProfundiodad(String emisora, String serie)
    throws Exception
  {
    return null;
  }
  
  public Hecho obtenerNotificacionDeHecho(String emisora, String serie)
    throws Exception
  {
    return null;
  }
  
  public void registrarOrdern(Orden orden)
    throws Exception
  {
    this.registrosDeOrdenes.put(orden.getNumeroDeOrden().toString(), orden);
  }
  
  public Compra buscarArregloCompra(String emisora, String carteraDePedidos)
    throws Exception
  {
    if (this.registrosDeCompras.get(emisora + carteraDePedidos + "B") == null)
    {
      this.registrosDeCompras.put(emisora + carteraDePedidos + "B", new Compra());
      return new Compra();
    }
    return (Compra)this.registrosDeCompras.get(emisora + carteraDePedidos + "B");
  }
  
  public Venta buscarArregloVenta(String emisora, String carteraDePedidos)
    throws Exception
  {
    if (this.registrosDeVentas.get(emisora + carteraDePedidos + "S") == null)
    {
      this.registrosDeVentas.put(emisora + carteraDePedidos + "S", new Venta());
      return new Venta();
    }
    return (Venta)this.registrosDeVentas.get(emisora + carteraDePedidos + "S");
  }
  
  public void atualizarArregoCompra(String idPostura, Compra compra)
    throws Exception
  {
    if (this.registrosDeCompras.get(idPostura) != null)
    {
      this.registrosDeCompras.remove(idPostura);
      this.registrosDeCompras.put(idPostura, compra);
    }
    else
    {
      this.registrosDeCompras.put(idPostura, compra);
    }
  }
  
  public void atualizarArregoVenta(String idPostura, Venta venta)
    throws Exception
  {
    if (this.registrosDeVentas.get(idPostura) != null)
    {
      this.registrosDeVentas.remove(idPostura);
      this.registrosDeVentas.put(idPostura, venta);
    }
    else
    {
      this.registrosDeVentas.put(idPostura, venta);
    }
  }
  
  public void eliminarOrdenD(D mensajeD)
    throws Exception
  {
    if (this.registrosDeOrdenes.get(mensajeD.getOrderNumber().toString()) != null) {
      this.registrosDeOrdenes.remove(mensajeD.getOrderNumber().toString());
    }
  }
  
  public Orden buscarOrdenA(Long orderNumber)
    throws Exception
  {
    if (this.registrosDeOrdenes.get(orderNumber.toString()) == null)
    {
      this.registrosDeOrdenes.put(orderNumber.toString(), new Orden());
      return new Orden();
    }
    return (Orden)this.registrosDeOrdenes.get(orderNumber.toString());
  }
  
  public void actualizarOrden(Orden orden)
    throws Exception
  {
    if (this.registrosDeOrdenes.get(orden.getNumeroDeOrden().toString()) == null)
    {
      this.registrosDeOrdenes.remove(orden.getNumeroDeOrden());
      this.registrosDeOrdenes.put(orden.getNumeroDeOrden().toString(), orden);
    }
    else
    {
      this.registrosDeOrdenes.put(orden.getNumeroDeOrden().toString(), orden);
    }
  }
  
  public Cartera buscarCartera(Integer orderBook)
    throws Exception
  {
    if (this.registroDeCarteras.get(orderBook.toString()) == null)
    {
      this.registroDeCarteras.put(orderBook.toString(), new Cartera());
      return new Cartera();
    }
    Cartera cartera = (Cartera)this.registroDeCarteras.get(orderBook.toString());
    
    return cartera;
  }
  
  public void actualizaCartera(Cartera cartera)
    throws Exception
  {
    if (this.registroDeCarteras.get(cartera.getCarteraDePedido().toString()) == null)
    {
      this.registroDeCarteras.remove(cartera.getCarteraDePedido().toString());
      this.registroDeCarteras.put(cartera.getCarteraDePedido().toString(), cartera);
    }
    else
    {
      this.registroDeCarteras.put(cartera.getCarteraDePedido().toString(), cartera);
    }
  }
  
  public Orden buscarOrden(Long orderNumber)
    throws Exception
  {
    if (this.registrosDeOrdenes.get(orderNumber.toString()) == null) {
      return null;
    }
    return (Orden)this.registrosDeOrdenes.get(orderNumber.toString());
  }
  
  public void eliminaOrdenU(Long originalOrderNumber)
  {
    if (this.registrosDeOrdenes.get(originalOrderNumber.toString()) != null) {
      this.registrosDeOrdenes.remove(originalOrderNumber.toString());
    }
  }
  
  public Emisora obtenerNotificacionDeEmisoraPorCartera(Integer orderBook)
  {
    if (this.notificacionesDeEmisoras.get("Emisora" + orderBook.toString()) == null)
    {
      this.notificacionesDeEmisoras.put("Emisora" + orderBook.toString(), new Emisora());
      return new Emisora();
    }
    Emisora emisora = (Emisora)this.notificacionesDeEmisoras.get("Emisora" + orderBook.toString());
    return emisora;
  }
  
  public Profundidad obtenerNotificacionDeProfundiodadPorCartera(Integer orderBook)
  {
    if (this.notificacionesDeProfundidad.get("Profundidad" + orderBook.toString()) == null)
    {
      this.notificacionesDeProfundidad.put("Profundidad" + orderBook.toString(), new Profundidad());
      return new Profundidad();
    }
    Profundidad profundidad = (Profundidad)this.notificacionesDeProfundidad.get("Profundidad" + orderBook.toString());
    return profundidad;
  }
  
  public Hecho obtenerNotificacionDeHechoPorCartera(Integer orderBook)
  {
    if (this.notificacionesDeHechos.get("Hecho" + orderBook.toString()) == null)
    {
      this.notificacionesDeHechos.put("Hecho" + orderBook.toString(), new Hecho());
      return new Hecho();
    }
    Hecho hecho = (Hecho)this.notificacionesDeHechos.get("Hecho" + orderBook.toString());
    return hecho;
  }
  
  public Compra obtenerArregloCompra(String emisora, String orderBook)
  {
    Compra comp = (Compra)this.registrosDeCompras.get(emisora + orderBook);
    return comp;
  }
  
  public Venta obtenerArregloVenta(String emisora, String orderBook)
  {
    Venta vent = (Venta)this.registrosDeVentas.get(emisora + orderBook);
    return vent;
  }
  
  public Orden obtenerArregloOrden(String orderNumber)
  {
    Orden ord = (Orden)this.registrosDeOrdenes.get(orderNumber);
    return ord;
  }
  
  public void actualizaEmisora(Emisora emisora)
  {
    if (this.notificacionesDeEmisoras.get("Emisora" + emisora.getInstrumentNumber().toString()) != null)
    {
      this.notificacionesDeEmisoras.remove("Emisora" + emisora.getInstrumentNumber().toString());
      this.notificacionesDeEmisoras.put("Emisora" + emisora.getInstrumentNumber().toString(), emisora);
    }
    else
    {
      this.notificacionesDeEmisoras.put("Emisora" + emisora.getInstrumentNumber().toString(), emisora);
    }
  }
  
  public void actualizaHecho(Hecho hecho)
  {
    if (this.notificacionesDeHechos.get("Hecho" + hecho.getNumeroDeInstrumento()) != null)
    {
      this.notificacionesDeHechos.remove("Hecho" + hecho.getNumeroDeInstrumento());
      this.notificacionesDeHechos.put("Hecho" + hecho.getNumeroDeInstrumento(), hecho);
    }
    else
    {
      this.notificacionesDeHechos.put("Hecho" + hecho.getNumeroDeInstrumento(), hecho);
    }
  }
  
  public void actualizaProfundidad(Profundidad profundidad)
  {
    if (this.notificacionesDeProfundidad.get("Profundidad" + profundidad
      .getNumeroDeInstrumento().toString()) != null)
    {
      this.notificacionesDeProfundidad.remove("Profundidad" + profundidad
        .getNumeroDeInstrumento().toString());
      this.notificacionesDeProfundidad.put("Profundidad" + profundidad
        .getNumeroDeInstrumento().toString(), profundidad);
    }
    else
    {
      this.notificacionesDeProfundidad.put("Profundidad" + profundidad
        .getNumeroDeInstrumento().toString(), profundidad);
    }
  }
  
  public void eliminaOrdenE(Long orderNumber)
    throws Exception
  {
    Orden orden = buscarOrden(orderNumber);
    this.registrosDeOrdenes.remove(orderNumber.toString());
    orden.setEliminaOrden("E");
    this.registrosDeOrdenes.put(orderNumber.toString(), orden);
  }
  
  public void agregarRegistroEmisora(Emisora emisora)
  {
    this.notificacionesDeEmisoras.put("Emisora" + emisora.getInstrumentNumber().toString(), emisora);
  }
  
  public void agregarRegistroProfundidad(Profundidad profundidad)
  {
    this.notificacionesDeProfundidad.put("Profundidad" + profundidad
      .getNumeroDeInstrumento().toString(), profundidad);
  }
  
  public void agregarRegistroHecho(Hecho hecho)
  {
    this.notificacionesDeHechos.put("Hecho" + hecho.getNumeroDeInstrumento(), hecho);
  }
  
  public void agregarRegistroCompra(Compra compra)
  {
    this.registrosDeCompras.put(compra.getIdcompra(), compra);
  }
  
  public void agregarRegistroVenta(Venta venta)
  {
    this.registrosDeVentas.put(venta.getIdventa(), venta);
  }
  
  public void agregarRegistroCartera(Cartera cartera)
  {
    this.registroDeCarteras.put(cartera.getCarteraDePedido(), cartera);
  }
  
  public void agregarRegistroOrden(Orden orden)
  {
    this.registrosDeOrdenes.put(orden.getNumeroDeOrden().toString(), orden);
  }
  
  public void borrarNotificacionesDiarias()
    throws Exception
  {
    for (Object key : this.notificacionesDeEmisoras.keySet()) {
      if (((Emisora)this.notificacionesDeEmisoras.get(key)).getClass().toString().contains("Emisora"))
      {
        Emisora emisora = new Emisora();
        Emisora emisoraLimpia = new Emisora();
        emisora = (Emisora)this.notificacionesDeEmisoras.get(key);
        this.notificacionesDeEmisoras.remove(key);
        emisoraLimpia.setInstrumentNumber(emisora.getInstrumentNumber());
        this.actualizaGrid2.sendBody(emisoraLimpia);
      }
    }
    for (Object key : this.notificacionesDeHechos.keySet()) {
      if (((Hecho)this.notificacionesDeHechos.get(key)).getClass().toString().contains("Emisora"))
      {
        Hecho hecho = new Hecho();
        Hecho hecho2 = new Hecho();
        hecho = (Hecho)this.notificacionesDeHechos.get(key);
        this.notificacionesDeHechos.remove(key);
        hecho2.setNumeroDeInstrumento(hecho.getNumeroDeInstrumento());
        this.actualizaGrid2.sendBody(hecho2);
      }
    }
    for (Object key : this.notificacionesDeProfundidad.keySet()) {
      if (((Profundidad)this.notificacionesDeProfundidad.get(key)).getClass().toString().contains("Emisora"))
      {
        Profundidad profundidad = new Profundidad();
        Profundidad profundidad2 = new Profundidad();
        profundidad = (Profundidad)this.notificacionesDeProfundidad.get(key);
        this.notificacionesDeProfundidad.remove(key);
        profundidad2.setNumeroDeInstrumento(profundidad.getNumeroDeInstrumento());
        this.actualizaGrid2.sendBody(profundidad2);
      }
    }
   
    for(Object key : this.registrosDeCompras.keySet()) {
    	if(((Compra)this.registrosDeCompras.get(key)).getClass().toString().contains("Compra")) {
    		Compra compra = new Compra();
    		Compra compra2 = new Compra();
    		compra = (Compra)this.registrosDeCompras.get(key);
    		this.registrosDeCompras.remove(key);
    		compra2.setIdcompra(compra.getIdcompra());
    		this.actualizaGrid2.sendBody(compra2);
    	}
    }
    
    for(Object key : this.registrosDeVentas.keySet()) {
    	if(((Venta)this.registrosDeVentas.get(key)).getClass().toString().contains("Venta")) {
    		Venta venta = new Venta();
    		Venta venta2 = new Venta();
    		venta = (Venta)this.registrosDeVentas.get(key);
    		this.registrosDeVentas.remove(key);
    		venta2.setIdventa(venta.getIdventa());
    		this.actualizaGrid2.sendBody(venta2);
    	}
    }
    
    for(Object key : this.registrosDeOrdenes.keySet()) {
    	if(((Orden)this.registrosDeOrdenes.get(key)).getClass().toString().contains("Orden")) {
    		Orden orden = new Orden();
    		Orden orden2 = new Orden();
    		orden = (Orden)this.registrosDeOrdenes.get(key);
    		this.registrosDeOrdenes.remove(key);
    		orden2.setNumeroDeOrden(orden.getNumeroDeOrden());
    		this.actualizaGrid2.sendBody(orden2);
    	}
    }    
  }
}

