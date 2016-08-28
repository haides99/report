(function () {
    var Datatable = React.createClass({
        getInitialState: function () {
            return {
                pageIndex: 0,
                pageSize: 10,
                orderColumn: 0,
                orderDir: 'asc',
                data: [],
                totalDisplayRecords: '-'
            };
        },

        update: function () {
            this.lastQuery && this.lastQuery.abort();
            this.lastQuery = $.ajax({
                url: this.props.url,
                method: 'POST',
                async: true,
                data: {
                    sEcho: 0,
                    sColumns: this.columnNames,
                    iDisplayStart: this.state.pageSize * this.state.pageIndex,
                    iDisplayLength: this.state.pageSize,
                    iSortCol_0: this.state.orderColumn,
                    sSortDir_0: this.state.orderDir,
                    queryProjectTeam: '',
                    queryUser: ''
                }
            }).done(function (data) {
                this.setState({
                    data: data.aaData,
                    totalDisplayRecords: data.iTotalDisplayRecords
                });
            }.bind(this));
        },

        onReorder: function (i, dir) {
            this.setState({orderColumn: i, orderDir: dir}, function () {
                this.update();
            }.bind(this));
        },

        changeTableState: function (newState) {
            this.setState(newState, function () {
                this.update();
            }.bind(this));
        },

        componentWillMount: function () {
            var columnNames = [];
            this.props.columns.map(function (column) {
                columnNames.push(column.name);
            });
            this.columnNames = columnNames.join(',');
        },

        componentDidMount: function () {
            this.update();
        },

        shouldComponentUpdate: function (nextProps, nextState) {
            return !(this.state.data === nextState.data);
        },

        render: function () {
            return <div>
                <table>
                    <Thead orderColumn={this.state.orderColumn}
                           orderDir={this.state.orderDir}
                           columns={this.props.columns}
                           changeTableState={this.changeTableState}/>
                    <Tbody data={this.state.data} columns={this.props.columns}/>
                </table>
                <Footer pageIndex={this.state.pageIndex}
                        pageSize={this.state.pageSize}
                        totalDisplayRecords={this.state.totalDisplayRecords}
                        changeTableState={this.changeTableState}/>
            </div>
        }
    });

    var Th = React.createClass({
        reorder: function (dir, e) {
            e.stopPropagation();
            this.props.changeTableState({
                orderColumn: dir == 'none' ? null : this.props.index,
                orderDir: dir
            });
        },
        render: function () {
            var getStyle = function (dir) {
                return {
                    color: this.props.ordering && this.props.orderDir === dir ? 'black' : '#ccc'
                }
            }.bind(this);
            var buttons = this.props.column.sortable === false ? '' :
                [<button key={0} onClick={this.reorder.bind(this, 'asc')}
                         style={getStyle("asc")}>↑</button>,
                    <button key={1} onClick={this.reorder.bind(this, 'desc')}
                            style={getStyle("desc")}>↓</button>,
                        <button key={2} onClick={this.reorder.bind(this, 'none')}
                            	style={getStyle("none")}>∅</button>];
            return <th>
                {this.props.column.title}
                {buttons}
            </th>
        }
    });

    var Thead = React.createClass({
        render: function () {
            var ths = this.props.columns.map(function (data, i) {
                var ordering = this.props.orderColumn === i, orderDir = null;
                ordering && (orderDir = this.props.orderDir);
                return <Th key={i}
                           column={data}
                           index={i}
                           ordering={ordering}
                           orderDir={orderDir}
                           changeTableState={this.props.changeTableState}/>
            }.bind(this));
            return <thead><tr>{ths}</tr></thead>
        }
    });

    var Tbody = React.createClass({
        render: function () {
            var component = this;
            var columns = this.props.columns;
            var trs = this.props.data.map(function (rowData, i) {
                var tds = rowData.map(function (item, i) {
                    var rendered = columns[i].render ? columns[i].render.call(rowData, item) : item;
                    return <td key={i}>{rendered}</td>
                });
                return <tr key={i}>{tds}</tr>
            });
            return <tbody>{trs}</tbody>
        }
    });

    var Footer = React.createClass({
        changeSize: function (e) {
            e.stopPropagation();
            var pageIndex = this.props.pageIndex;
            var pageSize = this.refs.pageSize.value;
            this.totalPage = Math.ceil(this.props.totalDisplayRecords / pageSize);
            if (pageIndex < 0) {
                pageIndex = 0;
            } else if (pageIndex > this.totalPage - 1) {
                pageIndex = this.totalPage - 1;
            }
            this.props.changeTableState({
                pageIndex: pageIndex,
                pageSize: pageSize
            });
        },

        previusPage: function (e) {
            e.stopPropagation();
            if (this.props.pageIndex > 0) {
                this.props.changeTableState({pageIndex: this.props.pageIndex - 1});
            }
        },

        naxtPage: function (e) {
            e.stopPropagation();
            if (this.props.pageIndex < this.totalPage - 1) {
                this.props.changeTableState({pageIndex: this.props.pageIndex + 1});
            }
        },

        jumpPage: function (target) {
            if (target < 0) {
                target = 0;
            } else if (target > this.totalPage - 1) {
                target = this.totalPage - 1;
            }
            this.props.changeTableState({pageIndex: target});
        },

        buttonJump: function (target, e) {
            e.stopPropagation();
            this.jumpPage(target);
        },

        inputJump: function (e) {
            e.stopPropagation();
            this.jumpPage(this.refs.pageJump.value - 1);
        },

        render: function () {
            var totalPage = this.totalPage = Math.ceil(this.props.totalDisplayRecords / this.props.pageSize);
            var buttons = [];
            var start = this.props.pageIndex;
            for (var i = this.props.pageIndex - 2; i < this.props.pageIndex + 3; i++) {
                if (i < 0 || i > this.totalPage - 1) continue;
                var style = {color: i === this.props.pageIndex ? 'gray' : 'green'}
                buttons.push(<button key={i} style={style}
                                     onClick={this.buttonJump.bind(this, i)}>{i + 1}</button>);
            }
            return <div>
                <select onChange={this.changeSize} defaultValue="10"
                        ref="pageSize">
                    <option value="10">10</option>
                    <option value="20">20</option>
                    <option value="30">30</option>
                </select>
                共{this.props.totalDisplayRecords}条/{totalPage}页
                <button onClick={this.previusPage}>上一页</button>
                {buttons}
                <button onClick={this.naxtPage}>下一页</button>
                <input type="text" ref="pageJump" placeholder="输入跳转页"/>
                <button onClick={this.inputJump}>确定</button>
            </div>
        }
    });

    window.Datatable = Datatable;
})();
