package com.gildedrose

import com.gildedrose.AssetClass.*

/*
We've been told in the Kata not do modify the Item class. I guess that reflects some real world situations
where we want to improve one small part of a larger legacy system without changing interfaces.

It feels natural for "ItemCategory" to belong on Item. We could subclass Item but the interface to GildedRose passes
us an array of Items. It feels wrong to replace the instances inside the array with subclasses.

Plan B is to use composition. Create a new object which composes the item and the category. This feels nicer
Now our items know how to age themselves based on how they are configured.

We'll still allow GildedRose to control what the categories are, and understand how to classify items
 */

class CategorizedItem(item: Item, private val category: ItemCategory) {

    // Item is mutable, to be defensive we don't want to mutate the item provided to us, so we make a private copy of the attributes
    // This is more code and not necessary for the solution, but feels like a sensible defensive approach
    private var quality = item.quality
    private var sellIn = item.sellIn

    companion object {
        private const val MAX_QUALITY = 50
        private const val MIN_QUALITY = 0

        val DEFAULT_ADJUSTMENTS = listOf(
            QualityAdjustment(1..Int.MAX_VALUE, 1),
            QualityAdjustment(Int.MIN_VALUE..0, 2)
        )
    }

    fun copyTo(item: Item) {
        // An unfortunate consequence of the 'public API' for Guilded Rose requiring mutable objects.
        // We wouldn't need to do this if we kept our copy of the item and mutated it.
        item.quality = this.quality
        item.sellIn = this.sellIn
    }

    fun ageItemByADay() {
        reassessQuality()
        decreaseTimeToSell()
        expireIfNecessary()
    }

    private fun reassessQuality() {
        val qualityAdjustment = calculateQualityAdjustmentBasedOnTimeToSell()
        val newQuality = when (category.assetClass) {
            Appreciates -> {
                (quality + qualityAdjustment).coerceAtMost(MAX_QUALITY)
            }
            Depreciates -> {
                (quality - qualityAdjustment).coerceAtLeast(MIN_QUALITY)
            }
            else -> {
                quality
            }
        }

        quality = newQuality
    }

    private fun expireIfNecessary() {
        if (category.expires && sellIn < 0) {
            quality = 0
        }
    }

    private fun decreaseTimeToSell() {
        if (category.assetClass != Eternal) {
            sellIn = sellIn - 1
        }
    }

    private fun calculateQualityAdjustmentBasedOnTimeToSell(): Int {
        for (override in category.adjustmentOverrides) {
            if (sellIn in override.daysUntilExpiryInclusive) {
                return override.qualityAdjustmentStep
            }
        }

        for (default in DEFAULT_ADJUSTMENTS) {
            if (sellIn in default.daysUntilExpiryInclusive) {
                return default.qualityAdjustmentStep
            }
        }

        error("Could not find a range that corresponded to the supplied days. Check DEFAULT_ADJUSTMENTS to ensure entire int range is covered")
    }
}