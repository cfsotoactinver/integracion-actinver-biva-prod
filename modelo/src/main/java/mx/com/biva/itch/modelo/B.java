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

public class B extends Message {
	final static Logger logger = LoggerFactory.getLogger(B.class);

	private Long matchNumber = Long.valueOf(0L);
	private Character reason;
	
	public B() {}

	public B(ArrayList<String> elements) {
		super(elements);
		try {
			this.matchNumber = Long.valueOf(Long.parseLong(elements.get(2)));
			this.reason = Character.valueOf(elements.get(3).charAt(0));
		} catch (Exception ex) {
			logger.error("Error: ", ex);
		}
	}

	public Long getMatchNumber() {
		return matchNumber;
	}

	public void setMatchNumber(Long matchNumber) {
		this.matchNumber = matchNumber;
	}

	public Character getReason() {
		return reason;
	}

	public void setReason(Character reason) {
		this.reason = reason;
	}

}
