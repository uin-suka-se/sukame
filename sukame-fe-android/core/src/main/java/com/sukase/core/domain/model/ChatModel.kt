package com.sukase.core.domain.model

data class ChatModel(
    val id: String,
    val photo: String,
    val type: String,
    val message: String,
    val datetime: String,
    val sender: SenderModel,
    val receiverList: List<ReceiverModel>,
)