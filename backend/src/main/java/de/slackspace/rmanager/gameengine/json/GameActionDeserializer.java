package de.slackspace.rmanager.gameengine.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.slackspace.rmanager.gameengine.action.GameAction;
import de.slackspace.rmanager.gameengine.exception.GameException;

public class GameActionDeserializer extends JsonDeserializer<List<GameAction>> {

	private Map<Integer, Class<? extends GameAction>> availableClazzes = new HashMap<Integer, Class<? extends GameAction>>();
	
	protected GameActionDeserializer() {
	}
	
	public GameActionDeserializer(HashMap<Integer, Class<? extends GameAction>> clazzMap) {
		availableClazzes = clazzMap;
	}

	@Override
	public List<GameAction> deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
		JsonNode node = parser.getCodec().readTree(parser);
		
        Iterator<JsonNode> iter = node.elements();
        List<GameAction> list = new ArrayList<>();
        
        // iterate through all objects and map to concrete type
        while(iter.hasNext()) {
        	JsonNode element = iter.next();
        	int type = (Integer) element.get("type").numberValue();
        	
        	Class<? extends GameAction> concreteClazz = availableClazzes.get(type);
        	
        	if(concreteClazz == null) {
        		throw new GameException("Could not find a deserializer for type " + type);
        	}
        	
        	// deserialize to concrete object
    		ObjectMapper mapper = new ObjectMapper();
    		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    		JavaType javaType = mapper.getTypeFactory().constructType(concreteClazz);
    		list.add((GameAction) mapper.readValue(element.traverse(), javaType));
        }
		
		return list;
	}

}
