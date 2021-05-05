package edu.axboot.controllers;

import com.chequer.axboot.core.api.response.ApiResponse;
import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import edu.axboot.domain._education.EducationYesjm;
import edu.axboot.domain._education.EducationYesjmService;
import edu.axboot.utils.MiscUtils;
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

// ---------------------------------------------------------------------------------------------
    //QueryDsl
    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.PageResponse list(RequestParams<EducationYesjm> requestParams) {
        List<EducationYesjm> list = educationYesjmService.getListUsingQueryDsl(requestParams);
        Page<EducationYesjm> page = MiscUtils.toPage(list, requestParams.getPageable());
        return Responses.PageResponse.of(page);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public EducationYesjm view(@PathVariable Long id){
        EducationYesjm entity = educationYesjmService.getByOneUsingQueryDsl(id);
        return entity;
    }

    @RequestMapping(method = {RequestMethod.POST}, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody EducationYesjm request) {
        educationYesjmService.persistUsingQueryDsl(request);
        return ok();
    }

    @RequestMapping(value = "/{ids}", method = RequestMethod.DELETE, produces = APPLICATION_JSON)
    public ApiResponse delete(@PathVariable(name = "ids") List<Long> ids){
        educationYesjmService.deleteUsingQueryDsl(ids);
        return ok();
    }
}