$(function () {
    $('#keyword').keypress(function (e) {
        var key = e.which; //e.which是按键的值 这里默认了回车键
        if (key == 13) {
            var q = $(this).val();
            if (q && q != '') {
                window.location.href = '/search?keyword=' + q;
            }
        }
    });
});

function search() {
    var q = $('#keyword').val();
    if (q && q != '') {
        // $.ajax({
        //     type:'GET',
        //     url:'/search?keyword=' + q,
        //     success:function(result){
        //         if(result.resultCode==200){
        //                 result.data;
        //         }
        //     }
        // })
        window.location.href = '/search?keyword=' + q;
    }
}