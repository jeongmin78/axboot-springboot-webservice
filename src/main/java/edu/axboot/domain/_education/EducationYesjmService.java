package edu.axboot.domain._education;

import com.chequer.axboot.core.api.ApiException;
import com.chequer.axboot.core.parameter.RequestParams;
import com.chequer.axboot.core.utils.CoreUtils;
import com.querydsl.core.BooleanBuilder;
import edu.axboot.domain.BaseService;
import edu.axboot.domain.file.CommonFile;
import edu.axboot.domain.file.CommonFileService;
import edu.axboot.domain.file.UploadParameters;
import edu.axboot.fileupload.FileUploadService;
import edu.axboot.fileupload.UploadFile;
import org.apache.commons.io.FileUtils;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.ReaderConfig;
import org.jxls.reader.XLSReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EducationYesjmService extends BaseService<EducationYesjm, Long> {
    private static final Logger logger = LoggerFactory.getLogger(EducationYesjmService.class);

    private EducationYesjmRepository educationYesjmRepository;

    @Autowired
    private CommonFileService commonFileService;

    @Autowired
    private FileUploadService fileuploadService;

    @Inject
    public EducationYesjmService(EducationYesjmRepository educationYesjmRepository) {
        super(educationYesjmRepository);
        this.educationYesjmRepository = educationYesjmRepository;
    }

    public Page<EducationYesjm> getPage(RequestParams<EducationYesjm> requestParams) {
        List<EducationYesjm> list = this.getListUsingQueryDsl(requestParams);
        Pageable pageable = requestParams.getPageable();
        int start = (int)pageable.getOffset();
        int end = (start+pageable.getPageSize() > list.size() ? list.size() : (start+pageable.getPageSize()));
        Page<EducationYesjm> pages = new PageImpl<>(list.subList(start,end), pageable, list.size());
        return pages;
    }

//    @Inject
    private EducationYesjmMapper educationYesjmMapper;


    public List<EducationYesjm> gets(RequestParams<EducationYesjm> requestParams) {
        List<EducationYesjm> list = this.getFilter(findAll(), requestParams.getString("companyNm",""),1);
        List<EducationYesjm> list2 = this.getFilter(list, requestParams.getString("ceo",""),2);
        List<EducationYesjm> list3 = this.getFilter(list2, requestParams.getString("bizno",""),3);
        List<EducationYesjm> list4 = this.getFilter(list3, requestParams.getString("useYn", ""),4);

        return list4;
    }

    private List<EducationYesjm> getFilter(List<EducationYesjm> sources, String filter, int typ){
        List<EducationYesjm> targets = new ArrayList<EducationYesjm>();
        for (EducationYesjm entity: sources){
            if("".equals(filter)){
                targets.add(entity);
            }else {
                if (typ == 1){
                    if (entity.getCompanyNm().contains(filter))
                        targets.add(entity);
                }
                else if (typ == 2){
                    if (entity.getCeo().contains(filter))
                        targets.add(entity);
                }
                else if (typ == 3){
                    if (entity.getBizno().equals(filter))
                        targets.add(entity);
                }
                else{
                    if (entity.getUseYn().equals(filter))
                        targets.add(entity);
                }
            }
        }
        return targets;
    }


    //MyBatis
    public List<EducationYesjm> getsByMyBatis(String companyNm, String ceo, String bizno, String useYn) {

        if (!"".equals(useYn) && !"Y".equals(useYn) && !"N".equals(useYn)){
            throw new RuntimeException("Y 아니면 N를 입력하세요"); //사용자가 exception을 인위적으로 잡아서 던짐
        }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("companyNm", companyNm);
        params.put("ceo", ceo);
        params.put("bizno", bizno);
        params.put("useYn", useYn);

        List<EducationYesjm> list = educationYesjmMapper.select(params);

        return list;
    }

    public EducationYesjm getOneByMyBatis(Long id) {
        return educationYesjmMapper.selectOne(id);
    }

    @Transactional
    public void saveByMybatis(EducationYesjm request) {
        if (request.getId() == null || request.getId() == 0) {
            educationYesjmMapper.insert(request);
        }else {
            educationYesjmMapper.update(request);
        }
    }

    @Transactional
    public void deleteByMybatis(Long id) {
        educationYesjmMapper.delete(id);
    }


//  QueryDSL ------------------------------------------------------------------------------

    // YesjmGrid
    public List<EducationYesjm> gets(String companyNm, String ceo, String bizno, String useYn) {
    BooleanBuilder builder = new BooleanBuilder();

    if (isNotEmpty(companyNm)) {
        builder.and(qEducationYesjm.companyNm.like("%" + companyNm + "%"));
    }
    if (isNotEmpty(ceo)) {
        builder.and(qEducationYesjm.ceo.contains(ceo));
    }
    if (isNotEmpty(bizno)){
        builder.and(qEducationYesjm.bizno.contains(bizno));
    }
    if (isNotEmpty(useYn)){
        builder.and(qEducationYesjm.useYn.eq(useYn));
    }

    List<EducationYesjm> educationYesjmList = select()
            .from(qEducationYesjm)
            .where(builder)
            .orderBy(qEducationYesjm.companyNm.asc())
            .fetch();

    return educationYesjmList;
}

    public EducationYesjm getByOne(RequestParams<EducationYesjm> requestParams) {
        Long id = requestParams.getLong("id");
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qEducationYesjm.id.eq(id));
        EducationYesjm educationYesjm = select().from(qEducationYesjm).where(builder).fetchOne();
        return educationYesjm;
    }

    @Transactional
    public void saveByQueryDsl(List<EducationYesjm> request) {
        for (EducationYesjm educationYesjm :request) {
            if (educationYesjm.isCreated()) {
                save(educationYesjm);
            } else if (educationYesjm.isModified()) {
                update(qEducationYesjm)
                        .set(qEducationYesjm.companyNm, educationYesjm.getCompanyNm())
                        .set(qEducationYesjm.ceo, educationYesjm.getCeo())
                        .set(qEducationYesjm.bizno, educationYesjm.getBizno())
                        .set(qEducationYesjm.tel, educationYesjm.getTel())
                        .set(qEducationYesjm.email, educationYesjm.getEmail())
                        .set(qEducationYesjm.useYn, educationYesjm.getUseYn())
                        .where(qEducationYesjm.id.eq(educationYesjm.getId()))
                        .execute();
            } else if (educationYesjm.isDeleted()){
                delete(qEducationYesjm)
                        .where(qEducationYesjm.id.eq(educationYesjm.getId()))
                        .execute();
            }
        }
    }

    // YesjmGridForm
    public EducationYesjm getByOneUsingQueryDsl(Long id) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qEducationYesjm.id.eq(id));

        EducationYesjm entity = select()
                .from(qEducationYesjm)
                .where(builder)
                .fetchOne();
        return entity;
    }

    public List<EducationYesjm> getListUsingQueryDsl(RequestParams<EducationYesjm> requestParams) {
        String companyNm = requestParams.getString("companyNm", "");
        String ceo = requestParams.getString("ceo", "");
        String bizno = requestParams.getString("bizno", "");
        String useYn = requestParams.getString("useYn", "");
        String filter = requestParams.getFilter();

        logger.info("회사명 : " + companyNm);
        logger.info("대표자 : " + ceo);
        logger.info("사업자번호 : " + bizno);
        logger.info("사용여부 : " + useYn);
        logger.info("검색 : " + filter);

        BooleanBuilder builder = new BooleanBuilder();

        if (isNotEmpty(companyNm)) {
            builder.and(qEducationYesjm.companyNm.contains(companyNm));
        }
        if (isNotEmpty(ceo)) {
            builder.and(qEducationYesjm.ceo.like("%" + ceo + "%"));
        }
        if (isNotEmpty(bizno)) {
            builder.and(qEducationYesjm.bizno.like(bizno + "%"));
        }
        if (isNotEmpty(useYn)) {
            builder.and(qEducationYesjm.useYn.eq(useYn));
        }

        if (isNotEmpty(filter)) {
            builder.and(qEducationYesjm.companyNm.contains(filter)
                    .or(qEducationYesjm.ceo.like("%" + filter + "%"))
                    .or(qEducationYesjm.bizno.like(filter + "%"))
            );
        }

        List<EducationYesjm> list = select()
                .from(qEducationYesjm)
                .where(builder)
                .orderBy(qEducationYesjm.companyNm.asc())
                .fetch();

        return list;
    }

    @Transactional
    public void persistUsingQueryDsl(EducationYesjm request){
        if (request.getId() == null || request.getId() == 0) {
            if (request.getFileIdList().size() > 0) {
                request.setAttachId(CoreUtils.getUUID().replaceAll("-",""));
                List<CommonFile> commonFileList = new ArrayList<>();
                for(Long fileId: request.getFileIdList()){
                    CommonFile commonFile = commonFileService.findOne(fileId);
                    commonFile.setTargetId(request.getAttachId());
                    commonFileList.add(commonFile);
                }
                request.setFileList(commonFileList);
            }
            this.educationYesjmRepository.save(request);
        } else {
            update(qEducationTeach)
                    .set(qEducationTeach.companyNm, request.getCompanyNm())
                    .set(qEducationTeach.ceo, request.getCeo())
                    .set(qEducationTeach.bizno, request.getBizno())
                    .set(qEducationTeach.tel, request.getTel())
                    .set(qEducationTeach.zip, request.getZip())
                    .set(qEducationTeach.address, request.getAddress())
                    .set(qEducationTeach.addressDetail, request.getAddressDetail())
                    .set(qEducationTeach.email, request.getEmail())
                    .set(qEducationTeach.remark, request.getRemark())
                    .set(qEducationTeach.useYn, request.getUseYn())
                    .where(qEducationTeach.id.eq(request.getId()))
                    .execute();
        }
    }
    @Transactional
    public void deleteUsingQueryDsl(List<Long> ids) {
        for(Long id: ids){
            deleteUsingQueryDsl(id);
        }
    }

    @Transactional
    public void deleteUsingQueryDsl(Long id) {
        delete(qEducationYesjm).where(qEducationYesjm.id.eq(id)).execute();
    }

