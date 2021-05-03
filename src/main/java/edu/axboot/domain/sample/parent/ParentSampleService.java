package edu.axboot.domain.sample.parent;

import com.chequer.axboot.core.parameter.RequestParams;
import com.querydsl.core.BooleanBuilder;
import edu.axboot.domain.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Service
public class ParentSampleService extends BaseService<ParentSample, String> {

	private ParentSampleRepository parentRepository;

	@Inject
	public ParentSampleService(ParentSampleRepository parentRepository) {
		super(parentRepository);
		this.parentRepository = parentRepository;
	}

	@Transactional
	public void deleteByKeys(List<String> keys) {
		keys.forEach(this::delete);
	}

    public List<ParentSample> get(RequestParams<ParentSample> requestParams) {
		String key = requestParams.getString("key","");
		String value = requestParams.getString("value", "");
		String useYn = requestParams.getString("useYn", "");

		BooleanBuilder builder = new BooleanBuilder();
		if (isNotEmpty(key)){
			builder.and(qParentSample.key.eq(key));
		}
		if (isNotEmpty(value)){
			builder.and(qParentSample.value.eq(value));
		}
		if (isNotEmpty(useYn)){
			builder.and(qParentSample.etc4.eq(useYn));
		}

		List<ParentSample> sample = select().from(qParentSample).where(builder).fetch();

		return sample;
	}
}