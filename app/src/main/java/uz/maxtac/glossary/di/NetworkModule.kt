package uz.maxtac.glossary.di

import android.content.Context
import android.content.SharedPreferences


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.maxtac.glossary.utils.Constants.LOCAL_SHARED_PREF
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(LOCAL_SHARED_PREF, Context.MODE_PRIVATE)
    }
//    @Singleton
//    @Provides
//    fun provideProductRepository(dataBase: FirebaseFirestore): ProductsRepository {
//        return ProductsRepositoryImpl(dataBase)
//    }
//
//    @Singleton
//    @Provides
//    fun provideSearchRepossitory(ktorClient: HttpClient): SearchRepository {
//        return SearchRepositoryImpl(ktorClient)
//    }
}