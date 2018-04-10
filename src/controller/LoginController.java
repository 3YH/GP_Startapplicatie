package controller;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.PrIS;
import server.Conversation;
import server.Handler;

import java.util.Map;

class LoginController implements Handler {
	private PrIS informatieSysteem;
	
	/**
	 * De LoginController klasse moet alle algemene aanvragen afhandelen. 
	 * Methode handle() kijkt welke URI is opgevraagd en laat dan de juiste 
	 * methode het werk doen. Je kunt voor elke nieuwe URI een nieuwe methode 
	 * schrijven.
	 * 
	 * @param infoSys - het toegangspunt tot het domeinmodel
	 */
	public LoginController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}
	
	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/login")) {
			login(conversation);
		}
	}
	
	/**
	 * Deze methode haalt eerst de opgestuurde JSON-data op. Daarna worden
	 * de benodigde gegevens uit het domeinmodel gehaald. Deze gegevens worden
	 * dan weer omgezet naar JSON en teruggestuurd naar de Polymer-GUI!
	 * 
	 * @param conversation - alle informatie over het request
	 */
	private void login(Conversation conversation) {
		JsonObject lJsonObjIn = (JsonObject) conversation.getRequestBodyAsJSON();
		
		String lGebruikersnaam = lJsonObjIn.getString("username");						// Uitlezen van opgestuurde inloggegevens... 
		String lWachtwoord = lJsonObjIn.getString("password");
		Map<String, String> loginInfo = informatieSysteem.loginDetails(lGebruikersnaam,lWachtwoord);

		if(loginInfo.isEmpty())
		{
			//LOGIN FAILED !
			conversation.sendJSONMessage("{\"rol\":\"undefined\"}");
		}
		else
		{
			JsonObjectBuilder lJsonObjectBuilder = Json.createObjectBuilder();
			lJsonObjectBuilder.add("rol", loginInfo.get("rol"));
			lJsonObjectBuilder.add("voornaam", loginInfo.get("voornaam"));
			lJsonObjectBuilder.add("achternaam", loginInfo.get("achternaam"));
			lJsonObjectBuilder.add("identificatienummer", loginInfo.get("identificatienummer"));		// en teruggekregen gebruikersrol als JSON-object...
			if(loginInfo.containsKey("group")) lJsonObjectBuilder.add("group", loginInfo.get("group"));
			if(loginInfo.containsKey("klasnaam")) lJsonObjectBuilder.add("klasnaam", loginInfo.get("klasnaam"));
			if(loginInfo.containsKey("klascode")) lJsonObjectBuilder.add("klascode", loginInfo.get("klascode"));
			String lJsonOut = lJsonObjectBuilder.build().toString();
			conversation.sendJSONMessage(lJsonOut);
		}
													// terugsturen naar de Polymer-GUI!
	}
}