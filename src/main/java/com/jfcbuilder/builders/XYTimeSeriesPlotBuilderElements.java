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

import java.awt.Paint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;

import com.jfcbuilder.types.ZeroBasedIndexRange;

/**
 * Helper class for storing and accessing properties common to different kinds of XY plot builders.
 * Intended for use in composition-type implementations.
 */
class XYTimeSeriesPlotBuilderElements {

  /**
   * Value used to indicate the default y-axis tick size should be used.
   * 
   * @see XYTimeSeriesPlotBuilderElements#usingDefaultYAxisTickSize()
   */
  private static final double USE_DEFAULT_Y_AXIS_TICK_SIZE = Double.NaN;

  private static final int DEFAULT_PLOT_WEIGHT = 1;

  private static final Paint DEFAULT_BACKGROUND_COLOR = XYPlot.DEFAULT_BACKGROUND_PAINT;
  private static final Paint DEFAULT_AXIS_FONT_COLOR = Axis.DEFAULT_TICK_LABEL_PAINT;
  private static final Paint DEFAULT_AXIS_COLOR = Axis.DEFAULT_AXIS_LINE_PAINT;

  private List<IXYTimeSeriesBuilder<?>> seriesBuilders;
  private List<IXYDatasetBuilder<?>> datasetBuilders;
  private List<LineBuilder> lineBuilders;
  private List<IXYAnnotationBuilder<?>> annotationBuilders;
  private ValueAxis xAxis;
  private long[] timeData;
  private ZeroBasedIndexRange indexRange;
  private int plotWeight;
  private String yAxisName;
  private Range yAxisRange;
  private double yAxisTickSize;
  private Paint backgroundColor;
  private Paint axisFontColor;
  private Paint axisColor;
  private boolean showGridLines;

  /**
   * Constructor.
   */
  public XYTimeSeriesPlotBuilderElements() {
    seriesBuilders = new ArrayList<>();
    datasetBuilders = new ArrayList<>();
    lineBuilders = new ArrayList<>();
    annotationBuilders = new ArrayList<>();
    xAxis = null;
    timeData = null;
    indexRange = null;
    plotWeight = DEFAULT_PLOT_WEIGHT;
    yAxisName = "";
    yAxisRange = null;
    yAxisTickSize = USE_DEFAULT_Y_AXIS_TICK_SIZE;
    backgroundColor = DEFAULT_BACKGROUND_COLOR;
    axisFontColor = DEFAULT_AXIS_FONT_COLOR;
    axisColor = DEFAULT_AXIS_COLOR;
    showGridLines = false;
  }

  /**
   * Sets the zero-based index range to be used with all data series.
   * 
   * @param indexRange The zero-based index range to be set
   */
  public void indexRange(ZeroBasedIndexRange indexRange) {
    this.indexRange = indexRange;
  }

  /**
   * Gets the zero-based index range to be used with all data series.
   * 
   * @return The zero-based index range that will be used
   */
  public ZeroBasedIndexRange indexRange() {
    return indexRange;
  }

  /**
   * Sets the x-axis to be used with all data series.
   * 
   * @param xAxis The x-axis to be set
   */
  public void xAxis(ValueAxis xAxis) {
    Objects.requireNonNull(xAxis, "X-axis cannot be null");
    this.xAxis = xAxis;
  }

  /**
   * Gets the x-axis to be used with all data series.
   * 
   * @return The x-axis that will be used
   */
  public ValueAxis xAxis() {
    return xAxis;
  }

  /**
   * Sets the time data to be used with all data series. Values should be in ascending order and
   * representing milliseconds since the epoch start.
   * 
   * @param timeData The time data to be set
   */
  public void timeData(long[] timeData) {
    Objects.requireNonNull(timeData, "Time data cannot be null");
    this.timeData = timeData;
  }

  /**
   * Gets the time data to be used with all data series.
   * 
   * @return The time data array that was configured
   */
  public long[] timeData() {
    return timeData;
  }

  /**
   * Registers an XY time series builder to be used for building the plot.
   * 
   * @param series The builder to be registered
   */
  public void series(IXYTimeSeriesBuilder<?> series) {
    if (series != null) {
      seriesBuilders.add(series);
    }
  }

  /**
   * Gets an unmodifiable list of the series builders to be used for building the plot.
   * 
   * @return The unmodifiable list of series builders
   */
  public List<IXYTimeSeriesBuilder<?>> unmodifiableSeries() {
    return Collections.unmodifiableList(seriesBuilders);
  }

  /**
   * Registers an XYDataset builder to be used for building the plot.
   * 
   * @param dataset The builder to be registered
   */
  public void dataset(IXYDatasetBuilder<?> dataset) {
    if (dataset != null) {
      datasetBuilders.add(dataset);
    }
  }

  /**
   * Gets an unmodifiable list of the dataset builders to be used for building the plot.
   * 
   * @return The unmodifiable list of dataset builders
   */
  public List<IXYDatasetBuilder<?>> unmodifiableDatasets() {
    return Collections.unmodifiableList(datasetBuilders);
  }

  /**
   * Registers a fixed line builder to be used for building the plot.
   * 
   * @param line The builder to be registered
   */
  public void line(LineBuilder line) {
    if (line != null) {
      lineBuilders.add(line);
    }
  }

