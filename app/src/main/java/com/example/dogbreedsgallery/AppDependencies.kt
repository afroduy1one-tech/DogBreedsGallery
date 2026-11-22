
package com.example.dogbreedsgallery

import android.content.Context
import androidx.room.Room
import com.example.dogbreedsgallery.data.datasource.LocalDataSource
import com.example.dogbreedsgallery.data.datasource.RemoteDataSource
import com.example.dogbreedsgallery.data.local.AppDatabase
import com.example.dogbreedsgallery.data.remote.RetrofitClient
import com.example.dogbreedsgallery.data.repository.BreedRepository

class AppDependencies(context: Context) {

    private val database = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "dog_breeds_database"
    ).build()

    private val breedDao = database.breedDao()

    private val localDataSource = LocalDataSource(
        breedDao = breedDao
    )

    private val remoteDataSource = RemoteDataSource(
        dogApiService = RetrofitClient.api
    )

    val breedRepository = BreedRepository(
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource
    )
}
