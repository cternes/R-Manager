package de.slackspace.rmanager.gameengine;


public class RManagerGameEngineTest {

//	@Test
//	public void whenTakingTurnMustDeserializeDataCorrectly() {
//		RManagerGameEngine cut = new RManagerGameEngine();
//		
//		byte[] gameState = cut.startNewGame("p1", "p2");
//		
//		List<GameAction> actions = new ArrayList<>();
//		actions.add(new BuyEstateAction("abc"));
//		actions.add(new BuyEstateAction("def"));
//		byte[] turnData = serialize(actions);
//		
//		cut.makeTurn(gameState, turnData, "p1");
//	}
//	
//	private byte[] serialize(List<GameAction> data) {
//		try {
//			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//			
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.writeValue(outputStream, data);
//			
//			return outputStream.toByteArray();
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
}
