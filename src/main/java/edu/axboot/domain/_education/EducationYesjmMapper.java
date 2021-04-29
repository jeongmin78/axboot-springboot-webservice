package edu.axboot.domain._education;

import com.chequer.axboot.core.mybatis.MyBatisMapper;

import java.util.HashMap;
import java.util.List;

public interface EducationYesjmMapper extends MyBatisMapper{

//    List<EducationYesjm> getByMyBatis(EducationYesjm educationYesjm);

    List<EducationYesjm> select(HashMap<String,String> params);
    EducationYesjm selectOne(Long id);

    int insert(EducationYesjm request);
    int update(EducationYesjm request);
    int delete(Long id);
}
