/*
 * jfreechart-builder-demo: a demonstration app for jfreechart-builder
 * 
 * (C) Copyright 2022, by Matt E.
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
     * @param freqScaleFactor Constant used to multiply the frequency component prior to calculating
     *        the sinusoid value. Lower and higher values are for lower and higher frequencies
     *        respectively
     * @param amplitudeOffset Amplitude offset of the sinusoid
     */
    public SinusoidParams(double amplitude, double freqScaleFactor, double amplitudeOffset) {
      this.amplitude = amplitude;
      this.freqScaleFactor = freqScaleFactor;
      this.amplitudeOffset = amplitudeOffset;
    }

    /**
     * Gets the configured sinusoid amplitude.
     * 
     * @return The amplitude value
     */
    public double getAmplitude() {
      return amplitude;
    }

    /**
     * Gets the configured sinusoid frequency scaling factor.
     * 
     * @return The scale factor value
     */
    public double getFreqScaleFactor() {
      return freqScaleFactor;
    }

    /**
     * Gets the configured sinusoid amplitude offset to use.
     * 
     * @return The offset value
     */
    public double getAmplitudeOffset() {
      return amplitudeOffset;
    }
  }

  /**
   * Helper method to generate random sinusoid configuration parameters.
   * 
   * @param freqScaleFactor A desired frequency scale factor
   * @return New {@link SinusoidParams} instance containing randomly generated parameters
   */
  public static SinusoidParams getRandParams(double freqScaleFactor) {
    final double amplitude = ThreadLocalRandom.current().nextDouble() * MAX_AMPLITUDE;
    final double yOffset = 2.0 * amplitude;
    final double freqFactor = ThreadLocalRandom.current().nextDouble() / freqScaleFactor;
    return new SinusoidParams(amplitude, freqFactor, yOffset);
  }

  /**
   * Generates a series of values representing a discrete random sinusoidal waveform. The waveform
   * will randomly be a sine or cosine series with random frequency, amplitude, and constant offset.
   * 
   * @param freqScaleFactor The frequency scaling factor to use
   * @param numElems The number of elements to produce in the series
   * @return New array containing the generated series
   */
  public static double[] getRandSeries(double freqScaleFactor, int numElems) {

    SinusoidParams params = Sinusoid.getRandParams(freqScaleFactor);

    double[] values = new double[numElems];

    if (ThreadLocalRandom.current().nextDouble() > 0.5) {
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

  /**
   * Calculates a cosine value at a discrete instant given a set of sinusoid parameters.
   * 
   * @param params The parameters to use in the calculation
   * @param n The discrete integer time instant
   * @return The calculated cosine value
   */
  public static double getCosineValue(SinusoidParams params, int n) {
    return params.getAmplitude() * Math.cos(TWO_PI * params.getFreqScaleFactor() * (double) n)
        + params.getAmplitudeOffset();
  }

  /**
   * Calculates a sine value at a discrete instant given a set of sinusoid parameters.
   * 
   * @param params The parameters to use in the calculation
   * @param n The discrete integer time instant
   * @return The calculated sine value
   */
  public static double getSineValue(SinusoidParams params, int n) {
    return params.getAmplitude() * Math.sin(TWO_PI * params.getFreqScaleFactor() * (double) n)
        + params.getAmplitudeOffset();
  }
}
