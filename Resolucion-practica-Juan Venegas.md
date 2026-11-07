1- Archivo útil con formato incorrecto de fecha.
La aplicación muestra y envía las fechas con el formato dd/MM/yyyy, así que con una fecha como 20/11/2026 el formateador toma 20 como mes, falla y la función devuelve null

se corrigio usando el pattern como se pide en la función, en lugar del formato que estaba escrito manualmente.

1.1 - TaskActivity con formato de fecha mal 
Esto ocurre porque Android comienza a contar los meses desde cero, mientras que nuestra aplicación los cuenta desde uno. Esto provoca que las fechas mostraran un mes antes del que se digita por ejemplo si se digita mes 11, aparece con el mes 10.

Como se solucionó: en la función OnDateSet se debe poner en la variable month  el formato + 1 para que Android comience desde 1 a 12 y no de 0 a 11 como sucedía anteriormente

![img](imagenes/Error1.png)

![img](imagenes/Error1.2.png)

2- En el Manifest no se llama a la clase TaskListActivity 
Al tocar la opción Task List te saca de la app por que esta llamado en el manifest.

Solución: llamar la clase TaskListActivity en el manifest para que lo lleve a la ventana correcta


![img](imagenes/Error2.png)

3- TaskListAdapter se repiten las tareas al crear nuevas
Este error se debe a que el programa está restando 1 a la posición de los elementos cuando la posición es mayor que cero.

Solución: Eliminar el if junto al else

![img](imagenes/Error3.png)

4- En el TaskListActivity el valor de id mal
El id en TaskListActivity el id devuelve el titulo y no su valor original por eso al tocar la opción Task List las tareas salen con valores nulos y en el id sale el nombre del titulo
Solución: Corregir el valor que debe devolver id para que no devuelva el valor del título y devuelva su id correcto.



![img](imagenes/Error4.png)