package com.kieling.itsector.di.modules

import com.kieling.itsector.ui.books.BooksFragment
import com.kieling.itsector.ui.detail.DetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeBooksFragment(): BooksFragment

    @ContributesAndroidInjector
    internal abstract fun contributeDetailFragment(): DetailFragment
}
