package com.sukase.core.domain.model

data class ConversationModel(
    val id: String,
    val name: String,
    val photo: String,
    val lastMessage: String,
    val dateTime: String,
    val participantList: List<ParticipantModel>,
)