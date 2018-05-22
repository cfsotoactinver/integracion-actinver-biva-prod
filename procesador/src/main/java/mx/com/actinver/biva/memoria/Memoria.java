package mx.com.actinver.biva.memoria;

import mx.com.actinver.terminal.notificaciones.*;

import mx.com.biva.itch.modelo.D;

public interface Memoria {
	
	public void agregarRegistroEmisora(Emisora emisora);
	public void agregarRegistroProfundidad(Profundidad profundidad);
	public void agregarRegistroHecho(Hecho hecho);
	public void agregarRegistroCompra(Compra compra);
	public void agregarRegistroVenta(Venta venta);
	public void agregarRegistroCartera(Cartera cartera);
	public void agregarRegistroOrden(Orden orden);

	public void inicializar() throws Exception;

	public Double obtenerMejorRegistroDeVentas(String emisora, String orderBook) throws Exception;

	public Double obtenerMejorRegistroDeVentasPosicion(String emisora, String orderBook) throws Exception;

	public Double obtenerMejorRegistroDeCompraPorVolumen(String emisora, String orderBook) throws Exception;

	public Double obtenerMejorRegistroDeCompraPorVolumenPosicion(String emisora, String orderBook) throws Exception;

	public Orden obtenerOrden(String emisora, String serie, String vervoDeLaOrden, Integer numeroDeOrden) throws Exception;

	public Emisora obtenerNotificacionDeEmisora(String emisora, String serie) throws Exception;

	public Profundidad obtenerNotificacionDeProfundiodad(String emisora, String serie) throws Exception;

	public Hecho obtenerNotificacionDeHecho(String emisora, String serie) throws Exception;

	public void registrarOrdern(Orden orden) throws Exception;

	public Orden buscarOrden(Long orderNumber) throws Exception;

	public Emisora obtenerNotificacionDeEmisoraPorCartera(Integer orderBook) throws Exception;

	public Profundidad obtenerNotificacionDeProfundiodadPorCartera(Integer orderBook) throws Exception;

	public void actualizaCartera(Cartera cartera) throws Exception;

	public Orden buscarOrdenA(Long orderNumber) throws Exception;

	public Cartera buscarCartera(Integer orderBook) throws Exception;

	public void actualizarOrden(Orden orden) throws Exception;

	public Compra buscarArregloCompra(String emisora, String carteraDePedidos) throws Exception;

	public void atualizarArregoCompra(String idPostura, Compra compra) throws Exception;

	public Venta buscarArregloVenta(String emisora, String carteraDePedidos) throws Exception;

	public void atualizarArregoVenta(String idPostura, Venta venta) throws Exception;

	public void eliminarOrdenD(D mensajeD) throws Exception;

	public void eliminaOrdenU(Long originalOrderNumber);

	public Hecho obtenerNotificacionDeHechoPorCartera(Integer orderBook) throws Exception;

	public void actualizaEmisora(Emisora emisora) throws Exception;

	public void actualizaHecho(Hecho hecho) throws Exception;

	public void actualizaProfundidad(Profundidad profundidad) throws Exception;

	public Compra obtenerArregloCompra(String emisora, String orderBook) throws Exception;

	public Venta obtenerArregloVenta(String emisora, String orderBook) throws Exception;

	public Orden obtenerArregloOrden(String orderNumber) throws Exception;

	public void eliminaOrdenE(Long orderNumber) throws Exception;
	
	public void borrarNotificacionesDiarias() throws Exception;

}