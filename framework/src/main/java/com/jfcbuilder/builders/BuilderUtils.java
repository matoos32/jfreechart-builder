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

import java.text.NumberFormat;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;

import com.jfcbuilder.types.BuilderConstants;
import com.jfcbuilder.types.XYTimeSeriesPlotBuilderElements;

/**
 * Utility methods for common logic within the framework.
 */
public abstract class BuilderUtils {

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
   * Helper method to create and initialize an XYPlot.
   * 
   * @param xAxis The x-axis to be used with the plot
   * @param yAxis The y-axis to be used with the plot
   * @param dataset An XYDataset to be plotted
   * @param renderer The renderer used to plot the dataset
   * @param elements Various settings used to initialize the plot object
   * @return The new XYPlot instance
   */
  public static XYPlot createPlot(final ValueAxis xAxis, NumberAxis yAxis, XYDataset dataset,
      XYItemRenderer renderer, XYTimeSeriesPlotBuilderElements elements) {

    final XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);

    plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD); // Main series behind the others

    plot.setBackgroundPaint(elements.backgroundColor());

    plot.setDomainGridlinesVisible(elements.majorGrid());
    plot.setDomainGridlinePaint(elements.majorGridColor());
    plot.setDomainGridlineStroke(elements.majorGridStyle());

    plot.setRangeGridlinesVisible(elements.majorGrid());
    plot.setRangeGridlinePaint(elements.majorGridColor());
    plot.setRangeGridlineStroke(elements.majorGridStyle());

    plot.setDomainMinorGridlinesVisible(elements.minorGrid());
    plot.setDomainMinorGridlinePaint(elements.minorGridColor());
    plot.setDomainMinorGridlineStroke(elements.minorGridStyle());

    plot.setRangeMinorGridlinesVisible(elements.minorGrid());
    plot.setRangeMinorGridlinePaint(elements.minorGridColor());
    plot.setRangeMinorGridlineStroke(elements.minorGridStyle());

    return plot;
  }

}
