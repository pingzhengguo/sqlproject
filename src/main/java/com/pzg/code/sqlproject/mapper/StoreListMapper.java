package com.pzg.code.sqlproject.mapper;

import com.pzg.code.sqlproject.vo.StoreList;
import com.pzg.code.sqlproject.vo.StoreListQueryVO;
import com.pzg.code.sqlproject.vo.StoreListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Description 存储列表dao层
 **/
@Mapper
public interface StoreListMapper {

    /**
     * @return com.hiynn.gybigdata.entity.StoreList
     * @Description 根据逐渐查找
     * @Author GuoYuanYuan
     * @Date 2019/5/9  16:16
     * @Param [slId]
     **/
    public StoreList selectByPrimaryKey(String slId);

    /**
     * @return java.util.List<com.hiynn.gybigdata.entity.StoreList>
     * @Description 根据SlStId查找
     * @Author GuoYuanYuan
     * @Date 2019/5/9  16:16
     * @Param [slStId]
     **/
    public List<StoreList> selectBySlStId(String slStId);

    /**
     * @return int
     * @Description 新增
     * @Author GuoYuanYuan
     * @Date 2019/5/9  16:16
     * @Param [storeList]
     **/
    public int insertStoreList(StoreList storeList);

    /**
     * @return int
     * @Description 删除
     * @Author GuoYuanYuan
     * @Date 2019/5/9  16:16
     * @Param [ids]
     **/
    public int deleteByPrimaryKey(String[] ids);

    /**
     * @return int
     * @Description 修改
     * @Author GuoYuanYuan
     * @Date 2019/5/9  16:16
     * @Param [storeList]
     **/
    public int updateByPrimaryKey(StoreList storeList);

    /**
     * @return java.util.List<com.hiynn.gybigdata.entity.vo.StoreListVO>
     * @Description 条件查询
     * @Author GuoYuanYuan
     * @Date 2019/5/9  16:17
     * @Param [storeListQueryVO]
     **/
    public List<StoreListVO> findByParam(StoreListQueryVO storeListQueryVO);

    /**
     * @return int
     * @Description 根据slSlcId查找
     * @Author GuoYuanYuan
     * @Date 2019/5/9  16:17
     * @Param [ids]
     **/
    public int selectByslSlcId(String[] ids);


    /**
     * @param slId
     * @param isSensitive
     * @return void    返回类型
     * @Title: updateSensitive
     * @Description: TODO(修改是否敏感)
     */
    public void updateSensitive(@Param("slId") String slId, @Param("isSensitive") Integer isSensitive);

    /**
     * @param slId
     * @param isRegister
     * @return void    返回类型
     * @Title: updateRegister
     * @Description: TODO(更改注册信息)
     */
    public void updateRegister(@Param("slId") String slId, @Param("isRegister") String isRegister);
}
