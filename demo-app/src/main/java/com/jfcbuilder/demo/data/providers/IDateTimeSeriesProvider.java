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

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

/**
 * Interface for providers of a chronological value series.
 */
public interface IDateTimeSeriesProvider {

  /**
   * Gets an array of date-time values in milliseconds since the epoch start. There will be as many
   * records as needed to divide the time window defined by a start and end date-time by the desired
   * temporal spacing between date-time values.
   * 
   * @param startDate The starting date-time of the series.
   * @param endDate The end date-time of the series.
   * @param spacing The temporal spacing (i.e. distance) between each value in the series.
   * @param skipDays Set identifying specific days of the week that should be excluded from the
   *        generated set, for example to create gaps or "blackout" days.
   * @return New array containing all the generated values.
   */
  long[] getDateTimes(LocalDateTime startDate, LocalDateTime endDate, ChronoUnit spacing,
      Set<DayOfWeek> skipDays);

}
