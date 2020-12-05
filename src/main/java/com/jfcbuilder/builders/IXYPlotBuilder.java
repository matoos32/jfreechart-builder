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
import java.text.NumberFormat;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;

import com.jfcbuilder.types.ZeroBasedIndexRange;

/**
 * Interface for all XYPlot builders.  Uses a generic to specify the concrete implementation of
 * the interface as the return type of setter methods. This is done to support method chaining on
 * the same builder instance. In this framework there can be different builder types that have
 * specialized methods. If the return types were made to be this interface instead of the concrete
 * class then those specialized methods of the classes not defined in the interface would be hidden
 * by only having access to the interface.
 * 
 * @param <T> The method chaining return type, which must be the type of the builder implementing
 *        this interface.
 */
public interface IXYPlotBuilder<T extends IXYPlotBuilder<T>> {

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
   * Sets the date-time values to be used when building the plot.
   * 
   * @param timeData The date-time values representing the milliseconds since the epoch start
   * @return Reference to this builder instance for method chaining
   */
  T timeData(long[] timeData);

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
  T series(IXYDatasetBuilder<?> series);

  /**
   * Registers a LineBuilder whose {@code build()} method will be called to generate its plot line
   * when this plot builder's{@code build()} method is called.
   * 
   * @param line The line builder representing the line that it will build
   * @return Reference to this builder instance for method chaining
   */
  T line(LineBuilder line);

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
   * @return Reference to this builder instance for method chaining
   */
  T gridLines();

  /**
   * Sets displaying grid lines OFF.
   * 
   * @return Reference to this builder instance for method chaining
   */
  T noGridLines();

  /**
   * Builds the XYPlot from all configured data and properties.
   * 
   * @return New instance of an XYPlot corresponding to all configured data and properties
   * @throws IllegalStateException If a XYTimeSeriesBuilder or LineBuilder are not configured,
   *         possibly if at least one XYTimeSeriesBuilder or LineBuilder are not of a specifically
   *         needed sub-type based on the implementation, if a time axis was not set, or if time
   *         data was not set.
   */
  XYPlot build() throws IllegalStateException;

}
