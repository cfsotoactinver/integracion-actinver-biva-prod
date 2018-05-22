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

public class A extends Message{

	final static Logger logger = LoggerFactory.getLogger(A.class);

	private Long orderNumber = Long.valueOf(0L);
	private Character orderVerb;
	private Long quantity = Long.valueOf(0L);
	private Integer orderBook = Integer.valueOf(0);
	private Integer price = Integer.valueOf(0);
	
	public A() {}

	public A(ArrayList<String> elements) {
		super(elements);
		try {
			this.orderNumber = Long.valueOf(Long.parseLong(elements.get(2)));
			this.orderVerb = Character.valueOf(elements.get(3).charAt(0));
			this.quantity = Long.valueOf(Long.parseLong(elements.get(4)));
			this.orderBook = Integer.valueOf(Integer.parseInt(elements.get(5)));
			this.price = Integer.valueOf(Integer.parseInt(elements.get(6)));
		} catch (Exception ex) {
			logger.error("Error: ", ex);
		}
	}

	
	public Long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Character getOrderVerb() {
		return orderVerb;
	}

	public void setOrderVerb(Character orderVerb) {
		this.orderVerb = orderVerb;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Integer getOrderBook() {
		return orderBook;
	}

	public void setOrderBook(Integer orderBook) {
		this.orderBook = orderBook;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}	

}