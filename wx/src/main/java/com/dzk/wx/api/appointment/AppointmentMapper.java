package com.dzk.wx.api.appointment;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {
    
    @Select("SELECT * FROM appointment WHERE parent_id = #{parentId}")
    List<Appointment> getAppointmentsByParentId(@Param("parentId") Long parentId);
    
    @Select("SELECT * FROM appointment WHERE doctor_id = #{doctorId}")
    List<Appointment> getAppointmentsByDoctorId(@Param("doctorId") Long doctorId);
    
    @Select("SELECT * FROM appointment WHERE id = #{id}")
    Appointment getAppointmentById(@Param("id") Long id);
}
