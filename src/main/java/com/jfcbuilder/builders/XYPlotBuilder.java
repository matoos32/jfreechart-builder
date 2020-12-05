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

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.jfcbuilder.types.BuilderConstants;
import com.jfcbuilder.types.Orientation;
import com.jfcbuilder.types.ZeroBasedIndexRange;

/**
 * Builder for producing general XYPlot plots using configured builder properties, series, and
 * datasets.
 */
public class XYPlotBuilder implements IXYPlotBuilder<XYPlotBuilder> {

  private XYTimeSeriesPlotBuilderElements elements;

  /**
   * Hidden constructor.
   */
  private XYPlotBuilder() {
    elements = new XYTimeSeriesPlotBuilderElements();
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static XYPlotBuilder get() {
    return new XYPlotBuilder();
  }

  @Override
  public XYPlotBuilder indexRange(ZeroBasedIndexRange indexRange) {
    elements.indexRange(indexRange);
    return this;
  }

  @Override
  public XYPlotBuilder xAxis(ValueAxis xAxis) {
    elements.xAxis(xAxis);
    return this;
  }

  @Override
  public XYPlotBuilder timeData(long[] timeData) {
    elements.timeData(timeData);
    return this;
  }

  @Override
  public XYPlotBuilder series(IXYTimeSeriesBuilder<?> series) {
    elements.series(series);
    return this;
  }

  @Override
  public XYPlotBuilder series(IXYDatasetBuilder<?> dataset) {
    elements.dataset(dataset);
    return this;
  }

  @Override
  public XYPlotBuilder line(LineBuilder line) {
    elements.line(line);
    return this;
  }

  @Override
  public XYPlotBuilder annotation(IXYAnnotationBuilder<?> annotation) {
    elements.annotation(annotation);
    return this;
  }

  @Override
  public XYPlotBuilder plotWeight(int weight) {
    elements.plotWeight(weight);
    return this;
  }

  @Override
  public int plotWeight() {
    return elements.plotWeight();
  }

  @Override
  public XYPlotBuilder yAxisName(String name) {
    elements.yAxisName(name);
    return this;
  }

  @Override
  public XYPlotBuilder yAxisRange(double lower, double upper) throws IllegalArgumentException {
    elements.yAxisRange(lower, upper);
    return this;
  }

  @Override
  public XYPlotBuilder yAxisTickSize(double size) {
    elements.yAxisTickSize(size);
    return this;
  }

  @Override
  public XYPlotBuilder backgroundColor(Paint color) {
    elements.backgroundColor(color);
    return this;
  }

  @Override
  public XYPlotBuilder axisFontColor(Paint color) {
    elements.axisFontColor(color);
    return this;
  }

  @Override
  public XYPlotBuilder axisColor(Paint color) {
    elements.axisColor(color);
    return this;
  }

  @Override
  public XYPlotBuilder gridLines() {
    elements.gridLines();
    return this;
  }

  @Override
  public XYPlotBuilder noGridLines() {
    elements.noGridLines();
    return this;
  }

  @Override
  public XYPlot build() throws IllegalStateException {

    elements.checkBuildPreconditions();

    final ValueAxis xAxis = elements.xAxis();
    xAxis.setAxisLinePaint(elements.axisColor());
    xAxis.setTickLabelPaint(elements.axisFontColor());

    final long[] timeData = elements.timeData();

    StringBuilder axisSubName = new StringBuilder();

    TimeSeriesCollection collection = new TimeSeriesCollection();
    StandardXYItemRenderer renderer = new StandardXYItemRenderer();

    double yMax = Double.MIN_VALUE;
    double yMin = Double.MAX_VALUE;

    final ZeroBasedIndexRange indexRange = elements.indexRange();

    for (IXYTimeSeriesBuilder<?> builder : elements.unmodifiableSeries()) {

      builder.indexRange(indexRange);
      builder.timeData(timeData);

      TimeSeries series = builder.build();

      if (!series.isEmpty()) {

        yMax = Math.max(yMax, series.getMaxY());
        yMin = Math.min(yMin, series.getMinY());

        renderer.setSeriesPaint(collection.getSeriesCount(), builder.color());
        renderer.setSeriesStroke(collection.getSeriesCount(), builder.style());
        collection.addSeries(series);
        // JFreeChart series key is the name given to the series.
        if (series.getKey() != null && !series.getKey().toString().isEmpty()) {
          axisSubName.append(' ').append(series.getKey().toString());
        }
      }
    }

    // TODO: Extract this code common with other classes to a factory method
    final NumberAxis yAxis = new NumberAxis(elements.yAxisName() + axisSubName.toString());
    yAxis.setMinorTickMarksVisible(true);
    yAxis.setMinorTickCount(2);
    yAxis.setMinorTickMarkOutsideLength(2);
    yAxis.setTickLabelFont(BuilderConstants.DEFAULT_FONT);
    yAxis.setAutoRangeIncludesZero(false);
    yAxis.setAutoRangeStickyZero(false);
    yAxis.setAxisLinePaint(elements.axisColor());
    yAxis.setTickLabelPaint(elements.axisFontColor());

    final XYPlot plot = new XYPlot(collection, xAxis, yAxis, renderer);
    plot.setBackgroundPaint(elements.backgroundColor());
    plot.setDomainGridlinesVisible(elements.showGridLines());
    plot.setRangeGridlinesVisible(elements.showGridLines());
    plot.setDomainGridlinePaint(BuilderConstants.DEFAULT_GRIDLINE_PAINT);
    plot.setRangeGridlinePaint(BuilderConstants.DEFAULT_GRIDLINE_PAINT);

    for (LineBuilder builder : elements.unmodifiableLines()) {
      ValueMarker line = builder.build();

      yMax = Math.max(yMax, line.getValue());
      yMin = Math.min(yMin, line.getValue());

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

    if (elements.yAxisRange() != null) {
      yAxis.setRange(elements.yAxisRange());
    } else {
      // Pad the y-axis so the min and max don't go right against the limit. Setting lower/upper
      // margin doesn't do it.
      yAxis.setRange(yMin * ((yMin > 0.0) ? 0.95 : 1.05), yMax * ((yMax < 0.0) ? 0.99 : 1.01));
    }

    if (!elements.usingDefaultYAxisTickSize()) {
      yAxis.setTickUnit(new NumberTickUnit(elements.yAxisTickSize()));
    }

    return plot;
  }

}
