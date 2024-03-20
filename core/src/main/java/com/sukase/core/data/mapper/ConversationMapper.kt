package com.sukase.core.data.mapper

import com.sukase.core.data.model.conversation.entity.ConversationEntity
import com.sukase.core.domain.model.ConversationModel
import com.sukase.core.domain.model.ParticipantModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun conversationMapper(input: ConversationEntity): ConversationModel =
    ConversationModel(
        id = input.id,
        name = input.name,
        photo = input.photo,
        lastMessage = input.lastMessage ?: "",
        dateTime = if (input.dateTime == null) "" else Instant.ofEpochMilli(input.dateTime)
            .atZone(ZoneId.systemDefault()).format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        ),
        participantList = input.participantsId.mapIndexed { index, i ->
            ParticipantModel(
                i.toString(),
                input.participantsUsername.elementAt(index),
                input.participantsFullName[index]
            )
        }
    )