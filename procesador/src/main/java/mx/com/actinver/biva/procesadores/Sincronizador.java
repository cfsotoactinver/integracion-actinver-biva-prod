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

public class Sincronizador
  implements Processor
{
  private PersistenciaBaseDeDatos baseDeDatos;
  private Memoria memoria;
  private static final Logger LOGGER = LoggerFactory.getLogger(Sincronizador.class);
  private ObjectMapper objectMapper = new ObjectMapper();
  private StringWriter stringWriter;
  private Indice indice;
  
  public void process(Exchange exchange)
    throws Exception
  {
    String tipo = exchange.getIn().getBody().toString();
    exchange.getIn().setHeader("enviaraGrid", Boolean.valueOf(false));
    if (tipo.contains("Emisora@"))
    {
      Emisora notificacionDeEmisora = (Emisora)exchange.getIn().getBody(Emisora.class);
      this.baseDeDatos.registraEnTrmCorro(notificacionDeEmisora);
      this.baseDeDatos.registraEnTrmUhech(notificacionDeEmisora);
      if ((notificacionDeEmisora.getPostura() != null) && (notificacionDeEmisora.getPostura().equals("compra"))) {
        this.baseDeDatos.registraEnCorroCompra(notificacionDeEmisora);
      } else if ((notificacionDeEmisora.getPostura() != null) && (notificacionDeEmisora.getPostura().equals("venta"))) {
        this.baseDeDatos.registraEnCorroVenta(notificacionDeEmisora);
      }
      RegistroGrid registro = new RegistroGrid();
      registro.setClave("Emisora" + notificacionDeEmisora.getInstrumentNumber().toString());
      registro.setTipo("Emisora");
      this.indice.getRegistros().add(registro);
      this.stringWriter = new StringWriter();
      this.objectMapper.writeValue(this.stringWriter, notificacionDeEmisora);
      registro.setValor(this.stringWriter.toString());
      exchange.getIn().setBody(registro);
      exchange.getIn().setHeader("indice", this.indice);
      exchange.getIn().setHeader("enviaraGrid", Boolean.valueOf(true));
    }
    else if (tipo.contains("Profundidad@"))
    {
      Profundidad notificacionDeProfundidad = (Profundidad)exchange.getIn().getBody(Profundidad.class);
      this.baseDeDatos.registraEnTrmpRfc(notificacionDeProfundidad);
      this.baseDeDatos.registraEnTrmpRfv(notificacionDeProfundidad);
      RegistroGrid registro = new RegistroGrid();
      registro.setClave("Profundidad" + notificacionDeProfundidad.getNumeroDeInstrumento().toString());
      registro.setTipo("Profundidad");
      this.indice.getRegistros().add(registro);
      this.stringWriter = new StringWriter();
      this.objectMapper.writeValue(this.stringWriter, notificacionDeProfundidad);
      registro.setValor(this.stringWriter.toString());
      exchange.getIn().setBody(registro);
      exchange.getIn().setHeader("indice", this.indice);
      exchange.getIn().setHeader("enviaraGrid", Boolean.valueOf(true));
    }
    else if (tipo.contains("Hecho@"))
    {
      Hecho notificacionDeHecho = (Hecho)exchange.getIn().getBody(Hecho.class);
      RegistroGrid registro = new RegistroGrid();
      registro.setClave("Hecho" + notificacionDeHecho.getNumeroDeInstrumento());
      registro.setTipo("Hecho");
      this.indice.getRegistros().add(registro);
      this.stringWriter = new StringWriter();
      this.objectMapper.writeValue(this.stringWriter, notificacionDeHecho);
      registro.setValor(this.stringWriter.toString());
      exchange.getIn().setBody(registro);
      exchange.getIn().setHeader("indice", this.indice);
      exchange.getIn().setHeader("enviaraGrid", Boolean.valueOf(true));
    }
    else if (tipo.contains("Orden@"))
    {
      Orden orden = (Orden)exchange.getIn().getBody(Orden.class);
      RegistroGrid registro = new RegistroGrid();
      registro.setClave(orden.getNumeroDeOrden().toString());
      registro.setTipo("Orden");
      this.indice.getRegistros().add(registro);
      this.stringWriter = new StringWriter();
      this.objectMapper.writeValue(this.stringWriter, orden);
      registro.setValor(this.stringWriter.toString());
      exchange.getIn().setBody(registro);
      exchange.getIn().setHeader("indice", this.indice);
      exchange.getIn().setHeader("enviaraGrid", Boolean.valueOf(true));
    }
    else if (tipo.contains("Cartera@"))
    {
      Cartera cartera = (Cartera)exchange.getIn().getBody(Cartera.class);
      RegistroGrid registro = new RegistroGrid();
      registro.setClave(cartera.getCarteraDePedido());
      registro.setTipo("Cartera");
      this.indice.getRegistros().add(registro);
      this.stringWriter = new StringWriter();
      this.objectMapper.writeValue(this.stringWriter, cartera);
      registro.setValor(this.stringWriter.toString());
      exchange.getIn().setBody(registro);
      exchange.getIn().setHeader("indice", this.indice);
      exchange.getIn().setHeader("enviaraGrid", Boolean.valueOf(true));
    }
    else if (tipo.contains("Compra@"))
    {
      Compra compra = (Compra)exchange.getIn().getBody(Compra.class);
      RegistroGrid registro = new RegistroGrid();
      registro.setClave(compra.getIdcompra() + "B");
      registro.setTipo("Compra");
      this.indice.getRegistros().add(registro);
      this.stringWriter = new StringWriter();
      this.objectMapper.writeValue(this.stringWriter, compra);
      registro.setValor(this.stringWriter.toString());
      exchange.getIn().setBody(registro);
      exchange.getIn().setHeader("indice", this.indice);
      exchange.getIn().setHeader("enviaraGrid", Boolean.valueOf(true));
    }
    else if (tipo.contains("Venta@"))
    {
      Venta venta = (Venta)exchange.getIn().getBody(Venta.class);
      RegistroGrid registro = new RegistroGrid();
      registro.setClave(venta.getIdventa() + "S");
      registro.setTipo("Venta");
      this.indice.getRegistros().add(registro);
      this.stringWriter = new StringWriter();
      this.objectMapper.writeValue(this.stringWriter, venta);
      registro.setValor(this.stringWriter.toString());
      exchange.getIn().setBody(registro);
      exchange.getIn().setHeader("indice", this.indice);
      exchange.getIn().setHeader("enviaraGrid", Boolean.valueOf(true));
    }
    else if (tipo.contains("H@"))
    {
      H mensajeH = (H)exchange.getIn().getBody(H.class);
      this.baseDeDatos.registraEnEstadoInstrumento(mensajeH);
    }
    else if (tipo.contains("R@"))
    {
      R mensajeR = (R)exchange.getIn().getBody(R.class);
      this.baseDeDatos.registraEnGrupo(mensajeR);
    }
    else if (tipo.contains("L@"))
    {
      L mensajeL = (L)exchange.getIn().getBody(L.class);
      this.baseDeDatos.registraEnTables(mensajeL, null);
    }
    else if (tipo.contains("M@"))
    {
      M mensajeM = (M)exchange.getIn().getBody(M.class);
      this.baseDeDatos.registraEnTables(null, mensajeM);
    }

  }
  
  public PersistenciaBaseDeDatos getBaseDeDatos()
  {
    return this.baseDeDatos;
  }
  
  public void setBaseDeDatos(PersistenciaBaseDeDatos baseDeDatos)
  {
    this.baseDeDatos = baseDeDatos;
  }
  
  public Memoria getMemoria()
  {
    return this.memoria;
  }
  
  public void setMemoria(Memoria memoria)
  {
    this.memoria = memoria;
  }
  
  public Indice getIndice()
  {
    return this.indice;
  }
  
  public void setIndice(Indice indice)
  {
    this.indice = indice;
  }
}
