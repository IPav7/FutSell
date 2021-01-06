package com.example.futsell.ui.main

data class Response(
    val status: String,
    val message: String,
    val player: Player? = null
)