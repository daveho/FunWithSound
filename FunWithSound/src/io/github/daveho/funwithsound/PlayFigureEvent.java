package io.github.daveho.funwithsound;

/**
 * Event to play a {@link SimpleFigure} at a specified time.
 */
public class PlayFigureEvent {
	private SimpleFigure figure;
	long startUs;
	
	public PlayFigureEvent() {
		
	}
	
	public void setFigure(SimpleFigure figure) {
		this.figure = figure;
	}
	
	public SimpleFigure getFigure() {
		return figure;
	}
	
	public void setStartUs(long startTimeUs) {
		this.startUs = startTimeUs;
	}
	
	public long getStartUs() {
		return startUs;
	}
}
