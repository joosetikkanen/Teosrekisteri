/**
 * 
 */
package fxJopitikk;

import java.net.URL;
import java.util.ResourceBundle;

import albumirekisteri.Artisti;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author jopitikk
 *
 */
public class ArtistiLuontiController implements ModalControllerInterface<Artisti>,Initializable{
	
	@FXML private GridPane gridArtisti;
	@FXML private Label labelVirhe;
	
    @FXML void handleCancel() {
    	artistiKohdalla = null;
    	ModalController.closeStage(labelVirhe);
    }

    @FXML void handleOK() {
    	if ( artistiKohdalla != null && artistiKohdalla.getNimi().trim().equals("") ) {
    		    naytaVirhe("Nimi ei saa olla tyhj‰");
    		    return;
    		}
    		ModalController.closeStage(labelVirhe);
    }
    
    private static Artisti apuartisti = new Artisti();
    private int kentta = 0;
    private TextField[] edits;
    private Artisti artistiKohdalla;

    /**
     * Luodaan artistin kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mit‰ dataan n‰ytet‰‰n oletuksena
     * @param kentta mik‰ kentt‰ saa fokuksen kun n‰ytet‰‰n
     * @return null jos painetaan Cancel, muuten t‰ytetty tietue
     */
    public static Artisti kysyArtisti(Stage modalityStage, Artisti oletus, int kentta) {
        return ModalController.<Artisti, ArtistiLuontiController>showModal(
                    ArtistiLuontiController.class.getResource("ArtistiLuontiDialog.fxml"),
                    "Rekisteri",
                    modalityStage, oletus,
                    ctrl -> ctrl.setKentta(kentta) 
                );
    }

	private void setKentta(int kentta) {
        this.kentta = kentta;
    }

	/**
	 * Luodaan gridpaneen artistin tiedot
	 * @param gridArtisti mihin tiedot luodaan
	 * @return luodut tekstikent‰t
	 */
	public static TextField[] luoKentat(GridPane gridArtisti) {
		gridArtisti.getChildren().clear();
		TextField[] edits = new TextField[apuartisti.getKenttia()];
		
		for (int i=0, k = apuartisti.ekaKentta(); k < apuartisti.getKenttia(); k++, i++) {
		    Label label = new Label(apuartisti.getKysymys(k));
		    gridArtisti.add(label, 0, i);
		    TextField edit = new TextField();
		    edits[k] = edit;
		    edit.setId("e"+k);
		    gridArtisti.add(edit, 1, i);
		}
		return edits;
	}
	
	/**
	 * N‰ytet‰‰n artistin tiedot TextField komponentteihin
	 * @param edits taulukko TextFieldeist‰ johon n‰ytet‰‰n
	 * @param artisti n‰ytett‰v‰ artisti
	 */
	public static void naytaArtisti(TextField[] edits, Artisti artisti) {
	    if (artisti == null) return;
	    for (int k = artisti.ekaKentta(); k < artisti.getKenttia(); k++) {
	        edits[k].setText(artisti.anna(k));
	    }
	}
	
	/**
	 * N‰ytet‰‰n virheilmoitus siihen tarkoitetussa labelissa 
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
	
	/**
	 * Tyhjent‰‰n tekstikent‰t 
	 * @param edits tyhjennett‰v‰t kent‰t
	 */
	public static void tyhjenna(TextField[] edits) {
	    for (TextField edit: edits) 
	        if ( edit != null ) edit.setText(""); 
	}
	
	/**
	 * Palautetaan komponentin id:st‰ saatava luku
	 * @param obj tutkittava komponentti
	 * @param oletus mik‰ arvo jos id ei ole kunnollinen
	 * @return komponentin id lukuna 
	 */
	public static int getFieldId(Object obj, int oletus) {
	    if ( !( obj instanceof Node)) return oletus;
	    Node node = (Node)obj;
	    return Mjonot.erotaInt(node.getId().substring(1),oletus);
	}

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		alusta();
		
	}

	@Override
	public Artisti getResult() {
		return artistiKohdalla;
	}

	/**
	 * Mit‰ tehd‰‰n kun dialogi on n‰ytetty
	 */
	@Override
	public void handleShown() {
		kentta = Math.max(apuartisti.ekaKentta(), Math.min(kentta, apuartisti.getKenttia()-1));
		edits[kentta].requestFocus();
	}

	@Override
	public void setDefault(Artisti oletus) {
		artistiKohdalla = oletus;
		naytaArtisti(edits, artistiKohdalla);
		
	}
	
	/**
	 * Tekee tarvittavat muut alustukset.
	 */
	protected void alusta() {
	    edits = luoKentat(gridArtisti);
	    for (TextField edit : edits)
	        if ( edit != null )
	            edit.setOnKeyReleased( e -> kasitteleMuutosArtistiin((TextField)(e.getSource())));
	    //gridArtisti.setFitToHeight(true);
	}

	/**
	 * K‰sitell‰‰n artistiin tullut muutos
	 * @param edit muuttunut kentt‰
	 */
	private void kasitteleMuutosArtistiin(TextField edit) {
		 if (artistiKohdalla == null) return;
		 int k = getFieldId(edit,apuartisti.ekaKentta());
		 String s = edit.getText();
		 String virhe = null;
		 virhe = artistiKohdalla.aseta(k,s); 
		 if (virhe == null) {
		     Dialogs.setToolTipText(edit,"");
		     edit.getStyleClass().removeAll("virhe");
		     naytaVirhe(virhe);
		 } else {
		     Dialogs.setToolTipText(edit,virhe);
		     edit.getStyleClass().add("virhe");
		     naytaVirhe(virhe);
		 }
	}

	

	
}
