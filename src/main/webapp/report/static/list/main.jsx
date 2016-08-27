Date.prototype.format = function(format) {
    var o = {
        "M+" : this.getMonth() + 1, // month
        "d+" : this.getDate(), // day
        "h+" : this.getHours(), // hour
        "m+" : this.getMinutes(), // minute
        "s+" : this.getSeconds(), // second
        "q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
        "S" : this.getMilliseconds() // millisecond
    }
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    for ( var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                : ("00" + o[k]).substr(("" + o[k]).length));
    return format;
};

var createReport = function() {
    var windowFeatures = "toolbar=no,scrollbars=yes,resizable=yes,menubar=no,location=no,status=no,top=100,left=100,width=800,height=600";
    window.open(window.config.root + "/report/static/reportEditor.html", "_blank", windowFeatures, true);
};

var editReport = function(reportId) {
    var windowFeatures = "toolbar=no,scrollbars=yes,resizable=yes,menubar=no,location=no,status=no,top=100,left=100,width=800,height=600";
    window.open(window.config.root + "/report/static/reportEditor.html?reportId=" + reportId, "_blank", windowFeatures, true);
};

var deleteReport = function(reportId) {
    var comfirm = confirm("确定要删除吗？");
    if (!comfirm) {
        return;
    }
    var deferred = $.ajax({
        url: window.config.root + "/report/report/" + reportId,
        method: "DELETE",
        async: true,
    }).done(function(data) {
        // no-op
    }).fail(function(jqXHR) {
        alert(jqXHR.responseText);
    });
    return deferred;
};

$("#create").click(createReport);

var columns = [{
    title: "序号",
    sortable: false,
    width: "5%"
}, {
    title: "名称",
    name: "name",
    width: "15%"
}, {
    title: "描述",
    name: "description",
    width: "15%"
}, {
    title: "注释",
    name: "comments",
    className: "cell-left",
    width: "35%"
}, {
    title: "编辑",
    sortable: false,
    width: "15%",
    render: function(id) {
        return <a href="javascript: void(0)" onClick={editReport.bind(null, id)}>编辑</a>;
    },
}, {
    title: "删除",
    sortable: false,
    width: "15%",
    render: function(id) {
        return <a href="javascript: void(0)" onClick={deleteReport.bind(null, id)}>删除</a>;
    },
}];

window.reportTable = ReactDOM.render(
    <Datatable url={"/report/reportPageQuery"} columns={columns} />,
    document.getElementById("reportTable")
);

