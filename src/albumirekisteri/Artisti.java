/**
 * 
 */
package albumirekisteri;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Calendar;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Artisti joka osaa huolehtia omasta tunnusnumerostaan
 * 
 * @author Joose Tikkanen ja Pertti Arvola
 */
public class Artisti implements Cloneable{
	
	private int tunnusNro;
	private String nimi = "";
	private String aktiivisena = "";
	
	private static int seuraavaNro = 1;
	
	

	/**
	 * Testataan artisti-luokkaa
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		Artisti soad = new Artisti();
		Artisti soad2 = new Artisti();
		soad.rekisteroi();
		soad2.rekisteroi();
		
		soad.tulosta(System.out);
		soad.taytaTiedot();
		soad.tulosta(System.out);
		
		soad2.taytaTiedot();
		soad2.tulosta(System.out);
		
		soad2.taytaTiedot();
		soad2.tulosta(System.out);
	}
	
	/**
	 * 
	 * @return kenttien lukumäärä
	 */
	public int getKenttia(){
		return 3;
	}
	
	/**
	 * @return asd
	 */
	public int ekaKentta(){
		return 1;
	}

	/** 
 	 * Antaa k:n kentän sisällön merkkijonona 
 	 * @param k monenenko kentän sisältö palautetaan 
 	 * @return kentän sisältö merkkijonona 
 	 */ 
 	public String anna(int k) { 
 	    switch ( k ) { 
 	    case 0: return "" + tunnusNro; 
 	    case 1: return "" + nimi; 
 	    case 2: return "" + aktiivisena; 
 	    default: return "Äääliö"; 
 	    } 
 	}
 	
 	/** 
 	 * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon 
 	 * @param k kuinka monennen kentän arvo asetetaan 
 	 * @param jono jonoa joka asetetaan kentän arvoksi 
 	 * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus. 
 	 */ 
 	public String aseta(int k, String jono) {
 		String tjono = jono.trim();
 	    StringBuilder sb = new StringBuilder(tjono);
 	    switch ( k ) { 
 	    case 0: 
 	        setTunnusNro(Mjonot.erota(sb, '§', getTunnusNro())); 
 	        return null; 
 	    case 1: 
 	        nimi = tjono; 
 	        return null; 
 	    case 2:  
 	        if ( !aktiivisenaTarkistus(tjono) ) return "Aktiivisena väärin"; 
 	        aktiivisena = tjono; 
 	        return null; 
 	    default: 
 	        return "ÄÄliö"; 
 	    } 
 	}
 	
 	/**
	 * 
	 * @param tjono asd
	 * @return asd
	 * @example
	 * <pre name="test">
	 * #import java.util.Calendar;
	 * int vuosiNyt = Calendar.getInstance().get(Calendar.YEAR);
	 * int seuraavaVuosi = vuosiNyt + 1;
	 * String vuosi = seuraavaVuosi + "";
	 * aktiivisenaTarkistus("1801") === true;
	 * aktiivisenaTarkistus("1799") === false;
	 * aktiivisenaTarkistus("2018") === true;
	 * aktiivisenaTarkistus(vuosi) === false;
	 * </pre>
	 */
	public static boolean aktiivisenaTarkistus(String tjono) {
 		int vuosiNyt = Calendar.getInstance().get(Calendar.YEAR);
		StringBuilder sb = new StringBuilder(tjono);
		int aktiivisena = Mjonot.erota(sb, ' ', -1);
		if ( !(1800 <= aktiivisena && aktiivisena <= vuosiNyt) ) return false;
		return true;
	}
	
	
	/** 
 	 * Palauttaa k:tta artistin kenttää vastaavan kysymyksen 
 	 * @param k kuinka monennen kentän kysymys palautetaan (0-alkuinen) 
 	 * @return k:netta kenttää vastaava kysymys 
 	 */ 
 	public String getKysymys(int k) { 
 	    switch ( k ) { 
 	    case 0: return "Tunnus nro"; 
 	    case 1: return "nimi"; 
 	    case 2: return "aktiivisena"; 
 	    default: return "Äääliö"; 
 	    } 
 	} 

	/**
	 * Apumetodi, jolla saadaan täytettyä testiarvot artistille. 
	 */
	public void taytaTiedot() {
		double n = (9999-1000)*Math.random() + 1000;
		
		nimi = "System of a Down " + (int)Math.round(n);
		aktiivisena = "1995-";
	}

	
	/**
     * Tulostetaan henkilön tiedot
     * @param out tietovirta johon tulostetaan
     */
	public void tulosta(PrintStream out){
		out.println(String.format("%03d", tunnusNro, 3) + "  " + nimi + "  "
                + aktiivisena);
	}
	
	
	/**
	 * Tulostetaan henkilön tiedot
	 * @param os tietovirta johon tulostetaan
	 */
	public void tulosta(OutputStream os) {
		tulosta(new PrintStream(os));
	}
	
	
	/**
	* Palauttaa artistin tunnusnumeron.
	* @return artistin tunnusnumero
	*/
	public int getTunnusNro(){
		return tunnusNro;
	}
	
