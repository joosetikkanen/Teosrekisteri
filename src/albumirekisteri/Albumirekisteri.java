/**
 * 
 */
package albumirekisteri;

import java.io.File;
import java.util.Collection;
import java.util.List;

import kanta.SailoException;

/**
 * Huolehtii Albumit ja Artistit -luokkien v‰lisest‰ yhteistyˆst‰ ja v‰litt‰‰ n‰it‰ tietoja pyydett‰ess‰
 * 
 * @author Joose Tikkanen ja Pertti Arvola
 * 
 * Testien alustus
 * @example
 * <pre name="testJAVA">
 *  private Albumirekisteri rekisteri;
 *  private Artisti soad1;
 *  private Artisti soad2;
 *  private int aid1;
 *  private int aid2;
 *  private Albumi pitsi21;
 *  private Albumi pitsi11;
 *  private Albumi pitsi22; 
 *  private Albumi pitsi12; 
 *  private Albumi pitsi23;
 *  
 *  @SuppressWarnings("javadoc")
 *  public void alustaRekisteri() {
 *    rekisteri = new Albumirekisteri();
 *    soad1 = new Artisti(); soad1.taytaTiedot(); soad1.rekisteroi();
 *    soad2 = new Artisti(); soad2.taytaTiedot(); soad2.rekisteroi();
 *    aid1 = soad1.getTunnusNro();
 *    aid2 = soad2.getTunnusNro();
 *    pitsi21 = new Albumi(aid2); pitsi21.taytaMezmerizeTiedot(aid2);
 *    pitsi11 = new Albumi(aid1); pitsi11.taytaMezmerizeTiedot(aid1);
 *    pitsi22 = new Albumi(aid2); pitsi22.taytaMezmerizeTiedot(aid2); 
 *    pitsi12 = new Albumi(aid1); pitsi12.taytaMezmerizeTiedot(aid1); 
 *    pitsi23 = new Albumi(aid2); pitsi23.taytaMezmerizeTiedot(aid2);
 *    try {
 *    rekisteri.lisaa(soad1);
 *    rekisteri.lisaa(soad2);
 *    rekisteri.lisaa(pitsi21);
 *    rekisteri.lisaa(pitsi11);
 *    rekisteri.lisaa(pitsi22);
 *    rekisteri.lisaa(pitsi12);
 *    rekisteri.lisaa(pitsi23);
 *    } catch ( Exception e) {
 *       System.err.println(e.getMessage());
 *    }
 *  }
 * </pre>
 */
public class Albumirekisteri {

	private Artistit artistit = new Artistit();
	private Albumit albumit = new Albumit();
	
	
	/**
	 * Testiohjelma albumirekisterist‰
	 * @param args ei k‰ytˆss‰
	 */
	public static void main(String[] args) {
		
		Albumirekisteri rekisteri = new Albumirekisteri();
		
		// rekisteri.lueTiedostosta("artistit");
		
		Artisti soad = new Artisti();
		Artisti soad2 = new Artisti();
		soad.rekisteroi();
		soad.taytaTiedot();
		soad2.rekisteroi();
		soad2.taytaTiedot();
		
		rekisteri.lisaa(soad);
		rekisteri.lisaa(soad2);
		
		int id1 = soad.getTunnusNro();
		int id2 = soad2.getTunnusNro();
		 Albumi mez11 = new Albumi(id1); mez11.taytaMezmerizeTiedot(id1); rekisteri.lisaa(mez11);
		 Albumi mez12 = new Albumi(id1); mez12.taytaMezmerizeTiedot(id1); rekisteri.lisaa(mez12);
		 Albumi mez21 = new Albumi(id2); mez21.taytaMezmerizeTiedot(id2); rekisteri.lisaa(mez21);
		 Albumi mez22 = new Albumi(id2); mez22.taytaMezmerizeTiedot(id2); rekisteri.lisaa(mez22);
		 Albumi mez23 = new Albumi(id2); mez23.taytaMezmerizeTiedot(id2); rekisteri.lisaa(mez23);
		
		for (int i = 0; i < rekisteri.getArtisteja(); i++) {
			Artisti artisti = rekisteri.annaArtisti(i);
	        System.out.println("Artisti paikassa: " + i);
	        artisti.tulosta(System.out);
	        List<Albumi> loytyneet = rekisteri.annaAlbumit(artisti);
	        for (Albumi albumi : loytyneet) {
				albumi.tulosta(System.out);
			}
	    }
	}
	
	
	/**
	 * Lis‰‰ rekisteriin uuden Artistin
	 * @param artisti lis‰tt‰v‰ artisti
	 * @example
	 * <pre name="test">
     * Albumirekisteri rekisteri = new Albumirekisteri();
     * Artisti soad = new Artisti(), soad2 = new Artisti();
     * soad.rekisteroi(); soad2.rekisteroi();
     * rekisteri.getArtisteja() === 0;
     * rekisteri.lisaa(soad); rekisteri.getArtisteja() === 1;
     * rekisteri.lisaa(soad2); rekisteri.getArtisteja() === 2;
     * rekisteri.lisaa(soad); rekisteri.getArtisteja() === 3;
     * rekisteri.getArtisteja() === 3;
     * rekisteri.annaArtisti(0) === soad;
     * rekisteri.annaArtisti(1) === soad2;
     * rekisteri.annaArtisti(2) === soad;
     * rekisteri.annaArtisti(3) === soad; #THROWS IndexOutOfBoundsException 
     * rekisteri.lisaa(soad); rekisteri.getArtisteja() === 4;
     * rekisteri.lisaa(soad); rekisteri.getArtisteja() === 5;
	 * </pre>
	 */
	public void lisaa(Artisti artisti) {
		artistit.lisaa(artisti);
	}
	
