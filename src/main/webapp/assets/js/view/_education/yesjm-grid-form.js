var fnObj = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        var paramObj = $.extend(caller.searchView.getData(), data);
        axboot.ajax({
            type: 'GET',
            url: '/api/v1/_education/yesjmgridform',
            data: paramObj,
            callback: function (res) {
                caller.gridView01.setData(res);
                caller.formView01.clear();
            },
            options: {
                // axboot.ajax 함수에 2번째 인자는 필수가 아닙니다. ajax의 옵션을 전달하고자 할때 사용합니다.
                onError: function (err) {
                    console.log(err);
                },
            },
        });
    },
    PAGE_SAVE: function (caller, act, data) {
        if (caller.formView01.validate()) {
            var item = caller.formView01.getData();

            var fileIds = [];
            var files = ax5.util.deepCopy(caller.formView01.UPLOAD.uploadedFiles);
            $.each(files, function (idx, o) {
                fileIds.push(o.id);
            });
            item.fileIdList = fileIds;

            if (!item.id) item.__created__ = true;
            axboot.ajax({
                type: 'POST',
                url: '/api/v1/_education/yesjmgridform',
                data: JSON.stringify(item),
                callback: function (res) {
                    ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                    axToast.push('저장 되었습니다');
                },
            });
        }
    },
    ITEM_CLICK: function (caller, act, data) {
        var id = (data || {}).id;
        if (!id) {
            axDialog.alert('id는 필수입니다.');
            return false;
        }
        axboot.ajax({
            type: 'GET',
            url: '/api/v1/_education/yesjmgridform/' + id,
            callback: function (res) {
                caller.formView01.setData(res);
            },
        });
    },
    ITEM_ADD: function (caller, act, data) {
        caller.formView01.initView();
        caller.gridView01.addRow();
    },
    PAGE_DELETE: function (caller, act, data) {
        axDialog.confirm({ msg: '삭제하시겠습니까?' }, function () {
            if (this.key == 'ok') {
                //ok버튼 누르면
                var items = caller.gridView01.getData('selected');
                if (!items.length) {
                    axDialog.alert('삭제할 데이터가 없습니다.');
                    return false;
                }
                var ids = items.map(function (value) {
                    //배열로 만들기
                    return value.id;
                });

                axboot.ajax({
                    type: 'DELETE',
                    url: '/api/v1/_education/yesjmgridform/' + ids.join(','), //join이 없어도 동작하는데 왜 사용하지?
                    callback: function (res) {
                        axToast.push('삭제 되었습니다');
                        ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                    },
                });
            }
        });
    },
    FORM_CLEAR: function (caller, act, data) {
        axDialog.confirm({ msg: LANG('ax.script.form.clearconfirm') }, function () {
            if (this.key == 'ok') {
                caller.formView01.clear();
                caller.formView01.initUploader();
                $('[data-ax-path="companyNm"]').focus(); //companyNm에 커서
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
    this.pageButtonView.initView();
    this.searchView.initView();
    this.gridView01.initView();
    this.formView01.initView({
        viewMode: false,
        editorReady: function () {},
    });

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
                ACTIONS.dispatch(ACTIONS.PAGE_DELETE);
            },
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
        this.target.attr('onsubmit', 'return false;');
        this.target.on('keydown.search', 'input, .form-control', function (e) {
            if (e.keyCode === 13) {
                //searchview의 항목들에서 enter누르면 검색
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
            }
        });

        this.useYn = $('.js-useYn').on('change', function () {
            //change 이벤트는 요소의 값이 변경될 때 사용됨.
            ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
        });
        this.filter = $('.js-filter');
    },
    getData: function () {
        return {
            pageNumber: this.pageNumber || 0,
            pageSize: this.pageSize || 50,
            useYn: this.useYn.val(),
            filter: this.filter.val(),
        };
    },
});

/**
 * gridView
 */
