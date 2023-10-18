var apiclient = (function(){
    return {
        getBlueprintsByAuthor: function(author, callback){
        callback(
            JSON.parse($.ajax({type: 'GET', url: 'blueprints/' + author, async: false}).responseText)
        )},

        getBlueprintByAuthorAndName: function(author, bpname, callback){
        var link = author + "/" + bpname;
        callback(
            JSON.parse($.ajax({type: 'GET', url: 'blueprints/' + link, async: false}).responseText)
        )},

        addPoints: function(x, y, author,bpname, callback){
        var link = author + "/" + bpname;
        var datos = JSON.stringify({author:author,"points":[{"x":x,"y":y}],"name":bpname});
        $.ajax({
            url: "blueprints/"+link,
            type: 'PUT',
            data: datos,
            contentType: "application/json"
        });
        callback();
        },

        deleteBp:function(author,blueprintName){
            return new Promise(function(resolve,reject){
                resolve(
                    $.ajax({
                        url: "blueprints/"+author + "/" + blueprintName,
                        type: 'DELETE'
                    })
                )
            })
        },

        createBp: function(author, bpName){
            var datos = JSON.stringify({author:author,"points":[],"name":bpName});
            return new Promise(function(resolve,reject){
                resolve(
                    $.ajax({
                        url: "blueprints/",
                        type: "POST",
                        data: datos,
                        contentType: "application/json"
                    })
                )})
        }
    }
})();
