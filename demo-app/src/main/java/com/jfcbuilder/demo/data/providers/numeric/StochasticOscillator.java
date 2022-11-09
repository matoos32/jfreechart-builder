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

import java.util.Objects;

/**
 * Calculates a fast stochastic oscillator data series from a source series.
 */
public class StochasticOscillator {

  public static class StochData {

    private static final double[] EMPTY_DATA = {};

    private double[] pctK;
    private double[] pctD;

    private StochData() {
      pctK = EMPTY_DATA;
      pctD = EMPTY_DATA;
    }

    private StochData(double[] pctK, double[] pctD) {

      Objects.requireNonNull(pctK, "%K cannot be null");
      Objects.requireNonNull(pctD, "%D cannot be null");

      this.pctK = pctK;
      this.pctD = pctD;
    }

    public static double[] getEmptyData() {
      return EMPTY_DATA;
    }

    public double[] getPctK() {
      return pctK;
    }

    public double[] getPctD() {
      return pctD;
    }
  }

  /**
   * Calculates a fast stochastic series for K-period and D-period smoothed version of K based on
   * supplied source data. The first values are set to NaN until a first number of values in the
   * specified periods are encountered.
   * 
   * @param K The period over which to calculate the un-smoothed %K values
   * @param D The period over which to calculate the SMA of the un-smoothed %K values
   * @param highs The source high values from which to calculate the stochastic oscillator values
   * @param lows The source low values from which to calculate the stochastic oscillator values
   * @param closes The source close values from which to calculate the stochastic oscillator values
   * @return New instance of a StochData referencing the calculated %K and %D series
   * @throws IllegalArgumentException If K or D are smaller than one (1), if any source array is
   *         null, or if the source arrays don't all have the same length.
   */
  public static StochData calculate(int K, int D, double[] highs, double[] lows, double[] closes)
      throws IllegalArgumentException {

    if (K < 1 || D < 1) {
      throw new IllegalArgumentException("K and D must be greater than zero");
    }

    if (highs == null || lows == null || closes == null) {
      throw new IllegalArgumentException("Highs, lows, and closes cannot be null");
    }

    if (highs.length != lows.length && highs.length != closes.length) {
      throw new IllegalArgumentException("Source series have length mismatch");
    }

    final int numElems = highs.length;

    if (numElems == 0) {
      return new StochData();
    }

    // If here there is at least one element.

    double[] pctK = new double[numElems];

    double periodLow = lows[0];
    double periodHigh = highs[0];

    for (int nSource = 0; nSource < numElems; nSource++) {

      if (nSource < K) {
        // Not enough values yet to look back over the entire first K period.
        pctK[nSource] = Double.NaN;
        continue;
      }

      // Iterate the K period starting at the first element back from the current source element
      // going to the current source element.
      for (int nK = nSource - K + 1; (nK <= nSource) && (nK < numElems); nK++) {
        if (nK < nSource) {
          // Haven't reached the source element at nSource so still inside the current K period
          periodLow = Math.min(periodLow, lows[nK]);
          periodHigh = Math.max(periodHigh, highs[nK]);
        } else {
          // The current K period has been completed.
          pctK[nSource] = 100.0 * (closes[nSource] - periodLow) / (periodHigh - periodLow);
        }
      }
    }
    
    // Now calculate %D from the %K series we just created.
    final double[] pctD = Sma.calculate(D, pctK);
    
    return new StochData(pctK, pctD);
  }
}
