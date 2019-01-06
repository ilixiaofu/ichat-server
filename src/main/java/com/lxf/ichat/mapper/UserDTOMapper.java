package com.lxf.ichat.mapper;

import com.lxf.ichat.dto.UserDTO;
import com.lxf.ichat.dto.UserDTOExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface UserDTOMapper {
    long countByExample(UserDTOExample example);

    int deleteByExample(UserDTOExample example);

    int deleteByPrimaryKey(String id);

    int insert(UserDTO record);

    int insertSelective(UserDTO record);

    List<UserDTO> selectByExample(UserDTOExample example);

    Set<UserDTO> selectUserFriendsPrimaryKey(String UID);

    UserDTO selectByPrimaryKey(String id);

    UserDTO selectByIDAndPaswd(UserDTO record);

    int updateByExampleSelective(@Param("record") UserDTO record, @Param("example") UserDTOExample example);

    int updateByExample(@Param("record") UserDTO record, @Param("example") UserDTOExample example);

    int updateByPrimaryKeySelective(UserDTO record);

    int updateByPrimaryKey(UserDTO record);
}