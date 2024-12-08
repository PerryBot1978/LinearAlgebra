package com.dtb.algebra

import com.dtb.algebra.matrix.Matrix
import com.dtb.algebra.matrix.NativeMatrix
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

import com.dtb.algebra.matrix.Matrix.Companion.times
import com.dtb.algebra.utils.AnyUtils.println
import kotlin.random.Random

object NativeMatrixTest {
	@Test
	fun constructor1() {
		for (i in 1..<20) {
			for (j in 1..<20) {
				val matrix = Matrix.new(i, j)
				assertEquals(matrix.width(), i)
				assertEquals(matrix.height(), j)

				for (i2 in 0..<i)
					for (j2 in 0..<j)
						assertEquals(matrix[i2, j2], 0.0)
			}
		}

		Matrix.new(1000, 1000)
	}
	@Test
	fun constructor2() {
		for (i in 1..<20)
			for (j in 1..<20)
				for (k in 1..<5) {
					val matrix = Matrix.new(i, j) { i, j -> (i * j * k).toDouble() }
					assertEquals(matrix.width(), i)
					assertEquals(matrix.height(), j)

					for (i2 in 0..<i)
						for (j2 in 0..<j)
							assertEquals(matrix[i2, j2], (i2 * j2 * k).toDouble())
				}

		Matrix.new(1000, 1000) { i, j -> (i + j).toDouble() }
	}

	@Test
	fun plus() {
		val matrix1 = Matrix.new(3, 3) { _, _ -> 1.0 }
		val matrix2 = Matrix.new(3, 3) { _, _ -> 2.0 }
		val sum     = Matrix.new(3, 3) { _, _ -> 3.0 }
		assertEquals(sum, matrix1 + matrix2)

		val matrix3 = Matrix.new(3, 2) { _, _ -> 3.0 }
		assertThrows<IllegalArgumentException> { matrix1 + matrix3 }
		assertThrows<IllegalArgumentException> { matrix3 + matrix2 }
	}

	@Test
	fun minus() {
		val matrix1 = Matrix.new(3, 3) { _, _ -> 1.0 }
		val matrix2 = Matrix.new(3, 3) { _, _ -> 2.0 }
		val diff1   = Matrix.new(3, 3) { _, _ -> -1.0 }
		val diff2   = Matrix.new(3, 3) { _, _ -> 1.0 }
		assertEquals(diff1, matrix1 - matrix2)
		assertEquals(diff2, matrix2 - matrix1)

		val matrix3 = Matrix.new(3, 2) { _, _ -> 3.0 }
		assertThrows<IllegalArgumentException> { matrix1 - matrix3 }
		assertThrows<IllegalArgumentException> { matrix3 - matrix2 }
	}

	@Test
	fun scalarTimes() {
		val matrix = Matrix.new(3, 3) { _, _ -> 1.0 }

		val double = Matrix.new(3, 3) { _, _ -> 2.0 }
		assertEquals(double, matrix * 2)
		assertEquals(double, 2 * matrix)

		val triple = Matrix.new(3, 3) { _, _ -> 3.0 }
		assertEquals(triple, matrix * 3)
		assertEquals(triple, 3 * matrix)

		val negative = Matrix.new(3, 3) { _, _ -> -1.0 }
		assertEquals(negative, matrix * -1)
		assertEquals(negative, -1 * matrix)
	}
	@Test
	fun div() {
		val matrix = Matrix.new(3, 3) { _, _ -> 1.0 }

		val half = Matrix.new(3, 3) { _, _ -> 0.5 }
		assertEquals(half, matrix / 2)

		val quarter = Matrix.new(3, 3) { _, _ -> 0.25 }
		assertEquals(quarter, matrix / 4)

		val negative = Matrix.new(3, 3) { _, _ -> -1.0 }
		assertEquals(negative, matrix / -1)
	}

	@Test
	fun matrixTimes() {
		val matrix1 = Matrix.new(2, 2) { i, j -> i - j + 2.0 }
		val square1 = Matrix.of("7,12;4,7")
		assertEquals(square1, matrix1 * matrix1)

		val matrix2 = Matrix.new(2, 2) { i, j -> i * j + 1.0 }
		val product1 = Matrix.of("5,8;3,5")
		assertEquals(product1, matrix1 * matrix2)

		val matrix3 = Matrix.new(3, 2) { i, j -> i + j + 1.0 }
		val matrix4 = Matrix.new(2, 3) { i, j -> i + j + 1.0 }
		val product2 = Matrix.of("14,20;20,29")
		assertEquals(product2, matrix3 * matrix4)
	}

	@Test
	fun hashcode() {
		for (i in 1..<20)
			for (j in 1..<20) {
				val matrix = NativeMatrix(i, j) { _, _ -> Random.nextDouble() }
				assertEquals(matrix.hashCode(), matrix.hashCode())
			}
	}
}