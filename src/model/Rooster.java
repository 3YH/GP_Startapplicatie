package model;

import java.util.ArrayList;

/**
 * Created by universal on 4/10/2018.
 */
public class Rooster
{
    private ArrayList<RoosterData> rooster = new ArrayList<RoosterData>();

    public Rooster()
    {
        //import data later.
    }

    public void importData(String rawData) throws Exception {
        String[] data = rawData.split("\n");
        for(String s : data)
        {
            rooster.add(new RoosterData(s));
        }
    }

    public ArrayList<RoosterData> getRoosterForKlasCode(String klascode)
    {
        ArrayList<RoosterData> data = new ArrayList<>();
        for(RoosterData d : rooster)
        {
            if(d.getGroup_code().equals(klascode))
            {
                data.add(d);
            }
        }
        return data;
    }

    public ArrayList<RoosterData> getRoosterForTeacher(String mail)
    {
        ArrayList<RoosterData> data = new ArrayList<>();
        for(RoosterData d : rooster)
        {
            if(d.getTeacher_email().equals(mail))
            {
                data.add(d);
            }
        }
        return data;
    }

    public void addItem(String date, String starttime, String endtime, String subject, String teacher_email, String location, String group_code)
    {
        rooster.add(new RoosterData(date, starttime, endtime, subject, teacher_email, location, group_code));
    }

    public Rooster(String rawData) throws Exception
    {
        String[] data = rawData.split("\n");
        for(String s : data)
        {
            rooster.add(new RoosterData(s));
        }
    }

    public ArrayList<RoosterData> getData()
    {
        return this.rooster;
    }


}