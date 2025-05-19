package com.app.habitmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.shared.data.HabitEntity
import com.app.shared.data.HabitRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HojeViewModel(private val repository: HabitRepository) : ViewModel() {

    private val _selectedDate = MutableStateFlow("")
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    val habits: StateFlow<List<HabitEntity>> = selectedDate
        .flatMapLatest { date -> repository.getHabitsByDate(date) }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun selectDate(date: String) {
        _selectedDate.value = date
    }
    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
        }
    }

    fun addHabit(name: String) {
        viewModelScope.launch {
            val habit = HabitEntity(
                nome = name,
                data = _selectedDate.value,
                concluido = false
            )
            repository.insertHabit(habit)
        }
    }

    fun updateHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.updateHabit(habit)
        }
    }
}

class HojeViewModelFactory(private val repository: HabitRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HojeViewModel::class.java)) {
            return HojeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
