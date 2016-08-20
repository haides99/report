"use strict";

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



// ====================== list ========================================================================


