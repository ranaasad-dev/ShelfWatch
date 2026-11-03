package com.superior.shelfwatch

import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@Composable
fun ShelfWatchHome(modifier: Modifier = Modifier) {
    val appContext = LocalContext.current.applicationContext

    val medicineDao = remember(appContext) {
        MedicineDatabase.getDatabase(appContext).medicineDao()
    }

    val medicinesFlow = remember(medicineDao) {
        medicineDao.getAllMedicines()
    }

    val medicines by medicinesFlow.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    var showAddMedicine by remember { mutableStateOf(false) }
    var medicineToEdit by remember { mutableStateOf<Medicine?>(null) }
    var medicineToDelete by remember { mutableStateOf<Medicine?>(null) }

    var isSaving by remember { mutableStateOf(false) }
    var isUpdating by remember { mutableStateOf(false) }
    var isDeleting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    if (showAddMedicine) {
        AddMedicineDialog(
            onSave = { medicine ->
                if (!isSaving) {
                    isSaving = true
                    errorMessage = null

                    scope.launch {
                        try {
                            medicineDao.insertMedicine(medicine)
                            showAddMedicine = false
                        } catch (exception: CancellationException) {
                            throw exception
                        } catch (exception: Exception) {
                            Log.e(
                                "ShelfWatch",
                                "Failed to save medicine",
                                exception
                            )
                            showAddMedicine = false
                            errorMessage =
                                "Medicine could not be saved. Please try again."
                        } finally {
                            isSaving = false
                        }
                    }
                }
            },
            onDismiss = {
                if (!isSaving) {
                    showAddMedicine = false
                }
            }
        )
    }

    medicineToEdit?.let { selectedMedicine ->
        EditMedicineDialog(
            medicine = selectedMedicine,
            onSave = { updatedMedicine ->
                if (!isUpdating) {
                    isUpdating = true
                    errorMessage = null

                    scope.launch {
                        try {
                            medicineDao.updateMedicine(updatedMedicine)
                            medicineToEdit = null
                        } catch (exception: CancellationException) {
                            throw exception
                        } catch (exception: Exception) {
                            Log.e(
                                "ShelfWatch",
                                "Failed to update medicine",
                                exception
                            )
                            medicineToEdit = null
                            errorMessage =
                                "Medicine could not be updated. Please try again."
                        } finally {
                            isUpdating = false
                        }
                    }
                }
            },
            onDismiss = {
                if (!isUpdating) {
                    medicineToEdit = null
                }
            }
        )
    }

    medicineToDelete?.let { selectedMedicine ->
        AlertDialog(
            onDismissRequest = {
                if (!isDeleting) {
                    medicineToDelete = null
                }
            },
            title = {
                Text(text = "Delete medicine?")
            },
            text = {
                Text(
                    text = "Delete ${selectedMedicine.name}? " +
                            "This action cannot be undone."
                )
            },
            confirmButton = {
                TextButton(
                    enabled = !isDeleting,
                    onClick = {
                        if (!isDeleting) {
                            isDeleting = true
                            errorMessage = null

                            scope.launch {
                                try {
                                    medicineDao.deleteMedicine(
                                        selectedMedicine
                                    )
                                    medicineToDelete = null
                                } catch (exception: CancellationException) {
                                    throw exception
                                } catch (exception: Exception) {
                                    Log.e(
                                        "ShelfWatch",
                                        "Failed to delete medicine",
                                        exception
                                    )
                                    medicineToDelete = null
                                    errorMessage =
                                        "Medicine could not be deleted. " +
                                                "Please try again."
                                } finally {
                                    isDeleting = false
                                }
                            }
                        }
                    }
                ) {
                    Text(
                        text = if (isDeleting) "Deleting..." else "Delete",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !isDeleting,
                    onClick = {
                        medicineToDelete = null
                    }
                ) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    DashboardLayout(
        medicines = medicines,
        isSaving = isSaving,
        isUpdating = isUpdating,
        errorMessage = errorMessage,
        onAdd = {
            errorMessage = null
            showAddMedicine = true
        },
        onEdit = { medicine ->
            errorMessage = null
            medicineToEdit = medicine
        },
        onDelete = { medicine ->
            errorMessage = null
            medicineToDelete = medicine
        },
        modifier = modifier
    )
}