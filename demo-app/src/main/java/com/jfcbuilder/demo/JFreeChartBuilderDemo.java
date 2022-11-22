/*
 * jfreechart-builder-demo: a demonstration app for jfreechart-builder
 * 
 * (C) Copyright 2020, by Matt E.
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

package com.jfcbuilder.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.TextAnchor;

import com.jfcbuilder.builders.ChartBuilder;
import com.jfcbuilder.builders.MarkerBuilder;
import com.jfcbuilder.builders.OhlcPlotBuilder;
import com.jfcbuilder.builders.OhlcSeriesBuilder;
import com.jfcbuilder.builders.VolumeXYPlotBuilder;
import com.jfcbuilder.builders.VolumeXYTimeSeriesBuilder;
import com.jfcbuilder.builders.XYArrowBuilder;
import com.jfcbuilder.builders.XYBoxBuilder;
import com.jfcbuilder.builders.XYLineBuilder;
import com.jfcbuilder.builders.XYTextBuilder;
import com.jfcbuilder.builders.XYTimeSeriesBuilder;
import com.jfcbuilder.builders.XYTimeSeriesPlotBuilder;
import com.jfcbuilder.demo.data.providers.AscendingDateTimeGenerator;
import com.jfcbuilder.demo.data.providers.IDateTimeSeriesProvider;
import com.jfcbuilder.demo.data.providers.IDohlcvProvider;
import com.jfcbuilder.demo.data.providers.RandomDohlcvGenerator;
import com.jfcbuilder.demo.data.providers.numeric.Sinusoid;
import com.jfcbuilder.demo.data.providers.numeric.Sma;
import com.jfcbuilder.demo.data.providers.numeric.StochasticOscillator;
import com.jfcbuilder.demo.data.providers.numeric.StochasticOscillator.StochData;
import com.jfcbuilder.types.BuilderConstants;
import com.jfcbuilder.types.DohlcvSeries;
import com.jfcbuilder.types.MinimalDateFormat;

/**
 * Test class for demonstrating JFreeChartBuilder capabilities. Contains a main application that
 * generates some test data and instantiates a window that displays charts built using the builder
 * framework. There is a <b><i>Demonstrations</i></b> drop-down menu from which you can pick the
 * different charts.
 */
public class JFreeChartBuilderDemo {

  // Setup constant reusable test data and settings ...
  
  private static final Stroke SOLID_LINE = BuilderConstants.SOLID_LINE;
  private static final Stroke THICK_DASHED_LINE = BuilderConstants.THICK_DASHED_LINE;

  private static final Color DARK_BLUE = BuilderConstants.DARK_BLUE;
  private static final Color DARK_GREEN = BuilderConstants.DARK_GREEN;
  private static final Color DARK_RED = BuilderConstants.DARK_RED;

  private static final Color TRANSPARENT_GREEN = BuilderConstants.TRANSPARENT_GREEN;
  private static final Color TRANSPARENT_DARK_GREEN = BuilderConstants.TRANSPARENT_DARK_GREEN;
  
  // Prepare the application data to be plotted ...

  private static final LocalDateTime endDate = LocalDateTime.now();
  private static final LocalDateTime startDate = endDate.minus(18, ChronoUnit.MONTHS);

  private static final Set<DayOfWeek> ohlcvSkipDays = Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

  private static IDateTimeSeriesProvider timeProvider = AscendingDateTimeGenerator.get();

  private static final long[] ohlcvDates = timeProvider.getDateTimes(startDate, endDate,
      ChronoUnit.DAYS, ohlcvSkipDays);

  private static final IDohlcvProvider dohlcvProvider = RandomDohlcvGenerator.get();

  private static final DohlcvSeries dohlcv = dohlcvProvider.getDohlcv(ohlcvDates);

  private static final double[] sma20 = Sma.calculate(20, dohlcv.closes());
  private static final double[] sma50 = Sma.calculate(50, dohlcv.closes());
  private static final double[] sma200 = Sma.calculate(200, dohlcv.closes());
  private static final double[] volSma90 = Sma.calculate(90, dohlcv.volumes());

  private static final int K = 14;
  private static final int D = 3;
  private static StochData stoch = StochasticOscillator.calculate(K, D, dohlcv.highs(),
      dohlcv.lows(), dohlcv.closes());

