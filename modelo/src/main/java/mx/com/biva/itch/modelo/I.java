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

public class I extends Message {

	final static Logger logger = LoggerFactory.getLogger(I.class);

	private Long theoreticalOpeningQuantity = Long.valueOf(0L);
	private Integer orderBook = Integer.valueOf(0);
	private Integer bestBid = Integer.valueOf(0);
	private Integer bestOffer = Integer.valueOf(0);
	private Integer theoreticalOpeningPrice = Integer.valueOf(0);
	private char crossType;
	
	public I(){}
	
	public I(ArrayList<String> elements) {
		super(elements);
		try {
			this.theoreticalOpeningQuantity = Long.valueOf(Long.parseLong(elements.get(2)));
			this.orderBook = Integer.valueOf(Integer.parseInt(elements.get(3)));
			this.bestBid = Integer.valueOf(Integer.parseInt(elements.get(4)));
			this.bestOffer = Integer.valueOf(Integer.parseInt(elements.get(5)));
			this.theoreticalOpeningPrice = Integer.valueOf(Integer.parseInt(elements.get(6)));
			this.crossType = Character.valueOf(elements.get(7).charAt(0));
		} catch (Exception ex) {
			logger.error("Error: ", ex);
		}
	}

	public Long getTheoreticalOpeningQuantity() {
		return theoreticalOpeningQuantity;
	}

	public void setTheoreticalOpeningQuantity(Long theoreticalOpeningQuantity) {
		this.theoreticalOpeningQuantity = theoreticalOpeningQuantity;
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

	public Integer getBestOffer() {
		return bestOffer;
	}

	public void setBestOffer(Integer bestOffer) {
		this.bestOffer = bestOffer;
	}

	public Integer getTheoreticalOpeningPrice() {
		return theoreticalOpeningPrice;
	}

	public void setTheoreticalOpeningPrice(Integer theoreticalOpeningPrice) {
		this.theoreticalOpeningPrice = theoreticalOpeningPrice;
	}

	public char getCrossType() {
		return crossType;
	}

	public void setCrossType(char crossType) {
		this.crossType = crossType;
	}

}
