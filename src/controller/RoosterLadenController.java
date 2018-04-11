package controller;

import model.PrIS;
import model.RoosterData;
import model.persoon.Docent;
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
                String roosterdata = "[";

                for(int i=0; i < rooster.size();i++)
                {
                    RoosterData d = rooster.get(i);
                    roosterdata += "{\"title\" : \""+d.getSubject()+"\", \"start\" : \""+d.getDate()+"T"+ d.getStarttime()+ ":00\", \"end\" : \""+d.getDate()+"T"+ d.getEndtime()+":00\"}";
                    if(rooster.size()-1 != i)
                    {
                        roosterdata += ",";
                    }
                    else
                    {
                        roosterdata += "]";
                    }
                }

                lJsonObjectBuilder.add("rol", "student"); //good login
                lJsonObjectBuilder.add("events", roosterdata);
            }
            else
            {
                System.out.println("Password mismatch: " + lWachtwoord + " " + s.getWachtwoord() + " for user: " + lNummer);
                lJsonObjectBuilder.add("rol", "undefined");     //login incorrect
            }
        }
        else if(lJsonObjIn != null && lJsonObjIn.containsKey("rol") && lJsonObjIn.getString("rol").equals("docent"))
        {
            System.out.println("Je stuurt een docent door!");
            String username = lJsonObjIn.getString("username");
            Docent d = this.infoSys.getDocent(username);
            if(d != null)
            {

                String roosterdata = "[";
                ArrayList<RoosterData> rooster = this.infoSys.getRoosterDataDocent(d.getGebruikersnaam());
                for(int i=0; i < rooster.size();i++)
                {
                    RoosterData roosterData = rooster.get(i);
                    roosterdata += "{\"title\" : \""+roosterData.getSubject()+"\", \"start\" : \""+roosterData.getDate()+"T"+ roosterData.getStarttime()+ ":00\", \"end\" : \""+roosterData.getDate()+"T"+ roosterData.getEndtime()+":00\"}";
                    if(rooster.size()-1 != i)
                    {
                        roosterdata += ",";
                    }
                }
                roosterdata += "]"; //als docent zijn rooster leeg is (Goed aangeleverde csv bestanden @hu !! #not)
                lJsonObjectBuilder.add("rol", "docent"); //good login
                lJsonObjectBuilder.add("events", roosterdata);

            }
            else
            {
                lJsonObjectBuilder.add("rol", "undefined");     //login correct, docent not found.
            }
        }
        else
        {
            lJsonObjectBuilder.add("rol", "undefined");     //login incorrect
        }




        String lJsonOut = lJsonObjectBuilder.build().toString();
        conversation.sendJSONMessage(lJsonOut);										// terugsturen naar de Polymer-GUI!	}
    }
}

