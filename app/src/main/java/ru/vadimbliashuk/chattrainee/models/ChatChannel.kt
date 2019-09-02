package ru.vadimbliashuk.chattrainee.models

class ChatChannel(val usersId: MutableList<String>) {
    constructor() : this(mutableListOf())
}