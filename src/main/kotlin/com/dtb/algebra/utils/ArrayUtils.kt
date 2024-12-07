package com.dtb.algebra.utils

import java.util.stream.Stream

object ArrayUtils {
	fun <T> Array<T>.stream(): Stream<T> = this.toList().stream()
}