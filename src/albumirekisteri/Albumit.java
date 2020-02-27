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
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import fi.jyu.mit.ohj2.WildChars;
import kanta.SailoException;

/**
 * Rekisterin albumit, joka osaa mm. lisätä uuden albumin
 * 
 * @author Joose Tikkanen ja Pertti Arvola
 */
public class Albumit implements Iterable<Albumi> {
	
	private boolean muutettu = false;
	private String tiedostonPerusNimi = "";
	
	private final ArrayList<Albumi> alkiot = new ArrayList<Albumi>();

	
	/**
	 * luokan testaus
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		Albumit albumit = new Albumit();
		Albumi mez1 = new Albumi();
		mez1.taytaMezmerizeTiedot(2);
		Albumi mez2 = new Albumi();
		mez2.taytaMezmerizeTiedot(1);
		Albumi mez3 = new Albumi();
		mez3.taytaMezmerizeTiedot(2);
		Albumi mez4 = new Albumi();
		mez4.taytaMezmerizeTiedot(2);
		
		albumit.lisaa(mez1);
		albumit.lisaa(mez2);
		albumit.lisaa(mez3);
		albumit.lisaa(mez2);
		albumit.lisaa(mez4);
		
		List<Albumi> albumit2 = albumit.annaAlbumit(2);
		
		for (Albumi alb : albumit2) {
			System.out.print(alb.getArtistiNro() + " ");
			alb.tulosta(System.out);
		}

	}
	
	
	/** 
	 * Poistaa valitun albumin 
	 * @param albumi poistettava albumi
	 * @return tosi jos löytyi poistettava tietue  
	 * @example 
	 * <pre name="test">  
	 * #import java.io.File; 
	 *  Albumit albumit = new Albumit(); 
	 *  Albumi pitsi21 = new Albumi(); pitsi21.taytaMezmerizeTiedot(2); 
	 *  Albumi pitsi11 = new Albumi(); pitsi11.taytaMezmerizeTiedot(1); 
	 *  Albumi pitsi22 = new Albumi(); pitsi22.taytaMezmerizeTiedot(2);  
	 *  Albumi pitsi12 = new Albumi(); pitsi12.taytaMezmerizeTiedot(1);  
	 *  Albumi pitsi23 = new Albumi(); pitsi23.taytaMezmerizeTiedot(2);  
	 *  albumit.lisaa(pitsi21); 
	 *  albumit.lisaa(pitsi11); 
	 *  albumit.lisaa(pitsi22); 
	 *  albumit.lisaa(pitsi12); 
	 *  albumit.poista(pitsi23) === false ; albumit.getLkm() === 4; 
	 *  albumit.poista(pitsi11) === true;   albumit.getLkm() === 3; 
	 *  List<Albumi> a = albumit.annaAlbumit(1); 
	 *  a.size() === 1;  
	 *  a.get(0) === pitsi12; 
	 * </pre> 
	 */ 
	public boolean poista(Albumi albumi) { 
	    boolean ret = alkiot.remove(albumi); 
	    if (ret) muutettu = true; 
	    return ret; 
	} 
	
	 
	 
