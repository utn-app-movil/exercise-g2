# Fase 0: Documentación de la solución de errores intencionales (Debugging)

En esta fase se identificaron y corrigieron los errores intencionales presentes en el proyecto.

---

## 1. Error en la visualización del mes
Al seleccionar una fecha, el mes mostrado no correspondía con el mes seleccionado en el calendario.

En el método `onDateSet` se agregó `+ 1` al valor del mes (`monthValue`), debido a que Android maneja los meses mediante índices que comienzan en cero.

**Evidencia:**


---

## 2. Formateo incorrecto en el código de almacenamiento
Se identificó una inconsistencia en el formato utilizado para almacenar la información entre `TaskActivity` y `util.kt`.

Se corrigió el patrón en `util.kt` para que coincidiera con el utilizado en `TaskActivity`, manteniendo un formato uniforme en ambos archivos.

**Evidencia:**


---

## 3. Error en la visualización de tareas duplicadas
Al mostrar las tareas almacenadas, se producía una duplicación de la primera tarea.

El problema se encontraba en el uso de `- 1` al calcular la posición de los elementos cuando el índice era mayor que 0.

**Evidencia:**


---

## 4. Visualización incorrecta del icono de eliminar
Después de almacenar una tarea, el icono de eliminar (basurero) continuaba mostrándose, aunque ya no correspondía al estado de la operación.

Se modificó el valor de `isEditMode` a `false` después de almacenar la información.

**Evidencia:**


---
