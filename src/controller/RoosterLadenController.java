package controller;

import model.PrIS;
import server.Conversation;
import server.Handler;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.util.Calendar;

/**
 * Created by universal on 4/10/2018.
 */
public class RoosterLadenController  implements Handler {

    /**
     * De SysteemDatumController klasse moet alle systeem (en test)-gerelateerde aanvragen
     * afhandelen. Methode handle() kijkt welke URI is opgevraagd en laat
     * dan de juiste methode het werk doen. Je kunt voor elke nieuwe URI
     * een nieuwe methode schrijven.
     *
     * @param infoSys - het toegangspunt tot het domeinmodel
     */
    public RoosterLadenController(PrIS infoSys) {
    }

    public void handle(Conversation conversation) {
        if (conversation.getRequestedURI().startsWith("/rooster/ophalen")) {
            ophalenRooster(conversation);
        }
    }

    private void ophalenRooster(Conversation conversation) {


        JsonObjectBuilder lJsonObjectBuilder = Json.createObjectBuilder();
        //Deze volgorde mag niet worden gewijzigd i.v.m. JS. (Hier mag dus ook geen andere JSON voor komen.)
        //lJsonObjectBuilder.add("eerste_lesdatum", PrIS.calToStandaardDatumString(lEersteLesDatum)).add("laatste_lesdatum", PrIS.calToStandaardDatumString(lLaatsteLesDatum));

        String lJsonOut = lJsonObjectBuilder.build().toString();

        conversation.sendJSONMessage(lJsonOut);										// terugsturen naar de Polymer-GUI!	}
    }
}

