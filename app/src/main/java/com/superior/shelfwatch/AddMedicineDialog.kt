package com.superior.shelfwatch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddMedicineDialog(
    onSave: (Medicine) -> Unit,
    onDismiss: () -> Unit
) {
    var medicineName by remember { mutableStateOf("") }
    var quantityText by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf<String?>(null) }

    val quantity = quantityText.toIntOrNull()
    val validQuantity = quantity != null && quantity >= 0
    val canSave = medicineName.isNotBlank() && validQuantity

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Add medicine")
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = medicineName,
                    onValueChange = { medicineName = it },
                    label = { Text(text = "Medicine name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = quantityText,
                    onValueChange = { quantityText = it },
                    label = { Text(text = "Quantity") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    isError = quantityText.isNotBlank() && !validQuantity,
                    singleLine = true
                )

                if (quantityText.isNotBlank() && !validQuantity) {
                    Text(text = "Enter a whole number of 0 or more.")
                }

                Spacer(modifier = Modifier.height(16.dp))

                ExpiryDateField(
                    expiryDate = expiryDate,
                    onDateChange = { expiryDate = it }
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = canSave,
                onClick = {
                    if (quantity != null && quantity >= 0) {
                        onSave(
                            Medicine(
                                name = medicineName.trim(),
                                quantity = quantity,
                                expiryDate = expiryDate
                            )
                        )
                    }
                }
            ) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}