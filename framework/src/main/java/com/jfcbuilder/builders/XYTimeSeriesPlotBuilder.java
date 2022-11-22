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
import java.awt.Stroke;
import java.text.NumberFormat;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.jfcbuilder.adapters.NumberMappedTimeSeriesCollection;
import com.jfcbuilder.types.Orientation;
import com.jfcbuilder.types.XYTimeSeriesPlotBuilderElements;
import com.jfcbuilder.types.ZeroBasedIndexRange;

/**
 * Builder for producing general XYPlot plots using configured builder properties, series, and
 * datasets.
 */
public class XYTimeSeriesPlotBuilder implements IXYTimeSeriesPlotBuilder<XYTimeSeriesPlotBuilder> {

  private XYTimeSeriesPlotBuilderElements elements;

  /**
   * Hidden constructor.
   */
  private XYTimeSeriesPlotBuilder() {
    elements = new XYTimeSeriesPlotBuilderElements();
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static XYTimeSeriesPlotBuilder get() {
    return new XYTimeSeriesPlotBuilder();
  }

  @Override
  public XYTimeSeriesPlotBuilder indexRange(ZeroBasedIndexRange indexRange) {
    elements.indexRange(indexRange);
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder xAxis(ValueAxis xAxis) {
    elements.xAxis(xAxis);
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder timeData(long[] timeData) {
    elements.timeData(timeData);
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder showTimeGaps(boolean showTimeGaps) {
    elements.showTimeGaps(showTimeGaps);
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder series(IXYTimeSeriesBuilder<?> series) {
    elements.series(series);
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder series(IXYTimeSeriesDatasetBuilder<?> dataset) {
    elements.dataset(dataset);
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder marker(MarkerBuilder line) {
    elements.marker(line);
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder annotation(IXYAnnotationBuilder<?> annotation) {
    elements.annotation(annotation);
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder plotWeight(int weight) {
    elements.plotWeight(weight);
    return this;
  }

  @Override
  public int plotWeight() {
    return elements.plotWeight();
  }

  @Override
  public XYTimeSeriesPlotBuilder yAxisName(String name) {
    elements.yAxisName(name);
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder yAxisRange(double lower, double upper)
      throws IllegalArgumentException {
    elements.yAxisRange(lower, upper);
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder yAxisTickSize(double size) {
    elements.yAxisTickSize(size);
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder yTickFormat(NumberFormat format) {
    elements.yTickFormat(format);
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder backgroundColor(Paint color) {
    elements.backgroundColor(color);
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder axisFontColor(Paint color) {
    elements.axisFontColor(color);
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder axisColor(Paint color) {
    elements.axisColor(color);
    return this;
  }

  /**
   * Sets displaying grid lines ON.
   * 
   * @deprecated This facility is replaced by {@link XYTimeSeriesPlotBuilder#majorGrid(boolean)} and
   *             {@link XYTimeSeriesPlotBuilder#minorGrid(boolean)}, and will be removed in a future release.
   */
  @Deprecated(since = "1.5.7", forRemoval = true)
  @Override
  public XYTimeSeriesPlotBuilder gridLines() {
    elements.majorGrid(true); // Legacy behavior
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder noGridLines() {
    majorGrid(false);
    minorGrid(false);
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder majorGrid(boolean enabled) {
    elements.majorGrid(enabled);
    return this;
  }
  
  @Override
  public XYTimeSeriesPlotBuilder majorGridColor(Paint color) {
    elements.majorGridColor(color);
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder majorGridStyle(Stroke style) {
    elements.majorGridStyle(style);
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder minorGrid(boolean enabled) {
    elements.minorGrid(enabled);
    return this;
  }
  
  @Override
  public XYTimeSeriesPlotBuilder minorGridColor(Paint color) {
    elements.minorGridColor(color);
    return this;
  }

  @Override
  public XYTimeSeriesPlotBuilder minorGridStyle(Stroke style) {
    elements.minorGridStyle(style);
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

    TimeSeriesCollection collection = elements.showTimeGaps() ? new TimeSeriesCollection()
        : new NumberMappedTimeSeriesCollection();

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

    final NumberAxis yAxis = BuilderUtils.createYAxis(elements);

    yAxis.setLabel(elements.yAxisName() + axisSubName.toString());
    
    final XYPlot plot = BuilderUtils.createPlot(xAxis, yAxis, collection, renderer, elements);

    for (MarkerBuilder builder : elements.unmodifiableLines()) {
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

      if (!elements.showTimeGaps()) {
        // We need to map annotation time series x-axis values to the number axis.
        builder.mapXToTimeIndex(timeData, elements.indexRange().getStartIndex(),
            elements.indexRange().getEndIndex());
      }

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

    return plot;
  }
}
