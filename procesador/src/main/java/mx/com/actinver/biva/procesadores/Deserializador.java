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
package mx.com.actinver.biva.procesadores;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import com.fasterxml.jackson.databind.ObjectMapper;

import mx.com.biva.itch.modelo.*;

public class Deserializador implements Processor {
	
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void process(Exchange exchange) throws Exception {
		String entrada = exchange.getIn().getBody(String.class);
		Integer indice = entrada.indexOf("\"type\":\"");
		Character tipo = entrada.charAt(indice + 8);
		switch (tipo) {
			case 'A':
				A mensajeA = this.objectMapper.readValue(entrada, A.class);
				exchange.getIn().setBody(mensajeA);
				break;
			case 'B':
				B mensajeB = this.objectMapper.readValue(entrada, B.class);
				exchange.getIn().setBody(mensajeB);
				break;
			case 'C':
				C mensajeC = this.objectMapper.readValue(entrada, C.class);
				exchange.getIn().setBody(mensajeC);
				break;
			case 'D':
				D mensajeD = this.objectMapper.readValue(entrada, D.class);
				exchange.getIn().setBody(mensajeD);
				break;
			case 'E':
				E mensajeE = this.objectMapper.readValue(entrada, E.class);
				exchange.getIn().setBody(mensajeE);
				break;
			case 'F':
				F mensajeF = this.objectMapper.readValue(entrada, F.class);
				exchange.getIn().setBody(mensajeF);
				break;
			case 'G':
				G mensajeG = this.objectMapper.readValue(entrada, G.class);
				exchange.getIn().setBody(mensajeG);
				break;
			case 'H':
				H mensajeH = this.objectMapper.readValue(entrada, H.class);
				exchange.getIn().setBody(mensajeH);
				break;
			case 'I':
				I mensajeI = this.objectMapper.readValue(entrada, I.class);
				exchange.getIn().setBody(mensajeI);
				break;
			case 'L':
				L mensajeL = this.objectMapper.readValue(entrada, L.class);
				exchange.getIn().setBody(mensajeL);
				break;
			case 'M':
				M mensajeM = this.objectMapper.readValue(entrada, M.class);
				exchange.getIn().setBody(mensajeM);
				break;
			case 'N':
				N mensajeN = this.objectMapper.readValue(entrada, N.class);
				exchange.getIn().setBody(mensajeN);
				break;
			case 'P':
				P mensajeP = this.objectMapper.readValue(entrada, P.class);
				exchange.getIn().setBody(mensajeP);
				break;
			case 'Q':
				Q mensajeQ = this.objectMapper.readValue(entrada, Q.class);
				exchange.getIn().setBody(mensajeQ);
				break;
			case 'R':
				R mensajeR = this.objectMapper.readValue(entrada, R.class);
				exchange.getIn().setBody(mensajeR);
				break;
			case 'S':
				S mensajeS = this.objectMapper.readValue(entrada, S.class);
				exchange.getIn().setBody(mensajeS);
				break;
			case 'T':
				T mensajeT = this.objectMapper.readValue(entrada, T.class);
				exchange.getIn().setBody(mensajeT);
				break;
			case 'U':
				U mensajeU = this.objectMapper.readValue(entrada, U.class);
				exchange.getIn().setBody(mensajeU);
				break;
			case 'X':
				X mensajeX = this.objectMapper.readValue(entrada, X.class);
				exchange.getIn().setBody(mensajeX);
				break;
		}
	}
}