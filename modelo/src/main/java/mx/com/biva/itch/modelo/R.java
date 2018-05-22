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
package mx.com.biva.itch.modelo;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class R extends Message {

	final static Logger logger = LoggerFactory.getLogger(R.class);

	private Integer quantityTickSizeTableId = Integer.valueOf(0);
	private Integer orderBook = Integer.valueOf(0);
	private Integer quantityDecimals = Integer.valueOf(0);
	private Integer priceTickSizeTableId = Integer.valueOf(0);
	private Integer priceDecimals = Integer.valueOf(0);
	private Integer deslistingMaturityDate = Integer.valueOf(0);
	private Integer deslistingTime = Integer.valueOf(0);
	private String isin;
	private String secCode;
	private String currency;
	private String group;
	private String quotationBasis;
	private String instrument;
	private Long minimumQuantity = Long.valueOf(0L);
	private char turnoverRatio;
	private String listingType;
	private String listingExchange  ;

	public R() {}

	public R(ArrayList<String> elements) {
		super(elements);
		try {
			this.orderBook = Integer.valueOf(Integer.parseInt(elements.get(2)));
			this.isin = elements.get(3);
			this.secCode = elements.get(4);
			this.currency = elements.get(5);
			this.group = elements.get(6);
			this.minimumQuantity = Long.valueOf(Long.parseLong(elements.get(7)));
			this.quantityTickSizeTableId = Integer.valueOf(Integer.parseInt(elements.get(8)));
			this.quantityDecimals = Integer.valueOf(Integer.parseInt(elements.get(9)));
			this.priceTickSizeTableId = Integer.valueOf(Integer.parseInt(elements.get(10)));
			this.priceDecimals = Integer.valueOf(Integer.parseInt(elements.get(11)));
			this.deslistingMaturityDate = Integer.valueOf(Integer.parseInt(elements.get(12)));
			this.deslistingTime = Integer.valueOf(Integer.parseInt(elements.get(13)));
			this.turnoverRatio = elements.get(14).charAt(0);
			this.quotationBasis = elements.get(15);
			this.instrument = elements.get(16);
			this.listingType=elements.get(17);
			this.listingExchange=elements.get(18);
		} catch (Exception ex) {
			logger.error("Error: ", ex);
		}
	}

	public Integer getQuantityTickSizeTableId() {
		return quantityTickSizeTableId;
	}

	public void setQuantityTickSizeTableId(Integer quantityTickSizeTableId) {
		this.quantityTickSizeTableId = quantityTickSizeTableId;
	}

	public Integer getOrderBook() {
		return orderBook;
	}

	public void setOrderBook(Integer orderBook) {
		this.orderBook = orderBook;
	}

	public Integer getQuantityDecimals() {
		return quantityDecimals;
	}

	public void setQuantityDecimals(Integer quantityDecimals) {
		this.quantityDecimals = quantityDecimals;
	}

	public Integer getPriceTickSizeTableId() {
		return priceTickSizeTableId;
	}

	public void setPriceTickSizeTableId(Integer priceTickSizeTableId) {
		this.priceTickSizeTableId = priceTickSizeTableId;
	}

	public Integer getPriceDecimals() {
		return priceDecimals;
	}

	public void setPriceDecimals(Integer priceDecimals) {
		this.priceDecimals = priceDecimals;
	}

	public Integer getDeslistingMaturityDate() {
		return deslistingMaturityDate;
	}

	public void setDeslistingMaturityDate(Integer deslistingMaturityDate) {
		this.deslistingMaturityDate = deslistingMaturityDate;
	}

	public Integer getDeslistingTime() {
		return deslistingTime;
	}

	public void setDeslistingTime(Integer deslistingTime) {
		this.deslistingTime = deslistingTime;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getSecCode() {
		return secCode;
	}

	public void setSecCode(String secCode) {
		this.secCode = secCode;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getQuotationBasis() {
		return quotationBasis;
	}

	public void setQuotationBasis(String quotationBasis) {
		this.quotationBasis = quotationBasis;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public Long getMinimumQuantity() {
		return minimumQuantity;
	}

	public void setMinimumQuantity(Long minimumQuantity) {
		this.minimumQuantity = minimumQuantity;
	}

	public char getTurnoverRatio() {
		return turnoverRatio;
	}

	public void setTurnoverRatio(char turnoverRatio) {
		this.turnoverRatio = turnoverRatio;
	}

	public String getListingExchange() {
		return listingExchange;
	}

	public void setListingExchange(String listingExchange) {
		this.listingExchange = listingExchange;
	}

	public String getListingType() {
		return listingType;
	}

	public void setListingType(String listingType) {
		this.listingType = listingType;
	}



}
