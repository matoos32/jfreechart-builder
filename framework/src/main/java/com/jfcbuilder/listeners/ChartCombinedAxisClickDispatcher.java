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

package com.jfcbuilder.listeners;

import java.util.List;
import java.util.Objects;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;

/**
 * Specialized {@link ChartMouseListener} implementation that handles clicks on a {@link ChartPanel}
 * and double-dispatches click handling to every combined axis sub-plot it contains.
 * <p>
 * Originally created to support synchronizing cross-hairs on combined axis sub-plots but could be
 * used for other purposes.
 */
public class ChartCombinedAxisClickDispatcher implements ChartMouseListener {

  /**
   * The parent {@link ChartPanel} containing the combined axis plot.
   */
  private ChartPanel parent;

  /**
   * Constructor.
   * 
   * @param parent The parent {@link ChartPanel} containing the combined axis plot
   * @throws NullPointerException if {@code parent} is {@code null}.
   */
  public ChartCombinedAxisClickDispatcher(ChartPanel parent) {
    Objects.requireNonNull(parent, "The parent ChartPanel must be provided.");
    this.parent = parent;
  }

  @Override
  public void chartMouseClicked(ChartMouseEvent e) {

    if (parent != null && e.getSource() instanceof JFreeChart) {
      
      Plot plot = e.getChart().getPlot();

      if (plot instanceof CombinedDomainXYPlot) {

        CombinedDomainXYPlot combinedPlot = (CombinedDomainXYPlot) plot;

        // TODO: Remove this unchecked cast suppression when a new version of JFreeChart is released
        // that provides a getSubplots() that doesn't return just List with type erasure. See:
        // https://github.com/jfree/jfreechart/blob/v1.5.3/src/main/java/org/jfree/chart/plot/CombinedDomainXYPlot.java#L311
        @SuppressWarnings("unchecked")
        List<XYPlot> subplots = combinedPlot.getSubplots();
        final int x = e.getTrigger().getX();
        final int y = (int) parent.getChartRenderingInfo().getPlotInfo().getDataArea().getY();
        subplots.forEach(p -> p.handleClick(x, y, parent.getChartRenderingInfo().getPlotInfo()));
      }
    }
  }

  @Override
  public void chartMouseMoved(ChartMouseEvent e) {
    // Do nothing.
  }
}
