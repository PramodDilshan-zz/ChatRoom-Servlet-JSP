$(document).ready(function () {
    setInterval(function(){ 
        //alert();
        $.ajax("PublicChatServlet",{
            accepts: {mycustomtype:"*/*"},
            data:{
                action:"see-messages"                                     
            },
            dataType: 'json',
            method:"POST" 
        }).done(function(result) {
            //alert(result);
            
            var myName=result.myName;
           var arr=result.msg;
//            alert(myName+"<br>"+arr);
//           $("#tblMsg").append("<tr>"+msg+"</tr>");
            var x = document.getElementsByTagName("div");
            var numP=x.length-1;
           for(var i=numP;i<arr.length;i++){
                    
               if(myName===arr[i].name){
                    $("#msgContainer").append("<div id=\"msgContainer-left\" style=\"background-color:#B6F751\"><h5>"+myName+"</h5>"+arr[i].message+"</div>");
                }else{
                    $("#msgContainer").append("<div id=\"msgContainer-right\"><h5>"+arr[i].name+"</h5>"+arr[i].message+"</div>");
                }

           }
        });
        },500); 
    
});

