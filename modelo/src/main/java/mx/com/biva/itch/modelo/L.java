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

public class L extends Message {

	final static Logger logger = LoggerFactory.getLogger(L.class);
	private Integer tickSizeTableId = Integer.valueOf(0);
	private Integer tickSizeP = Integer.valueOf(0);
	private Integer priceStart = Integer.valueOf(0);
	
	public L(){}

	public L(ArrayList<String> elements) {
		super(elements);
		try {
			this.tickSizeTableId = Integer.valueOf(Integer.parseInt(elements.get(2)));
			this.tickSizeP = Integer.valueOf(Integer.parseInt(elements.get(3)));
			this.priceStart = Integer.valueOf(Integer.parseInt(elements.get(4)));
		} catch (Exception ex) {
			logger.error("Error: ", ex);
		}
	}

	public Integer getTickSizeTableId() {
		return tickSizeTableId;
	}

	public void setTickSizeTableId(Integer tickSizeTableId) {
		this.tickSizeTableId = tickSizeTableId;
	}

	public Integer getTickSizeP() {
		return tickSizeP;
	}

	public void setTickSizeP(Integer tickSizeP) {
		this.tickSizeP = tickSizeP;
	}

	public Integer getPriceStart() {
		return priceStart;
	}

	public void setPriceStart(Integer priceStart) {
		this.priceStart = priceStart;
	}

}
