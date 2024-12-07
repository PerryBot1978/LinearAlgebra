package com.dtb.algebra.utils

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromHexString
import kotlinx.serialization.encodeToHexString
import java.io.File
import java.nio.file.Path

object StringUtils {
	fun String.path() = Path.of(this)
	fun String.file() = File(this)

	fun String.println() = println(this)
	fun String.writeToFile(filename: String) = filename.file().writeText(this)
	fun String.readFile(): String = File(this).readText()

	@OptIn(ExperimentalSerializationApi::class)
	fun String.cborEncode(): String = Cbor.encodeToHexString(this)
	@OptIn(ExperimentalSerializationApi::class)
	fun String.cborDecode(): String = Cbor.decodeFromHexString(this)
}