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

![img](imagenes/Error_04.png)

lo modifique asi agregando un +1

![img](imagenes/Error_4_1.png)

Error_04: En la funcion onItemClick se enviaba el titulo en vez de la Id

![img](imagenes/Error_05.png)

Lo correcto es ponerle el valor de Id para que ahora si devuelva la id en lugar del titulo

![img](imagenes/Error_05_01.png)


