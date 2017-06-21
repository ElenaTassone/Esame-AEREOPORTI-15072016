package it.polito.tdp.flight.model;
import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.flight.db.FlightDAO;

public class Model {

	private FlightDAO dao ;
	private Map<Integer, Airline> compagnie ;
	private Map<Integer, Airport> tuttiAereoporti ;
	private WeightedGraph<Airport, Route> grafo ;
	private WeightedGraph<Airport, Route> grafoRidotto ;
	private List<Route> rotte ;
	private Simulator sim ;
	
	public Model(){
		this.dao = new FlightDAO () ;
		// se non va spostare sotto
		this.compagnie = this.getCompagnie () ;
		this.tuttiAereoporti = this.getTuttiAeroporti() ;
		this.rotte = this.getRotte();
		
	}
	
	
	
	private List<Route> getRotte() {
		if(rotte== null)
			rotte = dao.getAllRoutes();
		return rotte;
	}



	public boolean selezionaRotte(int distanza) {
		this.grafo = new DirectedWeightedMultigraph<Airport, Route> (Route.class);
		// aggiungo TUTTI gli aereoporti
		Graphs.addAllVertices(grafo, this.tuttiAereoporti.values()) ;
		//aggiungo gli archi 
		this.addArchi(distanza) ;
		System.out.println(grafo);
		
		return this.raggiungibile(grafo) ;
	}



	private boolean raggiungibile(WeightedGraph<Airport, Route> graph) {
		List<Airport> elimina = new ArrayList<Airport> ();
		for(Airport a : graph.vertexSet()){
			// se l'airport non ha archi devo toglierlo
			if(grafo.edgesOf(a).size()==0)
				elimina.add(a);
		}
		grafoRidotto =  new DirectedWeightedMultigraph<Airport, Route>(Route.class) ;
		graph.removeAllVertices(elimina) ;
		grafoRidotto = graph ;
		
		for(Airport a : graph.vertexSet()){
			if(Graphs.neighborListOf(graph, a).size()!=graph.vertexSet().size())
				return false ;
		}
			return true;
	}



	private void addArchi(int max) {
		for(Route r : rotte){
			int idA1 = r.getSourceAirportId() ;
			int idA2 = r.getDestinationAirportId() ;
			Airport partenza = tuttiAereoporti.get(idA1) ;
			Airport arrivo = tuttiAereoporti.get(idA2) ;
			double distanza = this.getDistanza(partenza, arrivo);
			if(distanza<=max && !arrivo.equals(partenza)){
				grafo.addEdge(partenza, arrivo, r);
				// setto peso
				grafo.setEdgeWeight(r,this.getPeso(distanza)) ;
			}
		}
		
		
	}




	private double getPeso(double distanza) {
		return distanza/800;
		
	}



	private double getDistanza(Airport partenza, Airport arrivo) {
		double distanza =LatLngTool.distance(partenza.getCoords(), arrivo.getCoords(), LengthUnit.KILOMETER) ;
		return distanza;
	}



	private Map<Integer, Airport> getTuttiAeroporti() {
		if(this.tuttiAereoporti == null)
			tuttiAereoporti = dao.getAllAirports() ;
		return tuttiAereoporti;
	}



	private Map<Integer, Airline> getCompagnie() {
		if(this.compagnie == null)
			compagnie = dao.getAllAirlines() ;
		return compagnie;
	}



	public Airport getFiumicino() {
		Airport best = null ;
		double max = 0 ;
		Airport fiumicino = null ;
		for(Airport a : this.grafo.vertexSet()){
			if (a.getName().compareTo("Fiumicino")==0)
				fiumicino = a ;
		}
		
		for(Airport a : Graphs.neighborListOf(grafo, fiumicino)){
			if(this.getDistanza(fiumicino, a)>max){
				max = this.getDistanza(fiumicino, a) ;
				best = a ;
			}
		}
//		System.out.println(fiumicino);
		return best;
	}



	public Collection<Airport> getSimulazione(int k) {
		this.sim = new Simulator() ;
		
		List<Airport> lista = new ArrayList<Airport> () ;
		for(Airport a : grafoRidotto.vertexSet())
			lista.add(a) ;
		
		sim.run(lista, grafoRidotto, k);
		
		Collections.sort(lista);
		
		return lista;
	}

}
