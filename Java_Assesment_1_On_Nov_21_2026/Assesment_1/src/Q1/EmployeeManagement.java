package Q1;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class Employee {
    private int id;
    private String name;
    private String department;
    private double salary;

    public Employee(int id, String name, String department, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
}

public class EmployeeManagement {
    public static void main(String[] args) {
        List<Employee> l = new ArrayList<>();
        Employee e1 = new Employee(1, "Aman", "IT", 50000);
        Employee e2 = new Employee(1, "Nitesh", "finance", 50000);
        Employee e3 = new Employee(2, "shakti", "IT", 40000);
        Employee e4 = new Employee(2, "rajesh", "finance", 40000);
        Employee e5 = new Employee(2, "srinivas", "hr", 30000);
        Employee e6 = new Employee(3, "ejesh", "hr", 30000);
        Employee e7 = new Employee(3, "Javika", "ug", 60000);
        Employee e8 = new Employee(4, "Niresh", "ug", 60000);
        Employee e9 = new Employee(4, "dangi", "ir", 20000);
        Employee e10 = new Employee(4, "pikachu", "ir", 20000);
        Employee e11 = new Employee(5, "ben10", "fc", 45000);
        Employee e12 = new Employee(5, "dadu", "fc", 45000);
        Employee e13 = new Employee(6, "gangster", "ui", 35000);
        Employee e14 = new Employee(6, "baburao", "ui", 35000);
        Employee e15 = new Employee(6, "ultakhopdi", "ui", 35000);

        l.add(e1);
        l.add(e2);
        l.add(e3);
        l.add(e4);
        l.add(e5);
        l.add(e6);
        l.add(e7);
        l.add(e8);
        l.add(e9);
        l.add(e10);
        l.add(e11);
        l.add(e12);
        l.add(e13);
        l.add(e14);
        l.add(e15);

        System.out.println("===== Original List =====");
        for (Employee e : l) {
            System.out.println("id:" + e.getId() + " name : " + e.getName() + " depatment : " + e.getDepartment() + " salary : " + e.getSalary());
        }

        // ================= STEP 1: Duplicates find karo (Map se, ID ke basis par) =================
        Map<Integer, Employee> seen = new LinkedHashMap<>();
        List<Employee> duplicates = new ArrayList<>();
        for (Employee e : l) {
            if (seen.containsKey(e.getId())) {
                duplicates.add(e);
            } else {
                seen.put(e.getId(), e);
            }
        }

        // ================= STEP 2: Duplicate records display karo =================
        System.out.println("\n===== Duplicate Records =====");
        if (duplicates.isEmpty()) {
            System.out.println("Koi duplicate nahi mila.");
        } else {
            for (Employee e : duplicates) {
                System.out.println("id:" + e.getId() + " name : " + e.getName() + " depatment : " + e.getDepartment() + " salary : " + e.getSalary());
            }
        }

        // ================= STEP 3: Duplicates remove karo (insertion order maintain) =================
        List<Employee> unique = new ArrayList<>(seen.values());

        System.out.println("\n===== After Removing Duplicates =====");
        for (Employee e : unique) {
            System.out.println("id:" + e.getId() + " name : " + e.getName() + " depatment : " + e.getDepartment() + " salary : " + e.getSalary());
        }

        // ================= STEP 4: Second highest distinct salary =================
        List<Double> distinctSalaries = new ArrayList<>();
        for (Employee e : unique) {
            if (!distinctSalaries.contains(e.getSalary())) {
                distinctSalaries.add(e.getSalary());
            }
        }
        Collections.sort(distinctSalaries, Collections.reverseOrder());

        System.out.println("\n===== Second Highest Distinct Salary =====");
        if (distinctSalaries.size() >= 2) {
            System.out.println(distinctSalaries.get(1));
        } else {
            System.out.println("Second highest salary exist nahi karti");
        }

        // ================= STEP 5: Same salary wale employees (original list l par) =================
        Map<Double, List<Employee>> bySalary = new LinkedHashMap<>();
        for (Employee e : l) {
            if (!bySalary.containsKey(e.getSalary())) {
                bySalary.put(e.getSalary(), new ArrayList<>());
            }
            bySalary.get(e.getSalary()).add(e);
        }

        System.out.println("\n===== Employees With Same Salary =====");
        boolean found = false;
        for (List<Employee> group : bySalary.values()) {
            if (group.size() > 1) {
                found = true;
                System.out.println("Salary " + group.get(0).getSalary() + ":");
                for (Employee e : group) {
                    System.out.println("   id:" + e.getId() + " name : " + e.getName() + " depatment : " + e.getDepartment());
                }
            }
        }
        if (!found) {
            System.out.println("Koi employees same salary wale nahi mile.");
        }
    }
}