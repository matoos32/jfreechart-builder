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

package com.jfcbuilder.types;

import java.awt.Paint;
import java.awt.Stroke;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;

import com.jfcbuilder.builders.BuilderUtils;
import com.jfcbuilder.builders.IXYAnnotationBuilder;
import com.jfcbuilder.builders.IXYTimeSeriesBuilder;
import com.jfcbuilder.builders.IXYTimeSeriesDatasetBuilder;
import com.jfcbuilder.builders.MarkerBuilder;

/**
 * Helper class for storing and accessing properties common to different kinds of XY plot builders.
 * Intended for use in composition-type implementations.
 */
public class XYTimeSeriesPlotBuilderElements {

  /**
   * Value used to indicate the default y-axis tick size should be used.
   * 
   * @see XYTimeSeriesPlotBuilderElements#usingDefaultYAxisTickSize()
   */
  private static final double USE_DEFAULT_Y_AXIS_TICK_SIZE = Double.NaN;

  private static final int DEFAULT_PLOT_WEIGHT = 1;

  private static final Paint DEFAULT_AXIS_FONT_COLOR = Axis.DEFAULT_TICK_LABEL_PAINT;
  private static final Paint DEFAULT_AXIS_COLOR = Axis.DEFAULT_AXIS_LINE_PAINT;
  private static final Paint DEFAULT_BACKGROUND_COLOR = XYPlot.DEFAULT_BACKGROUND_PAINT;
  private static final Stroke DEFAULT_GRIDLINE_STYLE = XYPlot.DEFAULT_GRIDLINE_STROKE;
  private static final Paint DEFAULT_GRIDLINE_COLOR = XYPlot.DEFAULT_GRIDLINE_PAINT;

  private List<IXYTimeSeriesBuilder<?>> seriesBuilders;
  private List<IXYTimeSeriesDatasetBuilder<?>> datasetBuilders;
  private List<MarkerBuilder> markerBuilders;
  private List<IXYAnnotationBuilder<?>> annotationBuilders;
  private ValueAxis xAxis;
  private long[] timeData;
  private boolean showTimeGaps;
  private ZeroBasedIndexRange indexRange;
  private int plotWeight;
  private String yAxisName;
  private Range yAxisRange;
  private double yAxisTickSize;
  private NumberFormat yAxisTickFormat;
  private Paint backgroundColor;
  private Paint axisFontColor;
  private Paint axisColor;
  private boolean showMajorGridlines;
  private Paint majorGridColor;
  private Stroke majorGridStyle;
  private boolean showMinorGridlines;
  private Paint minorGridColor;
  private Stroke minorGridStyle;

