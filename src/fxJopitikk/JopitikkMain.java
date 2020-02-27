package fxJopitikk;
	
import albumirekisteri.Albumirekisteri;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * Pääohjelma Teosrekisteri-ohjelman käynnistämiseksi
 * @author Joose, Pertti
 * @version 5.4.2018
 *
 */
public class JopitikkMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			final FXMLLoader ldr = new FXMLLoader(getClass().getResource("JopitikkGUIView.fxml"));
	        final Pane root = (Pane)ldr.load();
	        final JopitikkGUIController appCtrl = (JopitikkGUIController)ldr.getController();
	
	        final Scene scene = new Scene(root);
	        scene.getStylesheets().add(getClass().getResource("jopitikk.css").toExternalForm());
	        primaryStage.setScene(scene);
	        // primaryStage.setTitle("Albumit");
	
	        primaryStage.setOnCloseRequest((event) -> {
	        	if ( !appCtrl.voikoSulkea() ) event.consume();
	            });
	        
	        Albumirekisteri rekisteri = new Albumirekisteri();
	        appCtrl.setAlbumirekisteri(rekisteri);
	        
	        
	        Application.Parameters params = getParameters();
	        if ( params.getRaw().size() > 0 )
	            appCtrl.lueTiedosto(params.getRaw().get(0));
	        else
	            if ( !appCtrl.avaa() ) Platform.exit();
	        
	        primaryStage.show();
        
	        } catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Käynnistetään käyttöliittymä    
	 * @param args komentorivin parametrit
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
