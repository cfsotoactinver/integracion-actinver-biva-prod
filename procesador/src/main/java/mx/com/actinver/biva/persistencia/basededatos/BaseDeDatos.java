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
package mx.com.actinver.biva.persistencia.basededatos;

import java.util.List;
import mx.com.actinver.biva.error.ErrorDeBaseDeDatos;
import mx.com.actinver.biva.memoria.*;
import mx.com.biva.itch.modelo.*;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import mx.com.actinver.terminal.notificaciones.*;
import mx.com.actinver.terminal.notificaciones.Nivel;

/**
 * @author Alexis Hurtado
 *
 */
public class BaseDeDatos implements PersistenciaBaseDeDatos {
  @Produce(uri = "activemq:queue:BIVA.ITCH.INSERT")
  private ProducerTemplate insert;
  // Servicios inyectados
  // private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private Memoria memoria;

  private Almacen almacen;
  private static final Logger LOGGER = LoggerFactory.getLogger(BaseDeDatos.class);
  private final static String ERROR_CAMPO_NULO = "El campo es nulo.";
  private final static String MENSAJE_INSERT_TRMCORRO = "Insertando en BIVA_TRMCORRO";
  private final static String ERROR_NOTIF_EMISORA_NULA =
      "La notificacion de emisora recibida es nula.";
  private final static String ERROR_NOTIF_PROFUNDIDAD_NULA =
      "La notificacion de profundidad es nula";
  private final static String ERROR_MENSAJE_R = "El mensaje R es nulo";
  private final static String ERROR_MENSAJE_LM = "Los mensajes L y M son nulos";
  private final static String ERROR_MENSAJE_ECP = "El mensaje E,C,P son nulos";
  private final static String ERROR_MENSAJE_H = "El mensaje H es nulo";
  private final static String MENSAJE_INSERT_TRMPRFC = "Insertando en BIVA_TRMPRFC";
  private final static String MENSAJE_INSERT_TRMPRFV = "Insertando en BIVA_TRMPRFV";
  private final static String MENSAJE_INSERT_TRMUHECH = "Insertando en BIVA_TRMUHECH";
  private final static String MENSAJE_INSERT_ITCH_CORROCOMPRA = "Insertando en ITCH_CORRO_COMPRA";
  private final static String MENSAJE_INSERT_ITCH_CORROVENTA = "Insertando en ITCH_CORRO_VENTA";
  private final static String MENSAJE_INSERT_ITCHGPO = "Insertando en ITCH_GRUPO";
  private final static String MENSAJE_INSERT_ITCHTABLES = "Insertando en ITCH_TABLES";
  private final static String MENSAJE_INSERT_UHECHO = "Insertando en ITCH_UHECHO";
  private final static String MENSAJE_INSERT_BIVA_EDO_INST =
      "Insertando en BIVA_ESTADO_INSTRUMENTO";
  // Sentencias SQL
  private final static String INSERT_TRMCORRO =
      "INSERT INTO PRODSQL.BIVA_TRMCORRO_RH (ISIN,BID_PRICE,BID_QUANTITY,BID_TIME,ASK_PRICE,ASK_QUANTITY,ASK_TIME,HORACAMBIO) VALUES (";
  private final static String INSERT_TRMPRFC =
      "INSERT INTO PRODSQL.BIVA_TRMPRFC_RH (EMISORA,SERIE,PC1,PC2,PC3,PC4,PC5,PC6,PC7,PC8,PC9,PC10,OC1,OC2,OC3,OC4,OC5,OC6,OC7,OC8,OC9,OC10,VC1,VC2,VC3,VC4,VC5,VC6,VC7,VC8,VC9,VC10,HORACAMBIO) VALUES (";
  private final static String INSERT_TRMPRFV =
      "INSERT INTO PRODSQL.BIVA_TRMPRFV_RH (EMISORA,SERIE,PV1,PV2,PV3,PV4,PV5,PV6,PV7,PV8,PV9,PV10,OV1,OV2,OV3,OV4,OV5,OV6,OV7,OV8,OV9,OV10,VV1,VV2,VV3,VV4,VV5,VV6,VV7,VV8,VV9,VV10,HORACAMBIO) VALUES (";
  private final static String INSERT_TRMUHECH =
      "INSERT INTO PRODSQL.BIVA_TRMUHECH_RH (ISIN,LAST_PRICE,LAST_TIME,TRADE_QUANTITY,TRADE_VALUE,TRADE_AVERAGE,CLOSE_PRICE,MAXIMUM_PRICE,MAXIMUM_TIME,MINIMUM_PRICE,MINIMUM_TIME,PERCENTAGE_CHANGE,HORACAMBIO) VALUES (";
  private final static String INSERT_ITCH_CORROCOMPRA =
      "INSERT INTO PRODSQL.ITCH_CORRO_COMPRA_RH (ORDERBOOK,CANTIDAD,PRECIO,HORAORIGEN) VALUES (";
  private final static String INSERT_ITCH_CORROVENTA =
      "INSERT INTO PRODSQL.ITCH_CORRO_VENTA_RH (ORDERBOOK,CANTIDAD,PRECIO,HORAORIGEN) VALUES (";
  private final static String INSERT_ITCH_GRUPO =
      "INSERT INTO PRODSQL.ITCH_GRUPO_RH (GRUPO_ID,ORDERBOOK,ISIN,SYMBOL,MONEDA,TABLEID_Q,TABLEID_P,PRICE_DECIMALS,HORAORIGEN) VALUES (";
  private final static String INSERT_ITCH_TABLES =
      "INSERT INTO PRODSQL.ITCH_TABLES_RH (TABLE_ID,TICK_SIZE,INICIA,HORAORIGEN) VALUES (";
  private final static String INSERT_ITCH_UHECHO =
      "INSERT INTO PRODSQL.ITCH_UHECHO_RH (ORDERBOOK,FOLIO_ASIGNACION,CANTIDAD,PRECIO,TRADE_INDICATOR,HORAORIGEN) VALUES (";
  private final static String INSERT_BIVA_ESTADO_INST =
      "INSERT  INTO PRODSQL.BIVA_ESTADO_INSTRUMENTO_RH (NUMERO_INSTRUMENTO,ESTATUS,HORAORIGEN) VALUES (";

