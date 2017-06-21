package it.polito.tdp.flight;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.flight.model.Airport;
import it.polito.tdp.flight.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FlightController {

	private Model model;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField txtDistanzaInput;

	@FXML
	private TextField txtPasseggeriInput;

	@FXML
	private TextArea txtResult;

	@FXML
	void doCreaGrafo(ActionEvent event) {
		String s = txtDistanzaInput.getText() ;
		try{
			int distanza = Integer.parseInt(s) ;
			if(model.selezionaRotte(distanza)==true)
				txtResult.setText("è possibile raggiungere tutti gli aereoporti da ogni aereooporto \n");
			else
				txtResult.setText("non è possibile raggiungere tutti gli aereoporti da ogni aereooporto \n");
			txtResult.appendText("L'aeroporto più lontano da Fiumicino e raggiungibile da esso è "+model.getFiumicino()+"\n");
		}
		catch (NumberFormatException e){
			txtResult.setText("ERRORE: Inserire un numero");
		}
	}

	@FXML
	void doSimula(ActionEvent event) {
		String input = txtPasseggeriInput.getText() ;
		try {
			int k = Integer.parseInt(input) ;
			Collection<Airport> lista = model.getSimulazione(k);
			txtResult.clear();
			for(Airport a : lista){
				txtResult.appendText(a+":"+a.getPasseggeri()+" passeggeri \n");
			}
			
		}
		catch(NumberFormatException e){
			txtResult.setText("ERRORE: inserire un numero di passeggeri");
		}
		
	}

	@FXML
	void initialize() {
		assert txtDistanzaInput != null : "fx:id=\"txtDistanzaInput\" was not injected: check your FXML file 'Untitled'.";
		assert txtPasseggeriInput != null : "fx:id=\"txtPasseggeriInput\" was not injected: check your FXML file 'Untitled'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Untitled'.";

	}

	public void setModel(Model model) {
		this.model = model;
	}
}
