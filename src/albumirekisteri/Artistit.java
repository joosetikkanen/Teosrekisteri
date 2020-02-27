/**
 * 
 */
package albumirekisteri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import fi.jyu.mit.ohj2.WildChars;
import kanta.SailoException;

/**
 * Reksiterin artistit, joka osaa mm. lisätä uuden artistin
 * 
 * @author Joose Tikkanen ja Pertti Arvola
 */
public class Artistit {
	
	private static final int MAX_ARTISTEJA = 8;
	private int lkm = 0;
	private Artisti[] alkiot = new Artisti[MAX_ARTISTEJA];
	private final int kasvatus = 8;

	private boolean muutettu = false;
	private String tiedostonPerusNimi = "nimet";
	private String kokoNimi = "";
	
	/**
     * Testiohjelma artistit-luokkaa
     * @param args ei käytössä
     */
	 public static void main(String[] args) {
		Artistit artistit = new Artistit();

        Artisti soad = new Artisti(), soad2 = new Artisti();
        soad.rekisteroi();
        soad.taytaTiedot();
        soad2.rekisteroi();
        soad2.taytaTiedot();

        try {
            artistit.lisaa(soad);
            artistit.lisaa(soad2);

            System.out.println("============= Artistit testi =================");

            for (int i = 0; i < artistit.getLkm(); i++) {
                Artisti artisti = artistit.anna(i);
                System.out.println("Artisti nro: " + i);
                artisti.tulosta(System.out);
            }

        } catch (IndexOutOfBoundsException ex) {
            System.out.println(ex.getMessage());
        }
    }
	 
	 
	    /**  
	 * Poistaa artistin jolla on valittu tunnusnumero   
	 * @param id poistettavan artistin tunnusnumero  
	 * @return 1 jos poistettiin, 0 jos ei löydy  
	 * @example  
	 * <pre name="test">   
	 * Artistit artistit = new Artistit();  
	 * Artisti soad1 = new Artisti(), soad2 = new Artisti(), soad3 = new Artisti();  
	 * soad1.rekisteroi(); soad2.rekisteroi(); soad3.rekisteroi();  
	 * int id1 = soad1.getTunnusNro();  
	 * artistit.lisaa(soad1); artistit.lisaa(soad2); artistit.lisaa(soad3);  
	 * artistit.poista(id1+1) === 1;  
	 * artistit.annaId(id1+1) === null; artistit.getLkm() === 2;  
	 * artistit.poista(id1) === 1; artistit.getLkm() === 1;  
	 * artistit.poista(id1+3) === 0; artistit.getLkm() === 1;  
	 * </pre>  
	 *   
	 */  
	public int poista(int id) {  
	    int ind = etsiId(id);  
	    if (ind < 0) return 0;  
	    lkm--;  
	    for (int i = ind; i < lkm; i++)  
	        alkiot[i] = alkiot[i + 1];  
	    alkiot[lkm] = null;  
	    muutettu = true;  
	    return 1;  
	}  

	 
    /**
     * Palauttaa rekisterin artistien lukumäärän
     * @return artistien lukumäärä
     */
    public int getLkm() {
        return lkm;
    }
    
    
    /**
     * Palauttaa viitteen i:teen artistiin.
     * @param i monennenko artistin viite halutaan
     * @return viite artistiin, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
    protected Artisti anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }

    
    /**
     * Lisää uuden artistin tietorakenteeseen.  Ottaa artistin omistukseensa.
     * @param artisti lisättävän artistin viite.  Huom tietorakenne muuttuu omistajaksi
     * @example
     * <pre name="test">
     * Artistit artistit = new Artistit();
     * Artisti aku1 = new Artisti(), aku2 = new Artisti();
     * artistit.getLkm() === 0;
     * artistit.lisaa(aku1); artistit.getLkm() === 1;
     * artistit.lisaa(aku2); artistit.getLkm() === 2;
     * artistit.lisaa(aku1); artistit.getLkm() === 3;
     * artistit.anna(0) === aku1;
     * artistit.anna(1) === aku2;
     * artistit.anna(2) === aku1;
     * artistit.anna(1) == aku1 === false;
     * artistit.anna(1) == aku2 === true;
     * artistit.lisaa(aku1); artistit.getLkm() === 4;
     * artistit.lisaa(aku1); artistit.getLkm() === 5;
     * </pre>
     */
	public void lisaa(Artisti artisti) {
		if (lkm >= alkiot.length) kasvataTaulukkoa();
		alkiot[lkm] = artisti;
        lkm++;
        muutettu = true;
	}
	
