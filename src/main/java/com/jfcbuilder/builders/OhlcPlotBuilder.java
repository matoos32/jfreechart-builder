/*
 * jfreechart-builder: a builder pattern module for working with the jfreechart library
 * 
 * (C) Copyright 2020, by Matt E. and project contributors
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
import java.awt.Paint;
import java.util.List;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import com.jfcbuilder.builders.renderers.CandlestickRendererBuilder;
import com.jfcbuilder.builders.types.BuilderConstants;
import com.jfcbuilder.builders.types.Orientation;
import com.jfcbuilder.builders.types.ZeroBasedIndexRange;

/**
 * Builder for producing stock market Open High Low Close (OHLC) plots using configured builder
 * properties, series, and datasets.
 */
public class OhlcPlotBuilder implements IXYPlotBuilder<OhlcPlotBuilder> {

  private static final String DEFAULT_Y_AXIS_NAME = "Price";

  private XYTimeSeriesPlotBuilderElements elements;
  private OhlcSeriesBuilder ohlcSeriesBuilder;

  /**
   * Hidden constructor.
   */
  private OhlcPlotBuilder() {
    elements = new XYTimeSeriesPlotBuilderElements();
    ohlcSeriesBuilder = null;
    yAxisName(DEFAULT_Y_AXIS_NAME);
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static OhlcPlotBuilder get() {
    return new OhlcPlotBuilder();
  }

  @Override
  public OhlcPlotBuilder indexRange(ZeroBasedIndexRange indexRange) {
    elements.indexRange(indexRange);
    return this;
  }

  @Override
  public OhlcPlotBuilder xAxis(ValueAxis xAxis) {
    elements.xAxis(xAxis);
    return this;
  }

  @Override
  public OhlcPlotBuilder timeData(long[] timeData) {
    elements.timeData(timeData);
    return this;
  }

  @Override
  public OhlcPlotBuilder series(IXYTimeSeriesBuilder<?> series) {
    elements.series(series);
    return this;
  }

  /**
   * Same as interface {@code IXYPlotBuilder<?>.series(IXYDatasetBuilder<?>)} but also checks that
   * only one {@code OhlcSeriesBuilder} is ever added because only one is allowed per plot.
   * 
   * @see IXYPlotBuilder#series(IXYTimeSeriesBuilder)
   * @throws IllegalArgumentException If an OhlcSeriesBuilder was already added.
   */
  @Override
  public OhlcPlotBuilder series(IXYDatasetBuilder<?> dataset) {

    if (dataset instanceof OhlcSeriesBuilder) {

      if (ohlcSeriesBuilder != null) {
        throw new IllegalArgumentException(
            "More than one " + OhlcSeriesBuilder.class.getSimpleName()
                + " being added but only one can be plotted.");
      }

      ohlcSeriesBuilder = (OhlcSeriesBuilder) dataset;

    } else {

      elements.dataset(dataset);
    }

    return this;
  }

  @Override
  public OhlcPlotBuilder line(LineBuilder line) {
    elements.line(line);
    return this;
  }

  @Override
  public OhlcPlotBuilder annotation(IXYAnnotationBuilder<?> annotation) {
    elements.annotation(annotation);
    return this;
  }

  @Override
  public OhlcPlotBuilder plotWeight(int weight) {
    elements.plotWeight(weight);
    return this;
  }

  @Override
  public int plotWeight() {
    return elements.plotWeight();
  }

  @Override
  public OhlcPlotBuilder yAxisName(String name) {
    elements.yAxisName(name);
    return this;
  }

  @Override
  public OhlcPlotBuilder yAxisRange(double lower, double upper) throws IllegalArgumentException {
    elements.yAxisRange(lower, upper);
    return this;
  }

  @Override
  public OhlcPlotBuilder yAxisTickSize(double size) {
    elements.yAxisTickSize(size);
    return this;
  }

  @Override
  public OhlcPlotBuilder backgroundColor(Paint color) {
    elements.backgroundColor(color);
    return this;
  }

  @Override
  public OhlcPlotBuilder axisFontColor(Paint color) {
    elements.axisFontColor(color);
    return this;
  }

  @Override
  public OhlcPlotBuilder axisColor(Paint color) {
    elements.axisColor(color);
    return this;
  }

  @Override
  public OhlcPlotBuilder gridLines() {
    elements.gridLines();
    return this;
  }

  @Override
  public OhlcPlotBuilder noGridLines() {
    elements.noGridLines();
    return this;
  }

  private void checkBuildPreconditions() throws IllegalStateException {

    elements.checkBuildPreconditions();

    if (ohlcSeriesBuilder == null) {
      final String ohlcBuilderName = OhlcSeriesBuilder.class.getSimpleName();
      throw new IllegalStateException("No " + ohlcBuilderName + " configured. Use series("
          + IXYDatasetBuilder.class.getSimpleName() + ") to add a just one instance of "
          + ohlcBuilderName);
    }
  }

  /**
   * Builds the DOHLC plot. The DOHLC series is shown behind all other series that were added.
   * 
   * @return New instance of an plot corresponding to all configured data sets and settings
   * @throws IllegalStateException If an OhlcSeriesBuilder, if time axis was not set, or if time
   *         data was not set.
   */
  @Override
  public XYPlot build() throws IllegalStateException {

    checkBuildPreconditions();

    final ValueAxis xAxis = elements.xAxis();
    xAxis.setAxisLinePaint(elements.axisColor());
    xAxis.setTickLabelPaint(elements.axisFontColor());

    final long[] timeData = elements.timeData();

    ohlcSeriesBuilder.indexRange(elements.indexRange());
    ohlcSeriesBuilder.timeData(elements.timeData());
    XYDataset ohlc = ohlcSeriesBuilder.build();

    // TODO: Extract this code common with other classes to a factory method
    NumberAxis yAxis = new NumberAxis();
    yAxis.setMinorTickMarksVisible(true);
    yAxis.setMinorTickCount(2);
    yAxis.setMinorTickMarkOutsideLength(2);
    yAxis.setTickLabelFont(BuilderConstants.DEFAULT_FONT);
    yAxis.setAutoRangeIncludesZero(false);
    yAxis.setAutoRangeStickyZero(false);
    yAxis.setAxisLinePaint(elements.axisColor());
    yAxis.setTickLabelPaint(elements.axisFontColor());

    XYPlot plot = new XYPlot(ohlc, xAxis, yAxis, getCandleRenderer());
    plot.setBackgroundPaint(elements.backgroundColor());
    plot.setForegroundAlpha(0.65f); // TODO: is this alpha needed?
    plot.setDomainGridlinesVisible(elements.showGridLines());
    plot.setRangeGridlinesVisible(elements.showGridLines());
    plot.setDomainGridlinePaint(BuilderConstants.DEFAULT_GRIDLINE_PAINT);
    plot.setRangeGridlinePaint(BuilderConstants.DEFAULT_GRIDLINE_PAINT);

    StringBuilder axisSubName = new StringBuilder();

    final List<IXYTimeSeriesBuilder<?>> seriesBuilders = elements.unmodifiableSeries();

    if (!seriesBuilders.isEmpty()) {

      StandardXYItemRenderer renderer = new StandardXYItemRenderer();
      TimeSeriesCollection collection = new TimeSeriesCollection();

      for (IXYTimeSeriesBuilder<?> builder : seriesBuilders) {

        builder.indexRange(elements.indexRange());
        builder.timeData(timeData);
        TimeSeries series = builder.build();

        if (!series.isEmpty()) {
          renderer.setSeriesPaint(collection.getSeriesCount(), builder.color());
          renderer.setSeriesStroke(collection.getSeriesCount(), builder.style());
          collection.addSeries(series);

          // JFreeChart series key is the name given to the series.
          if (series.getKey() != null && !series.getKey().toString().isEmpty()) {
            axisSubName.append(' ').append(series.getKey().toString());
          }
        }
      }

      for (LineBuilder builder : elements.unmodifiableLines()) {
        ValueMarker line = builder.build();

        if (builder.orientation() == Orientation.HORIZONTAL) {
          plot.addRangeMarker(line);
        } else {
          plot.addDomainMarker(line);
        }
      }

      for (IXYAnnotationBuilder<?> builder : elements.unmodifiableAnnotations()) {
        // Annotations don't have ability to get their max/min y-value to adjust y-axis range :(
        plot.addAnnotation(builder.build());
      }

      yAxis.setLabel(elements.yAxisName() + axisSubName.toString());

      final int seriesIndex = plot.getSeriesCount();

      // We don't create a new axis but map datasets to the time axis (i.e. domain axis used for the
      // main OHLCSeriesCollection data). Now create the dataset to axis mapping prior to even
      // adding the dataset so that when the dataset is added the min/max value range of the axis is
      // properly auto-calculated and set.
      plot.mapDatasetToDomainAxis(seriesIndex, 0);

      // Now also set the renderer that will be used because it will be used to determine the axis
      // min/max value range when this is auto-calculated. FIXME: Candlestick renderer offsets
      // center of candle from x-axis start pixel. When rendering general xy lines, those line
      // points are drawn at the x-axis start pixel. This causes indicator lines to be misaligned by
      // a few pixels from the OHLC bars and may provide false signals to those looking at charts.
      plot.setRenderer(seriesIndex, renderer, false);

      // We're now ready to add the dataset
      plot.setDataset(seriesIndex, collection);

    }

    return plot;
  }

  /**
   * Creates and configures a new candlestick renderer instance for use with plots
   * 
   * @return New renderer instance
   */
  private CandlestickRenderer getCandleRenderer() {

    Color upColor = (ohlcSeriesBuilder != null) ? ohlcSeriesBuilder.upColor()
        : BuilderConstants.DEFAULT_UP_COLOR;

    Color downColor = (ohlcSeriesBuilder != null) ? ohlcSeriesBuilder.downColor()
        : BuilderConstants.DEFAULT_DOWN_COLOR;

    return CandlestickRendererBuilder.get().upColor(upColor).downColor(downColor).build();
  }

}
