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

package com.jfcbuilder.demo.data.providers;

import com.jfcbuilder.types.DohlcvSeries;

/**
 * Interface for stock market Date Open High Low Close Volume (DOHLCV) data providers.
 */
public interface IDohlcvProvider {

  /**
   * Gets a DohlcvSeries series instance corresponding to specific date-time values. The provided
   * date-time values array may be referenced by the returned DohlcvSeries and hence may not be
   * garbage collected until that object is itself garbage collected.
   * 
   * @param dateTimes Array of ascending date-time values representing milliseconds since the epoch
   *        start.
   * @return Instance of a DohlcvSeries that uses the supplied date-time values.
   */
  DohlcvSeries getDohlcv(long[] dateTimes);

}