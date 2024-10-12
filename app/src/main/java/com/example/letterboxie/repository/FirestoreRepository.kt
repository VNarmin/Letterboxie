package com.example.letterboxie.repository

import com.example.letterboxie.utilities.Constants.PRIMARY_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepository @Inject constructor(private val firestore : FirebaseFirestore) {
    fun <T> saveDocument(userDocumentID : String, collectionPath : String, childDocumentID : String, data : T) :
            Flow < NetworkResponse < Unit >> = flow {
        emit(NetworkResponse.Loading)
        try {
            val userReference = firestore.collection(PRIMARY_COLLECTION).document(userDocumentID)
            userReference.collection(collectionPath).document(childDocumentID).set(data!!).await()
            emit(NetworkResponse.Success(Unit))
        } catch (exception: Exception) {
            emit(NetworkResponse.Error(exception.localizedMessage ?: "Unknown Error!!"))
        }
    }.flowOn(Dispatchers.IO)

    fun <T> retrieveCollection(userDocumentID : String, collectionPath : String, clazz : Class < T >) :
            Flow < NetworkResponse < List < T >>> = flow {
        emit(NetworkResponse.Loading)
        try {
            val userReference = firestore.collection(PRIMARY_COLLECTION).document(userDocumentID)
            val snapshot = userReference.collection(collectionPath).get().await()
            val data = snapshot.mapNotNull { data -> data.toObject(clazz) }
            emit(NetworkResponse.Success(data))
        } catch (exception : Exception) {
            emit(NetworkResponse.Error(exception.localizedMessage ?: "Unknown Error!!"))
        }
    }.flowOn(Dispatchers.IO)

    fun <T> retrieveDocument(userDocumentID : String, collectionPath : String, childDocumentID : String,
                             clazz : Class < T >) : Flow < NetworkResponse < T >> = flow {
        emit(NetworkResponse.Loading)
        try {
            val userReference = firestore.collection(PRIMARY_COLLECTION).document(userDocumentID)
            val documentReference = userReference.collection(collectionPath).document(childDocumentID)
            val snapshot = documentReference.get().await()
            if (snapshot.exists()) {
                val data = snapshot.toObject(clazz)
                data?.let { success ->
                    emit(NetworkResponse.Success(success))
                } ?: run { emit(NetworkResponse.Error("Data is null!!")) }
            } else emit(NetworkResponse.Error("Document does not exist!!"))
        } catch (exception : Exception) {
            emit(NetworkResponse.Error(exception.localizedMessage ?: "Unknown Error!!"))
        }
    }

    fun deleteDocument(userDocumentID : String, collectionPath : String, childDocumentID : String) :
            Flow < NetworkResponse < Unit >> = flow {
        emit(NetworkResponse.Loading)
        try {
            val userReference = firestore.collection(PRIMARY_COLLECTION).document(userDocumentID)
            val documentReference = userReference.collection(collectionPath).document(childDocumentID)
            documentReference.delete().await()
            emit(NetworkResponse.Success(Unit))
        } catch (exception : Exception) {
            emit(NetworkResponse.Error(exception.localizedMessage ?: "Unknown Error!!"))
        }
    }
}