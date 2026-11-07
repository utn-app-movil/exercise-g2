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






