package edu.ycp.cs.funwithsound;

/**
 * Event to play a {@link Figure} at a specified time.
 */
public class PlayFigureEvent {
	private Figure figure;
	long startUs;
	
	public PlayFigureEvent() {
		
	}
	
	public void setFigure(Figure figure) {
		this.figure = figure;
	}
	
	public Figure getFigure() {
		return figure;
	}
	
	public void setStartUs(long startTimeUs) {
		this.startUs = startTimeUs;
	}
	
	public long getStartUs() {
		return startUs;
	}
}
