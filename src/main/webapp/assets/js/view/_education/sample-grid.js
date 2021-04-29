var fnObj = {};
var sampleData = [
    { key: 11, value: 'value11', etc1: 1000, etc2: 11 },
    { pid: '11', key: '12', value: 'value12', etc1: 2000, etc2: 12 },
    { pid: '11', key: '13', value: 'value13', etc1: 3000, etc2: 13 },
    { pid: '11', key: '14', value: 'value14', etc1: 4000, etc2: 14 },
    { pid: '14', key: '15', value: 'value15', etc1: 5000, etc2: 15 },
    { pid: '14', key: '16', value: 'value16', etc1: 6000, etc2: 16 },
    { pid: '11', key: '17', value: 'value17', etc1: 7000, etc2: 17 },
];
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        var paramObj = caller.searchView.getData();
        // console.log('paramObj: ', paramObj);
        // return;
        axboot.ajax({
            type: 'GET',
            url: '/api/v1/samples/parent/queryDsl',
            data: paramObj,
            callback: function (res) {
                caller.gridView01.setData(res);
                caller.gridView02.setData(sampleData);
            },
        });

        return false;
    },
    PAGE_SAVE: function (caller, act, data) {
        var saveList = [].concat(caller.gridView01.getData('modified'));
        saveList = saveList.concat(caller.gridView01.getData('deleted'));

        // var saveList = {grid: saveList1, tree: saveList1}
        axboot.ajax({
            type: 'PUT',
            url: ['samples', 'parent'],
            data: JSON.stringify(saveList),
            callback: function (res) {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                // axToast.push(LANG('ax.script.save'));
            },
        });
    },
    ITEM_CLICK: function (caller, act, data) {},
    ITEM_ADD: function (caller, act, data) {
        caller.gridView01.addRow();
        // caller.gridView02.addRow();
    },
    ITEM_DEL: function (caller, act, data) {
        caller.gridView01.delRow('selected');
        // caller.gridView02.delRow('selected');
    },
});

// fnObj 기본 함수 스타트와 리사이즈
fnObj.pageStart = function () {
    this.pageButtonView.initView();
    this.searchView.initView();
    this.gridView01.initView();
    this.gridView02.initView();

    ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
};

fnObj.pageResize = function () {};

fnObj.pageButtonView = axboot.viewExtend({
    initView: function () {
        axboot.buttonClick(this, 'data-page-btn', {
            search: function () {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
            },
            save: function () {
                ACTIONS.dispatch(ACTIONS.PAGE_SAVE);
            },
            excel: function () {},
        });
    },
});

//== view 시작
/**
 * searchView
 */
fnObj.searchView = axboot.viewExtend(axboot.searchView, {
    initView: function () {
        this.target = $(document['searchView0']);
        this.target.attr('onsubmit', 'return ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);');
        this.filter = $('#filter');
        this.key = $('#key');
        this.value = $('#value');
        this.useYn = $('#useYn');
    },
    getData: function () {
        return {
            pageNumber: this.pageNumber,
            pageSize: this.pageSize,
            filter: this.filter.val(),
            key: this.key.val(),
            value: this.value.val(),
            useYn: this.useYn.val(),
        };
    },
});

fnObj.selectItems = [
    { value: 'Y', text: '사용' },
    { value: 'N', text: '미사용' },
];

/**
 * gridView
 */
fnObj.gridView01 = axboot.viewExtend(axboot.gridView, {
    initView: function () {
        var _this = this;

        this.target = axboot.gridBuilder({
            showRowSelector: true,
            frozenColumnIndex: 0,
            multipleSelect: true,
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                { key: 'key', label: 'KEY', width: 160, align: 'left', editor: 'text' },
                {
                    key: undefined,
                    label: 'VALUE',
                    align: 'center',
                    columns: [
                        { key: 'value', label: 'display', align: 'left' },
                        { key: 'value', label: 'textarea', align: 'left', editor: { type: 'textarea' } },
                    ],
                },
                {
                    key: undefined,
                    label: 'etc',
                    align: 'center',
                    columns: [
                        { key: 'etc1', label: 'money', width: 100, align: 'center', editor: { type: 'money' } },
                        { key: 'etc2', label: 'number', width: 100, align: 'center', editor: { type: 'number' } },
                        {
                            key: 'cost',
                            label: 'cost',
                            width: 100,
                            align: 'center',
                            formatter: function () {
                                var etc1 = this.item.etc1 || 0;
                                var etc2 = this.item.etc2 || 0;
                                return ax5.util.number(etc1 * etc2, { money: true });
                            },
                        },
                        { key: 'etc3', label: 'date', width: 100, align: 'center', editor: { type: 'date' } },
                    ],
                },
                {
                    key: 'etc4',
                    label: 'select',
                    width: 100,
                    align: 'center',
                    formatter: function () {
                        var i = 0,
                            len = fnObj.selectItems.length,
                            value;
                        for (; i < len; i++) {
                            if (this.item.etc4 === (value = fnObj.selectItems[i].value)) {
                                break;
                            }
                        }
                        return value === 'Y' ? '사용' : '미사용';
                    },

                    editor: { type: 'select', config: { columnsKeys: { optionValue: 'value', optionText: 'text' }, options: fnObj.selectItems } },
                },
                {
                    key: 'etc4',
                    label: 'checkbox',
                    width: 100,
                    align: 'center',
                    editor: { type: 'checkbox', config: { trueValue: 'Y', falseValue: 'N' } },
                },
            ],
            body: {
                mergeCells: ['value'],
                onClick: function () {
                    this.self.select(this.dindex, { selectedClear: true });
                },
                grouping: {
                    by: ['value'],
                    columns: [
                        {
                            label: function () {
                                return this.groupBy.labels.join(', ') + ' SUM';
                            },
                            colspan: 3,
                            align: 'center',
                        },
                        { key: 'etc1', collector: 'avg', formatter: 'money', align: 'right' },
                        { key: 'etc2', collector: 'sum', formatter: 'money', align: 'right' },
                        {
                            key: 'cost',
                            collector: function () {
                                var value = 0;
                                this.list.forEach(function (n) {
                                    if (!n.__isGrouping) value += (n.etc1 || 0) * (n.etc2 || 0);
                                });
                                return ax5.util.number(value, { money: 1 });
                            },
                            align: 'right',
                        },
                    ],
                },
            },
            footSum: [
                [
                    { label: 'SUMMARY', colspan: 3, align: 'center' },
                    { key: 'etc1', collector: 'avg', formatter: 'money', align: 'right' },
                    { key: 'etc2', collector: 'sum', formatter: 'money', align: 'right' },
                    {
                        key: 'cost',
                        collector: function () {
                            var value = 0;
                            this.list.forEach(function (n) {
                                if (!n.__isGrouping) value += (n.etc1 || 0) * (n.etc2 || 0);
                            });
                            return ax5.util.number(value, { money: 1 });
                        },
                        align: 'right',
                    },
                ],
            ],
        });

        axboot.buttonClick(this, 'data-grid-view-01-btn', {
            add: function () {
                ACTIONS.dispatch(ACTIONS.ITEM_ADD);
            },
            delete: function () {
                ACTIONS.dispatch(ACTIONS.ITEM_DEL);
            },
        });
    },
    getData: function (_type) {
        var list = [];
        var _list = this.target.getList(_type);

        if (_type == 'modified' || _type == 'deleted') {
            list = ax5.util.filter(_list, function () {
                delete this.deleted;
                return this.key;
            });
        } else {
            list = _list;
        }
        return list;
    },
    addRow: function () {
        this.target.addRow({ __created__: true }, 'last');
    },
});

