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

public class X extends Message {

	final static Logger logger = LoggerFactory.getLogger(X.class);
	private Integer orderBook = Integer.valueOf(0);
	private Integer referencePrice = Integer.valueOf(0);
	private char priceType;
	private char reason;
	
	public X() {}

	public X(ArrayList<String> elements) {
		super(elements);
		try {
			this.orderBook = Integer.valueOf(Integer.parseInt(elements.get(2)));
			this.referencePrice = Integer.valueOf(elements.get(3));
			this.reason = Character.valueOf(elements.get(4).charAt(0));
		} catch (Exception ex) {
			logger.error("Error: ", ex);
		}
	}

	public Integer getOrderBook() {
		return orderBook;
	}

	public void setOrderBook(Integer orderBook) {
		this.orderBook = orderBook;
	}

	public Integer getReferencePrice() {
		return referencePrice;
	}

	public void setReferencePrice(Integer referencePrice) {
		this.referencePrice = referencePrice;
	}

	public char getPriceType() {
		return priceType;
	}

	public void setPriceType(char priceType) {
		this.priceType = priceType;
	}

	public char getReason() {
		return reason;
	}

	public void setReason(char reason) {
		this.reason = reason;
	}

}
