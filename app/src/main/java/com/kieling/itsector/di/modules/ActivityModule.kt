package com.kieling.itsector.di.modules

import com.kieling.itsector.ui.books.BooksActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module(includes = [FragmentModule::class])
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeBookListingActivity(): BooksActivity
}
