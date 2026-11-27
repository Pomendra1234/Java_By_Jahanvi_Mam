package Practice;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

public class Test {
    public static void main(String[] args) {
        List<Employee> l;
        l = Arrays.asList(
                new Employee(101, "Amit", "IT", 50000),
                new Employee(102, "Rahul", "HR", 40000),
                new Employee(103, "Priya", "IT", 70000),
                new Employee(104, "Neha", "HR", 60000),
                new Employee(105, "Karan", "IT", 70000)
            );
        // 1. Group employees by department
        Stream<Employee> s=l.stream();
        Map<String,List<Employee>> group=s.collect(Collectors.groupingBy(Employee::getDepartment));
        group.forEach((String department,List<Employee> x)->{
            System.out.println(department+" "+x);
        });

        // 2. Highest paid employee in each department
        
    }
}
