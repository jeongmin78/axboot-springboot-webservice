var fnObj = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        var paramObj = $.extend(caller.searchView.getData(), data, { pageSize: 4 });
        axboot.ajax({
            type: 'GET',
            url: '/api/v1/_education/yesjmgridform/pages',
            data: paramObj,
            callback: function (res) {
                caller.gridView01.setData(res);
                caller.formView01.initView();
            },
            options: {
                // axboot.ajax 함수에 2번째 인자는 필수가 아닙니다. ajax의 옵션을 전달하고자 할때 사용합니다.
                onError: function (err) {
                    console.log(err);
                },
            },
        });

        return false;
    },
    PAGE_SAVE: function (caller, act, data) {
        var item = [].concat(caller.formView01.getData());
        axboot.ajax({
            type: 'POST',
            url: '/api/v1/_education/yesjmgridform',
            data: JSON.stringify(item),
            dataType: 'application/json',
            contentType: 'application/json;charset=UTF-8',
            callback: function (res) {
                console.log(item);
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                axToast.push('저장 되었습니다');
            },
        });
    },
    ITEM_CLICK: function (caller, act, data) {},
    ITEM_ADD: function (caller, act, data) {
        caller.formView01.initView();
        caller.gridView01.addRow();
    },
    ITEM_DEL: function (caller, act, data) {
        caller.gridView01.delRow('selected');
        // fnObj.formView01.getData();
    },

    dispatch: function (caller, act, data) {
        var result = ACTIONS.exec(caller, act, data);
        if (result != 'error') {
            return result;
        } else {
            // 직접코딩
            return false;
        }
    },
});

// fnObj 기본 함수 스타트와 리사이즈
fnObj.pageStart = function () {
    this.pageButtonView.initView();
    this.searchView.initView();
    this.gridView01.initView();
    this.formView01.initView();

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
            fn1: function () {
                ACTIONS.dispatch(ACTIONS.ITEM_DEL);
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
        this.companyNm = $('.js-companyNm');
        this.ceo = $('.js-ceo');
        this.bizno = $('.js-bizno');
        this.useYn = $('.js-useYn');
        // this.useYnTag = $('.js-useYn-tag');
    },
    getData: function () {
        return {
            id: this.id,
            pageType: this.pageType,
            pageNumber: this.pageNumber || 0,
            pageSize: this.pageSize || 0,
            filter: this.filter.val(),
            companyNm: this.companyNm.val(),
            ceo: this.ceo.val(),
            bizno: this.bizno.val(),
            useYn: this.useYn.val(),
            // useYnTag: this.useYnTag.val(),
        };
    },
});

/**
 * gridView
 */
fnObj.gridView01 = axboot.viewExtend(axboot.gridView, {
    initView: function () {
        var _this = this;

        this.target = axboot.gridBuilder({
            onPageChange: function (pageNumber) {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH, { pageNumber: pageNumber });
            },
            showRowSelector: true,
            frozenColumnIndex: 0,
            multipleSelect: true,
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                { key: 'companyNm', label: COL('company.name'), width: 120, align: 'left', editor: 'text' },
                { key: 'ceo', label: COL('company.ceo'), width: 80, align: 'center', editor: 'text' },
                { key: 'useYn', label: COL('use.or.not'), align: 'center', editor: 'text' },
            ],
            body: {
                onClick: function () {
                    this.self.select(this.dindex, { selectedClear: true });
                    fnObj.formView01.setData(this.item);
                },
            },
        });

        axboot.buttonClick(this, 'data-grid-view-01-btn', {
            // add: function () {
            //     ACTIONS.dispatch(ACTIONS.ITEM_ADD);
            // },
            // delete: function () {
            //     ACTIONS.dispatch(ACTIONS.ITEM_DEL);
            // },
        });
    },
    getData: function (_type) {
        var list = [];
        var _list = this.target.getList(_type);

        if (_type == 'modified' || _type == 'deleted') {
            list = ax5.util.filter(_list, function () {
                return this.id;
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
 * formView
 */
fnObj.formView01 = axboot.viewExtend(axboot.formView, {
    //뷰 영역에 폼뷰를 바인딩해서 인잇 뷰를 만들고
    getDefaultData: function () {
        return { useYn: 'Y' };
    },
    getData: function () {
        var item = {};
        this.target.find('input,select').each(function (i, elem) {
            //each : 찾은 객체를 for문 해라
            // var $elem = $(elem)); //아래와 동일함
            var $elem = $(this); //jquery 객체를 컨트롤할 거니까 $표시해주고
            var name = $elem.data('axPath'); //jsp에서 받은 데이터 이름이랑 값을 item배열에 매핑
            var value = $elem.val() || '';
            item[name] = value;
        });
    },
    setData: function (item) {
        var value;
        for (var prop in item) {
            value = item[prop] || '';
            $('[data-ax-path="' + prop + '"]').val(value);
        }
        //그리드에서 선택한 항목의 데이터를 폼에 바인딩하기
        console.log(item);
    },
    initView: function () {
        //선언부. pageStart가 호출하면 초기화됨.
        var _this = this; // == fnObj.forView01
        // 원래 this는 window를 가리키는데, 이 안에서 function을 정의하면 자기 스코프 최상위 객체 fnobj.forview01을 가리키는것이 된다.
        // _this를 정의한 이유는 setTimeout 같은 새로운 프레임을 정의하게되면 this는 window로 바뀌기 때문에
        // 항상 fnObj.formView01를 가리키도록 하기위해 _this를 정의해놓는것이다.

        _this.target = $('.js-form');
        _this.model = new ax5.ui.binder();
        _this.model.setModel({}, _this.target);

        console.log(_this.model.get());

        axboot.buttonClick(this, 'data-form-view-01-btn', {
            add: function () {
                ACTIONS.dispatch(ACTIONS.ITEM_ADD);
                fnObj.gridView01.setData(this.item);
            },
            // delete: function () {
            //     ACTIONS.dispatch(ACTIONS.ITEM_DEL);
            // },
        });
    },
});
