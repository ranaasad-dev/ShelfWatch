package com.superior.shelfwatch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardLayout(
    medicines: List<Medicine>,
    isSaving: Boolean,
    isUpdating: Boolean,
    errorMessage: String?,
    onAdd: () -> Unit,
    onEdit: (Medicine) -> Unit,
    onDelete: (Medicine) -> Unit,
    modifier: Modifier = Modifier
) {
    val lowStockCount = medicines.count { it.quantity < 10 }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val fontScale = LocalDensity.current.fontScale
            val compactScreen = maxHeight < 500.dp || fontScale > 1.2f

            Column(modifier = Modifier.fillMaxSize()) {
                ShelfWatchHeader()

                if (compactScreen) {
                    // Chhoti screen par logo fixed rahega aur
                    // dashboard aur list neeche scroll honge.
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 20.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item(key = "dashboard") {
                            DashboardSummary(
                                medicineCount = medicines.size,
                                lowStockCount = lowStockCount,
                                isSaving = isSaving,
                                isUpdating = isUpdating,
                                errorMessage = errorMessage,
                                onAdd = onAdd
                            )
                        }

                        medicineItems(
                            medicines = medicines,
                            onEdit = onEdit,
                            onDelete = onDelete
                        )
                    }
                } else {
                    // Normal screen par dashboard fixed rahega
                    // aur sirf medicine list scroll hogi.
                    DashboardSummary(
                        medicineCount = medicines.size,
                        lowStockCount = lowStockCount,
                        isSaving = isSaving,
                        isUpdating = isUpdating,
                        errorMessage = errorMessage,
                        onAdd = onAdd,
                        modifier = Modifier.padding(
                            horizontal = 20.dp
                        )
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            end = 20.dp,
                            top = 12.dp,
                            bottom = 20.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        medicineItems(
                            medicines = medicines,
                            onEdit = onEdit,
                            onDelete = onDelete
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShelfWatchHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "SW",
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                text = "ShelfWatch",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Pharmacy inventory",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DashboardSummary(
    medicineCount: Int,
    lowStockCount: Int,
    isSaving: Boolean,
    isUpdating: Boolean,
    errorMessage: String?,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Overview",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryCard(
                title = "Medicines",
                value = medicineCount.toString(),
                modifier = Modifier.weight(1f)
            )

            SummaryCard(
                title = "Low stock (<10)",
                value = lowStockCount.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Expiry alerts",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

                Text(text = "Expiry tracking will be added later.")
            }
        }

        Button(
            onClick = onAdd,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Add medicine")
        }

        if (isSaving) {
            Text(text = "Saving medicine...")
        }

        if (isUpdating) {
            Text(text = "Updating medicine...")
        }

        errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error
            )
        }

        Text(
            text = "Your medicines",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = value,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Text(text = title)
        }
    }
}

private fun LazyListScope.medicineItems(
    medicines: List<Medicine>,
    onEdit: (Medicine) -> Unit,
    onDelete: (Medicine) -> Unit
) {
    if (medicines.isEmpty()) {
        item(key = "empty") {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No medicines added yet",
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = "Add a medicine to see it here.")
                }
            }
        }
    } else {
        items(
            items = medicines,
            key = { medicine -> medicine.id }
        ) { medicine ->
            MedicineCard(
                medicine = medicine,
                onEdit = { onEdit(medicine) },
                onDelete = { onDelete(medicine) }
            )
        }
    }
}

@Composable
private fun MedicineCard(
    medicine: Medicine,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = medicine.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Quantity: ${medicine.quantity}")

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Expiry: ${medicine.expiryDate ?: "Not set"}")

            Row(modifier = Modifier.align(Alignment.End)) {
                TextButton(onClick = onEdit) {
                    Text(text = "Edit")
                }

                TextButton(onClick = onDelete) {
                    Text(
                        text = "Delete",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}