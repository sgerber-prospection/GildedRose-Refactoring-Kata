package com.gildedrose

import com.gildedrose.ItemCategory.*


class GildedRose(var items: Array<Item>) {

    fun updateQuality() {
        for (item in items) {
            val categorizedItem = categoriseItem(item)

            categorizedItem.ageItemByADay()

            categorizedItem.copyTo(item)
        }
    }

    private fun categoriseItem(item: Item): CategorizedItem {
        val clone = Item(name = item.name, sellIn = item.sellIn, quality = item.quality)
        val category = deriveCategory(item)

        return CategorizedItem(clone, category)
    }

    private fun deriveCategory(item: Item): ItemCategory {
        return if (item.name == "Aged Brie") {
            AgedCheese
        } else if (item.name.startsWith("Backstage passes")) {
            return BackstagePass
        } else if (item.name.startsWith("Sulfuras")) {
            return Legendary
        } else if (item.name.startsWith("Conjured")) {
            return Conjured
        } else {
            return Unremarkable
        }
    }

}

