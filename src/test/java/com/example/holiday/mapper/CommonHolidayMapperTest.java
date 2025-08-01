package com.example.holiday.mapper;

import com.example.holiday.model.CommonHolidays;
import com.example.holiday.vo.CommonHolidayVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommonHolidayMapperTest {
    
    @Autowired
    private CommonHolidayMapper mapper;
    
    @Test
    void testToVO() {
        // Given
        CommonHolidays commonHolidays = new CommonHolidays("2024-01-01", Arrays.asList("New Year's Day", "Capodanno"));
        
        // When
        CommonHolidayVO result = mapper.toVO(commonHolidays);
        
        // Then
        assertNotNull(result);
        assertEquals("2024-01-01", result.getDate());
        assertEquals(Arrays.asList("New Year's Day", "Capodanno"), result.getLocalNames());
    }
    
    @Test
    void testToVO_Null() {
        // When
        CommonHolidayVO result = mapper.toVO(null);
        
        // Then
        assertNull(result);
    }
    
    @Test
    void testToModel() {
        // Given
        CommonHolidayVO commonHolidayVO = new CommonHolidayVO("2024-12-25", Arrays.asList("Christmas Day", "Natale"));
        
        // When
        CommonHolidays result = mapper.toModel(commonHolidayVO);
        
        // Then
        assertNotNull(result);
        assertEquals("2024-12-25", result.getDate());
        assertEquals(Arrays.asList("Christmas Day", "Natale"), result.getLocalNames());
    }
    
    @Test
    void testToModel_Null() {
        // When
        CommonHolidays result = mapper.toModel(null);
        
        // Then
        assertNull(result);
    }
    
    @Test
    void testToVOList() {
        // Given
        List<CommonHolidays> commonHolidaysList = Arrays.asList(
            new CommonHolidays("2024-01-01", Arrays.asList("New Year's Day", "Capodanno")),
            new CommonHolidays("2024-12-25", Arrays.asList("Christmas Day", "Natale"))
        );
        
        // When
        List<CommonHolidayVO> result = mapper.toVOList(commonHolidaysList);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("2024-01-01", result.get(0).getDate());
        assertEquals("2024-12-25", result.get(1).getDate());
    }
    
    @Test
    void testToVOList_Null() {
        // When
        List<CommonHolidayVO> result = mapper.toVOList(null);
        
        // Then
        assertNull(result);
    }
    
    @Test
    void testToModelList() {
        // Given
        List<CommonHolidayVO> commonHolidayVOList = Arrays.asList(
            new CommonHolidayVO("2024-01-01", Arrays.asList("New Year's Day", "Capodanno")),
            new CommonHolidayVO("2024-12-25", Arrays.asList("Christmas Day", "Natale"))
        );
        
        // When
        List<CommonHolidays> result = mapper.toModelList(commonHolidayVOList);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("2024-01-01", result.get(0).getDate());
        assertEquals("2024-12-25", result.get(1).getDate());
    }
    
    @Test
    void testToModelList_Null() {
        // When
        List<CommonHolidays> result = mapper.toModelList(null);
        
        // Then
        assertNull(result);
    }
    
    @Test
    void testMapperInjectedBySpring() {
        // Given
        CommonHolidays commonHolidays = new CommonHolidays("2024-01-01", Arrays.asList("New Year's Day", "Capodanno"));
        
        // When
        CommonHolidayVO result = mapper.toVO(commonHolidays);
        
        // Then
        assertNotNull(result);
        assertEquals("2024-01-01", result.getDate());
        assertEquals(Arrays.asList("New Year's Day", "Capodanno"), result.getLocalNames());
    }
    
    @Test
    void testMapperListConversion() {
        // Given
        List<CommonHolidays> commonHolidaysList = Arrays.asList(
            new CommonHolidays("2024-01-01", Arrays.asList("New Year's Day", "Capodanno")),
            new CommonHolidays("2024-12-25", Arrays.asList("Christmas Day", "Natale"))
        );
        
        // When
        List<CommonHolidayVO> result = mapper.toVOList(commonHolidaysList);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("2024-01-01", result.get(0).getDate());
        assertEquals("2024-12-25", result.get(1).getDate());
    }
} 