  private static final int ohlcEndIndex = dohlcv.dates().length - 1;
  private static final int ohlcStartIndex = (int) Math.max(0.0, ohlcEndIndex * 0.75); // ~25% of the
                                                                                      // actual data

  private static final Set<DayOfWeek> NO_SINUSOID_SKIP_DAYS = Collections.emptySet();

  private static final long[] sinusoidDays = timeProvider.getDateTimes(startDate, endDate,
      ChronoUnit.DAYS, NO_SINUSOID_SKIP_DAYS);

  private static final int numSinusoidDayElems = sinusoidDays.length;

  private static final double[] sinDaily1 = Sinusoid.getRandSeries(60.0, numSinusoidDayElems);
  private static final double[] sinDaily2 = Sinusoid.getRandSeries(60.0, numSinusoidDayElems);
  private static final double[] sinDaily3 = Sinusoid.getRandSeries(60.0, numSinusoidDayElems);
  private static final double[] sinDaily4 = Sinusoid.getRandSeries(60.0, numSinusoidDayElems);

  private static final int sinusoidDailyEndIndex = numSinusoidDayElems - 1;
  private static final int sinusoidDailyStartIndex = (int) Math.max(0.0,
      sinusoidDailyEndIndex * 0.2); // 80% of data

  private static final LocalDateTime endHour = LocalDateTime.now();
  private static final LocalDateTime startHour = endHour.minus(8, ChronoUnit.HOURS);

  private static final long[] sinusoidMinutes = timeProvider.getDateTimes(startHour, endHour,
      ChronoUnit.MINUTES, NO_SINUSOID_SKIP_DAYS);

  private static final int numSinusoiMinuteElems = sinusoidMinutes.length;

  private static final double[] sinMinute1 = Sinusoid.getRandSeries(40.0, numSinusoiMinuteElems);
  private static final double[] sinMinute2 = Sinusoid.getRandSeries(60.0, numSinusoiMinuteElems);
  private static final double[] sinMinute3 = Sinusoid.getRandSeries(80.0, numSinusoiMinuteElems);
  private static final double[] sinMinute4 = Sinusoid.getRandSeries(100.0, numSinusoiMinuteElems);

  private static final int sinusoidMinuteEndIndex = numSinusoiMinuteElems - 1;
  private static final int sinusoidMinuteStartIndex = 0; // All data
  
  private static class StockChartConfig {
    
    public boolean majorGrid;
    public Paint majorGridColor;
    public Stroke majorGridStyle;
    public boolean minorGrid;
    public Paint minorGridColor;
    public Stroke minorGridStyle;
    public boolean annotate;
    
    public StockChartConfig() {
      this(true, XYPlot.DEFAULT_GRIDLINE_PAINT, XYPlot.DEFAULT_GRIDLINE_STROKE,
           false, XYPlot.DEFAULT_GRIDLINE_PAINT, XYPlot.DEFAULT_GRIDLINE_STROKE,
           false);
    }
    
