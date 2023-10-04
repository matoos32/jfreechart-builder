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

import java.awt.Color;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleInsets;

import com.jfcbuilder.adapters.NumberFormatDateAdapter;
import com.jfcbuilder.types.BuilderConstants;
import com.jfcbuilder.types.MinimalDateFormat;
import com.jfcbuilder.types.ZeroBasedIndexRange;

/**
 * The top-level builder for creating JFreeChart instances using configured parameters.
 */
public class ChartBuilder {

  private static final String DEFAULT_TITLE = "";

  private static final ZeroBasedIndexRange NO_INDEX_RANGE = null;
  private static final long[] NO_TIME_DATA = null;

  private static final double DEFAULT_SHARED_AXIS_MARGIN = 0.005;

  private String title;
  private ZeroBasedIndexRange indexRange;
  private long[] timeData;
  private DateFormat dateFormat;
  private boolean verticalTickLabels;
  private boolean showTimeGaps;
  private List<IXYTimeSeriesPlotBuilder<?>> xyPlotBuilders;

  /**
   * Hidden constructor.
   */
  private ChartBuilder() {
    title = DEFAULT_TITLE;
    indexRange = NO_INDEX_RANGE;
    timeData = NO_TIME_DATA;
    dateFormat = null;
    verticalTickLabels = false;
    showTimeGaps = true;
    xyPlotBuilders = new ArrayList<>();
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static ChartBuilder get() {
    return new ChartBuilder();
  }

  /**
   * Sets the main title of the chart.
   * 
   * @param title The title to be set
   * @return Same instance of this builder for chaining calls
   */
  public ChartBuilder title(String title) {
    this.title = (title == null) ? DEFAULT_TITLE : title;
    return this;
  }

  /**
   * Sets the zero-based indexes defining what elements to actually use in all the configured data
   * sets. If you don't call this method the full range of elements in the timeData array will be
   * used.
   * 
   * @param startIndex Zero based index of the start of the range
   * @param endIndex   Zero based index of the end of the range
   * @return Same instance of this builder for chaining method calls
   * @throws IllegalArgumentException If startIndex or endIndex are negative, or if startIndex is
   *                                  greater than endIndex
   */
  public ChartBuilder indexRange(int startIndex, int endIndex) throws IllegalArgumentException {
    indexRange = new ZeroBasedIndexRange(startIndex, endIndex);
    return this;
  }

  /**
   * Sets the epoch milliseconds date-time data common to all plots. You can also specify a start
   * and end index defining what parts of this data to use (see {@code indexRange(int, int)}
   * method). This is to allow use of an existing data set without having to create a truncated copy
   * of it just to be able to use the builder. The result is less computational and memory usage.
   * This class will not modify the date-time data passed to it but be warned it will access it
   * without any thread-safe protections. If you require thread-safety your code should not further
   * modify the data passed-in here, or you should supply a copy of your data.
   * 
   * @param timeData Array of date-time instances represented at milliseconds since the epoch
   * 
   * @return Same instance of this builder for chaining method calls
   * @throws NullPointerException If timeData is null
   */
  public ChartBuilder timeData(long[] timeData) {

    Objects.requireNonNull(timeData, "Time data is required.");

    this.timeData = timeData;

    return this;
  }
  
  /**
   * Overrides the time axis tick label format.
   * 
   * @param format The {@link DateFormat} to use. {@code null} is accepted to use the default
   *               format.
   * @return Same instance of this builder for chaining method calls
   */
  public ChartBuilder dateFormat(DateFormat format) {
    dateFormat = format;
    return this;
  }

  /**
   * Override the time axis flag specifying if tick labels should be drawn vertical or horizontal.
   * 
   * @param useVertical True to draw vertical labels, false for horizontal
   * @return Same instance of this builder for chaining method calls
   */
  public ChartBuilder verticalTickLabels(boolean useVertical) {
    this.verticalTickLabels = useVertical;
    return this;
  }

  /**
   * Sets whether or not time gaps should be rendered.
   * 
   * @param showTimeGaps True to render time gaps, false otherwise
   * @return Same instance of this builder for chaining method calls
   */
  public ChartBuilder showTimeGaps(boolean showTimeGaps) {
    this.showTimeGaps = showTimeGaps;
    return this;
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
   * Registers an IXYTimeSeriesPlotBuilder whose {@code build()} method will be called to generate
   * its plot when this chart builder's {@code build()} method is called.
   * 
   * @param builder The series builder representing the series that it will build
   * @return Same instance of this builder for chaining method calls
   */
  public ChartBuilder xyPlot(IXYTimeSeriesPlotBuilder<?> builder) {
    xyPlotBuilders.add(builder);
    return this;
  }

  private void checkBuildPreconditions() throws IllegalStateException {

    if (timeData == NO_TIME_DATA) {
      throw new IllegalStateException("No time data configured");
    }
  }

  /**
   * Builds the JFreeChart using all configured settings.
   * 
   * @return New instance of a JFreeChart corresponding to all configured data sets and settings
   * @throws IllegalStateException If time data to be shared by all plots was not set
   */
  public JFreeChart build() {

    checkBuildPreconditions();

    ZeroBasedIndexRange range = (indexRange != null) ? indexRange
        : new ZeroBasedIndexRange(0, timeData.length - 1);

    ValueAxis sharedAxis;

    if (showTimeGaps) {

      sharedAxis = createTimeAxis(timeData[range.getStartIndex()], timeData[range.getEndIndex()]);

    } else {

      sharedAxis = createGaplessTimeAxis(range, timeData);
    }

    sharedAxis.setLowerMargin(DEFAULT_SHARED_AXIS_MARGIN);
    sharedAxis.setUpperMargin(DEFAULT_SHARED_AXIS_MARGIN);
    sharedAxis.setAutoRange(true);
    sharedAxis.setTickLabelFont(BuilderConstants.DEFAULT_FONT);
    sharedAxis.setVerticalTickLabels(verticalTickLabels);

    CombinedDomainXYPlot parent = new CombinedDomainXYPlot(sharedAxis);

    for (IXYTimeSeriesPlotBuilder<?> b : xyPlotBuilders) {

      b = b.indexRange(range).xAxis(sharedAxis);

      if (b instanceof IXYTimeSeriesPlotBuilder) {
        IXYTimeSeriesPlotBuilder<?> tsBuilder = (IXYTimeSeriesPlotBuilder<?>) b;
        tsBuilder.timeData(timeData).showTimeGaps(showTimeGaps);
      }

      XYPlot xyPlot = b.build();

      xyPlot.setRangeAxisLocation(AxisLocation.TOP_OR_RIGHT, false);
      xyPlot.setDomainCrosshairVisible(true); // Enabled now to allow clicking to move crosshair
      xyPlot.setRangeCrosshairVisible(true);
      xyPlot.setDomainCrosshairLockedOnData(false);
      xyPlot.setRangeCrosshairLockedOnData(false);

      parent.add(xyPlot, b.plotWeight());
    }

    parent.setGap(10.0); // sub-plot spacing

    JFreeChart chart = createChart(parent, title);
    return chart;

  }

  /**
   * Creates a new {@link NumberAxis} with some opinionated settings for plot layouts.
   * <p>
   * The axis is configured with a special {@link NumberFormat} override implemented by
   * {@code jfreechart-builder}'s {@link NumberFormatDateAdapter}. This number format uses the
   * {@link NumberAxis} values as indices into a supplied array of millisecond date-time values.
   * Those are subsequently formatted into desired date strings.
   * 
   * @param startDateMs The axis range start date in milliseconds since the epoch start
   * @param endDateMs   The axis range end date in milliseconds since the epoch start
   * @return The new axis instance
   */
  private ValueAxis createGaplessTimeAxis(ZeroBasedIndexRange range, long[] timeData) {

    final String axisLabel = null;
    NumberAxis axis = new NumberAxis(axisLabel);

    axis.setAutoRangeStickyZero(false);

    DateFormat df = dateFormat != null ? dateFormat : new MinimalDateFormat();

    // A means is needed to receive number axis values, which in this context are zero-based indices
    // of actual time values stored in the timeData array. We then need to convert the time values
    // to a desired date string format. Java's NumberFormat defines abstract format() methods that
    // receive that number axis value for formatting. Using that, we then need to produce the date
    // formatting behavior of DateFormat. There is no practical means to lookup the time values
    // outside of a specialized NumberFormat implementation. This is the stateful
    // NumberFormatDateAdapter below. This probably means a chart/plot can't subsequently have its
    // data updated without recreating a whole new chart unless the timeAxis created here is
    // accessed in the existing chart, with setNumberFormatOverride() called on it again using
    // updated range, timeData, and date format.
    axis.setNumberFormatOverride(new NumberFormatDateAdapter(range, timeData, df));

    return axis;
  }

  /**
   * Creates a new {@link DateAxis} with some opinionated settings for plot layouts.
   * 
   * This axis renders time gaps on charts where points in time don't have data.
   * 
   * @param startDateMs The axis range start date in milliseconds since the epoch start
   * @param endDateMs   The axis range end date in milliseconds since the epoch start
   * @return The new axis instance
   */
  private ValueAxis createTimeAxis(long startDateMs, long endDateMs) {

    final String axisLabel = null;
    DateAxis axis = new DateAxis(axisLabel);

    if (dateFormat != null) {
      axis.setDateFormatOverride(dateFormat);
    }
    
    return axis;
  }

  /**
   * Creates a new JFreeChart object using an XYPlot instance.
   *
   * @param plot  The plot to be rendered
   * @param title A title string to render at the top of the chart
   * @return new instance of the image object
   */
  private JFreeChart createChart(final XYPlot plot, final String title) {

    final boolean showLegend = false;
    JFreeChart chart = new JFreeChart(title, BuilderConstants.DEFAULT_FONT, plot, showLegend);
    chart.setTitle(new TextTitle(title, BuilderConstants.DEFAULT_FONT));
    chart.setPadding(new RectangleInsets(0, 0, 0, 0));
    chart.setTextAntiAlias(true);
    chart.setAntiAlias(true);
    chart.setBorderVisible(false);
    chart.setBackgroundPaint(Color.WHITE);

    return chart;
  }
}
