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
 * Describes a range of a sample to be played
 * by {@link SampleBankUGen}.
 */
class SampleRange {
	final double startMs;
	final double endMs;

	/**
	 * Constructor.
	 * 
	 * @param startMs  start time in milliseconds
	 * @param endMs    end time in milliseconds
	 */
	public SampleRange(double startMs, double endMs) {
		this.startMs = startMs;
		this.endMs = endMs;
	}
}
