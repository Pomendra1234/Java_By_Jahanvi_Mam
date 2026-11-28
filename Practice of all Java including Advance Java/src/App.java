import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
    new Employee(101, "Amit", "IT", 50000),
    new Employee(102, "Rahul", "HR", 40000),
    new Employee(103, "Priya", "IT", 70000),
    new Employee(104, "Neha", "HR", 60000),
    new Employee(105, "Karan", "IT", 70000)
);

    // 1. Collectors.toList()
    List<String> names = employees.stream()
    .map((Employee x)->{
        return x.getName().repeat(2);
    })
    .collect(Collectors.toList());

    // names.forEach(System.out::println);

    // 2. Collectors.toSet()
    Set<String> departments = employees.stream()
    .map(Employee::getDepartment)
    .collect(Collectors.toSet());

    // departments.forEach(System.out::println);

     // 3. Collectors.toMap()
    Map<Integer, String> employeeMap = employees.stream()
    .collect(Collectors.toMap(
        (Employee x)->{
            return x.getId();
        },
        (Employee y)->{
            return y.getName();
        }
    ));

employeeMap.forEach((Integer x,String y)->{
    System.out.println(x+" "+y);
});

    // 4. Collectors.groupingBy()
    // Department-wise employees group karna
    Map<String,List<Employee>> dept=employees.stream().collect(Collectors.groupingBy(Employee::getDepartment));
    dept.forEach((String depts,List<Employee> list)->{
        System.out.println(depts+"->"+list);
    });

    // 5. Collectors.joining()
    // String join=employees.stream().map(Employee::getName).collect(Collectors.joining(","));   //separator preffix and suffix
     String join=employees.stream().map(Employee::getName).collect(Collectors.joining(",","[","]")); 
    
    System.out.println(join);


    }
   
}

