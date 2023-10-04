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

package com.jfcbuilder.listeners;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;

/**
 * Specialized {@link ChartMouseListener} implementation that handles clicks on a {@link ChartPanel}
 * and double-dispatches click handling to every combined axis sub-plot it contains.
 * <p>
 * Originally created to support synchronizing crosshairs on combined axis sub-plots but could be
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
    Objects.requireNonNull(parent, "Parent ChartPanel must be provided.");
    this.parent = parent;
  }

  /**
   * Gets the reference to the {@link ChartPanel} set as the parent/context.
   * 
   * @return The parent {@link ChartPanel}
   */
  public ChartPanel getParent() {
    return parent;
  }
  
  @Override
  public void chartMouseClicked(ChartMouseEvent e) {
    dispatchToSubplots(e);
  }

  /**
   * Logic for dispatching a {@link ChartMouseEvent} to each {@link XYPlot} subplot's
   * {@code handleClick()} method.
   * 
   * @param e The event to be dispatched
   */
  protected void dispatchToSubplots(ChartMouseEvent e) {

    if (parent != null && e != null) {

      Plot chartPlot = e.getChart().getPlot();

      if (chartPlot instanceof CombinedDomainXYPlot) {

        CombinedDomainXYPlot combinedPlot = (CombinedDomainXYPlot) chartPlot;

        // Here we fan out the click to every subplot. We need a common x-coordinate so use the
        // anchor point calculated by the parent ChartPanel (the adjusted click coordinate). We also
        // need to identify and provide the data area of subplots to the click handler call. That's
        // needed in XYPlot handleClick() to convert the click/anchor y-coordinate to the specific
        // subplot y-axis data coordinate, which may be outside the visible subplot data area.

        // Note: calling getEntity() on the event may not provide the clicked-on plot because other
        // types of entities like XY items in a series are clickable. To find the clicked chart we
        // simply use the anchor and search for the subplot within the combined plot.
        
        PlotRenderingInfo parentInfo = parent.getChartRenderingInfo().getPlotInfo();
        Point2D anchor = parent.getAnchor();
        XYPlot clickedPlot = combinedPlot.findSubplot(parentInfo, anchor);
        
        final int x = (int) anchor.getX();
        int y;

        int numSubplots = parentInfo.getSubplotCount();

        for (int i = 0; i < numSubplots; i++) {

          // WORKAROUND:
          // For plots that didn't actually get clicked on, provide the combined plot's plot info
          // and data area y-coordinate so that the click will be deemed to yes have been within the
          // acceptable area but then in downstream draw() processing it should be determined the
          // y-coordinate/value is outside the visible range of the data area. So we are assuming
          // and relying on this downstream processing, but it gives subplots a chance to do
          // something specific regarding the shared axis dimension. See XYPlot's handleClick() and
          // draw() methods.

          PlotRenderingInfo subplotInfo = parentInfo.getSubplotInfo(i);

          Rectangle2D area = subplotInfo.getDataArea();
          Point2D aSubplotCoord = new Point2D.Double(area.getX(), area.getY());

          XYPlot subplot = combinedPlot.findSubplot(parentInfo, aSubplotCoord);

          if (subplot != null) {

            boolean isClicked = subplot == clickedPlot;

            PlotRenderingInfo info = isClicked ? subplotInfo : parentInfo;

            y = (int) (isClicked ? anchor.getY() : parentInfo.getDataArea().getY());

            subplot.handleClick(x, y, info);
          }

        }
      }
    }    
  }
  
  @Override
  public void chartMouseMoved(ChartMouseEvent e) {
    // Do nothing.
  }
}
