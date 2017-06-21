package it.polito.tdp.flight.model;

import java.time.LocalTime;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.WeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class Simulator {
	
	private Queue<Event> queue ;
	private Random rn ;
	private int day ;
	
	
	public Simulator(){
		this.queue = new PriorityQueue<Event> () ;
		this.rn = new Random() ;
		this.day = 1 ;
	}
	
	public void run(List<Airport> lista, WeightedGraph<Airport,Route> grafo, int passeggeri) {
		LocalTime inizio = LocalTime.of(06, 00);
		
		for(int i = 0 ; i < passeggeri ; i++){
		
			int random1 = rn.nextInt(lista.size()) ;
			Airport partenza = lista.get(random1) ;
			
			List<Airport> destinazioni = Graphs.neighborListOf(grafo, partenza) ;
			int random2 = rn.nextInt(destinazioni.size());
			Airport arrivo = destinazioni.get(random2);
						
			Event temp = new Event (partenza,arrivo ,inizio,day) ;
			partenza.addPasseggero();
			queue.add(temp);
		}
		while(!queue.isEmpty()){
			
			Event e = queue.poll(); 
			
			if(e.getDay()<=2)
			{
			Airport p = e.getPartenza() ;
			Airport a = e.getArrivo() ;
			// rimuovo un passeggero e lo sposto nell'altro
			p.removePasseggero();
			a.addPasseggero();
			
			// nuovi aereoporti raggiungibili
			List<Airport> d = Graphs.neighborListOf(grafo, a) ;
			int random = rn.nextInt(d.size());
			// nuovo aereoporto da raggiungere
			Airport newArrivo = d.get(random);
			
			double tempo = 60*(e.getOrario().getHour()+this.getDurata(p, a)) ;
			int ora = (int) (tempo/60) ;
			int minute = (int) (tempo%60);
			
			LocalTime oraArrivo = LocalTime.of(ora, minute);
			
			System.out.println("Da "+p+" a"+a+"in "+ oraArrivo);
			boolean successivo = true;
			LocalTime oraPartenza = null;
			for(int i = 7; i <= 23 ; i=i+2){
				if(oraArrivo.getHour()<i){
					oraPartenza = LocalTime.of(i, 0) ;
					successivo = false;
					break ;
				}	
			}
			int giorno=1;
			if(successivo == true){
				day++;
				oraPartenza = LocalTime.of(06,0) ; 
			}
			
			
			Event nuovo = new Event (a, newArrivo, oraPartenza, day) ;
			
			queue.add(nuovo);
			}
		}
		
		
	}

	public double getDurata (Airport partenza, Airport arrivo){
		double distanza =LatLngTool.distance(partenza.getCoords(), arrivo.getCoords(), LengthUnit.KILOMETER) ;
		return distanza/800;
		}

			
}
