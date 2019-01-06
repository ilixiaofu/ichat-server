package com.lxf.ichat.mapper;

import com.lxf.ichat.dto.HeadportraitDTO;
import com.lxf.ichat.dto.HeadportraitDTOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HeadportraitDTOMapper {
    long countByExample(HeadportraitDTOExample example);

    int deleteByExample(HeadportraitDTOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HeadportraitDTO record);

    int insertSelective(HeadportraitDTO record);

    List<HeadportraitDTO> selectByExample(HeadportraitDTOExample example);

    HeadportraitDTO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HeadportraitDTO record, @Param("example") HeadportraitDTOExample example);

    int updateByExample(@Param("record") HeadportraitDTO record, @Param("example") HeadportraitDTOExample example);

    int updateByPrimaryKeySelective(HeadportraitDTO record);

    int updateByPrimaryKey(HeadportraitDTO record);
}