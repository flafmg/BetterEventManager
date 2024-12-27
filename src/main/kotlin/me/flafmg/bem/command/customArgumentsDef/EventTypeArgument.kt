package me.flafmg.bem.command

import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentInfo
import dev.jorel.commandapi.arguments.StringArgument
import me.flafmg.bem.manager.EventType

fun eventTypeArgument(nodeName: String): CustomArgument<EventType, String> {
    return CustomArgument(StringArgument(nodeName)) { info: CustomArgumentInfo<String> ->
        try {
            EventType.valueOf(info.input.uppercase())
        } catch (e: IllegalArgumentException) {
            throw CustomArgument.CustomArgumentException.fromMessageBuilder(
                CustomArgument.MessageBuilder("Unknow event type: ").appendArgInput()
            )
        }
    }.replaceSuggestions(ArgumentSuggestions.strings { _ ->
        EventType.values().map { it.name.lowercase() }.toTypedArray()
    }) as CustomArgument<EventType, String>
}