    public StockChartConfig(
      boolean majorGrid, Paint majorGridColor, Stroke majorGridStyle,
      boolean minorGrid, Paint minorGridColor, Stroke minorGridStyle,
      boolean annotate) {

      this.majorGrid = majorGrid;
      this.majorGridColor = majorGridColor;
      this.majorGridStyle = majorGridStyle;
      this.minorGrid = minorGrid;
      this.minorGridColor = minorGridColor;
      this.minorGridStyle = minorGridStyle;
      this.annotate = annotate;
    }    
  }
  
  
  private static JFreeChart simpleTimeSeriesWithAnnotations() {
    
    long[] timeArray = sinusoidDays;
    double[] array1 = sinDaily1;
    int startIndex = sinusoidDailyStartIndex;
    int endIndex = sinusoidDailyEndIndex;
    
    final int arrowIndex = (int) (0.75 * timeArray.length);
    final double arrowX = (double) timeArray[arrowIndex];
    final double arrowY = array1[arrowIndex];
    final String arrowTxt = String.format("%.1f", arrowY);
    
    return ChartBuilder.get()
      .title("Simple Time Series With Annotations")
      .timeData(timeArray)
      .indexRange(startIndex, endIndex)
      .xyPlot(XYTimeSeriesPlotBuilder.get()
        .series(XYTimeSeriesBuilder.get().name("Amplitude").data(array1).color(Color.BLUE).style(SOLID_LINE))
        .annotation(XYArrowBuilder.get().x(arrowX).y(arrowY).angle(180.0).color(Color.RED).text(arrowTxt))
        .annotation(XYArrowBuilder.get().x(arrowX).y(arrowY).angle(0.0).color(Color.RED))
        .annotation(XYTextBuilder.get().x(arrowX).y(arrowY).color(DARK_GREEN)
           .text("This value!").textPaddingLeft(5).textAlign(TextAnchor.BASELINE_LEFT).angle(90.0)))
      .build();
  }
  
  
  private static JFreeChart multiDailyTimeSeries() {

    long[] timeArray = sinusoidDays;
    double[] array1 = sinDaily1;
    double[] array2 = sinDaily2;
    double[] array3 = sinDaily3;
    double[] array4 = sinDaily4;
    int startIndex = sinusoidDailyStartIndex;
    int endIndex = sinusoidDailyEndIndex;
    
    return ChartBuilder.get()
      .title("Multi Daily Time Series")
      .timeData(timeArray)
      .indexRange(startIndex, endIndex)
      .xyPlot(XYTimeSeriesPlotBuilder.get().yAxisName("Values")
        .series(XYTimeSeriesBuilder.get().data(array1).color(Color.BLUE).style(SOLID_LINE))
        .series(XYTimeSeriesBuilder.get().data(array2).color(Color.RED).style(SOLID_LINE))
        .series(XYTimeSeriesBuilder.get().data(array3).color(DARK_GREEN).style(SOLID_LINE))
        .series(XYTimeSeriesBuilder.get().data(array4).color(Color.MAGENTA).style(SOLID_LINE)))
      .build();
  }
  
  
  private static JFreeChart multiPlotMinuteTimeSeries() {
    
    long[] timeArray = sinusoidMinutes;
    double[] array1 = sinMinute1;
    double[] array2 = sinMinute2;
    double[] array3 = sinMinute3;
    double[] array4 = sinMinute4;
    int startIndex = sinusoidMinuteStartIndex;
    int endIndex = sinusoidMinuteEndIndex;
    
    return ChartBuilder.get()
      .title("Multi Plot Minute Time Series")
      .timeData(timeArray)
      .indexRange(startIndex, endIndex)

      // Deprecation note: preserve a call to gridLines() for testing until it's removed.
      
      .xyPlot(XYTimeSeriesPlotBuilder.get().yAxisName("Values")
        .backgroundColor(Color.DARK_GRAY).axisColor(Color.RED).axisFontColor(Color.BLUE).gridLines()
        .series(XYTimeSeriesBuilder.get().data(array1).color(Color.YELLOW).style(SOLID_LINE))
        .series(XYTimeSeriesBuilder.get().data(array2).color(Color.RED).style(SOLID_LINE))
        .series(XYTimeSeriesBuilder.get().data(array3).color(Color.GREEN).style(SOLID_LINE))
        .series(XYTimeSeriesBuilder.get().data(array4).color(Color.MAGENTA).style(SOLID_LINE)))

      .xyPlot(XYTimeSeriesPlotBuilder.get().yAxisName("Amplitudes").noGridLines()
        .series(XYTimeSeriesBuilder.get().data(array2).color(Color.BLACK).style(SOLID_LINE))
        .series(XYTimeSeriesBuilder.get().data(array3).color(Color.LIGHT_GRAY).style(SOLID_LINE)))

      .xyPlot(XYTimeSeriesPlotBuilder.get().yAxisName("Series 1")
        .backgroundColor(DARK_GREEN).axisColor(Color.RED).axisFontColor(Color.BLUE)
        .majorGrid(false).minorGrid(true).minorGridColor(Color.CYAN).minorGridStyle(BuilderConstants.DASHED_LINE)
        .series(XYTimeSeriesBuilder.get().data(array1).color(Color.GREEN).style(SOLID_LINE)))

      .xyPlot(XYTimeSeriesPlotBuilder.get().yAxisName("Series 2")
        .backgroundColor(DARK_RED).axisColor(Color.RED).axisFontColor(Color.BLUE)
        .series(XYTimeSeriesBuilder.get().data(array2).color(Color.RED).style(SOLID_LINE)))

      .xyPlot(XYTimeSeriesPlotBuilder.get().yAxisName("Series 3")
        .backgroundColor(DARK_BLUE).axisColor(Color.RED).axisFontColor(Color.BLUE)
        .series(XYTimeSeriesBuilder.get().data(array3).color(Color.CYAN).style(SOLID_LINE)))

      .build();
  }

