/*
 * jfreechart-builder: a builder pattern module for working with the jfreechart library
 * 
 * (C) Copyright 2022, by Matt E. and project contributors
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

package com.jfcbuilder.builders;

import java.awt.Color;

import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.XYDataset;

import com.jfcbuilder.labels.VolumeXYToolTipGenerator;
import com.jfcbuilder.types.BuilderConstants;

/**
 * Builder for producing stock market volume plots using configured builder properties, series, and
 * datasets. Extends OhlcPlotBuilder to leverage it's bar drawing logic and to ensure bars in the
 * volume plot align with the corresponding ones in the price OHLC plot if one is configured.
 * Accomplishes this by overriding the generation of the XYDataset representing the OHLC series by
 * using volume OHLC values instead of price ones. Also allows superimposing other line series on
 * top like a moving average line. Also supports the ability to produce different series for
 * rendering "close up" and "close down" colors or rendering with a single color without
 * consideration of close up/down days.
 */
public class VolumeXYPlotBuilder extends OhlcPlotBuilder {

  private static final String DEFAULT_Y_AXIS_NAME = "Vol";

  VolumeXYTimeSeriesBuilder volumeSeriesBuilder;

  /**
   * Hidden constructor.
   */
  private VolumeXYPlotBuilder() {
    super();
    yAxisName(DEFAULT_Y_AXIS_NAME);
    volumeSeriesBuilder = null;
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static VolumeXYPlotBuilder get() {
    return new VolumeXYPlotBuilder();
  }

  @Override
  public OhlcPlotBuilder series(IXYTimeSeriesDatasetBuilder<?> dataset) {

    if (dataset instanceof VolumeXYTimeSeriesBuilder) {

      if (volumeSeriesBuilder != null) {
        throw new IllegalArgumentException(
            "More than one " + VolumeXYTimeSeriesBuilder.class.getSimpleName()
                + " being added but only one can be plotted.");
      }

      volumeSeriesBuilder = (VolumeXYTimeSeriesBuilder) dataset;

    } else {

      super.series(dataset);
    }

    return this;
  }

  @Override
  protected XYDataset buildOhlcDataset() throws IllegalStateException {

    if (volumeSeriesBuilder == null) {
      final String volumeSeriesBuilderName = VolumeXYTimeSeriesBuilder.class.getSimpleName();
      throw new IllegalStateException("No " + volumeSeriesBuilderName + " configured. Use series("
          + IXYTimeSeriesDatasetBuilder.class.getSimpleName()
          + "<T>) to add a just one instance of " + volumeSeriesBuilderName);
    }

    return volumeSeriesBuilder.indexRange(elements.indexRange()).timeData(elements.timeData())
        .build();
  }

  /**
   * Creates and configures a new candlestick renderer instance for use with volume plots.
   * 
   * @return New renderer instance
   */
  @Override
  protected CandlestickRenderer getCandleRenderer() {

    Color upColor = (volumeSeriesBuilder != null) ? volumeSeriesBuilder.upColor()
        : BuilderConstants.DEFAULT_UP_COLOR;

    Color downColor = (volumeSeriesBuilder != null) ? volumeSeriesBuilder.downColor()
        : BuilderConstants.DEFAULT_DOWN_COLOR;

   XYToolTipGenerator tooltipGenerator = new VolumeXYToolTipGenerator();

    return CandlestickRendererBuilder.get()
      .upColor(upColor)
      .downColor(downColor)
      .toolTipGenerator(tooltipGenerator)
      .build();
  }

}
