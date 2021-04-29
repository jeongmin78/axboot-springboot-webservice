package edu.axboot.domain;

import com.chequer.axboot.core.domain.base.AXBootBaseService;
import com.chequer.axboot.core.domain.base.AXBootJPAQueryDSLRepository;
import edu.axboot.domain._education.QEducationYesjm;
import edu.axboot.domain.code.QCommonCode;
import edu.axboot.domain.file.QCommonFile;
import edu.axboot.domain.program.QProgram;
import edu.axboot.domain.program.menu.QMenu;
import edu.axboot.domain.sample.parent.QParentSample;
import edu.axboot.domain.user.QUser;
import edu.axboot.domain.user.auth.QUserAuth;
import edu.axboot.domain.user.auth.menu.QAuthGroupMenu;
import edu.axboot.domain.user.role.QUserRole;

import java.io.Serializable;


public class BaseService<T, ID extends Serializable> extends AXBootBaseService<T, ID> {

    protected QUserRole qUserRole = QUserRole.userRole;
    protected QAuthGroupMenu qAuthGroupMenu = QAuthGroupMenu.authGroupMenu;
    protected QCommonCode qCommonCode = QCommonCode.commonCode;
    protected QUser qUser = QUser.user;
    protected QProgram qProgram = QProgram.program;
    protected QUserAuth qUserAuth = QUserAuth.userAuth;
    protected QMenu qMenu = QMenu.menu;
    protected QCommonFile qCommonFile = QCommonFile.commonFile;
    protected QEducationYesjm qEducationYesjm = QEducationYesjm.educationYesjm;
    protected QParentSample qParentSample = QParentSample.parentSample;

    protected AXBootJPAQueryDSLRepository<T, ID> repository;

    public BaseService() {
        super();
    }

    public BaseService(AXBootJPAQueryDSLRepository<T, ID> repository) {
        super(repository);
        this.repository = repository;
    }
}
