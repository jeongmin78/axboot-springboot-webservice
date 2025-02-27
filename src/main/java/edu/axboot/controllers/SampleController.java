package edu.axboot.controllers;

import com.chequer.axboot.core.parameter.RequestParams;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import edu.axboot.domain.sample.child.ChildSample;
import edu.axboot.domain.sample.child.ChildSampleService;
import edu.axboot.domain.sample.child.ChildSampleVO;
import edu.axboot.domain.sample.parent.ParentSample;
import edu.axboot.domain.sample.parent.ParentSampleService;
import edu.axboot.domain.sample.parent.ParentSampleVO;
import com.chequer.axboot.core.api.response.ApiResponse;
import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.utils.ModelMapperUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/samples")
public class SampleController extends BaseController {

    @Inject
    private ParentSampleService parentService;

    @Inject
    private ChildSampleService childService;

    @RequestMapping(value = "/parent", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.PageResponse parentList(Pageable pageable) {
        Page<ParentSample> pages = parentService.findAll(pageable);
        return Responses.PageResponse.of(ParentSampleVO.of(pages.getContent()), pages);
    }

    @RequestMapping(value = "/parent", method = {RequestMethod.POST, RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse parentSave(@RequestBody List<ParentSampleVO> list) {
        List<ParentSample> parentSampleList = ModelMapperUtils.mapList(list, ParentSample.class);
        parentService.save(parentSampleList);
        return ok();
    }

    @RequestMapping(value = "/parent", method = {RequestMethod.DELETE}, produces = APPLICATION_JSON)
    public ApiResponse parentDelete(@RequestParam(value = "key") List<String> keys) {
        parentService.deleteByKeys(keys);
        return ok();
    }

    @RequestMapping(value = "/child", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.PageResponse childList(@RequestParam(defaultValue = "") String parentKey, Pageable pageable) {
        Page<ChildSample> pages = childService.findByParentKeyWithPaging(parentKey, pageable);
        return Responses.PageResponse.of(ChildSampleVO.of(pages.getContent()), pages);
    }

    @RequestMapping(value = "/child", method = {RequestMethod.POST, RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse childSave(@RequestBody List<ChildSampleVO> list) {
        List<ChildSample> childSampleList = ModelMapperUtils.mapList(list, ChildSample.class);
        childService.save(childSampleList);
        return ok();
    }

    @RequestMapping(value = "/child", method = {RequestMethod.DELETE}, produces = APPLICATION_JSON)
    public ApiResponse childDelete(@RequestParam(value = "key") List<String> keys) {
        childService.deleteByKeys(keys);
        return ok();
    }

    @RequestMapping(value = "/parent/queryDsl", method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "key", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "value", value = "value", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "useYn", value = "useYn", dataType = "String", paramType = "query"),
    })
    public Responses.ListResponse parentList(RequestParams<ParentSample> requestParams) {
        List<ParentSample> pages = this.parentService.get(requestParams);
        return Responses.ListResponse.of(pages);
    }

}
