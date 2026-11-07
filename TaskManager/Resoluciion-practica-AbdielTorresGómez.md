**Error 1: Activity no registrada**

Se colocó un Breakpoint en `MainActivity.kt`, en la llamada a `util.openActivity()` del botón Task List. Al ejecutar `context.startActivity(intent)`, el depurador mostró una excepción `ActivityNotFoundException`, indicando que `TaskListActivity` no estaba registrada en `AndroidManifest.xml`.

![Evidencia](imgEvidencia/imgError1.png)

**Solución:** Se agregó `TaskListActivity` en `AndroidManifest.xml`.

![Evidencia](imgEvidencia/Solucion1.png)

**Error 2: Formato de fecha incorrecto**

Se colocó un Breakpoint en `TaskActivity.kt`, en la llamada a `parseStringToDateModern()` del método `saveTask()`. Al depurar, se identificó que el método recibía el formato `dd/MM/yyyy`, pero utilizaba `MM/dd/yyyy`, provocando una interpretación incorrecta de la fecha.

![Evidencia](imgEvidencia/imgError2.png)

**Solución:** Se reemplazó `"MM/dd/yyyy"` por `pattern` en el método `parseStringToDateModern()`.

![Evidencia](imgEvidencia/Solucion2.png)






