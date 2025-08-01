package com.example.holiday.mapper;

import com.example.holiday.model.CommonHolidays;
import com.example.holiday.vo.CommonHolidayVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommonHolidayMapper {
    
    @Mapping(source = "date", target = "date")
    @Mapping(source = "localNames", target = "localNames")
    CommonHolidayVO toVO(CommonHolidays commonHolidays);
    
    @Mapping(source = "date", target = "date")
    @Mapping(source = "localNames", target = "localNames")
    CommonHolidays toModel(CommonHolidayVO commonHolidayVO);
    
    List<CommonHolidayVO> toVOList(List<CommonHolidays> commonHolidaysList);
    
    List<CommonHolidays> toModelList(List<CommonHolidayVO> commonHolidayVOList);
} 