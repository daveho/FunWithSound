// FunWithSound - A Java/Processing library for music composition
// Copyright 2015, David Hovemeyer <david.hovemeyer@gmail.com>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