  private static ChartBuilder getDailyStockChartBuilderDefaults() {
    long[] timeArray = dohlcv.dates();
    int startIndex = ohlcStartIndex;
    int endIndex = ohlcEndIndex;
    
    final DecimalFormat volNumFormat = new DecimalFormat("#");
    volNumFormat.setGroupingUsed(true);
    volNumFormat.setGroupingSize(3);

    return ChartBuilder.get()

    .timeData(timeArray).indexRange(startIndex, endIndex)

    .xyPlot(OhlcPlotBuilder.get()
      .yAxisName("Price").plotWeight(3)
      .series(OhlcSeriesBuilder.get().ohlcv(dohlcv))
      .series(XYTimeSeriesBuilder.get().name("MA(50)").data(sma50)))
    
    .xyPlot(VolumeXYPlotBuilder.get()
      .yAxisName("Volume")
      .series(VolumeXYTimeSeriesBuilder.get().ohlcv(dohlcv))
      .series(XYTimeSeriesBuilder.get().name("MA(90)").data(volSma90)))
    
    .xyPlot(XYTimeSeriesPlotBuilder.get()
      .yAxisName("Stochastics(" + K + ", " + D + ")").yAxisRange(0.0, 100.0).yAxisTickSize(50.0)
      .series(XYTimeSeriesBuilder.get().data(stoch.getPctK()))
      .series(XYTimeSeriesBuilder.get().data(stoch.getPctD()))
      .marker(MarkerBuilder.get().horizontal().at(80.0))
      .marker(MarkerBuilder.get().horizontal().at(50.0))
      .marker(MarkerBuilder.get().horizontal().at(20.0)));
  }
  
