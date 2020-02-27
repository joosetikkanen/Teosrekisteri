package fxJopitikk;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Kysyt‰‰n rekisterin nimi ja luodaan t‰t‰ varten dialogi
 * 
 * @author Joose Tikkanen ja Pertti Arvola
 */
public class AloitusikkunaController implements ModalControllerInterface<String> {

    @FXML private ComboBoxChooser<String> cbVastaus;
    
    private String vastaus;

    @FXML private void handleCancel() {
    	ModalController.closeStage(cbVastaus);
    }

    @FXML private void handleOK() {
        //ModalController.showModal(JopitikkGUIController.class.getResource("JopitikkGUIView.fxml"), "Teosrekisteri", null, "");
    	vastaus = cbVastaus.getSelectedText();
    	ModalController.closeStage(cbVastaus);
    }


    /**
     * Luodaan nimenkysymisdialogi ja palautetaan valittu nimi tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @return null jos painetaan Cancel, muuten valittu nimi
     */
    public static String kysyNimi(Stage modalityStage) {
        return ModalController.showModal(
                AloitusikkunaController.class.getResource("AloitusikkunaGUI.fxml"),
                "Teosrekisteri", modalityStage, null);
    }


	@Override
	public String getResult() {
		return vastaus;
	}

	/**
	 * Mit‰ tehd‰‰n kun dialogi on n‰ytetty
	 */
	@Override
	public void handleShown() {
		cbVastaus.requestFocus();
	}

	@Override
	public void setDefault(String oletus) {
		cbVastaus.setAccessibleText(oletus);
	}
}