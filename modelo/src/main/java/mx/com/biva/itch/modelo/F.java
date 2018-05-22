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

public class F extends Message {

	final static Logger logger = LoggerFactory.getLogger(F.class);

	private Integer participantId = Integer.valueOf(0);
	private String participantCode;
	
	public F(){}
	
	public F(ArrayList<String> elements) {
		super(elements);
		try {
			this.participantId = Integer.valueOf(Integer.parseInt(elements.get(2)));
			this.participantCode = elements.get(3);
		} catch (Exception ex) {
			logger.error("Error: ", ex);
		}
	}

	public Integer getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Integer participantId) {
		this.participantId = participantId;
	}

	public String getParticipantCode() {
		return participantCode;
	}

	public void setParticipantCode(String participantCode) {
		this.participantCode = participantCode;
	}

}
