Resolucion Practica de Laboratorio - TaskManager

Estudiante: Julian
Curso: Desarrollo de Aplicaciones Moviles - UTN
Profesor Reviewer: ebarahonautn

Fase 0: Debugging y Solucion de Errores Intencionales

En la primera fase del laboratorio se utilizaron las herramientas de depuracion del IDE como Logcat, Breakpoints y Stack Trace para identificar y solucionar los 8 errores intencionales presentes en el proyecto base.

1. Error en AndroidManifest.xml:
La actividad TaskListActivity no estaba registrada en el manifiesto. Al intentar abrir la lista de tareas desde la pantalla principal la aplicacion lanzaba una excepcion ActivityNotFoundException. Se agrego la declaracion de TaskListActivity y TaskStatsActivity dentro del archivo AndroidManifest.xml.

2. Error en TaskListActivity.kt al abrir tarea:
En el metodo onItemClick de la lista se estaba pasando el titulo de la tarea (task.title) en lugar del id (task.id) en el extra EXTRA_MESSAGE_ID. Como en TaskActivity el metodo searchTask busca por id, la tarea no se cargaba. Se cambio para enviar el task.id.

3. Error en TaskListAdapter.kt:
En onBindViewHolder existia una condicion invalida val item = if (position > 0) tasks[position - 1] else tasks[position]. Esto causaba que el primer elemento se duplicara en el indice 1 y se perdiera la ultima tarea de la lista. Se corrigio para usar directamente tasks[position].

4. Error en util.kt parseStringToDateModern:
La funcion parseStringToDateModern tenia hardcoded el formato MM/dd/yyyy dentro de DateTimeFormatter en lugar de utilizar el parametro pattern recibido. Al enviar dd/MM/yyyy retornaba null y provocaba un NullPointerException al guardar. Se cambio para utilizar la variable pattern.

5. Error en TaskActivity.kt cleanScreen:
Al presionar cancelar o limpiar la pantalla, el metodo cleanScreen asignaba la variable isEditMode en true en lugar de false. Esto hacia que al intentar registrar una nueva tarea el sistema pensara que era una edicion e invocara updateTask. Se corrigio asignando isEditMode = false.

6. Error en TaskActivity.kt formato de fecha en DatePickerDialog:
DatePickerDialog devuelve los meses indexados desde 0. En onDateSet se imprimia directamente el mes recibido sin sumar 1, mostrando por ejemplo 00 para enero. Se soluciono sumando 1 al formatear la fecha y restando 1 al cargar una tarea existente en el DatePicker.

7. Error en TaskActivity.kt onCreateOptionsMenu:
Dentro del metodo onCreateOptionsMenu se estaba llamando a invalidateOptionsMenu(), lo que provocaba llamadas innecesarias y continuas al menu. Se elimino esa instruccion dentro de dicho metodo.

8. Confirmacion con AlertDialog en TaskActivity:
Se agrego la verificacion solicitada utilizando util.showDialogCondition antes de guardar una modificacion o de eliminar una tarea en TaskActivity.

Fase 1: Pantalla de Estadisticas y Resumen (TaskStatsActivity)

Se creo la nueva pantalla TaskStatsActivity y su layout activity_task_stats.xml para visualizar las metricas del sistema.
- Se muestra el total de tareas registradas.
- Se desglozan los conteos por estado: PENDING, IN_PROGRESS, COMPLETED y CANCELLED.
- Se incluyo una ProgressBar horizontal que representa el porcentaje de avance de tareas completadas respecto al total.
- En TaskViewModel se crearon los metodos getTotalTasksCount, getTasksCountByStatus y getCompletionPercentage utilizando funciones de orden superior de Kotlin como count y filter.
- Se coloco un boton en MainActivity para navegar hacia TaskStatsActivity mediante un Intent.

Fase 2: Busqueda Dinamica y Filtros por Estado en TaskListActivity

Se modifico la pantalla TaskListActivity agregando un SearchView y un Spinner de estados (TODAS, PENDING, IN_PROGRESS, COMPLETED, CANCELLED) en activity_task_list.xml.
En TaskListActivity.kt y TaskViewModel.kt se creo una funcion de filtrado combinado que filtra las tareas en tiempo real segun el texto ingresado (titulo, descripcion o asignado) y el estado seleccionado en el Spinner.

Fase 3: Eliminar Tareas con Gestos (Swipe to Delete) y Opcion Undo

En TaskListActivity se implemento ItemTouchHelper.SimpleCallback para permitir borrar tareas mediante el gesto de deslizar a la izquierda o derecha en el RecyclerView.
Al eliminar una tarea, se remueve de la lista y se muestra un Snackbar con la opcion DESHACER. Si el usuario presiona DESHACER, la tarea se restaura de inmediato.

Instrucciones para el Pull Request
Branch de trabajo: feature/practica-debugging-stats
Reviewer: ebarahonautn