  /*
   * (non-Javadoc)
   * 
   * @see mx.com.actinver.biva.persistencia.PersistenciaBaseDeDatos#registraEnTrmCorro (
   * mx.com.actinver.terminal.notificaciones.Emisora)
   */
  @Override
  public void registraEnTrmCorro(Emisora notificacionDeEmisora) throws ErrorDeBaseDeDatos {
    if (notificacionDeEmisora == null) {
      throw new ErrorDeBaseDeDatos(ERROR_NOTIF_EMISORA_NULA);
    } else {
      if (notificacionDeEmisora.getIsin() != null) {
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN ATOMIC ");
        sb.append("IF NOT EXISTS (SELECT 1 FROM PRODSQL.BIVA_TRMCORRO_RH WHERE ISIN=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getIsin() + "') ");
        sb.append("THEN ");
        sb.append(INSERT_TRMCORRO);
        sb.append("'");
        sb.append(notificacionDeEmisora.getIsin());
        sb.append("',");

        sb.append("'");
        sb.append(notificacionDeEmisora.getBestBid() == null ? 0 : notificacionDeEmisora
            .getBestBid());
        sb.append("',");

        sb.append("'");
        sb.append(notificacionDeEmisora.getBidVolume() == null ? 0 : notificacionDeEmisora
            .getBidVolume());
        sb.append("',");

        sb.append("'");
        sb.append(notificacionDeEmisora.getBidTime() == null ? "19691231-18:00:00.000"
            : notificacionDeEmisora.getBidTime());
        sb.append("',");

        sb.append("'");
        sb.append(notificacionDeEmisora.getBestOffer() == null ? 0 : notificacionDeEmisora
            .getBestOffer());
        sb.append("',");

        sb.append("'");
        sb.append(notificacionDeEmisora.getOfferVolume() == null ? 0 : notificacionDeEmisora
            .getOfferVolume());
        sb.append("',");

        sb.append("'");
        sb.append(notificacionDeEmisora.getOfferTime() == null ? "19691231-18:00:00.000"
            : notificacionDeEmisora.getOfferTime());
        sb.append("',");

        sb.append("CURRENT_TIMESTAMP); ");
        sb.append("ELSE ");
        sb.append("UPDATE PRODSQL.BIVA_TRMCORRO_RH SET ");
        sb.append("BID_PRICE=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getBestBid() == null ? 0 : notificacionDeEmisora
            .getBestBid());
        sb.append("',");

        sb.append("BID_QUANTITY=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getBidVolume() == null ? 0 : notificacionDeEmisora
            .getBidVolume());
        sb.append("',");

        sb.append("BID_TIME=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getBidTime() == null ? "19691231-18:00:00.000"
            : notificacionDeEmisora.getBidTime());
        sb.append("',");

        sb.append("ASK_PRICE=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getBestOffer() == null ? 0 : notificacionDeEmisora
            .getBestOffer());
        sb.append("',");

        sb.append("ASK_QUANTITY=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getOfferVolume() == null ? 0 : notificacionDeEmisora
            .getOfferVolume());
        sb.append("',");

        sb.append("ASK_TIME=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getOfferTime() == null ? "19691231-18:00:00.000"
            : notificacionDeEmisora.getOfferTime());
        sb.append("',");

        sb.append("HORACAMBIO=CURRENT_TIMESTAMP ");
        sb.append("WHERE ISIN=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getIsin() + "';");
        sb.append("END IF; ");
        sb.append("END ");
        insert.sendBody(sb.toString());
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see mx.com.actinver.biva.persistencia.PersistenciaBaseDeDatos#registraEnTrmpRfc (
   * mx.com.actinver.terminal.notificaciones.Profundidad)
   */
  @Override
  public void registraEnTrmpRfc(Profundidad notificacionDeProfundidad) throws ErrorDeBaseDeDatos {

    if (notificacionDeProfundidad == null) {
      throw new ErrorDeBaseDeDatos(ERROR_NOTIF_PROFUNDIDAD_NULA);
    } else {

      if (notificacionDeProfundidad.getLado() == 0 && notificacionDeProfundidad.getIssuer() != null
          && notificacionDeProfundidad.getSerie() != null) {
        List<Nivel> prof = notificacionDeProfundidad.getProfundidad();
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN ATOMIC ");
        sb.append("IF NOT EXISTS (SELECT 1 FROM PRODSQL.BIVA_TRMPRFC_RH WHERE EMISORA='"
            + notificacionDeProfundidad.getIssuer() + "' and " + "SERIE='"
            + notificacionDeProfundidad.getSerie() + "') ");
        sb.append("THEN ");
        sb.append(INSERT_TRMPRFC);
        sb.append("'");
        sb.append(notificacionDeProfundidad.getIssuer() == null ? "AMX" : notificacionDeProfundidad
            .getIssuer());
        sb.append("',");

        sb.append("'");
        sb.append(notificacionDeProfundidad.getSerie() == null ? "*" : notificacionDeProfundidad
            .getSerie());
        sb.append("',");

        int counter = 0;
        for (Nivel nivel : prof) {
          if (counter < 10) {
            sb.append(nivel.getPrecio() == null ? "0" : nivel.getPrecio());
            sb.append(",");
          } else {
            break;
          }
          counter++;
        }
        if (counter < 10) {
          while (counter < 10) {
            sb.append("0");
            sb.append(",");
            counter++;
          }
        }
        counter = 0;
        for (Nivel nivel : prof) {
          if (counter < 10) {
            sb.append(nivel.getOrdenes() == null ? "0" : nivel.getOrdenes());
            sb.append(",");
          } else if (counter == 10) {
            break;
          }
          counter++;
        }
        if (counter < 10) {
          while (counter < 10) {
            sb.append("0");
            sb.append(",");
            counter++;
          }
        }
        counter = 0;
        for (Nivel nivel : prof) {
          if (counter < 10) {
            sb.append(nivel.getVolumen() == null ? "0" : nivel.getVolumen());
            sb.append(",");
          } else if (counter == 10) {
            break;
          }
          counter++;
        }
        if (counter < 10) {
          while (counter < 10) {
            sb.append("0");
            sb.append(",");
            counter++;
          }

        }
        sb.append("CURRENT_TIMESTAMP); ");
        sb.append("ELSE ");
        sb.append("UPDATE PRODSQL.BIVA_TRMPRFC_RH SET ");
        counter = 1;
        for (Nivel nivel : prof) {
          sb.append("PC" + counter + "=");
          sb.append(nivel.getPrecio() == null ? "0" : nivel.getPrecio());
          if (counter < 10) {
            sb.append(",");
          } else if (counter == 10) {
            sb.append(",");
            break;
          }
          counter++;
        }
        if (counter < 10) {
          while (counter <= 10) {
            sb.append("PC" + counter + "=");
            sb.append("0");
            sb.append(",");
            counter++;
          }
        }
        counter = 1;
        for (Nivel nivel : prof) {
          sb.append("OC" + counter + "=");
          sb.append(nivel.getOrdenes() == null ? "0" : nivel.getOrdenes());
          if (counter < 10) {
            sb.append(",");
          } else if (counter == 10) {
            sb.append(",");
            break;
          }
          counter++;
        }
        if (counter < 10) {
          while (counter <= 10) {
            sb.append("OC" + counter + "=");
            sb.append("0");
            sb.append(",");
            counter++;
          }
        }
        counter = 1;
        for (Nivel nivel : prof) {
          sb.append("VC" + counter + "=");
          sb.append(nivel.getVolumen() == null ? "0" : nivel.getVolumen());
          if (counter < 10) {
            sb.append(",");
          } else if (counter == 10) {
            sb.append(",");
            break;
          }
          counter++;
        }
        if (counter < 10) {
          while (counter <= 10) {
            sb.append("VC" + counter + "=");
            sb.append("0");
            sb.append(",");
            counter++;
          }

        }
        sb.append("HORACAMBIO=CURRENT_TIMESTAMP ");
        sb.append("WHERE  EMISORA='" + notificacionDeProfundidad.getIssuer() + "' and " + "SERIE='"
            + notificacionDeProfundidad.getSerie() + "'; ");
        sb.append("END IF;");
        sb.append("END ");
        insert.sendBody(sb.toString());
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see mx.com.actinver.biva.persistencia.PersistenciaBaseDeDatos#registraEnTrmpRfv (
   * mx.com.actinver.terminal.notificaciones.Profundidad)
   */
  @Override
  public void registraEnTrmpRfv(Profundidad notificacionDeProfundidad) throws ErrorDeBaseDeDatos {
    if (notificacionDeProfundidad == null) {
      throw new ErrorDeBaseDeDatos(ERROR_NOTIF_PROFUNDIDAD_NULA);
    } else {

      if (notificacionDeProfundidad.getLado() == 1 && notificacionDeProfundidad.getIssuer() != null
          && notificacionDeProfundidad.getSerie() != null) {
        List<Nivel> prof = notificacionDeProfundidad.getProfundidad();
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN ATOMIC ");
        sb.append("IF NOT EXISTS (SELECT 1 FROM PRODSQL.BIVA_TRMPRFV_RH WHERE EMISORA='"
            + notificacionDeProfundidad.getIssuer() + "' and " + "SERIE='"
            + notificacionDeProfundidad.getSerie() + "') ");
        sb.append("THEN ");
        sb.append(INSERT_TRMPRFV);
        sb.append("'");
        sb.append(notificacionDeProfundidad.getIssuer() == null ? "AMX" : notificacionDeProfundidad
            .getIssuer());
        sb.append("',");

        sb.append("'");
        sb.append(notificacionDeProfundidad.getSerie() == null ? "*" : notificacionDeProfundidad
            .getSerie());
        sb.append("',");

        int counter = 0;
        for (Nivel nivel : prof) {

          if (counter < 10) {
            sb.append(nivel.getPrecio() == null ? "0" : nivel.getPrecio());
            sb.append(",");
          } else if (counter == 10) {

            break;
          }
          counter++;
        }
        if (counter < 10) {
          while (counter < 10) {
            sb.append("0");
            sb.append(",");
            counter++;
          }
        }
        counter = 0;
        for (Nivel nivel : prof) {

          if (counter < 10) {
            sb.append(nivel.getOrdenes() == null ? "0" : nivel.getOrdenes());
            sb.append(",");
          } else if (counter == 10) {

            break;
          }
          counter++;
        }
        if (counter < 10) {
          while (counter < 10) {
            sb.append("0");
            sb.append(",");
            counter++;
          }
        }
        counter = 0;
        for (Nivel nivel : prof) {

          if (counter < 10) {
            sb.append(nivel.getVolumen() == null ? "0" : nivel.getVolumen());
            sb.append(",");
          } else if (counter == 10) {
            break;
          }
          counter++;
        }
        if (counter < 10) {
          while (counter < 10) {
            sb.append("0");
            sb.append(",");
            counter++;
          }

        }
        sb.append("CURRENT_TIMESTAMP); ");
        sb.append("ELSE ");
        sb.append("UPDATE PRODSQL.BIVA_TRMPRFV_RH SET ");
        counter = 1;
        for (Nivel nivel : prof) {
          sb.append("PV" + counter + "=");
          sb.append(nivel.getPrecio() == null ? "0" : nivel.getPrecio());
          if (counter < 10) {
            sb.append(",");
          } else if (counter == 10) {
            sb.append(",");
            break;
          }
          counter++;
        }
        if (counter < 10) {
          while (counter <= 10) {
            sb.append("PV" + counter + "=");
            sb.append("0");
            sb.append(",");
            counter++;
          }
        }
        counter = 1;
        for (Nivel nivel : prof) {
          sb.append("OV" + counter + "=");
          sb.append(nivel.getOrdenes() == null ? "0" : nivel.getOrdenes());
          if (counter < 10) {
            sb.append(",");
          } else if (counter == 10) {
            sb.append(",");
            break;
          }
          counter++;
        }
        if (counter < 10) {
          while (counter <= 10) {
            sb.append("OV" + counter + "=");
            sb.append("0");
            sb.append(",");
            counter++;
          }
        }
        counter = 1;
        for (Nivel nivel : prof) {
          sb.append("VV" + counter + "=");
          sb.append(nivel.getVolumen() == null ? "0" : nivel.getVolumen());
          if (counter < 10) {
            sb.append(",");
          } else if (counter == 10) {
            sb.append(",");
            break;
          }
          counter++;
        }
        if (counter < 10) {
          while (counter <= 10) {
            sb.append("VV" + counter + "=");
            sb.append("0");
            sb.append(",");
            counter++;
          }

        }
        sb.append("HORACAMBIO=CURRENT_TIMESTAMP ");
        sb.append("WHERE  EMISORA='" + notificacionDeProfundidad.getIssuer() + "' and " + "SERIE='"
            + notificacionDeProfundidad.getSerie() + "'; ");
        sb.append("END IF;");
        sb.append("END ");
        insert.sendBody(sb.toString());
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see mx.com.actinver.biva.persistencia.PersistenciaBaseDeDatos#registraEnTrmUhech (
   * mx.com.actinver.terminal.notificaciones.Emisora)
   */
  @Override
  public void registraEnTrmUhech(Emisora notificacionDeEmisora) throws ErrorDeBaseDeDatos {

    if (notificacionDeEmisora == null) {
      throw new ErrorDeBaseDeDatos(ERROR_NOTIF_EMISORA_NULA);
    } else {
      if (notificacionDeEmisora.getIsin() != null) {

        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN ATOMIC ");
        sb.append("IF NOT EXISTS (SELECT 1 FROM PRODSQL.BIVA_TRMUHECH_RH WHERE ISIN=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getIsin() + "') ");
        sb.append("THEN ");
        sb.append(INSERT_TRMUHECH);
        sb.append("'");
        sb.append(notificacionDeEmisora.getIsin());
        sb.append("',");

        sb.append("'");
        sb.append(notificacionDeEmisora.getLastPrice() == null ? "1903.0" : notificacionDeEmisora
            .getLastPrice());
        sb.append("',");

        sb.append("'");
        sb.append(notificacionDeEmisora.getLastTime() == null ? "20171026-12:54:50.439"
            : notificacionDeEmisora.getLastTime());
        sb.append("',");

        sb.append("'");
        sb.append(notificacionDeEmisora.getTradedVolume() == null ? "338" : notificacionDeEmisora
            .getTradedVolume());
        sb.append("',");

        sb.append("'");
        sb.append(notificacionDeEmisora.getTradedAmount() == null ? "639867.98"
            : notificacionDeEmisora.getTradedAmount());
        sb.append("',");

        sb.append("'");
        sb.append(notificacionDeEmisora.getAveragePriceDay() == null ? "1890.461538"
            : notificacionDeEmisora.getAveragePriceDay());
        sb.append("',");

        sb.append("'");
        sb.append(notificacionDeEmisora.getOpeningPrice() == null ? "1879.0"
            : notificacionDeEmisora.getOpeningPrice());
        sb.append("',");

        sb.append("'");
        sb.append(notificacionDeEmisora.getMaxPriceOfTheDay() == null ? "1903.0"
            : notificacionDeEmisora.getMaxPriceOfTheDay());
        sb.append("',");

        sb.append("'");
        sb.append(notificacionDeEmisora.getMaxTime() == null ? "20171026-12:54:50.439"
            : notificacionDeEmisora.getMaxTime());
        sb.append("',");

        sb.append("'");
        sb.append(notificacionDeEmisora.getLowPriceOfTheDay() == null ? "1875.0"
            : notificacionDeEmisora.getLowPriceOfTheDay());
        sb.append("',");

        sb.append("'");
        sb.append(notificacionDeEmisora.getLowTime() == null ? "20171026-09:19:58.743"
            : notificacionDeEmisora.getLowTime());
        sb.append("',");
        try {

          Double total =
              (notificacionDeEmisora.getOpeningPrice() - notificacionDeEmisora.getLastPrice())
                  / notificacionDeEmisora.getLastPrice();
          total = total * 100;
          sb.append("'" + total + "',");
        } catch (Exception e) {
          sb.append("'0.0'").append(",");
        }
        sb.append("CURRENT_TIMESTAMP); ");
        sb.append("ELSE ");
        sb.append("UPDATE PRODSQL.BIVA_TRMUHECH_RH SET ");
        sb.append("LAST_PRICE=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getLastPrice() == null ? "1903.0" : notificacionDeEmisora
            .getLastPrice());
        sb.append("',");

        sb.append("LAST_TIME=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getLastTime() == null ? "20171026-12:54:50.439"
            : notificacionDeEmisora.getLastTime());
        sb.append("',");

        sb.append("TRADE_QUANTITY=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getTradedVolume() == null ? "338" : notificacionDeEmisora
            .getTradedVolume());
        sb.append("',");

        sb.append("TRADE_VALUE=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getTradedAmount() == null ? "639867.98"
            : notificacionDeEmisora.getTradedAmount());
        sb.append("',");

        sb.append("TRADE_AVERAGE=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getAveragePriceDay() == null ? "1890.461538"
            : notificacionDeEmisora.getAveragePriceDay());
        sb.append("',");

        sb.append("CLOSE_PRICE=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getOpeningPrice() == null ? "1879.0"
            : notificacionDeEmisora.getOpeningPrice());
        sb.append("',");

        sb.append("MAXIMUM_PRICE=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getMaxPriceOfTheDay() == null ? "1903.0"
            : notificacionDeEmisora.getMaxPriceOfTheDay());
        sb.append("',");

        sb.append("MAXIMUM_TIME=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getMaxTime() == null ? "20171026-12:54:50.439"
            : notificacionDeEmisora.getMaxTime());
        sb.append("',");

        sb.append("MINIMUM_PRICE=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getLowPriceOfTheDay() == null ? "1875.0"
            : notificacionDeEmisora.getLowPriceOfTheDay());
        sb.append("',");

        sb.append("MINIMUM_TIME=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getLowTime() == null ? "20171026-09:19:58.743"
            : notificacionDeEmisora.getLowTime());
        sb.append("',");

        sb.append("PERCENTAGE_CHANGE=");
        if ((notificacionDeEmisora.getOpeningPrice() != null && notificacionDeEmisora
            .getOpeningPrice() != 0)
            && (notificacionDeEmisora.getLastPrice() != null && notificacionDeEmisora
                .getLastPrice() != 0)
            && (notificacionDeEmisora.getLastPrice() != null && notificacionDeEmisora
                .getLastPrice() != 0)) {
          sb.append(
              "'"
                  + ((notificacionDeEmisora.getOpeningPrice() - notificacionDeEmisora
                      .getLastPrice()) / notificacionDeEmisora.getLastPrice()) * 100 + "'").append(
              ",");
        } else {
          sb.append("'0.0'").append(",");
        }
        sb.append("HORACAMBIO=CURRENT_TIMESTAMP ");
        sb.append("WHERE ISIN=");
        sb.append("'");
        sb.append(notificacionDeEmisora.getIsin() + "'; ");
        sb.append("END IF; ");
        sb.append("END ");
        insert.sendBody(sb.toString());
      }

    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see mx.com.actinver.biva.persistencia.PersistenciaBaseDeDatos#
   * registraEnCorroCompra(mx.com.actinver.terminal.notificaciones.Emisora)
   */
  @Override
  public void registraEnCorroCompra(Emisora notificacionDeEmisora) throws ErrorDeBaseDeDatos {

    if (notificacionDeEmisora == null) {
      throw new ErrorDeBaseDeDatos(ERROR_NOTIF_EMISORA_NULA);
    } else {

      LOGGER.debug(MENSAJE_INSERT_ITCH_CORROCOMPRA);
      StringBuilder miString = new StringBuilder();
      miString.append(INSERT_ITCH_CORROCOMPRA);

      if (notificacionDeEmisora.getInstrumentNumber() != null) {
        miString.append("'");
        miString.append(notificacionDeEmisora.getInstrumentNumber() + "'").append(",");
      } else {
        miString.append("'2038'").append(",");
      }
      if (notificacionDeEmisora.getBidVolume() != null) {
        miString.append("'");
        miString.append(notificacionDeEmisora.getBidVolume() + "'").append(",");
      } else {
        miString.append("'10000'").append(",");
      }
      if (notificacionDeEmisora.getBestBid() != null) {
        miString.append("'" + notificacionDeEmisora.getBestBid() + "'").append(",");
      } else {
        miString.append("'1864.41'").append(",");
      }
      miString.append("CURRENT_TIMESTAMP)");
      insert.sendBody(miString.toString());

    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see mx.com.actinver.biva.persistencia.PersistenciaBaseDeDatos#
   * registraEnCorroVenta(mx.com.actinver.terminal.notificaciones.Emisora)
   */
  @Override
  public void registraEnCorroVenta(Emisora notificacionDeEmisora) throws ErrorDeBaseDeDatos {
    if (notificacionDeEmisora == null) {
      throw new ErrorDeBaseDeDatos(ERROR_NOTIF_EMISORA_NULA);
    } else {

      LOGGER.debug(MENSAJE_INSERT_ITCH_CORROVENTA);
      StringBuilder miString = new StringBuilder();
      miString.append(INSERT_ITCH_CORROVENTA);

      if (notificacionDeEmisora.getInstrumentNumber() != null) {
        miString.append("'");
        miString.append(notificacionDeEmisora.getInstrumentNumber() + "'").append(",");
      } else {
        miString.append("'2038'").append(",");
      }
      if (notificacionDeEmisora.getOfferVolume() != null) {
        miString.append("'");
        miString.append(notificacionDeEmisora.getOfferVolume() + "'").append(",");
      } else {
        miString.append("'10000'").append(",");
      }
      if (notificacionDeEmisora.getBestOffer() != null) {
        miString.append("'" + notificacionDeEmisora.getBestOffer() + "'").append(",");
      } else {
        miString.append("'1864.41'").append(",");
      }
      miString.append("CURRENT_TIMESTAMP)");
      insert.sendBody(miString.toString());

    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see mx.com.actinver.biva.persistencia.PersistenciaBaseDeDatos#registraEnGrupo (mx.
   * com.biva.model.R)
   */
  @Override
  public void registraEnGrupo(R mensajeR) throws ErrorDeBaseDeDatos {

    if (mensajeR == null) {
      throw new ErrorDeBaseDeDatos(ERROR_MENSAJE_R);
    } else {
      if (mensajeR.getOrderBook() != null) {
        LOGGER.debug(MENSAJE_INSERT_ITCHGPO);
        StringBuilder miString = new StringBuilder();

        miString.append("BEGIN ATOMIC ");
        miString.append("IF NOT EXISTS (SELECT 1 FROM PRODSQL.ITCH_GRUPO_RH WHERE ORDERBOOK=");
        miString.append("'" + mensajeR.getOrderBook() + "') ");
        miString.append("THEN ");
        miString.append(INSERT_ITCH_GRUPO);
        miString.append("'");
        miString.append(mensajeR.getGroup() == null ? 0 : mensajeR.getGroup() + "'").append(",");
        miString.append("'");
        miString.append(mensajeR.getOrderBook() == null ? 2038 : mensajeR.getOrderBook() + "'")
            .append(",");
        miString.append("'");
        miString.append(mensajeR.getIsin() == null ? "*" : mensajeR.getIsin() + "'").append(",");
        miString.append("'");
        miString.append(mensajeR.getSecCode() == null ? " " : mensajeR.getSecCode() + "'").append(
            ",");
        miString.append("'");
        miString.append(mensajeR.getCurrency() == null ? " " : mensajeR.getCurrency() + "'")
            .append(",");
        miString.append("'");
        miString.append(
            mensajeR.getQuantityTickSizeTableId() == null ? 0 : mensajeR
                .getQuantityTickSizeTableId() + "'").append(",");
        miString.append("'");
        miString.append(
            mensajeR.getPriceTickSizeTableId() == null ? 0 : mensajeR.getPriceTickSizeTableId()
                + "'").append(",");
        miString.append("'");
        miString
            .append(mensajeR.getPriceDecimals() == null ? 0 : mensajeR.getPriceDecimals() + "'")
            .append(",");
        miString.append("CURRENT_TIMESTAMP); ");
        miString.append("ELSE ");
        miString.append("UPDATE PRODSQL.ITCH_GRUPO_RH SET ");
        miString.append("GRUPO_ID=");
        miString.append("'");
        miString.append(mensajeR.getGroup() == null ? 0 : mensajeR.getGroup() + "'").append(","); // que
                                                                                                  // pongo
                                                                                                  // el
        // el default
        miString.append("ISIN=");
        miString.append("'");
        miString.append(mensajeR.getIsin() == null ? "*" : mensajeR.getIsin() + "'").append(",");
        miString.append("SYMBOL=");
        miString.append("'");
        miString.append(mensajeR.getSecCode() == null ? " " : mensajeR.getSecCode() + "'")
            .append(",");
        miString.append("MONEDA=");
        miString.append("'");
        miString.append(mensajeR.getCurrency() == null ? " " : mensajeR.getCurrency() + "'")
            .append(",");
        miString.append("TABLEID_Q=");
        miString.append("'");
        miString.append(
            mensajeR.getQuantityTickSizeTableId() == null ? 0 : mensajeR
                .getQuantityTickSizeTableId() + "'").append(",");
        miString.append("TABLEID_P=");
        miString.append("'");
        miString.append(
            mensajeR.getPriceTickSizeTableId() == null ? 0 : mensajeR.getPriceTickSizeTableId()
                + "'").append(",");
        miString.append("PRICE_DECIMALS=");
        miString.append("'");
        miString
            .append(mensajeR.getPriceDecimals() == null ? 0 : mensajeR.getPriceDecimals() + "'")
            .append(",");
        miString.append("HORAORIGEN=CURRENT_TIMESTAMP ");
        miString.append("WHERE ORDERBOOK=");
        miString.append("'" + mensajeR.getOrderBook() + "'; ");
        miString.append("END IF; ");
        miString.append("END ");
        insert.sendBody(miString.toString());
      }

    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see mx.com.actinver.biva.persistencia.PersistenciaBaseDeDatos#registraEnTables (mx
   * .com.biva.model.L, mx.com.biva.model.M)
   */
  @Override
  public void registraEnTables(L mensajeL, M mensajeM) throws ErrorDeBaseDeDatos {

    if (mensajeL == null && mensajeM == null) {
      throw new ErrorDeBaseDeDatos(ERROR_MENSAJE_LM);
    } else {

      LOGGER.debug(MENSAJE_INSERT_ITCHTABLES);
      StringBuilder miString = new StringBuilder();

      if (mensajeL != null) {
        miString.append("BEGIN ATOMIC ");
        miString.append("IF NOT EXISTS (SELECT 1 FROM PRODSQL.ITCH_TABLES_RH WHERE TABLE_ID=");
        miString.append("'");
        miString.append(mensajeL.getTickSizeTableId() + "' AND TICK_SIZE="
            + mensajeL.getTickSizeP() + " AND INICIA=" + mensajeL.getPriceStart() + ")");
        miString.append("THEN ");
        miString.append(INSERT_ITCH_TABLES);
        miString.append("'");
        miString.append(mensajeL.getTickSizeTableId() == null ? 0 : mensajeL.getTickSizeTableId())
            .append("',");
        miString.append("'");
        miString.append(mensajeL.getTickSizeP() == null ? 0 : mensajeL.getTickSizeP()).append("',");
        miString.append("'");
        miString.append(mensajeL.getPriceStart() == null ? 0 : mensajeL.getPriceStart()).append(
            "',");
        miString.append("CURRENT_TIMESTAMP); ");
        miString.append("ELSE ");
        miString.append("UPDATE PRODSQL.ITCH_TABLES_RH SET ");
        miString.append("TABLE_ID=");
        miString.append("'");
        miString.append(mensajeL.getTickSizeTableId() == null ? 0 : mensajeL.getTickSizeTableId())
            .append("',");
        miString.append("TICK_SIZE=");
        miString.append("'");
        miString.append(mensajeL.getTickSizeP() == null ? 0 : mensajeL.getTickSizeP()).append("',");
        miString.append("INICIA=");
        miString.append("'");
        miString.append(mensajeL.getPriceStart() == null ? 0 : mensajeL.getPriceStart()).append(
            "',");
        miString.append("HORAORIGEN=CURRENT_TIMESTAMP ");
        miString.append("WHERE TABLE_ID=");
        miString.append("'");
        miString.append(mensajeL.getTickSizeTableId() + "' AND TICK_SIZE='"
            + mensajeL.getTickSizeP() + "' AND INICIA=" + mensajeL.getPriceStart() + ";");
        miString.append("END IF; ");
        miString.append("END ");
      }
      if (mensajeM != null) {

        miString.append("BEGIN ATOMIC ");
        miString.append("IF NOT EXISTS (SELECT 1 FROM PRODSQL.ITCH_TABLES_RH WHERE TABLE_ID=");
        miString.append("'");
        miString.append(mensajeM.getTickSizeTableId() + "' AND TICK_SIZE="
            + mensajeM.getTickSizeQ() + " AND INICIA=" + mensajeM.getQuantityStart() + ")");
        miString.append("THEN ");
        miString.append(INSERT_ITCH_TABLES);
        miString.append("'");
        miString.append(
            mensajeM.getTickSizeTableId() == null ? 0 : mensajeM.getTickSizeTableId() + "'")
            .append(",");
        miString.append("'");
        miString.append(mensajeM.getTickSizeQ() == null ? 0 : mensajeM.getTickSizeQ() + "'")
            .append(",");
        miString.append("'");
        miString
            .append(mensajeM.getQuantityStart() == null ? 0 : mensajeM.getQuantityStart() + "'")
            .append(",");
        miString.append("CURRENT_TIMESTAMP); ");
        miString.append("ELSE ");
        miString.append("UPDATE PRODSQL.ITCH_TABLES_RH SET ");
        miString.append("TABLE_ID=");
        miString.append("'");
        miString.append(
            mensajeM.getTickSizeTableId() == null ? 0 : mensajeM.getTickSizeTableId() + "'")
            .append(",");
        miString.append("TICK_SIZE=");
        miString.append("'");
        miString.append(mensajeM.getTickSizeQ() == null ? 0 : mensajeM.getTickSizeQ() + "'")
            .append(",");
        miString.append("INICIA=");
        miString.append("'");
        miString
            .append(mensajeM.getQuantityStart() == null ? 0 : mensajeM.getQuantityStart() + "'")
            .append(",");
        miString.append("HORAORIGEN=CURRENT_TIMESTAMP ");
        miString.append("WHERE TABLE_ID=");
        miString.append("'");
        miString.append(mensajeM.getTickSizeTableId() + "' AND TICK_SIZE='"
            + mensajeM.getTickSizeQ() + "' AND INICIA=" + mensajeM.getQuantityStart() + ";");
        miString.append("END IF; ");
        miString.append("END ");
      }
      insert.sendBody(miString.toString());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see mx.com.actinver.biva.persistencia.PersistenciaBaseDeDatos#registraEnUhecho (mx
   * .com.biva.model.E, mx.com.biva.model.C, mx.com.biva.model.P)
   */
  @Override
  public void registraEnUhecho(E mensajeE, C mensajeC, P mensajeP) throws Exception {

    Orden ord = new Orden();
    String em = null;
    if (mensajeE == null && mensajeC == null && mensajeP == null) {
      throw new ErrorDeBaseDeDatos(ERROR_MENSAJE_ECP);
    } else {
      LOGGER.debug(MENSAJE_INSERT_UHECHO);
      StringBuilder miString = new StringBuilder();

      miString.append(INSERT_ITCH_UHECHO);
      if (mensajeE != null) {
        em = mensajeE.getOrderNumber().toString();
        ord = almacen.buscarOrden(mensajeE.getOrderNumber());

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
        ord = almacen.buscarOrden(mensajeC.getOrderNumber());

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
      insert.sendBody(miString.toString());

    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see mx.com.actinver.biva.persistencia.PersistenciaBaseDeDatos#
   * registraEnEstadoInstrumento(mx.com.biva.model.H)
   */
  @Override
  public void registraEnEstadoInstrumento(H mensajeH) throws ErrorDeBaseDeDatos {

    if (mensajeH == null) {
      throw new ErrorDeBaseDeDatos(ERROR_MENSAJE_H);
    } else {
      if (mensajeH.getOrderBook() != null) {
        LOGGER.debug(MENSAJE_INSERT_BIVA_EDO_INST);
        StringBuilder miString = new StringBuilder();
        miString.append("BEGIN ATOMIC ");
        miString
            .append("IF NOT EXISTS (SELECT 1 FROM PRODSQL.BIVA_ESTADO_INSTRUMENTO_RH WHERE NUMERO_INSTRUMENTO=");
        miString.append("'" + mensajeH.getOrderBook() + "') ");
        miString.append("THEN ");
        miString.append(INSERT_BIVA_ESTADO_INST);
        miString.append("'");
        miString.append(mensajeH.getOrderBook() == null ? 0 : mensajeH.getOrderBook() + "'")
            .append(",");
        miString.append("'");
        miString.append(mensajeH.getTradingState() + "'").append(",");
        miString.append("CURRENT_TIMESTAMP); ");
        miString.append("ELSE ");
        miString.append("UPDATE PRODSQL.BIVA_ESTADO_INSTRUMENTO_RH SET ");
        miString.append("NUMERO_INSTRUMENTO=");
        miString.append("'");
        miString.append(mensajeH.getOrderBook() == null ? 0 : mensajeH.getOrderBook() + "'")
            .append(",");
        miString.append("ESTATUS=");
        miString.append("'");
        miString.append(mensajeH.getTradingState() + "'").append(",");
        miString.append("HORAORIGEN=CURRENT_TIMESTAMP ");
        miString.append("WHERE NUMERO_INSTRUMENTO=");
        miString.append("'" + mensajeH.getOrderBook() + "'; ");
        miString.append("END IF; ");
        miString.append("END ");
        insert.sendBody(miString.toString());
      }
    }
  }

  public Memoria getMemoria() {
    return memoria;
  }

  public void setMemoria(Memoria memoria) {
    this.memoria = memoria;
  }

  public Almacen getAlmacen() {
    return almacen;
  }

  public void setAlmacen(Almacen almacen) {
    this.almacen = almacen;
  }

}
