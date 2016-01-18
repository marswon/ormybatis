package cn.bblink.common.ormybatis.bean.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * insert批量对象mapping参数bean
 * 
 * @author donghui
 */
public class InsertBatchMappingParamBean extends BaseMappingParam{

	/**
	 * po 对象 map形式
	 */
	private List<Map<String, Object>> poMapList = new ArrayList<Map<String,Object>>();

	public InsertBatchMappingParamBean(){}
	
	public InsertBatchMappingParamBean(List poList){
		for (Object po : poList) {
            Map<String, Object> map = super.toMap(po);
            poMapList.add(map);
        }
	}

	public List<Map<String, Object>> getPoMapList() {return poMapList;}
	public void setPoMapList(List<Map<String, Object>> poMapList) {this.poMapList = poMapList;}

}
