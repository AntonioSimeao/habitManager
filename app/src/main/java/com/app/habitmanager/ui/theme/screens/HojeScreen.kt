package com.app.habitmanager.ui.theme.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.habitmanager.ui.viewmodel.HojeViewModel
import com.app.habitmanager.util.FirstLaunchDateManager
import com.app.shared.data.HabitEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
@Composable
fun HojeScreen(viewModel: HojeViewModel) {
    val context = LocalContext.current
    val startDate = remember { FirstLaunchDateManager.getFirstLaunchDate(context) }
    val today = remember { LocalDate.now() }
    val days = remember {
        val daysToShow = 30L + (today.toEpochDay() - startDate.toEpochDay())
        (0..daysToShow).map { startDate.plusDays(it) }
    }
    var selectedDate by remember { mutableStateOf(today) }

    val displayFormatter = DateTimeFormatter.ofPattern("d 'de' MMM", Locale("pt", "BR"))
    val formatter = DateTimeFormatter.ofPattern("dd/MM", Locale("pt", "BR"))

    var showDialog by remember { mutableStateOf(false) }
    var newHabitText by remember { mutableStateOf("") }
    var habitToDelete by remember { mutableStateOf<HabitEntity?>(null) }
    var habitToEdit by remember { mutableStateOf<HabitEntity?>(null) }
    var editHabitText by remember { mutableStateOf("") }

    LaunchedEffect(selectedDate) {
        viewModel.selectDate(selectedDate.toString())
    }

    val habits by viewModel.habits.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = selectedDate.format(displayFormatter),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
            if (habits.isNotEmpty()) {
                Text(
                    text = "+",
                    fontSize = 28.sp,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { showDialog = true },
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(days) { date ->
                val isSelected = date == selectedDate
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clickable { selectedDate = date }
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(12.dp)
                ) {
                    Text(text = date.format(formatter))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (habits.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .clickable { showDialog = true }
                            .background(MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
                            .padding(horizontal = 32.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = "+ Crie um novo hábito",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Text(
                        text = "Ou em Configurações > Lista de hábitos você pode adicionar vários hábitos e escolher onde inseri-los",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }


            }
        } else {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(habits) { habit ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                    ) {
                        Checkbox(
                            checked = habit.concluido,
                            onCheckedChange = {
                                viewModel.updateHabit(habit.copy(concluido = it))
                            }
                        )
                        Text(
                            text = habit.nome,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f) // ✅ ocupa todo o espaço restante
                                .padding(end = 8.dp)
                        )

                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar hábito",
                            modifier = Modifier
                                .clickable {
                                    habitToEdit = habit
                                    editHabitText = habit.nome
                                }
                                .padding(horizontal = 4.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Excluir hábito",
                            modifier = Modifier
                                .clickable {
                                    habitToDelete = habit
                                }
                                .padding(horizontal = 4.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        if (newHabitText.isNotBlank()) {
                            viewModel.addHabit(newHabitText)
                            newHabitText = ""
                            showDialog = false
                        }
                    }) { Text("Salvar") }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancelar")
                    }
                },
                title = { Text("Novo Hábito") },
                text = {
                    OutlinedTextField(
                        value = newHabitText,
                        onValueChange = { newHabitText = it },
                        label = { Text("Digite o hábito") }
                    )
                }
            )
        }


        if (habitToDelete != null) {
            val nome = habitToDelete!!.nome
            AlertDialog(
                onDismissRequest = { habitToDelete = null },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteHabit(habitToDelete!!)
                        habitToDelete = null
                    }) {
                        Text("Sim")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { habitToDelete = null }) {
                        Text("Cancelar")
                    }
                },
                title = { Text("Excluir hábito") },
                text = { Text("Deseja realmente excluir o hábito '${Text(text = nome, fontWeight = FontWeight.Bold)}' ?") }
            )
        }
        if (habitToEdit != null) {
            AlertDialog(
                onDismissRequest = { habitToEdit = null },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.updateHabit(habitToEdit!!.copy(nome = editHabitText))
                        habitToEdit = null
                    }) {
                        Text("Salvar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { habitToEdit = null }) {
                        Text("Cancelar")
                    }
                },
                title = { Text("Editar hábito") },
                text = {
                    OutlinedTextField(
                        value = editHabitText,
                        onValueChange = { editHabitText = it },
                        label = { Text("Novo nome do hábito") }
                    )
                }
            )
        }


    }
}


