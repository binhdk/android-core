package com.binh.core.data.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CommonRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FileTransferRetrofit