/**
 * gridView02
 */
fnObj.gridView02 = axboot.viewExtend(axboot.gridView, {
    initView: function () {
        var _this = this;

        this.target = axboot.gridBuilder({
            showLineNumber: false,
            showRowSelector: true,
            multipleSelect: true,
            target: $('[data-ax5grid="grid-view-02"]'),
            columns: [
                { key: 'key', label: 'KEY', width: 160, align: 'left', editor: 'text' },
                { key: 'value', label: 'VALUE', align: 'left', treeControl: true },
                {
                    key: undefined,
                    label: 'etc',
                    align: 'center',
                    columns: [
                        { key: 'etc1', label: 'money', width: 100, align: 'center', editor: { type: 'money' } },
                        { key: 'etc2', label: 'number', width: 100, align: 'center', editor: { type: 'number' } },
                        {
                            key: 'cost',
                            label: 'cost',
                            width: 100,
                            align: 'center',
                            formatter: function () {
                                var etc1 = this.item.etc1 || 0;
                                var etc2 = this.item.etc2 || 0;
                                return ax5.util.number(etc1 * etc2, { money: true });
                            },
                        },
                        { key: 'etc3', label: 'date', width: 100, align: 'center', editor: { type: 'date' } },
                    ],
                },
                {
                    key: 'etc4',
                    label: 'select',
                    width: 100,
                    align: 'center',
                    formatter: function () {
                        var i = 0,
                            len = fnObj.selectItems.length,
                            value;
                        for (; i < len; i++) {
                            if (this.item.etc4 === (value = fnObj.selectItems[i].value)) {
                                break;
                            }
                        }
                        return value === 'Y' ? '사용' : '미사용';
                    },

                    editor: { type: 'select', config: { columnsKeys: { optionValue: 'value', optionText: 'text' }, options: fnObj.selectItems } },
                },
                {
                    key: 'etc4',
                    label: 'checkbox',
                    width: 100,
                    align: 'center',
                    editor: { type: 'checkbox', config: { trueValue: 'Y', falseValue: 'N' } },
                },
            ],
            body: {
                onClick: function () {
                    this.self.select(this.dindex);
                    ACTIONS.dispatch(ACTIONS.ITEM_CLICK, this.list[this.dindex]);
                },
            },

            tree: {
                use: true,
                indentWidth: 10,
                arrowWidth: 15,
                iconWidth: 18,
                icons: {
                    openedArrow: '<i class="cqc-uni3E" aria-hidden="true"></i>',
                    collapsedArrow: '<i class="cqc-uni76" aria-hidden="true"></i>',
                    groupIcon: '<i class="cqc-folder" aria-hidden="true"></i>',
                    collapsedGroupIcon: '<i class="cqc-folder" aria-hidden="true"></i>',
                    itemIcon: '<i class="cqc-uni4F" aria-hidden="true"></i>',
                },
                columnKeys: {
                    parentKey: 'pid',
                    selfKey: 'key',
                },
            },
        });

        axboot.buttonClick(this, 'data-grid-view-02-btn', {
            'item-add': function () {
                this.addRow();
            },
            'item-remove': function () {
                this.delRow();
            },
        });
    },
    setData: function (_data) {
        this.target.setData(_data);
    },
    getData: function (_type) {
        var list = [];
        var _list = this.target.getList(_type);

        if (_type == 'modified' || _type == 'deleted') {
            list = ax5.util.filter(_list, function () {
                return this.key;
            });
        } else {
            list = _list;
        }
        return list;
    },
    align: function () {
        this.target.align();
    },
});
