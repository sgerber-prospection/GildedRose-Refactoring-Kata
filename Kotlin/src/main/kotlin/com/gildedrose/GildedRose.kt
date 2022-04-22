package com.gildedrose

import com.gildedrose.AssetClass.Appreciates
import com.gildedrose.AssetClass.Depreciates
import com.gildedrose.ItemCategory.*

private const val MAX_QUALITY = 50
private const val MIN_QUALITY = 0

class GildedRose(var items: Array<Item>) {

    companion object {
        val DEFAULT_ADJUSTMENTS = listOf(
            QualityAdjustment(1..Int.MAX_VALUE, 1),
            QualityAdjustment(Int.MIN_VALUE..0, 2)
        )
    }

    fun updateQuality() {
        for (item in items) {
            val category = deriveCategory(item)

            reassessQuality(category, item)

            ageItem(category, item)

            expireIfNecessary(item, category)
        }
    }

    private fun expireIfNecessary(item: Item, category: ItemCategory) {
        if (category.expires && item.sellIn < 0) {
            item.quality = 0
        }
    }

    private fun ageItem(category: ItemCategory, item: Item) {
        if (category != Legendary) {
            item.sellIn = item.sellIn - 1
        }
    }

    private fun reassessQuality(category: ItemCategory, item: Item) {
        val qualityAdjustment = category.calculateQualityAdjustmentBasedOnTimeToSell(item.sellIn)
        val newQuality = when (category.assetClass) {
            Appreciates -> {
                (item.quality + qualityAdjustment).coerceAtMost(MAX_QUALITY)
            }
            Depreciates -> {
                (item.quality - qualityAdjustment).coerceAtLeast(MIN_QUALITY)
            }
            else -> {
                item.quality
            }
        }

        item.quality = newQuality
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

    private fun ItemCategory.calculateQualityAdjustmentBasedOnTimeToSell(daysRemaining: Int): Int {
        for (override in adjustmentOverrides) {
            if (daysRemaining in override.daysUntilExpiryInclusive) {
                return override.qualityAdjustmentStep
            }
        }

        for (default in DEFAULT_ADJUSTMENTS) {
            if (daysRemaining in default.daysUntilExpiryInclusive) {
                return default.qualityAdjustmentStep
            }
        }

        error("Could not find a range that corresponded to the supplied days. Check DEFAULT_ADJUSTMENTS to ensure entire int range is covered")
    }
}

