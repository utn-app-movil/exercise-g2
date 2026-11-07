Errores de la Fase 0:

1-	La fecha se guarda como un mes antes de la que se pone.
![primera imangen](imagenes/imagen2.png)
El limitante de el mes dice que empieza en 0 y termina en 11 por lo que si ponemos por ejemplo el mes de noviembre nos salía el de Octubre se arregla al agregar un +1 frente al month del getDateFormat

2-	al volver a lo hecho por ID se cambian entre el día y el mes.
![segunda imagen](imagenes/imagen1.png)
Es debido a que en el formato a la hora de guardar dice mes/día/año en vez de día/mes/año

3-	Al intentar abrir la task list se cierra todo el proyecto (se detiene el run del proyecto).
![tercera imagen](imagenes/imagen3.png)
Simplemente se crea en el apartado de manifests un activity para el TaskList

4-	Cuando guardamos un Task nuevo no se busca por el ID si no por el nombre asi que borra todo lo que tiene
![cuarta imagen](imagenes/imagen4.png)
se modifica al cambiar en el TaskListActivity en la parte de onItemClick cambiando el "task.tilte" de el final de la funcion por "task.id"

5-	Cuando se guardan dos Tasks la segunda no se puede guardar porque la primera le cae encima y la borra
![quinta imagen](imagenes/imagen5.png)
Para modificarlo hay que eliminar el -1 que se encuetra en TaskListAdapter.kt y ya no se duplican las task.