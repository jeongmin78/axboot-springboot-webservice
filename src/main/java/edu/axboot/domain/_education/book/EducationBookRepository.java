package edu.axboot.domain._education.book;

import edu.axboot.domain._education.EducationYesjm;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.jdo.annotations.Query;
import java.util.List;

public interface EducationBookRepository extends JpaRepository<EducationBook, Long> {

//    @Query("SELECT p FROM EDUCATION_YESJM p ORDER BY p.id DESC")
    List<EducationYesjm> findBy();
}
