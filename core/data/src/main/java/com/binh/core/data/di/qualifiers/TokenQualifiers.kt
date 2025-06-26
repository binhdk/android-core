package com.binh.core.data.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AccessTokenQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RefreshTokenQualifier