  /**
   * Constructor.
   */
  public XYTimeSeriesPlotBuilderElements() {
    seriesBuilders = new ArrayList<>();
    datasetBuilders = new ArrayList<>();
    markerBuilders = new ArrayList<>();
    annotationBuilders = new ArrayList<>();
    xAxis = null;
    timeData = null;
    showTimeGaps = BuilderConstants.DEFAULT_SHOW_TIME_GAPS;
    indexRange = null;
    plotWeight = DEFAULT_PLOT_WEIGHT;
    yAxisName = "";
    yAxisRange = null;
    yAxisTickSize = USE_DEFAULT_Y_AXIS_TICK_SIZE;
    yAxisTickFormat = BuilderUtils.getDefaultNumberFormat();
    backgroundColor = DEFAULT_BACKGROUND_COLOR;
    axisFontColor = DEFAULT_AXIS_FONT_COLOR;
    axisColor = DEFAULT_AXIS_COLOR;
    showMajorGridlines = true;
    majorGridColor = DEFAULT_GRIDLINE_COLOR;
    majorGridStyle = DEFAULT_GRIDLINE_STYLE;
    showMinorGridlines = false;
    minorGridColor = DEFAULT_GRIDLINE_COLOR;
    minorGridStyle = DEFAULT_GRIDLINE_STYLE;
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
   * Sets whether or not time gaps should be rendered.
   * 
   * @param showTimeGaps True to render time gaps, false otherwise
   */
  public void showTimeGaps(boolean showTimeGaps) {
    this.showTimeGaps = showTimeGaps;
  }

  /**
   * Gets whether to render time gaps.
   * 
   * @return True if time gaps should be rendered, false otherwise
   */
  public boolean showTimeGaps() {
    return showTimeGaps;
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
  public void dataset(IXYTimeSeriesDatasetBuilder<?> dataset) {
    if (dataset != null) {
      datasetBuilders.add(dataset);
    }
  }

  /**
   * Gets an unmodifiable list of the dataset builders to be used for building the plot.
   * 
   * @return The unmodifiable list of dataset builders
   */
  public List<IXYTimeSeriesDatasetBuilder<?>> unmodifiableDatasets() {
    return Collections.unmodifiableList(datasetBuilders);
  }

  /**
   * Registers a fixed marker builder to be used for building the plot.
   * 
   * @param marker The builder to be registered
   */
  public void marker(MarkerBuilder marker) {
    if (marker != null) {
      markerBuilders.add(marker);
    }
  }

  /**
   * Gets an unmodifiable list of the marker builders to be used for building the plot.
   * 
   * @return The unmodifiable list of marker builders
   */
  public List<MarkerBuilder> unmodifiableLines() {
    return Collections.unmodifiableList(markerBuilders);
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
   * Sets the main y-axis tick format to be used when building the plot.
   * 
   * @param format The plot's y-axis tick format to use
   */
  public void yTickFormat(NumberFormat format) {

    yAxisTickFormat = format == null ? BuilderUtils.getDefaultNumberFormat() : format;
  }

  /**
   * Gets the main y-axis tick number format to be used when building the plot.
   * 
   * @return The plot's y-axis tick format to use
   */
  public NumberFormat yAxisTickFormat() {
    return yAxisTickFormat;
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
   * Toggle displaying major grid lines ON or OFF.
   * 
   * @param enabled True to set displaying ON, false for OFF
   */
  public void majorGrid(boolean enabled) {
    showMajorGridlines = enabled;
  }

  /**
   * Returns true if displaying major grid lines is set ON or OFF.
   * 
   * @return True if ON, false if OFF.
   */
  public boolean majorGrid() {
    return showMajorGridlines;
  }

  /**
   * Sets the major grid color to use when building the plot.
   * 
   * @param color The color to set
   */
  public void majorGridColor(Paint color) {
    majorGridColor = color == null ? DEFAULT_GRIDLINE_COLOR : color;
  }

  /**
   * Gets the configured major grid color.
   * 
   * @return The color
   */
  public Paint majorGridColor() {
    return majorGridColor;
  }

  /**
   * Sets the major grid line style to use when building the plot.
   * 
   * @param style The style to set
   */
  public void majorGridStyle(Stroke style) {
    majorGridStyle = style == null ? DEFAULT_GRIDLINE_STYLE : style;
  }

  /**
   * Gets the configured major grid style.
   * 
   * @return The style
   */
  public Stroke majorGridStyle() {
    return majorGridStyle;
  }

  /**
   * Toggle displaying minor grid lines ON or OFF.
   * 
   * @param enabled True to set displaying ON, false for OFF
   */
  public void minorGrid(boolean enabled) {
    showMinorGridlines = enabled;
  }

  /**
   * Returns true if displaying minor grid lines is set ON or OFF.
   * 
   * @return True if ON, false if OFF.
   */
  public boolean minorGrid() {
    return showMinorGridlines;
  }

  /**
   * Sets the minor grid color to use when building the plot.
   * 
   * @param color The color to set
   */
  public void minorGridColor(Paint color) {
    minorGridColor = color == null ? DEFAULT_GRIDLINE_COLOR : color;
  }

  /**
   * Gets the configured minor grid color.
   * 
   * @return The color
   */
  public Paint minorGridColor() {
    return minorGridColor;
  }

  /**
   * Sets the minor grid line style to use when building the plot.
   * 
   * @param style The style to set
   */
  public void minorGridStyle(Stroke style) {
    minorGridStyle = style == null ? DEFAULT_GRIDLINE_STYLE : style;
  }

  /**
   * Gets the configured minor grid style.
   * 
   * @return The style
   */
  public Stroke minorGridStyle() {
    return minorGridStyle;
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
