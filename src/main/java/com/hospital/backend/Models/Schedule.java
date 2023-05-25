package com.hospital.backend.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
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

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<Day> days;

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
                if (day.getShifts().stream().anyMatch(o -> o.getUser().getId().equals(user.getId()) )) {
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

        // Find the Monday on/before the start of the month
        LocalDate startDate = monthStart.with(DayOfWeek.MONDAY);
        if (startDate.isAfter(monthStart)) {
            startDate = startDate.minusWeeks(1);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
        List<String> optionsFree = List.of(ShiftTypes.SHORT.getValue());


        for(Shift shift:shiftsUser){
            if(options12H.contains(shift.getType())){
                noHours+=12;
            } else if (options8H.contains(shift.getType())) {
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
        if(validDays.size()==0){
            System.out.println(this.getNoHoursUser(user,startDate,endDate));
        }
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
        days.set(optionalIndex.getAsInt(),day);
    }
}
