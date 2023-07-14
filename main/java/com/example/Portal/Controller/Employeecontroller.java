package com.example.Portal.Controller;

import com.example.Portal.Dto.*;
import com.example.Portal.Models.ApplyLeave;
import com.example.Portal.Models.Employee;
import com.example.Portal.Services.EmpServices;
import com.example.Portal.Services.Loginservices;
import com.example.Portal.Services.RefreshTokenService;
import com.example.Portal.Services.ServiceImplementation.LoginService;
import com.example.Portal.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@CrossOrigin
public class Employeecontroller {

    EmpServices empServices;
    Loginservices loginservices;
    RefreshTokenService refreshTokenService;
    JwtUtils jwtUtils;
    LoginService loginService;

    @Autowired
    public Employeecontroller(EmpServices empServices,Loginservices loginservices,RefreshTokenService refreshTokenService,JwtUtils jwtUtils,LoginService loginService) {
        this.empServices = empServices;
        this.loginservices=loginservices;
        this.refreshTokenService=refreshTokenService;
        this.jwtUtils=jwtUtils;
        this.loginService=loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDTO) {

        return empServices.checkin(loginDTO);
    }

    @GetMapping("/view")
    public List<EmployeeDto> listEmployees() {
        List<EmployeeDto> employeeDtos = empServices.findallemployee();
        return employeeDtos;
    }
    @GetMapping("/holidays")
    public List<HolidayDto> listholiday(){
        List<HolidayDto> holidayDetails=empServices.findallholidays();
        return holidayDetails;
    }
    @GetMapping("/workanniversary")
    public List<Workanniversarydto> workanniversarydtos(){
        List<Workanniversarydto> work=empServices.findallanniversary();
        return  work;
    }
    @GetMapping("/newhire")
    public List<NewHireDto> hireanniversary(){
        List<NewHireDto> hire=empServices.findallnewhires();
           return hire;
    }
    @GetMapping("/birthdays")
    public List<BirthdayDto> birthdays(){
        List<BirthdayDto> bday=empServices.findallbirthdays();
        return bday;
    }
    @PostMapping("/add")
    public String Employeeadd(@RequestBody EmployeeDto employeeDto){
        empServices.add(employeeDto);
        return "New Employee Added";
    }
    @GetMapping("/employee/{id}/view")
    public EmployeeDto view(@PathVariable("id")String id ){

        return empServices.viewuser(id);
    }

    @Transactional
    @DeleteMapping("/employee/{id}/delete")
    public String deleteemplo(@PathVariable("id") int id){
        empServices.deleteemplo(id);
        return "Employee Details Deleted Successfully";
    }

    @PutMapping("/employee/{id}/update")
    public String save(@PathVariable("id")int id ,@RequestBody Employee employee){
       return empServices.updateUser(id,employee);
    }

    @PostMapping("/holidayadd")
    public String holiday(@RequestBody HolidayDto holidayDto){
        return empServices.addholiday(holidayDto);
    }

    @PutMapping("/employee/{id}/leave")
    public String Leave(@PathVariable("id") int id, @RequestBody ApplyLeave applyLeave){
        Employee Leave1=empServices.getEmployeeById(id);
        String type=applyLeave.getLeaveType();
        int available=empServices.getAvailabledays(type);
        if((int)(ChronoUnit.DAYS.between(applyLeave.getEndingDate(),applyLeave.getStartingDate()))>available){
            return "Leave Request is sent to Manager";
        }
        else
            return "No available Days";
    }
}
