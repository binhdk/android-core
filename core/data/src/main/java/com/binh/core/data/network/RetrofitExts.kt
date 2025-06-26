package com.binh.core.data.network

import retrofit2.Retrofit

inline fun <reified T> Retrofit.createService(): T = create(T::class.java)