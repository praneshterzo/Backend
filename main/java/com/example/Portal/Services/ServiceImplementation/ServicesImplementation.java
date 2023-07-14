package com.example.Portal.Services.ServiceImplementation;

import com.example.Portal.Dto.*;
import com.example.Portal.Handler.ResponseHandler;
import com.example.Portal.Models.Employee;
import com.example.Portal.Models.Holiday;
import com.example.Portal.Models.Leave;
import com.example.Portal.Models.Login;
import com.example.Portal.Repoistry.HolidayRepoistry;
import com.example.Portal.Repoistry.Leaverepoistry;
import com.example.Portal.Repoistry.EmployeeRepoistry;
import com.example.Portal.Repoistry.LoginRepoistry;
import com.example.Portal.Services.EmpServices;
import com.example.Portal.Services.Loginservices;
import com.example.Portal.Services.RefreshTokenService;
import com.example.Portal.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ServicesImplementation implements EmpServices {

    EmployeeRepoistry employeerepo;

    Leaverepoistry leaverepoistry;

    HolidayRepoistry holidayRepoistry;

    LoginRepoistry loginRepoistry;

    EmpServices empServices;

    Loginservices loginservices;

    RefreshTokenService refreshTokenService;

    JwtUtils jwtUtils;

    LoginService loginService;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    public ServicesImplementation(EmployeeRepoistry employeerepo, Leaverepoistry leaverepoistry, HolidayRepoistry holidayRepoistry, LoginRepoistry loginRepoistry, EmpServices empServices, Loginservices loginservices, RefreshTokenService refreshTokenService, JwtUtils jwtUtils, LoginService loginService, List<EmployeeDto> emplist) {
        this.employeerepo = employeerepo;
        this.leaverepoistry = leaverepoistry;
        this.holidayRepoistry = holidayRepoistry;
        this.loginRepoistry = loginRepoistry;
        this.empServices = empServices;
        this.loginservices = loginservices;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtils = jwtUtils;
        this.loginService = loginService;
        Emplist = emplist;
    }

    public List<EmployeeDto> Emplist;
    @Override
    public void save(Employee emp) {
        employeerepo.save(emp);
    }

    @Override
    public void save(Holiday holiday) {
        holidayRepoistry.save(holiday);
    }


    @Override
    public Employee getByEmployeeEmail(String email) {
        return employeerepo.findByEmpEmail(email);
    }

    @Override
    public List<EmployeeDto> findallemployee() {
        List<Employee> emp=employeerepo.findAll();
        return emp.stream().map((empl)-> mapToEmployeeDto(empl)).collect(Collectors.toList());
    }
    @Override
    public List<HolidayDto> findallholidays(){
        List<Holiday> holi=holidayRepoistry.findAll();
        return holi.stream().map((hol)-> mapToHolidayDto(hol)).collect(Collectors.toList());
    }

    @Override
    public List<Workanniversarydto> findallanniversary() {
        List<Employee> emp=employeerepo.findAll();
        return emp.stream().filter(i->
                        Stream.of(i.getEmpJoiningDate(), new Date())
                                .mapToLong(Date::getTime)
                                .mapToObj(time -> {
                                    Date d = new Date(time);
                                    return new int[]{d.getDate(), d.getMonth()};
                                })
                                .distinct()
                                .count() == 1
                )
                .map(this::mapToWorkAnniversary).toList();
    }

    @Override
    public List<NewHireDto> findallnewhires() {
        List<Employee> emp=employeerepo.findAll();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1); // Subtract one month from the current date
        return emp.stream().filter(i ->
                        i.getEmpJoiningDate().after(calendar.getTime()))
                .map(this::maptonewhire)
                .collect(Collectors.toList());
    }

    @Override
    public List<BirthdayDto> findallbirthdays() {
        List<Employee> emp=employeerepo.findAll();
        return emp.stream().filter(i->
                Stream.of(i.getEmpDatefBirth(), new Date())
                        .mapToLong(Date::getTime)
                        .mapToObj(time -> {
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(time);
                            return new int[]{cal.get(Calendar.DATE), cal.get(Calendar.MONTH)};
                        })
                        .distinct()
                        .count()==1
        ).map(this::maptobirthday).collect(Collectors.toList());
    }

    @Override
    public Workanniversarydto mapToWorkAnniversary(Employee employee) {
        Workanniversarydto workanniversarydto= Workanniversarydto.builder()
                .empName(employee.getEmpFirstname())
                .empDepartment(employee.getEmpDepartment())
                .empJoiningdate(employee.getEmpJoiningDate())
                .build();

        return workanniversarydto;

    }

    @Override
    public NewHireDto maptonewhire(Employee employee) {
        NewHireDto newHireDto=NewHireDto.builder()
                .name(employee.getEmpFirstname())
                .department(employee.getEmpDepartment())
                .JoiningDate(employee.getEmpJoiningDate())
                .build();
        return newHireDto;
    }

    @Override
    public BirthdayDto maptobirthday(Employee employee) {
            BirthdayDto birthdayDto=BirthdayDto.builder()
                    .name(employee.getEmpFirstname())
                    .designation(employee.getEmpDesignation())
                    .Dateofbirth(employee.getEmpDatefBirth())
                    .build();
            return birthdayDto;
    }


    @Override
    public Employee getEmployeeById(int id) {

        return employeerepo.findByEmpId(id);
    }

    @Override
    public void deleteemplo(int id) {

        employeerepo.deleteByEmpId(id);
    }

    @Override
    public int getAvailabledays(String type) {
        if (type.equals("Medeicalleave")) {
            return Leave.Medicalleave;
        }
        else if (type.equals("PaidLeave")) {
            return Leave.PaidLeave;
        }
        else
            return Leave.Vacationdays;
    }

    @Override
    public ResponseEntity<Object> checkin(LoginDto loginDTO) {

        Employee employee = empServices.getByEmployeeEmail(loginDTO.getEmail());
        if(employee==null){
            return ResponseHandler.generateResponse("User not found , please sign up again!!!", HttpStatus.BAD_REQUEST);
        }

        if(!employee.isActive())
            return ResponseHandler.generateResponse("User is not active", HttpStatus.FORBIDDEN);

        AuthenticationResponseDto responseDto = loginService.authenticate(loginDTO);
        System.out.println(responseDto);
        if (responseDto != null)
            return ResponseHandler.generateResponse( responseDto,"Login Successful", HttpStatus.OK);
        else
            return ResponseHandler.generateResponse("Try again",HttpStatus.EXPECTATION_FAILED);

    }

    @Override
    public EmployeeDto viewuser(String id) {
        Employee employee = empServices.getEmployeeById((Integer.parseInt(id)));
        EmployeeDto employeeDto= new EmployeeDto();
        employeeDto.setEmpEmail(employee.getEmpEmail());
        employeeDto.setEmpPassword(employee.getEmpPassword());
        employeeDto.setEmpDateofBirth(employee.getEmpDatefBirth());
        employeeDto.setEmpRole(employee.getEmpRole());
        employeeDto.setEmpManager(employee.getEmpManager());
        employeeDto.setEmpSalary(employee.getEmpSalary());
        employeeDto.setEmpMobileNumber(employee.getEmpMobileNumber());
        employeeDto.setEmpJoiningDate(employee.getEmpJoiningDate());
        employeeDto.setEmpEmail(employee.getEmpEmail());
        employeeDto.setEmpDepartment(employee.getEmpDepartment());
        employeeDto.setEmpEmploymentType(employee.getEmpEmploymentType());
        employeeDto.setEmpAddress(employee.getEmpAddress());
        employeeDto.setEmpDesignation(employee.getEmpDesignation());
        employeeDto.setEmpFirstname(employee.getEmpFirstname());
        employeeDto.setEmpLastname(employee.getEmpLastname());
        employeeDto.setPaternityLeave(employee.getPaternityLeave());
        employeeDto.setSickLeave(employee.getSickLeave());
        employeeDto.setEarnedLeave(employee.getEarnedLeave());
        return employeeDto;
    }

    @Override
    public String updateUser(int id, Employee employee) {
        String errorMsg="";
        Employee empl = empServices.getEmployeeById(id);
        if(employee.getEmpEmail().endsWith("@terzo.com"))
            empl.setEmpEmail(employee.getEmpEmail());
        else
            errorMsg+="Enter Valid email";
        empl.setEmpDatefBirth(employee.getEmpDatefBirth());
        empl.setEmpPassword(passwordEncoder.encode(employee.getEmpPassword()));
        empl.setEmpAddress(employee.getEmpAddress());
        empl.setEmpDesignation(employee.getEmpDesignation());
        empl.setApplyLeaves(employee.getApplyLeaves());
        empl.setEmpEmploymentType(employee.getEmpEmploymentType());
        empl.setEmpDepartment(employee.getEmpDepartment());
        empl.setEmpRole(employee.getEmpRole());
        empl.setEmpJoiningDate(employee.getEmpJoiningDate());
        empl.setEmpManager(employee.getEmpManager());
        empl.setEmpSalary(employee.getEmpSalary());
        empl.setEmpMobileNumber(employee.getEmpMobileNumber());
        empl.setLeaves(employee.getLeaves());
        empl.setLogins(employee.getLogins());
        empServices.save(empl);
        if(errorMsg.equals(""))
            return errorMsg;
        else
            return "Updated Successfully";
    }

    @Override
    public String addholiday(HolidayDto holidayDto) {
        Holiday holiday=new Holiday();
        holiday.setHolidayDate(holidayDto.getHolidayDate());
        holiday.setHolidayEvent(holidayDto.getHolidayEvent());
        empServices.save(holiday);
        return "Holiday is added";
    }

    private EmployeeDto mapToEmployeeDto(Employee empl){
        EmployeeDto employeeDto=EmployeeDto.builder()
                .empId(empl.getEmpId())
                .empFirstname(empl.getEmpFirstname())
                .empLastname(empl.getEmpLastname())
                .empPassword(empl.getEmpPassword())
                .sickLeave(empl.getSickLeave())
                .paternityLeave(empl.getPaternityLeave())
                .earnedLeave(empl.getEarnedLeave())
                .empDateofBirth(empl.getEmpDatefBirth())
                .empAddress(empl.getEmpAddress())
                .empDepartment(empl.getEmpDepartment())
                .empDesignation(empl.getEmpDesignation())
                .empEmail(empl.getEmpEmail())
                .empJoiningDate(empl.getEmpJoiningDate())
                .empRole(empl.getEmpRole())
                .empEmploymentType(empl.getEmpEmploymentType())
                .empManager(empl.getEmpManager())
                .empMobileNumber(empl.getEmpMobileNumber())
                .empSalary(empl.getEmpSalary())
                .empRole(empl.getEmpRole())

                .build();
        return employeeDto;
    }

    private HolidayDto mapToHolidayDto(Holiday hol) {
        HolidayDto holidayDto=HolidayDto.builder()
                .holidayDate(hol.getHolidayDate())
                .holidayEvent(hol.getHolidayEvent())
                .build();
        return holidayDto;

    }

    @Override
    public void add(EmployeeDto employeeDto) {
        Employee emp=new Employee();
        Login login=new Login();
        if(employeeDto.getEmpEmail().endsWith("@terzo.com"))
            emp.setEmpEmail(employeeDto.getEmpEmail());
        emp.setEmpPassword(passwordEncoder.encode(employeeDto.getEmpPassword()));
        emp.setEmpDatefBirth(employeeDto.getEmpDateofBirth());
        emp.setRole(loginservices.findByName(employeeDto.getEmpRole()));
        emp.setEmpRole(employeeDto.getEmpRole());
        emp.setEmpManager(employeeDto.getEmpManager());
        emp.setEmpSalary(employeeDto.getEmpSalary());
        emp.setEmpMobileNumber(employeeDto.getEmpMobileNumber());
        emp.setEmpJoiningDate(employeeDto.getEmpJoiningDate());
        emp.setEmpEmail(employeeDto.getEmpEmail());
        emp.setEmpDepartment(employeeDto.getEmpDepartment());
        emp.setEmpEmploymentType(employeeDto.getEmpEmploymentType());
        emp.setEmpAddress(employeeDto.getEmpAddress());
        emp.setEmpDesignation(employeeDto.getEmpDesignation());
        emp.setActive(true);
        emp.setEmpFirstname(employeeDto.getEmpFirstname());
        emp.setEmpLastname(employeeDto.getEmpLastname());
        emp.setPaternityLeave(employeeDto.getPaternityLeave());
        emp.setSickLeave(employeeDto.getSickLeave());
        emp.setEarnedLeave(employeeDto.getEarnedLeave());
        empServices.save(emp);
        Employee id=empServices.getByEmployeeEmail(emp.getEmpEmail());
        emp.setEmpId(id.getEmpId());
        login.setEmpId(emp.getEmpId());
        login.setUserEmail(employeeDto.getEmpEmail());
        login.setUserPassword(passwordEncoder.encode(employeeDto.getEmpPassword()));
        login.setRole1(loginservices.findByName(employeeDto.getEmpRole()));
        loginservices.save(login);
    }


}
