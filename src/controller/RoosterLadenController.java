package controller;

import model.PrIS;
import model.RoosterData;
import model.persoon.Student;
import server.Conversation;
import server.Handler;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
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
    private PrIS infoSys;
    public RoosterLadenController(PrIS infoSys) {
        this.infoSys = infoSys;
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

        JsonObject lJsonObjIn = (JsonObject) conversation.getRequestBodyAsJSON();

        if(lJsonObjIn != null && lJsonObjIn.containsKey("rol") && lJsonObjIn.getString("rol").equals("student"))
        {
            String lNummer = lJsonObjIn.getString("leerlingnummer");						// Uitlezen van meegestuurde leerlingnummer
            String lWachtwoord = lJsonObjIn.getString("password");                               //uitlezen van MD5 pass.

            Student s = this.infoSys.getStudent(Integer.parseInt(lNummer));
            if(s==null)
            {
                lJsonObjectBuilder.add("rol", "undefined"); //HA. FAKE LOGIN busted...
            }
            else if(s.komtWachtwoordOvereen(lWachtwoord))
            {
                //GELDIGE VERIFICATIE :D
                ArrayList<RoosterData> rooster = this.infoSys.getRoosterData(s.getKlasCode());
                System.out.println("TODO: Rooster terugsturen in valid json :D (verification succesful!)");
                lJsonObjectBuilder.add("rol", "student"); //HA. FAKE LOGIN busted...
            }
            else
            {
                lJsonObjectBuilder.add("rol", "undefined");     //login incorrect
            }
        }
        else
        {
            System.out.println("Je stuurt geen student door!");
        }




        String lJsonOut = lJsonObjectBuilder.build().toString();
        conversation.sendJSONMessage(lJsonOut);										// terugsturen naar de Polymer-GUI!	}
    }
}

