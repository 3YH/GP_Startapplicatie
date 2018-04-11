package model;

/**
 * Created by universal on 4/10/2018.
 */
public class RoosterData {
    private String date;
    private String starttime;
    private String endtime;
    private String subject;
    private String teacher_email;
    private String location;
    private String group_code;

    public RoosterData(String parse) throws Exception {
        String[] input_strings = parse.split(","); //Comma delimited file (CSV)
        if(input_strings.length == 7) {
            date = input_strings[0];
            starttime = input_strings[1];
            endtime = input_strings[2];
            subject = input_strings[3];
            teacher_email = input_strings[4];
            location = input_strings[5];
            group_code = input_strings[6];
        }
        else
        {
            throw new Exception("Parse exception");
        }

    }
    public RoosterData(String date, String starttime, String endtime, String subject, String teacher_email, String location, String group_code) {
        this.date = date;
        this.starttime = starttime;
        this.endtime = endtime;
        this.subject = subject;
        this.teacher_email = teacher_email;
        this.location = location;
        this.group_code = group_code;
    }

    public String getDate() {
        String returndate = "";
        String[] dateparts = this.date.split("-");
        returndate += dateparts[2];                                             //jaar
        if(dateparts[1].length() == 1) dateparts[1] = "0" + dateparts[1];       //maand
        if(dateparts[0].length() == 1) dateparts[0] = "0" + dateparts[0];       //dag
        returndate += "-" + dateparts[1] + "-";
        returndate += dateparts[0];
        return returndate;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getSubject() {
        return subject;
    }

    public String getTeacher_email() {
        return teacher_email;
    }

    public String getLocation() {
        return location;
    }

    public String getGroup_code() {
        return group_code;
    }
}
