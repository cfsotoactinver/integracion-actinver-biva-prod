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

public class C extends Message{

	final static Logger logger = LoggerFactory.getLogger(C.class);

	private Integer executionPrice = Integer.valueOf(0);
	private Integer priceDecimals = Integer.valueOf(0);
	private Long orderNumber = Long.valueOf(0L);
	private Long executedQuantity = Long.valueOf(0L);
	private Long matchNumber = Long.valueOf(0L);
	private Character tradeIndicator;
	private char printable;
	private Float executionPriceF = Float.valueOf(0.0F);
	
	public C() {}

	public C(ArrayList<String> elements) {
		super(elements);
		try {
			this.orderNumber = Long.valueOf(Long.parseLong(elements.get(2)));
			this.executedQuantity = Long.valueOf(Long.parseLong(elements.get(3)));
			this.matchNumber = Long.valueOf(Long.parseLong(elements.get(4)));
			this.tradeIndicator = Character.valueOf(elements.get(5).charAt(0));
			this.printable = elements.get(6).charAt(0);
			this.executionPrice = Integer.valueOf(Integer.parseInt(elements.get(7)));
		} catch (Exception ex) {
			logger.error("Error: ", ex);
		}
	}

	public Integer getExecutionPrice() {
		return executionPrice;
	}

	public void setExecutionPrice(Integer executionPrice) {
		this.executionPrice = executionPrice;
	}

	public Integer getPriceDecimals() {
		return priceDecimals;
	}

	public void setPriceDecimals(Integer priceDecimals) {
		this.priceDecimals = priceDecimals;
	}

	public Long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Long getMatchNumber() {
		return matchNumber;
	}

	public void setMatchNumber(Long matchNumber) {
		this.matchNumber = matchNumber;
	}

	public Character getTradeIndicator() {
		return tradeIndicator;
	}

	public void setTradeIndicator(Character tradeIndicator) {
		this.tradeIndicator = tradeIndicator;
	}

	public char getPrintable() {
		return printable;
	}

	public void setPrintable(char printable) {
		this.printable = printable;
	}

	public Float getExecutionPriceF() {
		return executionPriceF;
	}

	public void setExecutionPriceF(Float executionPriceF) {
		this.executionPriceF = executionPriceF;
	}

	public Long getExecutedQuantity() {
		return executedQuantity;
	}

	public void setExecutedQuantity(Long executedQuantity) {
		this.executedQuantity = executedQuantity;
	}

}