	/** 
 	 * Korvaa artistin tietorakenteessa.  Ottaa artistin omistukseensa. 
 	 * Etsitään samalla tunnusnumerolla oleva artisti.  Jos ei löydy, 
 	 * niin lisätään uutena artistina. 
 	 * @param artisti lisätäävän artistin viite.  Huom tietorakenne muuttuu omistajaksi 
 	 * <pre name="test"> 
 	 * Artistit artistit = new Artistit(); 
 	 * Artisti aku1 = new Artisti(), aku2 = new Artisti(); 
 	 * aku1.rekisteroi(); aku2.rekisteroi(); 
 	 * artistit.getLkm() === 0; 
 	 * artistit.korvaaTaiLisaa(aku1); artistit.getLkm() === 1; 
 	 * artistit.korvaaTaiLisaa(aku2); artistit.getLkm() === 2; 
 	 * </pre> 
 	 */ 
 	public void korvaaTaiLisaa(Artisti artisti) { 
 	    int id = artisti.getTunnusNro(); 
 	    for (int i = 0; i < lkm; i++) { 
 	        if ( alkiot[i].getTunnusNro() == id ) { 
 	            alkiot[i] = artisti; 
 	            muutettu = true; 
 	            return; 
 	        } 
 	    } 
 	    lisaa(artisti); 
 	} 

	
	/**
	 * kasvattaa taulukkoa jos se täyttyy
	 */
	private void kasvataTaulukkoa() {
		Artisti[] alkiotKasvatettu = new Artisti[lkm + kasvatus];
		
		for (int i = 0; i < lkm; i++) {
			alkiotKasvatettu[i] = alkiot[i];
		}
		alkiot = alkiotKasvatettu;
	}
	
	
    /**
     * Lukee artistit tiedostosta. 
     * @param tied tiedoston perusnimi
     * @throws SailoException jos lukeminen epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import kanta.SailoException;
     * #import java.io.File;
     * 
     *  Artistit artistit = new Artistit();
     *  Artisti soad1 = new Artisti(), soad2 = new Artisti();
     *  soad1.taytaTiedot();
     *  soad2.taytaTiedot();
     *  String hakemisto = "testirekisteri";
     *  String tiedNimi = hakemisto+"/nimet";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  artistit.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  artistit.lisaa(soad1);
     *  artistit.lisaa(soad2);
     *  artistit.tallenna();
     *  artistit = new Artistit();            // Poistetaan vanhat luomalla uusi
     *  artistit.lueTiedostosta(tiedNimi);  // johon ladataan tiedot tiedostosta.
     *  artistit.anna(0) === soad1;
     *  artistit.anna(1) === soad2;
     *  artistit.lisaa(soad2);
     *  artistit.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     *  dir.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String tied) throws SailoException  {
        setTiedostonPerusNimi(tied);
        try ( Scanner fi = new Scanner(new FileInputStream(new File(getTiedostonNimi()))) ) {
            while ( fi.hasNext() ){
                String rivi = fi.nextLine();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Artisti artisti = new Artisti();
                artisti.parse(rivi); 
                lisaa(artisti);
            }
            muutettu = false;
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        }
    }

    
    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }
    
    
    /**
     * Tallentaa artistit tiedostoon.  
     * Tiedoston muoto:
     * <pre>
     * Artistit
     * ; kommenttirivi
     * 2|System of a Down|1995
     * 3|Gorillaz|1998
     * </pre>
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); // if .. System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); // if .. System.err.println("Ei voi nimetä");

        try ( PrintStream fo = new PrintStream(new FileOutputStream(getTiedostonNimi(), true)) ) {
            fo.println(getKokoNimi());
            for (int i = 0; i < getLkm(); i++) {
				fo.println(alkiot[i].toString());
			}
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        }

        muutettu = false;
    }
    
    
    /**
     * Asettaa tiedoston perusnimen ilan tarkenninta
     * @param nimi tallennustiedoston perusnimi
     */
	public void setTiedostonPerusNimi(String nimi) {
		tiedostonPerusNimi = nimi;
	}
	
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }

    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }
    
    /**
     * Palauttaa Kerhon koko nimen
     * @return Kerhon koko nimi merkkijononna
     */
    public String getKokoNimi() {
        return kokoNimi;
    }
    
    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }
    
    /** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien artistien viitteet 
     * @param hakuehto hakuehto 
     * @param k etsittävän kentän indeksi  
     * @return tietorakenteen löytyneistä artisteista 
     * @example 
     * <pre name="test"> 
     *   Artistit artistit = new Artistit(); 
     *   Artisti artisti1 = new Artisti(); artisti1.parse("1|Bowie|1960"); 
     *   Artisti artisti2 = new Artisti(); artisti2.parse("2|SOAD||2001"); 
     *   artistit.lisaa(artisti1); artistit.lisaa(artisti2);
     * </pre> 
     */ 
   // @SuppressWarnings("unused") // hakuehtoa ei käytetä vielä
    public Collection<Artisti> etsi(String hakuehto, int k) { 
    	String ehto = "*";  
     	if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto;  
     	int hk = k;  
        Collection<Artisti> loytyneet = new ArrayList<Artisti>(); 
        for (int i = 0; i < getLkm(); i++) {
        	 if (WildChars.onkoSamat(alkiot[i].anna(hk), ehto)) loytyneet.add(alkiot[i]);
		}
         
        return loytyneet; 
    }
    
    
    /**  
 	 * Etsii artistin id:n perusteella  
 	 * @param id tunnusnumero, jonka mukaan etsitään  
 	 * @return artisti jolla etsittävä id tai null  
 	 * <pre name="test">   
 	 * Artistit artistit = new Artistit();  
 	 * Artisti soad1 = new Artisti(), soad2 = new Artisti(), soad3 = new Artisti();  
 	 * soad1.rekisteroi(); soad2.rekisteroi(); soad3.rekisteroi();  
 	 * int id1 = soad1.getTunnusNro();  
 	 * artistit.lisaa(soad1); artistit.lisaa(soad2); artistit.lisaa(soad3);  
 	 * artistit.annaId(id1  ) == soad1 === true;  
 	 * artistit.annaId(id1+1) == soad2 === true;  
 	 * artistit.annaId(id1+2) == soad3 === true;  
 	 * </pre>  
 	 */  
 	public Artisti annaId(int id) { 
 		for (int i = 0; i < lkm; i++) {
 			if (id == alkiot[i].getTunnusNro()) return alkiot[i];
		}
 	    return null;  
 	}  
 	
 	
 	/**  
 	 * Etsii artistin id:n perusteella  
 	 * @param id tunnusnumero, jonka mukaan etsitään  
 	 * @return löytyneen artistin indeksi tai -1 jos ei löydy  
 	 * <pre name="test">  
 	 * Artistit artistit = new Artistit();  
 	 * Artisti soad1 = new Artisti(), soad2 = new Artisti(), soad3 = new Artisti();  
 	 * soad1.rekisteroi(); soad2.rekisteroi(); soad3.rekisteroi();  
 	 * int id1 = soad1.getTunnusNro();  
 	 * artistit.lisaa(soad1); artistit.lisaa(soad2); artistit.lisaa(soad3);  
 	 * artistit.etsiId(id1+1) === 1;  
 	 * artistit.etsiId(id1+2) === 2;  
 	 * </pre>  
 	 */  
 	public int etsiId(int id) {  
 	    for (int i = 0; i < lkm; i++)  
 	        if (id == alkiot[i].getTunnusNro()) return i;  
 	    return -1;  
 	}  
}

