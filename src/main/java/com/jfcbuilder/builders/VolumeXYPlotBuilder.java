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

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.jfcbuilder.builders.renderers.XYBarRendererBuilder;
import com.jfcbuilder.builders.types.BuilderConstants;
import com.jfcbuilder.builders.types.Orientation;
import com.jfcbuilder.builders.types.ZeroBasedIndexRange;

/**
 * Builder for producing stock market volume plots using configured builder properties, series, and
 * datasets. Uses a bar renderer for volume bars and allows superimposing other line series on top
 * like a moving average line. Also supports the ability to produce different series for rendering
 * "close up" and "close down" colors or rendering with a single color without consideration of
 * close up/down days.
 */
public class VolumeXYPlotBuilder implements IXYPlotBuilder<VolumeXYPlotBuilder> {

  private static final String DEFAULT_Y_AXIS_NAME = "Vol";
  private static final Color DEFAULT_BAR_OUTLINE_COLOR = BuilderConstants.DEFAULT_OUTLINE_COLOR;

  private XYTimeSeriesPlotBuilderElements elements;

  private VolumeXYTimeSeriesBuilder uniformVolSeriesBuilder;
  private VolumeXYTimeSeriesBuilder closeUpVolSeriesBuilder;
  private VolumeXYTimeSeriesBuilder closeDownVolSeriesBuilder;

