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
 * Calculates an N-period simple moving average (SMA) from a source series.
 */
public class Sma {

  /**
   * Executes the calculation.
   * 
   * @param period The period (eg: 10, 20, 50, 200) over which to calculate the SMA values
   * @param source The source values from which to calculate the SMA
   * @return New array containing the SMA values. The first elements in the array are set to NaN
   *         until an initial first period is reached.
   * @throws NullPointerException If source is null.
   * @throws IllegalArgumentException If period is smaller than one (1).
   */
  public static double[] calculate(int period, double[] source) throws IllegalArgumentException {
    
    Objects.requireNonNull(source);
    
    if(period < 1) {
      throw new IllegalArgumentException("Period must be greater than zero");
    }
    
    final int numElems = source.length;
    
    double[] result = new double[source.length];
    
    int n = 0;
    
    for( ; n < period && n < numElems; n++) {
      result[n] = Double.NaN;
    }
    
    for( ; n < numElems; n++) {
      result[n] = calcAverage(n - period, n, source);
    }
    
    return result;
  }

  private static double calcAverage(int startIndex, int endIndex, double[] source) {
    
    double sum = 0.0;
    
    for(int n = startIndex; n <= endIndex; n++) {
      sum += source[n];
    }
    
    final int numElems = endIndex - startIndex + 1;
    
    return sum / (double) numElems; 
  }

}
