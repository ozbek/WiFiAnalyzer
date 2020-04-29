/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.vrem.wifianalyzer.wifi.band

import androidx.core.util.Pair
import com.vrem.util.STRING_EMPTY
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class WiFiChannelsGHZ5Test {
    private val fixture: WiFiChannelsGHZ5 = WiFiChannelsGHZ5()

    @Test
    fun testGetWiFiChannelByFrequency() {
        validateFrequencyToChannel(5180, 5320, 10, 36, 2)
        validateFrequencyToChannel(5500, 5720, 10, 100, 2)
        validateFrequencyToChannel(5745, 5825, 10, 149, 2)
    }

    private fun validateFrequencyToChannel(frequencyStart: Int, frequencyEnd: Int, frequencyIncrement: Int, channelStart: Int, channelIncrement: Int) {
        var channel = channelStart
        var frequency = frequencyStart
        while (frequency <= frequencyEnd) {
            assertEquals(channel, fixture.wiFiChannelByFrequency(frequency).channel)
            channel += channelIncrement
            frequency += frequencyIncrement
        }
    }

    @Test
    fun testGetWiFiChannelByFrequencyFail() {
        assertEquals(WiFiChannel.UNKNOWN, fixture.wiFiChannelByFrequency(5167))
        assertEquals(WiFiChannel.UNKNOWN, fixture.wiFiChannelByFrequency(5828))
    }

    @Test
    fun testGetWiFiChannelFirst() {
        assertEquals(36, fixture.wiFiChannelFirst().channel)
    }

    @Test
    fun testGetWiFiChannelLast() {
        assertEquals(165, fixture.wiFiChannelLast().channel)
    }

    @Test
    fun testGetWiFiChannelPair() {
        val wiFiChannelPair: Pair<WiFiChannel, WiFiChannel> = fixture.wiFiChannelPairFirst(Locale.JAPAN.country)
        validatePair(36, 64, wiFiChannelPair)
    }

    @Test
    fun testGetWiFiChannelPairWithInvalidCountry() {
        val wiFiChannelPair: Pair<WiFiChannel, WiFiChannel> = fixture.wiFiChannelPairFirst(STRING_EMPTY)
        validatePair(36, 64, wiFiChannelPair)
    }

    @Test
    fun testGetWiFiChannelPairs() {
        val wiFiChannelPairs: List<Pair<WiFiChannel, WiFiChannel>> = fixture.wiFiChannelPairs()
        assertEquals(3, wiFiChannelPairs.size)
        validatePair(36, 64, wiFiChannelPairs[0])
        validatePair(100, 144, wiFiChannelPairs[1])
        validatePair(149, 165, wiFiChannelPairs[2])
    }

    private fun validatePair(expectedFirst: Int, expectedSecond: Int, pair: Pair<WiFiChannel, WiFiChannel>) {
        assertEquals(expectedFirst, pair.first!!.channel)
        assertEquals(expectedSecond, pair.second!!.channel)
    }

    @Test
    fun testGetWiFiChannelByFrequency5GHZ() {
        // setup
        val wiFiChannelPair: Pair<WiFiChannel, WiFiChannel> = fixture.wiFiChannelPairs()[1]
        // execute
        val actual: WiFiChannel = fixture.wiFiChannelByFrequency(2000, wiFiChannelPair)
        // validate
        assertEquals(WiFiChannel.UNKNOWN, actual)
    }

    @Test
    fun testGetWiFiChannelByFrequency5GHZInRange() {
        // setup
        val wiFiChannelPair: Pair<WiFiChannel, WiFiChannel> = fixture.wiFiChannelPairs()[1]
        // execute
        val actual: WiFiChannel = fixture.wiFiChannelByFrequency(wiFiChannelPair.first!!.frequency, wiFiChannelPair)
        // validate
        assertEquals(wiFiChannelPair.first, actual)
    }
}