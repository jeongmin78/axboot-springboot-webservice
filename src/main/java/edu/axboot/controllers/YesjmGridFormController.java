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
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/_education/yesjmgridform")
public class YesjmGridFormController extends BaseController {

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

    //QueryDsl
    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse list(
            @RequestParam(value = "companyNm", required = false) String companyNm,
            @RequestParam(value = "ceo",required = false) String ceo,
            @RequestParam(value = "bizno",required = false) String bizno,
            @RequestParam(value = "useYn",required = false) String useYn) {
        List<EducationYesjm> list = educationYesjmService.gets(companyNm,ceo,bizno,useYn);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public EducationYesjm view(@PathVariable Long id){
        EducationYesjm entity = educationYesjmService.getByOne(id);
        return entity;
    }

    @RequestMapping(method = RequestMethod.POST, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody EducationYesjm request) {
        educationYesjmService.persist(request);
        return ok();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = APPLICATION_JSON)
    public ApiResponse delete(@PathVariable Long id){
        educationYesjmService.remove(id);
        return ok();
    }

}