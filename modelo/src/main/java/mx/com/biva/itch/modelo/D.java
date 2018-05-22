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

public class D extends Message {

	final static Logger logger = LoggerFactory.getLogger(D.class);

	private Long orderNumber = Long.valueOf(0L);
	
	public D() {}

	public D(ArrayList<String> elements) {
		super(elements);
		try {
			this.orderNumber = Long.valueOf(Long.parseLong(elements.get(2)));
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
}
