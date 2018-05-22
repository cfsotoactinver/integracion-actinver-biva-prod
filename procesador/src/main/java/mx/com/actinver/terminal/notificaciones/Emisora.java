/**
 * Copyright 2018 Actinver
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package mx.com.actinver.terminal.notificaciones;

public class Emisora  {
	
	private String issuer; //DIS
	private String series; //*
	private Integer instrumentNumber; //2038
	private String isin; //US2546871060
	private Long bidVolume; //10000
	private Double bestBid; //1864.41
	private String bidTime; //20171026-13:08:48.399
	private Double bestOffer; //1909.99
	private Long offerVolume; //20000
	private String offerTime; //20171026-13:08:48.641
	private Double lastPrice; //1903.0
	private Long lastVolume; //50
	private String lastTime; //20171026-12:54:50.439
	private Double previousSettlementPrice; //1902.0
	private Double openingPrice; //1879.0
	private Double maxPriceOfTheDay; //1903.0
	private String maxTime; //20171026-12:54:50.439
	private Double lowPriceOfTheDay; //1875.0
	private String lowTime; //20171026-09:19:58.743
	private Double lastPriceOfTheDay; //1903.0
	private Long tradedVolume; //338
	private Double tradedAmount; //639867.98
	private String averagePriceDay; //1890.461538
	private String lastUpdateTime; //20171026-13:08:48.641
	private String instrumentStatus; //N
	private Long volumenAcumulado;
	private Double precioAcumulado;
	private String postura;


	private static final String PIPE = "|";
	
	public String topipes() {
		StringBuilder resultado = new StringBuilder();
		resultado.append(issuer == null ? "" : issuer);
		resultado.append(PIPE);
		resultado.append(series == null ? "" : series);
		resultado.append(PIPE);
		resultado.append(instrumentNumber == null ? "" : instrumentNumber);
		resultado.append(PIPE);
		resultado.append(isin == null ? "" : isin);
		resultado.append(PIPE);
		resultado.append(bidVolume == null ? "0" : bidVolume);
		resultado.append(PIPE);
		resultado.append(bestBid == null ? "0" : bestBid);
		resultado.append(PIPE);
		resultado.append(bidTime == null ? "19691231-18:00:00.000" : bidTime);
		resultado.append(PIPE);
		resultado.append(offerVolume == null ? "0" : offerVolume);
		resultado.append(PIPE);
		resultado.append(bestOffer == null ? "0" : bestOffer);
		resultado.append(PIPE);
		resultado.append(offerTime == null ? "19691231-18:00:00.000" : offerTime);
		resultado.append(PIPE);
		resultado.append(lastPrice == null ? "0" : lastPrice);
		resultado.append(PIPE);
		resultado.append(lastVolume == null ? "0" : lastVolume);
		resultado.append(PIPE);
		resultado.append(lastTime == null ? "19691231-18:00:00.000" : lastTime);
		resultado.append(PIPE);
		resultado.append(previousSettlementPrice == null ? "0" : previousSettlementPrice);
		resultado.append(PIPE);
		resultado.append(openingPrice == null ? "0" : openingPrice);
		resultado.append(PIPE);
		resultado.append(maxPriceOfTheDay == null ? "0" : maxPriceOfTheDay);
		resultado.append(PIPE);
		resultado.append(maxTime == null ? "19691231-18:00:00.000" : maxTime);
		resultado.append(PIPE);
		resultado.append(lowPriceOfTheDay == null ? "0" : lowPriceOfTheDay);
		resultado.append(PIPE);
		resultado.append(lowTime == null ? "19691231-18:00:00.000" : lowTime);
		resultado.append(PIPE);
		resultado.append(lastPriceOfTheDay == null ? "0" : lastPriceOfTheDay);
		resultado.append(PIPE);
		resultado.append(tradedVolume == null ? "0" : tradedVolume);
		resultado.append(PIPE);
		resultado.append(tradedAmount == null ? "0" : tradedAmount);
		resultado.append(PIPE);
		resultado.append(averagePriceDay == null ? "0" : averagePriceDay);
		resultado.append(PIPE);
		resultado.append(lastUpdateTime == null ? "19691231-18:00:00.000" : lastUpdateTime);
		resultado.append(PIPE);
		resultado.append(instrumentStatus == null ? "N" : instrumentStatus);
		return resultado.toString();
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public Integer getInstrumentNumber() {
		return instrumentNumber;
	}

	public void setInstrumentNumber(Integer instrumentNumber) {
		this.instrumentNumber = instrumentNumber;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}



	public Long getBidVolume() {
	    return bidVolume;
	}

	public void setBidVolume(Long bidVolume) {
	    this.bidVolume = bidVolume;
	}

	public Double getBestBid() {
		return bestBid;
	}

	public void setBestBid(Double bestBid) {
		this.bestBid = bestBid;
	}

	public String getBidTime() {
		return bidTime;
	}

	public void setBidTime(String bidTime) {
		this.bidTime = bidTime;
	}

	public Double getBestOffer() {
		return bestOffer;
	}

	public void setBestOffer(Double bestOffer) {
		this.bestOffer = bestOffer;
	}


	public String getOfferTime() {
		return offerTime;
	}

	public void setOfferTime(String offerTime) {
		this.offerTime = offerTime;
	}

	public Double getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(Double lastPrice) {
		this.lastPrice = lastPrice;
	}


	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public Double getPreviousSettlementPrice() {
		return previousSettlementPrice;
	}

	public void setPreviousSettlementPrice(Double previousSettlementPrice) {
		this.previousSettlementPrice = previousSettlementPrice;
	}

	public Double getOpeningPrice() {
		return openingPrice;
	}

	public void setOpeningPrice(Double openingPrice) {
		this.openingPrice = openingPrice;
	}

	public Double getMaxPriceOfTheDay() {
		return maxPriceOfTheDay;
	}

	public void setMaxPriceOfTheDay(Double maxPriceOfTheDay) {
		this.maxPriceOfTheDay = maxPriceOfTheDay;
	}

	public String getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(String maxTime) {
		this.maxTime = maxTime;
	}

	public Double getLowPriceOfTheDay() {
		return lowPriceOfTheDay;
	}

	public void setLowPriceOfTheDay(Double lowPriceOfTheDay) {
		this.lowPriceOfTheDay = lowPriceOfTheDay;
	}

	public String getLowTime() {
		return lowTime;
	}

	public void setLowTime(String lowTime) {
		this.lowTime = lowTime;
	}

	public Double getLastPriceOfTheDay() {
		return lastPriceOfTheDay;
	}

	public void setLastPriceOfTheDay(Double lastPriceOfTheDay) {
		this.lastPriceOfTheDay = lastPriceOfTheDay;
	}

	public Long getTradedVolume() {
		return tradedVolume;
	}

	public void setTradedVolume(Long tradedVolume) {
		this.tradedVolume = tradedVolume;
	}

	public Double getTradedAmount() {
		return tradedAmount;
	}

	public void setTradedAmount(Double tradedAmount) {
		this.tradedAmount = tradedAmount;
	}

	public String getAveragePriceDay() {
		return averagePriceDay;
	}

	public void setAveragePriceDay(String averagePriceDay) {
		this.averagePriceDay = averagePriceDay;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Double getPrecioAcumulado() {
		return precioAcumulado;
	}

	public void setPrecioAcumulado(Double precioAcumulado) {
		this.precioAcumulado = precioAcumulado;
	}

	public Long getOfferVolume() {
	    return offerVolume;
	}

	public void setOfferVolume(Long offerVolume) {
	    this.offerVolume = offerVolume;
	}

	public Long getLastVolume() {
	    return lastVolume;
	}

	public void setLastVolume(Long lastVolume) {
	    this.lastVolume = lastVolume;
	}

	public Long getVolumenAcumulado() {
	    return volumenAcumulado;
	}

	public void setVolumenAcumulado(Long volumenAcumulado) {
	    this.volumenAcumulado = volumenAcumulado;
	}

	public String getInstrumentStatus() {
	    return instrumentStatus;
	}

	public void setInstrumentStatus(String instrumentStatus) {
	    this.instrumentStatus = instrumentStatus;
	}

	public String getPostura() {
		return postura;
	}

	public void setPostura(String postura) {
		this.postura = postura;
	}
		
}