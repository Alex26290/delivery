package argubaydulin.study.delivery.core.domain.model.courier

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class StoragePlaceTest {

    @Test
    fun shouldTestThatTwoEntitiesAreEqualById() {
        //arrange
        //act
        val id = UUID.randomUUID()
        val storagePlaceResult1 =
            StoragePlace.create(
                id = id,
                name = "рюкзак",
                totalVolume = 30,
                orderId = null
            )
        val storagePlaceResult2 = StoragePlace.create(
            id = id,
            name = "рюкзак",
            totalVolume = 30,
            orderId = null
        )

        //assert
        assertTrue(storagePlaceResult1.isSuccess)
        assertTrue(storagePlaceResult2.isSuccess)
        assertEquals(storagePlaceResult1.value, storagePlaceResult2.value)
    }

    @Test
    fun shouldTestThatTwoEntitiesAreNotEqual() {
        //arrange
        //act
        val storagePlaceResult1 =
            StoragePlace.create(
                id = UUID.randomUUID(),
                name = "рюкзак",
                totalVolume = 30,
                orderId = UUID.randomUUID()
            )
        val storagePlaceResult2 = StoragePlace.create(
            id = UUID.randomUUID(),
            name = "багажник",
            totalVolume = 10,
            orderId = UUID.randomUUID()
        )

        //assert
        assertTrue(storagePlaceResult1.isSuccess)
        assertTrue(storagePlaceResult2.isSuccess)
        assertEquals(storagePlaceResult1.value.name, "рюкзак")
        assertEquals(storagePlaceResult1.value.totalVolume, 30)
        assertEquals(storagePlaceResult2.value.name, "багажник")
        assertEquals(storagePlaceResult2.value.totalVolume, 10)
        assertNotEquals(storagePlaceResult1.value, storagePlaceResult2.value)
    }

    @Test
    fun shouldFailOnCreateWhenTotalVolumeIsZero() {
        //arrange
        //act
        val storagePlace = StoragePlace
            .create(
                id = UUID.randomUUID(),
                name = "рюкзак",
                totalVolume = 0,
                orderId = UUID.randomUUID()
            )

        //assert
        assertTrue(storagePlace.isFailure)
        assertThat(storagePlace.error).isNotNull
    }

    @Test
    fun shouldFailOnCreateWhenNameIsBlank() {
        //arrange
        //act
        val storagePlace = StoragePlace
            .create(
                id = UUID.randomUUID(),
                name = "",
                totalVolume = 10,
                orderId = UUID.randomUUID()
            )

        //assert
        assertTrue(storagePlace.isFailure)
        assertThat(storagePlace.error).isNotNull
    }

    @ParameterizedTest
    @CsvSource(
        "10, 20, true",
        "20, 20, true",
        "30, 20, false",
    )
    fun shouldTestCanStoreWhenStoragePlaceIsEmpty(volume: Int, totalVolume: Int, canStore: Boolean) {
        //arrange
        val storagePlaceResult = StoragePlace
            .create(id = UUID.randomUUID(), name = "рюкзак", totalVolume = totalVolume)

        //act
        val actual = storagePlaceResult
            .value
            .canStore(volume).isSuccess

        //assert
        assertEquals(canStore, actual)
    }

    @Test
    fun shouldTestCanStoreWhenStoragePlaceIsOccupied() {
        //arrange
        val volume = 10
        val storagePlaceResult = StoragePlace
            .create(
                id = UUID.randomUUID(),
                name = "рюкзак",
                totalVolume = 20,
                orderId = UUID.randomUUID()
            )

        //act
        val canStore = storagePlaceResult
            .value
            .canStore(volume)

        //assert
        assertTrue(canStore.isFailure)
    }


    @Test
    fun shouldStoreCorrectOrderWithCorrectOrderId() {
        //arrange
        val storagePlace = StoragePlace
            .create(
                id = UUID.randomUUID(),
                name = "рюкзак",
                totalVolume = 20,
                orderId = null
            )
        val orderId = UUID.randomUUID()

        //act
        val result = storagePlace.value.store(orderId, 15)

        //assert
        assertTrue(result.isSuccess)
        assertEquals(orderId, storagePlace.value.orderId)
    }


    @Test
    fun shouldFailWhenTryToStoreOrderWhenStorageIsAlreadyOccupied() {
        //arrange
        val orderId = UUID.randomUUID()
        val storagePlace = StoragePlace
            .create(
                id = UUID.randomUUID(),
                name = "рюкзак",
                totalVolume = 20,
                orderId = orderId
            )
        val newOrderId = UUID.randomUUID()

        //act
        val result = storagePlace.value.store(newOrderId, 15)

        //assert
        assertTrue(result.isFailure)
        assertEquals(orderId, storagePlace.value.orderId)
    }

    @Test
    fun shouldClearStoragePlace() {
        //arrange
        val orderId = UUID.randomUUID()
        val storagePlaceResult = StoragePlace
            .create(
                id = UUID.randomUUID(),
                name = "рюкзак",
                totalVolume = 20,
                orderId = orderId
            )

        val storagePlace = storagePlaceResult.value

        //act
        storagePlace.clear(orderId)

        //assert
        assertTrue(storagePlaceResult.isSuccess)
        assertNull(storagePlace.orderId)
    }
}