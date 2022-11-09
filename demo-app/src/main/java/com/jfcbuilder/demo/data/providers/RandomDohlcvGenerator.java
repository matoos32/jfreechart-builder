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

package com.jfcbuilder.demo.data.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import com.jfcbuilder.demo.data.providers.numeric.Sinusoid;
import com.jfcbuilder.demo.data.providers.numeric.Sinusoid.SinusoidParams;
import com.jfcbuilder.types.DohlcvSeries;

/**
 * Provides randomized Date Open High Low Close Volume (DOHLCV) data sets that follow a sinusoidal
 * summation trend line.
 */
public class RandomDohlcvGenerator implements IDohlcvProvider {

  private static final double MAX_TRENDLINE_AMPLITUDE = 2.0;
  private static final double MAX_CANDLE_RANGE_PERCENT_CHANGE = 0.10;
  private static final double MAX_VOLUME = 100_000_000.0;

  /**
   * Hidden constructor
   */
  private RandomDohlcvGenerator() {
    // Explicitly do nothing
  }

  /**
   * Factory method for create new instances of this class.
   * 
   * @return New instance of this class
   */
  public static RandomDohlcvGenerator get() {
    return new RandomDohlcvGenerator();
  }

  private static double randDouble() {
    return ThreadLocalRandom.current().nextDouble();
  }

  @Override
  public DohlcvSeries getDohlcv(long[] dateTimes) {

    Objects.requireNonNull(dateTimes, "Date-times cannot be null");

    final int numElems = dateTimes.length;

    List<Double> opens = new ArrayList<>(numElems);
    List<Double> highs = new ArrayList<>(numElems);
    List<Double> lows = new ArrayList<>(numElems);
    List<Double> closes = new ArrayList<>(numElems);
    List<Double> volumes = new ArrayList<>(numElems);

    // Low frequency (LF) sinusoid setup
    final double lfAmplitude = randDouble() * MAX_TRENDLINE_AMPLITUDE;
    final double lfYOffset = 2.0 * lfAmplitude;
    final SinusoidParams lfParams = new SinusoidParams(lfAmplitude, 1.0 / 120.0, lfYOffset);

    // High frequency(HF) sinusoid setup
    final double hfAmplitude = lfAmplitude * 0.70; // 70% attenuation of LF amplitude
    final double hfYOffset = 2.0 * hfAmplitude;
    final SinusoidParams hfParams = new SinusoidParams(hfAmplitude, 1.0 / 40.0, hfYOffset);

    boolean closeUp;
    double trendline, range, open, high, low, close, highShadow, lowShadow;

    for (int n = 0; n < numElems; n++) {

      trendline = Sinusoid.getCosineValue(lfParams, n) + Sinusoid.getSineValue(hfParams, n);

      range = MAX_CANDLE_RANGE_PERCENT_CHANGE * randDouble() * trendline;

      closeUp = randDouble() > 0.5;

      high = trendline + (range / 2.0);
      low = Math.max(trendline - (range / 2.0), 0.0);
      highShadow = high - (high * 0.05 * randDouble());
      lowShadow = low + (low * 0.05 * randDouble());
      close = closeUp ? highShadow : lowShadow;
      open = !closeUp ? highShadow : lowShadow;

      opens.add(open);
      highs.add(high);
      lows.add(low);
      closes.add(close);

      // volumes.add(MIN_VOLUME + ((MAX_VOLUME - MIN_VOLUME) * randDouble()));
      volumes.add(MAX_VOLUME * trendline / MAX_TRENDLINE_AMPLITUDE * randDouble());
    }

    double[] open_arr = opens.stream().mapToDouble(Double::doubleValue).toArray();
    double[] high_arr = highs.stream().mapToDouble(Double::doubleValue).toArray();
    double[] low_arr = lows.stream().mapToDouble(Double::doubleValue).toArray();
    double[] close_arr = closes.stream().mapToDouble(Double::doubleValue).toArray();
    double[] volume_arr = volumes.stream().mapToDouble(Double::doubleValue).toArray();

    return new DohlcvSeries(dateTimes, open_arr, high_arr, low_arr, close_arr, volume_arr);
  }
}
