-- 실습용 그리드 폼 추가 ########################################

INSERT INTO MENU_M (MENU_ID, MENU_GRP_CD, MENU_NM, MULTI_LANGUAGE, PARENT_ID, LEVEL, SORT, PROG_CD, CREATED_AT, CREATED_BY, UPDATED_AT, UPDATED_BY )
VALUES (300, 'SYSTEM_MANAGER', '정민 샘플', '{"ko":"정민 샘플","en":"정민 샘플"}',  null, 0, 99, null, sysdate(), 'system', sysdate(), 'system');
-- 실습용 그리드 추가
-- 프로그램 생성
INSERT INTO PROG_M (PROG_CD, PROG_NM, PROG_PH, TARGET, AUTH_CHECK, SCH_AH, SAV_AH )
VALUES ('yesjm-grid', '실습용 그리드', '/jsp/_education/yesjm-grid.jsp', '_self', 'Y', 'Y', 'Y');
-- 메뉴 생성
INSERT INTO MENU_M (MENU_ID, MENU_GRP_CD, MENU_NM, MULTI_LANGUAGE, PARENT_ID, LEVEL, SORT, PROG_CD, CREATED_AT, CREATED_BY, UPDATED_AT, UPDATED_BY )
VALUES (301, 'SYSTEM_MANAGER', '실습용 그리드', '{"ko":"실습용 그리드","en":"실습용 그리드"}',  300, 1, 1, 'yesjm-grid', sysdate(), 'system', sysdate(), 'system');
-- 메뉴 권한
INSERT INTO AUTH_GROUP_MAP_M (GRP_AUTH_CD , MENU_ID, SCH_AH, SAV_AH, CREATED_AT, CREATED_BY, UPDATED_AT, UPDATED_BY )
VALUES ('S0001', '301',  'Y', 'Y', sysdate(), 'system', sysdate(), 'system');

-- 프로그램 생성
INSERT INTO PROG_M (PROG_CD, PROG_NM, PROG_PH, TARGET, AUTH_CHECK, SCH_AH, SAV_AH, DEL_AH )
VALUES ('yesjm-grid-form', '실습용 그리드 폼', '/jsp/_education/yesjm-grid-form.jsp', '_self', 'Y', 'Y', 'Y', 'Y');
-- 메뉴 생성
INSERT INTO MENU_M (MENU_ID, MENU_GRP_CD, MENU_NM, MULTI_LANGUAGE, PARENT_ID, LEVEL, SORT, PROG_CD, CREATED_AT, CREATED_BY, UPDATED_AT, UPDATED_BY )
VALUES (302, 'SYSTEM_MANAGER', '실습용 그리드 폼', '{"ko":"실습용 그리드 폼","en":"실습용 그리드 폼"}',  300, 1, 2, 'yesjm-grid-form', sysdate(), 'system', sysdate(), 'system');
-- 메뉴 권한
INSERT INTO AUTH_GROUP_MAP_M (GRP_AUTH_CD , MENU_ID, SCH_AH, SAV_AH, DEL_AH, CREATED_AT, CREATED_BY, UPDATED_AT, UPDATED_BY )
VALUES ('S0001', '302',  'Y', 'Y', 'Y', sysdate(), 'system', sysdate(), 'system');

-- 프로그램 생성
INSERT INTO PROG_M (PROG_CD, PROG_NM, PROG_PH, TARGET, AUTH_CHECK, SCH_AH, SAV_AH, DEL_AH )
VALUES ('yesjm-grid-modal', '실습용 그리드 모달', '/jsp/_education/yesjm-grid-modal.jsp', '_self', 'Y', 'Y', 'Y', 'Y');
-- 메뉴 생성
INSERT INTO MENU_M (MENU_ID, MENU_GRP_CD, MENU_NM, MULTI_LANGUAGE, PARENT_ID, LEVEL, SORT, PROG_CD, CREATED_AT, CREATED_BY, UPDATED_AT, UPDATED_BY )
VALUES (303, 'SYSTEM_MANAGER', '실습용 그리드 모달', '{"ko":"실습용 그리드 모달","en":"실습용 그리드 모달"}',  300, 1, 3, 'yesjm-grid-modal', sysdate(), 'system', sysdate(), 'system');
-- 메뉴 권한
INSERT INTO AUTH_GROUP_MAP_M (GRP_AUTH_CD , MENU_ID, SCH_AH, SAV_AH, DEL_AH, CREATED_AT, CREATED_BY, UPDATED_AT, UPDATED_BY )
VALUES ('S0001', '303',  'Y', 'Y', 'Y', sysdate(), 'system', sysdate(), 'system');

INSERT INTO PROG_M (PROG_CD, PROG_NM, PROG_PH, TARGET, AUTH_CHECK, SCH_AH, SAV_AH)
VALUES ('yesjm-excel', '실습용 엑셀', '/jsp/_education/yesjm-excel.jsp', '_self', 'Y', 'Y', 'Y');
-- 메뉴 생성
INSERT INTO MENU_M (MENU_ID, MENU_GRP_CD, MENU_NM, MULTI_LANGUAGE, PARENT_ID, LEVEL, SORT, PROG_CD, CREATED_AT, CREATED_BY, UPDATED_AT, UPDATED_BY )
VALUES (310, 'SYSTEM_MANAGER', '실습용 엑셀', '{"ko":"실습용 엑셀","en":"실습용 엑셀"}',  300, 1, 10, 'yesjm-excel', sysdate(), 'system', sysdate(), 'system');
-- 메뉴 권한
INSERT INTO AUTH_GROUP_MAP_M (GRP_AUTH_CD , MENU_ID, SCH_AH, SAV_AH, CREATED_AT, CREATED_BY, UPDATED_AT, UPDATED_BY )
VALUES ('S0001', '310',  'Y', 'Y', sysdate(), 'system', sysdate(), 'system');