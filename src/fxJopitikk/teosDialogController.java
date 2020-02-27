/**
 * 
 */
package fxJopitikk;

import java.net.URL;
import java.util.ResourceBundle;

import albumirekisteri.Albumi;
import albumirekisteri.Artisti;
import fi.jyu.mit.fxgui.ComboBoxChooser;
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
public class teosDialogController implements ModalControllerInterface<Albumi>,Initializable {
	
	@FXML private GridPane gridAlbumi;
	@FXML private Label labelVirhe;
    @FXML private ComboBoxChooser<String> cbArtistit;

	//@FXML private static ComboBoxChooser<Artisti> cbArtistit;
	
	 private static Albumi apuAlbumi = new Albumi();
	 private int kentta = 0;
	 private TextField[] edits;
	 private Albumi albumiKohdalla;
	 private static Artisti[] artistit;
	 //private static JopitikkGUIController ctrl;

	@FXML
	void handleCancel() {
		albumiKohdalla = null;
    	ModalController.closeStage(labelVirhe);
	}

	@FXML
	void handleOK() {
	    if (cbArtistit.getSelectedObject() == null) {
            naytaVirhe("Valitse artisti");
            return;
        }
		if ( albumiKohdalla != null && albumiKohdalla.getNimi() == null || albumiKohdalla.getNimi().trim().equals("")) {
		    naytaVirhe("Nimi ei saa olla tyhj‰");
		    return;
		}
		
		ModalController.closeStage(labelVirhe);
	}
	
	 @FXML
	 void handleArtisti() {
	     kasitteleArtisti();
	 }
	 
	 /**
	  * Yhdistet‰‰n comboboxista valittu artisti valitulle albumille
	  */
	 private void kasitteleArtisti() {
	     String artistinNimi = cbArtistit.getSelectedObject();
         int artistiID = -1;
         for (int i = 0; i < artistit.length; i++) {
            if (artistit[i].getNimi().equals(artistinNimi)) artistiID = artistit[i].getTunnusNro();
         }
         albumiKohdalla.aseta(1, "" + artistiID);
         
         for (TextField edit : edits)
             if ( edit != null && edit.getLength() > 0 )
                 kasitteleMuutosAlbumiin(edit);
	 }

    /**
     * N‰ytet‰‰n virhe siihen tarkoitettuun labeliin
     * @param virhe n‰ytett‰v‰ virheteksti
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

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		alusta();
		
	}

	/**
	 * Tekee tarvittavat muut alustukset.
	 */
	protected void alusta() {
		
	    for (int i = 0; i < artistit.length; i++) {
            cbArtistit.add(artistit[i].getNimi());
        }
	    edits = luoKentat(gridAlbumi);
	    for (TextField edit : edits)
	        if ( edit != null )
	            edit.setOnKeyReleased( e -> kasitteleMuutosAlbumiin((TextField)(e.getSource())));
	    
	    //gridArtisti.setFitToHeight(true);
	}

	/**
	 * K‰sitell‰‰n albumiin tullut muutos
	 * @param edit muuttunut kentt‰
	 */
	private void kasitteleMuutosAlbumiin(TextField edit) {
		 if (albumiKohdalla == null || cbArtistit.getSelectedObject() == null) return;
		 int k = getFieldId(edit,apuAlbumi.ekaKentta());
		 String s = edit.getText();
		 String virhe = null;
		 virhe = albumiKohdalla.aseta(k,s);
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
	
	/**
	 * Luodaan gridpaneen albumin tiedot
     * @param gridAlbumi mihin tiedot luodaan
     * @return luodut tekstikent‰t
	 */
	public static TextField[] luoKentat(GridPane gridAlbumi) {
		gridAlbumi.getChildren().clear();
		
		TextField[] edits = new TextField[apuAlbumi.getKenttia()];
		
		for (int i = 0, k = apuAlbumi.ekaKentta(); k < apuAlbumi.getKenttia(); k++, i++) {
		    Label label = new Label(apuAlbumi.getKysymys(k));
		    gridAlbumi.add(label, 0, i);
		    TextField edit = new TextField();
		    edits[k] = edit;
		    edit.setId("e"+k);
		    gridAlbumi.add(edit, 1, i);
		}
		return edits;
	}

	@Override
	public Albumi getResult() {
		return albumiKohdalla;
	}

	/**
	 * Mit‰ tehd‰‰n kun dialogi on n‰ytetty
	 */
	@Override
	public void handleShown() {
		kentta = Math.max(apuAlbumi.ekaKentta(), Math.min(kentta, apuAlbumi.getKenttia()-1));
		edits[kentta].requestFocus();
	}

	@Override
	public void setDefault(Albumi oletus) {
		albumiKohdalla = oletus;
		naytaAlbumi(edits, albumiKohdalla);
		
	}

	/**
	 * N‰ytet‰‰n artistin tiedot TextField komponentteihin
	 * @param edits taulukko TextFieldeist‰ johon n‰ytet‰‰n
	 * @param albumi n‰ytett‰v‰ albumi
	 */
	public static void naytaAlbumi(TextField[] edits, Albumi albumi) {
	    if (albumi == null) return;
	    for (int k = albumi.ekaKentta(); k < albumi.getKenttia(); k++) {
	        edits[k].setText(albumi.anna(k));
	    }
	}
	/**
     * Luodaan albumin kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mit‰ dataan n‰ytet‰‰n oletuksena
     * @param kentta mik‰ kentt‰ saa fokuksen kun n‰ytet‰‰n
	 * @param artistitNyt viite nykyisiin artisteihin
     * @return null jos painetaan Cancel, muuten t‰ytetty tietue
     */
    public static Albumi kysyAlbumi(Stage modalityStage, Albumi oletus, int kentta, Artisti[] artistitNyt) {
    	artistit = artistitNyt;
        return ModalController.<Albumi, teosDialogController>showModal(
        		teosDialogController.class.getResource("teosDialog.fxml"),
                    "Rekisteri",
                    modalityStage, oletus,
                    ctrl -> ctrl.setKentta(kentta) 
                );
    }


	private void setKentta(int kentta) {
        this.kentta = kentta;
    }

	/**
	 * Tyhjent‰‰n tekstikent‰t 
	 * @param edits tyhjennett‰v‰t kent‰t
	 */
	public static void tyhjenna(TextField[] edits) {
	    for (TextField edit: edits) 
	        if ( edit != null ) edit.setText(""); 
	}

}
