#### Escuela Colombiana de Ingeniería
#### Procesos de desarrollo de software - PDSW
#### Construción de un cliente 'grueso' con un API REST, HTML5, Javascript y CSS3. Parte II.
#### Integrantes: Cristian Rodriguez y Julia Mejia



![](img/mock2.png)

1. Agregue al canvas de la página un manejador de eventos que permita capturar los 'clicks' realizados, bien sea a través del mouse, o a través de una pantalla táctil. Para esto, tenga en cuenta [este ejemplo de uso de los eventos de tipo 'PointerEvent'](https://mobiforge.com/design-development/html5-pointer-events-api-combining-touch-mouse-and-pen) (aún no soportado por todos los navegadores) para este fin. Recuerde que a diferencia del ejemplo anterior (donde el código JS está incrustado en la vista), se espera tener la inicialización de los manejadores de eventos correctamente modularizado, tal [como se muestra en este codepen](https://codepen.io/hcadavid/pen/BwWbrw).  
En la clase app.js agregamos la funcion init() y la variable del canvas

	```javascript
	var canvas = document.getElementById("myCanvas"),
	        context = canvas.getContext("2d"); ...
	function init(){
	            let coords = canvas.getBoundingClientRect();
	            apiclient.addPoints((event.clientX - (screen.width/2)), (event.clientY - Math.round(coords.top) - 1), author, blueprintName, getNameAuthorBlueprints);
	            apiclient.getBlueprintByAuthorAndName(author, blueprintName, pintar);
	         }
	```

2. Agregue lo que haga falta en sus módulos para que cuando se capturen nuevos puntos en el canvas abierto (si no se ha seleccionado un canvas NO se debe hacer nada):
	1. Se agregue el punto al final de la secuencia de puntos del canvas actual (sólo en la memoria de la aplicación, AÚN NO EN EL API!).
	2. Se repinte el dibujo.  
    	<img width="716" alt="image" src="https://github.com/juliamejia/HTML5-JS_REST_CLIENT_Blueprints-2/assets/98657146/2acd19be-507b-410c-ac5a-985ef318e503">  

    	Primero observamos que el plano solo tiene una linea  
		<img width="250" alt="image" src="https://github.com/juliamejia/HTML5-JS_REST_CLIENT_Blueprints-2/assets/98657146/caa3fae5-46bb-4a26-9dc2-f87f08131f23">

		dando click y arrastrando el mouse podemos agregar mas al dibujo

		<img width="271" alt="image" src="https://github.com/juliamejia/HTML5-JS_REST_CLIENT_Blueprints-2/assets/98657146/8d46692f-cea0-42f1-a504-2fbb7310a41a">  

3. Agregue el botón Save/Update. Respetando la arquitectura de módulos actual del cliente, haga que al oprimirse el botón:
	1. Se haga PUT al API, con el plano actualizado, en su recurso REST correspondiente.
	2. Se haga GET al recurso /blueprints, para obtener de nuevo todos los planos realizados.
	3. Se calculen nuevamente los puntos totales del usuario.

	Para lo anterior tenga en cuenta:

	* jQuery no tiene funciones para peticiones PUT o DELETE, por lo que es necesario 'configurarlas' manualmente a través de su API para AJAX. Por ejemplo, para hacer una peticion PUT a un recurso /myrecurso:

	```javascript
    return $.ajax({
        url: "/mirecurso",
        type: 'PUT',
        data: '{"prop1":1000,"prop2":"papas"}',
        contentType: "application/json"
    });
    
	```
	Para éste note que la propiedad 'data' del objeto enviado a $.ajax debe ser un objeto jSON (en formato de texto). Si el dato que quiere enviar es un objeto JavaScript, puede convertirlo a jSON con: 
	
	```javascript
	JSON.stringify(objetojavascript),
	```
	* Como en este caso se tienen tres operaciones basadas en _callbacks_, y que las mismas requieren realizarse en un orden específico, tenga en cuenta cómo usar las promesas de JavaScript [mediante alguno de los ejemplos disponibles](http://codepen.io/hcadavid/pen/jrwdgK).  
En apimock.js agregamos la funcion de añadir puntos  y la llamamos en la funcion init() de app.js  
		```javascript
	 	function addPoints(x, y, author, bpname, callback){
	        var insert = {"x": x, "y":y};
	        mockdata[author].find(function(e){return e.name===bpname}).points.push(insert);
	        callback();
	    }
	
		return {
		    addPoints : addPoints,
	
			getBlueprintsByAuthor:function(authname,callback){
				callback(mockdata[authname]);
			},
	
			getBlueprintByAuthorAndName:function(authname,bpname,callback){
	
				callback(mockdata[authname].find(function(e){return e.name===bpname}));
			}
		}
	 	```


4. Agregue el botón 'Create new blueprint', de manera que cuando se oprima: 
	* Se borre el canvas actual.
	* Se solicite el nombre del nuevo 'blueprint' (usted decide la manera de hacerlo).
	
	Esta opción debe cambiar la manera como funciona la opción 'save/update', pues en este caso, al oprimirse la primera vez debe (igualmente, usando promesas):

	1. Hacer POST al recurso /blueprints, para crear el nuevo plano.
	2. Hacer GET a este mismo recurso, para actualizar el listado de planos y el puntaje del usuario.  
    	
    	<img width="721" alt="image" src="https://github.com/juliamejia/HTML5-JS_REST_CLIENT_Blueprints-2/assets/98657146/bffbf093-515e-42c6-a102-35ee2bf26a14">  
     
     	Al hacer click eb create a new blueprint nos muestra una ventana donde pondremos el nombre de la obra que queramos crear  
      
      	<img width="272" alt="image" src="https://github.com/juliamejia/HTML5-JS_REST_CLIENT_Blueprints-2/assets/98657146/317963ec-399f-4094-b4cd-4582e158faf6">  
       
       	Y vemos que se crea  
	
       	<img width="374" alt="image" src="https://github.com/juliamejia/HTML5-JS_REST_CLIENT_Blueprints-2/assets/98657146/656d8b19-0550-40d1-a062-d202a504b3fc">  

5. Agregue el botón 'DELETE', de manera que (también con promesas):
	* Borre el canvas.
	* Haga DELETE del recurso correspondiente.
	* Haga GET de los planos ahora disponibles.
	En la clase app.js creamos la funcion deleteBp() y deleteCanvas()  

	```javascript
	function deleteBp(){
	         apiclient.deleteBp(author, blueprintName).then(() => {
	         deleteCanvas();
	         getNameAuthorBlueprints();
	         })
	         .catch(err => console.log(err))
	         }
	
	         function deleteCanvas(){
	            var c = document.getElementById("myCanvas");
	            var ctx = c.getContext("2d");
	            ctx.clearRect(0, 0, c.width, c.height);
	
	         }
	```

### EJECUCION 
1. clonar el repositorio con : git clone https://github.com/juliamejia/HTML5-JS_REST_CLIENT_Blueprints-2.git
2. Entrar a la carpeta y desde donde esta el archivo pom abrir una consola
3. Usar el comando : mvn compile
4. Usar el comando :mvn spring-boot:run
5. Desde cualquier browser entrar a https://localhost:8080/index.html

### Criterios de evaluación

1. Funcional
	* La aplicación carga y dibuja correctamente los planos.
	* La aplicación actualiza la lista de planos cuando se crea y almacena (a través del API) uno nuevo.
	* La aplicación permite modificar planos existentes.
	* La aplicación calcula correctamente los puntos totales.
2. Diseño
	* Los callback usados al momento de cargar los planos y calcular los puntos de un autor NO hace uso de ciclos, sino de operaciones map/reduce.
	* Las operaciones de actualización y borrado hacen uso de promesas para garantizar que el cálculo del puntaje se realice sólo hasta cando se hayan actualizados los datos en el backend. Si se usan callbacks anidados se evalúa como R.
	
