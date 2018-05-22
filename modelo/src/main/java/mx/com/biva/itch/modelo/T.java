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

public class T extends Message {

	final static Logger logger = LoggerFactory.getLogger(T.class);
	private String hhmmss;
	private Integer second;
	
	public T() {}

	public T(ArrayList<String> elements) {
		super(elements);
		try {
			this.setType(Character.valueOf(elements.get(0).charAt(0)));
			this.setTimestamp(Integer.valueOf(elements.get(1)));
			this.second = (Integer.valueOf(elements.get(1)));
		} catch (Exception ex) {
			logger.error("Error: ", ex);
		}
	}

	public String getHhmmss() {
		return hhmmss;
	}

	public void setHhmmss(String hhmmss) {
		this.hhmmss = hhmmss;
	}

	public Integer getSecond() {
		return second;
	}

	public void setSecond(Integer second) {
		this.second = second;
	}

}
