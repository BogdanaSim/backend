package com.hospital.backend.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.sk.PrettyTable;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;

@Entity
@Table(name = "Schedules")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @ToString.Exclude
    @JsonIgnore
    private Department department;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<Day> days;

    @Column(name = "role_staff")
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleStaff roleStaff;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ScheduleStatus scheduleStatus;

    public String toString(List<User> users) {
        List<String> strings = new java.util.ArrayList<>(days.stream()
                .map(object -> Objects.toString(object, null))
                .toList());
        strings.add(0, "Firstname Lastname");
        String[] array = strings.toArray(new String[0]);
        PrettyTable table = new PrettyTable(array);
        for (User user : users) {
            List<Shift> shifts = new ArrayList<>();
            for (Day day : days) {
                if (day.getShifts().stream().anyMatch(o -> o.getUser().getId().equals(user.getId()))) {
                    shifts.add(day.getShifts().stream().filter(o -> o.getUser().getId().equals(user.getId())).findFirst().get());
                } else {
                    if (day.getDate().getDayOfWeek() == DayOfWeek.SATURDAY || day.getDate().getDayOfWeek() == DayOfWeek.SUNDAY)
                        shifts.add(new Shift(1L, " ", user, day));
                    else
                        shifts.add(new Shift(1L, "L", user, day));
                }

            }
            strings = new java.util.ArrayList<>(shifts.stream()
                    .map(object -> Objects.toString(object, null))
                    .toList());
            strings.add(0, user.getFirstName() + " " + user.getLastName());
            array = strings.toArray(new String[0]);
            table.addRow(array);
        }
        return "Schedule{"+department.getName()+" " + date.getMonth() + " " + date.getYear() +
                '}' + "\n" + table;
    }

    public Map<String,Boolean> getWeeksAndStatus(){
        Map<String,Boolean> result = new HashMap<>();
        LocalDate monthStart = date.withDayOfMonth(1);
        LocalDate monthEnd = date.withDayOfMonth(monthStart.lengthOfMonth());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Find the Monday on/before the start of the month
        LocalDate startDate = monthStart.with(DayOfWeek.MONDAY);
        if (startDate.isBefore(monthStart)) {
            LocalDate plus=startDate.plusWeeks(1).minusDays(1);
            result.put(monthStart.format(formatter) + ";" + plus.format(formatter),false);
            startDate = startDate.plusWeeks(1);

        }

        while (startDate.isBefore(monthEnd)) {
            LocalDate endDate = startDate.plusDays(6).isBefore(monthEnd) ? startDate.plusDays(6) : monthEnd;
            if(startDate.getDayOfWeek()==DayOfWeek.MONDAY && endDate.getDayOfWeek()==DayOfWeek.SUNDAY)
                result.put(startDate.format(formatter) + ";" + endDate.format(formatter),true);
            else{
                result.put(startDate.format(formatter) + ";" + endDate.format(formatter),false);

            }
            startDate = startDate.plusWeeks(1);
        }
        return result;
    }

    public int getNoHoursUser(User user,LocalDate startDate, LocalDate endDate){
        List<Shift> shiftsUser = new ArrayList<>();
        for(Day day:days){
            shiftsUser.addAll(day.getShifts().stream().filter(item->item.getUser()==user && checkDateInterval(day.getDate(),startDate,endDate)).toList());

        }
        int noHours=0;
        List<String> options12H = List.of(ShiftTypes.MORNING.getValue(),ShiftTypes.NIGHT.getValue());
        List<String> options8H = List.of(ShiftTypes.SHORT.getValue());
        List<String> optionsFree = List.of(ShiftTypes.SICK_LEAVE.getValue(),ShiftTypes.REST_LEAVE.getValue());


        for(Shift shift:shiftsUser){
            if(options12H.contains(shift.getType())){
                noHours+=12;
            } else if (options8H.contains(shift.getType()) || optionsFree.contains(shift.getType())) {
                noHours+=8;
            }
        }
        return noHours;
    }

    public int getNoHoursUser8H(User user,LocalDate startDate, LocalDate endDate){
        List<Shift> shiftsUser = new ArrayList<>();
        for(Day day:days){
            shiftsUser.addAll(day.getShifts().stream().filter(item->item.getUser()==user && checkDateInterval(day.getDate(),startDate,endDate)).toList());

        }
        int noHours=0;
        List<String> options12H = List.of(ShiftTypes.MORNING.getValue(),ShiftTypes.NIGHT.getValue());
        List<String> options8H = List.of(ShiftTypes.SHORT.getValue());
        List<String> optionsFree = List.of(ShiftTypes.SICK_LEAVE.getValue(),ShiftTypes.REST_LEAVE.getValue());


        for(Shift shift:shiftsUser){
            if(!Objects.equals(shift.getType(), ShiftTypes.FREE.getValue()))
                noHours+=8;
        }
        return noHours;
    }

    public int getNoWorkingDays(){
        int count = 0;
        for(Day day: days){
            DayOfWeek dayOfWeek = day.getDate().getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                count++;
            }
        }
        return count;
    }

    public int findHighestMultiple(int a) {
        int b1 = (a / 12) * 12;  // Find the largest multiple of 12 less than or equal to a

        while (b1 >= 0) {
            if ((a - b1) % 8 == 0) {
                return b1;
            }
            b1 -= 12;
        }

        return -1;  // Return -1 if no suitable b is found
    }

    public int getNoHoursMonth(User user){
        List<Shift> shiftsUser = new ArrayList<>();
        for(Day day:days){
            shiftsUser.addAll(day.getShifts().stream().filter(item->item.getUser()==user).toList());

        }
        int noHours=0;
        List<String> options12H = List.of(ShiftTypes.MORNING.getValue(),ShiftTypes.NIGHT.getValue());
        List<String> options8H = List.of(ShiftTypes.SHORT.getValue());
        List<String> optionsFree = List.of(ShiftTypes.SICK_LEAVE.getValue(),ShiftTypes.REST_LEAVE.getValue());


        for(Shift shift:shiftsUser){
            if(options12H.contains(shift.getType())){
                noHours+=12;
            } else if (options8H.contains(shift.getType()) || optionsFree.contains(shift.getType())) {
                noHours+=8;
            }
        }
        return noHours;
    }

    public String getWeekByDay(Day day){
        Map<String,Boolean> weeks = this.getWeeksAndStatus();
        for (Map.Entry<String,Boolean> entry : weeks.entrySet()){
            String sd = entry.getKey().split(";")[0];
            String ed=entry.getKey().split(";")[1];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(sd,formatter);
            LocalDate endDate = LocalDate.parse(ed,formatter);
            if(day.getDate().isEqual(startDate) || day.getDate().isEqual(endDate)|| (day.getDate().isAfter(startDate)&& day.getDate().isBefore(endDate))){
                return entry.getKey();
            }
        }
        return ";";

    }

    public String checkForCompleteUser(User user,int noHours){
        Map<String,Boolean> weeks = this.getWeeksAndStatus();
        for (Map.Entry<String,Boolean> entry : weeks.entrySet()){
            String sd = entry.getKey().split(";")[0];
            String ed=entry.getKey().split(";")[1];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(sd,formatter);
            LocalDate endDate = LocalDate.parse(ed,formatter);
            if( entry.getValue() && getNoHoursUser(user,startDate,endDate)+8<=noHours){
                return entry.getKey();
            }
        }
        return "";


    }

    public Day selectForCompleteMonth(User user){
        List<Shift> shiftsUser;
        Random rand = new Random();
        List<Day> freeDays=new ArrayList<>();
        for(Day day:days){
            shiftsUser = day.getShifts().stream().filter(item->item.getUser()==user).toList();
            if(shiftsUser.isEmpty())
                freeDays.add(day);
//            shiftsUser.addAll(day.getShifts().stream().filter(item->item.getUser()==user).toList());

        }
        return freeDays.get(rand.nextInt(freeDays.size()));

    }



    public Day selectDayWithShift(User user,String value){
        System.out.println(this.getNoHoursMonth(user) + user.toString() +"<" +this.getNoWorkingDays()*8);

        List<Shift> shiftsUser;
        Random rand = new Random();
        List<Day> shiftDays=new ArrayList<>();
        for(Day day:days){
            shiftsUser = day.getShifts().stream().filter(item->item.getUser()==user && Objects.equals(item.getType(), value)).toList();
            if(!shiftsUser.isEmpty())
                shiftDays.add(day);
//            shiftsUser.addAll(day.getShifts().stream().filter(item->item.getUser()==user).toList());

        }
        Day d = new Day();
        int maxVal = shiftDays.stream().map(s -> {
           return s.getNoShifts(value);
        }).max(Integer::compare).orElse(0);

        shiftDays=shiftDays.stream().filter(d1->d1.getNoShifts(value)==maxVal).toList();
        if(shiftDays.isEmpty()){
            System.out.println("got empty day");
            return new Day();

        }

        Day day  = shiftDays.get(rand.nextInt(shiftDays.size()));
        System.out.println(day.toString()+ " "+day.getShifts().toString());
        return day;



    }

    public Day selectDayWithShift1(User user,String value, String value2){
        System.out.println(this.getNoHoursMonth(user) + user.toString() +"<" +this.getNoWorkingDays()*8);

        List<Shift> shiftsUser;
        Random rand = new Random();
        List<Day> shiftDays=new ArrayList<>();
        for(Day day:days){
            shiftsUser = day.getShifts().stream().filter(item->item.getUser()==user && Objects.equals(item.getType(), value)).toList();
            if(!shiftsUser.isEmpty())
                shiftDays.add(day);
//            shiftsUser.addAll(day.getShifts().stream().filter(item->item.getUser()==user).toList());

        }
        Day d = new Day();
        int maxVal = shiftDays.stream().map(s -> s.getNoShifts(value)).max(Integer::compare).orElse(0);

        shiftDays=shiftDays.stream().filter(d1->d1.getNoShifts(value)==maxVal).toList();
        if(shiftDays.isEmpty()){
            System.out.println("got empty day - ");
            return new Day();


        }

        Day day  = shiftDays.get(rand.nextInt(shiftDays.size()));
        System.out.println(day.toString()+ " "+day.getShifts().toString());
        Shift shift = day.selectRandomShift(value);
        shift.setType(value2);
        day.setSpecificTypeShift(shift,value);
        return day;


    }

    public boolean checkDateInterval(LocalDate date, LocalDate startDate, LocalDate endDate){
        return (date.isEqual(startDate) || date.isEqual(endDate)|| (date.isAfter(startDate)&& date.isBefore(endDate)));
    }

    public Day selectDayUser(User user,String dates){
        String sd = dates.split(";")[0];
        String ed=dates.split(";")[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(sd,formatter);
        LocalDate endDate = LocalDate.parse(ed,formatter);
        Random rand = new Random();
        List<Day> validDays = days.stream().filter(item->checkDateInterval(item.getDate(),startDate,endDate) && !item.getShifts().stream()
                .map(Shift::getUser).distinct()
                .toList().contains(user)).toList();
//        if(validDays.isEmpty())
//            return new Day();


        return validDays.get(rand.nextInt(validDays.size()));
    }

    public void setSpecificDay(Day day){
//        int index = days.indexOf(new Day() {
//            public boolean equals(Day day1) {
//                return day.getDate().isEqual(day1.getDate());
//            }
//        });
//        days.set(index,day);
        OptionalInt optionalIndex = IntStream.range(0, days.size())
                .filter(i -> Objects.equals(days.get(i).getDate(), day.getDate()))
                .findFirst();
        if(optionalIndex.isEmpty())
            return;
        days.set(optionalIndex.getAsInt(),day);
    }

    public Day getDayWithoutUser(User user){
        Random rand = new Random();

        List<Day> validDays = days.stream().filter(item->!item.getShifts().stream()
                .map(Shift::getUser).distinct()
                .toList().contains(user)).toList();
//        if(validDays.isEmpty())
//            return new Day();


        return validDays.get(rand.nextInt(validDays.size()));

    }
}
