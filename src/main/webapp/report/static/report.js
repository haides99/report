"use strict";

var fileLoaded = $.ajax({
    url: window.config.root + "/report/listFile",
    method: "GET",
    async: true,
    cache: false,
}).done(function(data) {
    for (var i = 0; i < data.length; i++) {
        $("#files").append('<div class="fileRow"><span style="display:inline-block;width: 200px;">' + data[i] + '</span>&nbsp<button data-file="' + data[i] + '">load</button></div>');
    }
    $("#files button").click(function() {
        $.ajax({
            url: window.config.root + "/report/loadData?fileName=" + $(this).data("file"),
            method: "POST",
            async: true,
            cache: false,
        }).done(function(data) {
            alert(data);
        }).fail(function(jqXHR) {
            alert(jqXHR.responseText);
        });
    });
}).fail(function(jqXHR) {
    alert(jqXHR.responseText);
});

$.when(fileLoaded).then(function() {
    $("#uploadForm")[0].action = window.config.root + "/report/upload"
    $("#uploadForm").show();
});

// ====================== demo ========================================================================

var createReport = function() {
    var windowFeatures = "toolbar=no,scrollbars=yes,resizable=yes,menubar=no,location=no,status=no,top=100,left=100,width=800,height=600";
    window.open(window.config.root + "/report/static/reportEditor.html", "_blank", windowFeatures, true);
}

var editReport = function(reportId) {
    var windowFeatures = "toolbar=no,scrollbars=yes,resizable=yes,menubar=no,location=no,status=no,top=100,left=100,width=800,height=600";
    window.open(window.config.root + "/report/static/reportEditor.html?reportId=" + reportId, "_blank", windowFeatures, true);
}

var deleteReport = function(reportId) {
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
}

$("#create").click(createReport);

$("#edit").click(function() {
    var reportId = $("#reportId").val();
    if (reportId.trim()) {
        editReport(reportId);
    }
});

$("#delete").click(function() {
    var reportId = $("#reportId").val();
    if (reportId.trim()) {
        var confirmed = confirm("are you sure to delete this report?");
        if (!confirmed) {
            return;
        }
        deleteReport(reportId).done(function() {
            alert("delete success");
        });
    }
});
