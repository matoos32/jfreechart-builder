/*
 * jfreechart-builder-demo: a demonstration app for jfreechart-builder
 * 
 * (C) Copyright 2020, by Matt E.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.jfcbuilder.demo.data.providers.numeric;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Class for generating sinusoidal data.
 */
public class Sinusoid {

  private static final double TWO_PI = 2.0 * Math.PI;

  private static final double MAX_AMPLITUDE = 100.0;

  /**
   * Parameters that define the shape of a sinusoid.
   */
  public static class SinusoidParams {

    double amplitude;
    double freqScaleFactor;
    double amplitudeOffset;

    /**
     * Constructor
     * 
     * @param amplitude The amplitude of the sinusoid
     * @param freqScaleFactor Constant used to multiple the frequency component prior to calculating
     *        the sinusoid value
     * @param amplitudeOffset Amplitude offset of the sinusoid
     */
    public SinusoidParams(double amplitude, double freqScaleFactor, double amplitudeOffset) {
      this.amplitude = amplitude;
      this.freqScaleFactor = freqScaleFactor;
      this.amplitudeOffset = amplitudeOffset;
    }

    public double getAmplitude() {
      return amplitude;
    }

    public double getFreqScaleFactor() {
      return freqScaleFactor;
    }

    public double getAmplitudeOffset() {
      return amplitudeOffset;
    }
  }

  public static SinusoidParams getRandParams(double freqScaleFactor) {
    final double amplitude = ThreadLocalRandom.current().nextDouble() * MAX_AMPLITUDE;
    final double yOffset = 2.0 * amplitude;
    final double freqFactor = ThreadLocalRandom.current().nextDouble() / freqScaleFactor;
    return new SinusoidParams(amplitude, freqFactor, yOffset);
  }

  public static double[] getRandSeries(double freqScaleFactor, int numElems) {

    SinusoidParams params = Sinusoid.getRandParams(freqScaleFactor);
    
    double[] values = new double[numElems];

    if(ThreadLocalRandom.current().nextDouble() > 0.5) {
      for (int n = 0; n < numElems; n++) {
        values[n] = getCosineValue(params, n);
      }      
    } else {
      for (int n = 0; n < numElems; n++) {
        values[n] = getSineValue(params, n);
      }
    }

    return values;
  }

  public static double getCosineValue(SinusoidParams params, int n) {
    return params.getAmplitude() * Math.cos(TWO_PI * params.getFreqScaleFactor() * (double) n)
        + params.getAmplitudeOffset();
  }

  public static double getSineValue(SinusoidParams params, int n) {
    return params.getAmplitude() * Math.sin(TWO_PI * params.getFreqScaleFactor() * (double) n)
        + params.getAmplitudeOffset();
  }
}
