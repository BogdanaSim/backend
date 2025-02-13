package com.hospital.backend.Services;

import com.hospital.backend.Exceptions.ScheduleNotFoundException;
import com.hospital.backend.Models.*;
import com.hospital.backend.Repositories.DaysRepository;
import com.hospital.backend.Repositories.SchedulesRepository;
import com.hospital.backend.Repositories.VacationRequestsRepository;
import com.hospital.backend.RulesConfig.DroolsBeanFactory;
import lombok.RequiredArgsConstructor;

import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScheduleService implements IScheduleService {


    private KieSession kieSession;
    private final SchedulesRepository schedulesRepository;
    private final DaysRepository daysRepository;
    private final VacationRequestsRepository vacationRequestsRepository;
    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);


    @Override
    public Schedule save(Schedule schedule) {
        logger.info("save schedule: " + schedule.getDate());
        Optional<Schedule> scheduleOptional = this.schedulesRepository.findByDate(schedule.getDate());
        scheduleOptional.ifPresent(value -> schedule.setId(value.getId()));
        return schedulesRepository.save(schedule);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("delete schedule: " + id);
        schedulesRepository.findById(id).orElseThrow(ScheduleNotFoundException::new);
        schedulesRepository.deleteById(id);
    }

    @Override
    public Schedule findById(Long id) {
        logger.info("find schedule with id: " + id);
        return schedulesRepository.findById(id).orElseThrow(ScheduleNotFoundException::new);

    }

    @Override
    public Schedule update(Schedule schedule) {
        logger.info("update schedule: " + schedule.getId());
        Schedule sc = schedulesRepository.findById(schedule.getId()).orElseThrow(ScheduleNotFoundException::new);
        if(schedule.getDays()==null)
            schedule.setDays(sc.getDays());
        return schedulesRepository.save(schedule);
    }

    public void addNewDaysSchedule(Schedule schedule) {
        int year = schedule.getDate().getYear();
        Month month = schedule.getDate().getMonth();
        YearMonth ym = YearMonth.of(year, month);
        LocalDate firstOfMonth = ym.atDay(1);
        LocalDate firstOfFollowingMonth = ym.plusMonths(1).atDay(1);
        firstOfMonth.datesUntil(firstOfFollowingMonth).forEach(date -> {
            Day day = new Day();
            day.setSchedule(schedule);
            day.setDate(date);
            Optional<Day> dayOptional = this.daysRepository.findByDate(date);
            dayOptional.ifPresent(value -> day.setId(value.getId()));
            if (dayOptional.isEmpty()) {
                daysRepository.save(day);
            }

        });

    }

    public List<Day> getDaysWithVacationShifts(Schedule schedule, List<Day> days) {
        int year = schedule.getDate().getYear();
        Month month = schedule.getDate().getMonth();
        YearMonth ym = YearMonth.of(year, month);
        LocalDate firstOfMonth = ym.atDay(1);
        LocalDate lastOfMonth = ym.atEndOfMonth();
        List<VacationRequest> vacationRequests = vacationRequestsRepository.findRequestsWithinDateRange(firstOfMonth, lastOfMonth);

        for (VacationRequest vacationRequest : vacationRequests) {
            LocalDate overlappingStart = vacationRequest.getStartDate().isBefore(firstOfMonth) ? firstOfMonth : vacationRequest.getStartDate();
            LocalDate overlappingEnd = vacationRequest.getEndDate().isAfter(lastOfMonth) ? lastOfMonth : vacationRequest.getEndDate();
            days.stream()
                    .filter(event -> (event.getDate().isEqual(overlappingStart) || event.getDate().isAfter(overlappingStart))
                            && (event.getDate().isEqual(overlappingEnd) || event.getDate().isBefore(overlappingEnd)))
                    .forEach(event ->
                    {
                        List<Shift> shifts = event.getShifts();
                        Shift newShift = new Shift();
                        newShift.setDay(event);
                        newShift.setUser(vacationRequest.getUser());
                        newShift.setType(vacationRequest.getType());
                        shifts.add(newShift);
                        event.setShifts(shifts);
                    });

        }

//        return days;
        return this.setFreeDaysForFirstDay(schedule,days);

    }


    public List<Day> setFreeDaysForFirstDay(Schedule schedule, List<Day> days){
        Optional<Schedule> schedule1=schedulesRepository.findSchedulesByDateAndDepartmentAndRoleStaffAndScheduleStatus(schedule.getDate().minusMonths(1),schedule.getDepartment(),schedule.getRoleStaff(),ScheduleStatus.VALID);
        if(schedule1.isPresent()){
            Day firstDay = days.stream().filter(d->Objects.equals(d.getDate(),schedule.getDate())).findFirst().get();
            LocalDate maxDate =  Collections.max(schedule1.get().getDays(), Comparator.comparing(Day::getDate)).getDate();
            Day lastDay = schedule1.get().getDays().stream().filter(d->Objects.equals(d.getDate(),maxDate)).findFirst().get();
            List<Shift> shifts = lastDay.getShifts();
            shifts.stream().filter(s-> Objects.equals(s.getType(), ShiftTypes.NIGHT.getValue())).forEach(
                    s->{
                        if(firstDay.getShifts().stream().filter(s1->s1.getUser()==s.getUser()).findFirst().isEmpty()){
                            List<Shift> shifts1 = firstDay.getShifts();
                            Shift newShift = new Shift();
                            newShift.setDay(firstDay);
                            newShift.setUser(s.getUser());
                            newShift.setType(ShiftTypes.FREE.getValue());
                            shifts1.add(newShift);
                            firstDay.setShifts(shifts1);
                        }
                    }
            );
            days.stream()
                    .filter(d -> d.getDate().equals(firstDay.getDate()))
                    .findFirst()
                    .ifPresent(d1 -> {
                        int index = days.indexOf(d1);
                        days.set(index, firstDay);
                    });
        }

        return days;
    }

    public List<Day> getNewDaysSchedule(Schedule schedule, User user) {
        List<Day> newDaysSchedule = new ArrayList<>();
        int year = schedule.getDate().getYear();
        Month month = schedule.getDate().getMonth();
        YearMonth ym = YearMonth.of(year, month);
        LocalDate firstOfMonth = ym.atDay(1);
        LocalDate firstOfFollowingMonth = ym.plusMonths(1).atDay(1);
        firstOfMonth.datesUntil(firstOfFollowingMonth).forEach(date -> {
            Day day = new Day();
            day.setSchedule(schedule);
            day.setDate(date);
            day.setShifts(new ArrayList<>());
            newDaysSchedule.add(day);


        });
        kieSession = new DroolsBeanFactory().getKieSession();

        for (Day day : newDaysSchedule) {
            kieSession.insert(day);

        }
        kieSession.setGlobal("user", user);
        kieSession.fireAllRules();
        kieSession.dispose();
        return newDaysSchedule;
    }

    @Transactional
    public Schedule generateNew12hDaysSchedule(Schedule schedule, List<User> users) {
        if(schedule.getId()!=null){
            daysRepository.deleteAll(daysRepository.findDaysBySchedule(schedule));
        }
        List<Day> newDaysList = new ArrayList<>();
        int year = schedule.getDate().getYear();
        Month month = schedule.getDate().getMonth();
        YearMonth ym = YearMonth.of(year, month);
        LocalDate firstOfMonth = ym.atDay(1);
        LocalDate firstOfFollowingMonth = ym.plusMonths(1).atDay(1);
        Schedule finalSchedule = schedule;
        List<Day> finalNewDaysList = newDaysList;
        schedule.setScheduleStatus(ScheduleStatus.INVALID);
        firstOfMonth.datesUntil(firstOfFollowingMonth).forEach(date -> {
            Day day = new Day();
            day.setSchedule(finalSchedule);
            day.setDate(date);
            day.setShifts(new ArrayList<>());
            finalNewDaysList.add(day);


        });
        kieSession = new DroolsBeanFactory().getKieSession(ResourceFactory.newClassPathResource("com.hospital.backend.rules/ScheduleRules_12h.drl"));
        newDaysList = getDaysWithVacationShifts(schedule,newDaysList);
        for (Day day : newDaysList) {
            kieSession.insert(day);

        }
        schedule.setDays(newDaysList);
        kieSession.setGlobal("users", users);
        kieSession.setGlobal("schedule", schedule);
        kieSession.fireAllRules();
        kieSession.dispose();

        kieSession = new DroolsBeanFactory().getKieSession(ResourceFactory.newClassPathResource("com.hospital.backend.rules/ScheduleRules_12h_Short.drl"));
        schedule.setDays(newDaysList);
        for (User user : users) {
            kieSession.insert(user);
        }
        kieSession.setGlobal("schedule", schedule);
        kieSession.fireAllRules();
        kieSession.dispose();
        schedule= (Schedule) kieSession.getGlobal("schedule");
        kieSession = new DroolsBeanFactory().getKieSession(ResourceFactory.newClassPathResource("com.hospital.backend.rules/ScheduleRules_12h_Complete.drl"));
        for (User user : users) {
            kieSession.insert(user);
        }
        kieSession.setGlobal("schedule", schedule);
        kieSession.fireAllRules();
        kieSession.dispose();
        Schedule newSchedule = (Schedule) kieSession.getGlobal("schedule");


        kieSession = new DroolsBeanFactory().getKieSession(ResourceFactory.newClassPathResource("com.hospital.backend.rules/ScheduleRules_12h_Free.drl"));
        newDaysList = newSchedule.getDays();
        for (Day day : newDaysList) {
            kieSession.insert(day);

        }
        kieSession.fireAllRules();
        kieSession.dispose();
        newSchedule.setDays(newDaysList);
        schedule=newSchedule;

        if(schedule.getId()!=null) {

            daysRepository.saveAll(schedule.getDays());
            schedulesRepository.setTypeForSchedule(schedule.getScheduleType(),schedule.getId());
            return schedule;

        }
        return schedulesRepository.save(schedule);
//        return schedule;
    }



    @Transactional
    public Schedule generateNew8hDaysSchedule(Schedule schedule, List<User> users) {
        if(schedule.getId()!=null){
            daysRepository.deleteAll(daysRepository.findDaysBySchedule(schedule));
        }
        List<Day> newDaysList = new ArrayList<>();
        int year = schedule.getDate().getYear();
        Month month = schedule.getDate().getMonth();
        YearMonth ym = YearMonth.of(year, month);
        LocalDate firstOfMonth = ym.atDay(1);
        LocalDate firstOfFollowingMonth = ym.plusMonths(1).atDay(1);
        Schedule finalSchedule = schedule;
        List<Day> finalNewDaysList = newDaysList;
        schedule.setScheduleStatus(ScheduleStatus.INVALID);
        firstOfMonth.datesUntil(firstOfFollowingMonth).forEach(date -> {
            Day day = new Day();
            day.setSchedule(finalSchedule);
            day.setDate(date);
            day.setShifts(new ArrayList<>());
            finalNewDaysList.add(day);


        });
        kieSession = new DroolsBeanFactory().getKieSession(ResourceFactory.newClassPathResource("com.hospital.backend.rules/ScheduleRules_8h.drl"));
        newDaysList = getDaysWithVacationShifts(schedule,newDaysList);
        for (Day day : newDaysList) {
            kieSession.insert(day);

        }
        schedule.setDays(newDaysList);
        kieSession.setGlobal("users", users);
        kieSession.setGlobal("schedule", schedule);
        kieSession.fireAllRules();
        kieSession.dispose();
        schedule = (Schedule) kieSession.getGlobal("schedule");
        kieSession = new DroolsBeanFactory().getKieSession(ResourceFactory.newClassPathResource("com.hospital.backend.rules/ScheduleRules_8h_Complete.drl"));
        schedule.setDays(newDaysList);
        for (User user : users) {
            kieSession.insert(user);
        }
        kieSession.setGlobal("schedule", schedule);
        kieSession.fireAllRules();
        kieSession.dispose();
        Schedule newSchedule = (Schedule) kieSession.getGlobal("schedule");

        kieSession = new DroolsBeanFactory().getKieSession(ResourceFactory.newClassPathResource("com.hospital.backend.rules/ScheduleRules_8h_Free.drl"));
        newDaysList = newSchedule.getDays();
        for (Day day : newDaysList) {
            kieSession.insert(day);

        }
        kieSession.fireAllRules();
        kieSession.dispose();
        newSchedule.setDays(newDaysList);
        schedule=newSchedule;

        if(schedule.getId()!=null) {
            daysRepository.saveAll(schedule.getDays());
            schedulesRepository.setTypeForSchedule(schedule.getScheduleType(),schedule.getId());
            return schedule;

        }
        return schedulesRepository.save(schedule);
    }

    public List<LocalDate> getDatesByDepartment(Long idDepartment) {
        Department department=new Department();
        department.setId(idDepartment);
        List<Schedule> schedules = schedulesRepository.findByDepartmentAndScheduleStatus(department,ScheduleStatus.VALID);

        return schedules.stream()
                .map(Schedule::getDate)
                .collect(Collectors.toList());
    }

    public Schedule findScheduleByDateAndDepartment(LocalDate date, Long departmentId){
        Department department = new Department();
        department.setId(departmentId);
        return schedulesRepository.findScheduleByDateAndDepartment(date,department).get();
    }
    public Schedule findScheduleByDateAndDepartmentAndRoleStaff(LocalDate date, Long departmentId, String roleStaff){
        Department department = new Department();
        department.setId(departmentId);
        return schedulesRepository.findSchedulesByDateAndDepartmentAndRoleStaff(date,department,RoleStaff.valueOf(roleStaff)).orElseThrow(ScheduleNotFoundException::new);
    }

    public Schedule findSchedulesByDateAndDepartmentAndRoleStaffAndScheduleStatus(LocalDate date,  Long departmentId, String roleStaff, String scheduleStatus){
        Department department = new Department();
        department.setId(departmentId);
        Optional<Schedule> schedule = schedulesRepository.findSchedulesByDateAndDepartmentAndRoleStaffAndScheduleStatus(date,department,RoleStaff.valueOf(roleStaff),ScheduleStatus.valueOf(scheduleStatus));
        return schedule.orElseGet(Schedule::new);
    }

}
