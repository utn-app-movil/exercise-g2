Error 1 

No se puede abrir la taskListActivity
El error fue encontrado al probar la aplicación.
y en el debbugeo aparece el siguiente error
![alt text](errors/image.png)
el cual indica que la actividad no está registrada en el adroid manifest.
Para solucionarlo de agregó la activity.
![alt text](errors/image-1.png)

Error 2 Item fecha

Siempre que se guarda un mes aparece el anterior, además que enero empieza en 0 y febrero en 1.
![alt text](errors/image-2.png)
En las variables se puede apreciar como están incorrectas.
Para solucionarlo se le sumó uno.
![alt text](errors/image-3.png)

Al buscar una tarea para editarla aparece el formato de la fecha incorrecto.
![alt text](errors/image-4.png)
![alt text](errors/image-5.png)
El error está en util.
![alt text](errors/image-6.png)
Para solucionarlo sólo se cambió el orden.

Al editar la fecha de una tarea aunque se seleccione la misma fecha el mes aparece de forma incorrecta.
![alt text](errors/image-7.png)
Para solucionarlo se realizó lo siguiente.
![alt text](errors/image-8.png)

Error 3
Al editar una tarea ya creada desde la lista aparece en vez del id el título.
![alt text](errors/image-100.png)
El error se encuentra acá.
![alt text](errors/image000.png)
Para solucionarlo se realizó lo siguiente.
![alt text](errors/image-200.png)

Error 4
Si se registran más de tres tareas la primera aparece dos veces, la segunda aparece en la tercera posición y la tercera no aparece.
![alt text](errors/image-300.png)
El error ocurre en el método onBindViewHolder() de TaskListAdapter.kt, que se encarga de asignar los datos de cada tarea a su posición en el RecyclerView. El problema es que se utilizaba position - 1, por lo que, cuando el RecyclerView solicitaba mostrar la tarea de la posición 1, el código obtenía la tarea de la posición 0. Esto provocaba que se repitieran tareas y que la última no apareciera. 
![alt text](errors/image-500.png)
![alt text](errors/image-400.png)
![alt text](errors/image-600.png)
Para solucionarlo se realizó lo siguiente.
![alt text](errors/image-700.png)

Error 5
Al presionar el botón de limpiar la pantalla y poner un id igual a una tarea ya registrada e intentar guardarla en vez de mostrar que los datos están repetidos, actualiza la tarea.
El error se encuentra acá, al aparecer erroneamente isEditMode=true.
![alt text](errors/image-800.png)
Para solucionarlo se realizó lo siguiente.

Error 6
Al presionar el botón de borrar aunque no tenga datos aparece eliminado exitosamente.
esto se debe a que el método removeTask del TaskViewModel no posee una validación del id.
![alt text](errors/image-13.png)
Para solucionarlo se realizó lo siguiente.
![alt text](errors/image-14.png)

Error 7 
El método saveTask() en TaskViewModel puede permitir guardar ids repetidos.
Al intentar registrar una tarea con un identificador existente, la aplicación mostró el mensaje "The information is already stored", impidiendo realizar el registro duplicado. Durante la revisión del código se identificó que esta validación se encuentra implementada en TaskActivity.kt, pero no directamente en TaskViewModel.kt. Esto representa una debilidad en la lógica de negocio, ya que otros componentes podrían invocar el método del ViewModel sin realizar la misma validación.
Para que este quede solucionado lo mejor sería dejar el código así.
![alt text](errors/image-9.png)
![alt text](errors/image-10.png)

Error 8
El método updateTask en TaskViewModel no posee la validación correspondiente con el id así que podría terminar guardando una tarea que vamos a editar como una nueva.
![alt text](errors/image-11.png)
Para solucionarlo se realizó lo siguiente.
![alt text](errors/image-12.png)