package argubaydulin.study.delivery.core.domain.model.courier

import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class StoragePlaceTest {


    @Test
    fun shouldTestThat2EntitiesAreEqual() {
        val storagePlace1 =
            StoragePlace.create(
                id = UUID.randomUUID(),
                name = "рюкзак",
                totalVolume = 30,
                orderId = UUID.randomUUID()
            )
        val storagePlace2 = StoragePlace.create(
            id = UUID.randomUUID(),
            name = "багажник",
            totalVolume = 10,
            orderId = UUID.randomUUID()
        )

        assertTrue(storagePlace1.isSuccess)
        assertTrue(storagePlace2.isSuccess)
        assertEquals(storagePlace1.value.name, "рюкзак")
        assertEquals(storagePlace1.value.totalVolume, 30)
        assertEquals(storagePlace2.value.name, "багажник")
        assertEquals(storagePlace2.value.totalVolume, 10)
        assertNotEquals(storagePlace1.value, storagePlace2.value)
    }
}