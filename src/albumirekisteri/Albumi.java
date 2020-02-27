/**
 * 
 */
package albumirekisteri;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Calendar;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Albumi joka osaa huolehtia mm. tunnusnumerostaan.
 *
 * @author Joose Tikkanen ja Pertti Arvola
 */
public class Albumi implements Cloneable{
	
	private int tunnusNro;
	private int artistiNro;
	private String nimi;
	private String tyylilaji;
	private int julkaisuvuosi;
	private String tuottaja;
	private String kesto;
	private String levyYhtio;
	
	private static int seuraavaNro = 1;
	
	/**
	 * Alustetaan albumi.
	 */
	public Albumi(){
		//
	}
	
	/**
	 * Alustetaan tietyn artistin albumi.
	 * @param artistiNro artistin viitenumero
	 */
	public Albumi(int artistiNro){
		this.artistiNro = artistiNro;
	}
	
	/** 
 	 * @return albumien kenttien lukum��r� 
 	 */ 
 	public int getKenttia() { 
 	    return 8; 
 	} 
 	
 	
 	/** 
 	 * @return ensimm�inen k�ytt�j�n sy�tett�v�n kent�n indeksi 
 	 */ 
 	public int ekaKentta() { 
 	    return 2; 
 	} 
 	 
 	
 	/** 
 	 * @param k mink� kent�n kysymys halutaan 
 	 * @return valitun kent�n kysymysteksti 
 	 */ 
 	public String getKysymys(int k) { 
 	    switch (k) { 
 	        case 0: 
 	            return "id"; 
 	        case 1: 
 	            return "artistiId"; 
 	        case 2: 
 	            return "nimi"; 
 	        case 3: 
 	            return "tyylilaji"; 
 	        case 4: 
 	            return "julkaisuvuosi";
 	        case 5:
 	        	return "tuottaja";
 	        case 6:
 	        	return "kesto";
 	        case 7:
 	        	return "levyYhtio";
 	        default: 
 	            return "???"; 
 	    } 
 	} 
 	
 	
 	/** 
 	 * @param k Mink� kent�n sis�lt� halutaan 
 	 * @return valitun kent�n sis�lt� 
 	 * @example 
 	 * <pre name="test"> 
 	 *   Albumi alb = new Albumi(); 
 	 *   alb.parse("   2   |  10  |   mezmerize  | metalli | 1995 | pertti | 32:54 | asdf "); 
 	 *   alb.anna(0) === "2";    
 	 *   alb.anna(1) === "10";    
 	 *   alb.anna(2) === "mezmerize";    
 	 *   alb.anna(3) === "metalli";    
 	 *   alb.anna(4) === "1995";
 	 *   alb.anna(5) === "pertti";
 	 *   alb.anna(6) === "32:54";
 	 *   alb.anna(7) === "asdf";
 	 *    
 	 * </pre> 
 	 */ 
 	public String anna(int k) { 
 	    switch (k) { 
 	   case 0: 
            return "" + tunnusNro; 
        case 1: 
            return "" + artistiNro; 
        case 2: 
            return nimi; 
        case 3: 
            return tyylilaji; 
        case 4: 
            return "" + julkaisuvuosi;
        case 5:
        	return tuottaja;
        case 6:
        	return kesto;
        case 7:
        	return levyYhtio;
        default: 
            return "???"; 
 	    } 
 	} 
 	
 	
 	/** 
 	 * Asetetaan valitun kent�n sis�lt�.  Mik�li asettaminen onnistuu, 
 	 * palautetaan null, muutoin virheteksti. 
 	 * @param k mink� kent�n sis�lt� asetetaan 
 	 * @param s asetettava sis�lt� merkkijonona 
 	 * @return null jos ok, muuten virheteksti 
 	 * @example 
 	 * <pre name="test"> 
 	 *   Albumi alb = new Albumi(); 
 	 *   alb.aseta(3,"kissa") === null; 
 	 *   alb.aseta(3,"rokki")  === null; 
 	 *   alb.aseta(4,"kissa") === "Julkaisuvuosi v��rin"; 
 	 *   alb.aseta(4, "1900")    === null; 
 	 *    
 	 * </pre> 
 	 */ 
 	public String aseta(int k, String s) {
 	    if (s == null) return "";
 	    String st = s.trim(); 
 	    StringBuilder sb = new StringBuilder(st); 
 	    switch (k) { 
 	        case 0: 
 	            setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro())); 
 	            return null; 
 	        case 1: 
 	            artistiNro = Mjonot.erota(sb, '|', artistiNro); 
 	            return null; 
 	        case 2: 
 	            nimi = st; 
 	            return null; 
 	        case 3: 
 	            tyylilaji = st; 
 	            return null; 
 	        case 4: 
 	            if (!julkaisuvTarkistus(st)) return "Julkaisuvuosi v��rin"; 
 	            julkaisuvuosi = Mjonot.erota(sb, '|', -1);
 	            return null;
 	        case 5:
 	        	tuottaja = st;
 	        	return null;
 	        case 6:
 	        	kesto = st;
 	        	return null;
 	        case 7:
 	        	levyYhtio = st;
 	        	return null;
 	
