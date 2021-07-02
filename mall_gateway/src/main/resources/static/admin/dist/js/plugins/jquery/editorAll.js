$(function () {
        var editor;
        //详情编辑器
        editor = KindEditor.create('textarea[id="editor"]', {
            items: ['source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
                'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
                'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'multiimage',
                'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
                'anchor', 'link', 'unlink'],
            uploadJson: '/admin/upload/file',
            filePostName: 'file'
        });
    })
