
/*
Q6. Collections and Java 8 – Employee Analysis (15 Marks)
Use the following employee data to build a Java application. You may represent the records using an Employee class and a collection.
11.	101, Amit, IT, 50000
12.	102, Rahul, HR, 40000
13.	103, Priya, IT, 70000
14.	104, Neha, HR, 60000
15.	105, Karan, IT, 70000
Interview challenge:
 Using Collections and Java 8 features
, group employees by department; 
find the highest-paid employee in each department;
 find the second-highest distinct salary;
sort employees by salary and then by name;
and calculate the average salary department-wise.

*/
package Q6;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        List<Employee> list = new ArrayList<>();
        Employee e1 = new Employee(101, "Amit", "IT", 50000);
        Employee e2 = new Employee(102, "Rahul", "HR", 40000);
        Employee e3 = new Employee(103, "Priya", "IT", 70000);
        Employee e4 = new Employee(104, "Neha", "HR", 60000);
        Employee e5 = new Employee(105, "Karan", "IT", 70000);

        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);

        System.out.println("===== All Employees =====");
        list.forEach(e -> System.out.println(e));

        // ================= 1: Department ke hisaab se group karo =================
        Map<String, List<Employee>> byDept = list.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, TreeMap::new, Collectors.toList()));

        System.out.println("\n===== 1. Group by Department =====");
        byDept.forEach((dept, emps) -> {
            System.out.println(dept + ":");
            emps.forEach(e -> System.out.println("   " + e));
        });

        // ================= 2: Har department ka highest paid employee
        // =================
        Map<String, Optional<Employee>> highestPaid = list.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, TreeMap::new,
                        Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary))));

        System.out.println("\n===== 2. Highest Paid Employee (Department-wise) =====");
        highestPaid.forEach((dept, emp) -> System.out.println(dept + " -> " + emp.get()));

        // Tie handle karna: IT mein Priya aur Karan dono 70000 hain
        System.out.println("\n----- 2b. Tie ke saath (same max salary wale sab) -----");
        byDept.forEach((dept, emps) -> {
            double max = emps.stream().mapToDouble(Employee::getSalary).max().getAsDouble();
            System.out.println(dept + " (" + max + "):");
            emps.stream()
                    .filter(e -> e.getSalary() == max)
                    .forEach(e -> System.out.println("   " + e.getName()));
        });

        // ================= 3: Second highest distinct salary =================
        Optional<Double> secondHighest = list.stream()
                .map(Employee::getSalary)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .findFirst();

        System.out.println("\n===== 3. Second Highest Distinct Salary =====");
        System.out.println(secondHighest.isPresent() ? secondHighest.get() : "Second highest salary exist nahi karti");

                // ================= 4: Employees ko sort karo =================

        
        System.out.println("\n===== 4. Sort Employees by Salary and then by Name; =====");
        // 4a: Pehle sirf SALARY se sort (chhota se bada)
        List<Employee> bySalary = list.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary))
                .collect(Collectors.toList());

        System.out.println("\n===== 4a. Sort by Salary =====");
        bySalary.forEach(e -> System.out.println(e));

        // 4b: Phir alag se sirf NAME se sort (A-Z)
        List<Employee> byName = list.stream()
                .sorted(Comparator.comparing(Employee::getName))
                .collect(Collectors.toList());

        System.out.println("\n===== 4b. Sort by Name =====");
        byName.forEach(e -> System.out.println(e));

        // ================= 5: Department-wise average salary =================
        Map<String, Double> avgSalary = list.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, TreeMap::new,
                        Collectors.averagingDouble(Employee::getSalary)));

        System.out.println("\n===== 5. Average Salary (Department-wise) =====");
        avgSalary.forEach((dept, avg) -> System.out.println(dept + " -> " + String.format("%.2f", avg)));
    }
}
