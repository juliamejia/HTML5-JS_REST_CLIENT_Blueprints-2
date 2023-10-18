var apiclient = apiclient;
//para apiclient es cambiar
//todo lo q dice apimock a apiclient :)
var app = (function (){
    var author;
    var blueprintName;
    var canvas = document.getElementById("myCanvas"),
        context = canvas.getContext("2d");

    function getName() {
            $("#name").text(author + "'s " + "blueprints:");
        }

     function getNameAuthorBlueprints() {
        author = $("#author").val();
        if (author === "") {
            alert("Debe ingresar un nombre");
        } else {
            apiclient.getBlueprintsByAuthor(author,parceroData);
        }
     }

     var parceroData = function( data) {
         $("#table tbody").empty();
         if (data === undefined) {
             alert("No existe el autor");
             $("#name").empty();
             $("#points").text("Total Points");
             $("#nameblu").empty();
         } else {
             getName();
             const datanew = data.map((elemento) => {
                 return {
                     name: elemento.name,
                     puntos: elemento.points.length
                 }
             });

             datanew.map((elementos) => {
                 $("#table > tbody:last").append($("<tr><td>" + elementos.name + "</td><td>" + elementos.puntos.toString() +
                     "</td><td>" + "<button  id=" + elementos.name + " onclick=app.getBlueprintByAuthorAndName(this)>open</button>" + "</td>"));
             });

             const totalPuntos = datanew.reduce((suma, {puntos}) => suma + puntos, 0);

             $("#points").text("Total user points: " + totalPuntos);
            }
         }
         function getBlueprintByAuthorAndName(data) {
                 author = $("#author").val();
                 blueprintName = data.id;
                 $("#nameblu").text("Current blueprint: " + blueprintName);
                 apiclient.getBlueprintByAuthorAndName(author, blueprintName, pintaparcero);
             }

         function pintaparcero(data) {
                 const puntos = data.points;
                 var c = document.getElementById("myCanvas");
                 var ctx = c.getContext("2d");
                 ctx.clearRect(0, 0, c.width, c.height);
                 ctx.beginPath();
                 for (let i = 1; i < puntos.length; i++) {
                     ctx.moveTo(puntos[i - 1].x, puntos[i - 1].y);
                     ctx.lineTo(puntos[i].x, puntos[i].y);
//                     if (i === puntos.length - 1) {
//                         ctx.moveTo(puntos[i].x, puntos[i].y);
//                         ctx.lineTo(puntos[0].x, puntos[0].y);
//                     }
                 }
                 ctx.stroke();

             }
         function fun(){
         }

         function init(){
            let coords = canvas.getBoundingClientRect();
            apiclient.addPoints((event.clientX - (screen.width/2)), (event.clientY - Math.round(coords.top) - 1), author, blueprintName, getNameAuthorBlueprints);
//            getNameAuthorBlueprints();
            apiclient.getBlueprintByAuthorAndName(author, blueprintName, pintaparcero);
         }

//         function deleteBp(){
//            apiclient.deleteBp(author, blueprintName, fun).then(deleteCanvas).then(getNameAuthorBlueprints)
////            getNameAuthorBlueprints();
//         }
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

         function createBp(){
            var bpName = prompt("Por favor digite el nombre del plano", "")
            apiclient.createBp(author, bpName).then(()=>{
                getNameAuthorBlueprints();
            })
            .catch(err => console.log(err))
         }
//        const promiseCreate = new Promise(function createBp(){
//                                                      var bpName = prompt("Por favor digite el nombre del plano", "");
//                                                      apiclient.createBp(author, bpName, fun)});



     return{
        createBp: createBp,
        deleteBp: deleteBp,
        init,init,
        getBlueprintByAuthorAndName:getBlueprintByAuthorAndName,
        getNameAuthorBlueprints: getNameAuthorBlueprints
     }
})();