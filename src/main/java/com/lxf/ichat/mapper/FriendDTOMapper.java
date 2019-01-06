package com.lxf.ichat.mapper;

import com.lxf.ichat.dto.FriendDTO;
import com.lxf.ichat.dto.FriendDTOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FriendDTOMapper {
    long countByExample(FriendDTOExample example);

    int deleteByExample(FriendDTOExample example);

    int insert(FriendDTO record);

    int insertSelective(FriendDTO record);

    List<FriendDTO> selectByExample(FriendDTOExample example);

    int updateByExampleSelective(@Param("record") FriendDTO record, @Param("example") FriendDTOExample example);

    int updateByExample(@Param("record") FriendDTO record, @Param("example") FriendDTOExample example);
}