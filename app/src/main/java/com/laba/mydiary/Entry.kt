package com.laba.mydiary

class Entry(
    val id: Long,
    val title: String,
    val date: String,
    val text: String,
    val images: ArrayList<String>?
) {
}