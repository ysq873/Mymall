package com.yyyysq.mall_user.serviceApi;


import com.yyyysq.mall_config.utils.PageQueryUtil;
import com.yyyysq.mall_config.utils.PageResult;
import com.yyyysq.mall_user.dto.MallUserDTO;

import com.yyyysq.mall_user.entity.MallUser;
import org.apache.catalina.session.StandardSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface MallUserService {

//    PageResult getAdminUserPage(PageQueryUtil pageQueryUtil);
//
//    Integer getTotalUser(PageQueryUtil pageQueryUtil);
//
//    boolean lockUsers(Integer[] id, Integer idStatus);

    String register(String loginName, String password);

    String login(String loginName, String password);

    MallUserDTO updateAddress(MallUser mallUser, MallUserDTO mallUserDTO);

    MallUserDTO updateUserInfo(MallUser mallUser,MallUserDTO mallUserDTO);



}
