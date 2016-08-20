"use strict";

(function() {
    var ReportEditor = React.createClass({
        
        getInitialState: function() {
            return {
                name: this.props.report.name || "",
                description: this.props.report.description || "",
                comments: this.props.report.comments || "",
                version: this.props.report.version || "",
            };
        },
        
        updateField: function(fieldName, refName, e) {
            var obj = {};
            obj[fieldName] = this.refs[refName].value;
            this.setState(obj);
        },
        
        render: function() {
            if (!this.props.display) {
                return null;
            }
            var labelStype = {
                width: "100px",
                display: "inline-block",
                textAlign: "right",
            };
            
            return <div>
                <div>
                    <label style={labelStype}>Name:</label>
                    <input type="text" 
                        ref="nameInput" 
                        onChange={this.updateField.bind(this, "name", "nameInput")} 
                        value={this.state.name} 
                        style={{width: "200px"}} />
                </div>
                <div>
                    <label style={labelStype}>Description:</label>
                    <input type="text" 
                        ref="descriptionInput" 
                        onChange={this.updateField.bind(this, "description", "descriptionInput")} 
                        value={this.state.description} 
                        style={{width: "200px"}} />
                </div>
                <div>
                    <label style={labelStype}>Comments:</label>
                    <textarea ref="commentsInput" 
                        onChange={this.updateField.bind(this, "comments", "commentsInput")} 
                        value={this.state.comments} 
                        style={{width: "198px"}} />
                </div>
                <div>
                    <label style={labelStype}>Version:</label>
                    <input type="text" 
                        ref="versionInput" 
                        onChange={this.updateField.bind(this, "version", "versionInput")} 
                        value={this.state.version} 
                        style={{width: "200px"}} />
                </div>
                <hr />
                <ul>
                    <li>generate a screen</li>
                    <li>generate an excel file from a template</li>
                    <li>full screen</li>
                    <li>adjusted</li>
                    <li>custom</li>
                </ul>
            </div>
        },
    });

    var ColumnEditor = React.createClass({
        
        getInitialState: function() {
            return {
                columns: this.props.columns || [],
            };
        },
        
        updateField: function(index, fieldName, refName, e) {
            var columns = this.state.columns.slice();
            columns[index][fieldName] = this.refs[refName].value;
            this.setState({columns: columns});
        },
        
        setColumnNames: function(columnNames) {
            var columns = this.state.columns.slice();
            var newColumns = columnNames.map(function(name, index) {
                var column = null;
                for (var i = 0; i < columns.length; i++) {
                    if (columns[i].name == name) {
                        column = columns[i];
                        column.indexNo = index;
                        break;
                    }
                }
                if (!column) {
                    column = {
                        name: name,
                        description: "",
                        indexNo: index,
                        type: 2,
                        alignement: 1,
                    }
                }
                return column;
            });
            this.setState({columns: newColumns});
        },
        
        render: function() {
            if (!this.props.display) {
                return null;
            }
            var trs = this.state.columns.map(function(column, i) {
                return <tr key={i}>
                    <td>{column.name}</td>
                    <td>
                        <input ref={"description-" + i} 
                            value={column.description}
                            onChange={this.updateField.bind(this, i, "description", "description-" + i)} />
                    </td>
                    <td>
                        <select ref={"type-" + i} 
                            value={column.type}
                            onChange={this.updateField.bind(this, i, "type", "type-" + i)} >
                            <option value="1">Integer</option>
                            <option value="2">String</option>
                            <option value="3">Float</option>
                            <option value="4">Datetime</option>
                            <option value="5">Date</option>
                            <option value="6">TimeDuration</option>
                        </select>
                    </td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>
                        <select ref={"alignement-" + i} 
                            value={column.alignement}
                            onChange={this.updateField.bind(this, i, "alignement", "alignement-" + i)} >
                            <option value="1">Left</option>
                            <option value="2">Center</option>
                            <option value="3">Right</option>
                            <option value="4">Fill</option>
                        </select>
                    </td>
                </tr>
            }.bind(this));
            return <div>
                <table style={{width: "100%"}}>
                    <thead>
                        <tr>
                            <th>Column</th>
                            <th>Name</th>
                            <th>Type</th>
                            <th>Total</th>
                            <th>Scroll</th>
                            <th>Width</th>
                            <th>Align</th>
                        </tr>
                    </thead>
                    <tbody>{trs}</tbody>
                </table>
                <hr />
                <ul>
                    <li>image-file file path</li>
                    <li>preview</li>
                </ul>
            </div>;
        },
        
    });

    var ParameterEditor = React.createClass({
        
        getInitialState: function() {
            return {
                parameters: this.props.parameters || [],
            };
        },
        
        updateField: function(index, fieldName, refName, e) {
            var parameters = this.state.parameters.slice();
            parameters[index][fieldName] = this.refs[refName].value;
            this.setState({parameters: parameters});
        },
        
        setParameterNames: function(parameterNames) {
            var parameters = this.state.parameters.slice();
            var newParameters = parameterNames.map(function(name, index) {
                var parameter = null;
                for (var i = 0; i < parameters.length; i++) {
                    if (parameters[i].name == name) {
                        parameter = parameters[i];
                        parameter.indexNo = index;
                        break;
                    }
                }
                if (!parameter) {
                    parameter = {
                        name: name,
                        description: "",
                        indexNo: index,
                        type: 2,
                        defaultValue: '',
                        listSqlText: '',
                        listSqlDefault: '',
                        multiSelection: 0,
                    }
                }
                return parameter;
            });
            this.setState({parameters: newParameters});
        },
        
        render: function() {
            if (!this.props.display) {
                return null;
            }
            var trs = this.state.parameters.map(function(parameter, i) {
                return <tr key={i}>
                    <td>{parameter.name}</td>
                    <td>
                        <input ref={"description-" + i} 
                            value={parameter.description}
                            onChange={this.updateField.bind(this, i, "description", "description-" + i)} />
                    </td>
                    <td>
                        <select ref={"type-" + i} 
                            value={parameter.type}
                            onChange={this.updateField.bind(this, i, "type", "type-" + i)} >
                            <option value="1">Integer</option>
                            <option value="2">String</option>
                            <option value="3">Float</option>
                            <option value="4">Boolean</option>
                            <option value="5">Datetime</option>
                        </select>
                    </td>
                    <td>
                        <input ref={"defaultValue-" + i} 
                            value={parameter.defaultValue}
                            onChange={this.updateField.bind(this, i, "defaultValue", "defaultValue-" + i)} />
                    </td>
                    <td>
                        <input ref={"listSqlText-" + i} 
                            value={parameter.listSqlText}
                            onChange={this.updateField.bind(this, i, "listSqlText", "listSqlText-" + i)} />
                    </td>
                    <td>
                        <input ref={"listSqlDefault-" + i} 
                            value={parameter.listSqlDefault}
                            onChange={this.updateField.bind(this, i, "listSqlDefault", "listSqlDefault-" + i)} />
                    </td>
                    <td>
                        <select ref={"multiSelection-" + i} 
                            value={parameter.multiSelection}
                            onChange={this.updateField.bind(this, i, "multiSelection", "multiSelection-" + i)} >
                            <option value="0">false</option>
                            <option value="1">true</option>
                        </select>
                    </td>
                </tr>
            }.bind(this));
            return <div>
                <table style={{width: "100%"}}>
                    <thead>
                        <tr>
                            <th>Parameter</th>
                            <th>Name</th>
                            <th>Type</th>
                            <th>Default</th>
                            <th>ListSqlText</th>
                            <th>ListSqlDefault</th>
                            <th>Multi Selection</th>
                        </tr>
                    </thead>
                    <tbody>{trs}</tbody>
                </table>
                <hr />
                <ul>
                    <li>preview</li>
                </ul>
            </div>;
        },
        
    });
    
    var Editor = React.createClass({
        
        getInitialState: function() {
            return {
                activeTab: "Report",
                reportId: this.props.report.id,
                sqlText: this.props.report.sqlText || "",
                columns: [],
                parameters: [],
            };
        },
        
        changeTab: function(tabName, e) {
            this.setState({
                activeTab: tabName,
            });
        },
        
        updateSqlText: function(e) {
            this.setState({
                sqlText: this.refs.sqlTextInput.value,
            });
        },
        
        save: function() {
            // save report
            var reportData = {
               sqlText: this.state.sqlText,
               name: this.refs.reportEditor.state.name,
               description: this.refs.reportEditor.state.description,
               comments: this.refs.reportEditor.state.comments,
               version: this.refs.reportEditor.state.version,
            };
            if (!reportData.sqlText.trim()) {
                alert("sql text cannot be empty");
                return;
            }
            if (!reportData.name.trim()) {
                alert("name cannot be empty");
                return;
            }
            // TODO add validate
            var columns = this.refs.columnEditor.state.columns;
            var parameters = this.refs.parameterEditor.state.parameters;
            if (this.state.reportId) {
                $.ajax({
                    url: window.config.root + "/report/report/" + this.state.reportId + "/updateAll",
                    method: "PUT",
                    async: true,
                    data: {
                        reportJson: JSON.stringify(reportData),
                        columnArrJson: JSON.stringify(columns),
                        parameterArrJson: JSON.stringify(parameters),
                    },
                }).then(function(data) {
                    console.log(data)
                    showMessage("update success");
                }, function(jqXHR, a, b) {
                    console.log(jqXHR, a, b)
                    alert(jqXHR.responseText);
                });
            } else {
                $.ajax({
                    url: window.config.root + "/report/report/createAll",
                    method: "POST",
                    async: true,
                    data: {
                        reportJson: JSON.stringify(reportData),
                        columnArrJson: JSON.stringify(columns),
                        parameterArrJson: JSON.stringify(parameters),
                    },
                }).then(function(data) {
                    console.log(data)
                    if (!Number.isInteger(data)) {
                        alert("create fail");
                    }
                    this.setState({reportId: data});
                    showMessage("create success");
                }.bind(this), function(jqXHR, a, b) {
                    console.log(jqXHR, a, b)
                    alert(jqXHR.responseText);
                });
            }
        },
        
        analyse: function() {
            $.ajax({
                url: window.config.root + "/report/analyseSql",
                method: "POST",
                async: true,
                data: {
                    sql: this.state.sqlText,
                },
            }).then(function(data) {
                console.log(data)
                this.refs.columnEditor.setColumnNames(data);
                var parameterNames = this.parseParameters(this.state.sqlText);
                console.log(parameterNames);
                this.refs.parameterEditor.setParameterNames(parameterNames);
            }.bind(this), function(jqXHR, a, b) {
                console.log(jqXHR, a, b)
                alert(jqXHR.responseText);
            });

        },
        
        parseParameters: function(sql) {
            sql = sql.replace(/\\./g, '').replace(/".*?"/g, '').replace(/'.*?'/g, '');
            var parameterNames = [];
            var regexp = /:([0-9A-Za-z_]+)([^0-9A-Za-z_])/g;
            while (true) {
                var result = regexp.exec(sql);
                if (!result) {
                    break;
                }
                parameterNames.push(result[1]);
            }
            return parameterNames;
        },
        
        render: function() {
            var liStyle= {
                display: "inline-block",
                width: "90px",
                textAlign: "center",
                cursor: "pointer",
                border: "1px solid lightGray",
                marginRight: "-1px",
                zIndex: 1,
                position: "relative",
            };
            var activeStyle = $.extend(true, {}, liStyle, {borderBottom: "1px solid white", border: "1px solid black", cursor: "default", zIndex: 3});
            
            var lis = ["Report", "Columns", "Parameters"].map(function(item, i) {
                return <li key={i} 
                    onClick={this.changeTab.bind(this, item)} 
                    style={this.state.activeTab == item ? activeStyle : liStyle}>
                    {item}
                </li>
            }.bind(this));
            
            var buttons = <div></div>;
            if (this.props.showButtons) {
                buttons = <div>
                    <button onClick={this.analyse}>Analyse</button>
                    <button disabled={"disabled"}>Validate</button>
                    <button onClick={this.save}>Save</button>
                    <button disabled={"disabled"}>Close</button>
                </div>
            }
            
            return (
                <div>
                    <fieldset>
                        <legend>Report SQL</legend>
                        <textarea ref="sqlTextInput" 
                            onChange={this.updateSqlText} 
                            value={this.state.sqlText} 
                            style={{height: "200px", width: "100%", padding: 0}} />
                        </fieldset>
                    <fieldset>
                        <legend>Report Detail</legend>
                        <ul style={{margin: 0, padding: 0}}>{lis}</ul>
                        <div style={{width: "100%", minHeight: "200px", border: "1px solid black", marginTop: "-1px", zIndex: 2, position: "relative"}}>
                            <ReportEditor ref="reportEditor" display={this.state.activeTab == "Report"} report={this.props.report}/>
                            <ColumnEditor ref="columnEditor" display={this.state.activeTab == "Columns"} columns={this.props.columns}/>
                            <ParameterEditor ref="parameterEditor" display={this.state.activeTab == "Parameters"} parameters={this.props.parameters}/>
                        </div>
                    </fieldset>
                    {buttons}
                </div>
            );
        }
    });

    var queryString = function () {
        // This function is anonymous, is executed immediately and
        // the return value is assigned to QueryString!
        var query_string = {};
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i=0;i<vars.length;i++) {
            var pair = vars[i].split("=");
                // If first entry with this name
            if (typeof query_string[pair[0]] === "undefined") {
              query_string[pair[0]] = decodeURIComponent(pair[1]);
                // If second entry with this name
            } else if (typeof query_string[pair[0]] === "string") {
                var arr = [query_string[pair[0]], decodeURIComponent(pair[1])];
                query_string[pair[0]] = arr;
                // If third or later entry with this name
            } else {
                query_string[pair[0]].push(decodeURIComponent(pair[1]));
            }
        } 
        return query_string;
    }();
    
    var reportId = queryString.reportId;
    
    if (!reportId) {
        window.showMessage = window.showMessage || alert;
        window.showButtons = window.showButtons === void 0;
        window.editor = ReactDOM.render(
            <Editor report={{}} columns={[]} parameters={[]} showButtons={window.showButtons} />,
            document.getElementById("editorContainer")
        );
    } else {
        var report;
        var columns = [];
        var parameters = [];
        
        var reportLoaded = $.ajax({
            url: window.config.root + "/report/report/" + reportId,
            method: "GET",
            cache: false,
            async: true,
        }).done(function(data) {
            console.log(data);
            report = data;
        });
        
        var columnLoaded = $.ajax({
            url: window.config.root + "/report/report/" + reportId + "/columns",
            method: "GET",
            cache: false,
            async: true,
        }).done(function(data) {
            console.log(data);
            columns = data;
        });
        
        var parameterLoaded = $.ajax({
            url: window.config.root + "/report/report/" + reportId + "/parameters",
            method: "GET",
            cache: false,
            async: true,
        }).done(function(data) {
            console.log(data);
            parameters = data;
        });
        
        $.when(reportLoaded, columnLoaded, parameterLoaded).then(function() {
            window.showMessage = window.showMessage || alert;
            window.showButtons = window.showButtons === void 0;
            window.editor = ReactDOM.render(
                <Editor report={report} columns={columns} parameters={parameters} showButtons={window.showButtons} />,
                document.getElementById("editorContainer")
            );
        });
    }
    
})();