  /**
   * Hidden constructor.
   */
  private VolumeXYPlotBuilder() {
    elements = new XYTimeSeriesPlotBuilderElements();
    uniformVolSeriesBuilder = null;
    closeUpVolSeriesBuilder = null;
    closeDownVolSeriesBuilder = null;
    yAxisName(DEFAULT_Y_AXIS_NAME);
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static VolumeXYPlotBuilder get() {
    return new VolumeXYPlotBuilder();
  }

  @Override
  public VolumeXYPlotBuilder indexRange(ZeroBasedIndexRange indexRange) {
    elements.indexRange(indexRange);
    return this;
  }

  @Override
  public VolumeXYPlotBuilder xAxis(ValueAxis xAxis) {
    elements.xAxis(xAxis);
    return this;
  }

  @Override
  public VolumeXYPlotBuilder timeData(long[] timeData) {
    elements.timeData(timeData);
    return this;
  }

  /**
   * Same as interface {@code IXYPlotBuilder<?>.series(IXYTimeSeriesBuilder<?>)} but also checks
   * that only one {@code VolumeXYTimeSeriesBuilder} for a "uniform" series is added (no up or down
   * OHLC day colored rendering) or only one {@code VolumeXYTimeSeriesBuilder} is added for up days
   * and only one for down days, because only one of each of these types is allowed per plot.
   * 
   * @see IXYPlotBuilder#series(IXYTimeSeriesBuilder)
   * @throws IllegalArgumentException If an VolumeXYTimeSeriesBuilder of same series type (uniform,
   *         up day, down day) was already added.
   */
  @Override
  public VolumeXYPlotBuilder series(IXYTimeSeriesBuilder<?> series) {

    if (series instanceof VolumeXYTimeSeriesBuilder) {

      VolumeXYTimeSeriesBuilder volBuilder = (VolumeXYTimeSeriesBuilder) series;

      if (volBuilder.isUniformSeries()) {

        if (uniformVolSeriesBuilder != null) {
          throw new IllegalArgumentException(
              "More than one " + VolumeXYTimeSeriesBuilder.class.getSimpleName()
                  + " being added as uniform type series but only one can be plotted.");
        }

        if (closeUpVolSeriesBuilder != null || closeDownVolSeriesBuilder != null) {

          throw new IllegalArgumentException(
              "Up or down OHLC day " + VolumeXYTimeSeriesBuilder.class.getSimpleName()
                  + "series already added. Cannot also use a uniform series.");
        }

        uniformVolSeriesBuilder = volBuilder;

      } else {

        if (uniformVolSeriesBuilder != null) {

          throw new IllegalArgumentException(
              "Uniform OHLC day " + VolumeXYTimeSeriesBuilder.class.getSimpleName()
                  + "series already added. Cannot also use a pair of up/down day series.");
        }

        if (volBuilder.isCloseUpSeries()) {

          if (closeUpVolSeriesBuilder != null) {

            throw new IllegalArgumentException(
                "More than one " + VolumeXYTimeSeriesBuilder.class.getSimpleName()
                    + " being added as close up type series but only one can be plotted.");
          }

          closeUpVolSeriesBuilder = volBuilder;

        } else {

          if (closeDownVolSeriesBuilder != null) {

            throw new IllegalArgumentException(
                "More than one " + VolumeXYTimeSeriesBuilder.class.getSimpleName()
                    + " being added as close down type series but only one can be plotted.");
          }

          closeDownVolSeriesBuilder = volBuilder;
        }
      }

    } else {

      elements.series(series);
    }

    return this;
  }

  @Override
  public VolumeXYPlotBuilder series(IXYDatasetBuilder<?> dataset) {
    elements.dataset(dataset);
    return this;
  }

  @Override
  public VolumeXYPlotBuilder line(LineBuilder line) {
    elements.line(line);
    return this;
  }

  @Override
  public VolumeXYPlotBuilder annotation(IXYAnnotationBuilder<?> annotation) {
    elements.annotation(annotation);
    return this;
  }

  @Override
  public VolumeXYPlotBuilder plotWeight(int weight) {
    elements.plotWeight(weight);
    return this;
  }

  @Override
  public int plotWeight() {
    return elements.plotWeight();
  }

  @Override
  public VolumeXYPlotBuilder yAxisName(String name) {
    elements.yAxisName(name);
    return this;
  }

  @Override
  public VolumeXYPlotBuilder yAxisRange(double lower, double upper)
      throws IllegalArgumentException {
    elements.yAxisRange(lower, upper);
    return this;
  }

  @Override
  public VolumeXYPlotBuilder yAxisTickSize(double size) {
    elements.yAxisTickSize(size);
    return this;
  }

  @Override
  public VolumeXYPlotBuilder backgroundColor(Paint color) {
    elements.backgroundColor(color);
    return this;
  }

  @Override
  public VolumeXYPlotBuilder axisFontColor(Paint color) {
    elements.axisFontColor(color);
    return this;
  }

  @Override
  public VolumeXYPlotBuilder axisColor(Paint color) {
    elements.axisColor(color);
    return this;
  }

  @Override
  public VolumeXYPlotBuilder gridLines() {
    elements.gridLines();
    return this;
  }

  @Override
  public VolumeXYPlotBuilder noGridLines() {
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

    TimeSeriesCollection xyCollection = new TimeSeriesCollection();
    StandardXYItemRenderer stdXRenderer = new StandardXYItemRenderer();

    double yMax = 0.0;
    double yMin = 0.0;

    final ZeroBasedIndexRange indexRange = elements.indexRange();

    for (IXYTimeSeriesBuilder<?> builder : elements.unmodifiableSeries()) {

      builder.indexRange(indexRange);
      builder.timeData(timeData);

      TimeSeries series = builder.build();

      if (!series.isEmpty()) {

        yMax = Math.max(yMax, series.getMaxY());
        yMin = Math.min(yMin, series.getMinY());

        stdXRenderer.setSeriesPaint(xyCollection.getSeriesCount(), builder.color());
        stdXRenderer.setSeriesStroke(xyCollection.getSeriesCount(), builder.style());
        xyCollection.addSeries(series);
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

    final XYPlot plot = new XYPlot(xyCollection, xAxis, yAxis, stdXRenderer);
    plot.setBackgroundPaint(elements.backgroundColor());
    plot.setDomainGridlinesVisible(elements.showGridLines());
    plot.setRangeGridlinesVisible(elements.showGridLines());
    plot.setDomainGridlinePaint(BuilderConstants.DEFAULT_GRIDLINE_PAINT);
    plot.setRangeGridlinePaint(BuilderConstants.DEFAULT_GRIDLINE_PAINT);

    if (uniformVolSeriesBuilder != null) {

      uniformVolSeriesBuilder.indexRange(indexRange);
      uniformVolSeriesBuilder.timeData(timeData);

      TimeSeriesCollection volCollection = new TimeSeriesCollection();

      TimeSeries series = uniformVolSeriesBuilder.build();

      yMax = Math.max(yMax, series.getMaxY());
      yMin = Math.min(yMin, series.getMinY());

      volCollection.addSeries(series);

      XYBarRenderer renderer = createXYBarRenderer(uniformVolSeriesBuilder.color(),
          DEFAULT_BAR_OUTLINE_COLOR);

      plot.setDataset(plot.getDatasetCount(), volCollection);
      plot.setRenderer(plot.getDatasetCount(), renderer, false);

    } else {

      closeUpVolSeriesBuilder.indexRange(indexRange);
      closeUpVolSeriesBuilder.timeData(timeData);

      TimeSeriesCollection volUpCollection = new TimeSeriesCollection();

      TimeSeries series = closeUpVolSeriesBuilder.build();

      yMax = Math.max(yMax, series.getMaxY());
      yMin = Math.min(yMin, series.getMinY());

      volUpCollection.addSeries(series);

      XYBarRenderer volUpRenderer = createXYBarRenderer(closeUpVolSeriesBuilder.color(),
          DEFAULT_BAR_OUTLINE_COLOR);

      int datasetIndex = plot.getDatasetCount();
      plot.setDataset(datasetIndex, volUpCollection);
      plot.setRenderer(datasetIndex, volUpRenderer, false);

      closeDownVolSeriesBuilder.indexRange(indexRange);
      closeDownVolSeriesBuilder.timeData(timeData);

      TimeSeriesCollection volDownCollection = new TimeSeriesCollection();

      series = closeDownVolSeriesBuilder.build();

      yMax = Math.max(yMax, series.getMaxY());
      yMin = Math.min(yMin, series.getMinY());

      volDownCollection.addSeries(series);

      XYBarRenderer volDownRenderer = createXYBarRenderer(closeDownVolSeriesBuilder.color(),
          DEFAULT_BAR_OUTLINE_COLOR);

      datasetIndex = plot.getDatasetCount();
      plot.setDataset(datasetIndex, volDownCollection);
      plot.setRenderer(datasetIndex, volDownRenderer, false);

    }

    for (LineBuilder line : elements.unmodifiableLines()) {
      ValueMarker marker = line.build();

      if (line.orientation() == Orientation.HORIZONTAL) {
        yMax = Math.max(yMax, marker.getValue());
        yMin = Math.min(yMin, marker.getValue());
        plot.addRangeMarker(marker);
      } else {
        // Vertical lines have infinite y-value so don't count them in y-min/max
        plot.addDomainMarker(marker);
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
      yAxis.setRange(yMin * ((yMin > 0.0) ? 0.95 : 1.05), yMax * ((yMax < 0.0) ? 0.99 : 1.05));
    }

    if (!elements.usingDefaultYAxisTickSize()) {
      yAxis.setTickUnit(new NumberTickUnit(elements.yAxisTickSize()));
    }

    return plot;
  }

  private XYBarRenderer createXYBarRenderer(Color fillColor, Color outlineColor) {
    return XYBarRendererBuilder.get().fillColor(fillColor).outlineColor(outlineColor).build();
  }

}
