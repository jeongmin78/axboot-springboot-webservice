package edu.axboot.controllers;

import com.chequer.axboot.core.api.response.ApiResponse;
import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import edu.axboot.domain._education.EducationYesjm;
import edu.axboot.domain._education.EducationYesjmService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/_education/yesjmGridForm/myBatis")
public class YesjmGridFormMybatisController extends BaseController {

    @Inject
    private EducationYesjmService educationYesjmService;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse list(
            @RequestParam(value = "companyNm", required = false) String companyNm,
            @RequestParam(value = "ceo",required = false) String ceo,
            @RequestParam(value = "bizno",required = false) String bizno,
            @RequestParam(value = "useYn",required = false) String useYn) {
        List<EducationYesjm> list = educationYesjmService.getsByMyBatis(companyNm,ceo,bizno,useYn);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public EducationYesjm view(@PathVariable Long id){
        EducationYesjm entity = educationYesjmService.getOneByMyBatis(id);
        return entity;
    }

    @RequestMapping(method = RequestMethod.POST, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody EducationYesjm request) {
        educationYesjmService.saveByMybatis(request);
        return ok();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = APPLICATION_JSON)
    public ApiResponse delete(@PathVariable Long id){
        educationYesjmService.deleteByMybatis(id);
        return ok();
    }

}