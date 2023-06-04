package com.hospital.backend.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;

@Entity
@Table(name = "days")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<Shift> shifts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    @ToString.Exclude
    @JsonIgnore
    private Schedule schedule;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Day day = (Day) o;
        return getId() != null && Objects.equals(getId(), day.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        if (date != null) {
            return "Day{" + date.getDayOfMonth() + '}';
        } else {
            return "Day{date=null}";
        }
    }
    public boolean checkStatusDay(Map<String,Boolean> statusWeeks){
        boolean result = false;
        for (var entry : statusWeeks.entrySet()) {
            List<String> dates = List.of(entry.getKey().split(";"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
            LocalDate startDate = LocalDate.parse(dates.get(0), formatter);
            LocalDate endDate = LocalDate.parse(dates.get(1), formatter);
            if(date.isAfter(startDate) && date.isBefore(endDate))
            {
                result = entry.getValue();
                break;
            }
        }
        return result;
    }

    public void setNoShifts(Schedule newSchedule, List<User> users,int noMin,String value){
        Random rand = new Random();
        String str = this.schedule.getWeekByDay(this);
//        String sd = str.split(";")[0];
//        String ed=str.split(";")[1];
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate startDate = LocalDate.parse(sd,formatter);
//        LocalDate endDate = LocalDate.parse(ed,formatter);
        while(getNoShifts(value)<noMin){
            List<User> takenUsers =   shifts.stream().distinct()
                    .map(Shift::getUser)
                    .toList();//.stream().filter(user -> newSchedule.getNoHoursUser(user,startDate,endDate)+12>40).toList();
//            int minValue = users.stream()
//                    .mapToInt(item->newSchedule.getNoHoursUser(item,startDate,endDate))
//                    .min()
//                    .orElse(0);
//            List<User> validUsers = new ArrayList<>(users.stream().filter(item -> !takenUsers.contains(item) && newSchedule.getNoHoursUser(item, startDate, endDate) + 12 <= 24 ).toList());
            List<User> validUsers = new ArrayList<>(users.stream().filter(item -> !takenUsers.contains(item) && newSchedule.getNoHoursMonth(item) + 12 <= newSchedule.findHighestMultiple(newSchedule.getNoWorkingDays()*8) ).toList());
            int minValue = validUsers.stream()
                    .mapToInt(newSchedule::getNoHoursMonth)
                    .min()
                    .orElse(0);
            validUsers=validUsers.stream().filter(i->newSchedule.getNoHoursMonth(i)== minValue).toList();
            if(validUsers.isEmpty()) {
                System.out.println("special");
                return;
            }
            User randomUser = validUsers.get(rand.nextInt(validUsers.size()));


            Shift newShift = new Shift();
            newShift.setDay(this);
            newShift.setUser(randomUser);
            newShift.setType(value);
            this.shifts.add(newShift);
            List<Shift> userShifts = new ArrayList<>();
            userShifts.add(newShift);
            randomUser.setShifts(userShifts);
            newSchedule.setSpecificDay(this);
//            int index = users.indexOf(randomUser);
//            users.set(index,randomUser);
        }
//        return users;
    }

    public List<Shift> setNightShifts(Schedule newSchedule, List<User> users,int noMin,String value, Day nextDay){
        Random rand = new Random();
        List<Shift> freeDays = new ArrayList<>();
        String str = this.schedule.getWeekByDay(this);
//        String sd = str.split(";")[0];
//        String ed=str.split(";")[1];
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate startDate = LocalDate.parse(sd,formatter);
//        LocalDate endDate = LocalDate.parse(ed,formatter);
        while(getNoShifts(value)<noMin){
            List<User> takenUsers =   shifts.stream().distinct()
                    .map(Shift::getUser)
                    .toList();//.stream().filter(user -> newSchedule.getNoHoursUser(user,startDate,endDate)+12>40).toList();
//            List<User> validUsers = new ArrayList<>(users.stream().filter(item -> !takenUsers.contains(item) && newSchedule.getNoHoursUser(item, startDate, endDate) + 12 <= 24 && newSchedule.getNoHoursUser(item, startDate, endDate)==minValue).toList());
            List<User> validUsers = new ArrayList<>(users.stream().filter(item -> !takenUsers.contains(item) && newSchedule.getNoHoursMonth(item) + 12 <= newSchedule.findHighestMultiple(newSchedule.getNoWorkingDays()*8)).toList());
            int minValue = validUsers.stream()
                    .mapToInt(newSchedule::getNoHoursMonth)
                    .min()
                    .orElse(0);
            validUsers=validUsers.stream().filter(i->newSchedule.getNoHoursMonth(i)== minValue).toList();
            User randomUser = validUsers.get(rand.nextInt(validUsers.size()));

            Shift newShift = new Shift();
            newShift.setDay(this);
            newShift.setUser(randomUser);
            newShift.setType(value);
            Shift freeShift = new Shift();
            freeShift.setDay(nextDay);
            freeShift.setUser(randomUser);
            freeShift.setType(ShiftTypes.FREE.getValue());
            this.shifts.add(newShift);
            freeDays.add(freeShift);
            newSchedule.setSpecificDay(this);
//            List<Shift> userShifts = new ArrayList<>();
//            userShifts.add(newShift);
//            userShifts.add(freeShift);
//            randomUser.setShifts(userShifts);
//            int index = users.indexOf(randomUser);
//            users.set(index,randomUser);

        }
//        Map<String, Object> result = new HashMap<>();
//        result.put("freeDays", freeDays);
//        result.put("users", users);
        return freeDays;
    }

    public List<Shift> setNightShifts8H(Schedule newSchedule, List<User> users, int noMin, String value, Day nextDay){
        Random rand = new Random();
        List<Shift> freeDays = new ArrayList<>();
        String str = this.schedule.getWeekByDay(this);
        String sd = str.split(";")[0];
        String ed=str.split(";")[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(sd,formatter);
        LocalDate endDate = LocalDate.parse(ed,formatter);
        while(getNoShifts(value)<noMin){
            List<User> takenUsers =   shifts.stream().distinct()
                    .map(Shift::getUser)
                    .toList();//.stream().filter(user -> newSchedule.getNoHoursUser(user,startDate,endDate)+12>40).toList();
//            int minValue = users.stream()
//                    .mapToInt(item->newSchedule.getNoHoursUser(item,startDate,endDate))
//                    .min()
//                    .orElse(0);
//            List<User> validUsers = new ArrayList<>(users.stream().filter(item -> !takenUsers.contains(item) && newSchedule.getNoHoursUser(item, startDate, endDate) + 12 <= 24 && newSchedule.getNoHoursUser(item, startDate, endDate)==minValue).toList());
            List<User> validUsers = new ArrayList<>(users.stream().filter(item -> !takenUsers.contains(item) && newSchedule.getNoHoursUser8H(item, startDate, endDate) + 8 <= 40).toList());
            int minValue = validUsers.stream()
                    .mapToInt(item->newSchedule.getNoHoursUser8H(item,startDate,endDate))
                    .min()
                    .orElse(0);
            validUsers = validUsers.stream().filter(i->newSchedule.getNoHoursUser8H(i,startDate,endDate)== minValue).toList();

            User randomUser = validUsers.get(rand.nextInt(validUsers.size()));

            Shift newShift = new Shift();
            newShift.setDay(this);
            newShift.setUser(randomUser);
            newShift.setType(value);
            Shift freeShift = new Shift();
            freeShift.setDay(nextDay);
            freeShift.setUser(randomUser);
            freeShift.setType(ShiftTypes.FREE.getValue());
            this.shifts.add(newShift);
            freeDays.add(freeShift);
            newSchedule.setSpecificDay(this);
//            List<Shift> userShifts = new ArrayList<>();
//            userShifts.add(newShift);
//            userShifts.add(freeShift);
//            randomUser.setShifts(userShifts);
//            int index = users.indexOf(randomUser);
//            users.set(index,randomUser);

        }
//        Map<String, Object> result = new HashMap<>();
//        result.put("freeDays", freeDays);
//        result.put("users", users);
        return freeDays;
    }
    public void setNoShifts8H(Schedule newSchedule, List<User> users,int noMin,String value){

        Random rand = new Random();
        String str = this.schedule.getWeekByDay(this);
        String sd = str.split(";")[0];
        String ed=str.split(";")[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(sd,formatter);
        LocalDate endDate = LocalDate.parse(ed,formatter);
        while(getNoShifts(value)<noMin){
            List<User> takenUsers =   shifts.stream().distinct()
                    .map(Shift::getUser)
                    .toList();//.stream().filter(user -> newSchedule.getNoHoursUser(user,startDate,endDate)+12>40).toList();
            int minValue = users.stream()
                    .mapToInt(item->newSchedule.getNoHoursUser(item,startDate,endDate))
                    .min()
                    .orElse(0);
//            List<User> validUsers = new ArrayList<>(users.stream().filter(item -> !takenUsers.contains(item) && newSchedule.getNoHoursUser(item, startDate, endDate) + 12 <= 24 ).toList());
            List<User> validUsers = new ArrayList<>(users.stream().filter(item -> !takenUsers.contains(item) && newSchedule.getNoHoursUser8H(item, startDate, endDate) + 8 <= 40 ).toList());
            if(Objects.equals(value, ShiftTypes.MORNING.getValue())){
                minValue = validUsers.stream()
                        .mapToInt(item->newSchedule.getNoHoursUser8H(item,startDate,endDate))
                        .min()
                        .orElse(0);
                int finalMinValue = minValue;
                validUsers = validUsers.stream().filter(i->newSchedule.getNoHoursUser8H(i,startDate,endDate)== finalMinValue).toList();

            }

            if(validUsers.isEmpty()) {
                System.out.println("special "+this.toString());
                return;
            }
            User randomUser = validUsers.get(rand.nextInt(validUsers.size()));


            Shift newShift = new Shift();
            newShift.setDay(this);
            newShift.setUser(randomUser);
            newShift.setType(value);
            this.shifts.add(newShift);
            List<Shift> userShifts = new ArrayList<>();
            userShifts.add(newShift);
            randomUser.setShifts(userShifts);
            newSchedule.setSpecificDay(this);
//            int index = users.indexOf(randomUser);
//            users.set(index,randomUser);
        }
//        return users;
    }
    public void setFreeShifts(List<Shift> nShifts){
        for(Shift shift : nShifts){
            Shift newShift = new Shift();
            newShift.setDay(this);
            newShift.setUser(shift.getUser());
            newShift.setType(ShiftTypes.FREE.getValue());
            this.shifts.add(newShift);
        }
    }

    public void completeNoShifts(List<User> users,int noMin,String value){
        Random rand = new Random();

        while(getNoShifts(value)<noMin){
            List<User> takenUsers =   shifts.stream().filter(item-> Objects.equals(item.getType(), value)).distinct()
                    .map(Shift::getUser)
                    .toList();
            List<User> validUsers = users.stream().filter(item->!takenUsers.contains(item)).toList();
            User randomUser = validUsers.get(rand.nextInt(validUsers.size()));
            Shift newShift = new Shift();
            newShift.setDay(this);
            newShift.setUser(randomUser);
            newShift.setType(value);
            this.shifts.add(newShift);
        }
    }

    public List<User> setShiftsUsers(List<User> users){
        List<User> takenUsers =   shifts.stream().distinct()
                .map(Shift::getUser)
                .toList();
        for(User user:users){
            if(takenUsers.contains(user)){
                List<Shift> shiftList = new ArrayList<>(shifts.stream().filter(item -> item.getUser() == user)
                        .toList());
                shiftList.addAll(user.getShifts());
                user.setShifts(shiftList);
            }
        }
        return users;
    }

    public int getNoShifts(String value){
        return shifts.stream().filter(item -> Objects.equals(item.getType(), value)).toList().size();

    }

    public List<Shift> getTypeShifts(String type){
        return shifts.stream().filter(item -> Objects.equals(item.getType(), type)).toList();

    }

    public Day setShiftsIfNotLast(Day day2, List<Shift> shiftList){
        if(!this.checkIfLast()){
            day2.setShifts(shiftList);
        }
        return day2;
    }


    public boolean isDayFilled(int noMinM, int noMinN,String valueM, String valueN){
        return getNoShifts(valueM) >=noMinM && getNoShifts(valueN)>=noMinN;
    }

//    public boolean checkForUserShift(User user){
//
//    }

    public boolean checkIfLast(){
        LocalDate end = date.withDayOfMonth(date.getMonth().length(date.isLeapYear()));
        return end == date;
    }

    public void removeFreeShifts(){
        this.shifts= this.shifts.stream().filter(item-> !Objects.equals(item.getType(), ShiftTypes.FREE.getValue())).toList();
    }

    public void setFreeDays(List<Shift> shifts){
        for (Shift shift : shifts) {
            boolean exists = this.shifts.stream()
                    .anyMatch(event -> event.getUser().equals(shift.getUser()));

            if (!exists) {
                this.shifts.add(shift);
            }
        }
    }

    public Shift selectRandomShift(String value){
        Random random = new Random();
        List<Shift> shiftList = this.shifts.stream().filter(i-> Objects.equals(i.getType(), value)).toList();
        return shiftList.get(random.nextInt(shiftList.size()));

    }



    public void setSpecificTypeShift(Shift shift,String value){

        OptionalInt optionalIndex = IntStream.range(0, shifts.size())
                .filter(i -> Objects.equals(shifts.get(i).getType(), value))
                .findFirst();
        if(optionalIndex.isEmpty())
           return;
        shifts.set(optionalIndex.getAsInt(),shift);
    }

    public boolean isDateValid(){
        if(this.date==null)
            System.out.println("null");
        return this.date!=null;
    }
}
