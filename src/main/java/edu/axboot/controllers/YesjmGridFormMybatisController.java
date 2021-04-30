package edu.axboot.controllers;

import com.chequer.axboot.core.api.response.ApiResponse;
import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import edu.axboot.domain._education.EducationYesjm;
import edu.axboot.domain._education.EducationYesjmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/_education/yesjmgridform/mybatis")
public class YesjmGridFormMybatisController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(EducationYesjmService.class);

    @Inject
    private EducationYesjmService educationYesjmService;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse list(
            @RequestParam(value = "companyNm", required = false) String companyNm,
            @RequestParam(value = "ceo",required = false) String ceo,
            @RequestParam(value = "bizno",required = false) String bizno,
            @RequestParam(value = "useYn",required = false) String useYn) {

        try {
            List<EducationYesjm> list = educationYesjmService.getsByMyBatis(companyNm, ceo, bizno, useYn);
            return Responses.ListResponse.of(list);
        }
        catch (BadSqlGrammarException e){
            logger.error("마이바티스 조회 오류. 쿼리 확인해 보세요");
            return Responses.ListResponse.of(null);
        }catch ( RuntimeException e){
            logger.error(e.getMessage());
            return Responses.ListResponse.of(null);
        }
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