  /**
   * Gets an unmodifiable list of the line builders to be used for building the plot.
   * 
   * @return The unmodifiable list of line builders
   */
  public List<LineBuilder> unmodifiableLines() {
    return Collections.unmodifiableList(lineBuilders);
  }

  /**
   * Registers an IXYAnnotationBuilder to be used for building the plot.
   * 
   * @param annotation The builder to be registered
   */
  public void annotation(IXYAnnotationBuilder<?> annotation) {
    if (annotation != null) {
      annotationBuilders.add(annotation);
    }
  }

  /**
   * Gets an unmodifiable list of the annotation builders to be used for building the plot.
   * 
   * @return The unmodifiable list of annotation builders
   */
  public List<IXYAnnotationBuilder<?>> unmodifiableAnnotations() {
    return Collections.unmodifiableList(annotationBuilders);
  }

  /**
   * Sets the layout weight to be used when rendering the plot.
   * 
   * @param weight The plot's layout weight to be used
   */
  public void plotWeight(int weight) {
    this.plotWeight = weight;
  }

  /**
   * Gets the layout weight to be used when rendering the plot.
   * 
   * @return The plot's layout weight
   */
  public int plotWeight() {
    return plotWeight;
  }

  /**
   * Sets the y-axis name to be used when building the plot.
   * 
   * @param name The plot's y-axis name to be set
   */
  public void yAxisName(String name) {
    yAxisName = name == null ? "" : name;
  }

  /**
   * Gets the y-axis name to be used when building the plot.
   * 
   * @return The plot's y-axis name
   */
  public String yAxisName() {
    return yAxisName;
  }

  /**
   * Sets the main y-axis Range to be used when building the plot.
   * 
   * @param lower The plot's y-axis lower range bound to use
   * @param upper The plot's y-axis upper range bound to use
   */
  public void yAxisRange(double lower, double upper) {

    if (lower == Double.NaN || upper == Double.NaN) {
      throw new IllegalArgumentException("Y-axis range lower and upper cannot be NaN");
    }

    yAxisRange = new Range(lower, upper);
  }

  /**
   * Gets the main y-axis Range to be used when building the plot.
   * 
   * @return The plot's y-axis Range
   */
  public Range yAxisRange() {
    return yAxisRange;
  }

  /**
   * Sets the main y-axis tick size to be used when building the plot.
   * 
   * @param size The plot's y-axis tick size to use
   */
  public void yAxisTickSize(double size) {

    if (size < 0.0) {
      throw new IllegalArgumentException("Y-axis tick size must be greater than zero");
    }

    yAxisTickSize = size;
  }

  /**
   * Gets the main y-axis tick size to be used when building the plot.
   * 
   * @return The plot's y-axis tick size
   */
  public double yAxisTickSize() {
    return yAxisTickSize;
  }

  /**
   * Checks if the builder is configured to use the default y-axis tick size.
   * 
   * @return True if the default y-axis tick size will be used, false otherwise
   */
  public boolean usingDefaultYAxisTickSize() {
    return Double.isNaN(yAxisTickSize); // USE_DEFAULT_Y_AXIS_TICK_SIZE
  }

  /**
   * Sets the plot background color to use when building the plot.
   * 
   * @param color The color to set
   */
  public void backgroundColor(Paint color) {
    backgroundColor = color == null ? DEFAULT_BACKGROUND_COLOR : color;
  }

  /**
   * Gets the plot background color to use when building the plot.
   * 
   * @return The color
   */
  public Paint backgroundColor() {
    return backgroundColor;
  }

  /**
   * Sets the axis font color to use when building the plot.
   * 
   * @param color The color to set
   */
  public void axisFontColor(Paint color) {
    axisFontColor = color == null ? DEFAULT_AXIS_FONT_COLOR : color;
  }

  /**
   * Gets the axis font color to use when building the plot.
   * 
   * @return The color
   */
  public Paint axisFontColor() {
    return axisFontColor;
  }

  /**
   * Sets the axis color to use when building the plot.
   * 
   * @param color The color to set
   */
  public void axisColor(Paint color) {
    axisColor = color == null ? DEFAULT_AXIS_COLOR : color;
  }

  /**
   * Gets the axis color to use when building the plot.
   * 
   * @return The color
   */
  public Paint axisColor() {
    return axisColor;
  }

  /**
   * Sets displaying grid lines ON.
   */
  public void gridLines() {
    showGridLines = true;
  }

  /**
   * Sets displaying grid lines OFF.
   */
  public void noGridLines() {
    showGridLines = false;
  }

  /**
   * Returns whether or not to show grid lines.
   * 
   * @return True to show grid lines, false otherwise.
   */
  public boolean showGridLines() {
    return showGridLines;
  }

  /**
   * Helper method to check if the preconditions for invoking {@code build()} have been satisfied.
   * That is, that the x-axis and time data shared by all plots have been configured.
   * 
   * @throws IllegalStateException if any of the preconditions are not met
   */
  public void checkBuildPreconditions() throws IllegalStateException {

    if (xAxis == null) {
      throw new IllegalStateException("No x-axis configured");
    }

    if (timeData == null) {
      throw new IllegalStateException("No time data configured");
    }
  }
}
