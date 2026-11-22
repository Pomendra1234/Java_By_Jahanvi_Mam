
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
import java.util.stream.Stream;

public class Main {
    public static void group(String department,ArrayList<Employee> emps){
        Stream grouping=emps.stream();
        Stream grouping2=grouping.filter(
            (Object e)->{
                Employee e1=(Employee)e;
                return e1.department==department;
            }
        );
        grouping2.forEach(
            (Object e)->{
                Employee e1=(Employee)e;
                System.out.println(String.format("id %d name %s department %s salary %f",e1.id,e1.name,e1.department,e1.salary ));
            }
        );
    }
    public static void highestSalary(String department,ArrayList<Employee> emps){
        Stream grouping=emps.stream();
        Stream grouping2=grouping.filter(
            (Object e)->{
                Employee e1=(Employee)e;
                return e1.department==department;
            }
        );
        grouping2.forEach(
            (Object e)->{
                Employee e1=(Employee)e;
                System.out.println(String.format("id %d name %s department %s salary %f",e1.id,e1.name,e1.department,e1.salary ));
            }
        );
    }
    public static void main(String[] args) {
        Employee emp1=new Employee(101, "Amit", "IT", 50000);
        Employee emp2=new Employee(102, "Rahul", "HR", 40000);
        Employee emp3=new Employee(103, "Priya", "IT", 70000);
        Employee emp4=new Employee(104, "Neha", "HR", 60000);
        Employee emp5=new Employee(105, "Karan", "IT", 70000);
        
        ArrayList<Employee> emps=new ArrayList<>();
        emps.add(emp1);
        emps.add(emp2);
        emps.add(emp3);
        emps.add(emp4);
        emps.add(emp5);

        //  group employees by department;
        group("HR",emps);
        group("IT",emps);


        
    }
}
