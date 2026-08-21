package com.mastering.lambdas.misc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeGroupingByDepartment {
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    List<Employee> employees = List.of(
            new Employee("Alice Johnson", "IT", createSalary(new BigDecimal("72000.00"))),
            new Employee("Bob Smith", "IT", createSalary(new BigDecimal("68000.00"))),
            new Employee("Charlie Brown", "IT", createSalary(new BigDecimal("81000.00"))),
            new Employee("Diana Miller", "IT", createSalary(new BigDecimal("75500.00"))),
            new Employee("Ethan Davis", "IT", createSalary(new BigDecimal("92000.00"))),
            new Employee("Fiona Wilson", "IT", createSalary(new BigDecimal("63500.00"))),

            new Employee("George Moore", "HR", createSalary(new BigDecimal("58000.00"))),
            new Employee("Hannah Taylor", "HR", createSalary(new BigDecimal("62500.00"))),
            new Employee("Ian Anderson", "HR", createSalary(new BigDecimal("71000.00"))),
            new Employee("Julia Thomas", "HR", createSalary(new BigDecimal("66000.00"))),
            new Employee("Kevin Jackson", "HR", createSalary(new BigDecimal("74500.00"))),
            new Employee("Laura White", "HR", createSalary(new BigDecimal("60500.00"))),

            new Employee("Michael Harris", "Finance", createSalary(new BigDecimal("85000.00"))),
            new Employee("Natalie Martin", "Finance", createSalary(new BigDecimal("78000.00"))),
            new Employee("Oliver Thompson", "Finance", createSalary(new BigDecimal("95000.00"))),
            new Employee("Patricia Garcia", "Finance", createSalary(new BigDecimal("73500.00"))),
            new Employee("Quentin Martinez", "Finance", createSalary(new BigDecimal("88000.00"))),
            new Employee("Rachel Robinson", "Finance", createSalary(new BigDecimal("81500.00"))),

            new Employee("Samuel Clark", "Marketing", createSalary(new BigDecimal("62000.00"))),
            new Employee("Tina Rodriguez", "Marketing", createSalary(new BigDecimal("69500.00"))),
            new Employee("Victor Lewis", "Marketing", createSalary(new BigDecimal("76000.00"))),
            new Employee("Wendy Lee", "Marketing", createSalary(new BigDecimal("64500.00"))),
            new Employee("Xavier Walker", "Marketing", createSalary(new BigDecimal("73000.00"))),
            new Employee("Yvonne Hall", "Marketing", createSalary(new BigDecimal("67500.00"))),

            new Employee("Zachary Allen", "Sales", createSalary(new BigDecimal("59000.00"))),
            new Employee("Amelia Young", "Sales", createSalary(new BigDecimal("67000.00"))),
            new Employee("Benjamin King", "Sales", createSalary(new BigDecimal("72500.00"))),
            new Employee("Chloe Wright", "Sales", createSalary(new BigDecimal("78500.00"))),
            new Employee("Daniel Scott", "Sales", createSalary(new BigDecimal("83000.00"))),
            new Employee("Emma Green", "Sales", createSalary(new BigDecimal("70500.00")))
    );

    private BigDecimal createSalary(BigDecimal input) {
        return input.setScale(SCALE, ROUNDING_MODE);
    }

    record Employee(
            String name,
            String department,
            BigDecimal salary
    ) {}

    /**
     * Calculate top 3 highest-paid employees in each department,
     * sorted by salary desc
     *
     * @return A map of top 3 highest-paid employees in each department, sorted by salary
     */
    private Map<String, List<Employee>> calculateTop3HighestPaidEmplyeesInEachDepartment() {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::department,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                employee -> employee.stream()
                                        .sorted(Comparator.comparing(Employee::salary).reversed().thenComparing(Employee::name))
                                        .limit(3)
                                        .toList()
                        ))
                );
    }

    public static void main(String[] args) {
        new EmployeeGroupingByDepartment().calculateTop3HighestPaidEmplyeesInEachDepartment()
                .entrySet()
                .forEach(System.out::println);
    }
}
