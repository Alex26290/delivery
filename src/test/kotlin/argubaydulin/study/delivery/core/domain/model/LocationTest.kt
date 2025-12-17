package argubaydulin.study.delivery.core.domain.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LocationTest {

    @ParameterizedTest
    @CsvSource(
        "1,2, true",
        "5,${Location.MAX_COORDINATE + 1}, false",
        "5,${Location.MIN_COORDINATE - 1}, false",
        "${Location.MIN_COORDINATE - 1},5, false",
        "${Location.MAX_COORDINATE + 1},5, false",
    )
    fun shouldTestLocationCreation(x: Int, y: Int, isValid: Boolean) {
        //arrange
        //act
        val result = Location.create(x, y)

        //assert
        if (isValid) {
            assertTrue(result.isSuccess)
            assertEquals(result.value.x, x)
            assertEquals(result.value.y, y)
        } else {
            assertTrue(result.isFailure)
            assertThat(result.error).isNotNull
        }
    }

    @Test
    fun shouldBeEqual() {
        //arrange
        val location1 = Location.create(3, 7).value
        val location2 = Location.create(3, 7).value

        //act
        val result = location1 == location2

        //assert
        assertTrue(result)
    }

    @Test
    fun shouldBeNotEqual() {
        //arrange
        val location1 = Location.create(3, 6).value
        val location2 = Location.create(3, 7).value

        //act
        val result = location1 == location2

        //assert
        assertFalse(result)
    }

    @ParameterizedTest
    @CsvSource(
        "1,2,1,2,0",
        "1,1,1,10,9",
        "1,1,10,10,18",
    )
    fun shouldCorrectlyCalculateDistance(x1: Int, y1: Int, x2: Int, y2: Int, expectedDistance: Int) {

        //arrange
        val location1 = Location.create(x1, y1).value
        val location2 = Location.create(x2, y2).value

        //act
        val actualDistance = location1.distanceTo(location2).value

        //assert
        assertEquals(expectedDistance, actualDistance)
    }
}