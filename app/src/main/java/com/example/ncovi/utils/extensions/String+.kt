package com.example.ncovi.utils.extensions

fun String.formatDate() = this.replace("T", " ").replace(".000Z", "")
