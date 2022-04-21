package com.gildedrose

import com.gildedrose.GildedRose.ItemCategory.*

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
            if (category != AgedCheese && category != BackstagePass) {
                if (item.quality > 0) {
                    if (category != Legendary) {
                        item.quality = item.quality - 1
                    }
                }
            } else {
                if (item.quality < 50) {
                    item.quality = item.quality + 1

                    if (category == BackstagePass) {
                        if (item.sellIn < 11) {
                            if (item.quality < 50) {
                                item.quality = item.quality + 1
                            }
                        }

                        if (item.sellIn < 6) {
                            if (item.quality < 50) {
                                item.quality = item.quality + 1
                            }
                        }
                    }
                }
            }

            if (category != Legendary) {
                item.sellIn = item.sellIn - 1
            }

            if (item.sellIn < 0) {
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
                    if (item.quality < 50) {
                        item.quality = item.quality + 1
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

