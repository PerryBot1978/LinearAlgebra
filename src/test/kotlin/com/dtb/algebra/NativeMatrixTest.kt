package com.dtb.algebra

import com.dtb.algebra.matrix.NativeMatrix
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

import com.dtb.algebra.matrix.Matrix.Companion.times
import kotlin.random.Random

object NativeMatrixTest {
	@Test
	fun constructor1() {
		for (i in 1..<20) {
			for (j in 1..<20) {
				val matrix = NativeMatrix(i, j)
				assertEquals(matrix.width, i)
				assertEquals(matrix.height, j)

				for (i2 in 0..<i)
					for (j2 in 0..<j)
						assertEquals(matrix[i2, j2], 0.0)
			}
		}

		NativeMatrix(1000, 1000)
	}
	@Test
	fun constructor2() {
		for (i in 1..<20)
			for (j in 1..<20)
				for (k in 1..<5) {
					val matrix = NativeMatrix(i, j) { i, j -> (i * j * k).toDouble() }
					assertEquals(matrix.width, i)
					assertEquals(matrix.height, j)

					for (i2 in 0..<i)
						for (j2 in 0..<j)
							assertEquals(matrix[i2, j2], (i2 * j2 * k).toDouble())
				}

		NativeMatrix(1000, 1000) { i, j -> (i + j).toDouble() }
	}

	@Test
	fun plus() {
		val matrix1 = NativeMatrix(3, 3) { _, _ -> 1.0 }
		val matrix2 = NativeMatrix(3, 3) { _, _ -> 2.0 }
		val sum     = NativeMatrix(3, 3) { _, _ -> 3.0 }
		assertEquals(sum, matrix1 + matrix2)

		val matrix3 = NativeMatrix(3, 2) { _, _ -> 3.0 }
		assertThrows<IllegalArgumentException> { matrix1 + matrix3 }
		assertThrows<IllegalArgumentException> { matrix3 + matrix2 }
	}

	@Test
	fun minus() {
		val matrix1 = NativeMatrix(3, 3) { _, _ -> 1.0 }
		val matrix2 = NativeMatrix(3, 3) { _, _ -> 2.0 }
		val diff1   = NativeMatrix(3, 3) { _, _ -> -1.0 }
		val diff2   = NativeMatrix(3, 3) { _, _ -> 1.0 }
		assertEquals(diff1, matrix1 - matrix2)
		assertEquals(diff2, matrix2 - matrix1)

		val matrix3 = NativeMatrix(3, 2) { _, _ -> 3.0 }
		assertThrows<IllegalArgumentException> { matrix1 - matrix3 }
		assertThrows<IllegalArgumentException> { matrix3 - matrix2 }
	}

	@Test
	fun scalarTimes() {
		val matrix = NativeMatrix(3, 3) { _, _ -> 1.0 }

		val double = NativeMatrix(3, 3) { _, _ -> 2.0 }
		assertEquals(double, matrix * 2)
		assertEquals(double, 2 * matrix)

		val triple = NativeMatrix(3, 3) { _, _ -> 3.0 }
		assertEquals(triple, matrix * 3)
		assertEquals(triple, 3 * matrix)

		val negative = NativeMatrix(3, 3) { _, _ -> -1.0 }
		assertEquals(negative, matrix * -1)
		assertEquals(negative, -1 * matrix)
	}
	@Test
	fun div() {
		val matrix = NativeMatrix(3, 3) { _, _ -> 1.0 }

		val half = NativeMatrix(3, 3) { _, _ -> 0.5 }
		assertEquals(half, matrix / 2)

		val quarter = NativeMatrix(3, 3) { _, _ -> 0.25 }
		assertEquals(quarter, matrix / 4)

		val negative = NativeMatrix(3, 3) { _, _ -> -1.0 }
		assertEquals(negative, matrix / -1)
	}

	@Test
	fun matrixTimes() {

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