	/** 
	 * Poistaa kaikki tietyn artistin albumit 
	 * @param tunnusNro viite siihen, mihin liittyvät tietueet poistetaan 
	 * @return montako poistettiin  
	 * @example 
	 * <pre name="test"> 
	 *  Albumit albumit = new Albumit(); 
	 *  Albumi pitsi21 = new Albumi(); pitsi21.taytaMezmerizeTiedot(2); 
	 *  Albumi pitsi11 = new Albumi(); pitsi11.taytaMezmerizeTiedot(1); 
	 *  Albumi pitsi22 = new Albumi(); pitsi22.taytaMezmerizeTiedot(2);  
	 *  Albumi pitsi12 = new Albumi(); pitsi12.taytaMezmerizeTiedot(1);  
	 *  Albumi pitsi23 = new Albumi(); pitsi23.taytaMezmerizeTiedot(2);  
	 *  albumit.lisaa(pitsi21); 
	 *  albumit.lisaa(pitsi11); 
	 *  albumit.lisaa(pitsi22); 
	 *  albumit.lisaa(pitsi12); 
	 *  albumit.lisaa(pitsi23); 
	 *  albumit.poistaArtistinAlbumit(2) === 3;  albumit.getLkm() === 2; 
	 *  albumit.poistaArtistinAlbumit(3) === 0;  albumit.getLkm() === 2; 
	 *  List<Albumi> a = albumit.annaAlbumit(2); 
	 *  a.size() === 0;  
	 *  a = albumit.annaAlbumit(1); 
	 *  a.get(0) === pitsi11; 
	 *  a.get(1) === pitsi12; 
	 * </pre> 
	 */ 
	public int poistaArtistinAlbumit(int tunnusNro) { 
	    int n = 0; 
	    for (Iterator<Albumi> it = alkiot.iterator(); it.hasNext();) { 
	        Albumi alb = it.next(); 
	        if ( alb.getArtistiNro() == tunnusNro ) { 
	            it.remove(); 
	            n++; 
	        } 
	    } 
	    if (n > 0) muutettu = true; 
	    return n; 
	} 
	
	
	/**    
 	 * Laitetaan muutos, jolloin pakotetaan tallentamaan.      
 	 */    
 	public void setMuutos() {    
 	    muutettu = true;    
 	}   

	
	/**
	 * Haetaan kaikki artistin albumit
	 * @param jasenenNro artistin nro jolle albumeja haetaan
	 * @return tietorakenne jossa viitteet löydetteyihin albumeihin
	 * @example
	 * <pre name="test">
	 * #import java.util.*;
     * 
     *  Albumit albumit = new Albumit();
     *  Albumi mez21 = new Albumi(2); albumit.lisaa(mez21);
     *  Albumi mez11 = new Albumi(1); albumit.lisaa(mez11);
     *  Albumi mez22 = new Albumi(2); albumit.lisaa(mez22);
     *  Albumi mez12 = new Albumi(1); albumit.lisaa(mez12);
     *  Albumi mez23 = new Albumi(2); albumit.lisaa(mez23);
     *  Albumi mez51 = new Albumi(5); albumit.lisaa(mez51);
     *  
     *  List<Albumi> loytyneet;
     *  loytyneet = albumit.annaAlbumit(3);
     *  loytyneet.size() === 0; 
     *  loytyneet = albumit.annaAlbumit(1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == mez11 === true;
     *  loytyneet.get(1) == mez12 === true;
     *  loytyneet = albumit.annaAlbumit(5);
     *  loytyneet.size() === 1; 
     *  loytyneet.get(0) == mez51 === true;
	 * </pre>
	 */
	public List<Albumi> annaAlbumit(int jasenenNro) {
		List<Albumi> loydetyt = new ArrayList<Albumi>();
		for (Albumi albumi : alkiot) {
			if (albumi.getArtistiNro() == jasenenNro) loydetyt.add(albumi);
		}
		return loydetyt;
	}

	
	/**
	 * Lisää uuden albumin tietorakenteeseen
	 * @param alb lisättävä albumi
	 */
	public void lisaa(Albumi alb) {
		alkiot.add(alb);
		muutettu = true;
	}
	
