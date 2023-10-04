/*
 * jfreechart-builder: a builder pattern module for working with the jfreechart library
 * 
 * (C) Copyright 2023, by Matt E. and project contributors
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
import java.awt.Stroke;
import java.text.NumberFormat;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;

import com.jfcbuilder.types.ZeroBasedIndexRange;

/**
 * Interface for all time series based XYPlot builders. Uses a generic to specify the concrete
 * implementation of the interface as the return type of setter methods. This is done to support
 * method chaining on the same builder instance. In this framework there can be different builder
 * types that have specialized methods. If the return types were made to be this interface instead
 * of the concrete class then those specialized methods of the classes not defined in the interface
 * would be hidden by only having access to the interface.
 * 
 * @param <T> The method chaining return type, which must be the type of the builder implementing
 *            this interface.
 */
public interface IXYTimeSeriesPlotBuilder<T extends IXYTimeSeriesPlotBuilder<T>> {

  /**
   * Sets the date-time values to be used when building the plot.
   * 
   * @param timeData The date-time values representing the milliseconds since the epoch start
   * @return Reference to this builder instance for method chaining
   */
  T timeData(long[] timeData);

  /**
   * Toggle whether to show time gaps at x-values where there is no corresponding time instance.
   * 
   * @param showTimeGaps True to show time gaps, false otherwise.
   * @return Reference to this builder instance for method chaining
   */
  T showTimeGaps(boolean showTimeGaps);

  /**
   * Sets the zero-based index range used to index into all source data for building the XYPlot.
   * This is an optimization to allow clients to supply existing data without having to copy and/or
   * crop it to match the desired size axis range in the chart.
   * 
   * @param indexRange The index range to be set
   * @return Reference to this builder instance for method chaining
   */
  T indexRange(ZeroBasedIndexRange indexRange);

  /**
   * Sets the x-axis used when building the plot.
   * 
   * @param xAxis The axis to be set
   * @return Reference to this builder instance for method chaining
   */
  T xAxis(ValueAxis xAxis);

  /**
   * Registers an IXYTimeSeriesBuilder whose {@code build()} method will be called to generate its
   * XYTimeSeries when this plot builder's {@code build()} method is called.
   * 
   * @param series The series builder representing the series that it will build
   * @return Reference to this builder instance for method chaining
   */
  T series(IXYTimeSeriesBuilder<?> series);

  /**
   * Registers an IXYDatasetBuilder whose {@code build()} method will be called to generate its
   * XYDataset when this plot builder's {@code build()} method is called.
   * 
   * @param series The series builder representing the series that it will build
   * @return Reference to this builder instance for method chaining
   */
  T series(IXYTimeSeriesDatasetBuilder<?> series);

  /**
   * Registers a {@link MarkerBuilder} whose {@code build()} method will be called to generate its
   * plot line when this plot builder's{@code build()} method is called.
   * 
   * @param marker The marker builder representing the marker that it will build
   * @return Reference to this builder instance for method chaining
   */
  T marker(MarkerBuilder marker);

  /**
   * Registers an IXYAnnotationBuilder whose {@code build()} method will be called to generate its
   * XYAnnotation when this plot builder's {@code build()} method is called.
   * 
   * @param annotation The annotation builder representing the annotation that it will build
   * @return Reference to this builder instance for method chaining
   */
  T annotation(IXYAnnotationBuilder<?> annotation);

  /**
   * Sets the plot weight to be attributed to the plot. A default of 1 will be used if this is not
   * set.
   * 
   * @param weight The plot weight to be set
   * @return Reference to this builder instance for method chaining
   */
  T plotWeight(int weight);

  /**
   * Gets the currently configured plot weight.
   * 
   * @return The plot weight
   */
  int plotWeight();

  /**
   * Sets the plot's y-axis name.
   * 
   * @param name The axis name to be set
   * @return Reference to this builder instance for method chaining
   */
  T yAxisName(String name);

  /**
   * Sets an explicit y-axis value range to use instead of having the y-axis auto-calculate it.
   * 
   * @param lower The lower bound to be set
   * @param upper The upper bound to be set
   * @return Reference to this builder instance for method chaining
   * @throws IllegalArgumentException If the lower bound is greater than the upper one
   */
  T yAxisRange(double lower, double upper) throws IllegalArgumentException;

  /**
   * Sets the plot's y-axis tick size.
   * 
   * @param size The tick size to be set
   * @return Reference to this builder instance for method chaining
   */
  T yAxisTickSize(double size);

  /**
   * Sets the plot's y-axis tick format.
   * 
   * @param format The tick format to be set
   * @return Reference to this builder instance for method chaining
   */
  T yTickFormat(NumberFormat format);

  /**
   * Sets the plot background color to use when building the plot.
   * 
   * @param color The color to set
   * @return Reference to this builder instance for method chaining
   */
  T backgroundColor(Paint color);

  /**
   * Sets the axis font color to use when building the plot.
   * 
   * @param color The color to set
   * @return Reference to this builder instance for method chaining
   */
  T axisFontColor(Paint color);

  /**
   * Sets the axis color to use when building the plot.
   * 
   * @param color The color to set
   * @return Reference to this builder instance for method chaining
   */
  T axisColor(Paint color);

  /**
   * Sets displaying grid lines ON.
   * 
   * @deprecated This facility is replaced by {@link IXYTimeSeriesPlotBuilder#majorGrid(boolean)}
   *             and {@link IXYTimeSeriesPlotBuilder#minorGrid(boolean)}, and will be removed in a
   *             future release.
   *             <p>
   *             <b>For removal since v1.5.7</b>
   * 
   * @return Reference to this builder instance for method chaining
   */
  @Deprecated
  T gridLines();

  /**
   * Sets displaying all grid lines OFF. Shorthand for calling both
   * {@link IXYTimeSeriesPlotBuilder#majorGrid(boolean)} and
   * {@link IXYTimeSeriesPlotBuilder#minorGrid(boolean)} with a value of {@code false}.
   * 
   * @return Reference to this builder instance for method chaining
   */
  T noGridLines();

  /**
   * Toggles displaying major grid lines ON or OFF.
   * 
   * @param enabled True to show major grid lines, false to turn them off.
   * 
   * @return Reference to this builder instance for method chaining
   */
  T majorGrid(boolean enabled);

  /**
   * Sets the major grid color to use when building the plot.
   * 
   * @param color The color to set
   * @return Reference to this builder instance for method chaining
   */
  T majorGridColor(Paint color);

  /**
   * Sets the major grid line style to use when building the plot.
   * 
   * @param style The style to set
   * @return Reference to this builder instance for method chaining
   */
  T majorGridStyle(Stroke style);

  /**
   * Toggles displaying minor grid lines ON or OFF.
   * 
   * @param enabled True to show minor grid lines, false to turn them off.
   * 
   * @return Reference to this builder instance for method chaining
   */
  T minorGrid(boolean enabled);

  /**
   * Sets the minor grid color to use when building the plot.
   * 
   * @param color The color to set
   * @return Reference to this builder instance for method chaining
   */
  T minorGridColor(Paint color);

  /**
   * Sets the minor grid line style to use when building the plot.
   * 
   * @param style The style to set
   * @return Reference to this builder instance for method chaining
   */
  T minorGridStyle(Stroke style);

  /**
   * Builds the XYPlot from all configured data and properties.
   * 
   * @return New instance of an XYPlot corresponding to all configured data and properties
   * @throws IllegalStateException If the minimum needed configuration for building an XYPlot is not
   *                               setup in the builder.
   */
  XYPlot build() throws IllegalStateException;

}