  private static ChartBuilder getDailyStockChartBuilder(StockChartConfig cfg) {
    
    long[] timeArray = dohlcv.dates();
    int startIndex = ohlcStartIndex;
    int endIndex = ohlcEndIndex;
    
    final DecimalFormat volNumFormat = new DecimalFormat("#");
    volNumFormat.setGroupingUsed(true);
    volNumFormat.setGroupingSize(3);

    ChartBuilder chart = ChartBuilder.get();
    
    chart.timeData(timeArray).indexRange(startIndex, endIndex);

    OhlcPlotBuilder ohlcPlot = OhlcPlotBuilder.get()
      .yAxisName("Price").plotWeight(3)
      .majorGrid(cfg.majorGrid).majorGridColor(cfg.majorGridColor).majorGridStyle(cfg.majorGridStyle)
      .minorGrid(cfg.minorGrid).minorGridColor(cfg.minorGridColor).minorGridStyle(cfg.minorGridStyle)
      .series(OhlcSeriesBuilder.get().ohlcv(dohlcv).upColor(Color.WHITE).downColor(Color.RED))
      .series(XYTimeSeriesBuilder.get().name("MA(20)").data(sma20).color(Color.MAGENTA).style(SOLID_LINE))
      .series(XYTimeSeriesBuilder.get().name("MA(50)").data(sma50).color(Color.BLUE).style(SOLID_LINE))
      .series(XYTimeSeriesBuilder.get().name("MA(200)").data(sma200).color(Color.RED).style(SOLID_LINE));
    
    OhlcPlotBuilder volumePlot = VolumeXYPlotBuilder.get()
      .yAxisName("Volume").yTickFormat(volNumFormat)
      .majorGrid(cfg.majorGrid).majorGridColor(cfg.majorGridColor).majorGridStyle(cfg.majorGridStyle)
      .minorGrid(cfg.minorGrid).minorGridColor(cfg.minorGridColor).minorGridStyle(cfg.minorGridStyle)
      .series(VolumeXYTimeSeriesBuilder.get().ohlcv(dohlcv).upColor(Color.DARK_GRAY).downColor(Color.RED))
      .series(XYTimeSeriesBuilder.get().name("MA(90)").data(volSma90).color(Color.BLUE).style(SOLID_LINE));
    
    XYTimeSeriesPlotBuilder tsPlot = XYTimeSeriesPlotBuilder.get()
      .yAxisName("Stochastics(" + K + ", " + D + ")").yAxisRange(0.0, 100.0).yAxisTickSize(50.0)
      .majorGrid(cfg.majorGrid).majorGridColor(cfg.majorGridColor).majorGridStyle(cfg.majorGridStyle)
      .minorGrid(cfg.minorGrid).minorGridColor(cfg.minorGridColor).minorGridStyle(cfg.minorGridStyle)
      .series(XYTimeSeriesBuilder.get().data(stoch.getPctK()).color(Color.RED).style(SOLID_LINE))
      .series(XYTimeSeriesBuilder.get().data(stoch.getPctD()).color(Color.BLUE).style(SOLID_LINE))
      .marker(MarkerBuilder.get().horizontal().at(80.0).color(Color.BLACK).style(SOLID_LINE))
      .marker(MarkerBuilder.get().horizontal().at(50.0).color(Color.BLUE).style(SOLID_LINE))
      .marker(MarkerBuilder.get().horizontal().at(20.0).color(Color.BLACK).style(SOLID_LINE));

    if (cfg.annotate) {

      // Points ("p") of interest in the plots
      int lookback = 10;
      int p1Index = ohlcEndIndex - lookback;
      long p1Date = timeArray[p1Index];
      double p1Price = dohlcv.highs()[p1Index];
      double p1Volume = dohlcv.volumes()[p1Index];
      double p1Stoch = stoch.getPctK()[p1Index];
      
      lookback = 5;
      int p2Index = ohlcEndIndex - lookback;
      long p2Date = timeArray[p2Index];
      double p2Price = dohlcv.highs()[p2Index];
      double p2Volume = dohlcv.volumes()[p2Index];
      double p2Stoch = stoch.getPctK()[p2Index];
      
      lookback = 36;
      int p3Index = ohlcEndIndex - lookback;
      long p3Date = timeArray[p3Index];
      double p3Open = dohlcv.opens()[p3Index];
      double p3Close = dohlcv.closes()[p3Index];
      double p3Volume = dohlcv.volumes()[p3Index];
      
      lookback = 28;
      int p4Index = ohlcEndIndex - lookback;
      long p4Date = timeArray[p4Index];
      
      double resistanceLevel = dohlcv.closes()[0];
      double volumeLine = dohlcv.volumes()[0];
      
      ohlcPlot

        .annotation(XYArrowBuilder.get()
          .x(p1Date).y(p1Price)
          .text(String.format("%.2f", p1Price))
          .angle(270.0).textAlign(TextAnchor.BOTTOM_CENTER)
          .arrowLength(40.0).tipRadius(3.0)
          .color(DARK_GREEN))

        .annotation(XYLineBuilder.get()
          .x1(p1Date).y1(p1Price)
          .x2(p2Date).y2(p2Price)
          .color(Color.MAGENTA).style(THICK_DASHED_LINE))
        
        .annotation(XYBoxBuilder.get()
          .x1(p3Date).y1(p3Open)
          .x2(p4Date).y2(p3Close)
          .outlineStyle(SOLID_LINE).outlineColor(TRANSPARENT_DARK_GREEN)
          .fillColor(TRANSPARENT_GREEN))
        
        .marker(MarkerBuilder.get().horizontal().at(resistanceLevel)
          .color(Color.LIGHT_GRAY).style(SOLID_LINE));


      volumePlot
      
        .annotation(XYArrowBuilder.get()
          .x(p1Date).y(p1Volume)
          .text(String.format("%.0f", p1Volume))
          .angle(325.0).textAlign(TextAnchor.BOTTOM_CENTER)
          .arrowLength(30.0).tipRadius(4.0)
          .color(DARK_GREEN))

        .annotation(XYLineBuilder.get()
          .x1(p1Date).y1(p1Volume)
          .x2(p2Date).y2(p2Volume)
          .color(Color.MAGENTA).style(THICK_DASHED_LINE))

        .annotation(XYBoxBuilder.get()
          .x1(p3Date).y1(p3Volume)
          .x2(p4Date).y2(p3Volume/2.0)
          .outlineStyle(THICK_DASHED_LINE).outlineColor(Color.CYAN)
          .fillColor(Color.YELLOW))
        
        .marker(MarkerBuilder.get().horizontal().at(volumeLine)
          .color(DARK_GREEN).style(SOLID_LINE));


      tsPlot

        .annotation(XYArrowBuilder.get()
          .x(p1Date).y(p1Stoch)
          .text(String.format("%.0f", p1Stoch))
          .angle(90.0).textAlign(TextAnchor.BOTTOM_CENTER).textPaddingLeft(5)
          .color(DARK_GREEN))
  
        .annotation(XYLineBuilder.get()
          .x1(p1Date).y1(p1Stoch)
          .x2(p2Date).y2(p2Stoch)
          .color(Color.MAGENTA).style(THICK_DASHED_LINE))
      
        .annotation(XYBoxBuilder.get()
          .x1(p3Date).y1(70.0)
          .x2(p4Date).y2(30.0)
          .outlineStyle(THICK_DASHED_LINE));
    }
    
    chart.xyPlot(ohlcPlot);

    chart.xyPlot(volumePlot);

    chart.xyPlot(tsPlot);

    return chart;
  }
  
