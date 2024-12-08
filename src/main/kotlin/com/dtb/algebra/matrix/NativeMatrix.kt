package com.dtb.algebra.matrix

import com.dtb.algebra.utils.ArrayUtils.stream
import java.util.logging.Logger
import java.util.stream.IntStream
import kotlin.streams.asStream

open class NativeMatrix(val width: Int, val height: Int, initializer: (Int, Int) -> Double) : AbstractMatrix() {
	private val values: Array<Double> = Array(width * height) { initializer(it % width, it / width) }
	constructor(width: Int, height: Int): this(width, height, {_, _ -> 0.0 })

	override fun width(): Int  = this.width
	override fun height(): Int = this.height

	override operator fun plus(other: Matrix): NativeMatrix {
		if (this.width() != other.width() || this.height() != other.height())
			throw IllegalArgumentException("Invalid matrix argument size for Matrix.plus(Matrix)")
		return NativeMatrix(this.width, this.height) { i, j -> this[i, j] + other[i, j] }
	}
	override operator fun minus(other: Matrix): NativeMatrix {
		if (this.width() != other.width() || this.height() != other.height())
			throw IllegalArgumentException("Invalid matrix argument size for Matrix.minus(Matrix)")
		return NativeMatrix(this.width, this.height) { i, j -> this[i, j] - other[i, j] }
	}

	override operator fun times(other: Int): NativeMatrix = this * other.toDouble()
	override operator fun div(other: Int): NativeMatrix = this / other.toDouble()

	override operator fun times(other: Double): NativeMatrix {
		return NativeMatrix(this.width(), this.height()) { i, j -> this[i, j] * other }
	}
	override operator fun div(other: Double): NativeMatrix {
		return NativeMatrix(this.width(), this.height()) { i, j -> this[i, j] / other }
	}

	private operator fun get(i: Int): Double = values[i]
	override operator fun get(i: Int, j: Int): Double {
		if (j * width() + i >= this.values.size) {
			Logger
				.getLogger("Matrix")
				.severe("matrix[$i, $j] out of bounds for size [$width, $height]")
			throw IndexOutOfBoundsException()
		}
		return values[j * width() + i]
	}


	private operator fun set(i: Int, value: Double) { values[i] = value }
	private operator fun set(i: Int, j: Int, value: Double) { values[j * width + i] = value }

	override fun clone(): NativeMatrix {
		return NativeMatrix(width, height) {
			 i, j -> this[i, j]
		}
	}
}