	/**
	 * Asettaa tunnusnumeron ja samalla varmistaa että
	 * seuraava numero on aina suurempi kuin tähän mennessä suurin.
	 * @param nr asetettava tunnusnumero
	 */
	private void setTunnusNro(int nr) {
	    tunnusNro = nr;
	    if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
	}
	
	/**
	 * Palauttaa artistin tiedot merkkijonona jonka voi tallentaa tiedostoon.
	 * @return artisti tolppaeroteltuna merkkijonona 
	 * @example
	 * <pre name="test">
	 *   Artisti artisti = new Artisti();
	 *   artisti.parse("   3  |  System of a Down   | 1995|");
	 *   artisti.toString() === "3|System of a Down|1995";
	 * </pre>  
	 */
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder("");
	 	String erotin = ""; 
	 	for (int k = 0; k < getKenttia(); k++) { 
	 	    sb.append(erotin); 
	 	    sb.append(anna(k)); 
	 	    erotin = "|"; 
	 	} 
	 	return sb.toString(); 
	}
	
	
	/**
	 * Selvitää artistin tiedot | erotellusta merkkijonosta
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusNro.
	 * @param rivi rivi josta artistin tiedot otetaan
	 * @example
     * <pre name="test">
     *   Artisti artisti = new Artisti();
     *   artisti.parse("   3  |  Bowie   | 1960   |");
     *   artisti.getTunnusNro() === 3;
     *   artisti.toString() === "3|Bowie|1960"
     *
     *   artisti.rekisteroi();
     *   int n = artisti.getTunnusNro();
     *   artisti.parse(""+(n+20));
     *   artisti.rekisteroi();
     *   artisti.getTunnusNro() === n+20+1;
     *     
     * </pre>
	 */
	public void parse(String rivi) {
	    StringBuilder sb = new StringBuilder(rivi);
	    for (int k = 0; k < getKenttia(); k++) 
	     	aseta(k, Mjonot.erota(sb, '|')); 
	}
	
	/** 
 	 * Tutkii onko artistin tiedot samat kuin parametrina tuodun artistin tiedot 
 	 * @param artisti artisti johon verrataan 
 	 * @return true jos kaikki tiedot samat, false muuten 
 	 * @example 
 	 * <pre name="test"> 
 	 *   Artisti artisti1 = new Artisti(); 
 	 *   artisti1.parse("   3  |  Ankka Aku   | 1900"); 
 	 *   Artisti artisti2 = new Artisti(); 
 	 *   artisti2.parse("   3  |  Ankka Aku | 1900"); 
 	 *   Artisti artisti3 = new Artisti(); 
 	 *   artisti3.parse("   3  |  Ankka Aku   | 2000"); 
 	 *    
 	 *   artisti1.equals(artisti2) === true; 
 	 *   artisti2.equals(artisti1) === true; 
 	 *   artisti1.equals(artisti3) === false; 
 	 *   artisti3.equals(artisti2) === false; 
 	 * </pre> 
 	 */ 
 	public boolean equals(Artisti artisti) { 
 	    if ( artisti == null ) return false; 
 	    for (int k = 0; k < getKenttia(); k++) 
 	        if ( !anna(k).equals(artisti.anna(k)) ) return false; 
 	    return true; 
 	}
	
    @Override
    public boolean equals(Object artisti) {
    	if ( artisti instanceof Artisti ) return equals((Artisti)artisti); 
     	return false; 
    }

    @Override
    public int hashCode() {
        return tunnusNro;
    }
		
	/**
	 * Palauttaa artistin nimen
	 * @return artistin nimi
	 */
	public String getNimi() {
		return nimi;
	}	

	/**
	* Antaa artistille seuraavan rekisterinumeron.
	* @return artistin uusi tunnusNro
	* @example
	* <pre name="test">
	*   Artisti soad = new Artisti();
	*   soad.getTunnusNro() === 0;
	*   soad.rekisteroi();
	*   Artisti bowie = new Artisti();
	*   bowie.rekisteroi();
	*   int n1 = soad.getTunnusNro();
	*   int n2 = bowie.getTunnusNro();
	*   n1 === n2-1;
	* </pre>
	*/
	public int rekisteroi() {
		tunnusNro = seuraavaNro;
		seuraavaNro++;
		return tunnusNro;
	}
	
	/** 
 	 * Tehdään identtinen klooni artistista 
 	 * @return Object kloonattu artisti 
 	 * @example 
 	 * <pre name="test"> 
 	 * #THROWS CloneNotSupportedException  
 	 *   Artisti artisti = new Artisti(); 
 	 *   artisti.parse("   3  |  SOAD   | 123"); 
 	 *   Artisti kopio = artisti.clone(); 
 	 *   kopio.toString() === artisti.toString(); 
 	 *   artisti.parse("   4  |  Bowie   | 123"); 
 	 *   kopio.toString().equals(artisti.toString()) === false; 
 	 * </pre> 
 	 */ 
 	@Override 
 	public Artisti clone() throws CloneNotSupportedException { 
 	    Artisti uusi; 
 	    uusi = (Artisti) super.clone(); 
 	    return uusi; 
 	} 
}
