package com.binh.core.testing

import com.binh.core.testing.keystore.FakeKeyStore
import org.junit.BeforeClass

open class BaseTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            FakeKeyStore.setup
        }
    }
}