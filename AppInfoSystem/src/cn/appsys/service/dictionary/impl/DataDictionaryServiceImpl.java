package cn.appsys.service.dictionary.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.dictionary.DataDictionaryMapper;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.service.dictionary.DataDictionaryService;
@Service
public class DataDictionaryServiceImpl implements DataDictionaryService {

	@Resource
	DataDictionaryMapper dataDictionaryMapper;
	
	@Override
	public List<DataDictionary> getDataDictionaryList(String typeCode) {
		return dataDictionaryMapper.getDataDictionaryList(typeCode);
	}

}
