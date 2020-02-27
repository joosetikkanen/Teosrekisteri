package fxJopitikk;

import static fxJopitikk.teosDialogController.getFieldId;

import java.io.PrintStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import albumirekisteri.Albumi;
import albumirekisteri.Albumirekisteri;
import albumirekisteri.Artisti;
import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import kanta.SailoException;

/**
 * Käyttöliittymän tapahtumien käsittelijä
 * @author Joose, Pertti
 * @version 5.4.2018
 *
 */
public class JopitikkGUIController implements Initializable {
	@FXML private ComboBoxChooser<String> cbKentat;
	@FXML private TextField hakuehto;
	@FXML private ListChooser<Albumi> chooserAlbumit;
	@FXML private ScrollPane panelArtisti;
	@FXML private GridPane gridArtisti;
	@FXML private GridPane gridAlbumi;
	@FXML private ScrollPane panelAlbumi;
	@FXML private Label labelVirhe;


	
	private String rekisterinNimi;
	Albumirekisteri rekisteri;
	private Albumi albumiKohdalla;
	private Artisti apuArtisti = new Artisti();
	private Albumi apuAlbumi = new Albumi();
	//private TextArea areaArtisti = new TextArea();
	
	private TextField[] albumEdits;
	private TextField[] artistiEdits;
	private int kentta = 0;
	
	@FXML void handleApua() {
		Dialogs.showMessageDialog("Apua ei toimi vielä");
	}

	@FXML void handleAvaa() {
        avaa();
	}

	@FXML void handleHakuehto() {
		if ( albumiKohdalla != null )
            hae(albumiKohdalla.getTunnusNro()); 
	}

	/**
     * Näytetään virheilmoitus siihen tarkoitetussa labelissa 
     * @param virhe virheilmoitus
     */
    private void naytaVirhe(String virhe) {
	    if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }

    @FXML void handleLisaaAlbumi() {
    	uusiAlbumi();
	}

	@FXML void handleLopeta() {
		tallenna();
		Platform.exit();
	}

	@FXML void handlePoistaAlbumi() {
		poistaAlbumi();
	}
	

    @FXML void handleLisaaArtisti() {
		 uusiArtisti();
	 }
	
    @FXML void handleMuokkaaAlbumi() {
        muokkaa(kentta);
    }

	@FXML void handleTallenna() {
		tallenna();
	}

	@FXML void handleTietoja() {
		Dialogs.showMessageDialog("Tietoja ei toimi vielä");
	}

	/**
     * Tarkistetaan onko tallennus tehty
     * @return true jos saa sulkea sovelluksen, false jos ei
     */
	public boolean voikoSulkea() {
		tallenna();
		return true;
	}

	/**
     * Tietojen tallennus
     * @return null jos onnistuu, muuten virhe tekstinä
     */
    private String tallenna() {
        try {
            rekisteri.tallenna();
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
            return ex.getMessage();
        }
    }

	/**
	 * Kysytään tiedoston nimi ja luetaan se
	 * @return onnistuiko avaaminen
	 */
	public boolean avaa() {
		String uusinimi = AloitusikkunaController.kysyNimi(null);
		if (uusinimi == null) return false;
		lueTiedosto(uusinimi);
		return true;
	}
	
