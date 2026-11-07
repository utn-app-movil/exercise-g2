Fase 0 \
 error #1\
1.
al precionar el botón para abrir la task list esta se cerraba por lo que se busco
y al revisar el manifest este no tenia el activity de list se agrego y se 
probo y ya muestra las tareas
error : No abria la pantalla al precionar el boton [error](img-fase0/img%202.png)\
correccion: se agrego el manifest de la pantalla [solucion](img-fase0/img%201.png) [lista](img-fase0/img%203.png)





  -Errores de fechas -
2.

el formato de fecha estaba incorrecto ya que el mes daba - 1 ya que el calendario partia de indice 0 
error : formato de fecha incorrecto [calendario](img-fase0/img4.png) [formato](img-fase0/img%205.png)\
solucion: se le sumo uno para que el mes diera correcto [correccion](img-fase0/img%206.png) [correccion](img-fase0/img7.png)

en las task list al darle click a una tarea previamente guardada envia el titulo en la posicion del id 
error: envio de informacion incorrecta [task](img-fase0/img%208.png)[error](img-fase0/img%209%20(2).png)\
solucion: se debugueo la activity de list y se encontro la variable que se enviaba y  se modifico por la cual debia llegar[solucion](img-fase0/imh%2010.png) [solucion](img-fase0/img%2011.png)

al abrir el calendario por segunda ves no aparecia el mes el cual se selecciono si no un mes despues [error](img-fase0/img%2012.png) [error](img-fase0/img%2013.png) [error](img-fase0/img%2014.png)
error: muestra un mes mas la abrir la segunda ves
solucion: al debuguear se vio que enviaba un mes adelante por lo que se le resto -1 a esa variable y quedo solucionado [solucion](img-fase0/img%2015.png)


3.