  private static JFreeChart stockChartDailyWithGapsDefaultDateLabels() {
    
    StockChartConfig config = new StockChartConfig();

    return getDailyStockChartBuilder(config)
      .title("Stock Chart Time Series | Weekend Gaps | Default date labels (default locale)")
      .build();
  }
  
  private static JFreeChart stockChartDailyWithGapsDefaults() {
    return getDailyStockChartBuilderDefaults()
      .title("Stock Chart Time Series | Weekend Gaps | Defaults (No Styling)")
      .build();
  }
  
  private static JFreeChart stockChartDailyWithGapsWithAnnotations() {
    
    StockChartConfig config = new StockChartConfig();
    config.annotate = true;
    
    return getDailyStockChartBuilder(config)
      .title("Stock Chart Time Series | Weekend Gaps | Default date labels | Markers and Annotations")
      .build();
  }
  
  private static JFreeChart stockChartDailyWithGapsNoGrid() {
    
    StockChartConfig config = new StockChartConfig();
    config.majorGrid = false;
    config.minorGrid = false;
    
    return getDailyStockChartBuilder(config)
      .title("Stock Chart Time Series | Weekend Gaps | Default date labels | No Grid")
      .build();
  }
  
  private static JFreeChart stockChartDailyWithGapsCustomGrid() {
    
    StockChartConfig config = new StockChartConfig();
    config.majorGrid = true;
    config.majorGridColor = Color.CYAN;
    config.majorGridStyle = BuilderConstants.DASHED_LINE;
    config.minorGrid = true;
    config.minorGridColor = DARK_GREEN;
    config.minorGridStyle = BuilderConstants.THIN_DASHED_LINE;
    
    return getDailyStockChartBuilder(config)
      .title("Stock Chart Time Series | Weekend Gaps | Default date labels | Custom Grid")
      .build();
  }
  
  private static JFreeChart stockChartDailyWithGapsCustomDateLabels() {
    
    StockChartConfig config = new StockChartConfig();

    return getDailyStockChartBuilder(config)
      .title("Stock Chart Time Series | Weekend Gaps | SimpleDateFormat English locale")
      .dateFormat(new SimpleDateFormat("yy-MMM-dd", Locale.ENGLISH))
      .verticalTickLabels(true)
      .build();
  }
  
  private static JFreeChart stockChartDailyWithGapsMinimalDateLabels() {
    
    StockChartConfig config = new StockChartConfig();

    return getDailyStockChartBuilder(config)
      .title("Stock Chart Time Series | Weekend Gaps | Derived DateFormat (MinimalDateFormat(1) default locale)")
      .dateFormat(new MinimalDateFormat(1))
      .build();
  }
  
  private static JFreeChart stockChartDailyNoGapsDefaults() {
    return getDailyStockChartBuilderDefaults()
      .title("Stock Chart Time Series | Gapless | Defaults (No Styling)")
      .showTimeGaps(false)
      .build();
  }
  
  private static JFreeChart stockChartDailyNoGapsDefaultDateLabels() {
    
    StockChartConfig config = new StockChartConfig();

    return getDailyStockChartBuilder(config)
      .title("Stock Chart Time Series | Gapless | Default date labels (MinimalDateFormat(3) default locale)")
      .showTimeGaps(false)
      .build();
  }
  
