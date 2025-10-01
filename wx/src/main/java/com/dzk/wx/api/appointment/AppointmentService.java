package com.dzk.wx.api.appointment;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService extends ServiceImpl<AppointmentMapper, Appointment> {

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private AppointmentConverter appointmentConverter;

    public List<Appointment> getAppointmentsByParentId(Long parentId) {
        return appointmentMapper.getAppointmentsByParentId(parentId);
    }

    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentMapper.getAppointmentsByDoctorId(doctorId);
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentMapper.getAppointmentById(id);
    }

    public Appointment saveAppointment(Appointment appointment) {
        int result = appointmentMapper.insert(appointment);
        return appointmentMapper.getAppointmentById(appointment.getId());
    }

    public boolean updateAppointment(Appointment appointment) {
        return appointmentMapper.updateById(appointment) > 0;
    }

    public boolean deleteAppointment(Long id) {
        return appointmentMapper.deleteById(id) > 0;
    }
}
