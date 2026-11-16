Etapa número 0
Errores encontrados por Stacy Montero

1. 
Error: En la aplicación no corre el botón de Task List

Como se encontro el error? Corriendo la aplicación vi que el boton de task list no funcionaba

Como se soluciono? En el manifest se le agrego el activity de .TaskListActivity

[Error01](Imagenes/Error%2001.png)

[Error1](Imagenes/captura2-Error1.png)

2. 
Error:A la hora de guardar una lista, la fecha en el mes le resta -1, entonces se le debe de poner un +1

Como se encontro el error?Corriendo la aplicacion

Como se soluciono?Se le agrego un +1 en el onDateSet

[Imagenes](Imagenes/error02/error02.png)

[TaskManager](Imagenes/error02/errorTaskManager.png)

3. 
Error: A la hora de querer enviar una tarea ya guardada, la posicion de el id me pone el title

Como se encontro el error? Al correr la aplicacion, guardarlo y querer enviarlo

Como se soluciono? Dentro de TaskListActivity en el onItemClick se le cambio un value a task.id

[problema3](Imagenes/error03/error01.png)

4. 
Error: Al guardar una lista, por ejemplo la guardo en el mes noviembre y al volver a apretar me abre un mes despues "Diciembre"

Como se encontro el error? Al correr la aplicacion y querer abrir la lista ya guardada y modificarle el calendario me abre un mes despues de lo que yo puse

Como se soluciono? Dentro de TaskActivity se le modifico showDatePickerDialog poniendole un -1


[problema4](Imagenes/error04/Error4.png)

5. 
Error: Las tareas se mostraban repetidas en la lista

Como se encontro el error? Al revisar la lista de tareas, se identificó que algunas se mostraban repetidas porque la posición se estaba calculando incorrectamente en TaskListAdapter.kt.

Como se soluciono?  Se modificó la línea que obtenía las tareas, eliminando la resta de 1 y dejando solamente tasks[position]. De esta manera, cada tarea se muestra en la posición que le corresponde.

[problema5](Imagenes/error05/error5.png)

6. 
Error:Formato incorrecto de la fecha

Como se encontro el error? Al revisar el archivo util.kt, se identificó que el formato de fecha estaba definido como MM/dd/yyyy, aunque la aplicación utiliza el formato dd/MM/yyyy.

Como se soluciono?  Se modificó la línea para utilizar el parámetro pattern, que permite trabajar con el formato de fecha que recibe la función.

[Problema6](Imagenes/error06/error6.png)

7. 
Error:La pantalla permanecía en modo edición

Como se encontro el error? Al revisar la función cleanScreen() en TaskActivity.kt, se identificó que la variable isEditMode estaba definida como true, aunque la pantalla se estuviera limpiando.

Como se soluciono? Se cambió el valor de isEditMode de true a false, para que al limpiar los campos la pantalla vuelva al modo de creación de tareas.

[error7.png](Imagenes/error07/error7.png)

8. h
Error:  El calendario mostraba el mes anterior al crear una tarea

como se encontro el error? Al revisar el código de TaskActivity.kt, se identificó que en la función resetDate() el mes se obtenía comenzando desde 0, lo que podía provocar que el calendario mostrara el mes anterior.

Como se soluciono? Se agregó un +1 a la línea que obtiene el mes actual, para que el valor sea correcto al abrir el calendario.

[error8.png](Imagenes/error08/error8.png)









