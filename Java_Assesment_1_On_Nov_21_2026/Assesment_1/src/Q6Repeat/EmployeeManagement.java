/*
Q6. Collections and Java 8 – Employee Analysis (15 Marks)
Use the following employee data to build a Java application. You may represent the records using an Employee class and a collection.
11.	101, Amit, IT, 50000
12.	102, Rahul, HR, 40000
13.	103, Priya, IT, 70000
14.	104, Neha, HR, 60000
15.	105, Karan, IT, 70000
Interview challenge: Using Collections and Java 8 features, group employees by department; find the highest-paid employee in each department; find the second-highest distinct salary; sort employees by salary and then by name; and calculate the average salary department-wise.

*/
package Q6Repeat;
import java.util.*;
import java.util.stream.Collectors;

class Employee {

    private int id;
    private String name;
    private String department;
    private double salary;

    public Employee(int id, String name,
                    String department, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return id + " " + name + " " +
               department + " " + salary;
    }
}

public class EmployeeManagement {

    public static void main(String[] args) {

        List<Employee> employees = Arrays.asList(

            new Employee(101, "Amit", "IT", 50000),
            new Employee(102, "Rahul", "HR", 40000),
            new Employee(103, "Priya", "IT", 70000),
            new Employee(104, "Neha", "HR", 60000),
            new Employee(105, "Karan", "IT", 70000)

        );

        // 1. Group employees by department

        System.out.println("1. Employees Grouped By Department");

        Map<String, List<Employee>> groupedEmployees =
            employees.stream()
                .collect(Collectors.groupingBy(
                    Employee::getDepartment
                ));

        groupedEmployees.forEach((department, empList) -> {
            System.out.println(department + " : " + empList);
        });


        // 2. Highest paid employee in each department

        System.out.println("\n2. Highest Paid Employee Department-wise");

        Map<String, Double> maxSalaryByDept =
            employees.stream()
                .collect(Collectors.groupingBy(
                    Employee::getDepartment,
                    Collectors.mapping(
                        Employee::getSalary,
                        Collectors.maxBy(Double::compare)
                    )
                ))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().get()
                ));

        employees.stream()
            .filter(e -> e.getSalary() == maxSalaryByDept.get(e.getDepartment()))
            .forEach(System.out::println);


        // 3. Find second-highest distinct salary

        System.out.println("\n3. Second Highest Distinct Salary");

        OptionalDouble secondHighest =
            employees.stream()
                .mapToDouble(Employee::getSalary)
                .distinct()
                .boxed()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .mapToDouble(Double::doubleValue)
                .findFirst();

        if (secondHighest.isPresent()) {
            System.out.println(secondHighest.getAsDouble());
        } else {
            System.out.println("Second highest salary not found");
        }


        // 4. Sort employees by salary and then by name

        System.out.println("\n4. Employees Sorted By Salary and Name");

        employees.stream()
            .sorted(
                Comparator.comparingDouble(Employee::getSalary)
                    .thenComparing(Employee::getName)
            )
            .forEach(System.out::println);


        // 5. Average salary department-wise

        System.out.println("\n5. Average Salary Department-wise");

        Map<String, Double> averageSalary =
            employees.stream()
                .collect(Collectors.groupingBy(
                    Employee::getDepartment,
                    Collectors.averagingDouble(
                        Employee::getSalary
                    )
                ));

        averageSalary.forEach((department, average) -> {
            System.out.println(
                department + " : " + average
            );
        });
    }
}
