package com.yyyysq.mall_user_service.dao;


import com.yyyysq.mall_config.utils.PageQueryUtil;
import com.yyyysq.mall_user.entity.MallUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallUserMapper {

    public List<MallUser> findMallUserList(PageQueryUtil pageQueryUtil);

    public Integer getTotalMallUsers(PageQueryUtil pageQueryUtil);

    public Integer lockedUser(@Param("ids") Integer[] ids, @Param("lockedStatus") Integer lockedStatus);

    int deleteByPrimaryKey(Long userId);

    int insert(MallUser record);

    int insertSelective(MallUser record);

    MallUser selectByPrimaryKey(Long userId);

    MallUser selectByLoginName(String loginName);

    MallUser selectByLoginNameAndPasswd(@Param("loginName") String loginName, @Param("password") String password);

    int updateByPrimaryKeySelective(MallUser record);

    int updateByPrimaryKey(MallUser record);

}
