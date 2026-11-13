
# Errores y correcciones

## Error 1. Cierre al abrir la lista de tareas

**Problema:** Al ingresar a TaskListActivity, la aplicación se cerraba porque la actividad no estaba declarada en AndroidManifest.xml.

**Corrección:** Se agregó TaskListActivity al manifiesto, siguiendo el formato de las demás pantallas. Después del cambio, la pantalla pudo abrirse.

### Evidencias

**Error observado en Android Studio**

![Error 1.1](Evidencias/error1.1.png)

**Registro de la actividad en el manifiesto**

![Error 1.2](Evidencias/error1.2.png)

**Resultado: pantalla de lista abierta**

![Error 1.3](Evidencias/error1.3.png)

---

## Error 2. La tarea seleccionada no cargaba sus datos

**Problema:** Al seleccionar una tarea desde la lista, la pantalla de edición no mostraba sus datos.

**Causa y corrección:** En TaskListActivity.kt, onItemClick() enviaba task.title en lugar de task.id. Se cambió a task.id para buscar la tarea por su identificador.

### Evidencias

**Lista de tareas**

![Error 2.1](Evidencias/error2.1.png)

**Formulario sin datos**

![Error 2.2](Evidencias/error2.2.png)

**Antes: task.title**

![Error 2.3](Evidencias/error2.3.png)

**Después: task.id**

![Error 2.4](Evidencias/error2.4.png)

---

## Error 3. Tareas duplicadas en la lista

**Problema:** Al crear dos tareas, la lista repetía la primera y no mostraba correctamente la segunda.

**Causa y corrección:** En TaskListAdapter.kt se usaba tasks[position - 1]. Se reemplazó por tasks[position] para mostrar el elemento que corresponde a cada posición.

### Evidencias

**Antes: tarea duplicada**

![Error 3.1](Evidencias/error3.1.png)

**Después: tareas diferentes**

![Error 3.2](Evidencias/error3.2.png)

**Código anterior**

![Error 3.3](Evidencias/error3.3.png)

**Código corregido**

![Error 3.4](Evidencias/error3.4.png)

---

## Error 4. Fecha incorrecta y problema al guardar

**Problema:** El formulario mostraba un mes anterior al seleccionado y se observaba un mensaje "null" al intentar guardar. Además, al abrir nuevamente el calendario, aparecía un mes posterior al que estaba guardado.

**Corrección:** En TaskActivity.kt se agregó +1 al mes recibido en onDateSet() para mostrar y almacenar el mes correcto. También se ajustó el formato de fecha en util.kt. Además, en searchTask() se agregó -1 al valor de dueMonth para que, al abrir el calendario, apareciera el mes correspondiente a la fecha guardada.

### Evidencias

**Antes: fecha y mensaje de error**

![Error 4.1](Evidencias/error4.1.png)

**Código del selector de fecha**

![Error 4.2](Evidencias/error4.2.png)

**Código anterior de onDateSet()**

![Error 4.3](Evidencias/error4.3.png)

**Código corregido de onDateSet()**

![Error 4.4](Evidencias/error4.4.png)

**Ajuste del mes**

![Error 4.5](Evidencias/error4.5.png)

**Formato de fecha anterior en util.kt**

![Error 4.6](Evidencias/error4.6.png)

**Formato corregido en util.kt**

![Error 4.7](Evidencias/error4.7.png)

**Resultado en el formulario**

![Error 4.8](Evidencias/error4.8.png)

**Corrección en searchTask() y ajuste de dueMonth**

![Error 4.9](Evidencias/error4.9.png)