	 /**  
 	 * Korvaa artistin tietorakenteessa.  Ottaa artistin omistukseensa.  
 	 * Etsit‰‰n samalla tunnusnumerolla oleva artisti.  Jos ei lˆydy,  
 	 * niin lis‰t‰‰n uutena artistina.  
 	 * @param albumi lis‰tt‰v‰n artistin viite.
 	 */  
 	public void korvaaTaiLisaa(Albumi albumi) {  
 		albumit.korvaaTaiLisaa(albumi); 
 	} 
	
	
	/**
	 * lis‰‰ albumin rekisteriin
	 * @param alb lis‰tt‰v‰ albumi
	 */
	public void lisaa(Albumi alb){
		albumit.lisaa(alb);
	}

	
	/**
	 * Palauttaa albumirekisterin artistien m‰‰r‰n
	 * @return artistien m‰‰r‰
	 */
	public int getArtisteja() {
		return artistit.getLkm();
	}

	
	/**
	 * Palauttaa i:n artistin
	 * @param i monesko artisti palautetaan
	 * @return viite i:denteen artistiin
	 * @throws IndexOutOfBoundsException jos i v‰‰rin
	 */
	public Artisti annaArtisti(int i) throws IndexOutOfBoundsException {
		return artistit.anna(i);
	}
	
	/** Antaa artistin id:n perusteella
	 * @param id artistin id
	 * @return artisti
	 */
	public Artisti annaArtistiId(int id) {
	    return artistit.annaId(id);
	}
	
	/**
	 * Palauttaa "taulukossa" hakuehtoon vastaavien albumien viitteet 
	 * @param hakuehto hakuehto
	 * @param k etsitt‰v‰n kent‰n indeksi  
	 * @return tietorakenteen lˆytyneist‰ albumeista
	 */
	public Collection<Albumi> etsi(String hakuehto, int k){
		return albumit.etsi(hakuehto, k);
	}
	
	
	/**
	 * antaa tietyn artistin kaikki albumit
	 * @param artisti artisti jonka albumit halutaan
	 * @return albumilista
	 */
	public List<Albumi> annaAlbumit(Artisti artisti){
		return albumit.annaAlbumit(artisti.getTunnusNro());
	}
	
	/**
     * Asettaa tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
        artistit.setTiedostonPerusNimi(hakemistonNimi + "nimet");
        albumit.setTiedostonPerusNimi(hakemistonNimi + "albumit");
    }
    
    /**
     * Lukee rekisterin tiedot tiedostosta
     * @param nimi nimi jota k‰ytet‰‰n lukemisessa
     * @throws SailoException jos lukeminen ep‰onnistuu
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        artistit = new Artistit();
        albumit = new Albumit();

        setTiedosto(nimi);
        artistit.lueTiedostosta();
        albumit.lueTiedostosta();
    }
    
    /**
     * Tallenttaa rekisterin tiedot tiedostoon.  
     * Vaikka artistien tallettamien ep‰onistuisi, niin yritet‰‰n silti tallettaa
     * albumeja ennen poikkeuksen heitt‰mist‰.
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            artistit.tallenna();
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }

        try {
            albumit.tallenna();
        } catch ( SailoException ex ) {
            virhe += ex.getMessage();
        }
        if ( !"".equals(virhe) ) throw new SailoException(virhe);
    }
    
     /**3
 	 * @param artisti asd
     * @return  asd
     * @example 
 	 * <pre name="test"> 
 	 * #THROWS Exception 
 	 *   alustaRekisteri(); 
 	 *   rekisteri.etsi("*",0).size() === 5; 
 	 *   rekisteri.annaAlbumit(soad1).size() === 2; 
 	 *   rekisteri.poista(soad1) === 1; 
 	 *   rekisteri.etsi("*",0).size() === 3; 
 	 *   rekisteri.annaAlbumit(soad1).size() === 0; 
 	 *   rekisteri.annaAlbumit(soad2).size() === 3; 
 	 * </pre> 
 	 */ 
 	public int poista(Artisti artisti) { 
 	    if ( artisti == null ) return 0; 
 	    int ret = artistit.poista(artisti.getTunnusNro());  
 	    albumit.poistaArtistinAlbumit(artisti.getTunnusNro());  
 	    return ret;
 	} 
 	
 	
 	/**  
 	 * Poistaa t‰m‰n albumin
 	 * @param albumi poistettava albumi  
 	 * @example 
 	 * <pre name="test"> 
 	 * #THROWS Exception 
 	 *   alustaRekisteri(); 
 	 *   rekisteri.annaAlbumit(soad1).size() === 2; 
 	 *   rekisteri.poistaAlbumi(pitsi11); 
 	 *   rekisteri.annaAlbumit(soad1).size() === 1; 
 	 */  
 	public void poistaAlbumi(Albumi albumi) {  
 	    albumit.poista(albumi);  
 	}  
 	
 	/**    
 	 * Laitetaan albumit muuttuneeksi, niin pakotetaan tallentamaan.    
 	 */    
 	public void setAlbumiMuutos() {    
 	    albumit.setMuutos();    
 	}


    /**
     * rekisterin albumien lkm
     * @return lkm
     */
    public int getAlbumeja() {
        return albumit.getLkm();
        
    }    
}
