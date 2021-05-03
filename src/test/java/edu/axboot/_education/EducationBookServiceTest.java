package edu.axboot._education;

import edu.axboot.AXBootApplication;
import edu.axboot.controllers.dto.EducationListResponseDto;
import edu.axboot.controllers.dto.EducationResponseDto;
import edu.axboot.controllers.dto.EducationSaveRequestDto;
import edu.axboot.controllers.dto.EducationUpdateRequestDto;
import edu.axboot.domain._education.EducationBookService;
import edu.axboot.domain._education.EducationYesjmService;
import lombok.extern.java.Log;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertTrue;

@Log
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AXBootApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EducationBookServiceTest {

    @Autowired
    private EducationBookService educationBookService;
    private static final Logger logger = LoggerFactory.getLogger(EducationYesjmService.class);

    public static long testId = 0;

    @Test
    public void test1_거래처_한건_저장하기() {
        //given
        EducationSaveRequestDto saveDto = EducationSaveRequestDto.builder()
                .companyNm("단위테스트")
                .ceo("노정민")
                .bizno("123443211")
                .build();
        //when
        testId = this.educationBookService.save(saveDto);
        logger.info("\n"+ "ID ===============> " + testId);
        //then
        assertTrue(testId > 0);
    }
    @Test
    public void test2_거래처_한건_불러오기() {
        //given
        Long id = testId;
        //when
        EducationResponseDto result = this.educationBookService.findById(id);
        logger.info("\n"+ id + "============== " + result.getId());
        //then
        assertTrue(result.getId() == testId);
    }
    @Test
    public void test3_거래처_한건_수정하고_확인하기() {
        //given
        EducationUpdateRequestDto updateDto = EducationUpdateRequestDto.builder()
                .tel("010-5555-6666")
                .email("gggg@ffff")
                .build();
        //when
        long result = this.educationBookService.update(testId, updateDto);
        logger.info("\n"+ result + "============== " + testId);

        EducationResponseDto entity = this.educationBookService.findById(testId);
        logger.info("\n"+ entity.getTel() + "==============" + updateDto.getTel());
        logger.info("\n"+ entity.getEmail() + "==============" + updateDto.getEmail());
        //then
        assertTrue(testId == result);
    }
    @Test
    public void test4_거래처_수정_확인하기() {
        //given
        String companyNm = "단위테스트";
        String ceo = "";
        String bizno = "";

        //when
        List<EducationListResponseDto> result = this.educationBookService.findBy(companyNm,ceo,bizno);
        logger.info("\nlist: ============== " + result);
        //then
        assertTrue(result.size() > 0);
    }

}
