package com.dtb.algebra

import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.cbor.Cbor
import java.io.File
import java.nio.file.Path

fun String.path() = Path.of(this)
fun String.file() = File(this)

fun String.println() = println(this)
fun String.writeToFile(filename: String) = filename.file().writeText(this)
fun String.readFile(): String = File(this).readText()

@OptIn(ExperimentalSerializationApi::class)
fun String.cborEncode(): String = Cbor.encodeToHexString(this)
@OptIn(ExperimentalSerializationApi::class)
fun String.cborDecode(): String = Cbor.decodeFromHexString(this)

fun main(args: Array<String>): Unit = runBlocking {
    "Hello World!".cborEncode().writeToFile("temp.txt")
    "temp.txt".readFile().cborDecode().println()
}