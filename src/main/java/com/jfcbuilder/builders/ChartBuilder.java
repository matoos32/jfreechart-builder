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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;

import com.jfcbuilder.types.BuilderConstants;
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
  private List<IXYPlotBuilder<?>> xyPlotBuilders;

  /**
   * Hidden constructor.
   */
  private ChartBuilder() {
    title = DEFAULT_TITLE;
    indexRange = NO_INDEX_RANGE;
    timeData = NO_TIME_DATA;
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
   * Registers an IXYPlotBuilder whose {@code build()} method will be called to generate its plot
   * when this chart builder's {@code build()} method is called.
   * 
   * @param builder The series builder representing the series that it will build
   * @return Same instance of this builder for chaining method calls
   */
  public ChartBuilder xyPlot(IXYPlotBuilder<?> builder) {
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

    ValueAxis sharedAxis = createTimeAxis(timeData[range.getStartIndex()],
        timeData[range.getEndIndex()]);

    CombinedDomainXYPlot parent = new CombinedDomainXYPlot(sharedAxis);

    for (IXYPlotBuilder<?> b : xyPlotBuilders) {
      XYPlot xyPlot = b.indexRange(range).xAxis(sharedAxis).timeData(timeData).build();
      xyPlot.setRangeAxisLocation(AxisLocation.TOP_OR_RIGHT, false);
      xyPlot.setDomainCrosshairVisible(true);
      xyPlot.setRangeCrosshairVisible(true);
      parent.add(xyPlot, b.plotWeight());
    }

    parent.setGap(10.0); // sub-plot spacing

    JFreeChart chart = createChart(parent, title);
    return chart;

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
