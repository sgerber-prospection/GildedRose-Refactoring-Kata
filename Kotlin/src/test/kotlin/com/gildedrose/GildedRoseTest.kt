package com.gildedrose

import io.kotest.matchers.ints.shouldBeExactly
import org.junit.jupiter.api.Test

internal class GildedRoseTest {

    @Test
    fun `the number of days left to sell an item decreases each day`() {
        // Given ten days to sell an item
        val items = arrayOf(Item(
            "Whatever",
            sellIn = 10,
            quality = 0
        ))
        val app = GildedRose(items)

        // If a day passes...
        app.updateQuality()

        // We have one less day to sell (nine)
        app.items[0].sellIn shouldBeExactly 9
    }

    @Test
    fun `normal items decrease in quality every day when not past the sell by date`() {
        // Given more than a day left to sell an item and item with quality of 5
        val items = arrayOf(Item(
            "Normal item",
            sellIn = 10,
            quality = 5
        ))
        val app = GildedRose(items)

        // If a day passes
        app.updateQuality()

        // The quality will have decreased by one to four
        app.items[0].quality shouldBeExactly 4
    }

    @Test
    fun `normal items decrease in quality twice as fast when past the sell by date`() {
        // Given more than a day left to sell an item and item with quality of 5
        val items = arrayOf(Item(
            "Normal item",
            sellIn = 0,
            quality = 5
        ))
        val app = GildedRose(items)

        // If a day passes
        app.updateQuality()

        // The quality will have decreased by two
        app.items[0].quality shouldBeExactly 3
    }

    @Test
    fun `Aged Brie increases in quality as time passes`() {
        // Starting with aged Brie that has a quality of five
        val items = arrayOf(Item(
            "Aged Brie",
            sellIn = 3,
            quality = 5
        ))
        val app = GildedRose(items)

        // If a day passes
        app.updateQuality()

        // The quality will have increased by one to be six
        app.items[0].quality shouldBeExactly 6
    }

    @Test
    fun `Aged Brie quality cannot improve beyond 50`() {
        // Starting with aged Brie that has a quality of 49
        val items = arrayOf(Item(
            "Aged Brie",
            sellIn = 100,
            quality = 49
        ))
        val app = GildedRose(items)

        // If it sits on the shelf for a week...
        repeat(7) {
            app.updateQuality()
        }

        // The quality will have increased by one to be six
        app.items[0].quality shouldBeExactly 50
    }

    @Test
    fun `Backstage Passes increase in quality slightly when more than ten days out from the concert`() {
        // Starting with a backstage pass more than ten days left
        val items = arrayOf(Item(
            "Backstage passes to a TAFKAL80ETC concert",
            sellIn = 11,
            quality = 0
        ))
        val app = GildedRose(items)

        // If a day passes
        app.updateQuality()

        // The quality will have increased by one
        app.items[0].quality shouldBeExactly 1
    }

    @Test
    fun `Backstage Passes increase in quality slightly more when between five and ten days out from the concert`() {
        // Starting with a backstage pass with ten days left (or less...)
        val items = arrayOf(Item(
            "Backstage passes to a TAFKAL80ETC concert",
            sellIn = 10,
            quality = 0
        ))
        val app = GildedRose(items)

        // If a day passes
        app.updateQuality()

        // The quality will have increased by two
        app.items[0].quality shouldBeExactly 2
    }

    @Test
    fun `Backstage Passes increase in quality a fair bit more when five or less days from the concert`() {
        // Starting with a backstage pass with five days left (or less...)
        val items = arrayOf(Item(
            "Backstage passes to a TAFKAL80ETC concert",
            sellIn = 5,
            quality = 0
        ))
        val app = GildedRose(items)

        // If a day passes
        app.updateQuality()

        // The quality will have increased by two
        app.items[0].quality shouldBeExactly 3
    }

    @Test
    fun `Backstage Passes are worthless if the concert has passed`() {
        // Starting with a backstage pass with no time left
        val items = arrayOf(Item(
            "Backstage passes to a TAFKAL80ETC concert",
            sellIn = 0,
            quality = 100
        ))
        val app = GildedRose(items)

        // If a day passes
        app.updateQuality()

        // The quality will have dropped to nothing
        app.items[0].quality shouldBeExactly 0
    }

    @Test
    fun `Sulfuras never degrades in quality`() {
        // Given the Hand of Rangoros of a certain quality...
        val initialQuality = 80
        val items = arrayOf<Item>(Item("Sulfuras, Hand of Ragnaros", 10, initialQuality))
        val app = GildedRose(items)

        // When time passes
        app.updateQuality()

        // Quality never changes, because it is a legendary item
        app.items[0].quality shouldBeExactly initialQuality
    }
}