 	        default: 
 	            return "V��r� kent�n indeksi"; 
 	    } 
 	} 
 	
 	/**
	 * 
	 * @param tjono jono
	 * @return false jos 1800<vuosi<nykyvuosi, muuten true
	 * @example
	 * <pre name="test">
	 * #import java.util.Calendar;
	 * int vuosiNyt = Calendar.getInstance().get(Calendar.YEAR);
	 * int seuraavaVuosi = vuosiNyt + 1;
	 * String vuosi = seuraavaVuosi + "";
	 * julkaisuvTarkistus("1801") === true;
	 * julkaisuvTarkistus("1799") === false;
	 * julkaisuvTarkistus("2018") === true;
	 * julkaisuvTarkistus(vuosi) === false;
	 * </pre>
	 */
	public static boolean julkaisuvTarkistus(String tjono) {
 		int vuosiNyt = Calendar.getInstance().get(Calendar.YEAR);
		StringBuilder sb = new StringBuilder(tjono);
		int julkaisuvuosi = Mjonot.erota(sb, ' ', -1);
		if ( !(1800 <= julkaisuvuosi && julkaisuvuosi <= vuosiNyt) ) return false;
		return true;
	}
 	
 	
 	/** 
 	 * Tehd��n identtinen klooni albumista 
 	 * @return Object kloonattu albumi
 	 * @example 
 	 * <pre name="test"> 
 	 * #THROWS CloneNotSupportedException  
 	 *   Albumi alb = new Albumi(); 
 	 *   alb.parse("   2   |  10  |   mezmerize  | metalli | 1995 | pertti | 32:54 | asdf "); 
 	 *   Albumi kopio = alb.clone(); 
 	 *   kopio.toString() === alb.toString(); 
 	 *   alb.parse("   6  |  23  |   asdjkh  | rokki | 1900 | agdf | 12:53 | hthh "); 
 	 *   kopio.toString().equals(alb.toString()) === false; 
 	 * </pre> 
 	 */ 
 	@Override 
 	public Albumi clone() throws CloneNotSupportedException {  
 	    return (Albumi)super.clone(); 
 	} 
	
	/**
	 * Apumetodi, jolla saadaan t�ytetty� testiarvot albumille.
	 * nimi arvotaan, jotta kahdella albumilla ei olisi
	 * samoja tietoja.
	 * @param nro viite artistiin, jonka albumista on kyse
	 */
	public void taytaMezmerizeTiedot(int nro) {
		double n = (9999-1000)*Math.random() + 1000;
		artistiNro = nro;
		nimi = "Mezmerize " + (int)Math.round(n);
		tyylilaji = "Alternative metal";
		julkaisuvuosi = 2005;
		tuottaja = "Rick Rubin";
		kesto = "31:16";
		levyYhtio = "Columbia Records";
		
	}

	/**
	 * Tulostetaan albumin tiedot
	 * @param out tietovirta johon tulostetaan
	 */
	public void tulosta(PrintStream out) {
		out.println(getTunnusNro() + "|" + nimi + "|" + artistiNro + "|" + tyylilaji + "|" + julkaisuvuosi + "|" + tuottaja + "|" + kesto + "|" + levyYhtio);
		
	}
	
	/**
	 * Tulostetaan albumin tiedot
	 * @param os tietovirta johon tulostetaan
	 */
	public void tulosta(OutputStream os){
		tulosta(new PrintStream(os));
	}
	
	/**
	 * Antaa albumille seuraavan rek.nron.
	 * @return albumin uusi tunnusnro
	 * @example
	 * <pre name="test">
	 * Albumi albumi = new Albumi();
	 * albumi.getTunnusNro() === 0;
	 * albumi.rekisteroi();
	 * Albumi albumi2 = new Albumi();
	 * albumi2.rekisteroi();
	 * int n1 = albumi.getTunnusNro();
	 * int n2 = albumi2.getTunnusNro();
	 * n1 === n2-1;
	 * </pre>
	 */
	public int rekisteroi(){
		tunnusNro = seuraavaNro;
		seuraavaNro++;
		return tunnusNro;
	}
	
	/**
	 * Palautetaan albumin oma id
	 * @return albumin id
	 */
	public int getTunnusNro(){
		return tunnusNro;
	}
	
	/**
     * Asettaa tunnusnumeron ja samalla varmistaa ett�
     * seuraava numero on aina suurempi kuin t�h�n menness� suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if ( tunnusNro >= seuraavaNro ) seuraavaNro = tunnusNro + 1;
    }
	
	/**
	 * Palautetaan mille artistille albumi kuuluu
	 * @return alnumin id
	 */
	public int getArtistiNro(){
		return artistiNro;
	}
	
	/**
     * Palauttaa albumien tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return albumi tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Albumi albumi = new Albumi();
     *   albumi.parse("   2  |4   |Mezmerize              |alternative metal  |2005               |Rick Rubin         |36:11  |Columbia Records");
     *   albumi.toString()    === "2|4|Mezmerize|alternative metal|2005|Rick Rubin|36:11|Columbia Records";
     * </pre>
     */
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
        String erotin = ""; 
     	for (int k = 0; k < getKenttia(); k++) { 
     	        sb.append(erotin); 
     	        sb.append(anna(k)); 
     	        erotin = "|"; 
     	    } 
     	    return sb.toString(); 
    } 
    
    /**
     * Selvit�� albumin tiedot | erotellusta merkkijonosta.
     * Pit�� huolen ett� seuraavaNro on suurempi kuin tuleva tunnusnro.
     * @param rivi josta albumin tiedot otetaan
     * @example
     * <pre name="test">
     *   Albumi albumi = new Albumi();
     *   albumi.parse("   0   |  2  |   Mezmerize 1234  | Alternative metal | 2005 | Rick Rubin  | 31:16 | Columbia Records");
     *   albumi.getArtistiNro() === 2;
     *   albumi.toString()    === "0|2|Mezmerize 1234|Alternative metal|2005|Rick Rubin|31:16|Columbia Records";
     *   
     * </pre>
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        for (int k = 0; k < getKenttia(); k++) 
        	aseta(k, Mjonot.erota(sb, '|')); 
    } 
    
    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) return false;
        return this.toString().equals(obj.toString());
    }
    
    @Override
    public int hashCode() {
        return tunnusNro;
    }
	

	/**
	 * @param args ei k�yt�ss�
	 */
	public static void main(String[] args) {
		Albumi albumi = new Albumi();
		albumi.taytaMezmerizeTiedot(2);
		albumi.tulosta(System.out);

	}

	/**
	 * @return albumin nimi
	 */
	public String getNimi() {
		return nimi;
	}

}
