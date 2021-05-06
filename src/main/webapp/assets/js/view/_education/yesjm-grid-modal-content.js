var modalParams = modalParams || {};
var fnObj = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    MODAL_CLOSE: function (caller, act, data) {
        parent.axboot.modal.close();
    },
    PAGE_SEARCH: function (caller, act, data) {
        if (!modalParams.id) return false;
        axboot.ajax({
            type: 'GET',
            url: '/api/v1/_education/yesjmgridform/' + modalParams.id,
            callback: function (res) {
                caller.formView01.setData(res);
            },
        });
    },
    PAGE_SAVE: function (caller, act, data) {
        if (caller.formView01.validate()) {
            var item = caller.formView01.getData();
            if (!item.id) item.__created__ = true;
            axboot.ajax({
                type: 'POST',
                url: '/api/v1/_education/yesjmgridform',
                data: JSON.stringify(item),
                callback: function (res) {
                    // parent.axboot.modal.callback({ dirty: true });
                    axDialog.alert('저장 되었습니다', function () {
                        if (parent && parent.axboot && parent.axboot.modal) {
                            parent.axboot.modal.callback({ dirty: true });
                        }
                    });
                },
            });
        }
    },
    PAGE_DELETE: function (caller, act, data) {
        if (!modalParams.id) return false;
        axboot.ajax({
            type: 'DELETE',
            url: '/api/v1/_education/yesjmgridform/' + modalParams.id,
            callback: function (res) {
                axDialog.alert('삭제 되었습니다', function () {
                    if (parent && parent.axboot && parent.axboot.modal) {
                        parent.axboot.modal.callback({ dirty: true });
                    }
                });
            },
        });
    },
    FORM_CLEAR: function (caller, act, data) {
        axDialog.confirm({ msg: LANG('ax.script.form.clearconfirm') }, function () {
            if (this.key == 'ok') {
                caller.formView01.clear();
                $('[data-ax-path="companyNm"]').focus();
            }
        });
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
    var _this = this;
    _this.pageButtonView.initView();
    _this.formView01.initView();

    if (!modalParams.id) {
        $('[data-page-btn="delete"]').prop('disabled', true);
    } else ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
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
            close: function () {
                ACTIONS.dispatch(ACTIONS.MODAL_CLOSE);
            },
            delete: function () {
                ACTIONS.dispatch(ACTIONS.PAGE_DELETE);
            },
        });
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
        // this.model.get(); //formView 영역의 모든 데이터를 json형식으로 가져오는건가?
        var data = this.modelFormatter.getClearData(this.model.get()); //formatter양식 지우고 기본 데이터 형식으로 가져옴
        return $.extend({}, data);
    },
    setData: function (data) {
        if (typeof data === 'undefined') data = this.getDefaultData();
        data = $.extend({}, data);

        this.model.setModel(data);
        this.modelFormatter.formatting();
    },
    validate: function () {
        var item = this.model.get();

        var rs = this.model.validate();
        if (rs.error) {
            axDialog.alert(LANG('ax.script.form.validate', rs.error[0].jquery.attr('title')), function () {
                rs.error[0].jquery.focus();
            });
            return false;
        }

        var pattern;
        if (item.email) {
            //a~9사이로 시작해서 -_.포함가능하고a~9사이 다음 '@'만나고 a~9사이 '.' 만난다음 a~9사이 2개 이상
            pattern = /^[A-Za-z0-9]([-_.]?[A-Za-z0-9])*@[A-Za-z0-9]([-_.]?[A-Za-z0-9])*\.(?:[A-Za-z0-9]{2,}?)$/i;
            if (!pattern.test(item.email)) {
                axDialog.alert('이메일 형식을 확인하세요.', function () {
                    $('[data-ax-path="email"]').focus();
                });
                return false;
            }
        }
        //0~9사이 3개 '-' 0~9사이 2개 '-' 0~9사이 5개
        if (item.bizno && !(pattern = /^([0-9]{3})\-?([0-9]{2})\-?([0-9]{5})$/).test(item.bizno)) {
            axDialog.alert('사업자번호 형식을 확인하세요.'),
                function () {
                    $('[data-ax-path="bizno"]').focus();
                };
            return false;
        }
        return true;
    },
    initEvent: function () {
        axboot.buttonClick(this, 'data-form-view-01-btn', {
            'form-clear': function () {
                ACTIONS.dispatch(ACTIONS.FORM_CLEAR);
            },
        });
    },
    initView: function () {
        var _this = this; // this = fnObj.forView01

        _this.target = $('.js-form');

        this.model = new ax5.ui.binder();
        this.model.setModel(this.getDefaultData(), this.target);
        this.modelFormatter = new axboot.modelFormatter(this.model);

        this.initEvent();
    },
});
