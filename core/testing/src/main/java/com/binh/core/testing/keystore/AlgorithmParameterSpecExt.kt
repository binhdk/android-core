package com.binh.core.testing.keystore

import java.security.spec.AlgorithmParameterSpec

internal val AlgorithmParameterSpec.keystoreAlias: String
    get() = this::class.java.getDeclaredMethod("getKeystoreAlias").invoke(this) as String
