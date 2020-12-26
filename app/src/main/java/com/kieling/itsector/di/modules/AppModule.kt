package com.kieling.itsector.di.modules

import android.content.Context
import android.content.res.Resources
import androidx.room.Room
import com.kieling.itsector.BuildConfig
import com.kieling.itsector.app.App
import com.kieling.itsector.repository.api.ApiServices
import com.kieling.itsector.repository.api.network.LiveDataCallAdapterFactoryForRetrofit
import com.kieling.itsector.repository.db.AppDatabase
import com.kieling.itsector.repository.db.BooksDao
import com.kieling.itsector.repository.db.FavoriteBookDao
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ActivityModule::class, ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideBooksService(): ApiServices = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactoryForRetrofit())
        .build()
        .create(ApiServices::class.java)

    @Singleton
    @Provides
    fun provideDb(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "book-store-db")
            .fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideBooksDao(db: AppDatabase): BooksDao = db.booksDao()

    @Singleton
    @Provides
    fun provideFavoriteBookDao(db: AppDatabase): FavoriteBookDao = db.favoriteBookDao()

    @Singleton
    @Provides
    fun provideContext(application: App): Context = application.applicationContext

    @Provides
    @Singleton
    fun providesResources(application: App): Resources = application.resources
}
