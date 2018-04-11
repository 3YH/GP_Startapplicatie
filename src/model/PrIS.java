package model;

import java.util.ArrayList;

import model.klas.Klas;
import model.persoon.Docent;
import model.persoon.Student;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PrIS {
	private ArrayList<Docent> deDocenten;
	private ArrayList<Student> deStudenten;
	private ArrayList<Klas> deKlassen;
	private Rooster rooster;
	
	/**
	 * De constructor maakt een set met standaard-data aan. Deze data
	 * moet nog uitgebreidt worden met rooster gegevens die uit een bestand worden
	 * ingelezen, maar dat is geen onderdeel van deze demo-applicatie!
	 * 
	 * De klasse PrIS (PresentieInformatieSysteem) heeft nu een meervoudige
	 * associatie met de klassen Docent, Student, Vakken en Klassen Uiteraard kan dit nog veel
	 * verder uitgebreid en aangepast worden! 
	 * 
	 * De klasse fungeert min of meer als ingangspunt voor het domeinmodel. Op
	 * dit moment zijn de volgende methoden aanroepbaar:
	 * 
	 * String login(String gebruikersnaam, String wachtwoord)
	 * Docent getDocent(String gebruikersnaam)
	 * Student getStudent(String gebruikersnaam)
	 * ArrayList<Student> getStudentenVanKlas(String klasCode)
	 * 
	 * Methode login geeft de rol van de gebruiker die probeert in te loggen,
	 * dat kan 'student', 'docent' of 'undefined' zijn! Die informatie kan gebruikt 
	 * worden om in de Polymer-GUI te bepalen wat het volgende scherm is dat getoond 
	 * moet worden.
	 * 
	 */
	public PrIS() {
		deDocenten = new ArrayList<Docent>();
		deStudenten = new ArrayList<Student>();
		deKlassen = new ArrayList<Klas>();


		// Inladen klassen
		vulKlassen(deKlassen);

		// Inladen studenten in klassen
		vulStudenten(deStudenten, deKlassen);

		// Inladen docenten
		vulDocenten(deDocenten);

		vulRooster();
	
	} //Einde Pris constructor
	
	//deze method is static onderdeel van PrIS omdat hij als hulp methode 
	//in veel controllers gebruikt wordt
	//een standaardDatumString heeft formaat YYYY-MM-DD
	public static Calendar standaardDatumStringToCal(String pStadaardDatumString) {
		Calendar lCal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			lCal.setTime(sdf.parse(pStadaardDatumString));
		}  catch (ParseException e) {
			e.printStackTrace();
			lCal=null;
		}
		return lCal;
	}
	//deze method is static onderdeel van PrIS omdat hij als hulp methode 
	//in veel controllers gebruikt wordt
	//een standaardDatumString heeft formaat YYYY-MM-DD
	public static String calToStandaardDatumString (Calendar pCalendar) {
		int lJaar = pCalendar.get(Calendar.YEAR);
		int lMaand= pCalendar.get(Calendar.MONTH) + 1;
		int lDag  = pCalendar.get(Calendar.DAY_OF_MONTH);

		String lMaandStr = Integer.toString(lMaand);
		if (lMaandStr.length() == 1) {
			lMaandStr = "0"+ lMaandStr;
		}
		String lDagStr = Integer.toString(lDag);
		if (lDagStr.length() == 1) {
			lDagStr = "0"+ lDagStr;
		}
		String lString = 
				Integer.toString(lJaar) + "-" +
				lMaandStr + "-" +
				lDagStr;
		return lString;
	}

	public Docent getDocent(String gebruikersnaam) {
		Docent resultaat = null;
		
		for (Docent d : deDocenten) {
			if (d.getGebruikersnaam().equals(gebruikersnaam)) {
				resultaat = d;
				break;
			}
		}
		
		return resultaat;
	}

	public ArrayList<RoosterData> getRoosterData(String klascode)
	{
		return this.rooster.getRoosterForKlasCode(klascode);
	}
	public ArrayList<RoosterData> getRoosterDataDocent(String docent_mail)
	{
		return this.rooster.getRoosterForTeacher(docent_mail);
	}

	public Klas getKlasOmTijdMetVakEnDocent(String docent_mail, String vakcode, String starttijd, String datum)
	{
		for (Klas klas : deKlassen) {
			ArrayList<RoosterData> roosterData = getRoosterData(klas.getKlasCode());
			for(RoosterData data : roosterData)
			{
				if(data.getTeacher_email().equals(docent_mail) && data.getDate().equals(datum) && data.getStarttime().equals(starttijd))
				{
					return klas;
				}
			}
		}
		return null;
	}

	public Klas getKlasVanStudent(Student pStudent) {
	  //used
		for (Klas lKlas : deKlassen) {
			if (lKlas.bevatStudent(pStudent)){
				return (lKlas);
			}
		}
		return null;
	}
	
	public Student getStudent(String pGebruikersnaam) {
		// used
		Student lGevondenStudent = null;
		
		for (Student lStudent : deStudenten) {
			if (lStudent.getGebruikersnaam().equals(pGebruikersnaam)) {
				lGevondenStudent = lStudent;
				break;
			}
		}
		
		return lGevondenStudent;
	}
	
	public Student getStudent(int pStudentNummer) {
		// used
		Student lGevondenStudent = null;
		
		for (Student lStudent : deStudenten) {
			if (lStudent.getStudentNummer()==(pStudentNummer)) {
				lGevondenStudent = lStudent;
				break;
			}
		}
		
		return lGevondenStudent;
	}

	public Map<String, String> loginDetails(String username, String password)
	{
		Map<String, String> loginReturn = new HashMap<String, String>();

		for (Docent d : deDocenten) {
			if (d.getGebruikersnaam().equals(username)) {
				if (d.komtWachtwoordOvereen(password)) {
					loginReturn.put("rol","docent");
					loginReturn.put("voornaam", d.getVoornaam());
					loginReturn.put("achternaam", d.getVolledigeAchternaam());
					loginReturn.put("identificatienummer",""+d.getDocentNummer());
				}
			}
		}

		for (Student s : deStudenten) {
			if (s.getGebruikersnaam().equals(username)) {
				if (s.komtWachtwoordOvereen(password)) {
					loginReturn.put("rol","student");
					loginReturn.put("voornaam", s.getVoornaam());
					loginReturn.put("achternaam", s.getVolledigeAchternaam());
					loginReturn.put("identificatienummer",""+s.getStudentNummer());
                    loginReturn.put("group",s.getGroepId());						//WARNING: Student side only
                    loginReturn.put("klasnaam",s.getKlasnaam());						//WARNING: Student side only
                    loginReturn.put("klascode",s.getKlasCode());						//WARNING: Student side only
				}
			}
		}

		return loginReturn;
	}

	public String login(String gebruikersnaam, String wachtwoord) {
		for (Docent d : deDocenten) {
			if (d.getGebruikersnaam().equals(gebruikersnaam)) {
				if (d.komtWachtwoordOvereen(wachtwoord)) {
					return "docent";
				}
			}
		}
		
		for (Student s : deStudenten) {
			if (s.getGebruikersnaam().equals(gebruikersnaam)) {
				if (s.komtWachtwoordOvereen(wachtwoord)) {
					return "student";
				}
			}
		}
		
		return "undefined";
	}
	private void vulDocenten(ArrayList<Docent> pDocenten) {
		String csvFile = "././CSV/docenten.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
			
		try {
	
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
	
			        // use comma as separator
				String[] element = line.split(cvsSplitBy);
				String gebruikersnaam = element[0].toLowerCase();
				String voornaam = element[1];
				String tussenvoegsel = element[2];
				String achternaam = element[3];
				String md5_pass = element[4];
				pDocenten.add(new Docent(voornaam, tussenvoegsel, achternaam, md5_pass, gebruikersnaam, 1));
				
				//System.out.println(gebruikersnaam);
		
			}
	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void vulRooster()
	{
		Rooster rooster = new Rooster();

		String csvFile = "././CSV/rooster.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		try {

			br = new BufferedReader(new FileReader(csvFile));
			String rawData = "";
			while ((line = br.readLine()) != null) {
				rawData += line+"\n";
			}
			rooster.importData(rawData);
			this.rooster = rooster;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void vulKlassen(ArrayList<Klas> pKlassen) {
		//TICT-SIE-VIA is de klascode die ook in de rooster file voorkomt
		//V1A is de naam van de klas die ook als file naam voor de studenten van die klas wordt gebruikt
		//Klas k1 = new Klas("TICT-SIE-V1A", "V1A");
		Klas k2 = new Klas("TICT-SIE-V1B", "V1B");
		Klas k3 = new Klas("TICT-SIE-V1C", "V1C");
		Klas k4 = new Klas("TICT-SIE-V1D", "V1D");
		Klas k5 = new Klas("TICT-SIE-V1E", "V1E");
		Klas k6 = new Klas("TICT-SIE-V1F", "V1F");
		
		//pKlassen.add(k1);
		pKlassen.add(k2);
		pKlassen.add(k3);
		pKlassen.add(k4);
		pKlassen.add(k5);
		pKlassen.add(k6);
	}	
	private void vulStudenten(
			ArrayList<Student> pStudenten,
			ArrayList<Klas> pKlassen) {
		Student lStudent;
		for (Klas k : pKlassen) {			
			String csvFile = "././CSV/" + k.getNaam() + ".csv";
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = ",";

			try {

				br = new BufferedReader(new FileReader(csvFile));
				
				while ((line = br.readLine()) != null) {
				    // use comma as separator
					String[] element = line.split(cvsSplitBy);
					String gebruikersnaam = (element[3] + "." + element[2] + element[1] + "@student.hu.nl").toLowerCase();
					// verwijder spaties tussen  dubbele voornamen en tussen bv van der 
					gebruikersnaam = gebruikersnaam.replace(" ","");
					String lStudentNrString  = element[0];
					int lStudentNr = Integer.parseInt(lStudentNrString);
					lStudent = new Student(element[3], element[2], element[1],  element[4], gebruikersnaam, lStudentNr, k); //Volgorde 3-2-1 = voornaam, tussenvoegsel en achternaam
					pStudenten.add(lStudent);
					k.voegStudentToe(lStudent);
			
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}	
			
		}
	}	


}
