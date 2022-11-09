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

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;

/**
 * Specialized renderer for rendering XY bars when they have x-axis values mapped to a numeric
 * scale.
 */
class NumberMappedXYBarRenderer extends XYBarRenderer {

  /**
   * Generated value
   */
  private static final long serialVersionUID = 7850514485018892002L;

  /**
   * Pre-calculated and cached maximum expected y-value that can be displayed.
   */
  private double yMax;

  /**
   * Hidden no-args constructor.
   */
  @SuppressWarnings("unused")
  private NumberMappedXYBarRenderer() {
    // Explicitly do nothing
  }

  /**
   * Constructor
   * 
   * @param yMax The maximum y-value that is expected.
   */
  public NumberMappedXYBarRenderer(double yMax) {
    setYMax(yMax);
  }

  /**
   * Sets the maximum y-value that is expected.
   * 
   * @param yMax The maximum expected y-value.
   */
  public void setYMax(double yMax) {

    if (Double.isNaN(yMax)) {
      throw new IllegalArgumentException("Max y-value cannot be NaN");
    }

    this.yMax = yMax;
  }

  /**
   * Custom drawItem method to calculate a proper bar width when mapping timeseries values because
   * NumberMappedtimeSeriesCollection assumes the same start/end X value which creates a width of
   * zero. Assumes the time axis being mapped to numeric indexes is horizontally oriented.
   */
  @Override
  public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea,
      PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis,
      XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {

    if (!getItemVisible(series, item)) {
      return;
    }

    if (!(dataset instanceof IntervalXYDataset)) {
      return;
    }

    IntervalXYDataset intervalDataset = (IntervalXYDataset) dataset;

    double x = intervalDataset.getXValue(series, item);
    double y = intervalDataset.getYValue(series, item); // volume value

    RectangleEdge timeAxisEdge = plot.getDomainAxisEdge();
    double x2d = domainAxis.valueToJava2D(x, dataArea, timeAxisEdge);

    // Center the bar on it x-axis value and make its height relative to the data area it's drawn
    // on as a percentage of the full area height.

    double yAsPctFromMax = yMax == 0.0 ? 100.0 : y / yMax;

    final double areaMinY = dataArea.getMinY();
    final double areaMaxY = dataArea.getMaxY();
    final double areaHeight = areaMaxY - areaMinY;

    double barHeight = areaHeight * yAsPctFromMax;

    g2.setStroke(getItemStroke(series, item));
    g2.setPaint(getSeriesPaint(series));

    final int numBars = intervalDataset.getItemCount(series);

    final double barWidth = getBarWidth(dataArea, numBars);

    double barXCoord = x2d - (barWidth / 2.0);
    double barYCoord = areaMaxY - barHeight;

    Rectangle2D bar = new Rectangle2D.Double(barXCoord, barYCoord, barWidth, barHeight);

    g2.fill(bar);

    Stroke stroke = getItemOutlineStroke(series, item);
    Paint outlinePaint = getItemOutlinePaint(series, item);
    g2.setStroke(stroke);
    g2.setPaint(outlinePaint);
    g2.draw(bar);
  }

  private static double getBarWidth(Rectangle2D dataArea, int numBars) {

    return BuilderConstants.DEFAULT_BAR_WIDTH_PERCENT * (dataArea.getMaxX() - dataArea.getMinX())
        / (double) numBars;
  }

}
