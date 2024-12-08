package com.dtb.algebra.matrix

import com.dtb.algebra.utils.ArrayUtils.stream
import java.util.OptionalDouble
import java.util.stream.IntStream
import kotlin.streams.asStream

abstract class AbstractMatrix: Matrix {
	override fun toString(): String {
		val out = rows()
			.asStream()
			.map {
				val out = it
					.stream()
					.map { it.toString() }
					.reduce { str1, str2 -> "$str1, $str2" }
					.orElseThrow()
				"{$out}"
			}.reduce { row1, row2 -> "$row1, $row2" }
			.orElse("{}")
		return "{$out}"
	}
	override fun hashCode(): Int {
		return this
			.rows()
			.asStream()
			.map { it.stream().map { it.hashCode() }.reduce { i, j -> i * j }.orElse(0) }
			.reduce { num1, num2 -> num1 * num2 }
			.orElse(0)
	}
	override fun equals(other: Any?): Boolean {
		if (other == null)
			return false
		if (other is Matrix) {
			return IntStream
				.range(0, this.width())
				.allMatch { i -> IntStream
					.range(0, this.height())
					.allMatch { j -> other[i, j] == this[i, j] }
				}
		}
		return false
	}

	// Because Matrix is immutable, the determinate is always the same
	// Allowing for easy memoization
	private var determinateMemo: OptionalDouble = OptionalDouble.empty()
	override fun determinate(): Double {
		if (determinateMemo.isPresent)
			return determinateMemo.asDouble

		determinateMemo = OptionalDouble.of(
			super.determinate()
		)
		return determinateMemo.asDouble
	}
}