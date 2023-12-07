package com.dicoding.myintermediateapplication

import com.dicoding.myintermediateapplication.data.response.ListStoryItem

object DataDummy {
    val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXRCblRCMlljck5rRXM4ZXkiLCJpYXQiOjE3MDE5NjgyMjB9.K5_VqQhfa3Jqyrv8hjDxIvvd8R1-7IHSppsS_AgGW6I"
    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                "photo $i",
                name = "nama $i",
                createdAt = "created_at $i",
                description = "description $i"
            )
            items.add(quote)
        }
        return items
    }
}