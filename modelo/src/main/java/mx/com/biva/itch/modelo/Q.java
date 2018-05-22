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

public class Q extends Message {
	final static Logger logger = LoggerFactory.getLogger(Q.class);

	private Integer orderBook = Integer.valueOf(0);
	private Integer bestBid = Integer.valueOf(0);
	private Float bestBidF = Float.valueOf(0.0F);
	private Long bestBidSize = Long.valueOf(0L);
	private Integer bestOffer = Integer.valueOf(0);
	private Float bestOfferF = Float.valueOf(0.0F);
	private Long bestOfferSize = Long.valueOf(0L);
	private Integer priceDecimals = Integer.valueOf(0);
	
	public Q() {}

	public Q(ArrayList<String> elements) {
		super(elements);
		try {
			this.orderBook = Integer.valueOf(Integer.parseInt(elements.get(2)));
			this.bestBidF = Float.valueOf(Float.parseFloat(elements.get(3)));
			this.bestBidSize = Long.valueOf(Long.parseLong(elements.get(4)));
			this.bestOfferF = Float.valueOf(Float.parseFloat(elements.get(5)));
			this.bestOfferSize = Long.valueOf(Long.parseLong(elements.get(6)));
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

	public Integer getBestBid() {
		return bestBid;
	}

	public void setBestBid(Integer bestBid) {
		this.bestBid = bestBid;
	}

	public Float getBestBidF() {
		return bestBidF;
	}

	public void setBestBidF(Float bestBidF) {
		this.bestBidF = bestBidF;
	}

	public Long getBestBidSize() {
		return bestBidSize;
	}

	public void setBestBidSize(Long bestBidSize) {
		this.bestBidSize = bestBidSize;
	}

	public Integer getBestOffer() {
		return bestOffer;
	}

	public void setBestOffer(Integer bestOffer) {
		this.bestOffer = bestOffer;
	}

	public Float getBestOfferF() {
		return bestOfferF;
	}

	public void setBestOfferF(Float bestOfferF) {
		this.bestOfferF = bestOfferF;
	}

	public Long getBestOfferSize() {
		return bestOfferSize;
	}

	public void setBestOfferSize(Long bestOfferSize) {
		this.bestOfferSize = bestOfferSize;
	}

	public Integer getPriceDecimals() {
		return priceDecimals;
	}

	public void setPriceDecimals(Integer priceDecimals) {
		this.priceDecimals = priceDecimals;
	}

}
