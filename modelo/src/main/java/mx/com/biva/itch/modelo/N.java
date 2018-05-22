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

public class N extends Message {
	final static Logger logger = LoggerFactory.getLogger(N.class);

	private Long sequenceNumber = Long.valueOf(0L);

	private Integer orderBook = Integer.valueOf(0);
	private Integer newsId = Integer.valueOf(0);
	private Integer participantId = Integer.valueOf(0);
	private String title;
	private String reference;
	private String newsText;
	
	public N() {}

	public N(ArrayList<String> elements) {
		super(elements);
		try {
			this.orderBook = Integer.valueOf(Integer.parseInt(elements.get(2)));
			this.newsId = Integer.valueOf(Integer.parseInt(elements.get(3)));
			this.participantId = Integer.valueOf(Integer.parseInt(elements.get(4)));
			this.title = elements.get(5);
			this.reference = elements.get(6);
			this.newsText = elements.get(7);

		} catch (Exception ex) {
			logger.error("Error: ", ex);
		}
	}

	public Long getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Integer getOrderBook() {
		return orderBook;
	}

	public void setOrderBook(Integer orderBook) {
		this.orderBook = orderBook;
	}

	public Integer getNewsId() {
		return newsId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public Integer getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Integer participantId) {
		this.participantId = participantId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getNewsText() {
		return newsText;
	}

	public void setNewsText(String newsText) {
		this.newsText = newsText;
	}

}
