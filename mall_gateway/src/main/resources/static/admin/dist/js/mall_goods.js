$(function(){
   $("#jqGrid").jqGrid({
       url:"/admin/goods/list",
       dataType:"json",
       colModel:[
           {label: '商品编号', name: 'goodsId', index: 'goodsId', width: 60, key: true},
           {label: '商品名', name: 'goodsName', index: 'goodsName', width: 120},
           {label: '商品简介', name: 'goodsIntro', index: 'goodsIntro', width: 120},
           {label: '商品图片', name: 'goodsCoverImg', index: 'goodsCoverImg', width: 120, formatter: coverImageFormatter},
           {label: '商品库存', name: 'stockNum', index: 'stockNum', width: 60},
           {label: '商品售价', name: 'sellingPrice', index: 'sellingPrice', width: 60},
           {
               label: '上架状态',
               name: 'goodsSellStatus',
               index: 'goodsSellStatus',
               width: 80,
               formatter: goodsSellStatusFormatter
           },
           {label: '创建时间', name: 'createTime', index: 'createTime', width: 60}
       ],
       height: 760,
       rowNum: 20,
       rowList: [20, 50, 80], //显示多少条数据,默认为rowNum的20
       styleUI: 'Bootstrap',
       loadtext: '信息读取中...',
       rownumbers: false,
       rownumWidth: 20,
       autowidth: true,
       multiselect: true,
       pager: "#jqGridPager",

       jsonReader: { //用来读取服务器解析的返回数据
           root: "data.list",  //获取数据列表
           page: "data.currPage",  //获取当前页码
           total: "data.totalPage", //获取总页数
           records: "data.totalCount" //获取总数据行数
       },
       prmNames:{
            pages: "page", //参数为请求页码
            rows: "limit",  //参数为一页多少条数据
            order:  "order" //采用何种排序方式的参数
       },
       gridComplete: function () {
           //隐藏grid底部滚动条
           $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
       }
   });
    $(window).resize(function () {
        //根据这个class标签的宽度变化而变化
        $("#jqGrid").setGridWidth($(".card-body").width());
    });
    //自定义函数格式化单元格中的值
    function goodsSellStatusFormatter(cellvalue) {
        //商品上架状态 0-上架 1-下架，当前值
        if (cellvalue == 0) {
            return "<button type=\"button\" class=\"btn btn-block btn-success btn-sm\" style=\"width: 80%;\">销售中</button>";
        }
        if (cellvalue == 1) {
            return "<button type=\"button\" class=\"btn btn-block btn-secondary btn-sm\" style=\"width: 80%;\">已下架</button>";
        }
    }
    //前端接受商品主图链接并使用格式化函数将之转换为html形式在表格中显示出图片
    function coverImageFormatter(cellvalue) {
        return "<img src='" + cellvalue + "' height=\"80\" width=\"80\" alt='商品主图'/>";
    }

});
//每次进行操作后都要重新发起一次ajax请求重置页面
function reload() {
    initFlatPickr();
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}

/**
 * 添加商品
 */
function addGoods() {
    window.location.href = "/admin/goods/edit";
}

/**
 * 修改商品
 */
function editGoods() {
    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    window.location.href = "/admin/goods/edit/" + id;
}