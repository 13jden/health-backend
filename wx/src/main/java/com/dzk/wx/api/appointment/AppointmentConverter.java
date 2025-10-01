package com.dzk.wx.api.appointment;

import org.springframework.stereotype.Component;

@Component
public class AppointmentConverter {
    
    public AppointmentDto toDto(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        
        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());
        dto.setParentId(appointment.getParentId());
        dto.setDoctorId(appointment.getDoctorId());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setStatus(appointment.getStatus());
        return dto;
    }
    
    public Appointment toEntity(AppointmentDto.Input input) {
        if (input == null) {
            return null;
        }
        
        Appointment appointment = new Appointment();
        appointment.setParentId(input.getParentId());
        appointment.setDoctorId(input.getDoctorId());
        appointment.setAppointmentTime(input.getAppointmentTime());
        appointment.setStatus(input.getStatus());
        return appointment;
    }

    public AppointmentDto.Detail toDetail(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        
        AppointmentDto.Detail detail = new AppointmentDto.Detail();
        detail.setId(appointment.getId());
        detail.setParentId(appointment.getParentId());
        detail.setDoctorId(appointment.getDoctorId());
        detail.setAppointmentTime(appointment.getAppointmentTime());
        detail.setStatus(appointment.getStatus());
        detail.setCreateTime(appointment.getCreateTime() != null ? appointment.getCreateTime().toString() : null);
        detail.setUpdateTime(appointment.getUpdateTime() != null ? appointment.getUpdateTime().toString() : null);
        return detail;
    }
}