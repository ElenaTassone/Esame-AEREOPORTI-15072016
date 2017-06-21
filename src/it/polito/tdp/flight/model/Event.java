package it.polito.tdp.flight.model;

import java.time.LocalTime;

public class Event implements Comparable < Event > {
	
	private Airport partenza ;
	private Airport arrivo ;
	private LocalTime orario ;
	private int day;
	
	
	public Airport getArrivo() {
		return arrivo;
	}

	public void setArrivo(Airport arrivo) {
		this.arrivo = arrivo;
	}

	public LocalTime getOrario() {
		return orario;
	}

	public void setOrario(LocalTime orario) {
		this.orario = orario;
	}

	public Event(Airport partenza, Airport arrivo, LocalTime orario, int day ) {
		super();
		this.partenza = partenza;
		this.arrivo = arrivo;
		this.orario = orario;
		this.day = day ;
	}

	@Override
	public int compareTo(Event o) {				
		return this.getOrario().getHour()-o.getOrario().getHour();
	}

	public Airport getPartenza() {
		return partenza;
	}

	public void setPartenza(Airport partenza) {
		this.partenza = partenza;
	}

	public int getDay() {
		return day;
	}

	public void addDay() {
		this.day++;
	}
	
	

}
