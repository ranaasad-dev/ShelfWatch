package com.superior.shelfwatch

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate

@Composable
fun ExpiryDateField(
    expiryDate: String?,
    onDateChange: (String?) -> Unit
) {
    val context = LocalContext.current

    Column {
        Text(text = "Expiry date (optional)")

        Text(
            text = expiryDate ?: "No date selected"
        )

        Row {
            OutlinedButton(
                onClick = {
                    val initialDate = expiryDate
                        ?.let { savedDate ->
                            runCatching {
                                LocalDate.parse(savedDate)
                            }.getOrNull()
                        }
                        ?: LocalDate.now()

                    DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            val selectedDate = LocalDate.of(
                                year,
                                month + 1,
                                day
                            )
                            onDateChange(selectedDate.toString())
                        },
                        initialDate.year,
                        initialDate.monthValue - 1,
                        initialDate.dayOfMonth
                    ).show()
                }
            ) {
                Text(text = "Choose date")
            }

            if (expiryDate != null) {
                TextButton(
                    onClick = {
                        onDateChange(null)
                    }
                ) {
                    Text(text = "Clear")
                }
            }
        }
    }
}