//  Excel ------------------------------------------------------------------------------

    @Transactional
    public String saveDataByExcel(UploadFile uploadFile) throws Exception {
        String resultMsg = "";

        ReaderConfig.getInstance().setSkipErrors(true);

        XLSReader mainReader = ReaderBuilder.buildFromXML(new ClassPathResource("/excel/education_upload.xml").getInputStream());
        List<EducationYesjm> entities = new ArrayList();

        Map beans = new HashMap();
        beans.put("educationList", entities);

        String excelFile = uploadFile.getSavePath();
        File file = new File(excelFile);
        mainReader.read(FileUtils.openInputStream(file), beans);

        int rowIndex = 1;

        for (EducationYesjm entity : entities) {
            if (StringUtils.isEmpty(entity.getCompanyNm())) {
                resultMsg = String.format("%d 번째 줄의 회사명이 비어있습니다.", rowIndex);
                throw new ApiException(String.format("%d 번째 줄의 회사명이 비어있습니다.", rowIndex));
            }
            if (StringUtils.isEmpty(entity.getCeo())) {
                resultMsg = String.format("%d 번째 줄의 대표자가 비어있습니다.", rowIndex);
                throw new ApiException(String.format("%d 번째 줄의 대표자가 비어있습니다.", rowIndex));
            }
            if (StringUtils.isEmpty(entity.getUseYn())) {
                resultMsg = String.format("%d 번째 줄의 사용여부가 비어있습니다.", rowIndex);
                throw new ApiException(String.format("%d 번째 줄의 사용여부가 비어있습니다.", rowIndex));
            }

            save(entity);
            rowIndex++;
        }

        return resultMsg;
    }

    @Transactional
    public String uploadFileByExcel(MultipartFile multipartFile) throws Exception {
        UploadParameters uploadParameters = new UploadParameters();
        uploadParameters.setMultipartFile(multipartFile);
        UploadFile uploadFile = fileuploadService.addCommonFile(uploadParameters);
        String result = this.saveDataByExcel(uploadFile);
        fileuploadService.deleteFile(uploadFile.getSavePath());
        return result;
    }
}