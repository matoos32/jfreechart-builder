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

import java.text.NumberFormat;
import java.util.Arrays;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;

/**
 * Utility methods that can be used throughout the application.
 */
abstract class BuilderUtils {

  /**
   * Factory method for getting new instances of the default axis number format.
   * 
   * @return New instance of the default NumberFormat that can be used throughout the application
   */
  public static NumberFormat getDefaultNumberFormat() {
    return NumberFormat.getNumberInstance();
  }

  /**
   * Helper method for creating and initializing a y-axis object.
   * 
   * @param elements Various settings used to initialize the y-axis object
   * @return The new y-axis instance
   */
  public static NumberAxis createYAxis(XYTimeSeriesPlotBuilderElements elements) {
    NumberAxis yAxis = new NumberAxis();
    yAxis.setMinorTickMarksVisible(true);
    yAxis.setMinorTickCount(2);
    yAxis.setMinorTickMarkOutsideLength(2);
    yAxis.setTickLabelFont(BuilderConstants.DEFAULT_FONT);
    yAxis.setAutoRangeIncludesZero(false);
    yAxis.setAutoRangeStickyZero(false);
    yAxis.setAxisLinePaint(elements.axisColor());
    yAxis.setTickLabelPaint(elements.axisFontColor());
    yAxis.setNumberFormatOverride(elements.yAxisTickFormat());
    if (!elements.usingDefaultYAxisTickSize()) {
      yAxis.setTickUnit(new NumberTickUnit(elements.yAxisTickSize()));
    }
    return yAxis;
  }

  /**
   * Helper method to create and initialize an XYPlot
   * 
   * @param xAxis The x-axis to be used with the plot
   * @param yAxis The y-axis to be used with the plot
   * @param dataset An XYDataset to be plotted
   * @param renderer The renderer used to plot teh dataset
   * @param elements Various settings used to initialize the plot object
   * @return The new XYPlot instance
   */
  public static XYPlot createPlot(final ValueAxis xAxis, NumberAxis yAxis, XYDataset dataset,
      XYItemRenderer renderer, XYTimeSeriesPlotBuilderElements elements) {

    final XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
    plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD); // Main series behind the others
    plot.setBackgroundPaint(elements.backgroundColor());
    plot.setDomainGridlinesVisible(elements.showGridLines());
    plot.setRangeGridlinesVisible(elements.showGridLines());
    plot.setDomainGridlinePaint(BuilderConstants.DEFAULT_GRIDLINE_PAINT);
    plot.setRangeGridlinePaint(BuilderConstants.DEFAULT_GRIDLINE_PAINT);
    return plot;
  }

  /**
   * Helper method for replacing an annotation builder's configured x-axis date value with a
   * corresponding source array element index value. Uses {@code Arrays.binarySearch()} to search
   * the source array for the the date value. If found, replaces that value in the builder with the
   * found array index relative to the configured index range. The source time values are assumed to
   * be timestamps in milliseconds since the epoch start. It's also assumed these are in ascending
   * chronologic order. Failure to provide them in sorted order will result in undefined behavior as
   * per {@code Arrays.binarySearch()}.
   * 
   * @param timeData The array of source time values
   * @param builder The builder whose x-value is to be mapped to the source time index
   * @param indexRangeStartIndex The start index of the series index range
   * @param indexRangeEndIndex The end index of the series index range
   */
  public static void mapAnnotationXToTimeIndex(long[] timeData, IXYAnnotationBuilder<?> builder,
      int indexRangeStartIndex, int indexRangeEndIndex) {
    
    // +1 as the binary search method end index is exclusive.
    int xIndex = Arrays.binarySearch(timeData, indexRangeStartIndex, indexRangeEndIndex + 1,
        (long) builder.x());

    if (xIndex >= 0) {
      builder.x((double) (xIndex - indexRangeStartIndex));
    }
  }

}
