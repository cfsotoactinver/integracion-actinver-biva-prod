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
package mx.com.biva.itch;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

public final class LectorDeConfiguracionYaml {

	final static Logger LOGGER = LoggerFactory.getLogger(LectorDeConfiguracionYaml.class);
	
	private final Map<Object, Object> yMap;
	private Map<Object, Object> fMap;
	private Map<Object, Object> mMap;
	private final Yaml yaml;

	@SuppressWarnings("unchecked")
	public LectorDeConfiguracionYaml(InputStream filename) throws FileNotFoundException {
		yaml = new Yaml();
		Object y = yaml.load(filename);
		yMap = (Map<Object, Object>) y;
		buildFormats();
		buildMessages();
	}

	@SuppressWarnings("unchecked")
	public void buildFormats() {
		fMap = new HashMap<>();
		ArrayList<Object> fArray = (ArrayList<Object>) yMap.get("formats");
		for (int i = 0; i < fArray.size(); ++i) {
			Map<Object, Object> tempMap = (Map<Object, Object>) (fArray.get(i));
			fMap.put(tempMap.keySet().toArray()[0], tempMap.values().toArray()[0]);
		}
	}

	@SuppressWarnings("unchecked")
	public void buildMessages() {
		mMap = (Map<Object, Object>) yMap.get("messages");
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Object> getFields(String mType) {
		Map<Object, Object> tempMap = (Map<Object, Object>) mMap.get(mType);
		assert tempMap != null : "File type missing: " + mType;
		return (ArrayList<Object>) tempMap.get("fields");
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Object> getFormat(String field) {
		Object fieldVal = fMap.get(field);
		assert fieldVal != null : "Format field missing: " + field;
		return (ArrayList<Object>) fieldVal;
	}

	@SuppressWarnings("unchecked")
	public String getFieldName(String mType) {
		
		Map<Object, Object> tempMap = (Map<Object, Object>) mMap.get(mType);
		return tempMap.get("name").toString();
	}

}