package com.kieling.itsector

import com.kieling.itsector.ui.detail.DetailFragment
import org.junit.Assert.assertEquals
import org.junit.Test

class DetailFragmentUnitTest {
    private val detailFragment = DetailFragment.newInstance("")

    @Test
    fun getAuthorsNullList() {
        val authorsList = null
        val expected = ""
        val actual = detailFragment.getAuthorsToShow(authorsList)
        assertEquals(expected, actual)
    }

    @Test
    fun getAuthorsEmptyList() {
        val authorsList = emptyList<String>()
        val expected = ""
        val actual = detailFragment.getAuthorsToShow(authorsList)
        assertEquals(expected, actual)
    }

    @Test
    fun getAuthorsOneItemList() {
        val authorsList = listOf("John Smith")
        val expected = "John Smith"
        val actual = detailFragment.getAuthorsToShow(authorsList)
        assertEquals(expected, actual)
    }

    @Test
    fun getAuthorsTwoItemsList() {
        val authorsList = listOf("John Smith", "Allan Doug")
        val expected = "John Smith and Allan Doug"
        val actual = detailFragment.getAuthorsToShow(authorsList)
        assertEquals(expected, actual)
    }

    @Test
    fun getAuthorsList() {
        val authorsList =
            listOf("John Smith", "Allan Doug", "Barack Obama", "Donald Trump", "Richard Nixon")
        val expected = "John Smith, Allan Doug, Barack Obama, Donald Trump and Richard Nixon"
        val actual = detailFragment.getAuthorsToShow(authorsList)
        assertEquals(expected, actual)
    }
}
