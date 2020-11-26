# jfreechart-builder

A [builder pattern](https://en.wikipedia.org/wiki/Builder_pattern) module for working with the [jfreechart](https://github.com/jfree/jfreechart) library. Meant as a companion to [ChartFactory.java](https://github.com/jfree/jfreechart/blob/master/src/main/java/org/jfree/chart/ChartFactory.java) to build more complex charts with fewer lines of code.

Takes an opinionated approach to creating "good enough" charts while providing a more declarative way of parameterizing them.

Code like this:

```
public static final Stroke SOLID =
    new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

private static final Color DARK_GREEN = new Color(0, 150, 0);

ChartBuilder.instance()

    .title("Multi Plot Minute Time Series")

    .timeData(timestampsArray)

    .xyPlot(XYPlotBuilder.instance().yAxisName("Values")
        .series(XYTimeSeriesBuilder.instance().data(array1).color(Color.BLUE).style(SOLID))
        .series(XYTimeSeriesBuilder.instance().data(array2).color(Color.RED).style(SOLID))
        .series(XYTimeSeriesBuilder.instance().data(array3).color(DARK_GREEN).style(SOLID))
        .series(XYTimeSeriesBuilder.instance().data(array4).color(Color.MAGENTA).style(SOLID)))

    .xyPlot(XYPlotBuilder.instance().yAxisName("Amplitudes")
        .series(XYTimeSeriesBuilder.instance().data(array2).color(Color.GRAY).style(SOLID))
        .series(XYTimeSeriesBuilder.instance().data(array3).color(Color.LIGHT_GRAY).style(SOLID)))

    .xyPlot(XYPlotBuilder.instance().yAxisName("Series 1")
        .series(XYTimeSeriesBuilder.instance().data(array1).color(Color.BLUE).style(SOLID)))

    .xyPlot(XYPlotBuilder.instance().yAxisName("Series 2")
        .series(XYTimeSeriesBuilder.instance().data(array2).color(Color.RED).style(SOLID)))

    .xyPlot(XYPlotBuilder.instance().yAxisName("Series 3")
        .series(XYTimeSeriesBuilder.instance().data(array3).color(DARK_GREEN).style(SOLID)))

.build()
```

Produces a chart like this:

![A multi-plot minute time series chart](./screenshots/multi-plot-minute-time-series.png "Screenshot")

And code like this:

```
ChartBuilder.instance()

    .title("Stock Chart Time Series With Weekend Gaps")

    .timeData(timestampsArray) // Has weekend date timestamps missing

    .xyPlot(OhlcPlotBuilder.instance().yAxisName("Price").plotWeight(3)
        .series(OhlcSeriesBuilder.instance().ohlcv(ohlcv).upColor(Color.WHITE).downColor(Color.RED))
        .series(XYTimeSeriesBuilder.instance().name("MA(20)").data(sma20).color(Color.MAGENTA).style(SOLID))
        .series(XYTimeSeriesBuilder.instance().name("MA(50)").data(sma50).color(Color.BLUE).style(SOLID))
        .series(XYTimeSeriesBuilder.instance().name("MA(200)").data(sma200).color(Color.RED).style(SOLID)))

    .xyPlot(VolumeXYPlotBuilder.instance().yAxisName("Volume")
        .series(VolumeXYTimeSeriesBuilder.instance().ohlcv(ohlcv).closeUpSeries().color(Color.WHITE))
        .series(VolumeXYTimeSeriesBuilder.instance().ohlcv(ohlcv).closeDownSeries().color(Color.RED))
        .series(XYTimeSeriesBuilder.instance().name("MA(90)").data(volSma90).color(Color.BLUE).style(SOLID)))

    .xyPlot(XYPlotBuilder.instance().yAxisName("Stoch").yAxisRange(0.0, 100.0).yAxisTickSize(50.0)
        .series(XYTimeSeriesBuilder.instance().name("K(" + K + ")").data(stochK).color(Color.RED).style(SOLID))
        .series(XYTimeSeriesBuilder.instance().name("D(" + D + ")").data(stochD).color(Color.BLUE).style(SOLID))
        .line(LineBuilder.instance().orientation(Orientation.HORIZONTAL).atValue(80.0).color(Color.BLACK).style(SOLID))
        .line(LineBuilder.instance().orientation(Orientation.HORIZONTAL).atValue(50.0).color(Color.BLUE).style(SOLID))
        .line(LineBuilder.instance().orientation(Orientation.HORIZONTAL).atValue(20.0).color(Color.BLACK).style(SOLID)))

    .build()
```

Produces a chart like this:

![A stock chart time series chart with weekend gaps](./screenshots/stock-chart-time-series-weekend-gaps.png "Screenshot")


## Demo App

See the [jfreechart-builder-demo](https://github.com/matoos32/jfreechart-builder-demo) for an interactive demo used for development.

## Scope

Currently only supports XY time series plots and uses a [CombinedDomainXYPlot](https://github.com/jfree/jfreechart/blob/master/src/main/java/org/jfree/chart/plot/CombinedDomainXYPlot.java) in all cases. This produces left-to-right horizontal time axes and vertical value axes. The time axis is meant to be shared by all sub-plots. If you need different time axes then you'll need to `build()` multiple charts and lay those out as desired in your app.

Currently also only supports lines, stock market OHLC candlestick charts, and stock market volume bar charts.

In the future, more parameterization may be added like specifying background and axis colors, or even the actual series renderer objects themselves to fully leverage what [jfreechart](https://github.com/jfree/jfreechart) provides.


## Thread-safety and garbage collection

No thread-safety measures are deliberately taken. If you require thread-safety then either provide deep copies of parameter objects or or synchronize access to builders and data vs. what your app is doing.

Generally, primitive data arrays are copied into **jfreechart** objects. **jfreechart-builder** will maintain references to other objects passed-in like strings, colors, and drawing strokes. When the builders and charts they produce go out of scope,
the objects you provided (and other objects that may be referencing them) should be garbage collected as applicable.


## Versioning

The major and minor numbers are the same as the **jfreechart** major and minor used. This is to indicate general compatibility. The incremental ("patch") number is the version of this module.


## License

**jfreechart-builder** is not affiliated with the **jfreechart** project but for compatibility is provided under the terms of the same [LGPL 2.1 license](./license-LGPL.txt).

You should be aware of the contents of the **jfreechart-builder** JAR file built form this project.

It's understood it will contain the compiled `.class` files of only **jfreechart-builder** and should not incorporate any from **jfreechart**, however you must verify its contents to know what your build tools are actually producing.

If you need clarification on the LGPL vs. Java, please see the [FSF's tech note about it](https://www.gnu.org/licenses/lgpl-java.html).


## Incorporating into your project

### Building Prerequisites

* JDK 8 or greater [[1](https://openjdk.java.net/)] [[2](https://www.oracle.com/java/)] installed.
* [Apache Maven](https://maven.apache.org/) installed.
* Internet connection so Maven can download artifacts or you provide and install those into your local Maven repo by alternative means.

### Installing source code

`git clone` this repo locally.


### Building and installing the JAR

```
cd path/to/the/cloned/repo
```

Next, use `git` to checkout the desired branch or tag.

If you want to simply build the jar and figure out what to do with it next ...

```
mvn package
```

The jar will be in the `target/` folder.


If you want to build and install the jar into your Maven repo:

```
mvn install
```


### Including the dependency in a client project

Add this dependency to your project's `.pom` file:

```
<dependency>
  <groupId>com.jfcbuilder</groupId>
  <artifactId>jfreechart-builder</artifactId>
  <version>1.5.0</version>
<dependency>
```


## Contributing

Contributions are welcome. The project maintainers' time permitting, merge/pull requests will be reviewed. If accepted they will be merged. No guarantees are made that requests will be reviewed or merged.
