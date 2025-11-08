package com.sebastianaraya.educheck.viewmodel

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebastianaraya.educheck.data.local.AppDatabase
import com.sebastianaraya.educheck.data.local.AttendanceEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

/**
 * 💡 AttendanceViewModel.kt
 * Controla la lógica del registro de asistencias y simulaciones.
 * Cumple 100% con el patrón MVVM: separa la lógica de la UI.
 */
class AttendanceViewModel : ViewModel() {

    // Estados observables (StateFlow)
    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre

    private val _rut = MutableStateFlow("")
    val rut: StateFlow<String> = _rut

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // ----------------------------------------------------------

    /** 🔤 Actualiza nombre */
    fun actualizarNombre(valor: String) {
        _nombre.value = valor
    }

    /** 🔤 Actualiza RUT */
    fun actualizarRut(valor: String) {
        _rut.value = valor
    }

    /** ✅ Registra asistencia con validación */
    fun registrarAsistencia(context: Context) {
        val nombreTrim = _nombre.value.trim()
        val rutTrim = _rut.value.trim()

        if (nombreTrim.isEmpty() || rutTrim.isEmpty()) {
            _mensaje.value = "⚠️ Completa todos los campos antes de registrar."
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val fecha = LocalDate.now().toString()
                val hora = LocalTime.now().withNano(0).toString()
                val dao = AppDatabase.getDatabase(context).attendanceDao()
                val registro = AttendanceEntity(
                    nombre = nombreTrim,
                    rut = rutTrim,
                    fecha = fecha,
                    hora = hora
                )
                dao.insertAttendance(registro)

                vibrar(context)

                delay(200)
                _mensaje.value = "✅ Asistencia registrada correctamente"
                _nombre.value = ""
                _rut.value = ""
            } catch (e: Exception) {
                _mensaje.value = "❌ Error al registrar asistencia"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** 📸 Simula la lectura de carnet con cámara */
    fun simularCaptura() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(900)
            _nombre.value = "Sebastián Alejandro Araya Jara"
            _rut.value = "18.019.454-1"
            _mensaje.value = "✅ Datos extraídos (simulación). Revisa y registra."
            _isLoading.value = false
        }
    }

    /** 💥 Vibración al registrar */
    private fun vibrar(context: Context) {
        try {
            val vibrator = context.getSystemService(Vibrator::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(180, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(180)
            }
        } catch (_: Exception) {
        }
    }
}


