package mx.com.actinver.biva.procesadores;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.StringWriter;
import java.util.List;

import mx.com.actinver.biva.memoria.Cartera;
import mx.com.actinver.biva.memoria.Compra;
import mx.com.actinver.biva.memoria.Indice;
import mx.com.actinver.biva.memoria.Memoria;
import mx.com.actinver.biva.memoria.Orden;
import mx.com.actinver.biva.memoria.RegistroGrid;
import mx.com.actinver.biva.memoria.Venta;
import mx.com.actinver.biva.persistencia.basededatos.PersistenciaBaseDeDatos;
import mx.com.actinver.terminal.notificaciones.Emisora;
import mx.com.actinver.terminal.notificaciones.Hecho;
import mx.com.actinver.terminal.notificaciones.Profundidad;
import mx.com.biva.itch.modelo.C;
import mx.com.biva.itch.modelo.E;
import mx.com.biva.itch.modelo.H;
import mx.com.biva.itch.modelo.L;
import mx.com.biva.itch.modelo.M;
import mx.com.biva.itch.modelo.P;
import mx.com.biva.itch.modelo.R;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SincronizadorLimpieza implements Processor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SincronizadorLimpieza.class);
	private ObjectMapper objectMapper = new ObjectMapper();
	private StringWriter stringWriter;
	private Indice indiceNuevo;

	public void process(Exchange exchange) throws Exception {
		String tipo = exchange.getIn().getBody().toString();
		exchange.getIn().setHeader("enviaraGrid", Boolean.valueOf(false));
		if (tipo.contains("Emisora@")) {
			Emisora notificacionDeEmisora = (Emisora) exchange.getIn().getBody(
					Emisora.class);
			RegistroGrid registro = new RegistroGrid();
			registro.setClave("Emisora"
					+ notificacionDeEmisora.getInstrumentNumber().toString());
			registro.setTipo("Emisora");
			this.stringWriter = new StringWriter();
			this.objectMapper.writeValue(this.stringWriter,
					notificacionDeEmisora);
			registro.setValor(this.stringWriter.toString());
			exchange.getIn().setBody(registro);
			exchange.getIn().setHeader("indexar", Boolean.valueOf(false));
			exchange.getIn().setHeader("enviaraGrid", Boolean.valueOf(true));
		} else if (tipo.contains("Profundidad@")) {
			Profundidad notificacionDeProfundidad = (Profundidad) exchange
					.getIn().getBody(Profundidad.class);
			RegistroGrid registro = new RegistroGrid();
			registro.setClave("Profundidad"
					+ notificacionDeProfundidad.getNumeroDeInstrumento()
							.toString());
			registro.setTipo("Profundidad");
			this.stringWriter = new StringWriter();
			this.objectMapper.writeValue(this.stringWriter,
					notificacionDeProfundidad);
			registro.setValor(this.stringWriter.toString());
			exchange.getIn().setBody(registro);
			exchange.getIn().setHeader("indexar", Boolean.valueOf(false));
			exchange.getIn().setHeader("enviaraGrid", Boolean.valueOf(true));
		} else if (tipo.contains("Hecho@")) {
			Hecho notificacionDeHecho = (Hecho) exchange.getIn().getBody(
					Hecho.class);
			RegistroGrid registro = new RegistroGrid();
			registro.setClave("Hecho"
					+ notificacionDeHecho.getNumeroDeInstrumento());
			registro.setTipo("Hecho");
			this.stringWriter = new StringWriter();
			this.objectMapper
					.writeValue(this.stringWriter, notificacionDeHecho);
			registro.setValor(this.stringWriter.toString());
			exchange.getIn().setBody(registro);
			exchange.getIn().setHeader("indexar", Boolean.valueOf(false));
			exchange.getIn().setHeader("enviaraGrid", Boolean.valueOf(true));
		} else if (tipo.contains("Orden@")) {
			Orden orden = (Orden) exchange.getIn().getBody(Orden.class);
			RegistroGrid registro = new RegistroGrid();
			registro.setClave(orden.getNumeroDeOrden().toString());
			registro.setTipo("Orden");
			this.stringWriter = new StringWriter();
			this.objectMapper.writeValue(this.stringWriter, orden);
			registro.setValor(this.stringWriter.toString());
			exchange.getIn().setBody(registro);
			exchange.getIn().setHeader("indexar", Boolean.valueOf(false));
			exchange.getIn().setHeader("enviaraGrid", Boolean.valueOf(true));
		}

		else if (tipo.contains("Compra@")) {
			Compra compra = (Compra) exchange.getIn().getBody(Compra.class);
			RegistroGrid registro = new RegistroGrid();
			registro.setClave(compra.getIdcompra());
			registro.setTipo("Compra");
			this.stringWriter = new StringWriter();
			this.objectMapper.writeValue(this.stringWriter, compra);
			registro.setValor(this.stringWriter.toString());
			exchange.getIn().setBody(registro);
			exchange.getIn().setHeader("indexar", Boolean.valueOf(false));
			exchange.getIn().setHeader("enviaraGrid", Boolean.valueOf(true));
		} else if (tipo.contains("Venta@")) {
			Venta venta = (Venta) exchange.getIn().getBody(Venta.class);
			RegistroGrid registro = new RegistroGrid();
			registro.setClave(venta.getIdventa());
			registro.setTipo("Venta");
			this.stringWriter = new StringWriter();
			this.objectMapper.writeValue(this.stringWriter, venta);
			registro.setValor(this.stringWriter.toString());
			exchange.getIn().setBody(registro);
			exchange.getIn().setHeader("indexar", Boolean.valueOf(false));
			exchange.getIn().setHeader("enviaraGrid", Boolean.valueOf(true));
		} else if (tipo.contains("Cartera@")) {
			Cartera cartera = (Cartera) exchange.getIn().getBody(Cartera.class);
			RegistroGrid registro = new RegistroGrid();
			registro.setClave(cartera.getCarteraDePedido());
			registro.setTipo("Cartera");
			this.indiceNuevo.getRegistros().add(registro);
			this.stringWriter = new StringWriter();
			this.objectMapper.writeValue(this.stringWriter, cartera);
			registro.setValor(this.stringWriter.toString());
			exchange.getIn().setBody(registro);
			exchange.getIn().setHeader("indice", this.indiceNuevo);
			exchange.getIn().setHeader("indexar", Boolean.valueOf(true));
			exchange.getIn().setHeader("enviaraGrid", Boolean.valueOf(true));
		}else if (tipo.contains("reiniciar")) {
			indiceNuevo.inicializar();
			exchange.getIn().setHeader("enviaraGrid", Boolean.valueOf(false));
			exchange.getIn().setHeader("indexar", Boolean.valueOf(false));
		}
	}

	public Indice getIndiceNuevo() {
		return indiceNuevo;
	}

	public void setIndiceNuevo(Indice indiceNuevo) {
		this.indiceNuevo = indiceNuevo;
	}

}
