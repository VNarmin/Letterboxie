package com.example.letterboxie.dependencyInjection

import android.content.Context
import android.content.SharedPreferences
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.letterboxie.data.remote.KeyInterceptor
import com.example.letterboxie.data.remote.Service
import com.example.letterboxie.utilities.Constants.BASE_URL
import com.example.letterboxie.utilities.Constants.DATE_TIME_FORMAT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirestore() : FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context : Context) : SharedPreferences {
        return context.getSharedPreferences("letterboxie", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSimpleDateFormat() : SimpleDateFormat {
        return SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
    }

    @Provides
    @Singleton
    fun provideKeyInterceptor() : KeyInterceptor {
        return KeyInterceptor()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() : HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    fun provideChuckerInterceptor(@ApplicationContext context : Context) : ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(keyInterceptor : KeyInterceptor,
                            httpLoggingInterceptor : HttpLoggingInterceptor,
                            chuckerInterceptor : ChuckerInterceptor) : OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(keyInterceptor).
        addNetworkInterceptor(httpLoggingInterceptor).
        addNetworkInterceptor(chuckerInterceptor).readTimeout(60, TimeUnit.SECONDS).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient : OkHttpClient) : Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL).
        addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
    }

    @Provides
    @Singleton
    fun provideService(retrofit : Retrofit) : Service {
        return retrofit.create(Service::class.java)
    }
}