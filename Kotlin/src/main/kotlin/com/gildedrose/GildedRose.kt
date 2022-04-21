package com.gildedrose

import com.gildedrose.GildedRose.ItemCategory.*

private const val MAX_QUALITY = 50

class GildedRose(var items: Array<Item>) {

    enum class ItemCategory {
        BackstagePass,
        AgedCheese,
        Legendary,
        Unremarkable
    }

    fun updateQuality() {
        for (item in items) {
            val category = deriveCategory(item)

            reassessQuality(category, item)

            ageItem(category, item)

            if (item.sellIn < 0) {
                expireItem(item, category)
            }
        }
    }

    private fun expireItem(item: Item, category: ItemCategory) {
        if (category != AgedCheese) {
            if (category != BackstagePass) {
                if (item.quality > 0) {
                    if (category != Legendary) {
                        item.quality = item.quality - 1
                    }
                }
            } else {
                item.quality = 0
            }
        } else {
            if (item.quality < MAX_QUALITY) {
                item.quality = item.quality + 1
            }
        }
    }

    private fun ageItem(category: ItemCategory, item: Item) {
        if (category != Legendary) {
            item.sellIn = item.sellIn - 1
        }
    }

    private fun reassessQuality(category: ItemCategory, item: Item) {
        if (category != AgedCheese && category != BackstagePass) {
            if (item.quality > 0) {
                if (category != Legendary) {
                    item.quality = item.quality - 1
                }
            }
        } else {
            if (item.quality < MAX_QUALITY) {
                item.quality = item.quality + 1

                if (category == BackstagePass) {
                    if (item.sellIn < 11) {
                        if (item.quality < MAX_QUALITY) {
                            item.quality = item.quality + 1
                        }
                    }

                    if (item.sellIn < 6) {
                        if (item.quality < MAX_QUALITY) {
                            item.quality = item.quality + 1
                        }
                    }
                }
            }
        }
    }

    private fun deriveCategory(item: Item): ItemCategory {
        return if (item.name == "Aged Brie") {
            AgedCheese
        } else if (item.name.startsWith("Backstage passes")) {
            return BackstagePass
        } else if (item.name.startsWith("Sulfuras")) {
            return Legendary
        } else {
            return Unremarkable
        }
    }
}

