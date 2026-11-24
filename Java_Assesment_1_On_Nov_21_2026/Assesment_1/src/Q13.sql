-- ================= TABLE BANAO =================
CREATE TABLE Employee (
    ID INT PRIMARY KEY,
    Name VARCHAR(50),
    Department VARCHAR(50),
    Salary INT
);

INSERT INTO Employee VALUES
(1, 'Amit',   'IT',    50000),
(2, 'Rahul',  'HR',    40000),
(3, 'Priya',  'IT',    80000),
(4, 'Neha',   'HR',    60000),
(5, 'Karan',  'IT',    80000),
(6, 'Simran', 'Sales', 45000),
(7, 'Mohit',  'Sales', 55000);

-- ================= 22. Second-highest distinct salary =================
SELECT MAX(Salary) AS SecondHighestSalary
FROM Employee
WHERE Salary < (SELECT MAX(Salary) FROM Employee);

-- Alternative
SELECT DISTINCT Salary
FROM Employee
ORDER BY Salary DESC
LIMIT 1 OFFSET 1;

-- ================= 23. Har department ka highest salary =================
SELECT Department, MAX(Salary) AS HighestSalary
FROM Employee
GROUP BY Department;

-- ================= 24. Department ki average se zyada kamane wale =================
SELECT e.ID, e.Name, e.Department, e.Salary
FROM Employee e
WHERE e.Salary > (
    SELECT AVG(Salary)
    FROM Employee
    WHERE Department = e.Department
);

-- ================= 25. Departments jinme 1 se zyada employee hain =================
SELECT Department, COUNT(*) AS EmployeeCount
FROM Employee
GROUP BY Department
HAVING COUNT(*) > 1;

-- ================= 26. Duplicate salary wale employees =================
SELECT *
FROM Employee
WHERE Salary IN (
    SELECT Salary
    FROM Employee
    GROUP BY Salary
    HAVING COUNT(*) > 1
);

-- ================= 27. Third-highest distinct salary =================
SELECT DISTINCT Salary AS ThirdHighestSalary
FROM Employee
ORDER BY Salary DESC
LIMIT 1 OFFSET 2;

-- Alternative (MySQL 8+)
SELECT Salary AS ThirdHighestSalary
FROM (
    SELECT Salary, DENSE_RANK() OVER (ORDER BY Salary DESC) AS rnk
    FROM Employee
) t
WHERE rnk = 3
LIMIT 1;

-- ================= 28. Company mein max salary wale employee(s) =================
SELECT *
FROM Employee
WHERE Salary = (SELECT MAX(Salary) FROM Employee);