	/**
	 * Lukee albumit tiedostosta
	 * @param tied tiedoston nimen alkuosa
	 * @throws SailoException jos lukeminen epäonnistuu
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException 
     * #import java.io.File;
     * #import kanta.SailoException;
     *  Albumit albumit = new Albumit();
     *  Albumi pitsi21 = new Albumi(); pitsi21.taytaMezmerizeTiedot(2);
     *  Albumi pitsi11 = new Albumi(); pitsi11.taytaMezmerizeTiedot(1);
     *  Albumi pitsi22 = new Albumi(); pitsi22.taytaMezmerizeTiedot(2); 
     *  Albumi pitsi12 = new Albumi(); pitsi12.taytaMezmerizeTiedot(1); 
     *  Albumi pitsi23 = new Albumi(); pitsi23.taytaMezmerizeTiedot(2); 
     *  String tiedNimi = "testirekisteri";
     *  File ftied = new File(tiedNimi+".dat");
     *  ftied.delete();
     *  albumit.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  albumit.lisaa(pitsi21);
     *  albumit.lisaa(pitsi11);
     *  albumit.lisaa(pitsi22);
     *  albumit.lisaa(pitsi12);
     *  albumit.lisaa(pitsi23);
     *  albumit.tallenna();
     *  albumit = new Albumit();
     *  albumit.lueTiedostosta(tiedNimi);
     *  
     *  Iterator<Albumi> i = albumit.iterator();
     *  i.next().toString() === pitsi21.toString();
     *  i.next().toString() === pitsi11.toString();
     *  i.next().toString() === pitsi22.toString();
     *  i.next().toString() === pitsi12.toString();
     *  i.next().toString() === pitsi23.toString();
     *  i.hasNext() === false;
     *  albumit.lisaa(pitsi23);
     *  albumit.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
	 * </pre>
	 */
	public void lueTiedostosta(String tied) throws SailoException  {
        setTiedostonPerusNimi(tied);
        try ( Scanner fi = new Scanner(new FileInputStream(new File(getTiedostonNimi()))) ) {
            while ( fi.hasNext() ){
                String rivi = fi.nextLine();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Albumi albumi = new Albumi();
                albumi.parse(rivi);
                lisaa(albumi);
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
     * Tallentaa albumit tiedostoon.  
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); 
        ftied.renameTo(fbak); 

        try ( PrintStream fo = new PrintStream(new FileOutputStream(getTiedostonNimi(), false)) ) {
            for (Albumi alb : this){
            	fo.println(alb.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        }

        muutettu = false;
    }
	
	 /**
     * Asettaa tiedoston perusnimen ilan tarkenninta
     * @param tied tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonPerusNimi = tied;
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
        return tiedostonPerusNimi + ".dat";
    }
    
    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }

	
	/**
	 * Iteraattori kaikkien albumien läpikäymiseen
	 * @return albumi-iteraattori
	 */
	@Override
	public Iterator<Albumi> iterator() {
		return alkiot.iterator();
	}
	
	
	/**
	 * Palauttaa rekisterin albumien lkm:n
	 * @return albumien lkm
	 */
	public int getLkm(){
		return alkiot.size();
	}


	/** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien albumien viitteet 
     * @param hakuehto hakuehto 
     * @param k etsittävän kentän indeksi  
     * @return tietorakenteen löytyneistä albumeista 
     */ 
   // @SuppressWarnings("unused") // hakuehtoa ei käytetä vielä
    public Collection<Albumi> etsi(String hakuehto, int k) { 
        String ehto = "*";  
        if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto;  
        int hk = k;  
        Collection<Albumi> loytyneet = new ArrayList<Albumi>(); 
        for (int i = 0; i < getLkm(); i++) {
            String sisalto = alkiot.get(i).anna(hk);
            if (sisalto == null) continue;
            if (WildChars.onkoSamat(sisalto, ehto)) loytyneet.add(alkiot.get(i));
        }
         
        return loytyneet; 
    }


    /** 
     * Korvaa artistin tietorakenteessa.  Ottaa artistin omistukseensa. 
     * Etsitään samalla tunnusnumerolla oleva artisti.  Jos ei löydy, 
     * niin lisätään uutena artistina. 
     * @param albumi lisätäävän artistin viite.  Huom tietorakenne muuttuu omistajaksi 
     * <pre name="test"> 
     * Artistit artistit = new Artistit(); 
     * Artisti aku1 = new Artisti(), aku2 = new Artisti(); 
     * aku1.rekisteroi(); aku2.rekisteroi(); 
     * artistit.getLkm() === 0; 
     * artistit.korvaaTaiLisaa(aku1); artistit.getLkm() === 1; 
     * artistit.korvaaTaiLisaa(aku2); artistit.getLkm() === 2; 
     * </pre> 
     */ 
    public void korvaaTaiLisaa(Albumi albumi) { 
        int id = albumi.getTunnusNro();
        for (int i = 0; i < getLkm(); i++) { 
            if ( alkiot.get(i).getTunnusNro() == id ) { 
                alkiot.set(i, albumi); 
                muutettu = true; 
                return; 
            } 
        } 
        lisaa(albumi); 
    } 
}
