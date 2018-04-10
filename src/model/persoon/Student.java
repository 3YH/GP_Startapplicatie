//checked
package model.persoon;

import model.klas.Klas;

public class Student extends Persoon {

	private int studentNummer;
	private String groepId;
	private Klas klas;

	public Student(String pVoornaam, String pTussenvoegsel, String pAchternaam, String pWachtwoord,
			String pGebruikersnaam, int sStudentNummer, Klas klas) {
		super(pVoornaam, pTussenvoegsel, pAchternaam, pWachtwoord, pGebruikersnaam);
		this.studentNummer = sStudentNummer;
		this.setGroepId("");
		this.klas = klas;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj) && obj instanceof Student) {
				Student s = (Student) obj;
				return this.studentNummer == s.studentNummer;
		} else {
			return false;
		}
	}

	public String getGroepId() {
		return this.groepId;
	}

	public String getKlasnaam()
	{
		return this.klas.getNaam();
	}

	public String getKlasCode()
	{
		return this.klas.getKlasCode();
	}

	public void setGroepId(String pGroepId) {
		this.groepId = pGroepId;
	}

	public int getStudentNummer() {
		return this.studentNummer;
	}

	/*private void setStudentNummer(int pStudentNummer) {
		this.studentNummer = pStudentNummer;
	}*/

}
