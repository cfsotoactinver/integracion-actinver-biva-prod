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

public class U extends Message {

	final static Logger logger = LoggerFactory.getLogger(U.class);
	private Integer price = Integer.valueOf(0);
	private Long originalOrderNumber = Long.valueOf(0L);
	private Long newOrderNumber = Long.valueOf(0L);
	private Long quantity = Long.valueOf(0L);
	private Float priceF = Float.valueOf(0.0F);
	
	public U(){}

	public U(ArrayList<String> elements) {
		super(elements);
		try {
			this.originalOrderNumber = Long.valueOf(Long.parseLong(elements.get(2)));
			this.newOrderNumber = Long.valueOf(Long.parseLong(elements.get(3)));
			this.quantity = Long.valueOf(Long.parseLong(elements.get(4)));
			this.price = Integer.valueOf(Integer.parseInt(elements.get(5)));
		} catch (Exception ex) {
			logger.error("Error: ", ex);
		}
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}



	public Long getOriginalOrderNumber() {
		return originalOrderNumber;
	}

	public void setOriginalOrderNumber(Long originalOrderNumber) {
		this.originalOrderNumber = originalOrderNumber;
	}

	public Long getNewOrderNumber() {
		return newOrderNumber;
	}

	public void setNewOrderNumber(Long newOrderNumber) {
		this.newOrderNumber = newOrderNumber;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Float getPriceF() {
		return priceF;
	}

	public void setPriceF(Float priceF) {
		this.priceF = priceF;
	}

}
