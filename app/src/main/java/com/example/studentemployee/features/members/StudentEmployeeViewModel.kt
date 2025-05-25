package com.example.studentemployee.features.members

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentemployee.features.members.model.Anggota
import com.example.studentemployee.features.members.model.Divisi
import com.example.studentemployee.features.members.model.Kelompok
import com.example.studentemployee.repository.FirestoreRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.studentemployee.features.members.model.StudentEmployee

class StudentEmployeeViewModel:ViewModel() {
    private val repository = FirestoreRepository()

    private var _studentEmployeesData = MutableStateFlow<List<StudentEmployee>>(emptyList())
    val studentEmployeesData: StateFlow<List<StudentEmployee>> = _studentEmployeesData

    private val _selectedStudentEmployee = MutableStateFlow<StudentEmployee?>(null)
    val selectedStudentEmployee: StateFlow<StudentEmployee?> = _selectedStudentEmployee

    private val _selectedKelompok = MutableStateFlow<Kelompok?>(null)
    val selectedKelompok: StateFlow<Kelompok?> = _selectedKelompok

    private val _selectedAnggota = MutableStateFlow<Anggota?>(null)
    val selectedAnggota: StateFlow<Anggota?> = _selectedAnggota

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedDivisi = MutableStateFlow<Divisi?>(null)
    val selectedDivisi: StateFlow<Divisi?> = _selectedDivisi

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchAllStudentEmployees()
    }

    fun fetchAllStudentEmployees() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository.getAllStudentEmployeesData()
                .catch { e ->
                    Log.e("StudentEmployeeVM", "Error collecting student employees", e)
                    _error.value = e.localizedMessage ?: "An error occurred collecting data."
                    _studentEmployeesData.value = emptyList()
                    _isLoading.value = false
                }
                .collect { data ->
                    _studentEmployeesData.value = data
                    Log.d("StudentEmployeeVM", "Data received, count: ${data.size}")
                    _isLoading.value = false
                }
        }
    }

    fun selectDivisiById(divisiId: String?) {
        if (divisiId == null) {
            _selectedDivisi.value = null
            _selectedKelompok.value = null
            return
        }
        _selectedDivisi.value = _selectedStudentEmployee.value?.divisi?.find { it.id == divisiId }
        _selectedKelompok.value = null
        if (_selectedDivisi.value == null) {
            _error.value = "Selected Divisi (ID: $divisiId) not found in current Student Employee."
        } else {
            _error.value = null
        }
    }

    fun getStudentEmployeeById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _selectedStudentEmployee.value = null

            if (id.isEmpty()) {
                _error.value = "Student Employee ID cannot be empty."
                _isLoading.value = false
                return@launch
            }

            val result: Result<StudentEmployee?> = repository.getStudentEmployeeById(id)

            result.fold(
                onSuccess = { studentEmployee ->
                    _selectedStudentEmployee.value = studentEmployee
                    if (studentEmployee == null) {
                        _error.value = "Student Employee not found."
                        Log.d("StudentEmployeeVM", "Student Employee with ID $id not found (Result success, value null)")
                    } else {
                        Log.d("StudentEmployeeVM", "Successfully fetched SE by ID: $id, Data: $studentEmployee")
                    }
                },
                onFailure = { exception ->
                    Log.e("StudentEmployeeVM", "Error fetching SE by ID: $id", exception)
                    _error.value = exception.localizedMessage ?: "Failed to fetch details."
                    _selectedStudentEmployee.value = null
                }
            )
            _isLoading.value = false
        }
    }

    fun clearSelectedStudentEmployee() { _selectedStudentEmployee.value = null; _selectedDivisi.value = null; _selectedKelompok.value = null; _selectedAnggota.value = null; _error.value = null }
    fun clearSelectedDivisi() { _selectedDivisi.value = null; _selectedKelompok.value = null; _selectedAnggota.value = null; _error.value = null }
    fun clearSelectedKelompok() { _selectedKelompok.value = null; _selectedAnggota.value = null; _error.value = null }
    fun clearSelectedAnggota() { _selectedAnggota.value = null; _error.value = null }

    fun addStudentEmployee(
        year: String,
        batch: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val newStudentEmployee = StudentEmployee(year = year, batch = batch)
            val result = repository.addStudentEmployee(newStudentEmployee)
            result.fold(
                onSuccess = { newGeneratedId ->
                    Log.d("StudentEmployeeVM", "SE added successfully with ID: $newGeneratedId")
                    onSuccess(newGeneratedId)
                },
                onFailure = { e ->
                    Log.e("StudentEmployeeVM", "Error adding SE", e)
                    val errorMessage = e.localizedMessage ?: "Failed to add student employee."
                    _error.value = errorMessage
                    onFailure(errorMessage)
                }
            )
            _isLoading.value = false
        }
    }

    fun updateStudentEmployee(
        id: String,
        year: String,
        batch: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val studentEmployeeToUpdate = StudentEmployee(id = id, year = year, batch = batch)
            // Assuming repository.updateStudentEmployee returns Result<Unit>
            val result = repository.updateStudentEmployee(studentEmployeeToUpdate)
            result.fold(
                onSuccess = {
                    Log.d("StudentEmployeeVM", "SE updated successfully: $id")
                    // List should update automatically if using snapshot listener.
                    onSuccess()
                },
                onFailure = { e ->
                    Log.e("StudentEmployeeVM", "Error updating SE: $id", e)
                    val errorMessage = e.localizedMessage ?: "Failed to update student employee."
                    _error.value = errorMessage
                    onFailure(errorMessage)
                }
            )
            _isLoading.value = false
        }
    }

    fun deleteStudentEmployee(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            // Assuming repository.deleteStudentEmployee returns Result<Unit>
            val result = repository.deleteStudentEmployee(id)
            result.fold(
                onSuccess = {
                    Log.d("StudentEmployeeVM", "SE deleted successfully: $id")
                    // List should update automatically if using snapshot listener.
                    onSuccess()
                },
                onFailure = { e ->
                    Log.e("StudentEmployeeVM", "Error deleting SE: $id", e)
                    val errorMessage = e.localizedMessage ?: "Failed to delete student employee."
                    _error.value = errorMessage
                    onFailure(errorMessage)
                }
            )
            _isLoading.value = false
        }
    }

    // --- Divisi CRUD Methods ---

    fun getDivisiForEdit(studentEmployeeId: String, divisiId: String) {
        val studentEmployee = _studentEmployeesData.value.find { it.id == studentEmployeeId }
        // Or use _selectedStudentEmployee.value if it's guaranteed to be the correct one
        // val studentEmployee = _selectedStudentEmployee.value
        // if (studentEmployee?.id != studentEmployeeId) {
        //     _error.value = "Target Student Employee not selected or mismatch."
        //     _selectedDivisi.value = null
        //     return
        // }

        _selectedDivisi.value = studentEmployee?.divisi?.find { it.id == divisiId }
        if (_selectedDivisi.value == null) {
            _error.value = "Divisi not found for editing."
        } else {
            _error.value = null
        }
    }


    fun addDivisi(
        studentEmployeeId: String,
        divisiName: String,
        onSuccess: (newDivisiId: String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true; _error.value = null
            val result = repository.addDivisiToStudentEmployee(studentEmployeeId, divisiName)
            result.fold(
                onSuccess = { newId ->
                    // The snapshot listener in getAllStudentEmployeesData should refresh the list.
                    // If immediate UI update is needed before listener fires, or if listener is slow,
                    // you might manually update the _selectedStudentEmployee's divisi list here.
                    onSuccess(newId)
                },
                onFailure = { e ->
                    val msg = e.localizedMessage ?: "Failed to add Divisi."; _error.value = msg; onFailure(msg)
                }
            )
            _isLoading.value = false
        }
    }

    fun updateDivisi(
        studentEmployeeId: String,
        divisiToUpdate: Divisi, // Pass the whole Divisi object with its ID and new name
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true; _error.value = null
            val result = repository.updateDivisiInStudentEmployee(studentEmployeeId, divisiToUpdate)
            result.fold(
                onSuccess = {
                    // Snapshot listener should refresh.
                    onSuccess()
                },
                onFailure = { e ->
                    val msg = e.localizedMessage ?: "Failed to update Divisi."; _error.value = msg; onFailure(msg)
                }
            )
            _isLoading.value = false
        }
    }

    fun deleteDivisi(
        studentEmployeeId: String,
        divisiId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true; _error.value = null
            val result = repository.deleteDivisiFromStudentEmployee(studentEmployeeId, divisiId)
            result.fold(
                onSuccess = {
                    // Snapshot listener should refresh.
                    onSuccess()
                },
                onFailure = { e ->
                    val msg = e.localizedMessage ?: "Failed to delete Divisi."; _error.value = msg; onFailure(msg)
                }
            )
            _isLoading.value = false
        }
    }

    // --- Kelompok CRUD Methods ---

    fun getKelompokForEdit(studentEmployeeId: String, divisiId: String, kelompokId: String) {
        val studentEmployee = _studentEmployeesData.value.find { it.id == studentEmployeeId }
        // Or use _selectedStudentEmployee.value and _selectedDivisi.value if they are set
        val divisi = studentEmployee?.divisi?.find { it.id == divisiId }
        _selectedKelompok.value = divisi?.kelompok?.find { it.id == kelompokId }

        if (_selectedKelompok.value == null) {
            _error.value = "Kelompok not found for editing."
        } else {
            _error.value = null
        }
    }

    fun addKelompok(
        studentEmployeeId: String,
        divisiId: String,
        kelompokValue: Double, // The number for the kelompok
        onSuccess: (newKelompokId: String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true; _error.value = null
            val result = repository.addKelompokToDivisi(studentEmployeeId, divisiId, kelompokValue)
            result.fold(
                onSuccess = { newId ->
                    // Snapshot listener should refresh the list.
                    onSuccess(newId)
                },
                onFailure = { e ->
                    val msg = e.localizedMessage ?: "Failed to add Kelompok."; _error.value = msg; onFailure(msg)
                }
            )
            _isLoading.value = false
        }
    }

    fun updateKelompok(
        studentEmployeeId: String,
        divisiId: String,
        kelompokToUpdate: Kelompok,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true; _error.value = null
            val result = repository.updateKelompokInDivisi(studentEmployeeId, divisiId, kelompokToUpdate)
            result.fold(
                onSuccess = {
                    onSuccess()
                },
                onFailure = { e ->
                    val msg = e.localizedMessage ?: "Failed to update Kelompok."; _error.value = msg; onFailure(msg)
                }
            )
            _isLoading.value = false
        }
    }

    fun deleteKelompok(
        studentEmployeeId: String,
        divisiId: String,
        kelompokId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true; _error.value = null
            val result = repository.deleteKelompokFromDivisi(studentEmployeeId, divisiId, kelompokId)
            result.fold(
                onSuccess = {
                    onSuccess()
                },
                onFailure = { e ->
                    val msg = e.localizedMessage ?: "Failed to delete Kelompok."; _error.value = msg; onFailure(msg)
                }
            )
            _isLoading.value = false
        }
    }

    fun getAnggotaForEdit(studentEmployeeId: String, divisiId: String, kelompokId: String, anggotaId: String) {
        val studentEmployee = _studentEmployeesData.value.find { it.id == studentEmployeeId }
        val divisi = studentEmployee?.divisi?.find { it.id == divisiId }
        val kelompok = divisi?.kelompok?.find { it.id == kelompokId }
        _selectedAnggota.value = kelompok?.anggota?.find { it.id == anggotaId }

        if (_selectedAnggota.value == null) {
            _error.value = "Anggota not found for editing."
        } else {
            _error.value = null
        }
    }

    fun addAnggota(
        studentEmployeeId: String,
        divisiId: String,
        kelompokId: String,
        anggota: Anggota, // Pass Anggota object with nama & nim
        onSuccess: (newAnggotaId: String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true; _error.value = null
            val result = repository.addAnggotaToKelompok(studentEmployeeId, divisiId, kelompokId, anggota)
            result.fold(
                onSuccess = { newId -> onSuccess(newId) }, // Snapshot listener should refresh
                onFailure = { e -> val msg = e.localizedMessage ?: "Failed to add Anggota."; _error.value = msg; onFailure(msg) }
            )
            _isLoading.value = false
        }
    }

    fun updateAnggota(
        studentEmployeeId: String,
        divisiId: String,
        kelompokId: String,
        anggotaToUpdate: Anggota,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true; _error.value = null
            val result = repository.updateAnggotaInKelompok(studentEmployeeId, divisiId, kelompokId, anggotaToUpdate)
            result.fold(
                onSuccess = { onSuccess() }, // Snapshot listener should refresh
                onFailure = { e -> val msg = e.localizedMessage ?: "Failed to update Anggota."; _error.value = msg; onFailure(msg) }
            )
            _isLoading.value = false
        }
    }

    fun deleteAnggota(
        studentEmployeeId: String,
        divisiId: String,
        kelompokId: String,
        anggotaId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true; _error.value = null
            val result = repository.deleteAnggotaFromKelompok(studentEmployeeId, divisiId, kelompokId, anggotaId)
            result.fold(
                onSuccess = { onSuccess() }, // Snapshot listener should refresh
                onFailure = { e -> val msg = e.localizedMessage ?: "Failed to delete Anggota."; _error.value = msg; onFailure(msg) }
            )
            _isLoading.value = false
        }
    }

}