	/**
	 * Alustaa rekisterin lukemalla sen valitun nimisestä tiedostosta
     * @param nimi tiedosto josta rekisterin tiedot luetaan
     * @return null jos onnistuu, muuten virhe tekstinä
     */
	protected String lueTiedosto(String nimi) {
		rekisterinNimi = nimi;
		setTitle("Teosrekisteri - " + rekisterinNimi);
		try {
            rekisteri.lueTiedostosta(nimi);
            hae(0);
            return null;
        } catch (SailoException e) {
            hae(0);
            String virhe = e.getMessage(); 
            if ( virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
		
	}

	private void setTitle(String title) {
		ModalController.getStage(hakuehto).setTitle(title);
		
	}

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
        
    }

	/**
     * Tehdään tarvittavat alustukset. Alustetaan myös albumilistan kuuntelija 
	 */
	protected void alusta() {
		panelAlbumi.setFitToHeight(true);
		
		chooserAlbumit.clear();
		chooserAlbumit.addSelectionListener(e -> naytaAlbumi());
		artistiEdits = new TextField[apuArtisti.getKenttia()];
		albumEdits = teosDialogController.luoKentat(gridAlbumi);  
	    for (TextField edit: albumEdits)  
	         if ( edit != null ) {  
	             edit.setEditable(false);  
	             edit.setOnMouseClicked(e -> { if ( e.getClickCount() > 1 ) muokkaa(getFieldId(e.getSource(),0)); });  
	             edit.focusedProperty().addListener((a,o,n) -> kentta = getFieldId(edit,0));  
	         }
	    cbKentat.clear();
	    for (int k = apuAlbumi.ekaKentta(); k < apuAlbumi.getKenttia(); k++)  
	        cbKentat.add(apuAlbumi.getKysymys(k), null);
	    cbKentat.setSelectedIndex(0);
	    
	} 

	/**
	 * @param rekisteri rekisteri jota käytetään tässä liittymässä
	 */
	public void setAlbumirekisteri(Albumirekisteri rekisteri) {
		this.rekisteri = rekisteri;
		naytaAlbumi();
		
	}

	/**
	 * Näyttää listasta valitun artistin tiedot, tilapäisesti yhteen isoon edit-kenttään
	 */
	protected void naytaAlbumi() {
		albumiKohdalla = chooserAlbumit.getSelectedObject();
		teosDialogController.naytaAlbumi(albumEdits, albumiKohdalla); 
		gridAlbumi.setVisible(albumiKohdalla != null);
		if (albumiKohdalla != null) naytaArtisti(albumiKohdalla.getArtistiNro());
		naytaVirhe(null);
	}
	
	/**
	 * Näytetään valittua albumia koskevan artistin tiedot
	 * sille tarkoitetussa gridissä
	 * @param artistiNro albumia koskevan artistin id
	 */
	private void naytaArtisti(int artistiNro) {
        gridArtisti.getChildren().clear();
        Artisti artistiKohdalla = rekisteri.annaArtistiId(artistiNro);
        
        for (int i = 0, k = apuArtisti.ekaKentta(); k < apuArtisti.getKenttia(); i++, k++) {
            Label label = new Label(apuArtisti.getKysymys(k));
            gridArtisti.add(label, 0, i);
            TextField edit = new TextField();
            artistiEdits[k] = edit;
            //edit.setId("e"+k);
            edit.setText(artistiKohdalla.anna(k));
            gridArtisti.add(edit, 1, i);
        }
        gridArtisti.setVisible(albumiKohdalla != null);
        
    }

    /**
	 * Tulostaa artistin tiedot
	 * @param os tietovirta johon tulostetaan 
	 * @param artisti tulostettava artisti
	 */
	public void tulosta(PrintStream os, final Artisti artisti) {
		os.println("-----------------------------------------------");
		artisti.tulosta(os);
		os.println("---------------------------------------------");
		List<Albumi> albumit = rekisteri.annaAlbumit(artisti);
		for (Albumi alb : albumit) {
			alb.tulosta(os);
		}
		
	}

	/**
	 * Hakee albumien tiedot listaan
	 * @param albuminNro albumin numero, joka aktivoidaan haun jälkeen
	 */
	private void hae(int albuminNro) {
		int k = cbKentat.getSelectedIndex() + apuAlbumi.ekaKentta(); 
        String ehto = hakuehto.getText(); 
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*"; 
        naytaVirhe(null);
        
		chooserAlbumit.clear();
		
		int index = 0;
        Collection<Albumi> albumit;
        albumit = rekisteri.etsi(ehto, k);
        int i = 0;
		for (Albumi albumi : albumit) {
			if (albumi.getTunnusNro() == albuminNro) index = i;
			chooserAlbumit.add(albumi.getNimi(), albumi);
			i++;
		}
		if ( i == 0 ) teosDialogController.tyhjenna(albumEdits);  // jos ei yhtään albumia 
		
			
		chooserAlbumit.setSelectedIndex(index); // tästä tulee muutosviesti joka näyttää albumin
	}
		
	/**
	 * Luo uuden artistin jota aletaan editoimaan 
	 */
	protected void uusiArtisti() {
		Artisti uusi = new Artisti(); 
	 	uusi = ArtistiLuontiController.kysyArtisti(null, uusi, 0);  
	 	if ( uusi == null ) return;  
	 	uusi.rekisteroi();  
		rekisteri.lisaa(uusi);
		tallenna();
		
		if (albumiKohdalla != null)
		    hae(albumiKohdalla.getTunnusNro());  
		

	}
	
	/**
	 * Luo uuden albumin jota aletaan editoimaan 
	 */
	public void uusiAlbumi(){
		Albumi alb = new Albumi();
		Artisti[] artistit = annaArtistit();
		alb = teosDialogController.kysyAlbumi(null, alb, 0, artistit);
		if ( alb == null ) return;
		alb.rekisteroi();
		rekisteri.lisaa(alb);
		tallenna();
	 
		if (albumiKohdalla != null)
		    hae(albumiKohdalla.getTunnusNro());
		else hae(0);
	}
	
	/**
	 * Haetaan kaikki rekisteriin tallennetut artistit
	 * @return taulukko artisteista
	 */
     private Artisti[] annaArtistit() {
		Artisti[] artistit = new Artisti[rekisteri.getArtisteja()];
		for (int i = 0; i < artistit.length; i++) {
			artistit[i] = rekisteri.annaArtisti(i);
		}
		return artistit;
		
	}

	/**
     * @param rekisteri rekisteri jota käytetään tässä käyttöliittymässä
     */
    public void setRekisteri(Albumirekisteri rekisteri) {
        this.rekisteri = rekisteri;
        naytaAlbumi();
    }
    
    /**
     * Aktivoidaan teosdialogi jolla voi muokata albumin tietoja
     * @param k kenttä joka saa fokuksen kun näytetään dialogi
     */
 	private void muokkaa(int k) {  
 	     if ( albumiKohdalla == null ) return;  
 	     try {  
 	         Albumi alb;
 	         Artisti[] artistit = annaArtistit();
 	         alb = teosDialogController.kysyAlbumi(null, albumiKohdalla.clone(), k, artistit);  
 	         if ( alb == null ) return;  
 	         rekisteri.korvaaTaiLisaa(alb);  
 	         hae(alb.getTunnusNro());  
 	     } catch (CloneNotSupportedException e) {  
 	        System.err.println("Kloonaus ei onnistunut: " + e);
 	     }  
 	}
 	
 	/**
 	 * Poistaa valitun albumin rekisteristä
 	 */
    private void poistaAlbumi() {
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko albumi: " + albumiKohdalla.getNimi(), "Kyllä", "Ei") )
            return;
        rekisteri.poistaAlbumi(albumiKohdalla);
        int index = chooserAlbumit.getSelectedIndex();
        hae(0);
        chooserAlbumit.setSelectedIndex(index);
        
        for (TextField edit : artistiEdits) {
           if (edit != null) edit.setText("");
        }
        if (albumiKohdalla != null) naytaArtisti(albumiKohdalla.getArtistiNro());
         
    }
    
}
