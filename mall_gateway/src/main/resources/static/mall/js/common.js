function logout(){
    $.ajax({
        type:'GET',
        url:"/logout",
        success: function(result){
            window.location.href="/index";
        },
        error:function(){
            alert("跳转失败")
        }
    });
}