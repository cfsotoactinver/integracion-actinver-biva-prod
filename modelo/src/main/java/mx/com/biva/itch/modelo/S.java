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

public class S extends Message {
	final static Logger logger = LoggerFactory.getLogger(S.class);

	private Integer orderBook = Integer.valueOf(0);
	private String group;
	private Character eventCode;
	
	public S() {}

	public S(ArrayList<String> elements) {
		super(elements);
		try {
			this.group = elements.get(2);
			this.eventCode = elements.get(3).charAt(0);
			this.orderBook = Integer.valueOf(Integer.parseInt(elements.get(4)));
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

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Character getEventCode() {
		return eventCode;
	}

	public void setEventCode(Character eventCode) {
		this.eventCode = eventCode;
	}

}
