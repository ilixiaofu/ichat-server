package com.lxf.ichat.mapper;

import com.lxf.ichat.dto.StatusDTO;
import com.lxf.ichat.dto.StatusDTOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StatusDTOMapper {
    long countByExample(StatusDTOExample example);

    int deleteByExample(StatusDTOExample example);

    int deleteByPrimaryKey(String id);

    int insert(StatusDTO record);

    int insertSelective(StatusDTO record);

    List<StatusDTO> selectByExample(StatusDTOExample example);

    StatusDTO selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") StatusDTO record, @Param("example") StatusDTOExample example);

    int updateByExample(@Param("record") StatusDTO record, @Param("example") StatusDTOExample example);

    int updateByPrimaryKeySelective(StatusDTO record);

    int updateByPrimaryKey(StatusDTO record);
}