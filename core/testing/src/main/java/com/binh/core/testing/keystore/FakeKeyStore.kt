package com.binh.core.testing.keystore

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

object FakeKeyStore {

    val setup by lazy {
        Security.removeProvider("AndroidKeyStore")
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.removeProvider("AndroidOpenSSL")

        Security.addProvider(AndroidKeyStoreProvider())
        Security.addProvider(BouncyCastleProvider())
        Security.addProvider(AndroidOpenSSLProvider())
    }
}
