package com.example.Portal.Services;

import com.example.Portal.Dto.*;
import com.example.Portal.Models.Employee;
import com.example.Portal.Models.Holiday;
import org.hibernate.jdbc.Work;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmpServices {
    public void save(Employee emp);

    public void save(Holiday holiday);

    public void add(EmployeeDto employeeDto);
    public Employee getByEmployeeEmail(String email);

    List<EmployeeDto> findallemployee();

    List<HolidayDto> findallholidays();

    List<Workanniversarydto> findallanniversary();

    List<NewHireDto> findallnewhires();
    List<BirthdayDto> findallbirthdays();

    Workanniversarydto mapToWorkAnniversary(Employee employee);
    NewHireDto maptonewhire(Employee employee);

    BirthdayDto maptobirthday(Employee employee);

    Employee getEmployeeById(int id);

//    Leave getEmployeById(int id);

    void deleteemplo(int id);

    int getAvailabledays(String type);

    public ResponseEntity<Object> checkin(LoginDto loginDto);

    public EmployeeDto viewuser(String id);

    public String updateUser(int id,Employee employee);

    public String addholiday(HolidayDto holidayDto);
}
