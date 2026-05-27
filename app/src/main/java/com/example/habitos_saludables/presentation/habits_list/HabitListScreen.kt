package com.example.habitos_saludables.presentation.habits_list

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.habitos_saludables.domain.model.Habit
import com.example.habitos_saludables.presentation.motivation.QuoteComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(viewModel: HabitViewModel) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var habitToEdit by remember { mutableStateOf<Habit?>(null) }
    var textInput by remember { mutableStateOf("") }

    val celesteFondo = Color(0xFFE3F2FD)
    val azulOscuro = Color(0xFF0D47A1)

    val sugerenciasHabitos = listOf(
        "💧 Tomar Agua", "🚶 Caminar 30 min", "🍏 Comer Fruta",
        "📚 Leer 15 min", "🧘 Meditar 5 min", "😴 Dormir 8 horas", "🏋️ Hacer Ejercicio"
    )

    // Filtramos las sugerencias: Solo muestra las que NO están en la lista actual
    val sugerenciasDisponibles = sugerenciasHabitos.filter { sugerencia ->
        state.habits.none { it.name.equals(sugerencia, ignoreCase = true) }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { textInput = ""; showDialog = true },
                containerColor = azulOscuro,
                contentColor = Color.White
            ) { Icon(Icons.Default.Add, contentDescription = "Agregar") }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(celesteFondo)
                .padding(padding)
                .padding(16.dp)
        ) {
            QuoteComponent(state.motivationalQuote, state.motivationalAuthor, state.temperature)

            Text("Mis Hábitos Saludables", style = MaterialTheme.typography.titleLarge, color = azulOscuro)
            Spacer(modifier = Modifier.height(12.dp))

            // Solo muestra la sección de sugerencias si queda al menos una disponible
            if (sugerenciasDisponibles.isNotEmpty()) {
                Text("Sugerencias rápidas:", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(sugerenciasDisponibles) { sugerencia ->
                        Button(
                            onClick = { viewModel.onAddHabit(sugerencia) },
                            colors = ButtonDefaults.buttonColors(containerColor = azulOscuro),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(sugerencia, color = Color.White, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(state.habits) { habit ->
                    HabitItem(
                        habit = habit,
                        onToggle = { viewModel.onToggleHabit(habit) },
                        onDelete = { viewModel.onDeleteHabit(habit) },
                        onEdit = {
                            habitToEdit = habit
                            textInput = habit.name
                            showDialog = true
                        }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false; habitToEdit = null },
            title = { Text(if (habitToEdit == null) "Nuevo Hábito" else "Editar Hábito") },
            text = {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    label = { Text("Nombre del hábito") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val cleanText = textInput.trim()
                        if (cleanText.isNotBlank()) {
                            // Valida si ya existe antes de cerrar el diálogo
                            val exists = state.habits.any {
                                it.name.equals(cleanText, ignoreCase = true) && it.id != habitToEdit?.id
                            }

                            if (exists) {
                                Toast.makeText(context, "¡Este hábito ya está en tu lista!", Toast.LENGTH_SHORT).show()
                            } else {
                                if (habitToEdit == null) viewModel.onAddHabit(cleanText)
                                else viewModel.onEditHabit(habitToEdit!!, cleanText)
                                showDialog = false
                                habitToEdit = null
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = azulOscuro)
                ) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false; habitToEdit = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun HabitItem(habit: Habit, onToggle: () -> Unit, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = habit.isCompleted, onCheckedChange = { onToggle() })

            Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                Text(habit.name, style = MaterialTheme.typography.bodyLarge)
                Text("🔥 Racha: ${habit.streakDays} días", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }

            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Gray) }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red) }
        }
    }
}