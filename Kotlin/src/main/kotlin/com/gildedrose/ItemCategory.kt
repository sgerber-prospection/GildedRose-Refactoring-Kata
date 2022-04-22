package com.gildedrose

import com.gildedrose.AssetClass.*

enum class ItemCategory(
    val assetClass: AssetClass,
    val expires: Boolean = false,
    val adjustmentOverrides: List<QualityAdjustment> = emptyList()
) {
    // Normal items decay in value as per the defaults
    Unremarkable(Depreciates),

    // Aged cheese appreciates in value as per the defaults
    AgedCheese(Appreciates),

    // Backstage passes appreciate in value with special rules
    BackstagePass(
        Appreciates,
        expires = true,
        adjustmentOverrides = listOf(
            QualityAdjustment(0..5, 3),
            QualityAdjustment(6..10, 2)
        )
    ),

    // Conjured items depreciate, but twice as fast as normal items
    Conjured(
        Depreciates,
        adjustmentOverrides = listOf(
            QualityAdjustment(1..Int.MAX_VALUE, 2),
            QualityAdjustment(Int.MIN_VALUE .. 0, 4)
        )
    ),

    // Legendary items do not appreciate or depreciate or change in quality
    Legendary(
        Eternal,
        adjustmentOverrides = listOf(
            QualityAdjustment(Int.MIN_VALUE..Int.MAX_VALUE, 0)
        )
    ),
}

data class QualityAdjustment(val daysUntilExpiryInclusive: IntRange, val qualityAdjustmentStep: Int)

enum class AssetClass {
    Appreciates, Depreciates, Eternal
}