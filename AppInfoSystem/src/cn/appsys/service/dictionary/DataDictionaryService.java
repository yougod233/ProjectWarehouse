package cn.appsys.service.dictionary;

import java.util.List;

import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.DataDictionary;

public interface DataDictionaryService {
	public List<DataDictionary> getDataDictionaryList(String typeCode);
}
