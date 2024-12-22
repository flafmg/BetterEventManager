package me.flafmg.bem.util

import org.bukkit.Bukkit

const val PREFIX = "[BEM]"
const val RESET = "\u001B[0m"
const val RED = "\u001B[31m"
const val YELLOW = "\u001B[33m"
const val BLUE = "\u001B[34m"
const val GREEN = "\u001B[32m"

fun errLog(message: String) {
    val logMessage = "$PREFIX ${RED}✖ - $message$RESET";
    println(logMessage);
}

fun warnLog(message: String) {
    val logMessage = "$PREFIX ${YELLOW}⚠ - $message$RESET";
    println(logMessage);
}

fun infoLog(message: String) {
    val logMessage = "$PREFIX ${BLUE}ℹ - $message$RESET";
    println(logMessage);
}

fun successLog(message: String) {
    val logMessage = "$PREFIX ${GREEN}✔ - $message$RESET";
    println(logMessage);
}

fun blankLog(message: String) {
    val logMessage = "$PREFIX - $message";
    println(logMessage);
}
