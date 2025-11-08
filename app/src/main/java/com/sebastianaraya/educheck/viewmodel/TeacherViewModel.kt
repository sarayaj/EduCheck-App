package com.sebastianaraya.educheck.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sebastianaraya.educheck.data.local.TeacherEntity
import com.sebastianaraya.educheck.data.repository.TeacherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TeacherViewModel(private val repository: TeacherRepository) : ViewModel() {

    // 🔹 Estados observables para la UI
    private val _teachers = MutableStateFlow<List<TeacherEntity>>(emptyList())
    val teachers: StateFlow<List<TeacherEntity>> = _teachers

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _formError = MutableStateFlow<String?>(null)
    val formError: StateFlow<String?> = _formError

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    //  Cargar docentes desde la base de datos
    fun loadTeachers() {
        viewModelScope.launch {
            _isLoading.value = true
            _teachers.value = repository.getAllTeachers()
            _isLoading.value = false
        }
    }

    //  Registrar nuevo docente (con validación)
    fun registerTeacher(nombre: String, correo: String, password: String, confirmarPassword: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _formError.value = null

            val validation = validateTeacher(nombre, correo, password, confirmarPassword)
            if (validation != null) {
                _formError.value = validation
                _isLoading.value = false
                return@launch
            }

            val existing = repository.getAllTeachers().find { it.correo == correo }
            if (existing != null) {
                _formError.value = "El correo ya está registrado."
                _isLoading.value = false
                return@launch
            }

            val teacher = TeacherEntity(
                nombre = nombre,
                correo = correo,
                password = password,
                rut = ""
            )

            repository.insertTeacher(teacher)
            loadTeachers()
            _isLoading.value = false
        }
    }

    //  Login validado
    suspend fun loginUser(correo: String, password: String): TeacherEntity? {
        _isLoading.value = true
        _loginError.value = null
        val user = repository.loginUser(correo, password)
        _isLoading.value = false

        if (user == null) _loginError.value = "Credenciales incorrectas"
        return user
    }

    //  Validación de datos antes de registrar
    private fun validateTeacher(
        nombre: String,
        correo: String,
        password: String,
        confirmarPassword: String
    ): String? {
        return when {
            nombre.isBlank() -> "El nombre no puede estar vacío"
            correo.isBlank() || !correo.contains("@") -> "Correo inválido"
            password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            confirmarPassword != password -> "Las contraseñas no coinciden"
            else -> null
        }
    }

    //  Actualizar RUT del docente
    suspend fun updateRut(nombre: String, nuevoRut: String): Boolean {
        return try {
            val teacher = repository.getAllTeachers().find { it.nombre == nombre }
            if (teacher != null) {
                repository.updateTeacher(teacher.copy(rut = nuevoRut))
                true
            } else false
        } catch (e: Exception) {
            false
        }
    }

    //  Actualizar datos completos del docente
    fun updateTeacher(teacher: TeacherEntity) {
        viewModelScope.launch {
            repository.updateTeacher(teacher)
            loadTeachers()
        }
    }

    //  Eliminar todos los docentes registrados
    fun clearAllTeachers() {
        viewModelScope.launch {
            repository.deleteAllTeachers()
            _teachers.value = emptyList()
        }
    }
}

/**
  Factory para crear TeacherViewModel sin acceso directo al contexto.
 */
class TeacherViewModelFactory(private val repository: TeacherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeacherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TeacherViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}
