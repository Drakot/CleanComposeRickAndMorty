package com.aluengo.cleancomposerickandmorty.core.data

import com.aluengo.cleancomposerickandmorty.core.domain.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val api: ApiService) : Repository {

}