package com.pzg.code.sqlproject.mapper;

import com.pzg.code.sqlproject.vo.StoreList;
import com.pzg.code.sqlproject.vo.StoreListQueryVO;
import com.pzg.code.sqlproject.vo.StoreListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* @Description  存储列表dao层
**/
@Mapper
public interface StoreListMapper {

	/**
	* @Description  根据逐渐查找
	* @Author GuoYuanYuan
	* @Date  2019/5/9  16:16
	* @Param [slId]
	* @return com.hiynn.gybigdata.entity.StoreList
	**/
	public StoreList selectByPrimaryKey(String slId);

	/**
	* @Description  根据SlStId查找
	* @Author GuoYuanYuan
	* @Date  2019/5/9  16:16
	* @Param [slStId]
	* @return java.util.List<com.hiynn.gybigdata.entity.StoreList>
	**/
	public List<StoreList> selectBySlStId(String slStId);

	/**
	* @Description  新增
	* @Author GuoYuanYuan
	* @Date  2019/5/9  16:16
	* @Param [storeList]
	* @return int
	**/
    public int insertStoreList(StoreList storeList);

    /**
    * @Description  删除
    * @Author GuoYuanYuan
    * @Date  2019/5/9  16:16
    * @Param [ids]
    * @return int
    **/
    public int deleteByPrimaryKey(String[] ids);

    /**
    * @Description  修改
    * @Author GuoYuanYuan
    * @Date  2019/5/9  16:16
    * @Param [storeList]
    * @return int
    **/
    public int updateByPrimaryKey(StoreList storeList);

    /**
    * @Description  条件查询
    * @Author GuoYuanYuan
    * @Date  2019/5/9  16:17
    * @Param [storeListQueryVO]
    * @return java.util.List<com.hiynn.gybigdata.entity.vo.StoreListVO>
    **/
	public List<StoreListVO> findByParam(StoreListQueryVO storeListQueryVO);

	/**
	* @Description  根据slSlcId查找
	* @Author GuoYuanYuan
	* @Date  2019/5/9  16:17
	* @Param [ids]
	* @return int
	**/
	public int selectByslSlcId(String[] ids);
	
	
	/**
	* @Title: updateSensitive
	* @Description: TODO(修改是否敏感)
	* @return void    返回类型
	* @param slId
	* @param isSensitive
	*/ 
	public void updateSensitive(@Param("slId") String slId, @Param("isSensitive") Integer isSensitive);

	/**
	* @Title: updateRegister
	* @Description: TODO(更改注册信息)
	* @return void    返回类型
	* @param slId
	* @param isRegister
	*/ 
	public void updateRegister(@Param("slId") String slId, @Param("isRegister") String isRegister);
}
