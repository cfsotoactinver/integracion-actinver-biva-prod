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

public class P extends Message {

	final static Logger logger = LoggerFactory.getLogger(P.class);

	private Long executedQuantity = Long.valueOf(0L);
	private Long matchNumber = Long.valueOf(0L);
	private Integer orderBook = Integer.valueOf(0);
	private Integer executionPrice = Integer.valueOf(0);
	private Float executionPriceF = Float.valueOf(0.0F);
	private Integer priceDecimals = Integer.valueOf(0);
	private char printable;
	private char tradeIndicator;
	
	public P() {}

	public P(ArrayList<String> elements) {
		super(elements);
		try {
			this.executedQuantity = Long.valueOf(Long.parseLong(elements.get(2)));
			this.orderBook = Integer.valueOf(Integer.parseInt(elements.get(3)));
			this.printable = elements.get(4).charAt(0);
			this.executionPrice = Integer.valueOf(Integer.parseInt(elements.get(5)));
			this.matchNumber = Long.valueOf(Long.parseLong(elements.get(6)));
			this.tradeIndicator = elements.get(7).charAt(0);
		} catch (Exception ex) {
			logger.error("Error: ", ex);
		}
	}

	public Long getExecutedQuantity() {
		return executedQuantity;
	}

	public void setExecutedQuantity(Long executedQuantity) {
		this.executedQuantity = executedQuantity;
	}

	public Long getMatchNumber() {
		return matchNumber;
	}

	public void setMatchNumber(Long matchNumber) {
		this.matchNumber = matchNumber;
	}

	public Integer getOrderBook() {
		return orderBook;
	}

	public void setOrderBook(Integer orderBook) {
		this.orderBook = orderBook;
	}

	public Integer getExecutionPrice() {
		return executionPrice;
	}

	public void setExecutionPrice(Integer executionPrice) {
		this.executionPrice = executionPrice;
	}

	public Float getExecutionPriceF() {
		return executionPriceF;
	}

	public void setExecutionPriceF(Float executionPriceF) {
		this.executionPriceF = executionPriceF;
	}

	public Integer getPriceDecimals() {
		return priceDecimals;
	}

	public void setPriceDecimals(Integer priceDecimals) {
		this.priceDecimals = priceDecimals;
	}

	public char getPrintable() {
		return printable;
	}

	public void setPrintable(char printable) {
		this.printable = printable;
	}

	public char getTradeIndicator() {
		return tradeIndicator;
	}

	public void setTradeIndicator(char tradeIndicator) {
		this.tradeIndicator = tradeIndicator;
	}

}
