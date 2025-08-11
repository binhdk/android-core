package com.binh.core.data.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CommonHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FileTransferHttpClient
