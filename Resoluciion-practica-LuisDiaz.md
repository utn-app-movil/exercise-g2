Error_01: El botón de task list cuando lo presiono me saca de la aplicación, el androidManifest.xml solo tiene declarado MainActivity, SecondActivity y TaskActivity.
Solución: Falta declarar la función TaskListActivity dentro del androidManifest.xml

![img](imagenes/Error_01.png)

Error_02: En el taskList las dos primeras filas muestran el mismo dato guardado:

![img](imagenes/Error_02_2.png)

Lo corregi modificando la funcion de onBindViewOlder y ahora si funcionaba correctamente

![img](imagenes/Error__02.png)

![img](imagenes/Error_02_3.png)

![img](imagenes/Error_2_4.png)

Error_03: En util el formato de fecha esta mal, no esta llamando bien el metodo

![img](imagenes/Error_03.png)

lo correcto es 

![img](imagenes/Error_03_2.png)

Ademas se estan contando los meses desde 0 en lugar de 1

![img](imagenes/Error_03_4.png)

lo modifique asi agregando un +1

![img](imagenes/Error_03_1.png)

Error_04: En la funcion onItemClick se enviaba el titulo en vez de la Id

![img](imagenes/Error_04.png)

Lo correcto es ponerle el valor de Id para que ahora si devuelva la id en lugar del titulo

![img](imagenes/Error_04_02.png)

Error_05: La función cleanScreen deja el modo edición activo, cuando guarda isEditMode queda en true y una tarea nueva que agregue podria sobreescribir otra. 

![img](imagenes/Error_05.png)

La solución fue poner el valor en false.

![img](imagenes/Error_05_2.png)


Error_06: En TaskMemoryManager.kt las funciones deleteTask y getById no validan el ID, entomces si pongo un id 1 con un espacio, no se podria encontrar la tarea guardada con un id 1 sin espacio.

![img](imagenes/Error_06.png)

Le agregue un trim() para tener una mejor validacion

![img](imagenes/Error_06_2.png)

Error_07: En taskActivity.kt la función invalidateOptionsMenu() vuelve a ejecutar onCreateOptionsMenu, que la llamaba otra vez.

![img](imagenes/Error_07.png)

Borre ese llamado de la función invalidateOptionsMenu()

![img](imagenes/Erorr_07_2.png)

Error_08: En TaskMemoryManager.kt la función updateTask cambia de  una vez actualizada

![img](imagenes/Error_08.png)

![img](imagenes/Error_08_2.png)

Entonces agregue esta modificación para que la tarea que edito no salte o cambie de posición

![img](imagenes/Error_08_3.png)
