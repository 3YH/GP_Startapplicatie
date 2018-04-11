package controller;

import model.PrIS;
import model.RoosterData;
import model.klas.Klas;
import model.persoon.Student;
import server.Conversation;
import server.Handler;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;

/**
 * Created by universal on 4/11/2018.
 */
public class DetailsLadenController implements Handler {

    private PrIS infoSys;
    public DetailsLadenController(PrIS infoSys) {
        this.infoSys = infoSys;
    }

    public void handle(Conversation conversation) {
        if (conversation.getRequestedURI().startsWith("/rooster/details")) {
            ophalenDetails(conversation);
        }
    }

    private void ophalenDetails(Conversation conversation) {
        JsonObjectBuilder lJsonObjectBuilder = Json.createObjectBuilder();
        //Deze volgorde mag niet worden gewijzigd i.v.m. JS. (Hier mag dus ook geen andere JSON voor komen.)
        //lJsonObjectBuilder.add("eerste_lesdatum", PrIS.calToStandaardDatumString(lEersteLesDatum)).add("laatste_lesdatum", PrIS.calToStandaardDatumString(lLaatsteLesDatum));

        JsonObject lJsonObjIn = (JsonObject) conversation.getRequestBodyAsJSON();

        if (lJsonObjIn != null && lJsonObjIn.containsKey("rol") && lJsonObjIn.getString("rol").equals("student")) {
            String lNummer = lJsonObjIn.getString("leerlingnummer");                        // Uitlezen van meegestuurde leerlingnummer
            String lWachtwoord = lJsonObjIn.getString("password");                               //uitlezen van MD5 pass.

            String start = lJsonObjIn.getString("start");
            String end = lJsonObjIn.getString("end");
            String title = lJsonObjIn.getString("title");

            Student s = this.infoSys.getStudent(Integer.parseInt(lNummer));
            if(s==null)
            {
                lJsonObjectBuilder.add("rol", "undefined"); //HA. FAKE LOGIN busted...
            }
            else if(s.komtWachtwoordOvereen(lWachtwoord))
            {
                ArrayList<RoosterData> rooster = this.infoSys.getRoosterData(s.getKlasCode());
                for(RoosterData data : rooster)
                {
                    String data_title = data.getSubject();
                    String start_datetime = data.getDate()+"T"+ data.getStarttime()+":00";
                    String end_datetime = data.getDate()+"T"+ data.getEndtime()+":00";

                    if(data_title.equals(title) && start_datetime.equals(start) && end_datetime.equals(end))
                    {
                        lJsonObjectBuilder.add("cursuscode", data.getGroup_code()); //good login
                        lJsonObjectBuilder.add("lokaal", data.getLocation());
                        lJsonObjectBuilder.add("datum", data.getDateNL());
                        lJsonObjectBuilder.add("starttijd", data.getStarttime());
                        lJsonObjectBuilder.add("eindtijd", data.getEndtime());
                        lJsonObjectBuilder.add("docentemail", data.getTeacher_email());
                        lJsonObjectBuilder.add("klas", s.getKlasnaam());
                    }

                }
            }
            String lJsonOut = lJsonObjectBuilder.build().toString();
            conversation.sendJSONMessage(lJsonOut);										// terugsturen naar de Polymer-GUI!	}
        }

    }
}
