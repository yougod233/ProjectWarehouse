package cn.appsys.dao.dictionary;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DataDictionary;

public interface DataDictionaryMapper {
	public List<DataDictionary> getDataDictionaryList(@Param("typeCode") String typeCode);
}