  private static JFreeChart stockChartDailyNoGapsWithAnnotations() {
    
    StockChartConfig config = new StockChartConfig();
    config.annotate = true;
    
    return getDailyStockChartBuilder(config)
      .title("Stock Chart Time Series | Gapless | Default date labels | Markers and Annotations")
      .showTimeGaps(false)
      .build();
  }
  
  private static JFreeChart stockChartDailyNoGapsNoGrid() {
    
    StockChartConfig config = new StockChartConfig();
    config.majorGrid = false;
    config.minorGrid = false;
    
    return getDailyStockChartBuilder(config)
      .title("Stock Chart Time Series | Gapless | Default date labels | No Grid")
      .showTimeGaps(false)
      .build();
  }
  
  private static JFreeChart stockChartDailyNoGapsCustomGrid() {
    
    StockChartConfig config = new StockChartConfig();
    config.majorGrid = true;
    config.majorGridColor = Color.CYAN;
    config.majorGridStyle = BuilderConstants.DASHED_LINE;
    config.minorGrid = true;
    config.minorGridColor = DARK_GREEN;
    config.minorGridStyle = BuilderConstants.THIN_DASHED_LINE;
    
    return getDailyStockChartBuilder(config)
      .title("Stock Chart Time Series | Gapless | Default date labels | Custom Grid")
      .showTimeGaps(false)
      .build();
  }

  private static JFreeChart stockChartDailyNoGapsCustomDateLabels() {
    
    StockChartConfig config = new StockChartConfig();

    return getDailyStockChartBuilder(config)
      .title("Stock Chart Time Series | Gapless | SimpleDateFormat numeric month default locale")
      .showTimeGaps(false)
      .dateFormat(new SimpleDateFormat("yy-MM-dd"))
      .verticalTickLabels(true)
      .build();
  }

  /**
   * Main entry point to this demonstration application.
   * 
   * @param args The command line arguments
   */
  public static void main(String[] args) {
    
    List<JFreeChart> charts = new ArrayList<>();

    charts.add(simpleTimeSeriesWithAnnotations());

    charts.add(multiDailyTimeSeries());

    charts.add(multiPlotMinuteTimeSeries());

    charts.add(stockChartDailyWithGapsDefaults());
    
    charts.add(stockChartDailyWithGapsDefaultDateLabels());

    charts.add(stockChartDailyWithGapsWithAnnotations());
    
    charts.add(stockChartDailyWithGapsNoGrid());
    
    charts.add(stockChartDailyWithGapsCustomGrid());
    
    charts.add(stockChartDailyWithGapsCustomDateLabels());

    charts.add(stockChartDailyWithGapsMinimalDateLabels());

    charts.add(stockChartDailyNoGapsDefaults());

    charts.add(stockChartDailyNoGapsDefaultDateLabels());

    charts.add(stockChartDailyNoGapsWithAnnotations());
    
    charts.add(stockChartDailyNoGapsNoGrid());
    
    charts.add(stockChartDailyNoGapsCustomGrid());

    charts.add(stockChartDailyNoGapsCustomDateLabels());
    
    launchChartDemoWindow(charts);
  }


  /**
   * Helper method to build a GUI for showcasing the demo charts.
   * 
   * @param charts Container of charts to be demonstrated
   * @throws HeadlessException If a problem occurs
   */
  protected static void launchChartDemoWindow(List<JFreeChart> charts)
      throws HeadlessException {

    ChartPanel panel = new ChartPanel(null);

    JFrame frame = new JFrame(ChartBuilder.class.getSimpleName() + " Demo App");
    frame.add(panel);
    frame.setSize(new Dimension(800, 600));
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JMenuBar menuBar = new JMenuBar();
    frame.setJMenuBar(menuBar);

    JMenu demoMenu = new JMenu("Demonstrations");
    menuBar.add(demoMenu);
    
    ButtonGroup group = new ButtonGroup();
    JRadioButtonMenuItem item;

    for (JFreeChart chart : charts) {
      item = new JRadioButtonMenuItem(chart.getTitle().getText());
      item.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          if (e.getSource() instanceof JRadioButtonMenuItem) {
            JRadioButtonMenuItem context = (JRadioButtonMenuItem) e.getSource();
            context.setSelected(true);
            panel.setChart(chart);
          }
        }
      });
      group.add(item);
      demoMenu.add(item);

      if (panel.getChart() == null) {
        item.doClick();
      }
    }

    frame.setVisible(true);
  }
}
