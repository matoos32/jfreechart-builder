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
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;

import com.jfcbuilder.types.ZeroBasedIndexRange;

/**
 * The top-level builder for creating JFreeChart instances using configured parameters.
 */
public class ChartBuilder {

  private static final String DEFAULT_TITLE = "";

  private static final ZeroBasedIndexRange NO_INDEX_RANGE = null;
  private static final long[] NO_TIME_DATA = null;

  private String title;
  private ZeroBasedIndexRange indexRange;
  private long[] timeData;
  private boolean showTimeGaps;
  private List<IXYTimeSeriesPlotBuilder<?>> xyPlotBuilders;

  /**
   * Hidden constructor.
   */
  private ChartBuilder() {
    title = DEFAULT_TITLE;
    indexRange = NO_INDEX_RANGE;
    timeData = NO_TIME_DATA;
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
    this.title = (title == null) ? "" : title;
    return this;
  }

  /**
   * Sets the zero-based indexes defining what elements to actually use in all the configured data
   * sets. If you don't call this method the full range of elements in the timeData array will be
   * used.
   * 
   * @param startIndex Zero based index of the start of the range
   * @param endIndex Zero based index of the end of the range
   * @return Same instance of this builder for chaining method calls
   * @throws IllegalArgumentException If startIndex or endIndex are negative, or if startIndex is
   *         greater than endIndex
   */
  public ChartBuilder indexRange(int startIndex, int endIndex) throws IllegalArgumentException {
    indexRange = new ZeroBasedIndexRange(startIndex, endIndex);
    return this;
  }

  /**
   * Sets the date-time data common to all plots. Also requires specifying a start and end index
   * defining what parts of this data to use. This is to allow use of an existing data set without
   * having to create a truncated copy of it just to be able to use the builder. The result is less
   * computational and memory usage. This class will not modify the date-time data passed to it but
   * be warned it will access it without any thread-safe protections. If you require thread-safety
   * your code should not further modify the data passed-in here, or you should supply a copy of
   * your data.
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
   * Registers an IXYTimeSeriesPlotBuilder whose {@code build()} method will be called to
   * generate its plot when this chart builder's {@code build()} method is called.
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

    CombinedDomainXYPlot parent = new CombinedDomainXYPlot(sharedAxis);

    for (IXYTimeSeriesPlotBuilder<?> b : xyPlotBuilders) {

      b = b.indexRange(range).xAxis(sharedAxis);

      if (b instanceof IXYTimeSeriesPlotBuilder) {
        IXYTimeSeriesPlotBuilder<?> tsBuilder = (IXYTimeSeriesPlotBuilder<?>) b;
        tsBuilder.timeData(timeData).showTimeGaps(showTimeGaps);
      }

      XYPlot xyPlot = b.build();

      xyPlot.setRangeAxisLocation(AxisLocation.TOP_OR_RIGHT, false);
      xyPlot.setDomainCrosshairVisible(true);
      xyPlot.setRangeCrosshairVisible(true);
      parent.add(xyPlot, b.plotWeight());
    }

    parent.setGap(10.0); // sub-plot spacing

    JFreeChart chart = createChart(parent, title);
    return chart;

  }

  private ValueAxis createGaplessTimeAxis(ZeroBasedIndexRange range, long[] timeData) {

    final String timeAxisLabel = null;
    NumberAxis theTimeAxis = new NumberAxis(timeAxisLabel);
    theTimeAxis.setAutoRange(true);
    theTimeAxis.setAutoRangeStickyZero(false);
    theTimeAxis.setLowerMargin(0.005);
    theTimeAxis.setUpperMargin(0.005);
    theTimeAxis.setTickLabelFont(BuilderConstants.DEFAULT_FONT);
    theTimeAxis.setNumberFormatOverride(new NumberFormat() {

      // Generated value
      private static final long serialVersionUID = 2306007623536333089L;

      private int lastIntNum = 0;

      @Override
      public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {

        if (Double.isNaN(number)) {
          return toAppendTo;
        }

        final int intNum = (int) number;

        if ((intNum < 0) || (intNum >= timeData.length)) {
          return toAppendTo;
        } else {
          final double timeval = (double) timeData[range.getStartIndex() + intNum];
          final double lastTimeval = (double) timeData[range.getStartIndex() + lastIntNum];
          lastIntNum = intNum;

          if (Double.isNaN(timeval)) {
            return toAppendTo;
          }

          LocalDate date = Instant.ofEpochMilli((long) timeval).atZone(ZoneId.systemDefault())
              .toLocalDate();

          LocalDate lastDate = Instant.ofEpochMilli((long) lastTimeval)
              .atZone(ZoneId.systemDefault()).toLocalDate();

          if (!Double.isNaN(lastTimeval) && date.getMonth() != lastDate.getMonth()) {
            final String monthStr = date.getMonth().toString();

            return toAppendTo.append(
                monthStr.substring(0, 1).toUpperCase() + monthStr.substring(1, 3).toLowerCase());

          } else {
            return toAppendTo.append(date.getDayOfMonth());
          }
        }
      }

      @Override
      public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
        return format((double) number, toAppendTo, pos);
      }

      @Override
      public Number parse(String source, ParsePosition parsePosition) {
        // Not supported
        return null;
      }

    });

    return theTimeAxis;
  }

  /**
   * Creates a new temporal axis with some opinionated settings for plot layouts.
   * 
   * @param startDateMs The axis range start date in milliseconds since the epoch start
   * @param endDateMs The axis range end date in milliseconds since the epoch start
   * @return The new axis instance
   */
  private DateAxis createTimeAxis(long startDateMs, long endDateMs) {

    final String timeAxisLabel = null;
    DateAxis timeAxis = new DateAxis(timeAxisLabel);

    timeAxis.setLowerMargin(0.005); // Margin values tuned using trial and error.
    timeAxis.setUpperMargin(0.005);
    timeAxis.setTickLabelFont(BuilderConstants.DEFAULT_FONT);

    RegularTimePeriod startDate = new Millisecond(new Date(startDateMs));
    RegularTimePeriod endDate = new Millisecond(new Date(endDateMs));

    timeAxis.setRange(startDate.getStart(), endDate.getEnd());

    return timeAxis;
  }

  /**
   * Creates a new JFreeChart object using an XYPlot instance.
   *
   * @param plot The plot to be rendered
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
