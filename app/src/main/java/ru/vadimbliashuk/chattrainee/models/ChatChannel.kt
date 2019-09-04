package ru.vadimbliashuk.chattrainee.models

class ChatChannel(val usersIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}