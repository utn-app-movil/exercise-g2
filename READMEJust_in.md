# Resolución práctica TaskManager - Justin Chavarría

## Fase 0 - Errores encontrados

Para buscar los errores usé el modo Debug de Android Studio, poniendo breakpoints en los métodos donde se trabaja con la fecha y revisando los valores de las variables. También usé el Logcat cuando la app se caía.

Al principio tuve un problema con el emulador: la pantalla se quedaba en negro y el debug se desconectaba. Revisando el Logcat vi que el error no venía de la app sino del emulador (Baklava API 36.1), así que lo cambié por otro y ya pude depurar normal.

--imagen

### Error 1 - util.kt

En `parseStringToDateModern` el formato estaba fijo como `"MM/dd/yyyy"` y no se usaba el parámetro `pattern`. Como en `saveTask` se manda `"dd/MM/yyyy"`, el día y el mes quedaban al revés y la fecha no se guardaba bien.

Lo cambié para que use el pattern que recibe:

```kotlin
val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
```

--imagen

### Error 2 - TaskActivity.kt (onDateSet)

Cuando escogía una fecha en el calendario el mes salía uno menos, por ejemplo en enero salía `00`. Con el debug vi que el `DatePickerDialog` devuelve los meses empezando en 0, entonces había que sumarle 1 al mostrarlo:

```kotlin
tvDueDate.text = getDateFormatString(dayOfMonth, month + 1, year)
```

imagen

### Error 3 - TaskActivity.kt (searchTask)

Al buscar una tarea ya guardada y abrir el calendario, se abría un mes adelante. Pasaba lo contrario al error anterior: `monthValue` va de 1 a 12 pero el calendario ocupa de 0 a 11, entonces le resté 1:

```kotlin
dueMonth = task.dueDate.monthValue - 1
```

imagen

### Error 4 - AndroidManifest.xml

Cuando tocaba el botón para abrir la lista de tareas la app se caía. En el Logcat salía que no encontraba la activity, y revisando el `AndroidManifest.xml` vi que `TaskListActivity` no estaba declarada. La agregué dentro de `<application>`:

```xml
<activity
    android:name=".TaskListActivity"
    android:exported="false" />
```

imagen

### Error 5 -

### Error 6 -

### Error 7 -

### Error 8 -

### Diálogo de confirmación

## Fase 1 - Pantalla de estadísticas

## Fase 2 - Búsqueda y filtros

## Fase 3 - Swipe to delete