fnObj.gridView01 = axboot.viewExtend(axboot.gridView, {
    initView: function () {
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
                { key: 'bizno', label: COL('company.bizno'), width: 100, formatter: 'bizno', align: 'center' },
            ],
            body: {
                onClick: function () {
                    //grid.body.onClick.call({self: this, dindex:0, item: this.list[0]}); 내부적으로는 이렇게 생긴거임....?
                    this.self.select(this.dindex, { selectedClear: true }); //????? this.self라는 변수가 그리드 인스턴스
                    //별도의 정보를 클릭할때 파라미터를 던진다.
                    //가공된 this.... 아우 이해안된다..........
                    ACTIONS.dispatch(ACTIONS.ITEM_CLICK, this.item);
                },
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
        var _this = this;

        var remark = '';
        if (_this.editor) {
            remark = _this.editor.getData();
        }

        var data = this.modelFormatter.getClearData(this.model.get()); // 모델의 값을 포멧팅 전 값으로 치환.
        data.remark = remark;
        return $.extend({}, data);
    },
    setData: function (data) {
        if (typeof data === 'undefined') data = this.getDefaultData();
        data = $.extend({}, data);

        this.model.setModel(data);
        this.modelFormatter.formatting(); // 입력된 값을 포메팅 된 값으로 변경

        var _this = this;

        setTimeout(function () {
            _this.editor.setData(data.remark);
        }, 100);

        if (typeof data.fileList != 'undefined' && data.fileList.length > 0) {
            _this.UPLOAD.setUploadedFiles(data.fileList);
        }
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
    // clear: function () {},
    initEditor: function (obj) {
        var _this = this;
        var readOnly = obj.viewMode ? true : false;

        this.editor = CKEDITOR.replace('editor1', {
            filebrowserBrowseUrl: CONTEXT_PATH + '/ckeditor/fileBrowser?targetType=' + fnObj.formView01.getData().bbsId + '&targetId=' + UUID,
            filebrowserWindowWidth: '960',
            filebrowserWindowHeight: '600',
            imageUploadUrl: CONTEXT_PATH + '/ckeditor/uploadImage?targetType=' + fnObj.formView01.getData().bbsId + '&targetId=' + UUID,
            toolbarGroups: (function () {
                var viewmode_groups = [
                    { name: 'insert', groups: ['others', 'insert'] },
                    { name: 'tools', groups: ['tools'] },
                    { name: 'styles', groups: ['styles'] },
                ];

                var groups = [
                    { name: 'others', groups: ['others'] },
                    { name: 'insert', groups: ['insert'] },
                    { name: 'document', groups: ['mode', 'document', 'doctools'] },
                    { name: 'links', groups: ['links'] },
                    '/',
                    { name: 'basicstyles', groups: ['basicstyles', 'cleanup'] },
                    { name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align', 'bidi', 'paragraph'] },
                    '/',
                    { name: 'styles', groups: ['styles'] },
                    { name: 'colors', groups: ['colors'] },
                ];

                return readOnly ? viewmode_groups : groups;
            })(),
            readOnly: readOnly,
        });

        this.editor.once('instanceReady', function () {
            if (obj && obj.editorReady) {
                obj.editorReady();
            }
        });
        this.editor.on('notificationShow', function (evt) {
            evt.cancel();
        });
        this.editor.on('notificationUpdate', function (evt) {
            evt.cancel();
        });

        var form = '';
        if (!obj.viewMode) {
            CKEDITOR.instances.editor1.setData(form);
        }
    },
    initUploader: function () {
        var _this = this;
        _this.UPLOAD = new ax5.ui.uploader({
            //debug: true,
            target: $('[data-ax5uploader="upload1"]'),
            form: {
                action: '/api/v1/files/upload',
                fileName: 'file',
            },
            multiple: true,
            manualUpload: false,
            progressBox: true,
            progressBoxDirection: 'left',
            dropZone: {
                target: $('[data-uploaded-box="upload1"]'),
            },
            uploadedBox: {
                target: $('[data-uploaded-box="upload1"]'),
                icon: {
                    download: '<i class="cqc-download" aria-hidden="true"></i>',
                    delete: '<i class="cqc-minus" aria-hidden="true"></i>',
                },
                columnKeys: {
                    name: 'fileNm',
                    type: 'extension',
                    size: 'fileSize',
                    uploadedName: 'saveName',
                    uploadedPath: '',
                    downloadPath: '',
                    previewPath: '',
                    thumbnail: '',
                },
                lang: {
                    supportedHTML5_emptyListMsg: '<div class="text-center" style="padding-top: 30px;">첨부파일이 없습니다. </div>',
                    emptyListMsg: '<div class="text-center" style="padding-top: 30px;">Empty of List.</div>',
                },
                onchange: function () {},
                onclick: function () {
                    var fileIndex = this.fileIndex;
                    var file = this.uploadedFiles[fileIndex];

                    switch (this.cellType) {
                        case 'delete':
                            axDialog.confirm(
                                {
                                    theme: 'danger',
                                    msg: '삭제하시겠습니까?',
                                },
                                function () {
                                    if (this.key == 'ok') {
                                        $.ajax({
                                            contentType: 'application/json',
                                            method: 'get',
                                            url: '/api/v1/files/delete',
                                            /*
                                        data: JSON.stringify([{
                                            id: file.id
                                        }]),
                                        */
                                            data: { id: file.id },
                                            success: function (res) {
                                                if (res.error) {
                                                    alert(res.error.message);
                                                    return;
                                                }
                                                _this.UPLOAD.removeFile(fileIndex);
                                            },
                                        });
                                    }
                                }
                            );
                            break;

                        case 'download':
                            if (file.download) {
                                location.href = file.download;
                            }
                            break;
                    }
                },
            },
            validateSelectedFiles: function () {
                var limitCount = 5;
                if (this.uploadedFiles.length + this.selectedFiles.length > limitCount) {
                    alert('You can not upload more than ' + limitCount + ' files.');
                    return false;
                }
                return true;
            },
            onprogress: function () {},
            onuploaderror: function () {
                console.log(this.error);
                axDialog.alert(this.error.message);
            },
            onuploaded: function () {},
            onuploadComplete: function () {},
        });
    },
    initView: function (obj) {
        var _this = this; // this = fnObj.forView01

        _this.target = $('.js-form');

        this.model = new ax5.ui.binder();
        this.model.setModel(this.getDefaultData(), this.target);
        this.modelFormatter = new axboot.modelFormatter(this.model);

        this.initEditor(obj);
        this.initUploader();
        this.initEvent();
    },
});
