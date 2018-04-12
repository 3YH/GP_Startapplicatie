package controller;

import model.PrIS;
import model.persoon.Docent;
import model.persoon.Student;
import server.Conversation;
import server.Handler;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Created by universal on 4/12/2018.
 */
public class AbsentieAanpassenController implements Handler
{
    private PrIS infoSys;

    public AbsentieAanpassenController(PrIS infoSys) {
            this.infoSys = infoSys;
        }

    public void handle(Conversation conversation) {
        if (conversation.getRequestedURI().startsWith("/absentie/aanpassen")) {
            absentieAanpassen(conversation);
        }
    }

    private void absentieAanpassen(Conversation conversation) {
        JsonObjectBuilder lJsonObjectBuilder = Json.createObjectBuilder();
        //Deze volgorde mag niet worden gewijzigd i.v.m. JS. (Hier mag dus ook geen andere JSON voor komen.)
        //lJsonObjectBuilder.add("eerste_lesdatum", PrIS.calToStandaardDatumString(lEersteLesDatum)).add("laatste_lesdatum", PrIS.calToStandaardDatumString(lLaatsteLesDatum));

        JsonObject lJsonObjIn = (JsonObject) conversation.getRequestBodyAsJSON();

        if (lJsonObjIn != null && lJsonObjIn.containsKey("rol") && lJsonObjIn.getString("rol").equals("docent"))
        {
            String email = lJsonObjIn.getString("username");                        // Uitlezen van meegestuurde leerlingnummer
            String lWachtwoord = lJsonObjIn.getString("password");                               //uitlezen van MD5 pass.

            String naam = lJsonObjIn.getString("naam");
            String waarde = lJsonObjIn.getString("waarde");

            Docent d = this.infoSys.getDocent(email);

            if(d!=null && d.komtWachtwoordOvereen(lWachtwoord))
            {
                Student s = this.infoSys.getStudent(naam.replaceFirst(" ",".").replace(" ","").toLowerCase()+"@student.hu.nl");
                s.setStatus(waarde);
            }
            else
            {
                lJsonObjectBuilder.add("rol", "undefined"); //HA. FAKE LOGIN busted...
            }
            String lJsonOut = lJsonObjectBuilder.build().toString();
            conversation.sendJSONMessage(lJsonOut);
        }
    }

}
