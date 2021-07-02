package com.yyyysq.mall_user_service.service;


import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.yyyysq.mall_config.common.Constants;
import com.yyyysq.mall_config.common.ServiceResultEnum;
import com.yyyysq.mall_config.utils.*;
import com.yyyysq.mall_goods.VO.MallShoppingCartVO;
import com.yyyysq.mall_goods.dto.MallShoppingCartDTO;
import com.yyyysq.mall_goods.entity.MallShoppingCart;
import com.yyyysq.mall_goods.serviceApi.MallShoppingCartService;
import com.yyyysq.mall_user.dto.MallUserDTO;
import com.yyyysq.mall_user.entity.MallUser;
import com.yyyysq.mall_user.serviceApi.MallUserService;
import com.yyyysq.mall_user_service.dao.MallUserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.util.List;
import java.util.UUID;


@Service
public class MallUserServiceImpl implements MallUserService {

    @Reference
    MallShoppingCartService mallShoppingCartService;

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    MallUserMapper mallUserMapper;

    @Override
    public String register(String loginName, String password) {
        if (mallUserMapper.selectByLoginName(loginName) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        MallUser registerUser = new MallUser();
        registerUser.setLoginName(loginName);
        registerUser.setNickName(loginName);
        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        registerUser.setPasswordMd5(passwordMD5);
        if (mallUserMapper.insertSelective(registerUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String passwordMD5){
        //获取user
        MallUser user = mallUserMapper.selectByLoginNameAndPasswd(loginName, passwordMD5);
        if (user != null && user.getPasswordMd5().equals(passwordMD5)) {
            if (user.getLockedFlag() == 1) {
                return null;
            }
            //昵称太长 影响页面展示
            if (user.getNickName() != null && user.getNickName().length() > 7) {
                String tempNickName = user.getNickName().substring(0, 7) + "..";
                user.setNickName(tempNickName);
            }
//            //获取购物车商品数量
//            List<MallShoppingCartVO> mallShoppingCartVOList = mallShoppingCartService.getMyShoppingCartItems(user.getUserId());
//            int count = 0;
//            if(mallShoppingCartVOList != null) {
//                for (MallShoppingCartVO mallShoppingCartVO : mallShoppingCartVOList) {
//                    count += mallShoppingCartVO.getGoodsCount();
//                }
//            }
            //生成tokenId
            String token = TokenUtil.getToken(user.getUserId());
            MallUserDTO mallUserDTO = new MallUserDTO();
            BeanUtils.copyProperties(user, mallUserDTO); //这个方法要求前面参数必须包含后面所有参数，但是前面参数可以有后面参数没有的参数，但这里我们使用的自己生成的方法，将有对应的属性都转换了
//            mallUserDTO.setShopCartItemCount(count);
            //把user和tokenId放进redis缓存里
            redisTemplate.opsForValue().set(token,mallUserDTO, Duration.ofMinutes(30L));
            return token;
        }
        return null;
    }

    /**
     *
     * @param mallUser   //是提交地址的表单
     * @param oldMallUserDTO //原网站的用户信息
     * @return
     */
    @Override
    public MallUserDTO updateAddress(MallUser mallUser, MallUserDTO oldMallUserDTO) {
        MallUser userFrom = mallUserMapper.selectByPrimaryKey(oldMallUserDTO.getUserId());
        if (userFrom != null) {
            if (!StringUtils.isEmpty(mallUser.getNickName())) {
                userFrom.setNickName(MallUtil.cleanString(mallUser.getNickName()));
            }
            if (!StringUtils.isEmpty(mallUser.getAddress())) {
                userFrom.setAddress(MallUtil.cleanString(mallUser.getAddress()));
            }
            if (!StringUtils.isEmpty(mallUser.getIntroduceSign())) {
                userFrom.setIntroduceSign(MallUtil.cleanString(mallUser.getIntroduceSign()));
            }
            if (mallUserMapper.updateByPrimaryKeySelective(userFrom) > 0) {
                MallUserDTO mallUserDto = new MallUserDTO();
                userFrom = mallUserMapper.selectByPrimaryKey(mallUser.getUserId());
                BeanUtils.copyProperties(userFrom, mallUserDto);
                return mallUserDto;
            }
        }
            return null;
    }
    @Override
    public MallUserDTO updateUserInfo(MallUser mallUser, MallUserDTO userTemp) {
        MallUser userFromDB = mallUserMapper.selectByPrimaryKey(userTemp.getUserId());
        if (userFromDB != null) {
            if (!org.thymeleaf.util.StringUtils.isEmpty(mallUser.getNickName())) {
                userFromDB.setNickName(MallUtil.cleanString(mallUser.getNickName()));
            }
            if (!org.thymeleaf.util.StringUtils.isEmpty(mallUser.getAddress())) {
                userFromDB.setAddress(MallUtil.cleanString(mallUser.getAddress()));
            }
            if (!org.thymeleaf.util.StringUtils.isEmpty(mallUser.getIntroduceSign())) {
                userFromDB.setIntroduceSign(MallUtil.cleanString(mallUser.getIntroduceSign()));
            }
            if (mallUserMapper.updateByPrimaryKeySelective(userFromDB) > 0) {
                MallUserDTO mallUserDTO = new MallUserDTO();
//                System.out.println("更换成功");
                userFromDB = mallUserMapper.selectByPrimaryKey(mallUser.getUserId());
                MyBeanUtil.copyProperties(userFromDB, mallUserDTO);
                return mallUserDTO;
            }
        }
        return null;
    }
}
