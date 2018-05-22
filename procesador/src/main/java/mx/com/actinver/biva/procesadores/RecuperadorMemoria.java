package mx.com.actinver.biva.procesadores;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.com.actinver.biva.memoria.Cartera;
import mx.com.actinver.biva.memoria.Compra;
import mx.com.actinver.biva.memoria.Memoria;
import mx.com.actinver.biva.memoria.Orden;
import mx.com.actinver.biva.memoria.RegistroGrid;
import mx.com.actinver.biva.memoria.Venta;
import mx.com.actinver.terminal.notificaciones.Emisora;
import mx.com.actinver.terminal.notificaciones.Hecho;
import mx.com.actinver.terminal.notificaciones.Profundidad;

public class RecuperadorMemoria implements Processor {
	
	private Memoria memoria;
	private ObjectMapper objectMapper = new ObjectMapper();

	public Memoria getMemoria() {
		return memoria;
	}

	public void setMemoria(Memoria memoria) {
		this.memoria = memoria;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		
		
		String tipo = exchange.getIn().getHeader("tipoDeObjeto").toString();

		if (tipo.equals("Emisora")) {
			Emisora emisora = this.objectMapper.readValue(exchange.getIn().getBody(String.class), Emisora.class);
			this.memoria.agregarRegistroEmisora(emisora);
		} else if (tipo.equals("Profundidad")) {
			Profundidad profundidad = this.objectMapper.readValue(exchange.getIn().getBody(String.class), Profundidad.class);
			this.memoria.agregarRegistroProfundidad(profundidad);
		} else if (tipo.equals("Hecho")) {
			Hecho hecho = this.objectMapper.readValue(exchange.getIn().getBody(String.class), Hecho.class);
			this.memoria.agregarRegistroHecho(hecho);
		} else if (tipo.equals("Orden")) {
			Orden orden = this.objectMapper.readValue(exchange.getIn().getBody(String.class), Orden.class);
			this.memoria.agregarRegistroOrden(orden);
		} else if (tipo.equals("Cartera")) {
			Cartera cartera = this.objectMapper.readValue(exchange.getIn().getBody(String.class), Cartera.class);
			this.memoria.agregarRegistroCartera(cartera);
		} else if (tipo.equals("Compra")) {
			Compra compra = this.objectMapper.readValue(exchange.getIn().getBody(String.class), Compra.class);
			this.memoria.agregarRegistroCompra(compra);
		} else if (tipo.equals("Venta")) {
			Venta venta = this.objectMapper.readValue(exchange.getIn().getBody(String.class), Venta.class);
			this.memoria.agregarRegistroVenta(venta);
		}
	}

}