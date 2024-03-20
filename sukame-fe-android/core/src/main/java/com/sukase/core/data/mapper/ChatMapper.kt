package com.sukase.core.data.mapper

import com.sukase.core.UserPreferences
import com.sukase.core.data.model.chat.entity.ChatEntity
import com.sukase.core.data.model.conversation.entity.ConversationEntity
import com.sukase.core.domain.model.ChatModel
import com.sukase.core.domain.model.ReceiverModel
import com.sukase.core.domain.model.SenderModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


fun provideSenderModel(userPref: UserPreferences): SenderModel =
    SenderModel(userPref.uid, userPref.username, userPref.token)

fun provideReceiverListModel(input: ConversationEntity): List<ReceiverModel> =
    input.participantsId.mapIndexed { index, i ->
        ReceiverModel(
            i.toString(),
            input.participantsUsername.elementAt(index),
            input.participantsFullName[index]
        )
    }

fun chatEntityToModel(input: ChatEntity): ChatModel =
    ChatModel(
        id = input.id.toString(),
        photo = input.photo,
        type = input.type,
        message = input.message,
        datetime = Instant.ofEpochMilli(input.datetime).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
        sender = SenderModel(input.senderId.toString(), input.senderUsername, input.senderFullName),
        receiverList = input.receiverId.mapIndexedNotNull { index, i ->
            ReceiverModel(
                i.toString(),
                input.receiverUsername.elementAt(index),
                input.receiverFullName[index]
            ).takeIf { i != input.senderId }
        }
    )
