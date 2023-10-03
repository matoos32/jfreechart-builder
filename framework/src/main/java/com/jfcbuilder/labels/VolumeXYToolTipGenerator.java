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

package com.jfcbuilder.labels;

import java.text.DateFormat;
import java.text.NumberFormat;

import org.jfree.chart.labels.AbstractXYItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;

/**
 * Specialized tooltip generator for OHLC volume XY data.
 */
public class VolumeXYToolTipGenerator extends AbstractXYItemLabelGenerator
  implements XYToolTipGenerator {

  private static final long serialVersionUID = 1L;

  private static final String TOOLTIP_FORMAT_STR = "Date={1} Volume={2}";

  private static final DateFormat   DATE_FORMAT   = DateFormat.getInstance();
  private static final NumberFormat NUMBER_FORMAT = NumberFormat.getIntegerInstance();

  public VolumeXYToolTipGenerator() {
    super(TOOLTIP_FORMAT_STR, NUMBER_FORMAT, NUMBER_FORMAT);
  }

  @Override
  public String generateToolTip(XYDataset dataset, int series, int item) {

    return generateLabelString(dataset, series, item);
  }

  /**
   * Specialized {@code createItemArray()} to ensure up/down volume values are retrieved.
   */
  @Override
  protected Object[] createItemArray(XYDataset dataset, int series, int item) {

    Object[] result = new Object[3];

    result[0] = "";

    if (dataset instanceof OHLCDataset) {

      OHLCDataset ohlc = (OHLCDataset) dataset;

      result[1] = DATE_FORMAT.format(ohlc.getX(series, item));

      double open = ohlc.getOpenValue(series, item);
      double close = ohlc.getCloseValue(series, item);

      double y = close > open ? close : open;

      if (Double.isNaN(y)) {
        result[2] = "";
      } else {
        result[2] = NUMBER_FORMAT.format(y);
      }

    } else {
      // Should never be here.
      result[1] = "";
      result[2] = "";
    }

    return result;
  }

}
