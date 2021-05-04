<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ax" tagdir="/WEB-INF/tags" %>

<ax:set key="title" value="${pageName}"/>
<ax:set key="page_desc" value="${PAGE_REMARK}"/>
<ax:set key="page_auto_height" value="true"/>

<ax:layout name="base">
    <jsp:attribute name="script">
        <ax:script-lang key="ax.script" var="LANG" />
        <ax:script-lang key="ax.base" var="COL" />    
        <script type="text/javascript" src="<c:url value='/assets/js/view/_education/yesjm-grid-form.js' />"></script>
    </jsp:attribute>
    <jsp:body>

        <ax:page-buttons></ax:page-buttons>

        <div role="page-header">
            <ax:form name="searchView0">
                <ax:tbl clazz="ax-search-tbl" minWidth="500px">
                    <ax:tr>
                        <ax:td label='ax.base.use.or.not' width="200px">
                            <ax:common-code groupCd="USE_YN" clazz="js-useYn" emptyText="전체" />
                        </ax:td>
                        <ax:td label='ax.admin.search' width="300px">
                            <input type="text" name="filter" class="js-filter form-control" placeholder="회사명 / 대표자 / 사업자번호" />
                        </ax:td>
                    </ax:tr>
                </ax:tbl>
            </ax:form>
            <div class="H10"></div>
        </div>
​
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-5">
                    <!-- 목록 -->
                    <div class="ax-button-group" data-fit-height-aside="grid-view-01">
                        <div class="left">
                            <h2><i class="cqc-list"></i>
                                프로그램 목록 </h2>
                        </div>
                        <!-- <div class="right"> -->
                            <!-- <button type="button" class="btn btn-default" data-grid-view-01-btn="add"><i class="cqc-circle-with-plus"></i> 추가</button> -->
                            <!-- <button type="button" class="btn btn-default" data-grid-view-01-btn="delete"><i class="cqc-circle-with-plus"></i> 삭제</button> -->
                        <!-- </div> -->
                    </div>
                    <div data-ax5grid="grid-view-01" data-fit-height-content="grid-view-01" style="height: 300px;"></div>
                </div>
                <div class="col-md-7">
                    <div class="ax-button-group" data-fit-height-aside="form-view-01">
                        <div class="left">
                            <h2><i class="cqc-list"></i>
                                상세정보 </h2>
                        </div>
                        <div class="right">
                            <button type="button" class="btn btn-default" data-form-view-01-btn="form-clear">
                                <i class="cqc-erase"></i> <ax:lang id="ax.admin.clear"/>
                            </button>
                        </div>
                    </div>
                    <form name="form" class="js-form">
                        <div data-ax-tbl="" class="ax-form-tbl" style="">
                            <div data-ax-tr>
                                <div data-ax-td style="width:50%">
                                    <div data-ax-td-label style="width:120px">ID</div>
                                    <div data-ax-td-wrap>
                                        <input type="text" data-ax-path="id" class="form-control" value="" readonly="readonly">
                                    </div>
                                </div>
                                <div data-ax-td style="width:50%">
                                    <div data-ax-td-label style="width:120px">사용여부</div>
                                    <div data-ax-td-wrap>
                                        <ax:common-code groupCd="USE_YN" dataPath="useYn" />
                                    </div>
                                </div>
                            </div>
                            <div data-ax-tr>
                                <div data-ax-td style="width:50%">
                                    <div data-ax-td-label style="width:120px;">회사명</div>
                                    <div data-ax-td-wrap>
                                        <input type="text" name="companyNm" data-ax-path="companyNm" title="회사명" class="form-control" data-ax-validate="required" />
                                    </div>
                                </div>
                                <div data-ax-td style="width:50%">
                                    <div data-ax-td-label style="width:120px;">대표자</div>
                                    <div data-ax-td-wrap>
                                        <input type="text" name="ceo" data-ax-path="ceo" class="form-control" />
                                    </div>
                                </div>
                            </div>

                            <div data-ax-tr>
                                <div data-ax-td style="width:50%">
                                    <div data-ax-td-label style="width:120px;">사업자번호</div>
                                    <div data-ax-td-wrap>
                                        <input type="text" name="bizno" data-ax-path="bizno" title="사업자번호" data-ax5formatter="bizno" class="form-control" placeholder="000-00-00000" />
                                    </div>
                                </div>
                                <div data-ax-td style="width:50%">
                                    <div data-ax-td-label style="width:120px;">전화번호</div>
                                    <div data-ax-td-wrap>
                                        <input type="text" name="tel" data-ax-path="tel" class="form-control" />
                                    </div>
                                </div>
                            </div>

                            <div data-ax-tr>
                                <div data-ax-td style="width:50%">
                                    <div data-ax-td-label style="width:120px;">이메일</div>
                                    <div data-ax-td-wrap>
                                        <input type="text" name="email" data-ax-path="email" class="form-control" placeholder="x@x.xx" />
                                    </div>
                                </div>
                                <div data-ax-td style="width:50%">
                                    <div data-ax-td-label style="width:120px;">우편번호</div>
                                    <div data-ax-td-wrap>
                                        <input type="text" name="zip" data-ax-path="zip" class="form-control" />
                                    </div>
                                </div>
                            </div>

                            <div data-ax-tr>
                                <div data-ax-td style="width:100%">
                                    <div data-ax-td-label style="width:120px;">주소</div>
                                    <div data-ax-td-wrap>
                                        <input type="text" name="address" data-ax-path="address" class="form-control" />
                                    </div>
                                </div>
                            </div>

                            <div data-ax-tr>
                                <div data-ax-td style="width:100%">
                                    <div data-ax-td-label style="width:120px;">상세 주소</div>
                                    <div data-ax-td-wrap>
                                        <input type="text" name="addressDetail" data-ax-path="addressDetail" class="form-control" />
                                    </div>
                                </div>
                            </div>

                            <div data-ax-tr>
                                <div data-ax-td style="width:100%">
                                    <div data-ax-td-label style="width:120px;">비고</div>
                                    <div data-ax-td-wrap>
                                        <textarea name="remark" data-ax-path="remark" rows="5" class="form-control"></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
​
    </jsp:body>
</ax:layout>