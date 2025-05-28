package com.rma.lolytics.ui.core.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rma.lolytics.data.repository.firestore.FirestoreRepository
import kotlinx.coroutines.launch

class ProfileSettingsViewModel(
    private val firestoreRepository: FirestoreRepository,
): ViewModel() {

    internal fun updateProfilePicture(uri: Uri) {
        viewModelScope.launch {
            firestoreRepository.updateProfilePicture(
                imageUri = uri
            )
        }
    }
}