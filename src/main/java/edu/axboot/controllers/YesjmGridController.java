package edu.axboot.controllers;

import com.chequer.axboot.core.api.response.ApiResponse;
import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import edu.axboot.domain._education.EducationYesjm;
import edu.axboot.domain._education.EducationYesjmService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/_education/yesjmgrid")
public class YesjmGridController extends BaseController {

    @Inject
    private EducationYesjmService educationYesjmService;

    @RequestMapping(value = "/pages", method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNumber", value="페이지번호(0부터)", required = true, dataType = "integer", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name="pageSize", value="페이지크기", required = true, dataType = "integer", paramType = "query", defaultValue = "50"),
            @ApiImplicitParam(name="companyNm", value="회사명", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name="ceo", value="대표자", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name="bizno", value="사업자번호", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name="useYn", value="사용여부", dataType = "String", paramType = "query"),
    })
    public Responses.PageResponse pages(RequestParams<EducationYesjm> requestParams) {
        Page<EducationYesjm> pages = educationYesjmService.getPage(requestParams);
        return Responses.PageResponse.of(pages);
    }

    //JPA
    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name="companyNm", value="회사명", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name="ceo", value="대표자", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name="bizno", value="사업자번호", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name="useYn", value="사용여부", dataType = "String", paramType = "query"),
    })
    public Responses.ListResponse list(RequestParams<EducationYesjm> requestParams) {
        List<EducationYesjm> list = educationYesjmService.gets(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody List<EducationYesjm> request) {
        educationYesjmService.save(request);
        return ok();
    }

    @RequestMapping(value = "/querydsl", method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name="companyNm", value="회사명", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name="ceo", value="대표자", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name="bizno", value="사업자번호", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name="useYn", value="사용여부", dataType = "String", paramType = "query"),
    })
    public Responses.ListResponse getByQueryDslList(RequestParams<EducationYesjm> requestParams) {
        List<EducationYesjm> educationYesjm = this.educationYesjmService.getByQueryDsl(requestParams);
        return Responses.ListResponse.of(educationYesjm);
    }

    @RequestMapping(value = "/querydsl/selectone", method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", value="ID", required = true, dataType = "Long", paramType = "query"),
    })
    public EducationYesjm selectOne(RequestParams<EducationYesjm> requestParams) {
        EducationYesjm educationYesjm = this.educationYesjmService.getOneByQueryDsl(requestParams);
        return educationYesjm;
    }

    @RequestMapping(value = "/querydsl", method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save2(@RequestBody List<EducationYesjm> request) {
        educationYesjmService.saveByQueryDsl(request);
        return ok();
    }

/*    @RequestMapping(value = "/myBatis", method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyNm", value = "회사명", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ceo", value = "대표자", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bizno", value = "사업자번호", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "useYn", value = "사용여부 (Y/N)", dataType = "String", paramType = "query"),

    })
    public Responses.ListResponse getByMyBatisList(RequestParams<EducationYesjm> requestParams) {
        List<EducationYesjm> list = educationYesjmService.getByMyBatis(requestParams);
        return Responses.ListResponse.of(list);
    }*/
}