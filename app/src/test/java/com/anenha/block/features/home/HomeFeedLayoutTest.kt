package com.anenha.block.features.home

import android.content.res.Configuration
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeFeedLayoutTest {

    @Test
    fun `column counts match layout tiers`() {
        assertEquals(1, HomeFeedLayout.Compact.columns)
        assertEquals(2, HomeFeedLayout.Medium.columns)
        assertEquals(3, HomeFeedLayout.Large.columns)
    }

    @Test
    fun `fromConfiguration resolves Compact for handset portrait`() {
        val phonePortraitConfig = Configuration().apply {
            smallestScreenWidthDp = 393
            orientation = Configuration.ORIENTATION_PORTRAIT
        }
        assertEquals(HomeFeedLayout.Compact, HomeFeedLayout.fromConfiguration(phonePortraitConfig))
    }

    @Test
    fun `fromConfiguration resolves Medium for handset landscape and tablet portrait`() {
        val phoneLandscapeConfig = Configuration().apply {
            smallestScreenWidthDp = 393
            orientation = Configuration.ORIENTATION_LANDSCAPE
        }
        assertEquals(HomeFeedLayout.Medium, HomeFeedLayout.fromConfiguration(phoneLandscapeConfig))

        val tabletPortraitConfig = Configuration().apply {
            smallestScreenWidthDp = 800
            orientation = Configuration.ORIENTATION_PORTRAIT
        }
        assertEquals(HomeFeedLayout.Medium, HomeFeedLayout.fromConfiguration(tabletPortraitConfig))
    }

    @Test
    fun `fromConfiguration resolves Large for tablet landscape`() {
        val tabletLandscapeConfig = Configuration().apply {
            smallestScreenWidthDp = 800
            orientation = Configuration.ORIENTATION_LANDSCAPE
        }
        assertEquals(HomeFeedLayout.Large, HomeFeedLayout.fromConfiguration(tabletLandscapeConfig))
    }
}
