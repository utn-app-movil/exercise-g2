### Error1:Formato de fecha incorrecto

![primera imagen](img/formatoInvertido.png)

EL error era por como estaba el formato MM/dd/yyyy para solucionarlo se debe de poner asi dd/MM/yyyy

### Error1.2:El valor del mes refleja el anterior al seleccionado

![segunda imagen](img/valorMes+1.png)

debido a que por defecto se inica en "0" los meses estan erroneos,se agrega un +1 al month para solucionarlo

### Error2:TaskListActivity no estaba en el manifest

![tercera imagen](img/activityManifest.png)

al no estar esa activity en el manifes al darle click al boton la app se cae,se soluciona agregandolo

### Error3:Se duplica el id en la task list

![cuarta imagen](img/ErrorDuplication.png)

es debido al -1 luego de position,para solucionarlo se elimina el -1

### Error4:utiliza el nombre en vez de id

![quinta imagen](img/idNotFound.png)

debido a que utilizaba el task.tittle en vez del id al buscar no llegaba a nada,se soluciona cambiando el task.tittle por task.id