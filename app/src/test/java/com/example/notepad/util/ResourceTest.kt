package com.example.notepad.util

import org.junit.Assert.*
import org.junit.Test

class ResourceTest {

    // ─── Type checks ──────────────────────────────────────────────────────────

    @Test
    fun `Success isSuccess returns true`() {
        assertTrue(Resource.Success("data").isSuccess)
    }

    @Test
    fun `Success isError returns false`() {
        assertFalse(Resource.Success("data").isError)
    }

    @Test
    fun `Success isLoading returns false`() {
        assertFalse(Resource.Success("data").isLoading)
    }

    @Test
    fun `Error isError returns true`() {
        assertTrue(Resource.Error("oops").isError)
    }

    @Test
    fun `Error isSuccess returns false`() {
        assertFalse(Resource.Error("oops").isSuccess)
    }

    @Test
    fun `Loading isLoading returns true`() {
        assertTrue(Resource.Loading.isLoading)
    }

    @Test
    fun `Loading isSuccess returns false`() {
        assertFalse(Resource.Loading.isSuccess)
    }

    // ─── Helper functions ─────────────────────────────────────────────────────

    @Test
    fun `Success getOrNull returns data`() {
        val result = Resource.Success(42)
        assertEquals(42, result.getOrNull())
    }

    @Test
    fun `Error getOrNull returns null`() {
        assertNull(Resource.Error("fail").getOrNull())
    }

    @Test
    fun `Loading getOrNull returns null`() {
        assertNull(Resource.Loading.getOrNull())
    }

    @Test
    fun `Error errorMessageOrNull returns message`() {
        val result = Resource.Error("something went wrong")
        assertEquals("something went wrong", result.errorMessageOrNull())
    }

    @Test
    fun `Success errorMessageOrNull returns null`() {
        assertNull(Resource.Success("ok").errorMessageOrNull())
    }

    @Test
    fun `Loading errorMessageOrNull returns null`() {
        assertNull(Resource.Loading.errorMessageOrNull())
    }

    // ─── Throwable ────────────────────────────────────────────────────────────

    @Test
    fun `Error stores throwable when provided`() {
        val ex = RuntimeException("boom")
        val result = Resource.Error("boom", ex)
        assertEquals(ex, result.throwable)
    }

    @Test
    fun `Error throwable is null by default`() {
        assertNull(Resource.Error("no throwable").throwable)
    }

    // ─── Generic types ────────────────────────────────────────────────────────

    @Test
    fun `Success works with Unit type`() {
        val result: Resource<Unit> = Resource.Success(Unit)
        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `Success works with list type`() {
        val result = Resource.Success(listOf(1, 2, 3))
        assertEquals(3, result.getOrNull()?.size)
    }
}
