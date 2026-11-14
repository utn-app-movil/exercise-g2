# ✦ Fase 0: Documentación de la solución de errores intencionales (Debugging) ✦
Fase de solución de los errores intencionales, se identificaron y corrigieron en el proyecto. 

---

## 1. Error en la visualización del mes
Al seleccionar una fecha, el mes mostrado no correspondía con el mes seleccionado en el calendario.

En el método `onDateSet` se agregó `+ 1` al valor del mes (`monthValue`), ya que el formato lo evaluaba con un numero menos, haciendo que se mostrara un mes erróneo al elegido.

**Evidencia:** <br>
![Error 1](Evidencias/Picture1.png)


![Error 2](Evidencias/Picture2.png)

**Cambio en el método:** <br>
![Error 3](Evidencias/Picture3.png)

*Antes:* <br>
![Error 4](Evidencias/Picture4.png)


*Después:* <br>
![Error 5](Evidencias/Picture5.png)

---

## 2. Formateo incorrecto en el código de almacenamiento
Se identificó una inconsistencia en el formato utilizado para almacenar la información entre `TaskActivity` y `util.kt`.

Se corrigió el patrón en `util.kt` para que coincidiera con el utilizado en `TaskActivity`, manteniendo un mismo formato en ambos archivos.

**Evidencia:**
En TaskActivity se encontraba de esta forma: <br>
![Error 6](Evidencias/Picture6.png)

En util.kt se encontraba de esta otra forma: <br>
![Error 7](Evidencias/Picture7.png)

Se corrigió el patrón para que coincidiera con el de TaskActivity: <br>
![Error 8](Evidencias/Picture8.png)

---

## 3. Error en la visualización de tareas duplicadas
Al mostrar las tareas almacenadas, se producía una duplicación de la primera tarea.

El problema se encontraba en el uso de `- 1` al calcular la posición de los elementos ccuando la posición es mayor a 0, haciendo que se muestre duplicada la primera tarea.

**Evidencia:** 
Antes: <br>
![Error 9](Evidencias/Picture9.png)

![Error 10](Evidencias/Picture10.png)

*Ya con eliminar el calculo de la posición se corrige.* <br>
![Error 11](Evidencias/Picture11.png)

![Error 12](Evidencias/Picture12.png)

---

## 4. Visualización incorrecta del icono de eliminar
El icono de eliminar (basurero) continuaba mostrándose, después de que se haya almacenado una tarea, así haciendo que no haya nada que eliminar.

Se modificó el valor de `isEditMode` a `false` así evita que el icono se eliminar se siga mostrando después de almacenar la información 

**Evidencia:**

*Antes:* <br>
![Error 13](Evidencias/Picture13.png)

Con esto mostraba el icono de eliminar: <br>
![Error 14](Evidencias/Picture14.png)

Después: <br>
![Error 15](Evidencias/Picture15.png)

![Error 16](Evidencias/Picture16.png)

---
